package com.alinesno.infra.data.stream.service.impl;

import com.alinesno.infra.common.core.service.impl.IBaseServiceImpl;
import com.alinesno.infra.data.stream.entity.JobConfigHistoryEntity;
import com.alinesno.infra.data.stream.exchange.dto.JobConfigHistoryDTO;
import com.alinesno.infra.data.stream.mapper.JobConfigHistoryMapper;
import com.alinesno.infra.data.stream.service.IJobConfigHistoryService;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 作业配置历史Service业务层处理
 *
 * @version 1.0.0
 * @author luoxiaodong
 */
@Service
public class JobConfigHistoryServiceImpl extends IBaseServiceImpl<JobConfigHistoryEntity, JobConfigHistoryMapper> implements IJobConfigHistoryService {
    //日志记录
    @SuppressWarnings("unused")
    private static final Logger log = LoggerFactory.getLogger(JobConfigHistoryServiceImpl.class);

    @Autowired
    private JobConfigHistoryMapper jobConfigHistoryMapper;

    @Override
    public void insertJobConfigHistory(JobConfigHistoryDTO jobConfigHistoryDTO) {
        jobConfigHistoryMapper.insert(JobConfigHistoryDTO.toEntity(jobConfigHistoryDTO));
    }

    @Override
    public List<JobConfigHistoryDTO> getJobConfigHistoryByJobConfigId(Long jobConfigId) {
        return JobConfigHistoryDTO.toListDTO(jobConfigHistoryMapper.selectByJobConfigId(jobConfigId));
    }

    @Override
    public JobConfigHistoryDTO getJobConfigHistoryById(Long id) {
        return JobConfigHistoryDTO.toDTO(this.findById(id));
    }


    @Override
    public int deleteByJobConfigId(@Param("jobConfigId") Long jobConfigId){
        return jobConfigHistoryMapper.deleteByJobConfigId(jobConfigId) ;
    };
}
