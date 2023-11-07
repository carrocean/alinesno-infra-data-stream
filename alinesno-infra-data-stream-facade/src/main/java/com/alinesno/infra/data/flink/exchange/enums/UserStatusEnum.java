package com.alinesno.infra.data.flink.exchange.enums;

/**
 * @author zhp
 * @Description:
 * @date 2020-07-10
 * @time 00:47
 */
public enum UserStatusEnum {

  CLOSE(0, "停用"),

  OPEN(1, "启用"),

  UNKNOWN(-1, "未知");

  private Integer code;

  private String desc;

  UserStatusEnum(Integer code, String desc) {
    this.code = code;
    this.desc = desc;
  }

  public static UserStatusEnum getStatus(Integer code) {
    for (UserStatusEnum userStatusEnum : UserStatusEnum.values()) {
      if (userStatusEnum.getCode().equals(code)) {
        return userStatusEnum;
      }
    }
    return UserStatusEnum.UNKNOWN;
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
