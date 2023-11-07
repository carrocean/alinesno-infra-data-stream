package com.alinesno.infra.data.flink.exchange.controller;

import com.alinesno.infra.common.core.constants.SpringInstanceScope;
import com.alinesno.infra.common.web.adapter.rest.BaseController;
import com.alinesno.infra.common.facade.pageable.DatatablesPageBean;
import com.alinesno.infra.common.facade.pageable.TableDataInfo;
import com.alinesno.infra.common.facade.response.AjaxResult;
import com.alinesno.infra.data.flink.commom.enums.FileTypeEnum;
import com.alinesno.infra.data.flink.entity.UploadFileEntity;
import com.alinesno.infra.data.flink.exchange.config.CustomConfig;
import com.alinesno.infra.data.flink.service.ISystemConfigService;
import com.alinesno.infra.data.flink.service.IUploadFileService;
import io.swagger.annotations.Api;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.alinesno.infra.data.flink.exchange.dto.UploadFileDTO;
import java.io.File;
import java.util.Date;
import java.util.List;

/**
 * 处理与UploadFile相关的请求的Controller。
 * 继承自BaseController类并实现IUploadFileService接口。
 *
 * @version 1.0.0
 * @author luoxiaodong
 */
@Api(tags = "UploadFile")
@RestController
@Scope(SpringInstanceScope.PROTOTYPE)
@RequestMapping("/api/infra/data/flink/uploadFile")
public class UploadFileController extends BaseController<UploadFileEntity, IUploadFileService> {

    // 日志记录
    private static final Logger log = LoggerFactory.getLogger(UploadFileController.class);

    @Autowired
    private IUploadFileService service;

    @Autowired
    private ISystemConfigService systemConfigService;

    @Autowired
    private CustomConfig customConfig;

    /**
     * 获取UploadFile的DataTables数据。
     *
     * @param request HttpServletRequest对象。
     * @param model Model对象。
     * @param page DatatablesPageBean对象。
     * @return 包含DataTables数据的TableDataInfo对象。
     */
    @ResponseBody
    @PostMapping("/datatables")
    public TableDataInfo datatables(HttpServletRequest request, Model model, DatatablesPageBean page) {
        log.debug("page = {}", ToStringBuilder.reflectionToString(page));

        TableDataInfo tableDataInfo = this.toPage(model, this.getFeign(), page);

        List<UploadFileDTO> uploadFileDTOS = UploadFileDTO.toDTOList((List<UploadFileEntity>) tableDataInfo.getRows(), customConfig.getUrlForDown());

        tableDataInfo.setRows(uploadFileDTOS);

        return tableDataInfo ;

    }

    @Override
    public IUploadFileService getFeign() {
        return this.service;
    }

    @PostMapping(value = "/upload")
    public AjaxResult upload(@RequestParam("file") MultipartFile file) {

        Long operatorId = 0L ;
//        // 设置用户
//        ManagerAccountEntity account = CurrentAccountJwt.get() ; // CurrentAccountSession.get(request);
//        if ( account != null ) {
//            operatorId = account.getId() ;
//        }

        try {
            String uploadPath = systemConfigService.getUploadJarsPath();
            log.info("uploadPath={}", uploadPath);
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            String originalFilename = file.getOriginalFilename();
            File localFile = new File(uploadPath + originalFilename);
            file.transferTo(localFile);
            UploadFileDTO uploadFileDTO = new UploadFileDTO();
            uploadFileDTO.setFileName(originalFilename);
            uploadFileDTO.setFilePath(localFile.getPath());
            uploadFileDTO.setType(FileTypeEnum.JAR.getCode());
            uploadFileDTO.setEditor(operatorId);
            uploadFileDTO.setCreator(operatorId);
            uploadFileDTO.setCreateTime(new Date());
            service.addFile(uploadFileDTO);
            return AjaxResult.success();
        } catch (Exception e) {
            log.error("upload is error", e);
            return AjaxResult.error(e.getMessage());
        }
    }
}
