package com.alinesno.infra.data.stream.exchange.param;

import com.alinesno.infra.data.stream.exchange.page.PageParam;


/**
 * @author zhp
 * @Description:
 * @date 2020-07-15
 * @time 02:04
 */
public class JobConfigParam extends PageParam {

  /**
   * @author zhp
   * @date 2020-07-15
   * @time 02:07
   * @see com.alinesno.infra.data.stream.exchange.enums.JobConfigStatus
   */
  private Integer status;


  /**
   * 任务名称
   */
  private String jobName;


  /**
   * 任务类型 0:sql 1:自定义jar'
   */
  private Integer jobType;


  private Integer open;

  private String jobId;

  private String deployMode;

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public String getJobName() {
    return jobName;
  }

  public void setJobName(String jobName) {
    this.jobName = jobName;
  }

  public Integer getJobType() {
    return jobType;
  }

  public void setJobType(Integer jobType) {
    this.jobType = jobType;
  }

  public Integer getOpen() {
    return open;
  }

  public void setOpen(Integer open) {
    this.open = open;
  }

  public String getJobId() {
    return jobId;
  }

  public void setJobId(String jobId) {
    this.jobId = jobId;
  }

  public String getDeployMode() {
    return deployMode;
  }

  public void setDeployMode(String deployMode) {
    this.deployMode = deployMode;
  }
}
