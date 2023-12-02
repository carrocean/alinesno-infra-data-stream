package com.alinesno.infra.data.stream.mapper;

import com.alinesno.infra.common.facade.mapper.repository.IBaseMapper;
import com.alinesno.infra.data.stream.entity.JobRunLogEntity;
import com.alinesno.infra.data.stream.exchange.vo.JobRunStatDayVO;
import com.alinesno.infra.data.stream.exchange.vo.JobRunStatVO;
import com.alinesno.infra.data.stream.exchange.vo.jobTypeRunCount;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 作业运行日志Mapper接口
 *
 * @version 1.0.0
 * @author luoxiaodong
 */
@Repository
public interface JobRunLogMapper extends IBaseMapper<JobRunLogEntity> {

    int update(JobRunLogEntity record);

    int deleteByJobConfigId(@Param("jobConfigId") Long jobConfigId);

    List<jobTypeRunCount> jobTypeRunCnt(Long operatorId) ;

    List<JobRunStatDayVO> JobRunStatDay(Long operatorId);

    List<JobRunStatVO> JobRunStat(Long operatorId);
}
