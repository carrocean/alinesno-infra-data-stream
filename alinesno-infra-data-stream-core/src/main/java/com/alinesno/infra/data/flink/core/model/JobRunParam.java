package com.alinesno.infra.data.flink.core.model;

import com.alinesno.infra.data.flink.commom.enums.JobTypeEnum;
import com.alinesno.infra.data.flink.commom.model.CheckPointParam;

/**
 * @author zhp
 * @Description:
 * @date 2020-08-21
 * @time 02:10
 */
public class JobRunParam {

  /**
   * sql语句目录
   */
  private String sqlPath;

  /**
   * 任务类型
   */
  private JobTypeEnum jobTypeEnum;

  /**
   * CheckPoint 参数
   */
  private CheckPointParam checkPointParam;

  public String getSqlPath() {
    return sqlPath;
  }

  public void setSqlPath(String sqlPath) {
    this.sqlPath = sqlPath;
  }

  public JobTypeEnum getJobTypeEnum() {
    return jobTypeEnum;
  }

  public void setJobTypeEnum(JobTypeEnum jobTypeEnum) {
    this.jobTypeEnum = jobTypeEnum;
  }

  public CheckPointParam getCheckPointParam() {
    return checkPointParam;
  }

  public void setCheckPointParam(CheckPointParam checkPointParam) {
    this.checkPointParam = checkPointParam;
  }
}
