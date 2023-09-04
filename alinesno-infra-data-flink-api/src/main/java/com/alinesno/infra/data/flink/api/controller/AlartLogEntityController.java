package com.alinesno.infra.data.flink.api.controller;

import com.alinesno.infra.common.core.constants.SpringInstanceScope;
import com.alinesno.infra.common.core.rest.BaseController;
import com.alinesno.infra.common.facade.pageable.DatatablesPageBean;
import com.alinesno.infra.common.facade.pageable.TableDataInfo;
import com.alinesno.infra.data.flink.entity.AlartLogEntity;
import com.alinesno.infra.data.flink.service.IAlartLogEntityService;
import io.swagger.annotations.Api;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 处理与 AlartLogEntity 相关的请求的 Controller。
 * 继承自 BaseController 类并实现 IAlartLogEntityService 接口。
 *
 * @version 1.0.0
 * @author luoxiaodong
 */
@Api(tags = "AlartLogEntity")
@RestController
@Scope(SpringInstanceScope.PROTOTYPE)
@RequestMapping("/api/infra/simple/crm/alart_log_entity")
public class AlartLogEntityController extends BaseController<AlartLogEntity, IAlartLogEntityService> {

    // 日志记录
    private static final Logger log = LoggerFactory.getLogger(AlartLogEntityController.class);

    @Autowired
    private IAlartLogEntityService service;

    /**
     * 获取 AlartLogEntity 的 DataTables 数据。
     *
     * @param request HttpServletRequest 对象。
     * @param model Model 对象。
     * @param page DatatablesPageBean 对象。
     * @return 包含 DataTables 数据的 TableDataInfo 对象。
     */
    @ResponseBody
    @PostMapping("/datatables")
    public TableDataInfo datatables(HttpServletRequest request, Model model, DatatablesPageBean page) {
        log.debug("page = {}", ToStringBuilder.reflectionToString(page));
        return this.toDataInfo(model, this.getFeign(), page);
    }

    @Override
    public IAlartLogEntityService getFeign() {
        return this.service;
    }
}
