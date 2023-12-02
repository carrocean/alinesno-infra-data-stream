package com.alinesno.infra.data.stream.exchange.runner;

import com.alinesno.infra.data.stream.service.IIpStatusService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author zhp
 * @Description:
 * @date 2018/5/9
 * @time 下午4:12
 */
@Component
@Order(0)
@Configuration
public class ApplicationRunner implements org.springframework.boot.ApplicationRunner {

  // 日志记录
  private static final Logger log = LoggerFactory.getLogger(ApplicationRunner.class);


  @Autowired
  private IIpStatusService ipStatusService;


  @Override
  public void run(ApplicationArguments args) throws Exception {

    ipStatusService.registerIp();
  }


}
