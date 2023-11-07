package com.alinesno.infra.data.flink.service;

import com.alinesno.infra.common.facade.services.IBaseService;
import com.alinesno.infra.data.flink.exchange.common.ResponseBean;
import com.alinesno.infra.data.flink.exchange.enums.AlarmTypeEnum;
import com.alinesno.infra.data.flink.entity.JobAlarmConfigEntity;

import java.util.List;
import java.util.Map;

/**
 * 作业告警配置Service接口
 *
 * @version 1.0.0
 * @author luoxiaodong
 */
public interface IJobAlarmConfigService extends IBaseService<JobAlarmConfigEntity> {
    /**
     * 批量新增/修改
     *
     * @author zhp
     * @date 2021/2/27
     * @time 17:49
     */
    ResponseBean upSertBatchJobAlarmConfig(List<AlarmTypeEnum> alarmTypeEnumList, Long jobId, Long operatorId);


    /**
     * 按jobId查询
     *
     * @author zhp
     * @date 2021/2/27
     * @time 17:53
     */
    List<AlarmTypeEnum> findByJobId(Long jobId);


    /**
     * @author zhp
     */
    Map<Long, List<AlarmTypeEnum>> findByJobIdList(List<String> jobIdList);
}
