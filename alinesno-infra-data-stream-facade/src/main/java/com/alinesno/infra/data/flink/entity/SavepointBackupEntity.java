package com.alinesno.infra.data.flink.entity;

import com.gitee.sunchenbin.mybatis.actable.annotation.ColumnComment;
import com.gitee.sunchenbin.mybatis.actable.annotation.ColumnType;
import com.alinesno.infra.common.facade.mapper.entity.InfraBaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 功能名：Savepoint备份
 * 数据表：savepoint_backup
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
@TableName("savepoint_backup")
public class SavepointBackupEntity extends InfraBaseEntity {

    // jobConfigId
    @TableField("job_config_id")
	@ColumnType(length=50)
	@ColumnComment("jobConfigId")
    private Long jobConfigId;

    // 地址
    @TableField("savepoint_path")
	@ColumnType(length= 2048)
	@ColumnComment("地址")
    private String savepointPath;

    // 备份时间
    @TableField("backup_time")
	@ColumnType(length= 19)
	@ColumnComment("备份时间")
    private Date backupTime;
}
