package com.alinesno.infra.data.flink.service;

import com.alinesno.infra.common.facade.services.IBaseService;
import com.alinesno.infra.data.flink.entity.JobConfigHistoryEntity;
import com.alinesno.infra.data.flink.exchange.dto.JobConfigHistoryDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 作业配置历史Service接口
 *
 * @version 1.0.0
 * @author luoxiaodong
 */
public interface IJobConfigHistoryService extends IBaseService<JobConfigHistoryEntity> {

    /**
     * 新增记录
     *
     * @author zhp
     * @date 2021/5/5
     * @time 20:13
     */
    void insertJobConfigHistory(JobConfigHistoryDTO jobConfigHistoryDTO);


    /**
     * 查询历史记录
     *
     * @author zhp
     * @date 2021/5/5
     * @time 20:13
     */
    List<JobConfigHistoryDTO> getJobConfigHistoryByJobConfigId(Long jobConfigId);


    /**
     * 详情
     *
     * @author zhp
     * @date 2021/5/5
     * @time 20:14
     */
    JobConfigHistoryDTO getJobConfigHistoryById(Long id);

    int deleteByJobConfigId(@Param("jobConfigId") Long jobConfigId);
}
