package com.alinesno.infra.data.flink.exchange.ao;

import com.alinesno.infra.data.flink.exchange.common.ResponseBean;
import com.alinesno.infra.data.flink.exchange.dto.JobConfigDTO;
import com.alinesno.infra.data.flink.exchange.dto.JobRunParamDTO;

/**
 * @author zhp
 * @Description:
 * @date 2021/3/28
 * @time 10:01
 */
public interface JobBaseServiceAO {

  /**
   * 提交任务前校验数据
   *
   * @author zhp
   * @date 2021/3/28
   * @time 10:12
   */
  void checkStart(JobConfigDTO jobConfigDTO);

  /**
   * Savepoint前校验数据
   *
   * @author zhp
   * @date 2021/3/31
   * @time 19:54
   */
  void checkSavepoint(JobConfigDTO jobConfigDTO);

  /**
   * 管配置检查
   *
   * @author zhp
   * @date 2021/3/31
   * @time 20:06
   */
  ResponseBean checkClose(JobConfigDTO jobConfigDTO);

  /**
   * @author zhp
   * @date 2021/3/28
   * @time 10:12
   */
  Long insertJobRunLog(JobConfigDTO jobConfigDTO);


  /**
   * 将配置的sql 写入文件并且返回运行所需参数
   *
   * @author zhp
   * @date 2021/3/28
   * @time 10:37
   */
  JobRunParamDTO writeSqlToFile(JobConfigDTO jobConfigDTO);


  /**
   * 异步执行任务
   *
   * @author zhp
   * @date 2021/3/28
   * @time 10:55
   */
  void aSyncExecJob(final JobRunParamDTO jobRunParamDTO, final JobConfigDTO jobConfig, final long jobRunLogId, final String savepointPath);


}
