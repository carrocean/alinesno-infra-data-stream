package com.alinesno.infra.data.stream.exchange.ao;

import com.alinesno.infra.data.stream.exchange.common.ResponseBean;
import com.alinesno.infra.data.stream.exchange.dto.JobConfigDTO;

/**
 * @author zhp
 * @Description:
 * @date 2021/2/28
 * @time 11:25
 */
public interface JobConfigAO {

  /**
   * 新增
   *
   * @author zhp
   * @date 2021/2/28
   * @time 11:26
   */
  ResponseBean addJobConfig(JobConfigDTO jobConfigDTO);


  /**
   * 修改参数
   *
   * @author zhp
   * @date 2021/2/28
   * @time 11:26
   */
  ResponseBean updateJobConfigById(JobConfigDTO jobConfigDTO);
}
