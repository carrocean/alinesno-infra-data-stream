package com.alinesno.infra.data.stream.exchange.controller;

import com.alinesno.infra.common.core.constants.SpringInstanceScope;
import com.alinesno.infra.common.facade.pageable.DatatablesPageBean;
import com.alinesno.infra.common.facade.pageable.TableDataInfo;
import com.alinesno.infra.common.facade.response.AjaxResult;
import com.alinesno.infra.common.web.adapter.rest.BaseController;
import com.alinesno.infra.data.stream.entity.AlartLogEntity;
import com.alinesno.infra.data.stream.exchange.dto.AlartLogDTO;
import com.alinesno.infra.data.stream.service.IAlartLogService;
import io.swagger.annotations.Api;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 处理与AlartLog相关的请求的Controller。
 * 继承自BaseController类并实现IAlartLogService接口。
 *
 * @version 1.0.0
 * @author luoxiaodong
 */
@Slf4j
@Api(tags = "AlartLog")
@RestController
@Scope(SpringInstanceScope.PROTOTYPE)
@RequestMapping("/api/infra/data/flink/alartLog")
public class AlartLogController extends BaseController<AlartLogEntity, IAlartLogService> {

    @Autowired
    private IAlartLogService service;

    /**
     * 获取AlartLogEntity的DataTables数据。
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
        List<AlartLogDTO> alartLogDTO = AlartLogDTO.toListDTO((List<AlartLogEntity>) tableDataInfo.getRows());
        tableDataInfo.setRows(alartLogDTO);
        return tableDataInfo ;
    }

    @Override
    public IAlartLogService getFeign() {
        return this.service;
    }


    /**
     * 告警日志详情
     *
     * @author zhp
     * @since 1.0.0
     * @time 19:25
     */
    @RequestMapping("/logErrorInfo")
    public AjaxResult logErrorInfo(Long id) {
        AlartLogDTO alartLogDTO = service.findLogById(id);
        if (alartLogDTO == null || StringUtils.isEmpty(alartLogDTO.getFailLog())) {
            return AjaxResult.error("没有异常数据");
        }
        return AjaxResult.success(alartLogDTO.getFailLog());

    }


}
