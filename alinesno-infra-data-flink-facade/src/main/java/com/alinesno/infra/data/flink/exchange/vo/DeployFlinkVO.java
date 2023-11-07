package com.alinesno.infra.data.flink.exchange.vo;

import com.alinesno.infra.data.flink.commom.enums.JobTypeEnum;
import com.alinesno.infra.data.flink.exchange.enums.AlarmTypeEnum;
import com.alinesno.infra.data.flink.exchange.enums.DeployModeEnum;

import java.util.List;

public class DeployFlinkVO {


  public static class FlinkTask {

    /**
     * 任务编号
     */
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
     * sql语句
     */
    private String sqlFile;

    /**
     * 任务类型：SQL_STREAMING, JAR, SQL_BATCH; 为空默认为：SQL_STREAMING
     */
    private JobTypeEnum jobType;

    /**
     * 运行模式：YARN_PER, STANDALONE, LOCAL; 为空默认为：STANDALONE
     */
    private DeployModeEnum deployMode;

    private String flinkRunConfig;

    private String flinkCheckpointConfig;

    private String extJarPath;

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
     * 钉钉告警、回调、自动重启（DINGDING、CALLBACK_URL、AUTO_START_JOB）
     */
    private List<AlarmTypeEnum> alarmTypes;

    /**
     * 发布启动标记：默认为true，表示发布时自动启动
     */
    private Boolean deployStartFlag;

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

    public String getSqlFile() {
      return sqlFile;
    }

    public void setSqlFile(String sqlFile) {
      this.sqlFile = sqlFile;
    }

    public JobTypeEnum getJobType() {
      return jobType;
    }

    public void setJobType(JobTypeEnum jobType) {
      this.jobType = jobType;
    }

    public DeployModeEnum getDeployMode() {
      return deployMode;
    }

    public void setDeployMode(DeployModeEnum deployMode) {
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

    public List<AlarmTypeEnum> getAlarmTypes() {
      return alarmTypes;
    }

    public void setAlarmTypes(List<AlarmTypeEnum> alarmTypes) {
      this.alarmTypes = alarmTypes;
    }

    public Boolean getDeployStartFlag() {
      return deployStartFlag;
    }

    public void setDeployStartFlag(Boolean deployStartFlag) {
      this.deployStartFlag = deployStartFlag;
    }
  }

  private List<FlinkTask> taskList;

  public List<FlinkTask> getTaskList() {
    return taskList;
  }

  public void setTaskList(List<FlinkTask> taskList) {
    this.taskList = taskList;
  }
}
