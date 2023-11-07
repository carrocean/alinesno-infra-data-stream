package com.alinesno.infra.data.flink.exchange.controller;

import com.alinesno.infra.data.flink.exchange.common.SystemConstants;
import com.alinesno.infra.data.flink.exchange.common.util.FileUtils;
import com.alinesno.infra.data.flink.exchange.enums.SysConfigEnum;
import com.alinesno.infra.data.flink.service.ISystemConfigService;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * @author zhp
 * @Description:
 * @since 1.0.0
 */

@RestController
@RequestMapping("/readLocal")
public class ReadLocalDataController {

  // 日志记录
  private static final Logger log = LoggerFactory.getLogger(ReadLocalDataController.class);

  @Autowired
  private ISystemConfigService systemConfigService;

  @RequestMapping("/{fileName}")
  public void download(@PathVariable("fileName") String fileName, HttpServletResponse response) {

    try {
      ServletOutputStream outputStream = response.getOutputStream();
      String sqlPath = FileUtils.getSqlHome(systemConfigService
          .getSystemConfigByKey(SysConfigEnum.FLINK_STREAMING_PLATFORM_WEB_HOME.getKey()))
          + fileName;
      if (StringUtils.isEmpty(sqlPath)) {
        outputStream.write("uploadFileDTO is null".getBytes(StandardCharsets.UTF_8));
        outputStream.close();
        return;
      }
      File file = new File(sqlPath);
      if (!file.exists()) {
        outputStream.write("file is not exists".getBytes(StandardCharsets.UTF_8));
        outputStream.close();
        return;
      }
      InputStream inputStream = new FileInputStream(sqlPath);
      String filename = file.getName();
      response.addHeader("Content-Disposition",
          "attachment; filename=" + URLEncoder.encode(filename, SystemConstants.CODE_UTF_8));
      byte[] b = new byte[1024];
      int len;
      while ((len = inputStream.read(b)) > 0) {
        outputStream.write(b, 0, len);
      }
      inputStream.close();
      outputStream.close();
    } catch (Exception ex) {
      log.error("download is error ", ex);
    }
  }

}
