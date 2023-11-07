package com.alinesno.infra.data.flink.exchange.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 核心线程10个 最大线程100 队列100 公平策略 告警线程池
 *
 * @author zhp
 * @Description:
 * @date 2018/9/6
 * @time 下午21:58
 */

public final class AlarmPoolConfig {

  // 日志记录
  private static final Logger log = LoggerFactory.getLogger(AlarmPoolConfig.class);

  private static int corePoolSize = 10;

  private static int maximumPoolSize = 30;

  private static long keepAliveTime = 10;


  private static ThreadPoolExecutor threadPoolExecutor;

  private static AlarmPoolConfig alarmPoolConfig;

  private AlarmPoolConfig() {
    BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(100, true);
    threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime,
        TimeUnit.MINUTES, workQueue);
  }

  public static synchronized AlarmPoolConfig getInstance() {
    if (null == alarmPoolConfig) {
      synchronized (AlarmPoolConfig.class) {
        if (null == alarmPoolConfig) {
          alarmPoolConfig = new AlarmPoolConfig();
        }
      }
    }
    log.info("AlarmPoolConfig threadPoolExecutor={}", threadPoolExecutor);
    return alarmPoolConfig;
  }

  public synchronized ThreadPoolExecutor getThreadPoolExecutor() {
    return threadPoolExecutor;
  }
}
