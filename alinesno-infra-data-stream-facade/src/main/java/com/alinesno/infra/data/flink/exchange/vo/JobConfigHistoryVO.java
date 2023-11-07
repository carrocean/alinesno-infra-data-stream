package com.alinesno.infra.data.flink.exchange.vo;

import cn.hutool.core.collection.CollectionUtil;
import com.alinesno.infra.data.flink.exchange.common.util.DateFormatUtils;
import com.alinesno.infra.data.flink.exchange.dto.JobConfigHistoryDTO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author zhp
 * @date 2021/5/5
 * @time 19:49
 */

public class JobConfigHistoryVO implements Serializable {

  private static final long serialVersionUID = 1L;

  private Long id;

  /**
   * job_config主表Id
   */
  private Long jobConfigId;

  /**
   * 任务名称
   */
  private String jobName;

  /**
   * 提交模式: standalone 、yarn 、yarn-session
   */
  private String deployMode;

  /**
   * flink运行配置
   */
  private String flinkRunConfig;

  /**
   * checkPoint配置
   */
  private String flinkCheckpointConfig;

  /**
   * udf地址及连接器jar 如http://xxx.xxx.com/flink-streaming-udf.jar
   */
  private String extJarPath;

  /**
   * 更新版本号
   */
  private Long version;

  /**
   * 创建时间
   */
  private String createTime;

  /**
   * 修改时间
   */
  private String editTime;

  private Long creator;

  private Long editor;

  /**
   * sql语句
   */
  private String flinkSql;


  public static JobConfigHistoryVO toVO(JobConfigHistoryDTO jobConfigHistoryDTO,
                                        boolean isFlinkSql) {
    if (jobConfigHistoryDTO == null) {
      return null;
    }
    JobConfigHistoryVO jobConfigHistoryVO = new JobConfigHistoryVO();
    jobConfigHistoryVO.setId(jobConfigHistoryDTO.getId());
    jobConfigHistoryVO.setJobConfigId(jobConfigHistoryDTO.getJobConfigId());
    jobConfigHistoryVO.setJobName(jobConfigHistoryDTO.getJobName());
    jobConfigHistoryVO.setDeployMode(jobConfigHistoryDTO.getDeployMode());
    jobConfigHistoryVO.setFlinkRunConfig(jobConfigHistoryDTO.getFlinkRunConfig());
    jobConfigHistoryVO.setFlinkCheckpointConfig(jobConfigHistoryDTO.getFlinkCheckpointConfig());
    jobConfigHistoryVO.setExtJarPath(jobConfigHistoryDTO.getExtJarPath());
    jobConfigHistoryVO.setVersion(jobConfigHistoryDTO.getVersion());
    jobConfigHistoryVO.setCreateTime(DateFormatUtils.toFormatString(jobConfigHistoryDTO.getCreateTime()));
    jobConfigHistoryVO.setEditTime(DateFormatUtils.toFormatString(jobConfigHistoryDTO.getEditTime()));
    jobConfigHistoryVO.setCreator(jobConfigHistoryDTO.getCreator());
    jobConfigHistoryVO.setEditor(jobConfigHistoryDTO.getEditor());
    if (isFlinkSql) {
      jobConfigHistoryVO.setFlinkSql(jobConfigHistoryDTO.getFlinkSql());
    }
    return jobConfigHistoryVO;
  }

  public static List<JobConfigHistoryVO> toListVO(
      List<JobConfigHistoryDTO> jobConfigHistoryDTOList) {
    if (CollectionUtil.isEmpty(jobConfigHistoryDTOList)) {
      return Collections.EMPTY_LIST;
    }
    List<JobConfigHistoryVO> list = new ArrayList<>();

    for (JobConfigHistoryDTO jobConfigHistoryDTO : jobConfigHistoryDTOList) {
      list.add(toVO(jobConfigHistoryDTO, Boolean.FALSE));
    }

    return list;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getJobConfigId() {
    return jobConfigId;
  }

  public void setJobConfigId(Long jobConfigId) {
    this.jobConfigId = jobConfigId;
  }

  public String getJobName() {
    return jobName;
  }

  public void setJobName(String jobName) {
    this.jobName = jobName;
  }

  public String getDeployMode() {
    return deployMode;
  }

  public void setDeployMode(String deployMode) {
    this.deployMode = deployMode;
  }

  public String getFlinkRunConfig() {
    return flinkRunConfig;
  }

  public void setFlinkRunConfig(String flinkRunConfig) {
    this.flinkRunConfig = flinkRunConfig;
  }

  public String getFlinkCheckpointConfig() {
    return flinkCheckpointConfig;
  }

  public void setFlinkCheckpointConfig(String flinkCheckpointConfig) {
    this.flinkCheckpointConfig = flinkCheckpointConfig;
  }

  public String getExtJarPath() {
    return extJarPath;
  }

  public void setExtJarPath(String extJarPath) {
    this.extJarPath = extJarPath;
  }


  public Long getVersion() {
    return version;
  }

  public void setVersion(Long version) {
    this.version = version;
  }

  public String getCreateTime() {
    return createTime;
  }

  public void setCreateTime(String createTime) {
    this.createTime = createTime;
  }

  public String getEditTime() {
    return editTime;
  }

  public void setEditTime(String editTime) {
    this.editTime = editTime;
  }

  public Long getCreator() {
    return creator;
  }

  public void setCreator(Long creator) {
    this.creator = creator;
  }

  public Long getEditor() {
    return editor;
  }

  public void setEditor(Long editor) {
    this.editor = editor;
  }

  public String getFlinkSql() {
    return flinkSql;
  }

  public void setFlinkSql(String flinkSql) {
    this.flinkSql = flinkSql;
  }
}
