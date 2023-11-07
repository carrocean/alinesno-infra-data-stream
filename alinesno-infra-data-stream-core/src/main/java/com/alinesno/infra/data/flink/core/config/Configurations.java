package com.alinesno.infra.data.flink.core.config;


import org.apache.commons.lang3.StringUtils;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.table.api.TableEnvironment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zhp
 * @Description:
 * @date 2021/1/17
 * @time 23:57
 */

public class Configurations {

  // 日志记录
  private static final Logger log = LoggerFactory.getLogger(Configurations.class);


  /**
   * 单个设置Configuration
   *
   * @author zhp
   * @date 2021/3/23
   * @time 23:58
   */
  public static void setSingleConfiguration(TableEnvironment tEnv, String key, String value) {
    if (StringUtils.isEmpty(key) || StringUtils.isEmpty(value)) {
      return;
    }
    Configuration configuration = tEnv.getConfig().getConfiguration();
    log.info("#############setConfiguration#############\n  key={} value={}", key, value);
    configuration.setString(key, value);

  }


}
