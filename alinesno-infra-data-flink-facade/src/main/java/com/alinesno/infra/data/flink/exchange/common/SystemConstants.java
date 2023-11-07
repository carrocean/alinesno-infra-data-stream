package com.alinesno.infra.data.flink.exchange.common;

import com.alinesno.infra.data.flink.exchange.common.util.IpUtil;
import com.alinesno.infra.data.flink.exchange.exceptions.BizException;
import org.apache.commons.lang3.StringUtils;

/**
 * 系统常量
 *
 * @author zhp
 * @Description:
 * @date 2020-07-13
 * @time 01:33
 */
public class SystemConstants {

  public static final String COOKIE_NAME_SESSION_ID = "flink-pipeline-sessionid";

  public static final String STATUS_RUNNING = "RUNNING";

  public static final String STATUS_INITIALIZING = "INITIALIZING";

  public static final String STATUS_SCHEDULED = "SCHEDULED";

  public static final String STATUS_FINISHED = "FINISHED";

  public static final String STATUS_RESTARTING = "RESTARTING";

  public static final String BEANNAME_JOBSTANDALONESERVERAO = "jobStandaloneServerAO";

  public static final String BEANNAME_JOBYARNSERVERAO = "jobYarnServerAO";

  public static final String USER_NAME_TASK_AUTO = "task-auto";

  public static final String CODE_UTF_8 = "UTF-8";

  public static final String DEFAULT_SAVEPOINT_ROOT_PATH = "hdfs:///flink/savepoint/flink-pipeline/";

  public static final String SLASH = "/";

  public static final String HTTP_KEY = "http";

  public static final String HTTPS_KEY = "https";

  private static final String HTTP_YARN_APPS_QUERY = "ws/v1/cluster/apps?queue=";

  public static final String HTTP_YARN_APPS = "ws/v1/cluster/apps/";

  public static final String HTTP_YARN_CLUSTER_APPS = "cluster/app/";

  public static final String HTTP_STANDALONE_APPS = "#/job/";

  public static final String YQU = "yqu";


  public static String buildHttpQuery(String queueName) {
    if (StringUtils.isEmpty(queueName)) {
      throw new BizException("yarn 队列参数（-yqu）为空 ");
    }
    return HTTP_YARN_APPS_QUERY + queueName;
  }

  /**
   * 获取flink 运行文件
   *
   * @author zhp
   * @date 2020-09-21
   * @time 23:24
   */
  public static String buildFlinkBin(String flinkHome) {
    if (StringUtils.isEmpty(flinkHome)) {
      throw new BizException("flinkHome 不存在");
    }
    return flinkHome + "bin/flink";
  }


  /**
   * 构建钉钉告警消息内容
   *
   * @author zhp
   * @date 2020-09-23
   * @time 00:54
   */
  public static String buildDingdingMessage(String content) {
    String host_ip = System.getenv("HOST_IP");
    if  ( host_ip == null ) {
      host_ip = IpUtil.getInstance().getHostIp() ;
    }
    return "数据实时计算服务【" + host_ip + "】告警：" + content;
  }

  public static String buildQuartzJobKeyName(Long id) {
    return "job_key_id#" + id;
  }

}
