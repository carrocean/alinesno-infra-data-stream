package com.alinesno.infra.data.stream.exchange.config;

import org.apache.flink.shaded.guava30.com.google.common.cache.Cache;
import org.apache.flink.shaded.guava30.com.google.common.cache.CacheBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * @author zhp
 * @Description:
 * @since 1.0.0
 */
@Configuration
public class LocalCache {

  // 日志记录
  private static final Logger log = LoggerFactory.getLogger(LocalCache.class);

  private Cache<String, String> cache = null;

  private static final long DURATION = 5;

  public LocalCache() {
    cache = CacheBuilder.newBuilder().expireAfterWrite(DURATION, TimeUnit.MINUTES).build();
  }


  public void put(String key, String value) {
    cache.put(key, value);
  }

  public String get(String key) {
    try {
      return cache.get(key, new Callable<String>() {
        @Override
        public String call() throws Exception {
          return "";
        }
      });
    } catch (Exception e) {
      log.error("get LocalCache error", e);
    }
    return null;

  }

}
