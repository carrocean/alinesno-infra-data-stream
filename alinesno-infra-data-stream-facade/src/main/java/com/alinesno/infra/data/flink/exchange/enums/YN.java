package com.alinesno.infra.data.flink.exchange.enums;

/**
 * @author zhp
 * @Description:
 * @date 2015/7/11
 * @time 上午10:49
 */
public enum YN {
  Y(Boolean.TRUE, 1, "是"), N(Boolean.FALSE, 0, "否");

  private Boolean code;
  private int value;
  private String describe;

  YN(Boolean code, int value, String describe) {
    this.code = code;
    this.value = value;
    this.describe = describe;
  }

  public static YN getYNByValue(Integer value) {
    if (value == null) {
      return null;
    }
    for (YN obj : YN.values()) {
      if (obj.value == value) {
        return obj;
      }
    }
    return null;
  }

  public static int getValueByCode(Boolean code) {
    if (code == null) {
      return YN.N.getValue();
    }
    for (YN obj : YN.values()) {
      if (obj.code == code.booleanValue()) {
        return obj.value;
      }
    }
    return YN.N.getValue();
  }


  public Boolean getCode() {
    return code;
  }

  public void setCode(Boolean code) {
    this.code = code;
  }

  public int getValue() {
    return value;
  }

  public void setValue(int value) {
    this.value = value;
  }

  public String getDescribe() {
    return describe;
  }

  public void setDescribe(String describe) {
    this.describe = describe;
  }
}

