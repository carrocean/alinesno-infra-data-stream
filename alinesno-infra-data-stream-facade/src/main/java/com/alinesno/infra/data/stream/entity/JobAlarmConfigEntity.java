package com.alinesno.infra.data.stream.entity;

import com.gitee.sunchenbin.mybatis.actable.annotation.ColumnComment;
import com.gitee.sunchenbin.mybatis.actable.annotation.ColumnType;
import com.alinesno.infra.common.facade.mapper.entity.InfraBaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import static com.gitee.sunchenbin.mybatis.actable.constants.MySqlTypeConstant.BIGINT;
import static com.gitee.sunchenbin.mybatis.actable.constants.MySqlTypeConstant.INT;

/**
 * 功能名：任务告警配置
 * 数据表：job_alarm_config
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
@TableName("job_alarm_config")
public class JobAlarmConfigEntity extends InfraBaseEntity {

    // job_config主表id
    @TableField("job_id")
	@ColumnType(BIGINT)
	@ColumnComment("job_config主表id")
    private Long jobId;

    // 类型 1:钉钉告警 2:url回调 3:异常自动拉起任务
    @TableField("type")
	@ColumnType(INT)
	@ColumnComment("类型 1:钉钉告警 2:url回调 3:异常自动拉起任务")
    private Integer type;

    // 更新版本号
    @TableField("version")
	@ColumnType(BIGINT)
	@ColumnComment("更新版本号")
    private Long version;
}
