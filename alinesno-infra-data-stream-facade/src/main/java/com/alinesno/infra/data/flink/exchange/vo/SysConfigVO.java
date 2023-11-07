package com.alinesno.infra.data.flink.exchange.vo;



/**
 * @author zhp
 * @Description:
 * @date 2020-09-23
 * @time 00:07
 */

public class SysConfigVO {

  private String key;

  private String desc;

  public SysConfigVO(String key, String desc) {
    this.key = key;
    this.desc = desc;
  }

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public String getDesc() {
    return desc;
  }

  public void setDesc(String desc) {
    this.desc = desc;
  }
}
