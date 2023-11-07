package com.alinesno.infra.data.flink.exchange.dto;

import cn.hutool.core.collection.CollectionUtil;
import com.alinesno.infra.data.flink.entity.SavepointBackupEntity;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @author
 */
public class SavepointBackupDTO {

  private Long id;

  private Long jobConfigId;

  /**
   * backup地址
   */
  private String savepointPath;


  private Date backupTime;


  public static SavepointBackupDTO toDTO(SavepointBackupEntity savepointBackup) {
    if (savepointBackup == null) {
      return null;
    }
    SavepointBackupDTO savepointBackupDTO = new SavepointBackupDTO();
    savepointBackupDTO.setId(savepointBackup.getId());
    savepointBackupDTO.setJobConfigId(savepointBackup.getJobConfigId());
    savepointBackupDTO.setSavepointPath(savepointBackup.getSavepointPath());
    savepointBackupDTO.setBackupTime(savepointBackup.getBackupTime());
    return savepointBackupDTO;
  }

  public static List<SavepointBackupDTO> toDTOList(List<SavepointBackupEntity> savepointBackupList) {
    if (CollectionUtil.isEmpty(savepointBackupList)) {
      return Collections.emptyList();
    }
    List<SavepointBackupDTO> list = CollectionUtil.newArrayList();
    for (SavepointBackupEntity savepointBackup : savepointBackupList) {
      list.add(toDTO(savepointBackup));
    }
    return list;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getJobConfigId() {
    return jobConfigId;
  }

  public void setJobConfigId(Long jobConfigId) {
    this.jobConfigId = jobConfigId;
  }

  public String getSavepointPath() {
    return savepointPath;
  }

  public void setSavepointPath(String savepointPath) {
    this.savepointPath = savepointPath;
  }

  public Date getBackupTime() {
    return backupTime;
  }

  public void setBackupTime(Date backupTime) {
    this.backupTime = backupTime;
  }
}
