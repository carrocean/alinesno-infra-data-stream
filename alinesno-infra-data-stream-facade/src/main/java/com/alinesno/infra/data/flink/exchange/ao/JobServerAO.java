package com.alinesno.infra.data.flink.exchange.ao;

import com.alinesno.infra.data.flink.exchange.common.ResponseBean;

/**
 * @author zhp
 * @Description:
 * @date 2020-07-20
 * @time 23:11
 */
public interface JobServerAO {

  /**
   * 启动任务
   *
   * @author zhp
   * @date 2020-07-20
   * @time 23:12
   */
  void start(Long id, Long savepointId, String operatorId);


  /**
   * 关闭任务
   *
   * @author zhp
   * @date 2020-07-20
   * @time 23:12
   */
  void stop(Long id, Long operatorId);


  /**
   * 执行savepoint
   *
   * @author zhp
   * @date 2020-09-21
   * @time 00:41
   */
  void savepoint(Long id);


  /**
   * 开启配置
   *
   * @author zhp
   * @date 2020-08-12
   * @time 21:14
   */
  ResponseBean open(Long id, Long operatorId);

  /**
   * 关闭配置
   *
   * @author zhp
   * @date 2020-08-12
   * @time 21:14
   */
  ResponseBean close(Long id, Long operatorId);
}
