package com.alinesno.infra.data.stream.exchange.common.util;

import com.alinesno.infra.data.stream.exchange.dto.UserSession;
import org.apache.commons.codec.binary.Base64;

/**
 * @author zhp
 * @Description:
 * @date 2020-07-13
 * @time 23:13
 */
public class Base64Coded {

  /**
   * base64 解码
   *
   * @author zhp
   * @date 2020-07-13
   * @time 23:16
   */
  public static String decode(byte[] bytes) {
    return new String(Base64.decodeBase64(bytes));
  }

  /**
   * base64 编码
   *
   * @author zhp
   * @date 2020-07-13
   * @time 23:16
   */
  public static String encode(byte[] bytes) {
    return new String(Base64.encodeBase64(bytes));
  }


  public static void main(String[] args) {
    String string = UserSession.toJsonString(1, "zhp", "123456");
    //编码
    String encode = encode(string.getBytes());
    System.out.println(string + "\t编码后的字符串为：" + encode);
    //解码
    String decode = decode(encode.getBytes());
    System.out.println(encode + "\t字符串解码后为：" + decode + " " + UserSession.toUserSession(decode));

  }

}
