package com.alinesno.infra.data.flink.service.impl;

import com.alinesno.infra.common.core.service.impl.IBaseServiceImpl;
import com.alinesno.infra.data.flink.entity.IpStatusEntity;
import com.alinesno.infra.data.flink.mapper.IpStatusEntityMapper;
import com.alinesno.infra.data.flink.service.IIpStatusEntityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * IP状态实体Service业务层处理
 *
 * @version 1.0.0
 * @author luoxiaodong
 */
@Service
public class IpStatusEntityServiceImpl extends IBaseServiceImpl<IpStatusEntity, IpStatusEntityMapper> implements IIpStatusEntityService {
    // 日志记录
    @SuppressWarnings("unused")
    private static final Logger log = LoggerFactory.getLogger(IpStatusEntityServiceImpl.class);
}
