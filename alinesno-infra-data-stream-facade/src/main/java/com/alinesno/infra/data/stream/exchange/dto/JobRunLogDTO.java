package com.alinesno.infra.data.stream.exchange.dto;

import com.alinesno.infra.data.stream.entity.JobRunLogEntity;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @author zhp
 * @date 2020-08-17
 * @time 00:14
 */
public class JobRunLogDTO implements Serializable {

  private static final long serialVersionUID = 1L;

  private long id;

  private long jobConfigId;

  /**
   * 任务名称
   */
  private String jobName;

  /**
   * 任务描述
   */
  private String jobDesc;

  /**
   * 提交模式: standalone 、yarn 、yarn-session
   */
  private String deployMode;

  /**
   * 运行后的任务id
   */
  private String jobId;

  /**
   * 远程日志url的地址
   */
  private String remoteLogUrl;

  /**
   * 启动时间
   */
  private Date startTime;

  /**
   * 结束时间
   */
  private Date endTime;

  /**
   * 任务状态
   */
  private String jobStatus;


  private long creator;

  private long editor;

  /**
   * 创建时间
   */
  private Date createTime;

  /**
   * 修改时间
   */
  private Date editTime;


  /**
   * 启动时本地日志
   */
  private String localLog;

  private String runIp;


  public static JobRunLogEntity toEntity(JobRunLogDTO jobRunLogDTO) {
    if (jobRunLogDTO == null) {
      return null;
    }
    JobRunLogEntity jobRunLog = new JobRunLogEntity();
    jobRunLog.setId(jobRunLogDTO.getId());
    jobRunLog.setJobConfigId(jobRunLogDTO.getJobConfigId());
    jobRunLog.setJobName(jobRunLogDTO.getJobName());
    jobRunLog.setJobDesc(jobRunLogDTO.getJobDesc());
    jobRunLog.setDeployMode(jobRunLogDTO.getDeployMode());
    jobRunLog.setJobId(jobRunLogDTO.getJobId());
    jobRunLog.setRemoteLogUrl(jobRunLogDTO.getRemoteLogUrl());
    jobRunLog.setStartTime(jobRunLogDTO.getStartTime());
    jobRunLog.setEndTime(jobRunLogDTO.getEndTime());
    jobRunLog.setJobStatus(jobRunLogDTO.getJobStatus());
    jobRunLog.setOperatorId(jobRunLogDTO.getCreator());
    jobRunLog.setLastUpdateOperatorId(jobRunLogDTO.getEditor());
    jobRunLog.setAddTime(jobRunLogDTO.getCreateTime());
    jobRunLog.setUpdateTime(jobRunLogDTO.getEditTime());
    jobRunLog.setLocalLog(jobRunLogDTO.getLocalLog());
    jobRunLog.setRunIp(jobRunLogDTO.getRunIp());
    return jobRunLog;
  }


  public static JobRunLogDTO toDTO(JobRunLogEntity jobRunLog) {
    if (jobRunLog == null) {
      return null;
    }
    JobRunLogDTO jobRunLogDTO = new JobRunLogDTO();
    jobRunLogDTO.setId(jobRunLog.getId());
    jobRunLogDTO.setJobConfigId(jobRunLog.getJobConfigId());
    jobRunLogDTO.setJobName(jobRunLog.getJobName());
    jobRunLogDTO.setJobDesc(jobRunLog.getJobDesc());
    jobRunLogDTO.setDeployMode(jobRunLog.getDeployMode());
    jobRunLogDTO.setJobId(jobRunLog.getJobId());
    jobRunLogDTO.setRemoteLogUrl(jobRunLog.getRemoteLogUrl());
    jobRunLogDTO.setStartTime(jobRunLog.getStartTime());
    jobRunLogDTO.setEndTime(jobRunLog.getEndTime());
    jobRunLogDTO.setJobStatus(jobRunLog.getJobStatus());
    jobRunLogDTO.setCreateTime(jobRunLog.getAddTime());
    jobRunLogDTO.setEditTime(jobRunLog.getUpdateTime());
    jobRunLogDTO.setCreator( jobRunLog.getOperatorId() == null ? 0l : jobRunLog.getOperatorId() );
    jobRunLogDTO.setEditor( jobRunLog.getOperatorId() == null ? 0l : jobRunLog.getOperatorId() );
    jobRunLogDTO.setLocalLog(jobRunLog.getLocalLog());
    jobRunLogDTO.setRunIp(jobRunLog.getRunIp());
    return jobRunLogDTO;
  }

  public static List<JobRunLogDTO> toListDTO(List<JobRunLogEntity> jobRunLogList) {
    if (CollectionUtils.isEmpty(jobRunLogList)) {
      return Collections.emptyList();
    }
    List<JobRunLogDTO> list = new ArrayList<>();

    for (JobRunLogEntity jobRunLog : jobRunLogList) {
      list.add(JobRunLogDTO.toDTO(jobRunLog));
    }
    return list;


  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public long getJobConfigId() {
    return jobConfigId;
  }

  public void setJobConfigId(long jobConfigId) {
    this.jobConfigId = jobConfigId;
  }

  public String getJobName() {
    return jobName;
  }

  public void setJobName(String jobName) {
    this.jobName = jobName;
  }

  public String getJobDesc() {
    return jobDesc;
  }

  public void setJobDesc(String jobDesc) {
    this.jobDesc = jobDesc;
  }

  public String getDeployMode() {
    return deployMode;
  }

  public void setDeployMode(String deployMode) {
    this.deployMode = deployMode;
  }

  public String getJobId() {
    return jobId;
  }

  public void setJobId(String jobId) {
    this.jobId = jobId;
  }

  public String getRemoteLogUrl() {
    return remoteLogUrl;
  }

  public void setRemoteLogUrl(String remoteLogUrl) {
    this.remoteLogUrl = remoteLogUrl;
  }

  public Date getStartTime() {
    return startTime;
  }

  public void setStartTime(Date startTime) {
    this.startTime = startTime;
  }

  public Date getEndTime() {
    return endTime;
  }

  public void setEndTime(Date endTime) {
    this.endTime = endTime;
  }

  public String getJobStatus() {
    return jobStatus;
  }

  public void setJobStatus(String jobStatus) {
    this.jobStatus = jobStatus;
  }

  public long getCreator() {
    return creator;
  }

  public void setCreator(long creator) {
    this.creator = creator;
  }

  public long getEditor() {
    return editor;
  }

  public void setEditor(long editor) {
    this.editor = editor;
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

  public String getLocalLog() {
    return localLog;
  }

  public void setLocalLog(String localLog) {
    this.localLog = localLog;
  }

  public String getRunIp() {
    return runIp;
  }

  public void setRunIp(String runIp) {
    this.runIp = runIp;
  }
}
