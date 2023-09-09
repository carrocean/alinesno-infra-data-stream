package com.alinesno.infra.data.flink.service.impl;

import com.alinesno.infra.common.core.service.impl.IBaseServiceImpl;
import com.alinesno.infra.data.flink.entity.SystemConfigEntity;
import com.alinesno.infra.data.flink.mapper.SystemConfigMapper;
import com.alinesno.infra.data.flink.service.ISystemConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 系统配置Service业务层处理
 *
 * @version 1.0.0
 * @author luoxiaodong
 */
@Service
public class SystemConfigServiceImpl extends IBaseServiceImpl<SystemConfigEntity, SystemConfigMapper> implements ISystemConfigService {
    //日志记录
    @SuppressWarnings("unused")
    private static final Logger log = LoggerFactory.getLogger(SystemConfigServiceImpl.class);
}
