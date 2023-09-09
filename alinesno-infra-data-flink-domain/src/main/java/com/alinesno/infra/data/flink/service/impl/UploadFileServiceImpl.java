package com.alinesno.infra.data.flink.service.impl;

import com.alinesno.infra.common.core.service.impl.IBaseServiceImpl;
import com.alinesno.infra.data.flink.entity.UploadFileEntity;
import com.alinesno.infra.data.flink.mapper.UploadFileMapper;
import com.alinesno.infra.data.flink.service.IUploadFileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 上传文件Service业务层处理
 *
 * @version 1.0.0
 * @author luoxiaodong
 */
@Service
public class UploadFileServiceImpl extends IBaseServiceImpl<UploadFileEntity, UploadFileMapper> implements IUploadFileService {
    //日志记录
    @SuppressWarnings("unused")
    private static final Logger log = LoggerFactory.getLogger(UploadFileServiceImpl.class);
}
