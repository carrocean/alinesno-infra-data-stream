package com.alinesno.infra.data.stream.exchange.alarm.impl;

import com.alinesno.infra.data.stream.exchange.alarm.HttpAlarm;
import com.alinesno.infra.data.stream.exchange.common.util.HttpUtil;
import com.alinesno.infra.data.stream.exchange.enums.SysErrorEnum;
import com.alinesno.infra.data.stream.exchange.exceptions.BizException;
import com.alinesno.infra.data.stream.exchange.vo.CallbackDTO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 * @author zhp
 * @Description:http回调告警实现类
 * @date 2021/2/21
 * @time 11:38
 */
@Service
public class HttpAlarmImpl implements HttpAlarm {

  // 日志记录
  private static final Logger log = LoggerFactory.getLogger(HttpAlarmImpl.class);


  @Override
  public boolean send(String url, CallbackDTO callbackDTO) {
    if (StringUtils.isEmpty(url) || callbackDTO == null) {
      log.error("url={} is null or callbackDTO={} is null", url, callbackDTO);
      throw new BizException(SysErrorEnum.PARAM_IS_NULL.getErrorMsg());
    }
    RestTemplate restTemplate = HttpUtil.buildRestTemplate(HttpUtil.TIME_OUT_15_S);
    try {
      MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
      map.add("appId", callbackDTO.getAppId());
      map.add("deployMode", callbackDTO.getDeployMode());
      map.add("jobName", callbackDTO.getJobName());

      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
      HttpEntity<MultiValueMap<String, Object>> param = new HttpEntity(map, headers);
      //发起请求,服务地址，请求参数，返回消息体的数据类型
      ResponseEntity<String> response = restTemplate.postForEntity(url, param, String.class);
      if (response != null && response.getStatusCode().is2xxSuccessful()) {
        return true;
      }
      log.error("http请求失败 response={}", response);
      throw new BizException("http请求失败  status=" + response.getStatusCode().value());
    } catch (BizException bizException) {
      throw bizException;
    } catch (Exception e) {
      log.error("请求失败 url={} callbackDTO={}", url, callbackDTO, e);
      throw new BizException("http请求失败");
    }

  }

}
