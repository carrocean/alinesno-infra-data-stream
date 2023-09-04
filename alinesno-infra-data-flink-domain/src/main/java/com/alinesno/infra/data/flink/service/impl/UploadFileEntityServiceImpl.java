package com.alinesno.infra.data.flink.service.impl;

import com.alinesno.infra.common.core.service.impl.IBaseServiceImpl;
import com.alinesno.infra.data.flink.entity.UploadFileEntity;
import com.alinesno.infra.data.flink.mapper.UploadFileEntityMapper;
import com.alinesno.infra.data.flink.service.IUploadFileEntityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 上传文件实体Service业务层处理
 *
 * @version 1.0.0
 * @author luoxiaodong
 */
@Service
public class UploadFileEntityServiceImpl extends IBaseServiceImpl<UploadFileEntity, UploadFileEntityMapper> implements IUploadFileEntityService {
    // 日志记录
    @SuppressWarnings("unused")
    private static final Logger log = LoggerFactory.getLogger(UploadFileEntityServiceImpl.class);
}
