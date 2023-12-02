package com.alinesno.infra.data.stream.service.impl;

import com.alinesno.infra.common.core.service.impl.IBaseServiceImpl;
import com.alinesno.infra.data.stream.entity.UploadFileEntity;
import com.alinesno.infra.data.stream.exchange.common.MessageConstants;
import com.alinesno.infra.data.stream.exchange.config.CustomConfig;
import com.alinesno.infra.data.stream.exchange.dto.UploadFileDTO;
import com.alinesno.infra.data.stream.exchange.exceptions.BizException;
import com.alinesno.infra.data.stream.mapper.UploadFileMapper;
import com.alinesno.infra.data.stream.service.IUploadFileService;
import jakarta.validation.constraints.NotNull;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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

    @Autowired
    private UploadFileMapper uploadFileMapper;

    @Autowired
    private CustomConfig customConfig;


    @Override
    public void addFile(UploadFileDTO uploadFileDTO) {
        UploadFileEntity uploadFile = uploadFileMapper.getFileByName(uploadFileDTO.getFileName());
        if (uploadFile != null) {
            throw new BizException(MessageConstants.MESSAGE_012);
        }
        uploadFileMapper.insert(UploadFileDTO.toEntity(uploadFileDTO));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteFile(Long id) {
        UploadFileEntity uploadFile = uploadFileMapper.getFileById(id);
        if (uploadFile == null) {
            log.warn("fileName = {}  id={} is no ", id);
            return;
        }
        uploadFileMapper.deleteById(id);
        Boolean res = new File(uploadFile.getFilePath()).delete();
        log.info("文件 {} 清理 res={}", uploadFile.getFilePath(), res);
    }

    @Override
    public UploadFileDTO getUploadFileByFileName(String fileName) {
        return UploadFileDTO
                .toDTO(uploadFileMapper.getFileByName(fileName), customConfig.getUrlForDown());
    }

    @Override
    @Transactional
    public void deleteByIds(@NotNull String[] ids) {
        List<String> idList = new ArrayList();
        CollectionUtils.addAll(idList, ids);
        log.debug("delete id list:{}", idList.size());
        //删除记录时，删除记录的附件
        List<UploadFileEntity> UploadFileList = this.findByIds(idList);
        for (UploadFileEntity uploadFileEntity : UploadFileList) {
            String filePath = uploadFileEntity.getFilePath();
            File file = new File(filePath) ;
            if ( file.exists() ) {
                file.delete();
            }
        }

        this.mapper.deleteBatchIds(idList);
    }
}
