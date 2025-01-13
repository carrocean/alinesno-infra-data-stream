package com.alinesno.infra.data.stream.exchange.controller;

import com.alibaba.fastjson.JSONObject;
import com.alinesno.infra.common.core.constants.SpringInstanceScope;
import com.alinesno.infra.common.facade.pageable.DatatablesPageBean;
import com.alinesno.infra.common.facade.pageable.TableDataInfo;
import com.alinesno.infra.common.facade.response.AjaxResult;
import com.alinesno.infra.common.web.adapter.rest.BaseController;
import com.alinesno.infra.data.stream.entity.JobRunLogEntity;
import com.alinesno.infra.data.stream.exchange.config.CustomConfig;
import com.alinesno.infra.data.stream.exchange.enums.YN;
import com.alinesno.infra.data.stream.exchange.utils.Constants;
import com.alinesno.infra.data.stream.exchange.vo.JobRunLogVO;
import com.alinesno.infra.data.stream.exchange.vo.JobRunStatVO;
import com.alinesno.infra.data.stream.exchange.vo.jobTypeRunCount;
import com.alinesno.infra.data.stream.service.IJobRunLogService;
import io.swagger.annotations.Api;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 处理与JobRunLog相关的请求的Controller。
 * 继承自BaseController类并实现IJobRunLogService接口。
 *
 * @version 1.0.0
 * @author luoxiaodong
 */
@Api(tags = "JobRunLog")
@RestController
@Scope(SpringInstanceScope.PROTOTYPE)
@RequestMapping("/api/infra/data/flink/jobRunLog")
public class JobRunLogController extends BaseController<JobRunLogEntity, IJobRunLogService> {

    // 日志记录
    private static final Logger log = LoggerFactory.getLogger(JobRunLogController.class);

    @Autowired
    private IJobRunLogService service;

    @Autowired
    private CustomConfig customConfig;

    /**
     * 获取JobRunLog的DataTables数据。
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
        this.setConditions(page);
        return this.toPage(model, this.getFeign(), page);
    }

    @Override
    public IJobRunLogService getFeign() {
        return this.service;
    }

    /**
     * 查询日志详情
     *
     * @param logid
     * @return
     * @author wxj
     * @since 1.0.0
     * @version V1.0
     */
    @RequestMapping(value = "/logDetail", method = {RequestMethod.POST})
    public AjaxResult sysConfig(Long logid) {
        JobRunLogVO vo = JobRunLogVO
                .toVO(service.getDetailLogById(logid), YN.Y.getCode(), customConfig.getWebPort());
        return AjaxResult.success(vo);
    }

    /**
     * 查询每个任务类型的执行累计次数
     */
    @RequestMapping(value = "/jobTypeRunCnt", method = {RequestMethod.POST})
    public AjaxResult jobTypeRunCnt(HttpServletRequest request) {
        Long operatorId = 0L ;

//        // 设置用户
//        ManagerAccountEntity account = CurrentAccountJwt.get() ;
//        if (account != null) {
//            operatorId = account.getId() ;
//        }

        AjaxResult result = AjaxResult.success() ;

        Integer sqlStreamCount = 0 ;
        Integer jarCount = 0 ;
        Integer sqlBatchCount = 0 ;


        List<jobTypeRunCount> jobTypeRunCounts = service.jobTypeRunCnt(operatorId);
        for (jobTypeRunCount jobTypeRunCount : jobTypeRunCounts) {
            if( jobTypeRunCount.getJobType() == 0 ) {
                sqlStreamCount = jobTypeRunCount.getRunNum() ;
            }
            else if( jobTypeRunCount.getJobType() == 1 ) {
                jarCount = jobTypeRunCount.getRunNum() ;

            }
            else if( jobTypeRunCount.getJobType() == 2 ) {
                sqlBatchCount = jobTypeRunCount.getRunNum() ;

            }
        }

        result.put("sqlStreamCount", sqlStreamCount) ;
        result.put("jarCount", jarCount) ;
        result.put("sqlBatchCount", sqlBatchCount) ;

        return result;
    }

    /**
     * 近一个月执行情况曲线数据
     *
     * @return
     */
    @GetMapping("/coordinateData")
    public AjaxResult getCoordinateDataOpen(HttpServletRequest request) {

        Long operatorId = null ;

//        // 设置用户
//        ManagerAccountEntity account = CurrentAccountJwt.get() ;
//        if (account != null) {
//            operatorId = account.getId() ;
//        }

        JSONObject jsonObject = service.JobRunStatDay(operatorId);

        return AjaxResult.success(jsonObject);
    }

    /**
     * 查询每个任务类型的执行累计次数
     */
    @RequestMapping(value = "/jobRunStat", method = {RequestMethod.GET})
    public AjaxResult jobRunStat(HttpServletRequest request) {
        Long operatorId = 0L ;

//        // 设置用户
//        ManagerAccountEntity account = CurrentAccountJwt.get() ;
//        if (account != null) {
//            operatorId = account.getId() ;
//        }

        Integer sqlJobTotalCount    = 0 ;
        Integer sqlRunSuccessCount  = 0 ;
        Integer sqlOtherStatusCount = 0 ;
        Integer jarJobTotalCount    = 0 ;
        Integer jarRunSuccessCount  = 0 ;
        Integer jarOtherStatusCount = 0 ;

        List<JobRunStatVO> jobRunStatVOS = service.JobRunStat(operatorId);

        if ( jobRunStatVOS != null && jobRunStatVOS.size() > 0) {
            for (JobRunStatVO dayVO : jobRunStatVOS) {
                if ( dayVO.getJobType() == 1 )
                {
                    jarJobTotalCount    = dayVO.getJobTotalCount().intValue() ;
                    jarRunSuccessCount  = dayVO.getRunSuccessCount().intValue() ;
                    jarOtherStatusCount = dayVO.getOtherStatusCount().intValue() ;

                } else {
                    sqlJobTotalCount    = sqlJobTotalCount    + dayVO.getJobTotalCount().intValue() ;
                    sqlRunSuccessCount  = sqlRunSuccessCount  + dayVO.getRunSuccessCount().intValue() ;
                    sqlOtherStatusCount = sqlOtherStatusCount + dayVO.getOtherStatusCount().intValue() ;
                }
            }
        }

        AjaxResult result = AjaxResult.success() ;
        result.put("sqlJobTotalData",sqlJobTotalCount);
        result.put("sqlRunSuccessData",sqlRunSuccessCount);
        result.put("sqlOtherStatusData",sqlOtherStatusCount);
        result.put("jarJobTotalData",jarJobTotalCount);
        result.put("jarRunSuccessData",jarRunSuccessCount);
        result.put("jarOtherStatusData",jarOtherStatusCount);
        return result ;

    }

    private void setConditions(DatatablesPageBean page) {
        Map<String, Object> condition = page.getCondition();
        if (MapUtils.isNotEmpty(condition)) {
            String startDate = condition.get("startTime|eq") == null ? null : (String) condition.get("startTime|eq");
            String endDate = condition.get("endTime|eq") == null ? null : (String) condition.get("endTime|eq");

            if (StringUtils.isNotBlank(startDate)) {
                if (startDate.length() == Constants.DATE_TIME_LENGTH) {
                    /* yyyy-MM-dd HH:mm:ss */
                    //   String[] arr = startDate.split(" ");
                    //   startDate = arr[0] + " 00:00:00";
                } else if (startDate.length() == Constants.DATE_LENGTH) {
                    /* yyyy-MM-dd */
                    startDate = startDate.trim() + " 00:00:00";
                }
                condition.put("addTime|geTime", startDate);
            }
            if (StringUtils.isNotBlank(endDate)) {
                if (endDate.length() == Constants.DATE_TIME_LENGTH) {
                    /* yyyy-MM-dd HH:mm:ss */
                    // String[] arr = endDate.split(" ");
                    // endDate = arr[0] + " 23:59:59";
                } else if (endDate.length() == Constants.DATE_LENGTH) {
                    /* yyyy-MM-dd */
                    endDate = endDate.trim() + " 23:59:59";
                }
                condition.put("addTime|leTime", endDate);
            }
            condition.remove("startTime|eq");
            condition.remove("endTime|eq");
            page.setCondition(condition);
        }
    }
}
