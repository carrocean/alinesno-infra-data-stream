package com.alinesno.infra.data.stream.commom.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author zhp
 * @Description:
 * @since 1.0.0
 */
public class UrlUtils {

  private static final String REG_1 = "^([hH][tT]{2}[pP]://|[hH][tT]{2}[pP][sS]://)(([A-Za-z0-9-~]+).)+([A-Za-z0-9-~\\\\/])+$";

  // 日志记录
  private static final Logger log = LoggerFactory.getLogger(UrlUtils.class);


  public static boolean isHttpsOrHttp(String url) {
    Pattern p = Pattern.compile(REG_1);
    Matcher m = p.matcher(url.trim());
    if (!m.matches()) {
      return false;
    }
    return true;
  }
  public static List<String> getSqlList(String sqlUrl) {
    List<String> fileList = new ArrayList<String>();
    try {
      //2023-06-29
      log.info("待执行sql文件的Url:{}",sqlUrl);
      URL url = new URL(sqlUrl);
      InputStream in = url.openStream();
      InputStreamReader isr = new InputStreamReader(in);
      BufferedReader bufr = new BufferedReader(isr);
      String str;
      while ((str = bufr.readLine()) != null) {
        //2023-06-29
        log.info("从待执行sql文件的Url:{}中读取到sql:{}",sqlUrl,str);
        fileList.add(str);
      }
      bufr.close();
      isr.close();
      in.close();
      return fileList;
    } catch (Exception e) {
      //2023-06-29
      log.error("从待执行sql文件的Url:{}中读取的sql失败,异常信息：{}",sqlUrl,e.getMessage());
      e.printStackTrace();
    }
    return null;

  }
}
