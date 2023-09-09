package com.alinesno.infra.data.flink.entity;

import com.alinesno.infra.common.facade.mapper.entity.InfraBaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 功能名：告警日志
 * 数据表：alart_log
 * 表备注：
 *
 * @TableName 表名注解，用于指定数据库表名
 * @TableField 字段注解，用于指定数据库字段名
 *
 * @author luoxiaodong
 * @version 1.0.0
 */

@TableName("alart_log")
public class AlartLogEntity extends InfraBaseEntity {

    // job_config的id  如果0代表的是测试,
    @TableField("job_config_id")
    private String jobConfigId;

    // jobName
    @TableField("job_name")
    private String jobName;

    // 消息内容
    @TableField("message")
    private String message;

    // 1:钉钉
    @TableField("type")
    private Integer type;

    // 1:成功 0:失败
    @TableField("status")
    private Integer status;

    // 失败原因
    @TableField("fail_log")
    private String failLog;
}
