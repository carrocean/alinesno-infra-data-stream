package com.alinesno.infra.data.stream.exchange.dto;

import cn.hutool.core.collection.CollectionUtil;
import com.alinesno.infra.data.stream.commom.enums.JobTypeEnum;
import com.alinesno.infra.data.stream.entity.JobConfigEntity;
import com.alinesno.infra.data.stream.entity.JobConfigHistoryEntity;
import org.apache.commons.compress.utils.Lists;

import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @author zhp
 * @date 2021/5/5
 * @time 19:49
 */
public class JobConfigHistoryDTO implements Serializable {

  private static final long serialVersionUID = 1L;

  private Long id;

  /**
   * job_config主表Id
   */
  private Long jobConfigId;

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
   * flink运行配置
   */
  private String flinkRunConfig;

  /**
   * checkPoint配置
   */
  private String flinkCheckpointConfig;

  /**
   * udf地址及连接器jar 如http://xxx.xxx.com/flink-streaming-udf.jar
   */
  private String extJarPath;

  /**
   * 更新版本号
   */
  private Long version;

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

  /**
   * sql语句
   */
  private String flinkSql;

  /**
   * 任务类型
   */
  private JobTypeEnum jobTypeEnum;

  private String cron;


  public static JobConfigHistoryEntity toEntity(JobConfigHistoryDTO jobConfigHistoryDTO) {
    if (jobConfigHistoryDTO == null) {
      return null;
    }
    JobConfigHistoryEntity jobConfigHistory = new JobConfigHistoryEntity();
    jobConfigHistory.setId(jobConfigHistoryDTO.getId());
    jobConfigHistory.setJobConfigId(jobConfigHistoryDTO.getJobConfigId());
    jobConfigHistory.setJobName(jobConfigHistoryDTO.getJobName());
    jobConfigHistory.setJobDesc(jobConfigHistoryDTO.getJobDesc());
    jobConfigHistory.setDeployMode(jobConfigHistoryDTO.getDeployMode());
    jobConfigHistory.setFlinkRunConfig(jobConfigHistoryDTO.getFlinkRunConfig());
    jobConfigHistory.setFlinkCheckpointConfig(jobConfigHistoryDTO.getFlinkCheckpointConfig());
    jobConfigHistory.setExtJarPath(jobConfigHistoryDTO.getExtJarPath());
    jobConfigHistory.setVersion(jobConfigHistoryDTO.getVersion());
    jobConfigHistory.setAddTime(jobConfigHistoryDTO.getCreateTime());
    jobConfigHistory.setUpdateTime(jobConfigHistoryDTO.getEditTime());
    jobConfigHistory.setOperatorId(jobConfigHistoryDTO.getCreator());
    jobConfigHistory.setLastUpdateOperatorId(jobConfigHistoryDTO.getEditor());
    jobConfigHistory.setFlinkSql(jobConfigHistoryDTO.getFlinkSql());
    jobConfigHistory.setCron(jobConfigHistoryDTO.getCron());
    if (jobConfigHistoryDTO.getJobTypeEnum() != null) {
      jobConfigHistory.setJobType(jobConfigHistoryDTO.getJobTypeEnum().getCode());
    }
    return jobConfigHistory;
  }


  public static JobConfigHistoryDTO toDTO(JobConfigHistoryEntity jobConfigHistory) {
    if (jobConfigHistory == null) {
      return null;
    }
    JobConfigHistoryDTO jobConfigHistoryDTO = new JobConfigHistoryDTO();
    jobConfigHistoryDTO.setId(jobConfigHistory.getId());
    jobConfigHistoryDTO.setJobConfigId(jobConfigHistory.getJobConfigId());
    jobConfigHistoryDTO.setJobName(jobConfigHistory.getJobName());
    jobConfigHistoryDTO.setJobDesc(jobConfigHistory.getJobDesc());
    jobConfigHistoryDTO.setDeployMode(jobConfigHistory.getDeployMode());
    jobConfigHistoryDTO.setFlinkRunConfig(jobConfigHistory.getFlinkRunConfig());
    jobConfigHistoryDTO.setFlinkCheckpointConfig(jobConfigHistory.getFlinkCheckpointConfig());
    jobConfigHistoryDTO.setExtJarPath(jobConfigHistory.getExtJarPath());
    jobConfigHistoryDTO.setVersion(jobConfigHistory.getVersion());
    jobConfigHistoryDTO.setCreateTime(jobConfigHistory.getAddTime());
    jobConfigHistoryDTO.setEditTime(jobConfigHistory.getUpdateTime());
    jobConfigHistoryDTO.setCreator(jobConfigHistory.getOperatorId());
    jobConfigHistoryDTO.setEditor(jobConfigHistory.getLastUpdateOperatorId());
    jobConfigHistoryDTO.setFlinkSql(jobConfigHistory.getFlinkSql());
    jobConfigHistoryDTO.setCron(jobConfigHistory.getCron());
    jobConfigHistoryDTO.setJobTypeEnum(JobTypeEnum.getJobTypeEnum(jobConfigHistory.getJobType()));
    return jobConfigHistoryDTO;
  }

  public static List<JobConfigHistoryDTO> toListDTO(List<JobConfigHistoryEntity> jobConfigHistoryList) {
    if (CollectionUtil.isEmpty(jobConfigHistoryList)) {
      return Collections.EMPTY_LIST;
    }

    List<JobConfigHistoryDTO> list = Lists.newArrayList();

    for (JobConfigHistoryEntity jobConfigHistory : jobConfigHistoryList) {

      JobConfigHistoryDTO jobConfigHistoryDTO = JobConfigHistoryDTO.toDTO(jobConfigHistory);
      if (jobConfigHistoryDTO != null) {
        list.add(jobConfigHistoryDTO);
      }
    }

    return list;
  }


  public static JobConfigHistoryDTO to(JobConfigEntity jobConfig) {
    if (jobConfig == null) {
      return null;
    }
    JobConfigHistoryDTO jobConfigHistoryDTO = new JobConfigHistoryDTO();
    jobConfigHistoryDTO.setJobConfigId(jobConfig.getId());
    jobConfigHistoryDTO.setJobName(jobConfig.getJobName());
    jobConfigHistoryDTO.setJobDesc(jobConfig.getJobDesc());
    jobConfigHistoryDTO.setDeployMode(jobConfig.getDeployMode());
    jobConfigHistoryDTO.setFlinkRunConfig(jobConfig.getFlinkRunConfig());
    jobConfigHistoryDTO.setFlinkCheckpointConfig(jobConfig.getFlinkCheckpointConfig());
    jobConfigHistoryDTO.setExtJarPath(jobConfig.getExtJarPath());
    jobConfigHistoryDTO.setVersion(jobConfig.getVersion());
    jobConfigHistoryDTO.setCreateTime(jobConfig.getAddTime());
    jobConfigHistoryDTO.setEditTime(jobConfig.getUpdateTime());
    jobConfigHistoryDTO.setCreator(jobConfig.getOperatorId());
    jobConfigHistoryDTO.setEditor(jobConfig.getLastUpdateOperatorId());
    jobConfigHistoryDTO.setFlinkSql(jobConfig.getFlinkSql());
    jobConfigHistoryDTO.setCron(jobConfig.getCron());
    jobConfigHistoryDTO.setJobTypeEnum(JobTypeEnum.getJobTypeEnum(jobConfig.getJobType()));
    return jobConfigHistoryDTO;
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

  public String getFlinkRunConfig() {
    return flinkRunConfig;
  }

  public void setFlinkRunConfig(String flinkRunConfig) {
    this.flinkRunConfig = flinkRunConfig;
  }

  public String getFlinkCheckpointConfig() {
    return flinkCheckpointConfig;
  }

  public void setFlinkCheckpointConfig(String flinkCheckpointConfig) {
    this.flinkCheckpointConfig = flinkCheckpointConfig;
  }

  public String getExtJarPath() {
    return extJarPath;
  }

  public void setExtJarPath(String extJarPath) {
    this.extJarPath = extJarPath;
  }

  public Long getVersion() {
    return version;
  }

  public void setVersion(Long version) {
    this.version = version;
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

  public String getFlinkSql() {
    return flinkSql;
  }

  public void setFlinkSql(String flinkSql) {
    this.flinkSql = flinkSql;
  }

  public JobTypeEnum getJobTypeEnum() {
    return jobTypeEnum;
  }

  public void setJobTypeEnum(JobTypeEnum jobTypeEnum) {
    this.jobTypeEnum = jobTypeEnum;
  }

  public String getCron() {
    return cron;
  }

  public void setCron(String cron) {
    this.cron = cron;
  }
}
