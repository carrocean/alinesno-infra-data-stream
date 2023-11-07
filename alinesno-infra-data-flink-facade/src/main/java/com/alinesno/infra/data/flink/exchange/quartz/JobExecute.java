package com.alinesno.infra.data.flink.exchange.quartz;

import com.alinesno.infra.data.flink.commom.enums.JobTypeEnum;
import com.alinesno.infra.data.flink.exchange.ao.JobServerAO;
import com.alinesno.infra.data.flink.exchange.dto.JobConfigDTO;
import com.alinesno.infra.data.flink.exchange.enums.JobConfigStatus;
import com.alinesno.infra.data.flink.exchange.factory.ApplicationContextProvider;
import com.alinesno.infra.data.flink.exchange.factory.JobServerAOFactory;
import com.alinesno.infra.data.flink.service.IJobConfigService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * 批任务调用入口
 *
 * @author zhp
 * @Description:
 * @date 2022/10/29
 */

@Component
@Lazy
public class JobExecute implements Job {


  // 日志记录
  private static final Logger log = LoggerFactory.getLogger(JobExecute.class);


  private static final String NAME = "quartz-job";

  private IJobConfigService jobConfigService = ApplicationContextProvider
      .getBean(IJobConfigService.class);

  @Override
  public void execute(JobExecutionContext context) {
    log.info("开始执行定时任务");
    Long id = null;
    try {
      id = Long.valueOf(context.getJobDetail().getJobDataMap().get("id").toString());
      String jobName = (String) context.getJobDetail().getJobDataMap().get("jobName");
      JobServerAO jobServerAO = this.getJobServerAO(id);
      jobServerAO.start(id, null, NAME);
    } catch (Exception e) {
      log.error("JobExecute-execute is error id={}", id, e);
    }
  }

  private JobServerAO getJobServerAO(Long id) {
    JobConfigDTO jobConfigDTO = jobConfigService.getJobConfigById(id);
    if (jobConfigDTO == null || jobConfigDTO.getJobTypeEnum() != JobTypeEnum.SQL_BATCH) {
      log.error("不是批任务或者任务不存在 不能执行定时调度 id={} jobConfigDTO={}", id, jobConfigDTO);
      throw new NullPointerException("getJobServerAO is null");
    }
    if (jobConfigDTO.getStatus() == JobConfigStatus.RUN
        || jobConfigDTO.getStatus() == JobConfigStatus.STARTING) {
      throw new RuntimeException("bath_sql is run");
    }

    return JobServerAOFactory.getJobServerAO(jobConfigDTO.getDeployModeEnum());


  }
}
