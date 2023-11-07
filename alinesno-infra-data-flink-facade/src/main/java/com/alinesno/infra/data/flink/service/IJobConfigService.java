package com.alinesno.infra.data.flink.service;

import com.alinesno.infra.common.facade.services.IBaseService;
import com.alinesno.infra.data.flink.entity.BatchJob;
import com.alinesno.infra.data.flink.entity.JobConfigEntity;
import com.alinesno.infra.data.flink.exchange.common.ResponseBean;
import com.alinesno.infra.data.flink.exchange.dto.JobConfigDTO;
import com.alinesno.infra.data.flink.exchange.enums.JobConfigStatus;
import com.alinesno.infra.data.flink.exchange.enums.YN;
import com.alinesno.infra.data.flink.exchange.vo.JobStatusStatVO;
import com.alinesno.infra.data.flink.exchange.vo.jobTypeRunCount;

import java.util.List;

/**
 * 作业配置Service接口
 *
 * @version 1.0.0
 * @author luoxiaodong
 */
public interface IJobConfigService extends IBaseService<JobConfigEntity> {

    /**
     * 新增配置 返回主键Id
     *
     * @author zhp
     * @date 2020-07-14
     * @time 19:26
     */
    ResponseBean addJobConfig(JobConfigDTO jobConfigDTO);

    /**
     * 修改配置
     *
     * @author zhp
     * @date 2020-07-14
     * @time 19:26
     */
    void updateJobConfigById(JobConfigDTO jobConfig);

    /**
     * 修改配置(记录历史信息)
     *
     * @param jobConfigDTO
     * @author wxj
     * @date 2021年12月30日 下午2:33:31
     * @version V1.0
     */
    ResponseBean updateJobConfigByIdWithWriteHistory(JobConfigDTO jobConfigDTO);

    /**
     * @author zhp
     * @date 2020-08-18
     * @time 19:01
     */
    void updateJobConfigStatusById(Long id, JobConfigStatus jobConfigStatus);

    /**
     * 启动状态更新 有乐观锁
     *
     * @author zhp
     * @date 2021/2/28
     * @time 17:57
     */
    void updateStatusByStart(Long id, Long operatorId, Long jobRunLogId, Long version);


    /**
     * 单个查询任务详情
     *
     * @author zhp
     * @date 2020-07-14
     * @time 23:05
     */
    JobConfigDTO getJobConfigById(Long id);

    /**
     * 单个查询任务详情(包括删除)
     *
     * @param id
     * @return
     * @author wxj
     * @date 2021年12月28日 下午1:48:07
     * @version V1.0
     */
    JobConfigDTO getJobConfigByIdContainDelete(Long id);


    /**
     * 开启或者配置
     *
     * @author zhp
     * @date 2020-07-14
     * @time 19:27
     */
    ResponseBean openOrClose(Long id, YN yn, Long operatorId);

    /**
     * 删除任务
     *
     * @author zhp
     * @date 2020-07-14
     * @time 23:03
     */
    ResponseBean deleteJobConfigById(Long id);


    /**
     * 恢复删除任务
     *
     * @param id
     * @param operatorId
     * @author wxj
     * @date 2021年12月28日 下午2:07:31
     * @version V1.0
     */
    int recoveryDeleteJobConfigById(Long id);


    /**
     * 按状态获取任务
     *
     * @author zhp
     * @date 2020-09-22
     * @time 23:04
     */
    List<JobConfigDTO> findJobConfigByStatus(Integer... status);

    List<BatchJob> getAllBatchJobs();


    List<jobTypeRunCount> jobTypeCnt(Long operatorId) ;

    List<JobStatusStatVO> JobStatusStat(Long operatorId);

}
