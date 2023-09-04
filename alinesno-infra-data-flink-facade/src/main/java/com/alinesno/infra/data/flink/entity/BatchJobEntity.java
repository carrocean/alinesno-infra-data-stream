package com.alinesno.infra.data.flink.entity;

import com.alinesno.infra.common.facade.mapper.entity.BaseEntity;

/**
 * 批处理作业
 * 
 * 注意事项：
 * - 删除掉 @ColumnComment 和 @Excel 注解
 * - 去掉带有 length 属性的 @TableField 注解
 * - 使用 mybatis-plus 的 @TableName 和 @TableField 注解进行注解
 * - @TableField 不允许重复
 * - @TableField 没有 length 属性
 * - InfraBaseEntity 的路径为 com.alinesno.infra.common.facade.mapper.entity.InfraBaseEntity
 * - 类的字段需要添加中文注释信息
 * - 类需要添加详细的注释信息
 *
 * @author luoxiaodong
 * @version 1.0.0
 */
public class BatchJobEntity extends BaseEntity {
    private Long id;

    /**
     * 任务名称
     */
    private String jobName;

    /**
     * cron表达式
     */
    private String cron;

    public BatchJobEntity() {
    }

    public BatchJobEntity(Long id, String jobName, String cron) {
        this.id = id;
        this.jobName = jobName;
        this.cron = cron;
    }

    /**
     * 获取任务ID
     *
     * @return 任务ID
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置任务ID
     *
     * @param id 任务ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取任务名称
     *
     * @return 任务名称
     */
    public String getJobName() {
        return jobName;
    }

    /**
     * 设置任务名称
     *
     * @param jobName 任务名称
     */
    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    /**
     * 获取cron表达式
     *
     * @return cron表达式
     */
    public String getCron() {
        return cron;
    }

    /**
     * 设置cron表达式
     *
     * @param cron cron表达式
     */
    public void setCron(String cron) {
        this.cron = cron;
    }
}
