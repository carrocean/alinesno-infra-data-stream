package com.alinesno.infra.data.flink.exchange.enums;

/**
 * @author zhp
 * @Description:
 * @date 2020-08-07
 * @time 21:42
 */
public enum YarnStateEnum {
  NEW, NEW_SAVING, SUBMITTED, ACCEPTED, RUNNING, FINISHED, FAILED, KILLED, UNKNOWN;

  public static YarnStateEnum getYarnStateEnum(String state) {
    for (YarnStateEnum stateEnum : YarnStateEnum.values()) {
      if (stateEnum.name().equals(state)) {
        return stateEnum;
      }

    }

    return UNKNOWN;
  }
}
