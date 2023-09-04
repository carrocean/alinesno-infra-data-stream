package com.alinesno.infra.data.flink.api.controller;

import com.alinesno.infra.common.core.constants.SpringInstanceScope;
import com.alinesno.infra.common.core.rest.BaseController;
import com.alinesno.infra.common.facade.pageable.DatatablesPageBean;
import com.alinesno.infra.common.facade.pageable.TableDataInfo;
import com.alinesno.infra.data.flink.entity.JobConfigEntity;
import com.alinesno.infra.data.flink.service.IJobConfigEntityService;
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
 * 处理与 JobConfigEntity 相关的请求的 Controller。
 * 继承自 BaseController 类并实现 IJobConfigEntityService 接口。
 *
 * @version 1.0.0
 * @author luoxiaodong
 */
@Api(tags = "JobConfigEntity")
@RestController
@Scope(SpringInstanceScope.PROTOTYPE)
@RequestMapping("/api/infra/simple/crm/job_config_entity")
public class JobConfigEntityController extends BaseController<JobConfigEntity, IJobConfigEntityService> {

    // 日志记录
    private static final Logger log = LoggerFactory.getLogger(JobConfigEntityController.class);

    @Autowired
    private IJobConfigEntityService service;

    /**
     * 获取 JobConfigEntity 的 DataTables 数据。
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
    public IJobConfigEntityService getFeign() {
        return this.service;
    }
}
