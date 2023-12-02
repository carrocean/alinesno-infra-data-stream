package com.alinesno.infra.data.stream.exchange.rpc;

import com.alinesno.infra.data.stream.exchange.enums.DeployModeEnum;
import com.alinesno.infra.data.stream.exchange.rpc.model.JobStandaloneInfo;

/**
 * @author zhp
 * @Description:
 * @date 2020-09-18
 * @time 23:43
 */
public interface FlinkRestRpcAdapter {


  /**
   * Standalone 模式下获取状态
   *
   * @author zhp
   * @date 2020/11/3
   * @time 23:47
   */
  JobStandaloneInfo getJobInfoForStandaloneByAppId(String appId, DeployModeEnum deployModeEnum);


  /**
   * 基于flink rest API取消任务
   *
   * @author zhp
   * @date 2020/11/3
   * @time 22:50
   */
  void cancelJobForFlinkByAppId(String jobId, DeployModeEnum deployModeEnum);


  /**
   * 获取savepoint路径
   *
   * @author zhp
   * @date 2021/3/31
   * @time 22:01
   */
  String savepointPath(String jobId, DeployModeEnum deployModeEnum);


}
