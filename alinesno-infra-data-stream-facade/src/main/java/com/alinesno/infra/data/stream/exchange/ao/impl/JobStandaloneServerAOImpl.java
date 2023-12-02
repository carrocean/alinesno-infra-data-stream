package com.alinesno.infra.data.stream.exchange.ao.impl;

import com.alinesno.infra.common.facade.response.ResultCodeEnum;
import com.alinesno.infra.data.stream.commom.enums.JobTypeEnum;
import com.alinesno.infra.data.stream.entity.BatchJob;
import com.alinesno.infra.data.stream.exchange.ao.JobBaseServiceAO;
import com.alinesno.infra.data.stream.exchange.ao.JobServerAO;
import com.alinesno.infra.data.stream.exchange.common.MessageConstants;
import com.alinesno.infra.data.stream.exchange.common.ResponseBean;
import com.alinesno.infra.data.stream.exchange.common.SystemConstants;
import com.alinesno.infra.data.stream.exchange.dto.JobConfigDTO;
import com.alinesno.infra.data.stream.exchange.dto.JobRunParamDTO;
import com.alinesno.infra.data.stream.exchange.enums.*;
import com.alinesno.infra.data.stream.exchange.exceptions.BizException;
import com.alinesno.infra.data.stream.exchange.quartz.BatchJobManagerScheduler;
import com.alinesno.infra.data.stream.exchange.rpc.CommandRpcClinetAdapter;
import com.alinesno.infra.data.stream.exchange.rpc.FlinkRestRpcAdapter;
import com.alinesno.infra.data.stream.exchange.rpc.model.JobStandaloneInfo;
import com.alinesno.infra.data.stream.service.IJobConfigService;
import com.alinesno.infra.data.stream.service.ISavepointBackupService;
import com.alinesno.infra.data.stream.service.ISystemConfigService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Map;

/**
 * @author zhp
 * @Description:
 * @date 2020-07-20
 * @time 23:11
 */
@Component(SystemConstants.BEANNAME_JOBSTANDALONESERVERAO)
public class JobStandaloneServerAOImpl implements JobServerAO {

  // 日志记录
  private static final Logger log = LoggerFactory.getLogger(JobStandaloneServerAOImpl.class);


  @Autowired
  private IJobConfigService jobConfigService;

  @Autowired
  private ISavepointBackupService savepointBackupService;

  @Autowired
  private CommandRpcClinetAdapter commandRpcClinetAdapter;

  @Autowired
  private FlinkRestRpcAdapter flinkRestRpcAdapter;

  @Autowired
  private JobBaseServiceAO jobBaseServiceAO;

  @Autowired
  private BatchJobManagerScheduler batchJobRegister;

  @Autowired
  private ISystemConfigService systemConfigService;

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void start(Long id, Long savepointId, String operatorId) {
    JobConfigDTO jobConfigDTO = jobConfigService.getJobConfigById(id);
    log.info("[{}]开始启动任务[{}]", operatorId, jobConfigDTO.getJobName());
    jobConfigDTO.setEditor(Long.parseLong(operatorId));

    if ( StringUtils.isNotBlank(jobConfigDTO.getJobId()) ) {
      if ( !jobConfigDTO.getJobTypeEnum().equals(JobTypeEnum.SQL_BATCH) ) {
        JobStandaloneInfo jobStatus = flinkRestRpcAdapter.getJobInfoForStandaloneByAppId(jobConfigDTO.getJobId() ,jobConfigDTO.getDeployModeEnum());
        if ( StringUtils.isNotBlank(jobStatus.getState() ) && SystemConstants.STATUS_RUNNING.equalsIgnoreCase(jobStatus.getState()) ) {
          throw new BizException(
              "请检查Flink任务列表，任务ID=[" + jobConfigDTO.getJobId() + "]处于[ " + jobStatus.getState()
                  + "]状态，不能重复启动任务！");
        }
      }

    }

    //1、检查jobConfigDTO 状态等参数
    jobBaseServiceAO.checkStart(jobConfigDTO);

    // TODO 要不要检查集群上任务是否存在

    //2、将配置的sql 写入本地文件并且返回运行所需参数
    JobRunParamDTO jobRunParamDTO = jobBaseServiceAO.writeSqlToFile(jobConfigDTO);

    //3、插一条运行日志数据
    Long jobRunLogId = jobBaseServiceAO.insertJobRunLog(jobConfigDTO);

    //4、变更任务状态（变更为：启动中） 有乐观锁 防止重复提交
    jobConfigService.updateStatusByStart(jobConfigDTO.getId(), Long.valueOf(operatorId), jobRunLogId, jobConfigDTO.getVersion());

    String savepointPath = savepointBackupService.getSavepointPathById(id, savepointId);

    //异步提交任务
    jobBaseServiceAO.aSyncExecJob(jobRunParamDTO, jobConfigDTO, jobRunLogId, savepointPath);

  }


  @Override
  public void stop(Long id, Long operatorId) {
    JobConfigDTO jobConfigDTO = jobConfigService.getJobConfigById(id);
    log.info("[{}]开始停止任务[{}]", operatorId , jobConfigDTO.getJobName());

    if (jobConfigDTO == null) {
      throw new BizException(SysErrorEnum.JOB_CONFIG_JOB_IS_NOT_EXIST.getErrorMsg());
    }
    JobStandaloneInfo jobStandaloneInfo = flinkRestRpcAdapter
        .getJobInfoForStandaloneByAppId(jobConfigDTO.getJobId(), jobConfigDTO.getDeployModeEnum());
    log.info("任务[{}]当前状态为：{}", id, jobStandaloneInfo);
    if (jobStandaloneInfo == null || StringUtils.isNotEmpty(jobStandaloneInfo.getErrors())) {
      log.warn("开始停止任务[{}]，getJobInfoForStandaloneByAppId is error jobStandaloneInfo={}", id,
          jobStandaloneInfo);
    } else {
      // 停止前先savepoint
      if (StringUtils.isNotBlank(jobConfigDTO.getFlinkCheckpointConfig())
          && jobConfigDTO.getJobTypeEnum() != JobTypeEnum.SQL_BATCH
          && SystemConstants.STATUS_RUNNING.equals(jobStandaloneInfo.getState())) {
        log.info("开始保存任务[{}]的状态-savepoint", id);
        this.savepoint(id);
      }
      //停止任务
      if (SystemConstants.STATUS_RUNNING.equals(jobStandaloneInfo.getState())
          || SystemConstants.STATUS_RESTARTING.equals(jobStandaloneInfo.getState())) {
        flinkRestRpcAdapter
            .cancelJobForFlinkByAppId(jobConfigDTO.getJobId(), jobConfigDTO.getDeployModeEnum());
      }
    }
    JobConfigDTO jobConfig = new JobConfigDTO();
    jobConfig.setStatus(JobConfigStatus.STOP);
    jobConfig.setEditor(operatorId);
    jobConfig.setId(id);
    jobConfig.setJobId("");
    //变更状态
    jobConfigService.updateJobConfigById(jobConfig);
  }

  @Override
  public void savepoint(Long id) {
    JobConfigDTO jobConfigDTO = jobConfigService.getJobConfigById(id);

    jobBaseServiceAO.checkSavepoint(jobConfigDTO);

    JobStandaloneInfo jobStandaloneInfo = flinkRestRpcAdapter
        .getJobInfoForStandaloneByAppId(jobConfigDTO.getJobId(), jobConfigDTO.getDeployModeEnum());
    if (jobStandaloneInfo == null || StringUtils.isNotEmpty(jobStandaloneInfo.getErrors())
        || !SystemConstants.STATUS_RUNNING.equals(jobStandaloneInfo.getState())) {
      log.warn(MessageConstants.MESSAGE_007, jobConfigDTO.getJobName());
      throw new BizException(MessageConstants.MESSAGE_007);
    }

    //1、 执行savepoint
    try {
      //yarn模式下和集群模式下统一目录是hdfs:///flink/savepoint/flink-pipeline/
      //LOCAL模式本地模式下保存在flink根目录下
      String targetDirectory = SystemConstants.DEFAULT_SAVEPOINT_ROOT_PATH + id;
      if (DeployModeEnum.LOCAL.equals(jobConfigDTO.getDeployModeEnum())) {
        String savepointDir = String.valueOf(systemConfigService.getSystemConfigByKey(SysConfigEnum.FLINK_LOCAL_SAVEPOINT_HOME.getKey()));
        targetDirectory = savepointDir + "/" + id;
      }

      commandRpcClinetAdapter.savepointForPerCluster(jobConfigDTO.getJobId(), targetDirectory, jobConfigDTO.getCreator());
    } catch (Exception e) {
      log.error(MessageConstants.MESSAGE_008, e);
      throw new BizException(MessageConstants.MESSAGE_008);
    }

    String savepointPath = flinkRestRpcAdapter.savepointPath(jobConfigDTO.getJobId(),
        jobConfigDTO.getDeployModeEnum());
    if (StringUtils.isEmpty(savepointPath)) {
      log.warn(MessageConstants.MESSAGE_009, jobConfigDTO);
      throw new BizException(MessageConstants.MESSAGE_009);
    }
    //2、 执行保存Savepoint到本地数据库
    savepointBackupService.insertSavepoint(id, savepointPath, new Date(), jobConfigDTO.getCreator());
  }


  @Override
  @Transactional(rollbackFor = Exception.class)
  public ResponseBean open(Long id, Long operatorId) {
    ResponseBean result = new ResponseBean();
    JobConfigDTO jobConfigDTO = jobConfigService.getJobConfigById(id);
    if (jobConfigDTO == null) {
      result.setCode(ResultCodeEnum.FAIL);
      result.setMessage("没有找到对应的任务");
      return result;
    }
    if ( jobConfigDTO.getJobTypeEnum() == JobTypeEnum.SQL_BATCH && StringUtils.isNotEmpty( jobConfigDTO.getCron() ) ) {
      ResponseBean result1 = batchJobRegister.registerJob(new BatchJob(id, jobConfigDTO.getJobName(), jobConfigDTO.getCron()));
      if ( result1.getCode() != 200 ) {
          return result1 ;
      }
    }

    ResponseBean result2 = jobConfigService.openOrClose(id, YN.Y, operatorId);
    if ( result2.getCode() != 200 ) {
      return result2 ;
    }

    result.setCode(ResultCodeEnum.SUCCESS);
    return result ;


  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public ResponseBean close(Long id, Long operatorId) {
    JobConfigDTO jobConfigDTO = jobConfigService.getJobConfigById(id);

    ResponseBean checkCloseResult = jobBaseServiceAO.checkClose(jobConfigDTO);
    if ( checkCloseResult.getCode() != 200 ) {
      return checkCloseResult ;
    }


    ResponseBean openOrCloseResult = jobConfigService.openOrClose(id, YN.N, operatorId);
    if ( checkCloseResult.getCode() != 200 ) {
      return openOrCloseResult ;
    }

    if (jobConfigDTO.getJobTypeEnum() == JobTypeEnum.SQL_BATCH) {
      ResponseBean deleteJobResult = batchJobRegister.deleteJob(id);
      if ( deleteJobResult.getCode() != 200  ){
        return deleteJobResult ;
      }
    }

    ResponseBean result = new ResponseBean();
    result.setCode(ResultCodeEnum.SUCCESS);
    return result ;

  }


  private void checkSysConfig(Map<String, String> systemConfigMap, DeployModeEnum deployModeEnum) {
    if (systemConfigMap == null) {
      throw new BizException(SysErrorEnum.SYSTEM_CONFIG_IS_NULL.getErrorMsg());
    }
    if (!systemConfigMap.containsKey(SysConfigEnum.FLINK_HOME.getKey())) {
      throw new BizException(SysErrorEnum.SYSTEM_CONFIG_IS_NULL_FLINK_HOME.getErrorMsg());
    }

    if (DeployModeEnum.LOCAL == deployModeEnum
        && !systemConfigMap.containsKey(SysConfigEnum.FLINK_REST_HTTP_ADDRESS.getKey())) {
      throw new BizException(SysErrorEnum.SYSTEM_CONFIG_IS_NULL_FLINK_REST_HTTP_ADDRESS.getErrorMsg());
    }

    if (DeployModeEnum.STANDALONE == deployModeEnum
        && !systemConfigMap.containsKey(SysConfigEnum.FLINK_REST_HA_HTTP_ADDRESS.getKey())) {
      throw new BizException(SysErrorEnum.SYSTEM_CONFIG_IS_NULL_FLINK_REST_HA_HTTP_ADDRESS.getErrorMsg());
    }
  }
}
