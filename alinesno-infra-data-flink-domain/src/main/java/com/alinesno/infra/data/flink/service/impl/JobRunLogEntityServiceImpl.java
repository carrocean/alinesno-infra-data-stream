package com.alinesno.infra.data.flink.service.impl;

import com.alinesno.infra.common.core.service.impl.IBaseServiceImpl;
import com.alinesno.infra.data.flink.entity.JobRunLogEntity;
import com.alinesno.infra.data.flink.mapper.JobRunLogEntityMapper;
import com.alinesno.infra.data.flink.service.IJobRunLogEntityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 作业运行日志Service业务层处理
 *
 * @version 1.0.0
 * @author luoxiaodong
 */
@Service
public class JobRunLogEntityServiceImpl extends IBaseServiceImpl<JobRunLogEntity, JobRunLogEntityMapper> implements IJobRunLogEntityService {
    // 日志记录
    @SuppressWarnings("unused")
    private static final Logger log = LoggerFactory.getLogger(JobRunLogEntityServiceImpl.class);
}
