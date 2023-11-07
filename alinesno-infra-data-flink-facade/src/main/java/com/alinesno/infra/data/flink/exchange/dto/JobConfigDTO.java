package com.alinesno.infra.data.flink.exchange.dto;

import com.alinesno.infra.data.flink.commom.constant.SystemConstant;
import com.alinesno.infra.data.flink.commom.enums.JobTypeEnum;
import com.alinesno.infra.data.flink.entity.JobConfigEntity;
import com.alinesno.infra.data.flink.exchange.common.util.MatcherUtils;
import com.alinesno.infra.data.flink.exchange.enums.AlarmTypeEnum;
import com.alinesno.infra.data.flink.exchange.enums.DeployModeEnum;
import com.alinesno.infra.data.flink.exchange.enums.JobConfigStatus;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @author zhp
 * @date 2020-07-10
 * @time 01:46
 */

public class JobConfigDTO implements Serializable {

  private static final long serialVersionUID = 1L;

  private Long id;

  /**
   * 任务名称
   */
  private String jobName;

  /**
   * 任务描述
   */
  private String jobDesc;

  /**
   * flink的模式
   */
  private DeployModeEnum deployModeEnum;

  /**
   * flink运行配置
   */
  private String flinkRunConfig;

  /**
   * checkpointConfig 配置
   */
  private String flinkCheckpointConfig;

  /**
   * flink运行配置
   */
  private String jobId;

  /**
   * 1:开启 0: 关闭
   */
  private Integer isOpen;

  /**
   * @see JobConfigStatus 1:运行中 0: 停止中 -1:运行失败
   */
  private JobConfigStatus status;


  /**
   * 二方jar udf、 连接器 等jar如http://xxx.xxx.com/flink-streaming-udf.jar
   */
  private String extJarPath;

  /**
   * 最后一次启动时间
   */
  private Date lastStartTime;

  /**
   * 更新版本号 用于乐观锁
   */
  private Long version;

  /**
   * sql语句
   */
  private String flinkSql;


  /**
   * 任务类型
   */
  private JobTypeEnum jobTypeEnum;

  /**
   * 启动jar可能需要使用的自定义参数
   */
  private String customArgs;

  /**
   * 程序入口类
   */
  private String customMainClass;

  /**
   * 自定义jar的http地址 如:http://ccblog.cn/xx.jar
   */
  private String customJarUrl;

  private List<Integer> alarmTypes;

  private List<AlarmTypeEnum> alarmTypeEnumList;

  private String lastRunLogId;


  /**
   * 创建时间
   */
  private Date createTime;

  /**
   * 修改时间
   */
  private Date editTime;

  private long creator;

  private long editor;

  private String flinkRunUrl;

  private String alarmStrs;

  private int isDeleted;

  private String downloadUrl;

  /**
   * cron表达式
   */
  private String cron;

  private int jobType ;


  public List<String> getExtJarPathUrl() {
    if (extJarPath == null) {
      return Collections.emptyList();
    }
    List<String> urlJarsList = Lists.newArrayList();
    String[] urls = extJarPath.split(SystemConstant.LINE_FEED);
    for (String url : urls) {
      if (StringUtils.isEmpty(url)) {
        continue;
      }
      if (MatcherUtils.isHttpsOrHttp(url)) {
        urlJarsList.add(url.trim());
      } else {
        urlJarsList.add(downloadUrl + SystemConstant.VIRGULE  + url.trim());
      }
    }
    return urlJarsList;
  }

  public static JobConfigEntity toEntity(JobConfigDTO jobConfigDTO) {
    if (jobConfigDTO == null) {
      return null;
    }
    JobConfigEntity jobConfig = new JobConfigEntity();
    jobConfig.setId(jobConfigDTO.getId());
    jobConfig.setJobName(jobConfigDTO.getJobName());
    jobConfig.setJobDesc(jobConfigDTO.getJobDesc());
    if (jobConfigDTO.getDeployModeEnum() != null) {
      jobConfig.setDeployMode(jobConfigDTO.getDeployModeEnum().name());
    }
    jobConfig.setFlinkRunConfig(jobConfigDTO.getFlinkRunConfig());
    jobConfig.setFlinkCheckpointConfig(jobConfigDTO.getFlinkCheckpointConfig());
    jobConfig.setJobId(jobConfigDTO.getJobId());
    jobConfig.setIsOpen(jobConfigDTO.getIsOpen());
    jobConfig.setStatus(jobConfigDTO.getStatus().getCode());
    jobConfig.setLastStartTime(jobConfigDTO.getLastStartTime());
    jobConfig.setVersion(jobConfigDTO.getVersion());
    jobConfig.setFlinkSql(jobConfigDTO.getFlinkSql());
    jobConfig.setAddTime(jobConfigDTO.getCreateTime());
    jobConfig.setUpdateTime(jobConfigDTO.getEditTime());
    jobConfig.setOperatorId(jobConfigDTO.getCreator());
    jobConfig.setLastUpdateOperatorId(jobConfigDTO.getEditor());
    jobConfig.setLastRunLogId(jobConfigDTO.getLastRunLogId());
    jobConfig.setExtJarPath(jobConfigDTO.getExtJarPath());
    jobConfig.setCron(jobConfigDTO.getCron());

    if (jobConfigDTO.getJobTypeEnum() != null) {
      jobConfig.setJobType(jobConfigDTO.getJobTypeEnum().getCode());
    }
    jobConfig.setCustomArgs(jobConfigDTO.getCustomArgs());
    jobConfig.setCustomMainClass(jobConfigDTO.getCustomMainClass());
    jobConfig.setCustomJarUrl(jobConfigDTO.getCustomJarUrl());
    jobConfig.setHasDelete(jobConfigDTO.getIsDeleted());
    return jobConfig;
  }

  public static JobConfigDTO toDTO(JobConfigEntity jobConfig) {
    if (jobConfig == null) {
      return null;
    }
    JobConfigDTO jobConfigDTO = new JobConfigDTO();
    jobConfigDTO.setId(jobConfig.getId());
    jobConfigDTO.setJobName(jobConfig.getJobName());
    jobConfigDTO.setJobDesc(jobConfig.getJobDesc());
    jobConfigDTO.setDeployModeEnum(DeployModeEnum.getModel(jobConfig.getDeployMode()));
    jobConfigDTO.setFlinkRunConfig(jobConfig.getFlinkRunConfig());
    jobConfigDTO.setFlinkCheckpointConfig(jobConfig.getFlinkCheckpointConfig());
    jobConfigDTO.setJobId(jobConfig.getJobId());
    jobConfigDTO.setIsOpen(jobConfig.getIsOpen());
    jobConfigDTO.setStatus(JobConfigStatus.getJobConfigStatus(jobConfig.getStatus()));
    jobConfigDTO.setLastStartTime(jobConfig.getLastStartTime());
    jobConfigDTO.setVersion(jobConfig.getVersion());
    jobConfigDTO.setCreateTime(jobConfig.getAddTime());
    jobConfigDTO.setEditTime(jobConfig.getUpdateTime());
    jobConfigDTO.setCreator(jobConfig.getOperatorId());
    jobConfigDTO.setEditor(jobConfig.getLastUpdateOperatorId());
    jobConfigDTO.setFlinkSql(jobConfig.getFlinkSql());
    jobConfigDTO.setLastRunLogId(jobConfig.getLastRunLogId());
    jobConfigDTO.setExtJarPath(jobConfig.getExtJarPath());
    jobConfigDTO.setCron(jobConfig.getCron());
    jobConfigDTO.setJobTypeEnum(JobTypeEnum.getJobTypeEnum(jobConfig.getJobType()));
    jobConfigDTO.setCustomArgs(jobConfig.getCustomArgs());
    jobConfigDTO.setCustomMainClass(jobConfig.getCustomMainClass());
    jobConfigDTO.setCustomJarUrl(jobConfig.getCustomJarUrl());
    jobConfigDTO.setIsDeleted(jobConfig.getHasDelete());
    jobConfigDTO.setJobType(jobConfig.getJobType());
    return jobConfigDTO;
  }

  public static List<JobConfigDTO> toListDTO(List<JobConfigEntity> jobConfigList) {
    if (CollectionUtils.isEmpty(jobConfigList)) {
      return Collections.emptyList();
    }

    List<JobConfigDTO> jobConfigDTOList = new ArrayList<JobConfigDTO>();

    for (JobConfigEntity jobConfig : jobConfigList) {
      jobConfigDTOList.add(toDTO(jobConfig));
    }

    return jobConfigDTOList;
  }

  public static String buildRunName(String jobName) {

    return "flink@" + jobName;
  }

  public static JobConfigDTO bulidStop(long id) {
    JobConfigDTO jobConfig = new JobConfigDTO();
    jobConfig.setStatus(JobConfigStatus.STOP);
//    jobConfig.setEditor("sys_auto");
    jobConfig.setId(id);
    // jobConfig.setJobId("");
    return jobConfig;
  }


  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
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

  public DeployModeEnum getDeployModeEnum() {
    return deployModeEnum;
  }

  public void setDeployModeEnum(DeployModeEnum deployModeEnum) {
    this.deployModeEnum = deployModeEnum;
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

  public String getJobId() {
    return jobId;
  }

  public void setJobId(String jobId) {
    this.jobId = jobId;
  }

  public Integer getIsOpen() {
    return isOpen;
  }

  public void setIsOpen(Integer isOpen) {
    this.isOpen = isOpen;
  }

  public JobConfigStatus getStatus() {
    return status;
  }

  public void setStatus(JobConfigStatus status) {
    this.status = status;
  }

  public String getExtJarPath() {
    return extJarPath;
  }

  public void setExtJarPath(String extJarPath) {
    this.extJarPath = extJarPath;
  }

  public Date getLastStartTime() {
    return lastStartTime;
  }

  public void setLastStartTime(Date lastStartTime) {
    this.lastStartTime = lastStartTime;
  }

  public Long getVersion() {
    return version;
  }

  public void setVersion(Long version) {
    this.version = version;
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

  public String getCustomArgs() {
    return customArgs;
  }

  public void setCustomArgs(String customArgs) {
    this.customArgs = customArgs;
  }

  public String getCustomMainClass() {
    return customMainClass;
  }

  public void setCustomMainClass(String customMainClass) {
    this.customMainClass = customMainClass;
  }

  public String getCustomJarUrl() {
    return customJarUrl;
  }

  public void setCustomJarUrl(String customJarUrl) {
    this.customJarUrl = customJarUrl;
  }

  public List<Integer> getAlarmTypes() {
    return alarmTypes;
  }

  public void setAlarmTypes(List<Integer> alarmTypes) {
    this.alarmTypes = alarmTypes;
  }

  public List<AlarmTypeEnum> getAlarmTypeEnumList() {
    return alarmTypeEnumList;
  }

  public void setAlarmTypeEnumList(List<AlarmTypeEnum> alarmTypeEnumList) {
    this.alarmTypeEnumList = alarmTypeEnumList;
  }

  public String getLastRunLogId() {
    return lastRunLogId;
  }

  public void setLastRunLogId(String lastRunLogId) {
    this.lastRunLogId = lastRunLogId;
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

  public String getFlinkRunUrl() {
    return flinkRunUrl;
  }

  public void setFlinkRunUrl(String flinkRunUrl) {
    this.flinkRunUrl = flinkRunUrl;
  }

  public String getAlarmStrs() {
    return alarmStrs;
  }

  public void setAlarmStrs(String alarmStrs) {
    this.alarmStrs = alarmStrs;
  }

  public Integer getIsDeleted() {
    return isDeleted;
  }

  public void setIsDeleted(Integer isDeleted) {
    this.isDeleted = isDeleted;
  }

  public String getDownloadUrl() {
    return downloadUrl;
  }

  public void setDownloadUrl(String downloadUrl) {
    this.downloadUrl = downloadUrl;
  }

  public String getCron() {
    return cron;
  }

  public void setCron(String cron) {
    this.cron = cron;
  }

  public void setIsDeleted(int isDeleted) {
    this.isDeleted = isDeleted;
  }

  public int getJobType() {
    return jobType;
  }

  public void setJobType(int jobType) {
    this.jobType = jobType;
  }
}
