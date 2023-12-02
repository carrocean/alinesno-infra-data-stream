package com.alinesno.infra.data.stream.exchange.ao.impl;

import com.alinesno.infra.common.facade.response.ResultCodeEnum;
import com.alinesno.infra.data.stream.exchange.ao.JobConfigAO;
import com.alinesno.infra.data.stream.exchange.common.ResponseBean;
import com.alinesno.infra.data.stream.exchange.dto.JobConfigDTO;
import com.alinesno.infra.data.stream.service.IJobAlarmConfigService;
import com.alinesno.infra.data.stream.service.IJobConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * @author zhp
 * @Description:
 * @date 2021/2/28
 * @time 11:25
 */
@Component
public class JobConfigAOImpl implements JobConfigAO {

  // 日志记录
  private static final Logger log = LoggerFactory.getLogger(JobConfigAOImpl.class);

  @Autowired
  private IJobConfigService jobConfigService;

  @Autowired
  private IJobAlarmConfigService jobAlarmConfigService;

  @Override
  @Transactional(rollbackFor = Exception.class)
  public ResponseBean addJobConfig(JobConfigDTO jobConfigDTO) {

    ResponseBean addJobResult = jobConfigService.addJobConfig(jobConfigDTO);
    if ( addJobResult.getCode() != 200 ) {
      return addJobResult ;

    }else {
      Map<String, Long> dataMap = (Map<String, Long>)addJobResult.getData() ;
      long jobConfigId = dataMap.get("jobID");
      jobAlarmConfigService.upSertBatchJobAlarmConfig(jobConfigDTO.getAlarmTypeEnumList(), jobConfigId, jobConfigDTO.getCreator());

    }
    ResponseBean result = new ResponseBean();
    result.setCode(ResultCodeEnum.SUCCESS) ;
    return result ;
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public ResponseBean updateJobConfigById(JobConfigDTO jobConfigDTO) {

    ResponseBean updateJobResult = jobConfigService.updateJobConfigByIdWithWriteHistory(jobConfigDTO);
    if ( updateJobResult.getCode() != 200 ){
         return updateJobResult ;
    }
    ResponseBean upSertBatchJobResult = jobAlarmConfigService.upSertBatchJobAlarmConfig(jobConfigDTO.getAlarmTypeEnumList(), jobConfigDTO.getId(), jobConfigDTO.getCreator());
    if ( upSertBatchJobResult.getCode() != 200 ){
      return upSertBatchJobResult ;
    }

    ResponseBean result = new ResponseBean();
    result.setCode(ResultCodeEnum.SUCCESS);
    return result;
  }
}
