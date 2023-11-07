package com.alinesno.infra.data.flink.exchange.common.util;

import com.alinesno.infra.data.flink.exchange.common.SystemConstants;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;

/**
 * 加密
 *
 * @author zhp
 * @date 2020-07-13
 * @time 23:30
 */

public class Md5Utils {

 // 日志记录
 private static final Logger log = LoggerFactory.getLogger(Md5Utils.class);

 private  static final char HEXDIGITS[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
     'A', 'B', 'C', 'D', 'E', 'F'};
  public static String getMD5String(String value) {

    return DigestUtils.md5Hex(value);

  }

  public static final String md5(String s) {

    try {
      byte[] btInput = s.getBytes(SystemConstants.CODE_UTF_8);
      MessageDigest mdInst = MessageDigest.getInstance("MD5");
      mdInst.update(btInput);
      byte[] md = mdInst.digest();

      int j = md.length;
      char str[] = new char[j * 2];
      int k = 0;
      for (int i = 0; i < j; i++) {
        byte byte0 = md[i];
        str[k++] = HEXDIGITS[byte0 >>> 4 & 0xf];
        str[k++] = HEXDIGITS[byte0 & 0xf];
      }
      return new String(str);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  public static void main(String[] args) {
    log.debug("123456 MD5:{}",getMD5String("123456"));
  }
}
