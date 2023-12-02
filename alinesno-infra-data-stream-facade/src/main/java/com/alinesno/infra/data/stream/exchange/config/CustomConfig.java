package com.alinesno.infra.data.stream.exchange.config;

import com.alinesno.infra.data.stream.commom.constant.SystemConstant;
import com.alinesno.infra.data.stream.exchange.common.util.IpUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhp
 * @Description:
 * @date 2021/1/22
 * @time 22:50
 */
@Configuration

public class CustomConfig {


  @Value("${server.port}")
  private Integer webPort;


  private String localUrl;

  public String getHttpLocalUrl() {
    return "http://127.0.0.1:" + webPort + SystemConstant.VIRGULE;
  }

  public String getUrlForDown() {
    String host_ip = System.getenv("HOST_IP");
    if ( host_ip != null ) {
      return String.format("http://%s:%s/download/", host_ip, webPort);
    }else{
      return String.format("http://%s:%s/download/", IpUtil.getInstance().getHostIp(), webPort);
    }

  }

  public String getUrlForReadLocal() {
    String host_ip = System.getenv("HOST_IP");
    if ( host_ip != null ) {
      return String.format("http://%s:%s/readLocal/", host_ip, webPort);
    } else{
      return String.format("http://%s:%s/readLocal/", IpUtil.getInstance().getHostIp(), webPort);
    }

  }

  public Integer getWebPort() {
    return webPort;
  }

  public void setWebPort(Integer webPort) {
    this.webPort = webPort;
  }

  public String getLocalUrl() {
    return localUrl;
  }

  public void setLocalUrl(String localUrl) {
    this.localUrl = localUrl;
  }
}
