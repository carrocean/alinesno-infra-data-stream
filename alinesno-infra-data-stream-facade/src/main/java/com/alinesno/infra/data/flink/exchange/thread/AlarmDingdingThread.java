package com.alinesno.infra.data.flink.exchange.thread;

import com.alinesno.infra.data.flink.exchange.ao.AlarmServiceAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

/**
 * @author zhp
 * @Description:
 * @date 2021/2/21
 * @time 17:37
 */

public class AlarmDingdingThread implements Runnable {

  // 日志记录
  private static final Logger log = LoggerFactory.getLogger(AlarmDingdingThread.class);

  private AlarmServiceAO alarmServiceAO;

  private String content;

  private long jobConfigId;

  private String url;


  public AlarmDingdingThread(AlarmServiceAO alarmServiceAO, String content, long jobConfigId,
      String url) {
    this.alarmServiceAO = alarmServiceAO;
    this.content = content;
    this.jobConfigId = jobConfigId;
    this.url = url;
  }

  @Override
  public void run() {
    try {
      log.info("开始执行钉钉告警 content={} jobConfigId={}", content, jobConfigId);
      if (StringUtils.isEmpty(url)) {
        log.warn("没有配置钉钉url地址 不发送告警");
        return;
      }
      alarmServiceAO.sendForDingding(url, content, jobConfigId);
    } catch (Exception e) {
      log.error("告警失败 is error", e);
    }
  }
}
