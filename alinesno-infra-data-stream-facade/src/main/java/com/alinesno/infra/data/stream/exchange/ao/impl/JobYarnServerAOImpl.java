package com.alinesno.infra.data.stream.exchange.ao.impl;

import com.alinesno.infra.common.facade.response.ResultCodeEnum;
import com.alinesno.infra.data.stream.commom.enums.JobTypeEnum;
import com.alinesno.infra.data.stream.entity.BatchJob;
import com.alinesno.infra.data.stream.exchange.ao.JobBaseServiceAO;
import com.alinesno.infra.data.stream.exchange.ao.JobServerAO;
import com.alinesno.infra.data.stream.exchange.common.MessageConstants;
import com.alinesno.infra.data.stream.exchange.common.ResponseBean;
import com.alinesno.infra.data.stream.exchange.common.SystemConstants;
import com.alinesno.infra.data.stream.exchange.common.util.IpUtil;
import com.alinesno.infra.data.stream.exchange.dto.JobConfigDTO;
import com.alinesno.infra.data.stream.exchange.dto.JobRunParamDTO;
import com.alinesno.infra.data.stream.exchange.enums.JobConfigStatus;
import com.alinesno.infra.data.stream.exchange.enums.SysErrorEnum;
import com.alinesno.infra.data.stream.exchange.enums.YN;
import com.alinesno.infra.data.stream.exchange.exceptions.BizException;
import com.alinesno.infra.data.stream.exchange.quartz.BatchJobManagerScheduler;
import com.alinesno.infra.data.stream.exchange.rpc.CommandRpcClinetAdapter;
import com.alinesno.infra.data.stream.exchange.rpc.YarnRestRpcAdapter;
import com.alinesno.infra.data.stream.exchange.rpc.model.JobInfo;
import com.alinesno.infra.data.stream.service.IJobConfigService;
import com.alinesno.infra.data.stream.service.ISavepointBackupService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.net.InetAddress;
import java.net.SocketException;
import java.util.Date;

/**
 * @author zhp
 * @Description:
 * @date 2020-07-20
 * @time 23:11
 */
@Component(SystemConstants.BEANNAME_JOBYARNSERVERAO)
public class JobYarnServerAOImpl implements JobServerAO {

  // 日志记录
  private static final Logger log = LoggerFactory.getLogger(JobYarnServerAOImpl.class);

  //最大重试次数
  private static final Integer TRY_TIMES = 2;

  @Autowired
  private IJobConfigService jobConfigService;


  @Autowired
  private YarnRestRpcAdapter yarnRestRpcAdapter;

  @Autowired
  private CommandRpcClinetAdapter commandRpcClinetAdapter;

  @Autowired
  private ISavepointBackupService savepointBackupService;

  @Autowired
  private JobBaseServiceAO jobBaseServiceAO;

  @Autowired
  private BatchJobManagerScheduler batchJobRegister;

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void start(Long id, Long savepointId, String operatorId) {

    InetAddress inetAddress = null;
    try {
      inetAddress = IpUtil.getInetAddress();
      //20230627
      log.info("当前网络IP地址:{},用于判断在容器中执行还是在宿主机执行 ", inetAddress.getHostName());
    }catch ( SocketException e ) {
      e.printStackTrace();
    }

    JobConfigDTO jobConfigDTO = jobConfigService.getJobConfigById(id);
    log.info("[{}]开始启动任务[{}]", operatorId, jobConfigDTO.getJobName());

    //1、检查jobConfigDTO 状态等参数
    jobBaseServiceAO.checkStart(jobConfigDTO);

    if ( StringUtils.isNotEmpty(jobConfigDTO.getJobId()) ) {
      this.stop(jobConfigDTO);
    }

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
    if (jobConfigDTO == null) {
      throw new BizException(SysErrorEnum.JOB_CONFIG_JOB_IS_NOT_EXIST.getErrorMsg());
    }
    log.info("[{}]开始停止任务[{}]", operatorId , jobConfigDTO.getJobName());
    //1、停止前做一次savepoint操作
    try {
      this.savepoint(id);
    } catch (Exception e) {
      log.error(MessageConstants.MESSAGE_008, e);
    }
    //2、停止任务
    this.stop(jobConfigDTO);
    JobConfigDTO jobConfig = new JobConfigDTO();
    jobConfig.setStatus(JobConfigStatus.STOP);
    jobConfig.setEditor(operatorId);
    jobConfig.setId(id);
    jobConfig.setJobId("");
    //3、变更状态
    jobConfigService.updateJobConfigById(jobConfig);
  }

  @Override
  public void savepoint(Long id) {
    JobConfigDTO jobConfigDTO = jobConfigService.getJobConfigById(id);

    jobBaseServiceAO.checkSavepoint(jobConfigDTO);

    JobInfo jobInfo = yarnRestRpcAdapter.getJobInfoForPerYarnByAppId(jobConfigDTO.getJobId());
    if (jobInfo == null) {
      log.warn(MessageConstants.MESSAGE_007, jobConfigDTO.getJobName());
      throw new BizException(MessageConstants.MESSAGE_007);
    }
    //1、 执行savepoint
    try {
      commandRpcClinetAdapter.savepointForPerYarn(jobInfo.getId(),
          SystemConstants.DEFAULT_SAVEPOINT_ROOT_PATH + id, jobConfigDTO.getJobId(), jobConfigDTO.getCreator());
    } catch (Exception e) {
      log.error(MessageConstants.MESSAGE_008, e);
      throw new BizException(MessageConstants.MESSAGE_008);
    }

    String savepointPath = yarnRestRpcAdapter
        .getSavepointPath(jobConfigDTO.getJobId(), jobInfo.getId());
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
    if ( jobConfigDTO.getJobTypeEnum() == JobTypeEnum.SQL_BATCH && StringUtils.isNotEmpty(jobConfigDTO.getCron()) ) {
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

    if ( jobConfigDTO.getJobTypeEnum() == JobTypeEnum.SQL_BATCH ) {
      ResponseBean deleteJobResult = batchJobRegister.deleteJob(id);
      if ( deleteJobResult.getCode() != 200  ){
        return deleteJobResult ;
      }

    }

    ResponseBean result = new ResponseBean();
    result.setCode(ResultCodeEnum.SUCCESS);
    return result ;

  }


  private void stop(JobConfigDTO jobConfigDTO) {
    Integer retryNum = 1;
    while (retryNum <= TRY_TIMES) {
      JobInfo jobInfo = yarnRestRpcAdapter.getJobInfoForPerYarnByAppId(jobConfigDTO.getJobId());
      log.info("任务[{}]当前状态为：{}", jobConfigDTO.getId(), jobInfo);
      if (jobInfo != null && SystemConstants.STATUS_RUNNING.equals(jobInfo.getStatus())) {
        log.info("执行停止操作 jobYarnInfo={} retryNum={} id={}", jobInfo, retryNum,
            jobConfigDTO.getJobId());
        yarnRestRpcAdapter.cancelJobForYarnByAppId(jobConfigDTO.getJobId(), jobInfo.getId());
      } else {
        log.info("任务已经停止 jobYarnInfo={} id={}", jobInfo, jobConfigDTO.getJobId());
        break;
      }
      retryNum++;
    }
  }
}
