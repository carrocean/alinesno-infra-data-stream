package com.alinesno.infra.data.flink.entity;

import com.gitee.sunchenbin.mybatis.actable.annotation.ColumnComment;
import com.gitee.sunchenbin.mybatis.actable.annotation.ColumnType;
import com.alinesno.infra.common.facade.mapper.entity.InfraBaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import static com.gitee.sunchenbin.mybatis.actable.constants.MySqlTypeConstant.BIGINT;
import static com.gitee.sunchenbin.mybatis.actable.constants.MySqlTypeConstant.LONGTEXT;

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
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("alart_log")
public class AlartLogEntity extends InfraBaseEntity {

    // job_config的id  如果0代表的是测试,
    @TableField("job_config_id")
	@ColumnType(BIGINT)
	@ColumnComment("job_config的id")
    private Long jobConfigId;

    // jobName
    @TableField("job_name")
	@ColumnType(length= 255)
	@ColumnComment("jobName")
    private String jobName;

    // 消息内容
    @TableField("message")
	@ColumnType(length= 512)
	@ColumnComment("消息内容")
    private String message;

    // 1:钉钉
    @TableField("type")
	@ColumnType(length=11)
	@ColumnComment("1:钉钉")
    private Integer type;

    // 1:成功 0:失败
    @TableField("status")
	@ColumnType(length= 11)
	@ColumnComment("1:成功 0:失败")
    private Integer status;

    // 失败原因
    @TableField("fail_log")
	@ColumnType(LONGTEXT)
	@ColumnComment("失败原因")
    private String failLog;
}
