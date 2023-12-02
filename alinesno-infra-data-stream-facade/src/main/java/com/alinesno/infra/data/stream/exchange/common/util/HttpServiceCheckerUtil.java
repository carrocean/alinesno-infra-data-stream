package com.alinesno.infra.data.stream.exchange.common.util;

import com.alinesno.infra.data.stream.exchange.common.FlinkYarnRestUriConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

/**
 * @author zhp
 * @Description:
 * @date 2020/11/09
 * @time 22:13
 */

public class HttpServiceCheckerUtil {

  // 日志记录
  private static final Logger log = LoggerFactory.getLogger(HttpServiceCheckerUtil.class);
  public static final int TIMEOUTMILLSECONDS = 2000;

  /**
   * 检查url地址连接是否正常
   *
   * @author zhp
   * @date 2020/11/09
   * @time 22:45
   */
  public static boolean checkUrlConnect(String url) {
    try {
      log.info("checkUrlConnect url is {}", url);
      RestTemplate restTemplate =  HttpUtil.buildRestTemplate( HttpUtil.TIME_OUT_20_S);
      restTemplate.exchange(url.trim(), HttpMethod.GET, new HttpEntity<String>(null,
          new HttpHeaders()), String.class);
    } catch (ResourceAccessException e) {
      if (e.getCause() instanceof ConnectException || e
          .getCause() instanceof SocketTimeoutException) {
        log.error("[checkUrlConnect]网络异常或者超时 url={}", url, e);
        return false;
      } else {
        log.warn("[checkUrlConnect]url={} 出错了 {}", e);
        return false;
      }
    } catch (Exception e) {
      log.error("[checkUrlConnect]url={} 出错了 {}", e);
      return false;
    }
    log.info("网络检查正常 url={}", url);
    return true;
  }


  public static void main(String[] args) {
//        String url = "https://youtube.com/";
    String url = "http://192.168.1.113:8088/";
//    System.out.println(HttpServiceCheckerUtil.isConnect(url));

    String request = cn.hutool.http.HttpUtil
        .get("http://192.168.1.113:8088/" + FlinkYarnRestUriConstants.URI_YARN_INFO);
    System.out.println(request);
  }

}
