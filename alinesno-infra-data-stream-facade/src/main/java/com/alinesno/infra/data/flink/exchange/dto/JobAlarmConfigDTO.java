package com.alinesno.infra.data.flink.exchange.dto;

import cn.hutool.core.collection.CollectionUtil;
import com.alinesno.infra.data.flink.entity.JobAlarmConfigEntity;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author zhp
 * @date 2021/2/27
 * @time 17:09
 */
public class JobAlarmConfigDTO {

  private Long id;

  /**
   * job_config主表id
   */
  private Long jobId;

  /**
   * 类型 1:钉钉告警 2:url回调 3:异常自动拉起任务
   */
  private Integer type;


  public static JobAlarmConfigDTO toDTO(JobAlarmConfigEntity jobAlarmConfig) {
    if (jobAlarmConfig == null) {
      return null;
    }
    JobAlarmConfigDTO jobAlarmConfigDTO = new JobAlarmConfigDTO();
    jobAlarmConfigDTO.setId(jobAlarmConfig.getId());
    jobAlarmConfigDTO.setJobId(jobAlarmConfig.getJobId());
    jobAlarmConfigDTO.setType(jobAlarmConfig.getType());
    return jobAlarmConfigDTO;
  }

  public static JobAlarmConfigEntity toEntity(JobAlarmConfigDTO jobAlarmConfigDTO) {
    if (jobAlarmConfigDTO == null) {
      return null;
    }
    JobAlarmConfigEntity jobAlarmConfig = new JobAlarmConfigEntity();
    jobAlarmConfig.setId(jobAlarmConfigDTO.getId());
    jobAlarmConfig.setJobId(jobAlarmConfigDTO.getJobId());
    jobAlarmConfig.setType(jobAlarmConfigDTO.getType());
    return jobAlarmConfig;
  }

  public static List<JobAlarmConfigEntity> toEntityList(List<JobAlarmConfigDTO> jobAlarmConfigDTOList) {
    if (CollectionUtils.isEmpty(jobAlarmConfigDTOList)) {
      return Collections.emptyList();
    }
    List<JobAlarmConfigEntity> list = new ArrayList<>();

    for (JobAlarmConfigDTO jobAlarmConfigDTO : jobAlarmConfigDTOList) {
      list.add(toEntity(jobAlarmConfigDTO));
    }
    return list;
  }


  public static List<JobAlarmConfigDTO> toListDTO(List<JobAlarmConfigEntity> jobAlarmConfigList) {
    if (CollectionUtil.isEmpty(jobAlarmConfigList)) {
      return Collections.emptyList();
    }
    List<JobAlarmConfigDTO> list = new ArrayList<>();

    for (JobAlarmConfigEntity jobAlarmConfig : jobAlarmConfigList) {
      list.add(toDTO(jobAlarmConfig));
    }
    return list;

  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getJobId() {
    return jobId;
  }

  public void setJobId(Long jobId) {
    this.jobId = jobId;
  }

  public Integer getType() {
    return type;
  }

  public void setType(Integer type) {
    this.type = type;
  }
}
