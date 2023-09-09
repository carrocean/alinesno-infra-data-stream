package com.alinesno.infra.data.flink.service.impl;

import com.alinesno.infra.common.core.service.impl.IBaseServiceImpl;
import com.alinesno.infra.data.flink.entity.JobConfigHistoryEntity;
import com.alinesno.infra.data.flink.mapper.JobConfigHistoryMapper;
import com.alinesno.infra.data.flink.service.IJobConfigHistoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 作业配置历史Service业务层处理
 *
 * @version 1.0.0
 * @author luoxiaodong
 */
@Service
public class JobConfigHistoryServiceImpl extends IBaseServiceImpl<JobConfigHistoryEntity, JobConfigHistoryMapper> implements IJobConfigHistoryService {
    //日志记录
    @SuppressWarnings("unused")
    private static final Logger log = LoggerFactory.getLogger(JobConfigHistoryServiceImpl.class);
}
