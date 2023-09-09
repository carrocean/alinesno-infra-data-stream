package com.alinesno.infra.data.flink.service.impl;

import com.alinesno.infra.common.core.service.impl.IBaseServiceImpl;
import com.alinesno.infra.data.flink.entity.JobConfigEntity;
import com.alinesno.infra.data.flink.mapper.JobConfigMapper;
import com.alinesno.infra.data.flink.service.IJobConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

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
}
