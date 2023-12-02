package com.alinesno.infra.data.stream.exchange.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author zhp
 * @Description:
 * @date 2018/9/6
 * @time 下午21:58
 */


public final class WaitForPoolConfig {

  // 日志记录
  private static final Logger log = LoggerFactory.getLogger(WaitForPoolConfig.class);

  private static int corePoolSize = 20;

  private static int maximumPoolSize = 400;

  private static long keepAliveTime = 10;


  private static ThreadPoolExecutor threadPoolExecutor;

  private static WaitForPoolConfig alarmPoolConfig;

  private WaitForPoolConfig() {
    BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(100, true);
    threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime,
        TimeUnit.MINUTES,
        workQueue, new ThreadPoolExecutor.AbortPolicy());
  }

  public static synchronized WaitForPoolConfig getInstance() {
    if (null == alarmPoolConfig) {
      synchronized (WaitForPoolConfig.class) {
        if (null == alarmPoolConfig) {
          alarmPoolConfig = new WaitForPoolConfig();
        }
      }
    }
    log.info("WaitForPoolConfig threadPoolExecutor={}", threadPoolExecutor);
    return alarmPoolConfig;
  }

  public synchronized ThreadPoolExecutor getThreadPoolExecutor() {
    return threadPoolExecutor;
  }
}
