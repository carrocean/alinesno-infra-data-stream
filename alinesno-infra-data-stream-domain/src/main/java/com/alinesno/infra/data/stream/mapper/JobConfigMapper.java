package com.alinesno.infra.data.stream.mapper;

import com.alinesno.infra.common.facade.mapper.repository.IBaseMapper;
import com.alinesno.infra.data.stream.entity.BatchJob;
import com.alinesno.infra.data.stream.entity.JobConfigEntity;
import com.alinesno.infra.data.stream.exchange.vo.JobStatusStatVO;
import com.alinesno.infra.data.stream.exchange.vo.jobTypeRunCount;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 作业配置Mapper接口
 *
 * @version 1.0.0
 * @author luoxiaodong
 */
@Repository
public interface JobConfigMapper extends IBaseMapper<JobConfigEntity> {

    Integer selectCountByJobName(@Param("jobName") String jobName, @Param("id") Long id);

    JobConfigEntity selectByPrimaryKey(Long id);

    JobConfigEntity selectByPrimaryKeyContainDelete(Long id);

    int updateByPrimaryKeySelective(JobConfigEntity record);


    int updateStatusByStart(@Param("id") Long id, @Param("operatorId") Long operatorId, @Param("status") Integer status, @Param("jobRunLogId") Long jobRunLogId, @Param("oldVersion") Long oldVersion);



    int deleteById(@Param("id") Long id);

    int recoveryDeleteJobConfigById(@Param("id") Long id);

    List<JobConfigEntity> findJobConfigByStatus(@Param("statusList") List<Integer> statusList);

    List<BatchJob> getAllBatchJobs();

    List<jobTypeRunCount> jobTypeCnt(Long operatorId) ;

    List<JobStatusStatVO> JobStatusStat(Long operatorId);
}
