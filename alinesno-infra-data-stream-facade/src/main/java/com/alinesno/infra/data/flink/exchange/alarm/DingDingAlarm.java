package com.alinesno.infra.data.flink.exchange.alarm;

/**
 * @author zhp
 * @Description:
 * @date 2020-09-23
 * @time 23:59
 */
public interface DingDingAlarm {


  /**
   * @author zhp
   * @date 2020-09-25
   * @time 23:02
   */
  boolean send(String url, String content);

}
