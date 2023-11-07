package com.alinesno.infra.data.flink.exchange.thread;

import com.alinesno.infra.data.flink.exchange.ao.AlarmServiceAO;
import com.alinesno.infra.data.flink.exchange.vo.CallbackDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

/**
 * @author zhp
 * @Description:
 * @date 2021/2/21
 * @time 17:37
 */

public class AlarmHttpThread implements Runnable {

  // 日志记录
  private static final Logger log = LoggerFactory.getLogger(AlarmHttpThread.class);

  private AlarmServiceAO alarmServiceAO;

  private CallbackDTO callbackDTO;

  private String url;

  public AlarmHttpThread(AlarmServiceAO alarmServiceAO, CallbackDTO callbackDTO, String url) {
    this.alarmServiceAO = alarmServiceAO;
    this.callbackDTO = callbackDTO;
    this.url = url;
  }

  @Override
  public void run() {
    try {
      log.info("开始执行http回调告警 callbackDTO={} url={}", callbackDTO, url);
      if (StringUtils.isEmpty(url)) {
        log.warn("没有配置http回调地址 不发送告警");
        return;
      }
      alarmServiceAO.sendForHttp(url, callbackDTO);
    } catch (Exception e) {
      log.error("告警失败 is error", e);
    }
  }
}
