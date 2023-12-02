package com.alinesno.infra.data.stream.exchange.enums;


/**
 * @author zhp
 * @Description:
 * @date 2021/2/27
 * @time 11:05
 */

public enum AlarmTypeEnum {
  DINGDING(1, "钉钉告警"),
  CALLBACK_URL(2, "回调http告警"),
  AUTO_START_JOB(3, "任务退出自动拉起");


  private int code;

  private String desc;

  AlarmTypeEnum(int code, String desc) {
    this.code = code;
    this.desc = desc;
  }

  public static AlarmTypeEnum getAlarmTypeEnum(Integer code) {
    if (code == null) {
      return null;
    }

    for (AlarmTypeEnum alarMLogTypeEnum : AlarmTypeEnum.values()) {
      if (alarMLogTypeEnum.getCode() == code.intValue()) {
        return alarMLogTypeEnum;
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

  public String getDesc() {
    return desc;
  }

  public void setDesc(String desc) {
    this.desc = desc;
  }
}
