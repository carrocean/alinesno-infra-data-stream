package com.alinesno.infra.data.stream.exchange.listener;

import com.alinesno.infra.data.stream.service.IIpStatusService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

/**
 * @author zhp
 * @Description:
 * @date 2020-05-19
 * @time 23:16
 */
@Component
@Slf4j
public class ApplicationStopListener implements ApplicationListener<ContextClosedEvent> {

  // 日志记录
  private static final Logger log = LoggerFactory.getLogger(ApplicationStopListener.class);

  @Autowired
  private IIpStatusService ipStatusService;

  @Override
  public void onApplicationEvent(ContextClosedEvent event) {
    try {
      log.warn("## sart the ApplicationStopListener start 。。。。。");

      ipStatusService.cancelIp();

      log.warn("## stop the ApplicationStopListener  end 。。。。。");

    } catch (Throwable e) {
      log.warn("##something goes wrong when stopping ApplicationStopListener:", e);

    } finally {
      log.info("## ApplicationStopListener client is down.");
    }
  }
}
