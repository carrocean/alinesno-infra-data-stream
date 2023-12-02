package com.alinesno.infra.data.stream.exchange.enums;

/**
 * @author zhp
 * @Description:
 * @date 2020-09-25
 * @time 21:45
 */
public enum AlarmLogTypeEnum {

  DINGDING(1, "钉钉"),
  CALLBACK_URL(2, "自定义回调http"),
  OTHER(3, "其他");

  private int code;

  private String desc;

  AlarmLogTypeEnum(int code, String desc) {
    this.code = code;
    this.desc = desc;
  }

  public static AlarmLogTypeEnum getAlarmLogTypeEnum(Integer code) {
    if (code == null) {
      return null;
    }

    for (AlarmLogTypeEnum alarMLogTypeEnum : AlarmLogTypeEnum.values()) {
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
