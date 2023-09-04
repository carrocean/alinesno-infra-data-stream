package com.alinesno.infra.data.flink.service.impl;

import com.alinesno.infra.common.core.service.impl.IBaseServiceImpl;
import com.alinesno.infra.data.flink.entity.BatchJobEntity;
import com.alinesno.infra.data.flink.mapper.BatchJobMapper;
import com.alinesno.infra.data.flink.service.IBatchJobService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 批处理作业Service业务层处理
 *
 * @version 1.0.0
 * @author luoxiaodong
 */
@Service
public class BatchJobServiceImpl extends IBaseServiceImpl<BatchJobEntity, BatchJobMapper> implements IBatchJobService {
    // 日志记录
    @SuppressWarnings("unused")

    private static final Logger log = LoggerFactory.getLogger(BatchJobServiceImpl.class);
}
