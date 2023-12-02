package com.alinesno.infra.data.stream.exchange.param;


import com.alinesno.infra.data.stream.commom.constant.SystemConstant;
import com.alinesno.infra.data.stream.commom.enums.JobTypeEnum;
import com.alinesno.infra.data.stream.exchange.dto.JobConfigDTO;
import com.alinesno.infra.data.stream.exchange.enums.AlarmTypeEnum;
import com.alinesno.infra.data.stream.exchange.enums.DeployModeEnum;
import com.alinesno.infra.data.stream.exchange.enums.JobConfigStatus;
import com.alinesno.infra.data.stream.exchange.enums.YN;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhp
 * @Description:
 * @date 2020-08-06
 * @time 22:22
 */

public class UpsertJobConfigParam {

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
   *
   */
  private String deployMode;

  /**
   * flink运行配置
   */
  private String flinkRunConfig;

  /**
   * Checkpoint信息
   */
  private String flinkCheckpointConfig;


  /**
   * sql语句
   */
  private String flinkSql;


  /**
   * 二方jar udf、 连接器 等jar如http://xxx.xxx.com/flink-streaming-udf.jar
   */
  private String extJarPath;


  private String alarmTypes;


  /**
   * 任务类型 0:sql 1:自定义jar'
   */
  private Integer jobType;

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


  /**
   * 1:开启 0: 关闭
   */
  private Integer isOpen = YN.N.getValue();


  private Integer status = JobConfigStatus.STOP.getCode();

  private String cron;

  private Long operatorId;

  private Long creator;



  public static JobConfigDTO toDTO(UpsertJobConfigParam upsertJobConfigParam) {
    if (upsertJobConfigParam == null) {
      return null;
    }
    JobConfigDTO jobConfigDTO = new JobConfigDTO();
    jobConfigDTO.setId(upsertJobConfigParam.getId());
    jobConfigDTO.setDeployModeEnum(DeployModeEnum.getModel(upsertJobConfigParam.getDeployMode()));
    jobConfigDTO.setJobName(upsertJobConfigParam.getJobName());
    jobConfigDTO.setJobDesc(upsertJobConfigParam.getJobDesc());
    jobConfigDTO.setFlinkRunConfig(upsertJobConfigParam.getFlinkRunConfig());
    jobConfigDTO.setFlinkCheckpointConfig(upsertJobConfigParam.getFlinkCheckpointConfig());

    if (StringUtils.isNotEmpty(upsertJobConfigParam.getFlinkSql())
        && (JobTypeEnum.SQL_STREAMING.getCode() == upsertJobConfigParam.getJobType().intValue()
        || JobTypeEnum.SQL_BATCH.getCode() == upsertJobConfigParam.getJobType().intValue())) {
      jobConfigDTO.setFlinkSql(upsertJobConfigParam.getFlinkSql());
    } else {
      jobConfigDTO.setFlinkSql(SystemConstant.SPACE);
    }

    jobConfigDTO.setJobTypeEnum(JobTypeEnum.getJobTypeEnum(upsertJobConfigParam.getJobType()));
    jobConfigDTO.setCustomArgs(upsertJobConfigParam.getCustomArgs());
    jobConfigDTO.setCustomMainClass(upsertJobConfigParam.getCustomMainClass());
    jobConfigDTO.setCustomJarUrl(upsertJobConfigParam.getCustomJarUrl());

    jobConfigDTO.setIsOpen(upsertJobConfigParam.getIsOpen());
    jobConfigDTO.setStatus(JobConfigStatus.getJobConfigStatus(upsertJobConfigParam.getStatus()));
    if (StringUtils.isNotEmpty(upsertJobConfigParam.getExtJarPath())) {
      jobConfigDTO.setExtJarPath(upsertJobConfigParam.getExtJarPath().trim());
    } else {
      jobConfigDTO.setExtJarPath(SystemConstant.SPACE);
    }
    jobConfigDTO.setCron(upsertJobConfigParam.getCron());

    if (StringUtils.isNotEmpty(upsertJobConfigParam.getAlarmTypes())) {
      List<AlarmTypeEnum> list = new ArrayList<>();
      String[] types = upsertJobConfigParam.getAlarmTypes().split(",");
      for (String code : types) {
        AlarmTypeEnum alarmTypeEnum = AlarmTypeEnum.getAlarmTypeEnum(Integer.valueOf(code));
        if (alarmTypeEnum != null) {
          list.add(alarmTypeEnum);
        }
      }
      if (CollectionUtils.isNotEmpty(list)) {
        jobConfigDTO.setAlarmTypeEnumList(list);
      }
    }

    jobConfigDTO.setCreator(upsertJobConfigParam.getOperatorId());

    return jobConfigDTO;
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

  public String getFlinkSql() {
    return flinkSql;
  }

  public void setFlinkSql(String flinkSql) {
    this.flinkSql = flinkSql;
  }

  public String getExtJarPath() {
    return extJarPath;
  }

  public void setExtJarPath(String extJarPath) {
    this.extJarPath = extJarPath;
  }

  public String getAlarmTypes() {
    return alarmTypes;
  }

  public void setAlarmTypes(String alarmTypes) {
    this.alarmTypes = alarmTypes;
  }

  public Integer getJobType() {
    return jobType;
  }

  public void setJobType(Integer jobType) {
    this.jobType = jobType;
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

  public Integer getIsOpen() {
    return isOpen;
  }

  public void setIsOpen(Integer isOpen) {
    this.isOpen = isOpen;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public String getCron() {
    return cron;
  }

  public void setCron(String cron) {
    this.cron = cron;
  }

  public Long getOperatorId() {
    return operatorId;
  }

  public void setOperatorId(Long operatorId) {
    this.operatorId = operatorId;
  }

  public Long getCreator() {
    return creator;
  }

  public void setCreator(Long creator) {
    this.creator = creator;
  }
}
