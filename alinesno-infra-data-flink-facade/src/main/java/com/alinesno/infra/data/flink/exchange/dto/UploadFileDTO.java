package com.alinesno.infra.data.flink.exchange.dto;

import com.alinesno.infra.data.flink.entity.UploadFileEntity;
import com.alinesno.infra.data.flink.exchange.common.util.DateFormatUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.compress.utils.Lists;

import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @author zhp
 * @Description:
 * @date 2022/09/19
 */
public class UploadFileDTO {

  private Long id;
  /**
   * 文件名字
   */
  private String fileName;

  /**
   * 文件路径
   */
  private String filePath;

  /**
   * 1:jar
   */
  private Integer type;

  /**
   * 创建时间
   */
  private Date createTime;

  private String createTimeStr;

  private Long creator;

  private Long editor;

  private String downloadJarHttp;

  public static UploadFileEntity toEntity(UploadFileDTO uploadFileDTO) {
    if (uploadFileDTO == null) {
      return null;
    }
    UploadFileEntity uploadFile = new UploadFileEntity();
    uploadFile.setId(uploadFileDTO.getId());
    uploadFile.setFileName(uploadFileDTO.getFileName());
    uploadFile.setFilePath(uploadFileDTO.getFilePath());
    uploadFile.setType(uploadFileDTO.getType());
    uploadFile.setAddTime(uploadFileDTO.getCreateTime());
    uploadFile.setOperatorId(uploadFileDTO.getCreator());
    uploadFile.setLastUpdateOperatorId(uploadFileDTO.getEditor());
    return uploadFile;
  }


  public static UploadFileDTO toDTO(UploadFileEntity uploadFile, String downloadUrl) {
    if (uploadFile == null) {
      return null;
    }
    UploadFileDTO uploadFileDTO = new UploadFileDTO();
    uploadFileDTO.setId(uploadFile.getId());
    uploadFileDTO.setFileName(uploadFile.getFileName());
    uploadFileDTO.setFilePath(uploadFile.getFilePath());
    uploadFileDTO.setType(uploadFile.getType());
    uploadFileDTO.setDownloadJarHttp(downloadUrl + uploadFile.getFileName());
    if (uploadFile.getAddTime() != null) {
      uploadFileDTO.setCreateTime(uploadFile.getAddTime());
      uploadFileDTO.setCreateTimeStr(DateFormatUtils.toFormatString(uploadFile.getAddTime()));
    }
    return uploadFileDTO;
  }

  public static List<UploadFileDTO> toDTOList(List<UploadFileEntity> uploadFileList, String downloadUrl) {
    if (CollectionUtils.isEmpty(uploadFileList)) {
      return Collections.emptyList();
    }
    List<UploadFileDTO> list = Lists.newArrayList();
    for (UploadFileEntity uploadFile : uploadFileList) {
      list.add(toDTO(uploadFile, downloadUrl));
    }
    return list;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public String getFilePath() {
    return filePath;
  }

  public void setFilePath(String filePath) {
    this.filePath = filePath;
  }

  public Integer getType() {
    return type;
  }

  public void setType(Integer type) {
    this.type = type;
  }

  public Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }

  public String getCreateTimeStr() {
    return createTimeStr;
  }

  public void setCreateTimeStr(String createTimeStr) {
    this.createTimeStr = createTimeStr;
  }

  public Long getCreator() {
    return creator;
  }

  public void setCreator(Long creator) {
    this.creator = creator;
  }

  public Long getEditor() {
    return editor;
  }

  public void setEditor(Long editor) {
    this.editor = editor;
  }

  public String getDownloadJarHttp() {
    return downloadJarHttp;
  }

  public void setDownloadJarHttp(String downloadJarHttp) {
    this.downloadJarHttp = downloadJarHttp;
  }
}
