package com.alinesno.infra.data.stream.exchange.quartz;

import com.alinesno.infra.common.facade.response.ResultCodeEnum;
import com.alinesno.infra.data.stream.entity.BatchJob;
import com.alinesno.infra.data.stream.exchange.common.ResponseBean;
import com.alinesno.infra.data.stream.exchange.common.SystemConstants;
import com.alinesno.infra.data.stream.service.IJobConfigService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

/**
 * 批任务调度管理 1、新增job 2、删除job 3、全量一次加载任务（启动的时候）
 *
 * @author zhp
 * @Description:
 * @date 2022/10/29
 */
@Component

public class BatchJobManagerScheduler implements ApplicationRunner {

  // 日志记录
  private static final Logger log = LoggerFactory.getLogger(BatchJobManagerScheduler.class);

  @Autowired
  private SchedulerConfig schedulerConfig;

  @Autowired
  private IJobConfigService jobConfigService;

  /*
   *服务启动的时候注册批任务
   * @Param:[args]
   * @return: void
   * @Author: zhp
   * @date 2022/10/29
   */
  @Override
  public void run(ApplicationArguments args) throws Exception {
    log.info("##########开始注册批任务调度##########");
    this.batchRegisterJob();
    log.info("##########结束注册批任务调度##########");
  }


  /*
   *注册单个任务到调度器
   * @Param:[batchJob]
   * @return: void
   * @Author: zhp
   * @date 2022/10/29
   */
  public ResponseBean registerJob(BatchJob batchJob) {

    ResponseBean result = new ResponseBean();

    log.info("注册批任务到调度器 batchJob={}", batchJob);
    Scheduler scheduler = schedulerConfig.getScheduler();
    try {
      JobKey jobKey = new JobKey(SystemConstants.buildQuartzJobKeyName(batchJob.getId()));
      if (scheduler.checkExists(jobKey)) {
        log.info("任务已经存在不需要注册 batchJob={}", batchJob);
        result.setCode(ResultCodeEnum.FAIL);
        result.setMessage( String.format("任务已经存在不需要注册 batchJob=%s", batchJob) );
        return result;
      }
      JobDetail jobDetail = JobDetailAndTriggerBuild.buildJobDetail(batchJob.getId(), batchJob.getJobName());
      Trigger trigger = JobDetailAndTriggerBuild.buildTrigger(batchJob.getJobName(), batchJob.getCron());
      scheduler.scheduleJob(jobDetail, trigger);
    } catch (Exception e) {
      log.error("registerJob is error batchJob={}", batchJob, e);
      result.setCode(ResultCodeEnum.FAIL);
      result.setMessage( String.format("registerJob is error batchJob=%s", batchJob) );
      return result;
    }
    this.getJobKeysCount();
    result.setCode(ResultCodeEnum.SUCCESS);
    return result;

  }

  /*
   *删除一个job
   * @Param:[id]
   * @return: void
   * @Author: zhp
   * @date 2022/10/30
   */
  public ResponseBean deleteJob(Long id) {
    ResponseBean result = new ResponseBean();
    ResponseBean deleteJobResult = schedulerConfig.deleteJob(id);
    if ( deleteJobResult.getCode() != 200 ) {
      return deleteJobResult ;
    }

    this.getJobKeysCount();
    result.setCode(ResultCodeEnum.SUCCESS);
    return result ;

  }

  /*
   *查询任务个数
   * @Param:[]
   * @return: java.lang.Integer
   * @Author: zhp
   * @date 2022/10/30
   */
  public Integer getJobKeysCount() {
    GroupMatcher<JobKey> matcher = GroupMatcher.anyJobGroup();
    Set<JobKey> jobKeys = null;
    try {
      jobKeys = schedulerConfig.getScheduler().getJobKeys(matcher);
      log.info("当前调度器运行的任务有 {} 个", jobKeys.size());
      return jobKeys.size();
    } catch (Exception e) {
      log.error("getJobKeys is error,msg:{}",e.getMessage());
    }
    return 0;
  }

  /*
   *批量注册
   * @Param:[]
   * @return: void
   * @Author: zhp
   * @date 2022/10/30
   */
  public void batchRegisterJob() {

    List<BatchJob> list = jobConfigService.getAllBatchJobs();
    if (CollectionUtils.isEmpty(list)) {
      log.info("没有找到批任务，不需要注册定时调度");
      return;
    }
    for (BatchJob batchJob : list) {
      if (!StringUtils.isEmpty(batchJob.getCron())) {
        this.registerJob(batchJob);
      }
    }
    log.info("本次批量注册完成后一共有 {} 个任务 ", this.getJobKeysCount());
  }

}
