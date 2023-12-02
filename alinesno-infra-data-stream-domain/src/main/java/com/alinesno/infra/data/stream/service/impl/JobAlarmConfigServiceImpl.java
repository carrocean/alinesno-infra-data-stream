package com.alinesno.infra.data.stream.service.impl;

import com.alinesno.infra.common.core.service.impl.IBaseServiceImpl;
import com.alinesno.infra.common.facade.response.ResultCodeEnum;
import com.alinesno.infra.data.stream.entity.JobAlarmConfigEntity;
import com.alinesno.infra.data.stream.exchange.common.ResponseBean;
import com.alinesno.infra.data.stream.exchange.enums.AlarmTypeEnum;
import com.alinesno.infra.data.stream.exchange.enums.SysConfigEnum;
import com.alinesno.infra.data.stream.exchange.enums.SysErrorEnum;
import com.alinesno.infra.data.stream.mapper.JobAlarmConfigMapper;
import com.alinesno.infra.data.stream.service.IJobAlarmConfigService;
import com.alinesno.infra.data.stream.service.ISystemConfigService;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 作业告警配置Service业务层处理
 *
 * @version 1.0.0
 * @author luoxiaodong
 */
@Service
public class JobAlarmConfigServiceImpl extends IBaseServiceImpl<JobAlarmConfigEntity, JobAlarmConfigMapper> implements IJobAlarmConfigService {
    //日志记录
    @SuppressWarnings("unused")
    private static final Logger log = LoggerFactory.getLogger(JobAlarmConfigServiceImpl.class);

    @Autowired
    private JobAlarmConfigMapper jobAlarmConfigMapper;

    @Autowired
    private ISystemConfigService systemConfigService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseBean upSertBatchJobAlarmConfig(List<AlarmTypeEnum> alarmTypeEnumList, Long jobId, Long operatorId) {
        ResponseBean result = new ResponseBean();
        if (jobId == null) {
            result.setCode(ResultCodeEnum.FAIL) ;
            result.setMessage(SysErrorEnum.JOB_CONFIG_PARAM_IS_NULL.getErrorMsg());
            return result ;
        }
        ResponseBean checkSysConfigResult = this.checkSysConfig(alarmTypeEnumList);
        if ( checkSysConfigResult.getCode() != 200 ) {
            return checkSysConfigResult;
        }

        jobAlarmConfigMapper.deleteByJobId(jobId);
        if (CollectionUtils.isNotEmpty(alarmTypeEnumList)) {
            List<JobAlarmConfigEntity> list = new ArrayList<>();
            for (AlarmTypeEnum alarmTypeEnum : alarmTypeEnumList) {
                JobAlarmConfigEntity jobAlarmConfig = new JobAlarmConfigEntity();
                jobAlarmConfig.setJobId(jobId);
                jobAlarmConfig.setType(alarmTypeEnum.getCode());

                jobAlarmConfig.setAddTime(new Date());
                list.add(jobAlarmConfig);
            }
            this.saveOrUpdateBatch(list) ;
//            jobAlarmConfigMapper.insertBatch(list);
        }

        result.setCode(ResultCodeEnum.SUCCESS) ;
        return result ;
    }

    @Override
    public List<AlarmTypeEnum> findByJobId(Long jobId) {

        List<JobAlarmConfigEntity> list = jobAlarmConfigMapper.selectByJobId(jobId);
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }
        List<AlarmTypeEnum> alarmTypeEnumList = new ArrayList<>();

        for (JobAlarmConfigEntity jobAlarmConfig : list) {
            alarmTypeEnumList.add(AlarmTypeEnum.getAlarmTypeEnum(jobAlarmConfig.getType()));
        }

        return alarmTypeEnumList;
    }

    @Override
    public Map<Long, List<AlarmTypeEnum>> findByJobIdList(List<String> jobIdList) {

        List<JobAlarmConfigEntity> list = jobAlarmConfigMapper.selectByJobIdList(jobIdList);
        if (CollectionUtils.isEmpty(list)) {
            return Collections.EMPTY_MAP;
        }
        Map<Long, List<AlarmTypeEnum>> jobId2List = new HashMap<>();

        for (JobAlarmConfigEntity jobAlarmConfig : list) {
            List<AlarmTypeEnum> alarmTypeEnumList = jobId2List.get(jobAlarmConfig.getJobId());
            if (CollectionUtils.isEmpty(alarmTypeEnumList)) {
                alarmTypeEnumList = new ArrayList<>();
            }
            alarmTypeEnumList.add(AlarmTypeEnum.getAlarmTypeEnum(jobAlarmConfig.getType()));
            jobId2List.put(jobAlarmConfig.getJobId(), alarmTypeEnumList);


        }
        return jobId2List;
    }

    private ResponseBean checkSysConfig(List<AlarmTypeEnum> alarmTypeEnumList) {
        ResponseBean result = new ResponseBean();
        if ( CollectionUtils.isEmpty(alarmTypeEnumList) ) {
            result.setCode(ResultCodeEnum.SUCCESS);
            return result;
        }
        for (AlarmTypeEnum alarmTypeEnum : alarmTypeEnumList) {
            switch (alarmTypeEnum) {
                case DINGDING:
                    if ( !systemConfigService.isExist(SysConfigEnum.DINGDING_ALARM_URL.getKey()) ) {
                        result.setCode(ResultCodeEnum.FAIL);
                        result.setMessage(SysErrorEnum.ALARM_DINGDING_NULL.getErrorMsg());
                        return result;
                    }
                    break;
                case CALLBACK_URL:
                    if ( !systemConfigService.isExist(SysConfigEnum.CALLBACK_ALARM_URL.getKey()) ) {
                        result.setCode(ResultCodeEnum.FAIL);
                        result.setMessage(SysErrorEnum.ALARM_HTTP_NULL.getErrorMsg());
                        return result;
                    }
                    break;
                default: {
                    log.warn("不支持的告警模式");
                    result.setCode(ResultCodeEnum.FAIL);
                    result.setMessage("不支持的告警模式");
                    return result;

                }
            }
        }

        result.setCode(ResultCodeEnum.SUCCESS);
        return result;

    }
}
