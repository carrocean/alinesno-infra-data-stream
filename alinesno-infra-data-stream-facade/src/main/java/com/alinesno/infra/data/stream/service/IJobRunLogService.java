package com.alinesno.infra.data.stream.service;

import com.alibaba.fastjson.JSONObject;
import com.alinesno.infra.common.facade.services.IBaseService;
import com.alinesno.infra.data.stream.entity.JobRunLogEntity;
import com.alinesno.infra.data.stream.exchange.dto.JobRunLogDTO;
import com.alinesno.infra.data.stream.exchange.vo.JobRunStatVO;
import com.alinesno.infra.data.stream.exchange.vo.jobTypeRunCount;

import java.util.List;

/**
 * 作业运行日志Service接口
 *
 * @version 1.0.0
 * @author luoxiaodong
 */
public interface IJobRunLogService extends IBaseService<JobRunLogEntity> {

    /**
     * 新增任务 返回主键
     *
     * @author zhp
     * @date 2020-08-17
     * @time 00:33
     */
    Long insertJobRunLog(JobRunLogDTO jobRunLogDTO);


    /**
     * 日志更新
     *
     * @author zhp
     * @date 2020-08-24
     * @time 21:08
     */
    void updateLogById(String log, Long id);

    /**
     * 删除日志
     *
     * @author zhp
     * @date 2020-08-30
     * @time 23:44
     */
    void deleteLogByJobConfigId(Long jobConfigId);

    /**
     * 单个日志详情
     *
     * @author zhp
     * @date 2020-08-17
     * @time 19:49
     */
    JobRunLogDTO getDetailLogById(Long id);

    /**
     * 更新
     *
     * @author zhp
     * @date 2020-08-18
     * @time 19:17
     */
    void updateJobRunLogById(JobRunLogDTO jobRunLogDTO);


    List<jobTypeRunCount> jobTypeRunCnt(Long operatorId) ;

    JSONObject JobRunStatDay(Long operatorId);

    List<JobRunStatVO> JobRunStat(Long operatorId);
}
