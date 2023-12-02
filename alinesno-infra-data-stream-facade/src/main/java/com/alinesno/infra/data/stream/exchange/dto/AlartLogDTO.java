package com.alinesno.infra.data.stream.exchange.dto;

import cn.hutool.core.collection.CollectionUtil;
import com.alinesno.infra.data.stream.entity.AlartLogEntity;
import com.alinesno.infra.data.stream.exchange.enums.AlarmLogStatusEnum;
import com.alinesno.infra.data.stream.exchange.enums.AlarmLogTypeEnum;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @author
 */
public class AlartLogDTO {

  private
  Long id;

  private
  Long jobConfigId;


  private String jobName;


  /**
   * 消息内容
   */
  private String message;

  /**
   * 1:钉钉
   */
  private AlarmLogTypeEnum alarMLogTypeEnum;

  /**
   * 1:成功 0:失败
   */
  private AlarmLogStatusEnum alarmLogStatusEnum;


  /**
   * 失败原因
   */
  private String failLog;


  /**
   * 创建时间
   */
  private Date createTime;

  /**
   * 修改时间
   */
  private Date editTime;

  private Long creator;

  private Long editor;

  private Integer status;


  public static AlartLogEntity toEntity(AlartLogDTO alartLogDTO) {
    if (alartLogDTO == null) {
      return null;
    }
    AlartLogEntity alartLog = new AlartLogEntity();
    alartLog.setId(alartLogDTO.getId());
    alartLog.setJobConfigId(alartLogDTO.getJobConfigId());
    alartLog.setMessage(alartLogDTO.getMessage());
    alartLog.setType(alartLogDTO.getAlarMLogTypeEnum().getCode());
    alartLog.setStatus(alartLogDTO.getAlarmLogStatusEnum().getCode());
    alartLog.setFailLog(alartLogDTO.getFailLog());
    alartLog.setAddTime(alartLogDTO.getCreateTime());
    alartLog.setUpdateTime(alartLogDTO.getEditTime());
    alartLog.setOperatorId(alartLogDTO.getCreator());
    alartLog.setLastUpdateOperatorId(alartLogDTO.getEditor());
    alartLog.setJobName(alartLogDTO.getJobName());
    return alartLog;
  }

  public static AlartLogDTO toDTO(AlartLogEntity alartLog) {
    if (alartLog == null) {
      return null;
    }
    AlartLogDTO alartLogDTO = new AlartLogDTO();
    alartLogDTO.setId(alartLog.getId());
    alartLogDTO.setJobConfigId(alartLog.getJobConfigId());
    alartLogDTO.setMessage(alartLog.getMessage());
    alartLogDTO.setAlarMLogTypeEnum(AlarmLogTypeEnum.getAlarmLogTypeEnum(alartLog.getType()));
    alartLogDTO
        .setAlarmLogStatusEnum(AlarmLogStatusEnum.getAlarmLogStatusEnum(alartLog.getStatus()));
    alartLogDTO.setFailLog(alartLog.getFailLog());
    alartLogDTO.setCreateTime(alartLog.getAddTime());
    alartLogDTO.setEditTime(alartLog.getUpdateTime());
    alartLogDTO.setCreator(alartLog.getOperatorId());
    alartLogDTO.setEditor(alartLog.getLastUpdateOperatorId());
    alartLogDTO.setJobName(alartLog.getJobName());
    alartLogDTO.setStatus(alartLog.getStatus());
    return alartLogDTO;
  }

  public static List<AlartLogDTO> toListDTO(List<AlartLogEntity> alartLogList) {
    if (CollectionUtil.isEmpty(alartLogList)) {
      return Collections.emptyList();
    }
    List<AlartLogDTO> list = new ArrayList<>();
    for (AlartLogEntity alartLog : alartLogList) {
      AlartLogDTO alartLogDTO = AlartLogDTO.toDTO(alartLog);
      if (alartLog != null) {
        list.add(alartLogDTO);
      }
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

  public String getJobName() {
    return jobName;
  }

  public void setJobName(String jobName) {
    this.jobName = jobName;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public AlarmLogTypeEnum getAlarMLogTypeEnum() {
    return alarMLogTypeEnum;
  }

  public void setAlarMLogTypeEnum(AlarmLogTypeEnum alarMLogTypeEnum) {
    this.alarMLogTypeEnum = alarMLogTypeEnum;
  }

  public AlarmLogStatusEnum getAlarmLogStatusEnum() {
    return alarmLogStatusEnum;
  }

  public void setAlarmLogStatusEnum(AlarmLogStatusEnum alarmLogStatusEnum) {
    this.alarmLogStatusEnum = alarmLogStatusEnum;
  }

  public String getFailLog() {
    return failLog;
  }

  public void setFailLog(String failLog) {
    this.failLog = failLog;
  }

  public Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }

  public Date getEditTime() {
    return editTime;
  }

  public void setEditTime(Date editTime) {
    this.editTime = editTime;
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

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }
}
