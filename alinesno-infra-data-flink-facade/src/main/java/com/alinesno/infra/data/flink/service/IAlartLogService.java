package com.alinesno.infra.data.flink.service;

import com.alinesno.infra.common.facade.services.IBaseService;
import com.alinesno.infra.data.flink.exchange.dto.AlartLogDTO;
import com.alinesno.infra.data.flink.entity.AlartLogEntity;

/**
 * 告警日志Service接口
 *
 * @version 1.0.0
 * @author luoxiaodong
 */
public interface IAlartLogService extends IBaseService<AlartLogEntity> {
    void addAlartLog(AlartLogDTO alartLogDTO);

    /**
     * 按照id查询
     *
     * @author zhp
     * @date 2020-09-25
     * @time 21:49
     */
    AlartLogDTO findLogById(Long id);


    void deleteLogByJobConfigId(Long jobConfigId);
}
