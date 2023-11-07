package com.alinesno.infra.data.flink.service.impl;

import com.alinesno.infra.common.core.service.impl.IBaseServiceImpl;
import com.alinesno.infra.data.flink.entity.IpStatusEntity;
import com.alinesno.infra.data.flink.mapper.IpStatusMapper;
import com.alinesno.infra.data.flink.service.IIpStatusService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * IP状态Service业务层处理
 *
 * @version 1.0.0
 * @author luoxiaodong
 */
@Service
public class IpStatusServiceImpl extends IBaseServiceImpl<IpStatusEntity, IpStatusMapper> implements IIpStatusService {
    //日志记录
    @SuppressWarnings("unused")
    private static final Logger log = LoggerFactory.getLogger(IpStatusServiceImpl.class);

    @Override
    public void cancelIp() {

    }

    @Override
    public void updateHeartbeatBylocalIp() {

    }

    @Override
    public boolean isLeader() {
        return false;
    }

    @Override
    public void registerIp() {

    }
}
