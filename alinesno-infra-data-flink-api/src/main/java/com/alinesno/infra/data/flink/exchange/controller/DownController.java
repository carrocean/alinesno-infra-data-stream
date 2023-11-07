package com.alinesno.infra.data.flink.exchange.controller;

import com.alinesno.infra.data.flink.exchange.common.SystemConstants;
import com.alinesno.infra.data.flink.exchange.dto.UploadFileDTO;
import com.alinesno.infra.data.flink.service.IUploadFileService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.ServletOutputStream;
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
@RequestMapping("/download")
public class DownController {

  //日志记录
  private static final Logger log = LoggerFactory.getLogger(DownController.class);

  @Autowired
  private IUploadFileService uploadFileService;

  @RequestMapping("/{fileName}")
  public void download(@PathVariable("fileName") String fileName, HttpServletResponse response) {

    try {
      ServletOutputStream outputStream = response.getOutputStream();
      UploadFileDTO uploadFileDTO = uploadFileService.getUploadFileByFileName(fileName);
      if (uploadFileDTO == null || StringUtils.isEmpty(uploadFileDTO.getFilePath())) {
        outputStream.write("uploadFileDTO is null".getBytes(StandardCharsets.UTF_8));
        outputStream.close();
        return;
      }
      File file = new File(uploadFileDTO.getFilePath());
      if (!file.exists()) {
        outputStream.write("file is not exists".getBytes(StandardCharsets.UTF_8));
        outputStream.close();
        return;
      }
      InputStream inputStream = new FileInputStream(uploadFileDTO.getFilePath());
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

