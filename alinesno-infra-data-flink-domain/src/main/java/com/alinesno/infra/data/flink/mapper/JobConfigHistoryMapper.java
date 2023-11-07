package com.alinesno.infra.data.flink.mapper;

import com.alinesno.infra.common.facade.mapper.repository.IBaseMapper;
import com.alinesno.infra.data.flink.entity.JobConfigHistoryEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 作业配置历史Mapper接口
 *
 * @version 1.0.0
 * @author luoxiaodong
 */
@Repository
public interface JobConfigHistoryMapper extends IBaseMapper<JobConfigHistoryEntity> {

    List<JobConfigHistoryEntity> selectByJobConfigId(@Param("jobConfigId") Long jobConfigId);

    int deleteByJobConfigId(@Param("jobConfigId") Long jobConfigId);
}
