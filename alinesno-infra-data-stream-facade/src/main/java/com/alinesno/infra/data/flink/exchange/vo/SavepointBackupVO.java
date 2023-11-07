package com.alinesno.infra.data.flink.exchange.vo;

import cn.hutool.core.collection.CollectionUtil;
import com.alinesno.infra.data.flink.exchange.common.util.DateFormatUtils;
import com.alinesno.infra.data.flink.exchange.dto.SavepointBackupDTO;

import java.util.Collections;
import java.util.List;

/**
 * @author zhp
 * @date 2020-09-21
 * @time 00:02
 */
public class SavepointBackupVO {


  private Long id;
  /**
   * backup地址
   */
  private String savepointPath;


  private String backupTime;


  public static SavepointBackupVO toDTO(SavepointBackupDTO savepointBackupDTO) {
    if (savepointBackupDTO == null) {
      return null;
    }
    SavepointBackupVO savepointBackupVO = new SavepointBackupVO();
    savepointBackupVO.setId(savepointBackupDTO.getId());
    savepointBackupVO.setSavepointPath(savepointBackupDTO.getSavepointPath());
    savepointBackupVO
        .setBackupTime(DateFormatUtils.toFormatString(savepointBackupDTO.getBackupTime()));
    return savepointBackupVO;
  }

  public static List<SavepointBackupVO> toDTOList(List<SavepointBackupDTO> savepointBackupList) {
    if (CollectionUtil.isEmpty(savepointBackupList)) {
      return Collections.emptyList();
    }
    List<SavepointBackupVO> list = CollectionUtil.newArrayList();
    for (SavepointBackupDTO savepointBackupDTO : savepointBackupList) {
      list.add(toDTO(savepointBackupDTO));
    }
    return list;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getSavepointPath() {
    return savepointPath;
  }

  public void setSavepointPath(String savepointPath) {
    this.savepointPath = savepointPath;
  }

  public String getBackupTime() {
    return backupTime;
  }

  public void setBackupTime(String backupTime) {
    this.backupTime = backupTime;
  }
}
