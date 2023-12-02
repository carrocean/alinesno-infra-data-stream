package com.alinesno.infra.data.stream.exchange.enums;



/**
 * @author zhp
 * @Description:
 * @date 2020-07-20
 * @time 02:15
 */

public enum IpStatusEnum {

  START(1), STOP(-1);

  private int code;

  IpStatusEnum(int code) {
    this.code = code;
  }

  public int getCode() {
    return code;
  }

  public void setCode(int code) {
    this.code = code;
  }
}
