package com.alinesno.infra.data.flink.service.impl;

import com.alinesno.infra.common.core.service.impl.IBaseServiceImpl;
import com.alinesno.infra.data.flink.entity.AlartLogEntity;
import com.alinesno.infra.data.flink.mapper.AlartLogEntityMapper;
import com.alinesno.infra.data.flink.service.IAlartLogEntityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


/**
 * 告警日志Service业务层处理
 *
 * @version 1.0.0
 * @author luoxiaodong
 */
@Service
public class AlartLogEntityServiceImpl extends IBaseServiceImpl<AlartLogEntity, AlartLogEntityMapper> implements IAlartLogEntityService {
    // 日志记录
    @SuppressWarnings("unused")
    private static final Logger log = LoggerFactory.getLogger(AlartLogEntityServiceImpl.class);
}
