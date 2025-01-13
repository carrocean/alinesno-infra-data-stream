package com.alinesno.infra.data.stream.entity;

import com.alinesno.infra.common.facade.mapper.entity.InfraBaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gitee.sunchenbin.mybatis.actable.annotation.ColumnComment;
import com.gitee.sunchenbin.mybatis.actable.annotation.ColumnType;
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlTypeConstant;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

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
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("job_run_log")
public class JobRunLogEntity extends InfraBaseEntity {

    // jobConfigId
    @TableField("job_config_id")
	@ColumnType(MySqlTypeConstant.BIGINT)
	@ColumnComment("jobConfigId")
    private Long jobConfigId;

    // 任务名称
    @TableField("job_name")
	@ColumnType(length=64)
	@ColumnComment("任务名称")
    private String jobName;

    // 任务描述
    @TableField("job_desc")
	@ColumnType(length= 255)
	@ColumnComment("任务描述")
    private String jobDesc;

    // 提交模式: standalone 、yarn 、yarn-session
    @TableField("deploy_mode")
	@ColumnType(length=64)
	@ColumnComment("提交模式:")
    private String deployMode;

    // 运行后的任务id
    @TableField("job_id")
	@ColumnType(length=64)
	@ColumnComment("运行后的任务id")
    private String jobId;

    // 启动时本地日志
    @TableField("local_log")
	@ColumnType(MySqlTypeConstant.LONGTEXT)
	@ColumnComment("启动时本地日志")
    private String localLog;

    // 任务运行所在的机器
    @TableField("run_ip")
	@ColumnType(length=64)
	@ColumnComment("任务运行所在的机器")
    private String runIp;

    // 远程日志url的地址
    @TableField("remote_log_url")
	@ColumnType(length=128)
	@ColumnComment("远程日志url的地址")
    private String remoteLogUrl;

    // 启动时间
    @TableField("start_time")
	@ColumnType(MySqlTypeConstant.DATETIME)
	@ColumnComment("启动时间")
    private Date startTime;

    // 启动时间
    @TableField("end_time")
	@ColumnType(MySqlTypeConstant.DATETIME)
	@ColumnComment("启动时间")
    private Date endTime;

    // 任务状态
    @TableField("job_status")
	@ColumnType(length= 32)
	@ColumnComment("任务状态")
    private String jobStatus;
}
