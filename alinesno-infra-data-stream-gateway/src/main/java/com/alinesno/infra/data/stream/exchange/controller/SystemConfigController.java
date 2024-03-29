package com.alinesno.infra.data.stream.exchange.controller;

import com.alinesno.infra.common.core.constants.SpringInstanceScope;
import com.alinesno.infra.common.facade.pageable.DatatablesPageBean;
import com.alinesno.infra.common.facade.pageable.TableDataInfo;
import com.alinesno.infra.common.facade.response.AjaxResult;
import com.alinesno.infra.common.web.adapter.rest.BaseController;
import com.alinesno.infra.data.stream.entity.SystemConfigEntity;
import com.alinesno.infra.data.stream.exchange.exceptions.BizException;
import com.alinesno.infra.data.stream.service.ISystemConfigService;
import io.swagger.annotations.Api;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 处理与SystemConfig相关的请求的Controller。
 * 继承自BaseController类并实现ISystemConfigService接口。
 *  30204
 * @version 1.0.0
 * @author luoxiaodong
 */
@Api(tags = "SystemConfig")
@RestController
@Scope(SpringInstanceScope.PROTOTYPE)
@RequestMapping("/api/infra/data/flink/systemConfig")
public class SystemConfigController extends BaseController<SystemConfigEntity, ISystemConfigService> {

    // 日志记录
    private static final Logger log = LoggerFactory.getLogger(SystemConfigController.class);

    @Autowired
    private ISystemConfigService service;

    /**
     * 获取SystemConfig的DataTables数据。
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
        Long operatorId = 0L ;
        this.setConditions(page);
        TableDataInfo dataInfo = this.toPage(model, this.getFeign(), page);
        if ( dataInfo.getRows().size() < 1 ){
            service.initAccountSystemConfig(operatorId);
        }
        return this.toPage(model, this.getFeign(), page);
    }

    @Override
    public ISystemConfigService getFeign() {
        return this.service;
    }

    @RequestMapping(value = "/upsertSynConfig", method = RequestMethod.POST)
    public AjaxResult upsertSynConfig(@RequestBody SystemConfigEntity systemConfig) {
        try {
            service.addOrUpdateConfigByKey(systemConfig.getSysKey(),  systemConfig.getSysVal().trim());
        } catch ( BizException biz) {
            log.warn("upsertSynConfig is error ", biz);
            return AjaxResult.error(biz.getMessage());
        } catch (Exception e) {
            log.error("upsertSynConfig is error", e);
            return AjaxResult.error(e.getMessage());
        }
        return AjaxResult.success();
    }

    private void setConditions(DatatablesPageBean page) {
        Map<String, Object> condition = page.getCondition();
        condition.put("operator_id|", "无");
        page.setCondition(condition);
    }
}
