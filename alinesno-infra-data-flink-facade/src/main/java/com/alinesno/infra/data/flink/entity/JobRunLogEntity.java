package com.alinesno.infra.data.flink.entity;

import com.alinesno.infra.common.facade.mapper.entity.InfraBaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;

/**
 * 功能名：任务运行日志
 * 数据表：job_run_log
 * 表备注：
 *
 * @TableName 表名注解，用于指定数据库表名
 * @TableField 字段注解，用于指定数据库字段名
 *
 * @author luoxiaodong
 * @version 1.0.0
 */

@TableName("job_run_log")
public class JobRunLogEntity extends InfraBaseEntity {

    // jobConfigId
    @TableField("job_config_id")
    private String jobConfigId;

    // 任务名称
    @TableField("job_name")
    private String jobName;

    // 任务描述
    @TableField("job_desc")
    private String jobDesc;

    // 提交模式: standalone 、yarn 、yarn-session
    @TableField("deploy_mode")
    private String deployMode;

    // 运行后的任务id
    @TableField("job_id")
    private String jobId;

    // 启动时本地日志
    @TableField("local_log")
    private String localLog;

    // 任务运行所在的机器
    @TableField("run_ip")
    private String runIp;

    // 远程日志url的地址
    @TableField("remote_log_url")
    private String remoteLogUrl;

    // 启动时间
    @TableField("start_time")
    private LocalDateTime startTime;

    // 启动时间
    @TableField("end_time")
    private LocalDateTime endTime;

    // 任务状态
    @TableField("job_status")
    private String jobStatus;
}
