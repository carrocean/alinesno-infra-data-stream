package com.alinesno.infra.data.flink.exchange.quartz;


import com.alinesno.infra.common.facade.response.ResultCodeEnum;
import com.alinesno.infra.data.flink.exchange.common.ResponseBean;
import com.alinesno.infra.data.flink.exchange.common.SystemConstants;
import lombok.SneakyThrows;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * @author zhp
 * @Description:
 * @date 2022/10/29
 */
@Component

public class SchedulerConfig {

  // 日志记录
  private static final Logger log = LoggerFactory.getLogger(SchedulerConfig.class);

  private Scheduler scheduler;

  @PostConstruct
  public void init() throws SchedulerException {
    this.createScheduler();
    this.startScheduler();
  }


  @SneakyThrows
  public Scheduler createScheduler() throws SchedulerException {
    log.info("####create scheduler....");
    if (scheduler == null) {
      scheduler = StdSchedulerFactory.getDefaultScheduler();
    }
    return scheduler;
  }

  @SneakyThrows
  public void startScheduler() throws SchedulerException {
    log.info("#####start scheduler....");
    if (scheduler != null && !scheduler.isStarted()) {
      scheduler.start();
    }
  }

  @SneakyThrows
  @PreDestroy
  public void stopScheduler() throws SchedulerException {
    if (scheduler != null && scheduler.isStarted()) {
      log.info("#####shutdown scheduler....");
      scheduler.shutdown(true);
    }
  }

  public ResponseBean deleteJob(Long id) {
    ResponseBean result = new ResponseBean();
    result.setCode(ResultCodeEnum.SUCCESS);
    try {
      JobKey jobKey = new JobKey(SystemConstants.buildQuartzJobKeyName(id));
      if (scheduler.checkExists(jobKey)) {
        log.info("取消调度任务id={}", id);
        scheduler.deleteJob(jobKey);
      } else {
        log.info("没有在调度器里面不用取消 id={}", id);
      }
      return  result ;
    } catch (Exception e) {
      log.error("取消调度任务失败 id={},异常信息:{}", id, e.getMessage());
      result.setCode(ResultCodeEnum.FAIL);
      result.setMessage( String.format("取消调度任务异常,id=%s,异常信息:%s", id, e.getMessage() ) ) ;
      return  result ;
    }
  }

  public Scheduler getScheduler() {
    return scheduler;
  }

  public void setScheduler(Scheduler scheduler) {
    this.scheduler = scheduler;
  }
}
