package com.alinesno.infra.data.flink.exchange.utils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zhp
 * @Description:
 * @date 2022/10/11
 */

public class SystemInfoUtil {

  // 日志记录
  private static final Logger log = LoggerFactory.getLogger(SystemInfoUtil.class);

  public static String getEnv(String key) {
    try {
      return System.getenv().get(key);
    } catch (Exception e) {
      log.error("getEnv is error");
    }
    return null;
  }

}
