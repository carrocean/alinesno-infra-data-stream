package com.alinesno.infra.base.notice.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.alinesno.infra.common.facade.mapper.entity.InfraBaseEntity;

/**
 * 功能名：运行任务日志
 * 数据表：job_run_log
 * 表备注：运行任务日志
 * 
 * 此类表示运行任务日志实体。
 * 
 * 字段说明：
 * - jobConfigId: 任务配置ID
 * - jobName: 任务名称
 * - jobDesc: 任务描述
 * - deployMode: 提交模式，可选值为 standalone、yarn、yarn-session
 * - jobId: 运行后的任务ID
 * - localLog: 启动时本地日志
 * - runIp: 任务运行所在的机器
 * - remoteLogUrl: 远程日志URL的地址
 * - startTime: 启动时间
 * - endTime: 结束时间
 * - jobStatus: 任务状态
 * 
 * 该实体类继承自InfraBaseEntity，使用MyBatis-Plus提供的注解进行字段映射。
 *
 * @author luoxiaodong
 * @version 1.0.0
 */
@TableName("job_run_log")
public class JobRunLogEntity extends InfraBaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 任务配置ID
     */
    @TableField("job_config_id")
    private String jobConfigId;

    /**
     * 任务名称
     */
    @TableField("job_name")
    private String jobName;

    /**
     * 任务描述
     */
    @TableField("job_desc")
    private String jobDesc;

    /**
     * 提交模式，可选值为 standalone、yarn、yarn-session
     */
    @TableField("deploy_mode")
    private String deployMode;

    /**
     * 运行后的任务ID
     */
    @TableField("job_id")
    private String jobId;

    /**
     * 启动时本地日志
     */
    @TableField("local_log")
    private String localLog;

    /**
     * 任务运行所在的机器
     */
    @TableField("run_ip")
    private String runIp;

    /**
     * 远程日志URL的地址
     */
    @TableField("remote_log_url")
    private String remoteLogUrl;

    /**
     * 启动时间
     */
    @TableField("start_time")
    private Date startTime;

    /**
     * 结束时间
     */
    @TableField("end_time")
    private Date endTime;

    /**
     * 任务状态
     */
    @TableField("job_status")
    private String jobStatus;

    // getter and setter methods
}
