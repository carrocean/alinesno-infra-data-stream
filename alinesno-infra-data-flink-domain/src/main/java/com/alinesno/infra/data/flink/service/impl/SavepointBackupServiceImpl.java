package com.alinesno.infra.data.flink.service.impl;

import com.alinesno.infra.common.core.service.impl.IBaseServiceImpl;
import com.alinesno.infra.data.flink.entity.SavepointBackupEntity;
import com.alinesno.infra.data.flink.mapper.SavepointBackupMapper;
import com.alinesno.infra.data.flink.service.ISavepointBackupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 保存点备份Service业务层处理
 *
 * @version 1.0.0
 * @author luoxiaodong
 */
@Service
public class SavepointBackupServiceImpl extends IBaseServiceImpl<SavepointBackupEntity, SavepointBackupMapper> implements ISavepointBackupService {
    //日志记录
    @SuppressWarnings("unused")
    private static final Logger log = LoggerFactory.getLogger(SavepointBackupServiceImpl.class);
}
