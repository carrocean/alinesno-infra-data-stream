package com.alinesno.infra.data.flink.service.impl;

import com.alinesno.infra.common.core.service.impl.IBaseServiceImpl;
import com.alinesno.infra.data.flink.entity.SavepointBackupEntity;
import com.alinesno.infra.data.flink.mapper.SavepointBackupEntityMapper;
import com.alinesno.infra.data.flink.service.ISavepointBackupEntityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 保存点备份实体Service业务层处理
 *
 * @version 1.0.0
 * @author luoxiaodong
 */
@Service
public class SavepointBackupEntityServiceImpl extends IBaseServiceImpl<SavepointBackupEntity, SavepointBackupEntityMapper> implements ISavepointBackupEntityService {
    // 日志记录
    @SuppressWarnings("unused")
    private static final Logger log = LoggerFactory.getLogger(SavepointBackupEntityServiceImpl.class);
}
