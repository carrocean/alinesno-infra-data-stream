package com.alinesno.infra.data.flink.exchange.common.util;

import cn.hutool.core.util.StrUtil;
import com.alinesno.infra.data.flink.exchange.common.SystemConstants;
import com.alinesno.infra.data.flink.exchange.enums.SysErrorEnum;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import com.alinesno.infra.data.flink.exchange.exceptions.BizException;

/**
 * @author zhp
 * @Description:
 * @date 2020-07-20
 * @time 00:00
 */

public class FileUtils {

  // 日志记录
  private static final Logger log = LoggerFactory.getLogger(FileUtils.class);

  private static final String DIR_SQL = "sql/";

  /**
   * 存放sql文件路径
   *
   * @author zhp
   * @date 2020-07-20
   * @time 00:02
   */
  public static void createSqlHome(String sysHome) {
    if (StringUtils.isEmpty(sysHome)) {
      throw new BizException(SysErrorEnum.PARAM_IS_NULL);
    }
    if (!StrUtil.endWith(sysHome.trim(), SystemConstants.SLASH)) {
      throw new BizException("必须以/结尾");
    }
    if (!StrUtil.startWith(sysHome.trim(), SystemConstants.SLASH)) {
      throw new BizException("必须以/开头");
    }
    File dir = new File(sysHome + DIR_SQL);
    if (!dir.exists()) {
      dir.mkdirs();
    }
  }

  public static String getSqlHome(String sysHome) {
    createSqlHome(sysHome);
    return sysHome + DIR_SQL;
  }


  /**
   * 生产文件名称（sql的文件名）
   *
   * @author zhp
   * @date 2020-07-20
   * @time 00:01
   */
  public static String createFileName(String id) {
    return new StringBuffer("job_sql_").append(id).append(".sql").toString();
  }

  /**
   * 文件写入
   *
   * @author zhp
   * @date 2020-07-20
   * @time 00:29
   */
  public static void writeText(String filePath, String content, boolean isAppend) {
    log.info("写入文件 filePath={}", filePath);
    FileOutputStream outputStream = null;
    OutputStreamWriter outputStreamWriter = null;
    BufferedWriter bufferedWriter = null;
    try {
      outputStream = new FileOutputStream(filePath, isAppend);
      outputStreamWriter = new OutputStreamWriter(outputStream);
      bufferedWriter = new BufferedWriter(outputStreamWriter);
      bufferedWriter.write(content);
    } catch (Exception e) {
      log.error("写入异常", e);
      throw new BizException("文件写入失败");
    } finally {
      try {
        if (bufferedWriter != null) {
          bufferedWriter.close();
        }
        if (outputStreamWriter != null) {
          outputStreamWriter.close();
        }
        if (outputStream != null) {
          outputStream.close();
        }
      } catch (Exception e) {
        log.error("关闭写入文件异常", e);
      }
    }
  }

  public static void mkdirs(String path) {
    File dir = new File(path);
    if (!dir.exists()) {
      dir.mkdirs();
    }
  }

}
