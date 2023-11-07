package com.alinesno.infra.data.flink.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alinesno.infra.common.core.service.impl.IBaseServiceImpl;
import com.alinesno.infra.data.flink.exchange.dto.JobRunLogDTO;
import com.alinesno.infra.data.flink.entity.JobRunLogEntity;
import com.alinesno.infra.data.flink.exchange.vo.JobRunStatDayVO;
import com.alinesno.infra.data.flink.exchange.vo.jobTypeRunCount;
import com.alinesno.infra.data.flink.mapper.JobRunLogMapper;
import com.alinesno.infra.data.flink.service.IJobRunLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.alinesno.infra.data.flink.exchange.vo.JobRunStatVO;
import java.util.List;

/**
 * 作业运行日志Service业务层处理
 *
 * @version 1.0.0
 * @author luoxiaodong
 */
@Service
public class JobRunLogServiceImpl extends IBaseServiceImpl<JobRunLogEntity, JobRunLogMapper> implements IJobRunLogService {
    //日志记录
    @SuppressWarnings("unused")
    private static final Logger log = LoggerFactory.getLogger(JobRunLogServiceImpl.class);

    @Autowired
    private JobRunLogMapper jobRunLogMapper ;

    @Override
    public Long insertJobRunLog(JobRunLogDTO jobRunLogDTO) {
        JobRunLogEntity jobRunLog = JobRunLogDTO.toEntity(jobRunLogDTO);
        jobRunLogMapper.insert(jobRunLog);
        return jobRunLog.getId();
    }


    @Override
    public void updateLogById(String localLog, Long id) {
        try {
            JobRunLogEntity jobRunLog = new JobRunLogEntity();
            jobRunLog.setId(id);
            jobRunLog.setLocalLog(localLog);
            jobRunLogMapper.update(jobRunLog);
        } catch (Exception e) {
            log.error("更新日志 失败 id={} ,localLog={}", id, localLog, e);
        }

    }

    @Override
    public void deleteLogByJobConfigId(Long jobConfigId) {
        jobRunLogMapper.deleteByJobConfigId(jobConfigId);
    }

    @Override
    public JobRunLogDTO getDetailLogById(Long id) {
        return JobRunLogDTO.toDTO(jobRunLogMapper.selectById(id));
    }

    @Override
    public void updateJobRunLogById(JobRunLogDTO jobRunLogDTO) {
        jobRunLogMapper.update(JobRunLogDTO.toEntity(jobRunLogDTO));
    }


    @Override
    public List<jobTypeRunCount> jobTypeRunCnt(Long operatorId){
        return  jobRunLogMapper.jobTypeRunCnt(operatorId);

    } ;


    @Override
    public JSONObject JobRunStatDay(Long operatorId){

        int[] jobTotalCounts      =    new int[30];    //当天任务运行数
        int[] runSuccessCounts    =    new int[30];    //当天任务运行成功数
        int[] runFailCounts       =    new int[30];    //当天任务运行失败数
        int[] otherStatus         =    new int[30];    //当天任务运行其他状态数

        List<JobRunStatDayVO> jobRunStatDayVOS = jobRunLogMapper.JobRunStatDay(operatorId);

        if ( jobRunStatDayVOS != null && jobRunStatDayVOS.size() > 0) {
            int i = 0 ;
            for (JobRunStatDayVO dayVO : jobRunStatDayVOS) {
                jobTotalCounts[i]        = dayVO.getJobTotalCount().intValue();
                runSuccessCounts[i]      = dayVO.getRunSuccessCount().intValue();
                runFailCounts[i]         = dayVO.getRunFailCount().intValue();
                otherStatus[i]          = dayVO.getOtherStatusCount().intValue();
                i = i + 1 ;

            }
        }

        JSONObject result = new JSONObject();
        result.put("jobTotalData",jobTotalCounts);
        result.put("runSuccessData",runSuccessCounts);
        result.put("runFailData",runFailCounts);
        result.put("otherStatusData",otherStatus);

        return result ;

    };

    @Override
    public List<JobRunStatVO> JobRunStat(Long operatorId){
        return  jobRunLogMapper.JobRunStat(operatorId);
    };
}
