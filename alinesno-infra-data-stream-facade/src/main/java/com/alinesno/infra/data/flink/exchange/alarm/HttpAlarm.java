package com.alinesno.infra.data.flink.exchange.alarm;

import com.alinesno.infra.data.flink.exchange.vo.CallbackDTO;

/**
 * @author zhp
 * @Description:
 * @date 2021/2/21
 * @time 11:38
 */
public interface HttpAlarm {

  /**
   * 回调http
   *
   * @author zhp
   * @date 2021/2/21
   * @time 11:39
   */
  boolean send(String url, CallbackDTO callbackDTO);
}
