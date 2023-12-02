package com.alinesno.infra.data.stream.commom.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * @author zhp
 * @Description:
 * @date 2021/3/7
 * @time 16:27
 */
public enum StateBackendEnum {
  MEMORY("0"), FILE("1"), ROCKSDB("2");

  private String type;

  StateBackendEnum(String type) {
    this.type = type;
  }

  public static StateBackendEnum getStateBackend(String stateBackendType) {
    if (StringUtils.isEmpty(stateBackendType)) {
      return FILE;
    }

    for (StateBackendEnum stateBackendEnum : StateBackendEnum.values()) {
      if (stateBackendEnum.getType().equalsIgnoreCase(stateBackendType.trim())) {
        return stateBackendEnum;
      }

    }
    throw new RuntimeException("stateBackendType值只能是 0 1 2 非法参数值" + stateBackendType);
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }
}
