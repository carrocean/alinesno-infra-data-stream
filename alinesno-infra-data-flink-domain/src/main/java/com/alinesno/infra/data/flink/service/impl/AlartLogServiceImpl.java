package com.alinesno.infra.data.flink.service.impl;

import com.alinesno.infra.common.core.service.impl.IBaseServiceImpl;
import com.alinesno.infra.data.flink.exchange.dto.AlartLogDTO;
import com.alinesno.infra.data.flink.entity.AlartLogEntity;
import com.alinesno.infra.data.flink.mapper.AlartLogMapper;
import com.alinesno.infra.data.flink.service.IAlartLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 告警日志Service业务层处理
 *
 * @version 1.0.0
 * @author luoxiaodong
 */
@Service
public class AlartLogServiceImpl extends IBaseServiceImpl<AlartLogEntity, AlartLogMapper> implements IAlartLogService {
    //日志记录
    @SuppressWarnings("unused")
    private static final Logger log = LoggerFactory.getLogger(AlartLogServiceImpl.class);

    @Autowired
    private AlartLogMapper  alartLogMapper;

    @Override
    public void addAlartLog(AlartLogDTO alartLogDTO) {
        if (alartLogDTO == null) {
            return;
        }
        this.saveOrUpdate(AlartLogDTO.toEntity(alartLogDTO));
    }


    @Override
    public AlartLogDTO findLogById(Long id) {

        return AlartLogDTO.toDTO(this.findById(id) );
    }

    @Override
    public void deleteLogByJobConfigId(Long jobConfigId) {
        alartLogMapper.deleteByJobConfigId(jobConfigId);
    }
}
