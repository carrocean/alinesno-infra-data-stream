package com.alinesno.infra.data.flink.exchange.dto;

import com.alinesno.infra.data.flink.exchange.common.SystemConstants;
import com.alinesno.infra.data.flink.exchange.enums.SysConfigEnum;

import java.util.Map;

/**
 * @author zhp
 * @Description:
 * @date 2020-08-17
 * @time 20:14
 */

public class JobRunParamDTO {

  /**
   * flink bin目录地址
   */
  private String flinkBinPath;

  /**
   * flink 运行参数 如：
   */
  private String flinkRunParam;

  /**
   * sql语句存放的目录
   */
  private String sqlPath;


  /**
   * checkpointConfig
   */
  private String flinkCheckpointConfig;

  /**
   * flink_pipeline 所在目录 如：/use/local/flink_pipeline
   */
  private String sysHome;

  /**
   * 主类jar地址
   */
  private String mainJarPath;

  private String sqlUrl;


  public JobRunParamDTO(String flinkBinPath,
      String flinkRunParam,
      String sqlPath,
      String sysHome,
      String flinkCheckpointConfig, String sqlUrl) {
    this.flinkBinPath = flinkBinPath;
    this.flinkRunParam = flinkRunParam;
    this.sqlPath = sqlPath;
    this.sysHome = sysHome;
    this.flinkCheckpointConfig = flinkCheckpointConfig;
    this.sqlUrl = sqlUrl;
  }

  public static JobRunParamDTO buildJobRunParam(Map<String, String> systemConfigMap,
                                                JobConfigDTO jobConfigDTO, String sqlPath, String sqlUrl) {

    String flinkBinPath = SystemConstants
        .buildFlinkBin(systemConfigMap.get(SysConfigEnum.FLINK_HOME.getKey()));

    String flinkRunParam = jobConfigDTO.getFlinkRunConfig();

    String sysHome = systemConfigMap.get(SysConfigEnum.FLINK_STREAMING_PLATFORM_WEB_HOME.getKey());

    JobRunParamDTO jobRunParamDTO = new JobRunParamDTO(
        flinkBinPath,
        flinkRunParam,
        sqlPath,
        sysHome,
        jobConfigDTO.getFlinkCheckpointConfig(), sqlUrl
    );

    return jobRunParamDTO;

  }

  public String getFlinkBinPath() {
    return flinkBinPath;
  }

  public void setFlinkBinPath(String flinkBinPath) {
    this.flinkBinPath = flinkBinPath;
  }

  public String getFlinkRunParam() {
    return flinkRunParam;
  }

  public void setFlinkRunParam(String flinkRunParam) {
    this.flinkRunParam = flinkRunParam;
  }

  public String getSqlPath() {
    return sqlPath;
  }

  public void setSqlPath(String sqlPath) {
    this.sqlPath = sqlPath;
  }

  public String getFlinkCheckpointConfig() {
    return flinkCheckpointConfig;
  }

  public void setFlinkCheckpointConfig(String flinkCheckpointConfig) {
    this.flinkCheckpointConfig = flinkCheckpointConfig;
  }

  public String getSysHome() {
    return sysHome;
  }

  public void setSysHome(String sysHome) {
    this.sysHome = sysHome;
  }

  public String getMainJarPath() {
    return mainJarPath;
  }

  public void setMainJarPath(String mainJarPath) {
    this.mainJarPath = mainJarPath;
  }

  public String getSqlUrl() {
    return sqlUrl;
  }

  public void setSqlUrl(String sqlUrl) {
    this.sqlUrl = sqlUrl;
  }

  @Override
  public String toString() {
    return "JobRunParamDTO{" +
            "flinkBinPath='" + flinkBinPath + '\'' +
            ", flinkRunParam='" + flinkRunParam + '\'' +
            ", sqlPath='" + sqlPath + '\'' +
            ", flinkCheckpointConfig='" + flinkCheckpointConfig + '\'' +
            ", sysHome='" + sysHome + '\'' +
            ", mainJarPath='" + mainJarPath + '\'' +
            ", sqlUrl='" + sqlUrl + '\'' +
            '}';
  }
}
