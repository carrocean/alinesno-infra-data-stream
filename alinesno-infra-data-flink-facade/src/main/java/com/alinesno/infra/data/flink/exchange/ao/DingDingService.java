package com.alinesno.infra.data.flink.exchange.ao;

import com.alinesno.infra.data.flink.exchange.dto.JobConfigDTO;
import com.alinesno.infra.data.flink.exchange.enums.DeployModeEnum;

/**
 * 钉钉服务
 *
 * @author wxj
 * @version V1.0
 * @date 2022年1月5日 下午3:00:49
 */
public interface DingDingService {

  /**
   * 定制化告警通知
   *
   * @param cusContent
   * @param jobConfigDTO
   * @param deployModeEnum
   * @author wxj
   * @date 2022年1月5日 上午10:51:19
   * @version V1.0
   */
  void doAlarmNotify(String cusContent, JobConfigDTO jobConfigDTO, DeployModeEnum deployModeEnum);

}
