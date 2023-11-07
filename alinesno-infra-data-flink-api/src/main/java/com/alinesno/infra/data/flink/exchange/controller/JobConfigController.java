package com.alinesno.infra.data.flink.exchange.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.alinesno.infra.common.core.constants.SpringInstanceScope;
import com.alinesno.infra.common.web.adapter.rest.BaseController;
import com.alinesno.infra.common.facade.pageable.DatatablesPageBean;
import com.alinesno.infra.common.facade.pageable.TableDataInfo;
import com.alinesno.infra.common.facade.response.AjaxResult;
import com.alinesno.infra.common.facade.response.ResultCodeEnum;
import com.alinesno.infra.data.flink.commom.constant.SystemConstant;
import com.alinesno.infra.data.flink.commom.enums.JobTypeEnum;
import com.alinesno.infra.data.flink.commom.model.CheckPointParam;
import com.alinesno.infra.data.flink.commom.sql.SqlFileParser;
import com.alinesno.infra.data.flink.entity.JobConfigEntity;
import com.alinesno.infra.data.flink.exchange.ao.JobConfigAO;
import com.alinesno.infra.data.flink.exchange.ao.JobServerAO;
import com.alinesno.infra.data.flink.exchange.common.FlinkConstants;
import com.alinesno.infra.data.flink.exchange.common.ResponseBean;
import com.alinesno.infra.data.flink.exchange.common.SystemConstants;
import com.alinesno.infra.data.flink.exchange.common.util.CliConfigUtil;
import com.alinesno.infra.data.flink.exchange.dto.JobConfigDTO;
import com.alinesno.infra.data.flink.exchange.dto.SavepointBackupDTO;
import com.alinesno.infra.data.flink.exchange.enums.*;
import com.alinesno.infra.data.flink.exchange.exceptions.BizException;
import com.alinesno.infra.data.flink.exchange.factory.JobServerAOFactory;
import com.alinesno.infra.data.flink.exchange.param.UpsertJobConfigParam;
import com.alinesno.infra.data.flink.exchange.vo.JobStatusStatVO;
import com.alinesno.infra.data.flink.exchange.vo.SavepointBackupVO;
import com.alinesno.infra.data.flink.exchange.vo.jobTypeRunCount;
import com.alinesno.infra.data.flink.service.IJobAlarmConfigService;
import com.alinesno.infra.data.flink.service.IJobConfigService;
import com.alinesno.infra.data.flink.service.ISavepointBackupService;
import com.alinesno.infra.data.flink.service.ISystemConfigService;
import com.alinesno.infra.data.flink.validate.validation.SqlValidation;
import io.swagger.annotations.Api;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang3.RandomStringUtils;
import org.quartz.CronExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 处理与JobConfig相关的请求的Controller。
 * 继承自BaseController类并实现IJobConfigService接口。
 *
 * @version 1.0.0
 * @author luoxiaodong
 */
@Api(tags = "JobConfig")
@RestController
@Scope(SpringInstanceScope.PROTOTYPE)
@RequestMapping("/api/infra/data/flink/jobConfig")
public class JobConfigController extends BaseController<JobConfigEntity, IJobConfigService> {

    // 日志记录
    private static final Logger log = LoggerFactory.getLogger(JobConfigController.class);

    @Autowired
    private IJobConfigService service;

    @Autowired
    private ISystemConfigService systemConfigService;

    @Autowired
    private IJobAlarmConfigService jobAlarmConfigService;

    @Autowired
    private JobConfigAO jobConfigAO;

    @Autowired
    private ISavepointBackupService savepointBackupService;

    /**
     * 获取JobConfig的DataTables数据。
     *
     * @param request HttpServletRequest对象。
     * @param model Model对象。
     * @param page DatatablesPageBean对象。
     * @return 包含DataTables数据的TableDataInfo对象。
     */
    @ResponseBody
    @PostMapping("/datatables")
    public TableDataInfo datatables(HttpServletRequest request, Model model, DatatablesPageBean page) {
        log.debug("page = {}", ToStringBuilder.reflectionToString(page));

        //获取到数据后进行加工
        TableDataInfo tableDataInfo = this.toPage(model, this.getFeign(), page);
        List<JobConfigDTO> jobConfigDTO = JobConfigDTO.toListDTO(  (List<JobConfigEntity>)tableDataInfo.getRows() );
        for (JobConfigDTO configDTO : jobConfigDTO) {
            completeJObConfigDTO(configDTO) ;
        }
        tableDataInfo.setRows(jobConfigDTO);
        return tableDataInfo;


    }

    @Override
    public IJobConfigService getFeign() {
        return this.service;
    }

    @RequestMapping(value = "/deleteByID", method = {RequestMethod.DELETE})
    public AjaxResult deleteByID(HttpServletRequest request, Long id) {
        //通过ID删除，不需要操作员信息
        ResponseBean deleteRes = service.deleteJobConfigById(id);
        if ( deleteRes.getCode() != 200 ) {
            return AjaxResult.error(deleteRes.getMessage()) ;

        }

        return AjaxResult.success();
    }

    @RequestMapping(value = "/addConfig", method = {RequestMethod.POST})
    public AjaxResult addConfig(HttpServletRequest request,@RequestBody UpsertJobConfigParam upsertJobConfigParam) {

        AjaxResult restResult = checkUpsertJobConfigParam(upsertJobConfigParam);
        if (restResult != null) {
            return restResult;
        }
        //操作员，默认设置为0，后续处理
        Long operatorId = 0L  ;
//        // 设置用户
//        ManagerAccountEntity account = CurrentAccountJwt.get() ; // CurrentAccountSession.get(request);
//        if ( account != null ) {
//            operatorId = account.getId() ;
//
//        }
//
        upsertJobConfigParam.setOperatorId(operatorId);

        ResponseBean responseBean = jobConfigAO.addJobConfig(UpsertJobConfigParam.toDTO(upsertJobConfigParam));

        if ( responseBean.getCode() != 200 ) {
            return AjaxResult.error(responseBean.getMessage()) ;

        }else{
            return AjaxResult.success() ;
        }


    }


    //任务管理私有方法
    private String getAlarmTypes(List<AlarmTypeEnum> list) {
        if (list == null || list.size() == 0) {
            return null;
        }
        String result = "";
        for (AlarmTypeEnum alarmTypeEnum : list) {
            result += (result.length() > 0 ? "," : "") + alarmTypeEnum.getCode();
        }
        return result;
    }

    private String readTextFile(String fileName) {
        try (InputStream fin = new FileInputStream(fileName);) {
            byte[] buffer = new byte[fin.available()];
            fin.read(buffer);
            fin.close();
            String result = new String(buffer, SystemConstants.CODE_UTF_8);
            return result;
        } catch (Exception e) {
            log.error("读取文件[" + fileName + "]失败！", e);
        }
        return null;
    }

    //CHECKSTYLE:OFF
    private AjaxResult checkUpsertJobConfigParam(UpsertJobConfigParam upsertJobConfigParam) {
        if (upsertJobConfigParam == null) {
            return AjaxResult.error("参数不能空");
        }
        if (StringUtils.isEmpty(upsertJobConfigParam.getJobName())) {
            return AjaxResult.error("任务名称不能空");
        }
        if (upsertJobConfigParam.getJobName().length() > 50) {
            return AjaxResult.error("任务名称不能超过50个字符");
        }
        if (!upsertJobConfigParam.getJobName().matches("[0-9A-Za-z_]*")) {
            return AjaxResult.error("任务名称仅能含数字,字母和下划线");
        }

        // jar需要校验参数
        if (JobTypeEnum.JAR.equals(upsertJobConfigParam.getJobType())) {

            if (StringUtils.isEmpty(upsertJobConfigParam.getCustomMainClass())) {
                return AjaxResult.error("主类不能为空");
            }

            if (StringUtils.isEmpty(upsertJobConfigParam.getCustomJarUrl())) {
                return AjaxResult.error("主类jar的不能为空");
            }
        }
        // sql配置需要校验的参数JobType=null是兼容之前配置
        if (JobTypeEnum.SQL_STREAMING.equals(upsertJobConfigParam.getJobType())
                || upsertJobConfigParam.getJobType() == null
                || JobTypeEnum.SQL_STREAMING.getCode() == upsertJobConfigParam.getJobType().intValue()) {
            if (StringUtils.isEmpty(upsertJobConfigParam.getFlinkSql())) {
                return AjaxResult.error("sql语句不能为空");
            }
            if (StringUtils.isNotEmpty(upsertJobConfigParam.getExtJarPath())) {
                String[] urls = upsertJobConfigParam.getExtJarPath().split(SystemConstant.LINE_FEED);
                for (String url : urls) {
                    if (StringUtils.isEmpty(url)) {
                        continue;
                    }
                }
            }
        }
        if (JobTypeEnum.SQL_BATCH.getCode() == upsertJobConfigParam.getJobType() && StringUtils
                .isNotEmpty(upsertJobConfigParam.getCron())) {
            if (!CronExpression.isValidExpression(upsertJobConfigParam.getCron())) {
                return AjaxResult.error("cron表达式不准确");
            }
        }

        if (StringUtils.isNotEmpty(upsertJobConfigParam.getFlinkCheckpointConfig())) {
            CheckPointParam checkPointParam = CliConfigUtil
                    .checkFlinkCheckPoint(upsertJobConfigParam.getFlinkCheckpointConfig());
            ResponseBean checkPointResult = this.checkPointParam(checkPointParam);
            if (checkPointResult != null && checkPointResult.getCode() != 200 ) {
                return AjaxResult.error(checkPointResult.getMessage());
            }

        }

        if (DeployModeEnum.YARN_PER.name().equals(upsertJobConfigParam.getDeployMode()) ||
                DeployModeEnum.YARN_APPLICATION.name().equals(upsertJobConfigParam.getDeployMode())) {
            if (StringUtils.isEmpty(upsertJobConfigParam.getFlinkRunConfig())) {
                return AjaxResult.error("flink运行配置不能为空");
            }
            if (upsertJobConfigParam.getFlinkRunConfig().contains("-Dyarn.application.name=")) {
                return AjaxResult.error("请不要配置-Dyarn.application.name= 参数 ");
            }
        }

        return null;
    }
//CHECKSTYLE:ON

    /**
     * 获取JobServerAO
     *
     * @author zhp
     * @since 1.0.0
     * @time 11:19
     */
    private JobServerAO getJobServerAO(Long id) {
        JobConfigDTO jobConfigDTO = service.getJobConfigById(id);
        if (jobConfigDTO == null) {
            throw new BizException(SysErrorEnum.JOB_CONFIG_JOB_IS_NOT_EXIST);
        }
        return JobServerAOFactory.getJobServerAO(jobConfigDTO.getDeployModeEnum());
    }

    private ResponseBean checkPointParam(CheckPointParam checkPointParam) {
        ResponseBean responseResult = new ResponseBean();
        if (checkPointParam == null) {
            responseResult.setCode(ResultCodeEnum.SUCCESS);
            return responseResult;
        }
        if (StringUtils.isNotEmpty(checkPointParam.getCheckpointingMode())) {
            if (!(FlinkConstants.EXACTLY_ONCE.equalsIgnoreCase(checkPointParam.getCheckpointingMode())
                    || FlinkConstants.AT_LEAST_ONCE.equalsIgnoreCase(checkPointParam.getCheckpointingMode()))) {

                responseResult.setCode(ResultCodeEnum.FAIL);
                responseResult.setMessage("checkpointingMode 参数必须是  AT_LEAST_ONCE 或者 EXACTLY_ONCE");
                return responseResult;
            }
        }
        if (StringUtils.isNotEmpty(checkPointParam.getExternalizedCheckpointCleanup())) {
            if (!(FlinkConstants.DELETE_ON_CANCELLATION
                    .equalsIgnoreCase(checkPointParam.getExternalizedCheckpointCleanup())
                    || FlinkConstants.RETAIN_ON_CANCELLATION .equalsIgnoreCase(checkPointParam.getExternalizedCheckpointCleanup()))) {

                responseResult.setCode(ResultCodeEnum.FAIL);
                responseResult.setMessage("externalizedCheckpointCleanup 参数必须是DELETE_ON_CANCELLATION 或者 RETAIN_ON_CANCELLATION");
                return responseResult;

            }
        }
        responseResult.setCode(ResultCodeEnum.SUCCESS);
        return responseResult;
    }


    /**
     * 补充字段信息
     *
     * @author wxj
     * @date 2021年12月21日 下午5:01:47
     * @version V1.0
     */
    private void completeJObConfigDTO(JobConfigDTO jobConfigDTO) {
//        Map<DeployModeEnum, String> domainKey = new HashMap<>();
//        domainKey.put(DeployModeEnum.YARN_PER,
//                systemConfigService.getSystemConfigByKey(SysConfigEnum.YARN_RM_HTTP_ADDRESS.getKey()));
//        domainKey.put(DeployModeEnum.YARN_APPLICATION,
//                systemConfigService.getSystemConfigByKey(SysConfigEnum.YARN_RM_HTTP_ADDRESS.getKey()));
//        domainKey.put(DeployModeEnum.LOCAL,
//                systemConfigService.getSystemConfigByKey(SysConfigEnum.FLINK_REST_HTTP_ADDRESS.getKey()));
//        domainKey.put(DeployModeEnum.STANDALONE, systemConfigService
//                .getSystemConfigByKey(SysConfigEnum.FLINK_REST_HA_HTTP_ADDRESS.getKey()));
        // 补充FlinkRunUrl字段
//        String domain = domainKey.get(jobConfigDTO.getDeployModeEnum());
//        if (StringUtils.isNotEmpty(domain)) {
//            if ((DeployModeEnum.YARN_PER.equals(jobConfigDTO.getDeployModeEnum())
//                    || DeployModeEnum.YARN_APPLICATION.equals(jobConfigDTO.getDeployModeEnum()))
//                    && !StringUtils
//                    .isEmpty(jobConfigDTO.getJobId())) {
//                jobConfigDTO.setFlinkRunUrl(HttpUtil.buildUrl(domain,
//                        FlinkYarnRestUriConstants.getUriOverviewForYarn(jobConfigDTO.getJobId())));
//            }
//            if (DeployModeEnum.LOCAL.equals(jobConfigDTO.getDeployModeEnum()) && !StringUtils
//                    .isEmpty(jobConfigDTO.getJobId())) {
//                jobConfigDTO.setFlinkRunUrl(domain + String
//                        .format(FlinkYarnRestUriConstants.URI_YARN_JOB_OVERVIEW, jobConfigDTO.getJobId()));
//            }
//            if (DeployModeEnum.STANDALONE.equals(jobConfigDTO.getDeployModeEnum()) && !StringUtils
//                    .isEmpty(jobConfigDTO.getJobId())) {
//                String[] urls = domain.split(SystemConstant.SEMICOLON);
//                for (String url : urls) {
//                    if (HttpServiceCheckerUtil.checkUrlConnect(url)) {
//                        jobConfigDTO.setFlinkRunUrl(url.trim() + String
//                                .format(FlinkYarnRestUriConstants.URI_YARN_JOB_OVERVIEW, jobConfigDTO.getJobId()));
//                        break;
//                    }
//                }
//            }
//        }
        // 补充AlarmStrs字段
        List<AlarmTypeEnum> list = jobAlarmConfigService.findByJobId(jobConfigDTO.getId());
        if (CollectionUtil.isNotEmpty(list)) {
            List<Integer> alarmTypes = new ArrayList<Integer>();
            StringBuilder str = new StringBuilder("[");
            for (AlarmTypeEnum alarmTypeEnum : list) {
                alarmTypes.add(alarmTypeEnum.getCode());
                if (str.length() > 1) {
                    str.append(" ");
                }
                switch (alarmTypeEnum) {
                    case DINGDING:
                        str.append("钉钉");
                        break;
                    case CALLBACK_URL:
                        str.append("回调");
                        break;
                    case AUTO_START_JOB:
                        str.append("自动重启");
                        break;
                    default:
                }
            }
            str.append("]");
            jobConfigDTO.setAlarmStrs(str.toString());
            jobConfigDTO.setAlarmTypes(alarmTypes);
            jobConfigDTO.setAlarmTypeEnumList(list);
        }
    }

    @RequestMapping(value = "/open", method = {RequestMethod.POST} )
    public AjaxResult open(HttpServletRequest request, Long id) {
        Long operatorId = 0L ;
//        // 设置用户
//        ManagerAccountEntity account = CurrentAccountJwt.get() ; // CurrentAccountSession.get(request);
//        if ( account != null ) {
//            operatorId = account.getId() ;
//
//        }

        ResponseBean openResult = this.getJobServerAO(id).open(id, operatorId);
        if ( openResult.getCode() != 200 ) {
            log.warn("开始失败 id={},错误信息:{}", id, openResult.getMessage() );
            return AjaxResult.error( String.format("开始失败 id:%s,错误信息:%s", id, openResult.getMessage())  );
        } else{
            return AjaxResult.success();
        }

    }

    @RequestMapping(value = "/close", method = {RequestMethod.POST} )
    public AjaxResult close(HttpServletRequest request, Long id) {

        Long operatorId = 0L ;
//        // 设置用户
//        ManagerAccountEntity account = CurrentAccountJwt.get() ;
//        if ( account != null ) {
//            operatorId = account.getId() ;
//
//        }

        ResponseBean closeResult = this.getJobServerAO(id).close(id, operatorId);
        if ( closeResult.getCode() != 200 ) {
            return AjaxResult.error(closeResult.getMessage()) ;
        }

        return AjaxResult.success() ;

    }


    @RequestMapping(value = "/editConfig", method = {RequestMethod.POST})
    public AjaxResult editConfig(@RequestBody UpsertJobConfigParam upsertJobConfigParam) {
        AjaxResult checkResult = checkUpsertJobConfigParam(upsertJobConfigParam);
        if (checkResult != null) {
            return checkResult;
        }
        JobConfigDTO jobConfigDTO = service.getJobConfigById(upsertJobConfigParam.getId());
        if (jobConfigDTO == null) {
            return AjaxResult.error("数据不存在");
        }

        completeJObConfigDTO(jobConfigDTO);

        if (YN.getYNByValue(jobConfigDTO.getIsOpen()).getCode()) {
            return AjaxResult.error(SysErrorEnum.JOB_CONFIG_JOB_IS_OPEN.getErrorMsg());
        }

        Long operatorId = 0L ;
//        // 设置用户
//        ManagerAccountEntity account = CurrentAccountJwt.get() ;
//        if ( account != null ) {
//            operatorId = account.getId() ;
//
//        }

        upsertJobConfigParam.setOperatorId(operatorId);

        JobConfigDTO jobConfigNew = UpsertJobConfigParam.toDTO(upsertJobConfigParam);

        ResponseBean updateResult = jobConfigAO.updateJobConfigById(jobConfigNew);
        if ( updateResult.getCode() != 200 ) {
            return AjaxResult.error(updateResult.getMessage())  ;
        }

        return  AjaxResult.success() ;
    }

    @RequestMapping(value = "/start", method = {RequestMethod.POST})
    public AjaxResult start(HttpServletRequest request,Long id, Long savepointId) {
        Long operatorId = 0L ;
//        // 设置用户
//        ManagerAccountEntity account = CurrentAccountJwt.get() ;
//        if ( account != null ) {
//            operatorId = account.getId() ;
//
//        }

        try {
            this.getJobServerAO(id).start(id, savepointId, String.valueOf(operatorId));
        } catch (BizException e) {
            log.error("启动失败 id={}", id, e);
            return AjaxResult.error(e.getCode() + " " + e.getErrorMsg() ) ;
        } catch (Exception e) {
            log.error("启动失败 id={}", id, e);
            return AjaxResult.error(SysErrorEnum.START_JOB_FAIL.getErrorMsg());
        }
        return AjaxResult.success();

    }


    @RequestMapping(value = "/stop", method = {RequestMethod.POST})
    public AjaxResult stop(HttpServletRequest request,Long id) {

        Long operatorId = 0L ;
//        // 设置用户
//        ManagerAccountEntity account = CurrentAccountJwt.get() ;
//        if ( account != null ) {
//            operatorId = account.getId() ;
//
//        }

        try {
            this.getJobServerAO(id).stop(id ,operatorId);
        } catch (BizException e) {
            log.warn("停止失败 id={}", id, e);
            return AjaxResult.error(e.getCode() + " " + e.getErrorMsg() ) ;
        } catch (Exception e) {
            log.error("停止失败 id={}", id, e);
            return AjaxResult.error(SysErrorEnum.STOP_JOB_FAIL.getCode());
        }
        return AjaxResult.success();
    }

    @RequestMapping("/checkfSql")
    public AjaxResult checkfSql(String flinkSql) {
        if (StringUtils.isEmpty(flinkSql)) {
            return AjaxResult.error("flinkSql 参数不能为空");
        }

        try {
            List<String> listSql = SqlValidation.toSqlList(flinkSql);
            if (CollectionUtils.isEmpty(listSql)) {
                return AjaxResult.error("没有检测到有效sql语句,是否缺少了 ; 分隔符");
            }

            SqlValidation.explainStmt(SqlFileParser.parserSql(listSql));
        } catch (Exception e) {
            log.warn("校验失败flinkSql={}   errorMessage= {} ", flinkSql, e.getMessage());
            return AjaxResult.error(e.getMessage());
        }

        return AjaxResult.success();
    }

    @RequestMapping(value = "/copyConfig", method = {RequestMethod.POST})
    public AjaxResult copyConfig(HttpServletRequest request,Long id) {
        try {
            JobConfigDTO jobConfigDTO = service.getJobConfigById(id);
            if (jobConfigDTO == null) {
                return AjaxResult.error("原始拷贝数据不存在");
            }
            /*
             * copy job conf 默认将id去除
             * 默认在任务名称后面copy_随机字符_${jobConfigDTO.getJobName()}字符 状态默认重置为停止 开启配置
             * isOpen 0
             */
            jobConfigDTO.setId(null);
            jobConfigDTO.setJobName(
                    String
                            .format("copy_%s_%s", StringUtils.lowerCase(RandomStringUtils.randomAlphanumeric(4)),
                                    jobConfigDTO.getJobName()));
            jobConfigDTO.setStatus(JobConfigStatus.STOP);
            jobConfigDTO.setIsOpen(YN.N.getValue());
            jobConfigDTO.setJobId(null);
            jobConfigDTO.setLastRunLogId(null);
            jobConfigDTO.setVersion(0L);
            jobConfigDTO.setLastStartTime(null);
            jobConfigDTO.setLastRunLogId(null);

            jobConfigAO.addJobConfig(jobConfigDTO);

        } catch (BizException biz) {
            log.warn("copyJobConfigById is error ", biz);
            return AjaxResult.error(biz.getErrorMsg());
        } catch (Exception e) {
            log.error("copyJobConfigById is error", e);
            return AjaxResult.error(e.getMessage());
        }
        return AjaxResult.success();
    }

    /**
     * 查询每个任务类型的任务条数
     */
    @RequestMapping(value = "/jobTypeCnt", method = {RequestMethod.POST})
    public AjaxResult jobTypeCnt(HttpServletRequest request) {
        Long operatorId = 0L ;
//
//        // 设置用户
//        ManagerAccountEntity account = CurrentAccountJwt.get() ;
//        if (account != null) {
//            operatorId = account.getId() ;
//        }

        List<jobTypeRunCount> jobTypeCounts = service.jobTypeCnt(operatorId);

        return AjaxResult.success(jobTypeCounts);
    }


    /**
     * 自动巡检统计
     */
    @GetMapping("/autoInspectionStat")
    public AjaxResult getAutoInspectionStat(HttpServletRequest request) {
//        String operatorId = null ;
//
//        // 设置用户
//        ManagerAccountEntity account = CurrentAccountJwt.get() ;
//        if (account != null) {
//            operatorId = account.getId() ;
//        }

        AjaxResult result = AjaxResult.success() ;
        result.put("requestFailCnt", 3) ;
        result.put("requestSucCnt", 4) ;
        result.put("requestCnt", 7) ;

        return result;
    }

    /**
     * 作业执行情况
     */
    @GetMapping("/inspectionCount")
    public AjaxResult getInspectionCountStat(HttpServletRequest request) {
//        String operatorId = null ;
//
//        // 设置用户
//        ManagerAccountEntity account = CurrentAccountJwt.get() ;
//        if (account != null) {
//            operatorId = account.getId() ;
//        }

        AjaxResult result = AjaxResult.success() ;
        result.put("clearData", 20) ;
        result.put("healthCheck", 25) ;
        result.put("backup", 30) ;
        result.put("security", 31) ;
        result.put("cicd", 40) ;
        return result;
    }

    /**
     * 统计作业的状态
     *
     * @return
     */
    @GetMapping("/JobStatusStat")
    public AjaxResult getJobStatusStat(HttpServletRequest request) {

        Long operatorId = 0L ;
//
//        // 设置用户
//        ManagerAccountEntity account = CurrentAccountJwt.get() ;
//        if (account != null) {
//            operatorId = account.getId() ;
//
//        }
        List<JobStatusStatVO> jobStatusStatVO = service.JobStatusStat(operatorId);

        return AjaxResult.success(jobStatusStatVO);
    }

    @PostMapping("/savepoint")
    public AjaxResult savepoint(Long id) {
        try {
            this.getJobServerAO(id).savepoint(id);
        } catch (BizException e) {
            log.warn("savepoint is error {}", e , id);
            return AjaxResult.error(e.getErrorMsg() , e.getCode());
        } catch (Exception e) {
            log.error("savepoint is error {}", e, id);
            return AjaxResult.error(SysErrorEnum.SAVEPOINT_JOB_FAIL.getErrorMsg());
        }
        return AjaxResult.success();
    }

    /**
     * 获取SavePoint保存历史信息
     *
     * @param jobConfigId   任务编号
     * @param jobConfigId
     * @return
     * @author wxj
     * @since 1.0.0
     * @version V1.0
     */
    @PostMapping("/querySavePointList10")
    public AjaxResult querySavePointList10(Long jobConfigId) {
        List<SavepointBackupDTO> savepointBackupDTOList = savepointBackupService
                .lasterHistory10(jobConfigId);
        ModelMap modelMap = new ModelMap();
        modelMap.put("data", SavepointBackupVO.toDTOList(savepointBackupDTOList));
        JobConfigDTO jobConfigDTO = service.getJobConfigById(jobConfigId);
        if (jobConfigDTO != null && JobConfigStatus.RUN.getCode().intValue() != jobConfigDTO.getStatus()
                .getCode().intValue()
                && YN.getYNByValue(jobConfigDTO.getIsOpen()).getCode()) {
            modelMap.put("enable", true);
        } else {
            modelMap.put("enable", false);
        }
        modelMap.put("taskId", jobConfigId);
        return AjaxResult.success(modelMap);
    }

    @PostMapping("/addSavepoint")
    public AjaxResult addSavepoint(Long jobConfigId, String savepointPath) {
        Long operatorId = 0L ;
        try {
            if (StringUtils.isBlank(savepointPath)) {
                throw new BizException("SavePoint地址不能为空！");
            }
            JobConfigDTO jobdto = service.getJobConfigById(jobConfigId);
            if (jobdto == null) {
                throw new BizException("查找不到编号为[" + jobConfigId + "]的任务！");
            }

//            // 设置用户
//            ManagerAccountEntity account = CurrentAccountJwt.get() ; // CurrentAccountSession.get(request);
//            if (account != null) {
//                operatorId = account.getId() ;
//            }

            savepointPath = savepointPath.trim();
            List<SavepointBackupDTO> savepointBackupDTOList = savepointBackupService
                    .lasterHistory10(jobConfigId);
            for (SavepointBackupDTO savepointBackupDTO : savepointBackupDTOList) {
                if (savepointPath.equals(savepointBackupDTO.getSavepointPath())) {
                    throw new BizException("SavePoint地址[" + savepointPath + "]已经存在！");
                }
            }
            savepointBackupService.insertSavepoint(jobConfigId, savepointPath, new Date(), operatorId);
        } catch (BizException e) {
            log.error("addSavepoint is error jobConfigId={},savepointPath={}", jobConfigId, savepointPath,
                    e);
            return AjaxResult.error(e.getCode(), e.getErrorMsg());
        } catch (Exception e) {
            log.error("addSavepoint  error jobConfigId={},savepointPath={}", jobConfigId, savepointPath,
                    e);
            return AjaxResult.error(SysErrorEnum.ADD_SAVEPOINT_ERROR.getErrorMsg());
        }
        return AjaxResult.success();
    }

}
