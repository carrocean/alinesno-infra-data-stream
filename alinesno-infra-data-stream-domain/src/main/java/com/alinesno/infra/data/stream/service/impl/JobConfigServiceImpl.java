package com.alinesno.infra.data.stream.service.impl;

import com.alinesno.infra.common.core.service.impl.IBaseServiceImpl;
import com.alinesno.infra.common.facade.response.ResultCodeEnum;
import com.alinesno.infra.data.stream.entity.BatchJob;
import com.alinesno.infra.data.stream.entity.JobConfigEntity;
import com.alinesno.infra.data.stream.exchange.common.ResponseBean;
import com.alinesno.infra.data.stream.exchange.dto.JobConfigDTO;
import com.alinesno.infra.data.stream.exchange.dto.JobConfigHistoryDTO;
import com.alinesno.infra.data.stream.exchange.enums.*;
import com.alinesno.infra.data.stream.exchange.exceptions.BizException;
import com.alinesno.infra.data.stream.exchange.vo.JobStatusStatVO;
import com.alinesno.infra.data.stream.exchange.vo.jobTypeRunCount;
import com.alinesno.infra.data.stream.mapper.JobConfigMapper;
import com.alinesno.infra.data.stream.service.*;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 作业配置Service业务层处理
 *
 * @version 1.0.0
 * @author luoxiaodong
 */
@Service
public class JobConfigServiceImpl extends IBaseServiceImpl<JobConfigEntity, JobConfigMapper> implements IJobConfigService {
    //日志记录
    @SuppressWarnings("unused")
    private static final Logger log = LoggerFactory.getLogger(JobConfigServiceImpl.class);
    @Autowired
    private JobConfigMapper jobConfigMapper;

    @Autowired
    private IJobRunLogService jobRunLogService;

    @Autowired
    private IJobAlarmConfigService jobAlarmConfigService;

    @Autowired
    private ISystemConfigService systemConfigService;

    @Autowired
    private IJobConfigHistoryService jobConfigHistoryService;

    @Autowired
    private ISavepointBackupService savepointBackupService ;

    @Autowired
    private IAlartLogService alartLogService;

    @Override
    public ResponseBean addJobConfig(JobConfigDTO jobConfigDTO) {
        ResponseBean responseResult = new ResponseBean();
        if (jobConfigDTO == null) {
            responseResult.setCode(ResultCodeEnum.FAIL);
            responseResult.setMessage("参数jobConfigDTO为null!");
            return responseResult;
        }

        ResponseBean checkJobNameResult = this.checkJobName(jobConfigDTO.getJobName(), jobConfigDTO.getId());
        if ( checkJobNameResult.getCode() != 200 ) {
            return  checkJobNameResult ;
        }

        ResponseBean checkSystemConfigResult = this.checkSystemConfig(jobConfigDTO.getDeployModeEnum());

        if ( checkSystemConfigResult.getCode() != 200 ) {
            return  checkSystemConfigResult ;
        }


        JobConfigEntity jobConfig = JobConfigDTO.toEntity(jobConfigDTO);
        jobConfig.setAddTime( new Date());
        this.saveOrUpdate(jobConfig);
        this.insertJobConfigHistory(jobConfig.getId());
        Map<String, Long> dataMap = new HashMap<String, Long>();
        dataMap.put("jobID", jobConfig.getId());
        responseResult.setCode(ResultCodeEnum.SUCCESS);
        responseResult.setData(dataMap);

        return responseResult ;
    }

    @Override
    public void updateJobConfigById(JobConfigDTO jobConfigDTO) {

        if (jobConfigDTO == null || jobConfigDTO.getId() == null) {
            throw new BizException(SysErrorEnum.JOB_CONFIG_PARAM_IS_NULL);
        }
        JobConfigEntity jobConfig = jobConfigMapper.selectByPrimaryKey(jobConfigDTO.getId());
        if (jobConfig == null) {
            throw new BizException(SysErrorEnum.JOB_CONFIG_JOB_IS_NOT_EXIST);
        }
        if (jobConfigDTO.getDeployModeEnum() == null) {
            this.checkSystemConfig(DeployModeEnum.valueOf(jobConfig.getDeployMode()));
        } else {
            this.checkSystemConfig(jobConfigDTO.getDeployModeEnum());
        }

        if (StringUtils.isNotEmpty(jobConfigDTO.getJobName())) {
            this.checkJobName(jobConfigDTO.getJobName(), jobConfigDTO.getId());
        }
        JobConfigEntity jobConfigUpdate = JobConfigDTO.toEntity(jobConfigDTO);
        jobConfigUpdate.setUpdateTime( new Date());

        jobConfigMapper.updateByPrimaryKeySelective(jobConfigUpdate);

        // this.insertJobConfigHistory(jobConfigDTO.getId());
    }

    @Override
    public ResponseBean updateJobConfigByIdWithWriteHistory(JobConfigDTO jobConfigDTO) {
        ResponseBean responseResult = new ResponseBean();
        if (jobConfigDTO == null || jobConfigDTO.getId() == null) {
            responseResult.setCode(ResultCodeEnum.FAIL);
            responseResult.setMessage(SysErrorEnum.JOB_CONFIG_PARAM_IS_NULL.getErrorMsg());
            return  responseResult ;
        }
        JobConfigEntity jobConfig = jobConfigMapper.selectByPrimaryKey(jobConfigDTO.getId());
        if (jobConfig == null) {
            responseResult.setCode(ResultCodeEnum.FAIL);
            responseResult.setMessage(SysErrorEnum.JOB_CONFIG_JOB_IS_NOT_EXIST.getErrorMsg());
            return  responseResult ;
        }
        if (jobConfigDTO.getDeployModeEnum() == null) {
            ResponseBean checkSystemConfigResult = this.checkSystemConfig( DeployModeEnum.valueOf( jobConfig.getDeployMode() ) );
            if ( checkSystemConfigResult.getCode() != 200 ) {
                return checkSystemConfigResult ;
            }

        } else {
            ResponseBean checkSystemConfigResult = this.checkSystemConfig(jobConfigDTO.getDeployModeEnum());
            if ( checkSystemConfigResult.getCode() != 200 ) {
                return checkSystemConfigResult ;
            }
        }
        if (StringUtils.isNotEmpty(jobConfigDTO.getJobName())) {
            ResponseBean checkJobNameResult = this.checkJobName(jobConfigDTO.getJobName(), jobConfigDTO.getId());
            if ( checkJobNameResult.getCode() != 200 ) {
                return checkJobNameResult ;
            }
        }
        JobConfigEntity jobConfigUpdate = JobConfigDTO.toEntity(jobConfigDTO);
        jobConfigMapper.updateByPrimaryKeySelective(jobConfigUpdate);
        ResponseBean insertJobConfigHistoryResult = this.insertJobConfigHistory(jobConfigDTO.getId());
        if ( insertJobConfigHistoryResult.getCode() != 200 ) {
            return insertJobConfigHistoryResult ;
        }

        responseResult.setCode(ResultCodeEnum.SUCCESS);
        return responseResult;

    }

    @Override
    public void updateJobConfigStatusById(Long id, JobConfigStatus jobConfigStatus) {
        JobConfigEntity jobConfig = new JobConfigEntity();
        jobConfig.setId(id);
        jobConfig.setStatus(jobConfigStatus.getCode());
        jobConfigMapper.updateByPrimaryKeySelective(jobConfig);

    }

    @Override
    public void updateStatusByStart(Long id, Long operatorId , Long jobRunLogId, Long version) {
        int num = jobConfigMapper.updateStatusByStart(id, operatorId, JobConfigStatus.STARTING.getCode(), jobRunLogId, version);
        if (num < 1) {
            throw new BizException("启动状态更新失败");
        }
    }

    @Override
    public JobConfigDTO getJobConfigById(Long id) {
        if (id == null) {
            throw new BizException(SysErrorEnum.JOB_CONFIG_PARAM_IS_NULL);
        }

//        JobConfigDTO jobConfigDTO = JobConfigDTO.toDTO(jobConfigMapper.selectByPrimaryKey(id));
        JobConfigDTO jobConfigDTO = JobConfigDTO.toDTO( this.getById(id) );

        if (jobConfigDTO != null) {
            jobConfigDTO.setAlarmTypeEnumList(jobAlarmConfigService.findByJobId(id));
        }

        return jobConfigDTO;
    }

    /**
     * @param id
     * @return
     * @author wxj
     * @date 2021年12月28日 下午1:48:51
     * @version V1.0
     * @see
     */
    @Override
    public JobConfigDTO getJobConfigByIdContainDelete(Long id) {
        if (id == null) {
            return null;
        }
        JobConfigDTO jobConfigDTO = JobConfigDTO
                .toDTO(jobConfigMapper.selectByPrimaryKeyContainDelete(id));
        if (jobConfigDTO != null) {
            jobConfigDTO.setAlarmTypeEnumList(jobAlarmConfigService.findByJobId(id));
        }
        return jobConfigDTO;
    }

    @Override
    public ResponseBean openOrClose(Long id, YN yn, Long operatorId) {
        ResponseBean result = new ResponseBean();
        if (id == null) {
            result.setCode(ResultCodeEnum.FAIL);
            result.setMessage("参数不能为空");
            return result;
        }
        JobConfigEntity jobConfig = new JobConfigEntity();
        jobConfig.setId(id);
        jobConfig.setIsOpen(yn.getValue());
        jobConfig.setOperatorId(operatorId);
        jobConfig.setLastUpdateOperatorId(operatorId);
        jobConfigMapper.updateByPrimaryKeySelective(jobConfig);
        result.setCode(ResultCodeEnum.SUCCESS);
        return result ;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseBean deleteJobConfigById(Long id) {
        ResponseBean result = new ResponseBean ();
        JobConfigDTO jobConfigDTO = this.getJobConfigById(id);
        if ( jobConfigDTO == null ) {
            result.setCode(ResultCodeEnum.FAIL);
            result.setMessage("任务不存在!");
            return result ;
        }
        if ( JobConfigStatus.RUN.equals( jobConfigDTO.getStatus() ) || JobConfigStatus.STARTING.equals( jobConfigDTO.getStatus() ) ) {
            result.setCode(ResultCodeEnum.FAIL);
            result.setMessage("任务正在启动中或者正在运行，请先停止任务!");
            return result ;

        }
        if ( YN.Y.getValue() == jobConfigDTO.getIsOpen().intValue() ) {
            result.setCode(ResultCodeEnum.FAIL);
            result.setMessage("请先关闭任务!");
            return result ;
        }

        Integer savepointCnt = savepointBackupService.statisticsByJobConfigId(id);
        if ( savepointCnt > 0 ) {
            result.setCode(ResultCodeEnum.FAIL);
            result.setMessage(String.format("保存点数量为:%S,请先删除保存点!",savepointCnt));
            return result ;

        }

        //任务运行日志
        jobRunLogService.deleteLogByJobConfigId(id);
        //告警日志
        alartLogService.deleteLogByJobConfigId(id);
        //历史版本
        jobConfigHistoryService.deleteByJobConfigId(id);
        //任务列表
        jobConfigMapper.deleteById(id);
        return result.setCode(ResultCodeEnum.SUCCESS) ;
    }

    @Override
    public int recoveryDeleteJobConfigById(Long id) {
        return jobConfigMapper.recoveryDeleteJobConfigById(id);
    }



    @Override
    public List<JobConfigDTO> findJobConfigByStatus(Integer... status) {
        if (status == null) {
            return Collections.emptyList();
        }
        List<Integer> statusList = new ArrayList<>();
        for (Integer s : status) {
            statusList.add(s);
        }
        return JobConfigDTO.toListDTO(jobConfigMapper.findJobConfigByStatus(statusList));
    }

    @Override
    public List<BatchJob> getAllBatchJobs() {
        return jobConfigMapper.getAllBatchJobs();
    }


    /**
     * 检查任务名称是不是重复
     *
     * @author zhp
     * @date 2020-07-14
     * @time 13:56
     */
    private ResponseBean checkJobName(String jobName, Long id) {

        ResponseBean responseResult = new ResponseBean();
        long count = jobConfigMapper.selectCountByJobName(jobName, id);
        if (count >= 1) {
            responseResult.setCode(ResultCodeEnum.FAIL);
            responseResult.setMessage("任务名称:"+jobName+"已经存在!") ;  // SysErrorEnum JOB_CONFIG_JOB_NAME_IS_EXIST
        }else{
            responseResult.setCode(ResultCodeEnum.SUCCESS);
        }
        return responseResult ;
    }

    /**
     * 检查配置文件
     *
     * @author zhp
     * @since 1.0.0
     * @time 22:43
     */
    private ResponseBean checkSystemConfig(DeployModeEnum deployModeEnum) {
        ResponseBean responseResult = new ResponseBean();
        StringBuffer tips = new StringBuffer();
        String flinkHome = systemConfigService.getSystemConfigByKey(SysConfigEnum.FLINK_HOME.getKey());
        if (StringUtils.isEmpty(flinkHome)) {
            tips.append(" flinkHome、");
        }
        String webHome = systemConfigService.getSystemConfigByKey( SysConfigEnum.FLINK_STREAMING_PLATFORM_WEB_HOME.getKey() );
        if (StringUtils.isEmpty(webHome)) {
            tips.append(" web应用安装的目录、");
        }
        switch (deployModeEnum) {
            case YARN_PER:
            case YARN_APPLICATION:
                String yarnHttpAddress = systemConfigService.getSystemConfigByKey( SysConfigEnum.YARN_RM_HTTP_ADDRESS.getKey() );
                if (StringUtils.isEmpty(yarnHttpAddress)) {
                    tips.append("yarn_rm_http_address url地址、");
                }
                break;
            case LOCAL:
                String flinkHttpAddress = systemConfigService.getSystemConfigByKey( SysConfigEnum.FLINK_REST_HTTP_ADDRESS.getKey() );
                if (StringUtils.isEmpty(flinkHttpAddress)) {
                    tips.append("flink_rest_http_address url地址、");
                }
                break;
            case STANDALONE:
                String flinkHaHttpAddress = systemConfigService.getSystemConfigByKey( SysConfigEnum.FLINK_REST_HA_HTTP_ADDRESS.getKey() );
                if (StringUtils.isEmpty(flinkHaHttpAddress)) {
                    tips.append("flink_rest_ha_http_address url地址");
                }
                break;
            default:
                break;
        }
        if (StringUtils.isNotEmpty(tips.toString())) {
            responseResult.setCode(ResultCodeEnum.FAIL);
            responseResult.setMessage("请在系统设置面界面配置：" + tips.toString()) ;
        }else{
            responseResult.setCode(ResultCodeEnum.SUCCESS);
        }
        return responseResult ;
    }

    private ResponseBean insertJobConfigHistory(Long id) {
        ResponseBean responseResult = new ResponseBean();
        JobConfigEntity jobConfig = jobConfigMapper.selectByPrimaryKey(id);
        if (jobConfig == null) {
            log.warn("[insertJobConfigHistory] jobConfig is null id:{} ", id);
            responseResult.setCode(ResultCodeEnum.FAIL);
            responseResult.setMessage( String.format("[insertJobConfigHistory] jobConfig is null id:%s ", id) ) ;
            return responseResult;
        }
        jobConfigHistoryService.insertJobConfigHistory( JobConfigHistoryDTO.to(jobConfig) );
        responseResult.setCode(ResultCodeEnum.SUCCESS);
        return responseResult ;
    }


    @Override
    public List<jobTypeRunCount> jobTypeCnt(Long operatorId){
        return  jobConfigMapper.jobTypeCnt(operatorId);

    } ;


    @Override
    public List<JobStatusStatVO> JobStatusStat(Long operatorId){
        return  jobConfigMapper.JobStatusStat(operatorId);
    }
}
