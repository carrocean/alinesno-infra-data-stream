package com.alinesno.infra.data.stream.mapper;

import com.alinesno.infra.common.facade.mapper.repository.IBaseMapper;
import com.alinesno.infra.data.stream.entity.JobAlarmConfigEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 作业告警配置Mapper接口
 *
 * @version 1.0.0
 * @author luoxiaodong
 */
@Repository
public interface JobAlarmConfigMapper extends IBaseMapper<JobAlarmConfigEntity> {

    int insertBatch(@Param("list") List<JobAlarmConfigEntity> list);

    List<JobAlarmConfigEntity> selectByJobId(@Param("jobId") Long jobId);

    List<JobAlarmConfigEntity> selectByJobIdList(@Param("jobIdList") List<String> jobIdList);

    int deleteByJobId(@Param("jobId") Long jobId);
}
