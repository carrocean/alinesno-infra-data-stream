package com.alinesno.infra.data.stream.commom.enums;



/**
 * @author zhp
 * @Description:
 * @date 2021/3/28
 * @time 11:14
 */
public enum JobTypeEnum {

  SQL_STREAMING(0), JAR(1), SQL_BATCH(2);

  private int code;

  JobTypeEnum(int code) {
    this.code = code;
  }

  public static JobTypeEnum getJobTypeEnum(Integer code) {
    if (code == null) {
      return null;
    }
    for (JobTypeEnum jobTypeEnum : JobTypeEnum.values()) {
      if (code == jobTypeEnum.getCode()) {
        return jobTypeEnum;
      }
    }

    return null;
  }

  public int getCode() {
    return code;
  }

  public void setCode(int code) {
    this.code = code;
  }
}
