package com.alinesno.infra.data.stream.exchange.controller;

import com.alinesno.infra.common.core.constants.SpringInstanceScope;
import com.alinesno.infra.common.facade.pageable.DatatablesPageBean;
import com.alinesno.infra.common.facade.pageable.TableDataInfo;
import com.alinesno.infra.common.web.adapter.rest.BaseController;
import com.alinesno.infra.data.stream.entity.JobConfigHistoryEntity;
import com.alinesno.infra.data.stream.exchange.dto.JobConfigHistoryDTO;
import com.alinesno.infra.data.stream.service.IJobConfigHistoryService;
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

import java.util.List;

/**
 * 处理与JobConfigHistory相关的请求的Controller。
 * 继承自BaseController类并实现IJobConfigHistoryService接口。
 *
 * @version 1.0.0
 * @author luoxiaodong
 */
@Api(tags = "JobConfigHistory")
@RestController
@Scope(SpringInstanceScope.PROTOTYPE)
@RequestMapping("/api/infra/data/flink/jobConfigHistory")
public class JobConfigHistoryController extends BaseController<JobConfigHistoryEntity, IJobConfigHistoryService> {

    // 日志记录
    private static final Logger log = LoggerFactory.getLogger(JobConfigHistoryController.class);

    @Autowired
    private IJobConfigHistoryService service;

    /**
     * 获取JobConfigHistory的DataTables数据。
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

        //获取返回的数据后进行加工
        TableDataInfo tableDataInfo = this.toPage(model, this.getFeign(), page);
        List<JobConfigHistoryDTO> jobConfigHistoryDTOS = JobConfigHistoryDTO.toListDTO((List<JobConfigHistoryEntity>) tableDataInfo.getRows());
        tableDataInfo.setRows(jobConfigHistoryDTOS);
        return tableDataInfo ;

    }

    @Override
    public IJobConfigHistoryService getFeign() {
        return this.service;
    }
}
