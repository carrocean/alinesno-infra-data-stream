package com.alinesno.infra.data.flink.exchange.vo;

import com.alinesno.infra.data.flink.exchange.dto.JobConfigDTO;

/**
 * @author zhp
 * @Description:
 * @date 2021/2/21
 * @time 22:20
 */

public class CallbackDTO {

  private String appId;

  private String jobName;

  private String deployMode;

  private long jobConfigId;

  private long operatorId;


  public static CallbackDTO to(JobConfigDTO jobConfigDTO) {
    CallbackDTO callbackDTO = new CallbackDTO();
    callbackDTO.setJobConfigId(jobConfigDTO.getId());
    callbackDTO.setJobName(jobConfigDTO.getJobName());
    callbackDTO.setDeployMode(jobConfigDTO.getDeployModeEnum().name());
    callbackDTO.setAppId(jobConfigDTO.getJobId());
    callbackDTO.setOperatorId(jobConfigDTO.getCreator());
    return callbackDTO;
  }

  public String getAppId() {
    return appId;
  }

  public void setAppId(String appId) {
    this.appId = appId;
  }

  public String getJobName() {
    return jobName;
  }

  public void setJobName(String jobName) {
    this.jobName = jobName;
  }

  public String getDeployMode() {
    return deployMode;
  }

  public void setDeployMode(String deployMode) {
    this.deployMode = deployMode;
  }

  public long getJobConfigId() {
    return jobConfigId;
  }

  public void setJobConfigId(long jobConfigId) {
    this.jobConfigId = jobConfigId;
  }

  public long getOperatorId() {
    return operatorId;
  }

  public void setOperatorId(long operatorId) {
    this.operatorId = operatorId;
  }
}
