package com.alinesno.infra.data.flink.exchange.ao.impl;

import com.alinesno.infra.data.flink.commom.enums.JobTypeEnum;
import com.alinesno.infra.data.flink.exchange.ao.AlarmServiceAO;
import com.alinesno.infra.data.flink.exchange.ao.DingDingService;
import com.alinesno.infra.data.flink.exchange.ao.JobServerAO;
import com.alinesno.infra.data.flink.exchange.ao.TaskServiceAO;
import com.alinesno.infra.data.flink.exchange.common.SystemConstants;
import com.alinesno.infra.data.flink.exchange.common.util.YarnUtil;
import com.alinesno.infra.data.flink.exchange.config.AlarmPoolConfig;
import com.alinesno.infra.data.flink.exchange.config.SavePointThreadPool;
import com.alinesno.infra.data.flink.exchange.dto.JobConfigDTO;
import com.alinesno.infra.data.flink.exchange.enums.*;
import com.alinesno.infra.data.flink.exchange.exceptions.BizException;
import com.alinesno.infra.data.flink.exchange.rpc.FlinkRestRpcAdapter;
import com.alinesno.infra.data.flink.exchange.rpc.YarnRestRpcAdapter;
import com.alinesno.infra.data.flink.exchange.rpc.model.JobInfo;
import com.alinesno.infra.data.flink.exchange.rpc.model.JobStandaloneInfo;
import com.alinesno.infra.data.flink.exchange.thread.AlarmDingdingThread;
import com.alinesno.infra.data.flink.exchange.thread.AlarmHttpThread;
import com.alinesno.infra.data.flink.exchange.vo.CallbackDTO;
import com.alinesno.infra.data.flink.service.IJobAlarmConfigService;
import com.alinesno.infra.data.flink.service.IJobConfigService;
import com.alinesno.infra.data.flink.service.ISystemConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author zhp
 * @Description:
 * @date 2020-09-22
 * @time 19:59
 */
@Component
public class TaskServiceAOImpl implements TaskServiceAO {

  // 日志记录
  private static final Logger log = LoggerFactory.getLogger(TaskServiceAOImpl.class);

  @Autowired
  private IJobConfigService jobConfigService;

  @Autowired
  private FlinkRestRpcAdapter flinkRestRpcAdapter;

  @Autowired
  private YarnRestRpcAdapter yarnRestRpcAdapter;

  @Autowired
  private AlarmServiceAO alarmServiceAO;

  @Autowired
  private JobServerAO jobYarnServerAO;

  @Autowired
  private JobServerAO jobStandaloneServerAO;

  @Autowired
  private ISystemConfigService systemConfigService;

  @Autowired
  private IJobAlarmConfigService jobAlarmConfigService;

  @Autowired
  private DingDingService dingDingService;

  private ThreadPoolExecutor threadPoolExecutor = AlarmPoolConfig.getInstance()
      .getThreadPoolExecutor();

  @Override
  public void checkJobStatus() {
    List<JobConfigDTO> jobConfigDTOList = jobConfigService.findJobConfigByStatus(JobConfigStatus.RUN.getCode());
    if (CollectionUtils.isEmpty(jobConfigDTOList)) {
      log.warn("当前配置中没有运行的任务");
      return;
    }
    for (JobConfigDTO jobConfigDTO : jobConfigDTOList) {
      if (JobTypeEnum.SQL_BATCH.equals(jobConfigDTO.getJobTypeEnum())) {
        log.warn("批任务不需要状态校验");
        return;
      }
      List<AlarmTypeEnum> alarmTypeEnumList = jobAlarmConfigService.findByJobId(jobConfigDTO.getId());
      switch (jobConfigDTO.getDeployModeEnum()) {
        case YARN_PER:
        case YARN_APPLICATION:
          this.checkYarn(jobConfigDTO, alarmTypeEnumList);
          this.sleep();
          break;
        case LOCAL:
        case STANDALONE:
          this.checkStandalone(jobConfigDTO, alarmTypeEnumList);
          this.sleep();
          break;
        default:
          break;
      }
    }
  }

  @Override
  @Deprecated
  public void checkYarnJobByStop() {
    List<JobConfigDTO> jobConfigDTOList = jobConfigService.findJobConfigByStatus(JobConfigStatus.STOP.getCode());
    if (CollectionUtils.isEmpty(jobConfigDTOList)) {
      return;
    }
    for (JobConfigDTO jobConfigDTO : jobConfigDTOList) {
      if (jobConfigDTO.getIsOpen().intValue() == YN.N.getValue()) {
        continue;
      }
      if (JobTypeEnum.SQL_BATCH.equals(jobConfigDTO.getJobTypeEnum())) {
        log.warn("批任务不需要状态校验");
        return;
      }
      switch (jobConfigDTO.getDeployModeEnum()) {
        case YARN_PER:
        case YARN_APPLICATION:
          String appId = null;
          try {
            String queueName = YarnUtil.getQueueName(jobConfigDTO.getFlinkRunConfig());
            if (StringUtils.isEmpty(queueName)) {
              continue;
            }
            log.info("check job getJobName={} queueName={}", jobConfigDTO.getJobName(), queueName);
            appId = yarnRestRpcAdapter.getAppIdByYarn(jobConfigDTO.getJobName(), queueName, jobConfigDTO.getCreator());
          } catch ( BizException be) {
            if (SysErrorEnum.YARN_CODE.getCode().equals(be.getCode())) {
              continue;
            }
            log.error("[BizException]getAppIdByYarn  is error ", be);
          } catch (Exception e) {
            log.error("[Exception]getAppIdByYarn is error ", e);
            continue;
          }
          if (!StringUtils.isEmpty(appId)) {
            JobInfo jobInfo = yarnRestRpcAdapter.getJobInfoForPerYarnByAppId(appId);
            if (jobInfo != null && SystemConstants.STATUS_RUNNING.equals(jobInfo.getStatus())) {
              log.warn("执行停止操作 jobYarnInfo={} id={}", jobInfo, appId);
              yarnRestRpcAdapter.cancelJobForYarnByAppId(appId, jobInfo.getId());
            }
          }
          break;
        default:
          break;
      }
    }
  }

  @Override
  public void autoSavePoint() {
    List<JobConfigDTO> jobConfigDTOList = jobConfigService.findJobConfigByStatus(JobConfigStatus.RUN.getCode());
    if (CollectionUtils.isEmpty(jobConfigDTOList)) {
      log.warn("autoSavePoint is null  没有找到运行中的任务 ");
      return;
    }
    for (JobConfigDTO jobConfigDTO : jobConfigDTOList) {

      //sql、jar 流任务才执行SavePoint
      if (JobTypeEnum.SQL_STREAMING.equals(jobConfigDTO.getJobTypeEnum())
          || JobTypeEnum.JAR.equals(jobConfigDTO.getJobTypeEnum())) {
        SavePointThreadPool.getInstance().getThreadPoolExecutor()
            .execute(new SavePoint(jobConfigDTO));
        sleep();
      }


    }
  }

  /**
   * 执行SavePoint
   */
  class SavePoint implements Runnable {

    private JobConfigDTO jobConfigDTO;

    SavePoint(JobConfigDTO jobConfigDTO) {
      this.jobConfigDTO = jobConfigDTO;
    }

    @Override
    public void run() {
      try {
        switch (jobConfigDTO.getDeployModeEnum()) {
          case YARN_PER:
          case YARN_APPLICATION:
            jobYarnServerAO.savepoint(jobConfigDTO.getId());
            break;
          case LOCAL:
          case STANDALONE:
            jobStandaloneServerAO.savepoint(jobConfigDTO.getId());
            break;
          default:
        }

      } catch (Exception e) {
        log.error("执行savepoint 异常", e);
      }
    }
  }


  private void sleep() {
    try {
      Thread.sleep(4000);
    } catch (InterruptedException e) {
    }
  }

  private void checkYarn(JobConfigDTO jobConfigDTO, List<AlarmTypeEnum> alarmTypeEnumList) {
    if (StringUtils.isEmpty(jobConfigDTO.getJobId())) {
      log.error("任务[{}]配置不存在", jobConfigDTO.getId());
      return;
    }
    //查询任务状态
    JobInfo jobInfo = yarnRestRpcAdapter.getJobInfoForPerYarnByAppId(jobConfigDTO.getJobId());
    if (jobInfo != null && (SystemConstants.STATUS_RUNNING.equals(jobInfo.getStatus())
        || SystemConstants.STATUS_INITIALIZING.equals(jobInfo.getStatus())
        || SystemConstants.STATUS_SCHEDULED.equals(jobInfo.getStatus()))) {
      return;
    }
    log.info("jobInfo={}", jobInfo);
    //变更任务状态
    log.error("发现本地任务[{}]状态和yarn上不一致，准备自动修复本地web任务状态， {}",
        jobConfigDTO.getId(), jobConfigDTO);
    JobConfigDTO jobConfig = JobConfigDTO.bulidStop(jobConfigDTO.getId());
    jobConfigService.updateJobConfigById(jobConfig);
    //发送告警并且自动拉起任务
    this.alermAndAutoJob(alarmTypeEnumList,
        SystemConstants.buildDingdingMessage("检测到运行模式[" + jobConfigDTO.getDeployModeEnum().name()
            + "],任务名称：" + jobConfigDTO.getJobName() +"停止运行"), jobConfigDTO,
        jobConfigDTO.getDeployModeEnum());
  }

  private void checkStandalone(JobConfigDTO jobConfigDTO, List<AlarmTypeEnum> alarmTypeEnumList) {
    if (StringUtils.isEmpty(jobConfigDTO.getJobId())) {
      String message = SystemConstants.buildDingdingMessage(
          "检测到任务jobId异常任务[" + jobConfigDTO.getId() + "]名称：" + jobConfigDTO.getJobName());
      log.error(message);
      return;
    }
    //查询任务状态
    JobStandaloneInfo jobStandaloneInfo = flinkRestRpcAdapter.getJobInfoForStandaloneByAppId(jobConfigDTO.getJobId(), jobConfigDTO.getDeployModeEnum());
    if (jobStandaloneInfo != null && SystemConstants.STATUS_RUNNING
        .equals(jobStandaloneInfo.getState())) {
      return;
    }


    //变更任务状态
    log.error("发现本地任务[{}]状态和yarn上不一致，准备自动修复任务状态，jobStandaloneInfo={}", jobConfigDTO.getJobName(), jobStandaloneInfo);
    JobConfigDTO jobConfig = JobConfigDTO.bulidStop(jobConfigDTO.getId());
    jobConfigService.updateJobConfigById(jobConfig);
    //发送告警并且自动拉起任务
    this.alermAndAutoJob(alarmTypeEnumList,
        SystemConstants.buildDingdingMessage("检测到运行模式[" + jobConfigDTO.getDeployModeEnum().name()
            + "],任务名称：" + jobConfigDTO.getJobName()+"停止运行"), jobConfigDTO,
        DeployModeEnum.STANDALONE);
  }

  /**
   * 告警并且拉起任务， //TODO 如果拉起失败下次将不能拉起
   *
   * @author zhp
   * @date 2021/2/28
   * @time 19:50
   */
  private void alermAndAutoJob(List<AlarmTypeEnum> alarmTypeEnumList, String cusContent,
      JobConfigDTO jobConfigDTO, DeployModeEnum deployModeEnum) {
    threadPoolExecutor.execute(new Runnable() {
      @Override
      public void run() {
        dingDingService.doAlarmNotify(cusContent, jobConfigDTO, deployModeEnum);
      }
    });
    if (CollectionUtils.isEmpty(alarmTypeEnumList)) {
      log.warn("任务[{}-{}]没有配置告警，无法进行告警，并且任务将会被停止！！！", jobConfigDTO.getId(),
          jobConfigDTO.getJobName());
      return;
    }
    CallbackDTO callbackDTO = CallbackDTO.to(jobConfigDTO);
    //告警
    for (AlarmTypeEnum alarmTypeEnum : alarmTypeEnumList) {
      switch (alarmTypeEnum) {
        case DINGDING:
          this.dingdingAlarm(cusContent, callbackDTO.getJobConfigId(), callbackDTO.getOperatorId() );
          break;
        case CALLBACK_URL:
          this.httpAlarm(callbackDTO);
          break;
        default:
      }
    }
    //自动拉起
    if (alarmTypeEnumList.contains(AlarmTypeEnum.AUTO_START_JOB)) {
      log.info("校验任务不存在,开始自动拉起 JobConfigId={}", callbackDTO.getJobConfigId());
      try {
        switch (deployModeEnum) {
          case YARN_PER:
          case YARN_APPLICATION:
            jobYarnServerAO
                .start(callbackDTO.getJobConfigId(), null, SystemConstants.USER_NAME_TASK_AUTO);
            break;
          case STANDALONE:
            jobStandaloneServerAO
                .start(callbackDTO.getJobConfigId(), null, SystemConstants.USER_NAME_TASK_AUTO);
            break;
          default:
        }
      } catch (Exception e) {
        log.error("自动重启任务失败 JobConfigId={}", callbackDTO.getJobConfigId(), e);
      }
    }
  }

  /**
   * 钉钉告警
   *
   * @author zhp
   * @date 2021/2/28
   * @time 19:56
   */
  private void dingdingAlarm(String content, Long jobConfigId, Long operatorId) {
    String alartUrl = systemConfigService
        .getSystemConfigByKey(SysConfigEnum.DINGDING_ALARM_URL.getKey() );
    if (StringUtils.isEmpty(alartUrl)) {
      log.warn("##### 钉钉告警url没有设置，任务[{}]无法告警 #####", jobConfigId);
      return;
    }
    threadPoolExecutor
        .execute(new AlarmDingdingThread(alarmServiceAO, content, jobConfigId, alartUrl));
  }

  /**
   * 回调函数自定义告警
   *
   * @author zhp
   * @date 2021/2/28
   * @time 19:56
   */
  private void httpAlarm(CallbackDTO callbackDTO) {
    String alartUrl = systemConfigService
        .getSystemConfigByKey(SysConfigEnum.CALLBACK_ALARM_URL.getKey() );
    if (StringUtils.isEmpty(alartUrl)) {
      log.warn("##### 回调告警url没有设置，任务[{}]无法告警 #####", callbackDTO.getJobConfigId());
      return;
    }
    threadPoolExecutor.execute(new AlarmHttpThread(alarmServiceAO, callbackDTO, alartUrl));
  }
}
