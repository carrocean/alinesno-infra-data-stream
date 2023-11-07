package com.alinesno.infra.data.flink.exchange.ao.impl;

import cn.hutool.core.date.DateTime;
import com.alibaba.fastjson.JSON;
import com.alinesno.infra.data.flink.entity.JobConfigEntity;
import com.alinesno.infra.data.flink.exchange.alarm.DingDingAlarm;
import com.alinesno.infra.data.flink.exchange.alarm.HttpAlarm;
import com.alinesno.infra.data.flink.exchange.ao.AlarmServiceAO;
import com.alinesno.infra.data.flink.exchange.dto.AlartLogDTO;
import com.alinesno.infra.data.flink.exchange.enums.AlarmLogStatusEnum;
import com.alinesno.infra.data.flink.exchange.enums.AlarmLogTypeEnum;
import com.alinesno.infra.data.flink.exchange.vo.CallbackDTO;
import com.alinesno.infra.data.flink.service.IAlartLogService;
import com.alinesno.infra.data.flink.service.IJobConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author zhp
 * @Description:
 * @date 2020-09-25
 * @time 19:50
 */
@Component
public class AlarmServiceAOImpl implements AlarmServiceAO {

  // 日志记录
  private static final Logger log = LoggerFactory.getLogger(AlarmServiceAOImpl.class);

  @Autowired
  private IAlartLogService alartLogService;

  @Autowired
  private DingDingAlarm dingDingAlarm;


  @Autowired
  private HttpAlarm httpAlarm;

  @Autowired
  private IJobConfigService jobConfigService;

  @Override
  public boolean sendForDingding(String url, String content, long jobConfigId) {

    boolean isSuccess = false;
    String failLog = "";
    try {
      isSuccess = dingDingAlarm.send(url, content);
    } catch (Exception e) {
      log.error("dingDingAlarm.send is error", e);
      failLog = e.getMessage();
    }
    this.insertLog(isSuccess, jobConfigId, failLog, content, AlarmLogTypeEnum.DINGDING);

    return isSuccess;
  }

  @Override
  public boolean sendForHttp(String url, CallbackDTO callbackDTO) {

    boolean isSuccess = false;
    String failLog = "";
    try {
      isSuccess = httpAlarm.send(url, callbackDTO);
    } catch (Exception e) {
      log.error("dingDingAlarm.send is error", e);
      failLog = e.getMessage();
    }
    this.insertLog(isSuccess, callbackDTO.getJobConfigId(), failLog, JSON.toJSONString(callbackDTO),
        AlarmLogTypeEnum.CALLBACK_URL);

    return isSuccess;
  }


  private void insertLog(boolean isSuccess, long jobConfigId, String failLog, String content,
      AlarmLogTypeEnum alarMLogTypeEnum) {
    JobConfigEntity jobConfig = jobConfigService.findById(jobConfigId);
    AlartLogDTO alartLogDTO = new AlartLogDTO();
    alartLogDTO.setJobConfigId(jobConfigId);
    alartLogDTO.setAlarMLogTypeEnum(alarMLogTypeEnum);
    alartLogDTO.setCreator(jobConfig.getOperatorId());
    alartLogDTO.setEditTime( new DateTime() );
    alartLogDTO.setMessage(content);
    if (jobConfig != null) {
      alartLogDTO.setJobName(jobConfig.getJobName());
    } else {
      alartLogDTO.setJobName("测试");
    }
    if (isSuccess) {
      alartLogDTO.setAlarmLogStatusEnum(AlarmLogStatusEnum.SUCCESS);
    } else {
      alartLogDTO.setAlarmLogStatusEnum(AlarmLogStatusEnum.FAIL);
      alartLogDTO.setFailLog(failLog);
    }
    alartLogService.addAlartLog(alartLogDTO);

  }
}
