package com.alinesno.infra.data.flink.service.impl;

import com.alinesno.infra.common.core.service.impl.IBaseServiceImpl;
import com.alinesno.infra.data.flink.entity.JobAlarmConfigEntity;
import com.alinesno.infra.data.flink.mapper.JobAlarmConfigEntityMapper;
import com.alinesno.infra.data.flink.service.IJobAlarmConfigEntityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 作业告警配置Service业务层处理
 *
 * @version 1.0.0
 * @author luoxiaodong
 */
@Service
public class JobAlarmConfigEntityServiceImpl extends IBaseServiceImpl<JobAlarmConfigEntity, JobAlarmConfigEntityMapper> implements IJobAlarmConfigEntityService {
    // 日志记录
    @SuppressWarnings("unused")
    private static final Logger log = LoggerFactory.getLogger(JobAlarmConfigEntityServiceImpl.class);
}
