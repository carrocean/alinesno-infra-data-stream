package com.alinesno.infra.data.flink.exchange.ao.impl;

import cn.hutool.core.date.DateTime;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.api.config.annotation.NacosValue;
import com.alinesno.infra.data.flink.exchange.ao.DingDingService;
import com.alinesno.infra.data.flink.exchange.common.util.Md5Utils;
import com.alinesno.infra.data.flink.exchange.dto.AlartLogDTO;
import com.alinesno.infra.data.flink.exchange.dto.JobConfigDTO;
import com.alinesno.infra.data.flink.exchange.enums.AlarmLogStatusEnum;
import com.alinesno.infra.data.flink.exchange.enums.AlarmLogTypeEnum;
import com.alinesno.infra.data.flink.exchange.enums.DeployModeEnum;
import com.alinesno.infra.data.flink.exchange.utils.HttpClientToolUtils;
import com.alinesno.infra.data.flink.entity.JobConfigEntity;
import com.alinesno.infra.data.flink.service.IAlartLogService;
import com.alinesno.infra.data.flink.service.IJobConfigService;
import com.alinesno.infra.data.flink.service.IJobRunLogService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.*;

/**
 * @author wxj
 * @version V1.0
 * @date 2022年1月5日 下午3:02:00
 */
@Component
public class DingDingServiceImpl implements DingDingService {

  // 日志记录
  private static final Logger log = LoggerFactory.getLogger(DingDingServiceImpl.class);

  @Autowired
  private IAlartLogService alartLogService;

  @Autowired
  private IJobRunLogService jobRunLogService;

  @Autowired
  private IJobConfigService jobConfigService;

  @Override
  public void doAlarmNotify(String cusContent, JobConfigDTO jobConfigDTO,
      DeployModeEnum deployModeEnum) {
    long id = jobConfigDTO.getId();
    if (StringUtils.isNotBlank(this.alarmDingtalkTelephone)) {
      if (this.alarmDingTalkTasks.contains(id)) {
        String content = "大数据实时计算告警，" + cusContent;
        this.sendTelephoeNotify(id, this.alarmDingtalkTelephone, this.dingtalkTemplateId, 3,
            content);
      }
    }
    String un = this.alarmDingmsgUser;
    if (StringUtils.isNotBlank(un)) {
      if (this.alarmDingMsgTasks.contains(id)) {
        String content = "【大数据实时计算告警】\n" + cusContent + "\n通知时间：" + DateFormatUtils
            .format(new Date(), "yyyy-MM-dd HH:mm:ss");
        if (un.indexOf(",") > 0) {
          for (String str : un.split(",")) {
            if (StringUtils.isNotBlank(str)) {
              this.sendDingDingMsg(id, str, content);
            }
          }
        } else {
          this.sendDingDingMsg(id, this.alarmDingmsgUser, content);
        }
      }
    }
  }

  @NacosValue(value = "${DINGTALK_URL:}", autoRefreshed = true)
  private String dingtalkUrl;

  @NacosValue(value = "${DINGTALK_TEMPLATE_ID:}", autoRefreshed = true)
  private String dingtalkTemplateId;

  @NacosValue(value = "${DINGTALK_APPKEY:}", autoRefreshed = true)
  private String dingtalkAppkey;

  @NacosValue(value = "${DINGMSG_NOTIFY_URL:}", autoRefreshed = true)
  private String dingmsgNotifyUrl;

  @NacosValue(value = "${DINGMSG_APP_ID:}", autoRefreshed = true)
  private String dingmsgAppId;

  @NacosValue(value = "${DINGMSG_APP_KEY:}", autoRefreshed = true)
  private String dingmsgAppKey;

  private Set<Long> alarmDingTalkTasks = new HashSet<Long>();

  private Set<Long> alarmDingMsgTasks = new HashSet<Long>();

  @NacosValue(value = "${ALARM_DINGTALK_TASKS:}", autoRefreshed = true)
  public void setAlarmDingTalkTasks(String alarmDingTalkTasks) {
    this.alarmDingTalkTasks.clear();
    if (StringUtils.isBlank(alarmDingTalkTasks)) {
      return;
    }
    String[] ids = alarmDingTalkTasks.split(",");
    for (String id : ids) {
      if (isNumeric(id)) {
        this.alarmDingTalkTasks.add(Long.valueOf(id));
      }
    }
  }

  @NacosValue(value = "${ALARM_DINGMSG_TASKS:}", autoRefreshed = true)
  public void setAlarmDingMsgTasks(String alarmDingMsgTasks) {
    this.alarmDingMsgTasks.clear();
    if (StringUtils.isBlank(alarmDingMsgTasks)) {
      return;
    }
    String[] ids = alarmDingMsgTasks.split(",");
    for (String id : ids) {
      if (isNumeric(id)) {
        this.alarmDingMsgTasks.add(Long.valueOf(id));
      }
    }
  }

  @NacosValue(value = "${ALARM_DINGTALK_TELEPHONE:}", autoRefreshed = true)
  private String alarmDingtalkTelephone;

  @NacosValue(value = "${ALARM_DINGMSG_USER:}", autoRefreshed = true)
  private String alarmDingmsgUser;

  /**
   * 判断是否为数字
   *
   * @param str
   * @return
   * @author wxj
   * @date 2022年1月5日 下午3:22:29
   * @version V1.0
   */
  public static boolean isNumeric(String str) {
    for (int i = 0; i < str.length(); i++) {
      log.debug("charAt({}):{}",i,str.charAt(i));
      if (!Character.isDigit(str.charAt(i))) {
        return false;
      }
    }
    return true;
  }

  /**
   * 计算签名
   *
   * @param param
   * @param appKey
   * @return
   * @author wxj
   * @date 2022年1月5日 下午3:22:08
   * @version V1.0
   */
  public static String getSign(Map<String, String> param, String appKey) {
    Map<String, String> treeMap = new TreeMap<>(param);
    StringBuilder signBuilder = new StringBuilder();
    for (String key : treeMap.keySet()) {
      signBuilder.append("&").append(key).append("=").append(treeMap.get(key));
    }
    signBuilder.append(appKey);
    String signStr = StringUtils.substring(signBuilder.toString(), 1);  // 去掉第一个&
    return Md5Utils.md5(signStr);
  }

  /**
   * 电话告警
   *
   * @param jobConfigId
   * @param mobile
   * @param templateId,
   * @param count
   * @param content
   * @return
   * @author wxj
   * @date 2022年1月5日 下午3:13:38
   * @version V1.0
   */
  public JSONObject sendTelephoeNotify(long jobConfigId, String mobile, String templateId,
      Integer count, String content) {
    Map<String, String> param = new HashMap<>();
    param.put("mobile", mobile);
    param.put("templateId", templateId);
    param.put("count", count.toString());
    param.put("param", content);
    String url = this.dingtalkUrl;
    String sign = getSign(param, this.dingtalkAppkey);
    param.put("sign", sign);
    JSONObject result = new JSONObject();
    try {
      String ret = HttpClientToolUtils.doPost(url, param, null, 6000);
      log.warn("电话告警，任务[{}]请求 {} 参数：mobile={},templateId={},count={},content={} \n返回：{}",
          jobConfigId, url, mobile, templateId, count, content, ret);
      if (StringUtils.isBlank(ret)) {
        result.put("ret", false);
        result.put("msg", "接口调用异常，返回为空");
        String failLog =
            "电话告警，任务[" + jobConfigId + "]请求 " + url + " 参数：mobile=" + mobile + ",templateId="
                + templateId + ",count=" + count + ",content=" + content + " \n返回：" + result
                .toJSONString();
        this.insertLog(false, jobConfigId, failLog, content, AlarmLogTypeEnum.OTHER);
        return result;
      }
      JSONObject retJson = JSON.parseObject(ret);
      if (retJson.containsKey("code") && retJson.getInteger("code") != 200) {
        result.put("ret", false);
        result.put("msg", retJson.getString("msg"));
        String failLog =
            "电话告警，任务[" + jobConfigId + "]请求 " + url + " 参数：mobile=" + mobile + ",templateId="
                + templateId + ",count=" + count + ",content=" + content + " \n返回：" + result
                .toJSONString();
        this.insertLog(false, jobConfigId, failLog, content, AlarmLogTypeEnum.OTHER);
        return result;
      }
      result.put("ret", true);
      result.put("msg", "成功");
      String failLog =
          "电话告警，任务[" + jobConfigId + "]请求 " + url + " 参数：mobile=" + mobile + ",templateId="
              + templateId + ",count=" + count + ",content=" + content + " \n返回：" + result
              .toJSONString();
      this.insertLog(true, jobConfigId, failLog, content, AlarmLogTypeEnum.OTHER);
      return result;
    } catch (Exception e) {
      String failLog =
          "电话告警，任务[" + jobConfigId + "]请求 " + url + " 参数：mobile=" + mobile + ",templateId="
              + templateId + ",count=" + count + ",content=" + content + " \n异常：" + e.getMessage();
      log.error(failLog, e);
      result.put("ret", false);
      result.put("msg", "接口调用失败");
      this.insertLog(false, jobConfigId, failLog, content, AlarmLogTypeEnum.OTHER);
      return result;
    }
  }

  /**
   * 钉钉消息告警
   *
   * @param jobConfigId
   * @param un
   * @param content
   * @return
   * @author wxj
   * @date 2022年1月5日 下午3:13:38
   * @version V1.0
   */
  public JSONObject sendDingDingMsg(long jobConfigId, String un, String content) {
    Map<String, String> param = new HashMap<>();
    param.put("un", un);
    param.put("appid", this.dingmsgAppId);
    param.put("content", content);
    String url = this.dingmsgNotifyUrl;
    String sign = getSign(param, this.dingmsgAppKey);
    param.put("sign", sign);
    JSONObject result = new JSONObject();
    try {
      String ret = HttpClientToolUtils.doPost(url, param, null, 6000);
      log.warn("钉钉告警，任务[{}]请求 {} 参数：un={},content={} \n返回：{}", jobConfigId, url, un, content, ret);
      if (StringUtils.isBlank(ret)) {
        result.put("ret", false);
        result.put("msg", "接口调用异常，返回为空");
        String failLog =
            "钉钉告警，任务[" + jobConfigId + "]请求 " + url + " 参数：un=" + un + ",content=" + content
                + " \n返回：" + result.toJSONString();
        this.insertLog(false, jobConfigId, failLog, content, AlarmLogTypeEnum.OTHER);
        return result;
      }
      JSONObject retJson = JSON.parseObject(ret);
      if (retJson.containsKey("code") && retJson.getInteger("code") != 200) {
        result.put("ret", false);
        result.put("msg", retJson.getString("msg"));
        String failLog =
            "钉钉告警，任务[" + jobConfigId + "]请求 " + url + " 参数：un=" + un + ",content=" + content
                + " \n返回：" + result.toJSONString();
        this.insertLog(false, jobConfigId, failLog, content, AlarmLogTypeEnum.OTHER);
        return result;
      }
      result.put("ret", true);
      result.put("msg", "成功");
      String failLog =
          "钉钉告警，任务[" + jobConfigId + "]请求 " + url + " 参数：un=" + un + ",content=" + content
              + " \n返回：" + result.toJSONString();
      this.insertLog(true, jobConfigId, failLog, content, AlarmLogTypeEnum.OTHER);
      return result;
    } catch (Exception e) {
      String failLog =
          "钉钉告警，任务[" + jobConfigId + "]请求 " + url + " 参数：un=" + un + ",content=" + content
              + " \n异常：" + e.getMessage();
      log.error(failLog, e);
      result.put("ret", false);
      result.put("msg", "接口调用失败");
      this.insertLog(false, jobConfigId, failLog, content, AlarmLogTypeEnum.OTHER);
      return result;
    }
  }

  private void insertLog(boolean isSuccess, long jobConfigId, String failLog, String content,
      AlarmLogTypeEnum alarMLogTypeEnum) {
    JobConfigEntity jobConfig = jobConfigService.findById(jobConfigId);
    AlartLogDTO alartLogDTO = new AlartLogDTO();
    alartLogDTO.setJobConfigId(jobConfigId);
    alartLogDTO.setAlarMLogTypeEnum(alarMLogTypeEnum);
    alartLogDTO.setMessage(content);
    alartLogDTO.setFailLog(failLog);
    alartLogDTO.setEditTime( new DateTime() );
    alartLogDTO.setCreator(jobConfig.getOperatorId());
    if (jobConfig != null) {
      alartLogDTO.setJobName(jobConfig.getJobName());
    } else {
      alartLogDTO.setJobName("测试");
    }
    alartLogDTO.setAlarmLogStatusEnum(isSuccess ? AlarmLogStatusEnum.SUCCESS : AlarmLogStatusEnum.FAIL);
    alartLogService.addAlartLog(alartLogDTO);
  }
}
