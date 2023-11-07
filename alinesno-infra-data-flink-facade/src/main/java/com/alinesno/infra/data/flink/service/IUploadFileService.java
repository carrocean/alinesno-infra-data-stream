package com.alinesno.infra.data.flink.service;

import com.alinesno.infra.common.facade.services.IBaseService;
import com.alinesno.infra.data.flink.entity.UploadFileEntity;
import com.alinesno.infra.data.flink.exchange.dto.UploadFileDTO;
import jakarta.validation.constraints.NotNull;
import org.springframework.transaction.annotation.Transactional;

/**
 * 上传文件Service接口
 *
 * @version 1.0.0
 * @author luoxiaodong
 */
public interface IUploadFileService extends IBaseService<UploadFileEntity> {

    void addFile(UploadFileDTO uploadFileDTO);


    void deleteFile(Long id);

    UploadFileDTO getUploadFileByFileName(String fileName);


    @Transactional
    void deleteByIds(@NotNull String[] ids);
}
