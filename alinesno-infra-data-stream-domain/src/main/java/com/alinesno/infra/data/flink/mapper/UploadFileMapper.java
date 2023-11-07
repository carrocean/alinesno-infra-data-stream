package com.alinesno.infra.data.flink.mapper;

import com.alinesno.infra.common.facade.mapper.repository.IBaseMapper;
import com.alinesno.infra.data.flink.entity.UploadFileEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 上传文件Mapper接口
 *
 * @version 1.0.0
 * @author luoxiaodong
 */
@Repository
public interface UploadFileMapper extends IBaseMapper<UploadFileEntity> {

    UploadFileEntity getFileByName(@Param("fileName") String fileName);

    UploadFileEntity getFileById(@Param("id") Long id);
}
