package com.alinesno.infra.data.flink.exchange.common.util;

import com.alinesno.infra.data.flink.exchange.common.MessageConstants;
import com.alinesno.infra.data.flink.exchange.exceptions.BizException;
import org.apache.commons.lang3.StringUtils;

/**
 * @author zhp
 * @Description:
 * @date 2021/5/5
 * @time 10:46
 */
public class LinuxInfoUtil {

  public static String loginName() {
    String userName = System.getProperty("user.name");
    if (StringUtils.isNotEmpty(userName)) {
      return userName;
    }
    throw new BizException(MessageConstants.MESSAGE_011);

  }
}
