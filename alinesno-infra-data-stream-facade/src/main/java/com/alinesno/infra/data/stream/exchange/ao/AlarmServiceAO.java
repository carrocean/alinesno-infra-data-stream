package com.alinesno.infra.data.stream.exchange.ao;

import com.alinesno.infra.data.stream.exchange.vo.CallbackDTO;

/**
 * @author zhp
 * @Description:
 * @date 2020-09-25
 * @time 19:50
 */
public interface AlarmServiceAO {

  /**
   * 发送钉钉告警消息
   *
   * @author zhp
   * @date 2020-09-25
   * @time 19:53
   */
  boolean sendForDingding(String url, String content, long jobConfigId);


  /**
   * 发送http请求回调
   *
   * @author zhp
   * @date 2021/2/21
   * @time 11:31
   */
  boolean sendForHttp(String url, CallbackDTO callbackDTO);
}
