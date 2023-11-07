package com.alinesno.infra.data.flink.exchange.enums;

/**
 * @author zhp
 * @Description:
 * @date 2020-09-25
 * @time 21:45
 */
public enum AlarmLogStatusEnum {
  SUCCESS(1, "成功"),
  FAIL(0, "失败");

  private int code;


  private String desc;


  AlarmLogStatusEnum(int code, String desc) {
    this.code = code;
    this.desc = desc;
  }

  public static AlarmLogStatusEnum getAlarmLogStatusEnum(Integer code) {
    if (code == null) {
      return null;
    }

    for (AlarmLogStatusEnum alarmLogStatusEnum : AlarmLogStatusEnum.values()) {
      if (alarmLogStatusEnum.getCode() == code.intValue()) {
        return alarmLogStatusEnum;
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
