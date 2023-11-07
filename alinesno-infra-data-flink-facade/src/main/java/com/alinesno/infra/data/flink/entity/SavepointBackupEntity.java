package com.alinesno.infra.data.flink.entity;

import com.alinesno.infra.common.facade.mapper.entity.InfraBaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
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
    private Long jobConfigId;

    // 地址
    @TableField("savepoint_path")
    private String savepointPath;

    // 备份时间
    @TableField("backup_time")
    private Date backupTime;

}
