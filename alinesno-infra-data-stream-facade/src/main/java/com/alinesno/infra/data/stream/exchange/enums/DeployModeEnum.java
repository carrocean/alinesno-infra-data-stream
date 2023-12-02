package com.alinesno.infra.data.stream.exchange.enums;

import com.alinesno.infra.data.stream.exchange.exceptions.BizException;
import org.apache.commons.lang3.StringUtils;

/**
 * @author zhp
 * @Description:
 * @date 2020-08-15
 * @time 20:41
 */
public enum DeployModeEnum {
  YARN_PER, STANDALONE, LOCAL, YARN_APPLICATION;

  public static DeployModeEnum getModel(String model) {
    if (StringUtils.isEmpty(model)) {
      throw new BizException("运行模式不能为空");
    }
    for (DeployModeEnum deployModeEnum : DeployModeEnum.values()) {
      if (deployModeEnum.name().equals(model.trim().toUpperCase())) {
        return deployModeEnum;
      }

    }
    throw new BizException("运行模式不存在");
  }
}
