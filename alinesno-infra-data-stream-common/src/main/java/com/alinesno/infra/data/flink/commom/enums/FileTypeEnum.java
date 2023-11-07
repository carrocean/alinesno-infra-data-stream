package com.alinesno.infra.data.flink.commom.enums;


public enum FileTypeEnum {

  JAR(1);

  private int code;

  FileTypeEnum(int code) {
    this.code = code;
  }

  public int getCode() {
    return code;
  }

  public void setCode(int code) {
    this.code = code;
  }
}
