package com.alinesno.infra.data.stream.exchange.vo;

import com.alinesno.infra.data.stream.exchange.common.util.DateFormatUtils;
import com.alinesno.infra.data.stream.exchange.dto.JobConfigDTO;
import com.alinesno.infra.data.stream.exchange.enums.AlarmTypeEnum;
import com.alinesno.infra.data.stream.exchange.enums.YN;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * @author zhp
 * @Description:
 * @date 2020-08-04
 * @time 01:28
 */

public class DetailJobConfigVO {

  private Long id;

  /**
   * 任务名称
   */
  private String jobName;


  /**
   * flink运行配置
   */
  private String jobId;


  private String deployMode;
  /**
   * 1:开启 0: 关闭
   */
  private Integer isOpen;


  private String openStr;


  private Integer status;


  private String statusStr;


  private String lastStartTime;

  /**
   * 创建时间
   */
  private String createTime;

  /**
   * 修改时间
   */
  private String editTime;

  /**
   * flink运行配置
   */
  private String flinkRunConfig;

  /**
   * flink运行配置
   */
  private String flinkCheckpointConfig;


  /**
   * 二方jar udf、 连接器 等jar如http://xxx.xxx.com/flink-streaming-udf.jar
   */
  private String extJarPath;

  /**
   * sql语句
   */
  private String flinkSql;


  /**
   * 任务类型
   */
  private Integer jobType;

  /**
   * 启动jar可能需要使用的自定义参数
   */
  private String customArgs;

  /**
   * 程序入口类
   */
  private String customMainClass;

  /**
   * 自定义jar的http地址 如:http://ccblog.cn/xx.jar
   */
  private String customJarUrl;

  private List<Integer> types;


  public static DetailJobConfigVO toVO(JobConfigDTO jobConfigDTO) {
    if (jobConfigDTO == null) {
      return null;
    }
    DetailJobConfigVO detailJobConfigVO = new DetailJobConfigVO();
    detailJobConfigVO.setId(jobConfigDTO.getId());
    detailJobConfigVO.setJobName(jobConfigDTO.getJobName());
    detailJobConfigVO.setFlinkRunConfig(jobConfigDTO.getFlinkRunConfig());
    if (StringUtils.isNotEmpty(jobConfigDTO.getFlinkCheckpointConfig())) {
      detailJobConfigVO.setFlinkCheckpointConfig(
          jobConfigDTO.getFlinkCheckpointConfig().replaceAll("\"", "&quot;"));
    }
    detailJobConfigVO.setJobId(jobConfigDTO.getJobId());
    detailJobConfigVO.setIsOpen(jobConfigDTO.getIsOpen());
    detailJobConfigVO.setOpenStr(YN.getYNByValue(jobConfigDTO.getIsOpen()).getDescribe());
    detailJobConfigVO.setStatusStr(jobConfigDTO.getStatus().getDesc());
    detailJobConfigVO.setStatus(jobConfigDTO.getStatus().getCode());
    detailJobConfigVO
        .setLastStartTime(DateFormatUtils.toFormatString(jobConfigDTO.getLastStartTime()));
    detailJobConfigVO.setCreateTime(DateFormatUtils.toFormatString(jobConfigDTO.getCreateTime()));
    detailJobConfigVO.setEditTime(DateFormatUtils.toFormatString(jobConfigDTO.getEditTime()));
    detailJobConfigVO.setFlinkSql(jobConfigDTO.getFlinkSql());
    detailJobConfigVO.setDeployMode(jobConfigDTO.getDeployModeEnum().name());
    detailJobConfigVO.setExtJarPath(jobConfigDTO.getExtJarPath());
    if (CollectionUtils.isNotEmpty(jobConfigDTO.getAlarmTypeEnumList())) {

      List<AlarmTypeEnum> alarmTypeEnumList = jobConfigDTO.getAlarmTypeEnumList();
      List<Integer> types = Lists.newArrayList();
      for (AlarmTypeEnum alarmTypeEnum : alarmTypeEnumList) {
        types.add(alarmTypeEnum.getCode());
      }
      detailJobConfigVO.setTypes(types);
    }

    detailJobConfigVO.setCustomArgs(jobConfigDTO.getCustomArgs());
    detailJobConfigVO.setCustomJarUrl(jobConfigDTO.getCustomJarUrl());
    detailJobConfigVO.setCustomMainClass(jobConfigDTO.getCustomMainClass());
    if (jobConfigDTO.getJobTypeEnum() != null) {
      detailJobConfigVO.setJobType(jobConfigDTO.getJobTypeEnum().getCode());
    }

    return detailJobConfigVO;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getJobName() {
    return jobName;
  }

  public void setJobName(String jobName) {
    this.jobName = jobName;
  }

  public String getJobId() {
    return jobId;
  }

  public void setJobId(String jobId) {
    this.jobId = jobId;
  }

  public String getDeployMode() {
    return deployMode;
  }

  public void setDeployMode(String deployMode) {
    this.deployMode = deployMode;
  }

  public Integer getIsOpen() {
    return isOpen;
  }

  public void setIsOpen(Integer isOpen) {
    this.isOpen = isOpen;
  }

  public String getOpenStr() {
    return openStr;
  }

  public void setOpenStr(String openStr) {
    this.openStr = openStr;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public String getStatusStr() {
    return statusStr;
  }

  public void setStatusStr(String statusStr) {
    this.statusStr = statusStr;
  }

  public String getLastStartTime() {
    return lastStartTime;
  }

  public void setLastStartTime(String lastStartTime) {
    this.lastStartTime = lastStartTime;
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

  public String getFlinkSql() {
    return flinkSql;
  }

  public void setFlinkSql(String flinkSql) {
    this.flinkSql = flinkSql;
  }

  public Integer getJobType() {
    return jobType;
  }

  public void setJobType(Integer jobType) {
    this.jobType = jobType;
  }

  public String getCustomArgs() {
    return customArgs;
  }

  public void setCustomArgs(String customArgs) {
    this.customArgs = customArgs;
  }

  public String getCustomMainClass() {
    return customMainClass;
  }

  public void setCustomMainClass(String customMainClass) {
    this.customMainClass = customMainClass;
  }

  public String getCustomJarUrl() {
    return customJarUrl;
  }

  public void setCustomJarUrl(String customJarUrl) {
    this.customJarUrl = customJarUrl;
  }

  public List<Integer> getTypes() {
    return types;
  }

  public void setTypes(List<Integer> types) {
    this.types = types;
  }
}
