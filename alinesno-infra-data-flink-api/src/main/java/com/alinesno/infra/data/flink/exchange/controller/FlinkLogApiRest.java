package com.alinesno.infra.data.flink.exchange.controller;

import com.alinesno.infra.data.flink.exchange.common.util.IpUtil;
import com.alinesno.infra.data.flink.exchange.common.util.LinuxInfoUtil;
import com.alinesno.infra.data.flink.exchange.enums.SysConfigEnum;
import com.alinesno.infra.data.flink.exchange.exceptions.BizException;
import com.alinesno.infra.data.flink.service.ISystemConfigService;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.*;

/**
 * @author zhp
 * @Description:
 * @date 2021/5/5
 * @time 10:23
 */
@RestController
@RequestMapping("/log")
public class FlinkLogApiRest {

  //日志记录
  private static final Logger log = LoggerFactory.getLogger(FlinkLogApiRest.class);

  @Autowired
  private ISystemConfigService systemConfigService;

  @GetMapping(value = "/getFlinkLocalJobLog")
  public String getFlinkLocalJobLog(HttpServletResponse response) {
    String logPath = this.getLogPath();
    log.info("日志文件地址 logPath={}", logPath);
    response.setContentType("text/html; charset=utf-8");  // 设置响应内容类型为HTML

    try {
      PrintWriter out = response.getWriter();
      readFileContent(logPath, out);


//      File file = new File(logPath);
//      InputStream fis = new BufferedInputStream(new FileInputStream(file));
//      byte[] buffer = new byte[fis.available()];
//      fis.read(buffer);
//      fis.close();
//      response.reset();
//      response.addHeader("Content-Length", "" + file.length());
//      response.setContentType("text/plain; charset=utf-8");
//      OutputStream toClient = new BufferedOutputStream(response.getOutputStream(), 2048);
//      toClient.write(buffer);
//      toClient.flush();
//      toClient.close();

    } catch (Exception ex) {
      log.error("[getFlinkLocalJobLog is error]", ex);
      return ex.getMessage();
    }
    return "ok";
  }

  private void readFileContent(String filePath, PrintWriter out) {
    try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath))) {
      String line = bufferedReader.readLine();
      while (line != null) {
        out.println(line);    // 写入文件内容
        out.println("<br>");  // 增加浏览器换行符
        line = bufferedReader.readLine();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private String getLogPath() {
    String fileName = String
        .format("flink-%s-client-%s.log", LinuxInfoUtil.loginName(), IpUtil.getHostName());
    String flinkName = systemConfigService
        .getSystemConfigByKey(SysConfigEnum.FLINK_HOME.getKey());
    String logPath = flinkName + "log/" + fileName;
    File file = new File(logPath);
    if (file.exists()) {
      return logPath;
    }
    fileName = String
        .format("flink--client-%s.log", IpUtil.getHostName());
    logPath = flinkName + "log/" + fileName;
    if (new File(logPath).exists()) {
      return logPath;
    }
    throw new BizException("not find client-log file logPath=" + logPath);
  }

}
