package com.alinesno.infra.data.stream.exchange.enums;

/**
 * @author zhp
 * @Description:
 * @date 2020-07-10
 * @time 00:59
 */
public enum JobConfigStatus {

  FAIL(-1, "失败"),

  RUN(1, "运行中"),

  STOP(0, "停止"),

  STARTING(2, "启动中"),

  SUCCESS(3, "提交成功"),

  UNKNOWN(-2, "未知");

  private Integer code;

  private String desc;

  JobConfigStatus(Integer code, String desc) {
    this.code = code;
    this.desc = desc;
  }

  public static JobConfigStatus getJobConfigStatus(Integer code) {
    for (JobConfigStatus jobConfigStatus : JobConfigStatus.values()) {
      if (jobConfigStatus.getCode().equals(code)) {
        return jobConfigStatus;
      }
    }
    return JobConfigStatus.UNKNOWN;
  }

  public Integer getCode() {
    return code;
  }

  public void setCode(Integer code) {
    this.code = code;
  }

  public String getDesc() {
    return desc;
  }

  public void setDesc(String desc) {
    this.desc = desc;
  }

}
