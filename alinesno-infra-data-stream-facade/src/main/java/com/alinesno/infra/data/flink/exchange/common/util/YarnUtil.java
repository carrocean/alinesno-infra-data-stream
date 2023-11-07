package com.alinesno.infra.data.flink.exchange.common.util;

import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zhp
 * @Description:
 * @date 2020-09-11
 * @time 01:49
 */

public class YarnUtil {

  // 日志记录
  private static final Logger log = LoggerFactory.getLogger(YarnUtil.class);

  public static String getQueueName(String flinkRunConfig) throws ParseException {

    String getYarnQueueName = CliConfigUtil.getYarnQueueName(flinkRunConfig);
    log.info("得到队列名称是：getYarnQueueName={}", getYarnQueueName);

    return getYarnQueueName;
  }


}
