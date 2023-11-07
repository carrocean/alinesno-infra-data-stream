package com.alinesno.infra.data.flink.exchange.factory;

import com.alinesno.infra.data.flink.exchange.ao.JobServerAO;
import com.alinesno.infra.data.flink.exchange.common.SystemConstants;
import com.alinesno.infra.data.flink.exchange.enums.DeployModeEnum;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import java.util.Map;

/**
 * @author zhp
 * @Description:
 * @date 2022/10/29
 */
@Component
public class JobServerAOFactory implements ApplicationContextAware {

  // 日志记录
  private static final Logger log = LoggerFactory.getLogger(JobServerAOFactory.class);


  private static Map<DeployModeEnum, JobServerAO> beanMap;

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    Map<String, JobServerAO> map = applicationContext.getBeansOfType(JobServerAO.class);
    beanMap = Maps.newHashMap();
    for (Map.Entry<String, JobServerAO> entry : map.entrySet()) {
      switch (entry.getKey()) {
        case SystemConstants.BEANNAME_JOBSTANDALONESERVERAO:
          beanMap.put(DeployModeEnum.LOCAL, entry.getValue());
          beanMap.put(DeployModeEnum.STANDALONE, entry.getValue());
          break;
        case SystemConstants.BEANNAME_JOBYARNSERVERAO:
          beanMap.put(DeployModeEnum.YARN_APPLICATION, entry.getValue());
          beanMap.put(DeployModeEnum.YARN_PER, entry.getValue());
          break;
        default:
          log.error("不存在的bean类型 name={}", entry.getKey());
          throw new RuntimeException("不存在的bean类型");

      }
    }
  }

  public static JobServerAO getJobServerAO(DeployModeEnum deployModeEnum) {
    return beanMap.get(deployModeEnum);
  }
}
