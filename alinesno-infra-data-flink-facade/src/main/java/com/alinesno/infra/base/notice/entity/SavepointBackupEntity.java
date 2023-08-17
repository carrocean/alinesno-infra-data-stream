package com.alinesno.infra.base.notice.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.alinesno.infra.common.facade.mapper.entity.InfraBaseEntity;

/**
 * 功能名：savepoint备份地址
 * 数据表：savepoint_backup
 * 表备注：savepoint备份地址
 * 
 * 此类表示savepoint备份地址实体。
 * 
 * 字段说明：
 * - jobConfigId: 任务配置ID
 * - savepointPath: 地址
 * - backupTime: 备份时间
 * 
 * 该实体类继承自InfraBaseEntity，使用MyBatis-Plus提供的注解进行字段映射。
 * 
 * 注意：由于没有提供字段的中文注释信息，以下字段注释仅供参考。
 *
 * @author luoxiaodong
 * @version 1.0.0
 */
@TableName("savepoint_backup")
public class SavepointBackupEntity extends InfraBaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 任务配置ID
     */
    @TableField("job_config_id")
    private String jobConfigId;

    /**
     * 地址
     */
    @TableField("savepoint_path")
    private String savepointPath;

    /**
     * 备份时间
     */
    @TableField("backup_time")
    private Date backupTime;

    // getter and setter methods

    public String getJobConfigId() {
        return this.jobConfigId;
    }

    public SavepointBackupEntity setJobConfigId(String jobConfigId) {
        this.jobConfigId = jobConfigId;
        return this;
    }

    public String getSavepointPath() {
        return this.savepointPath;
    }

    public SavepointBackupEntity setSavepointPath(String savepointPath) {
        this.savepointPath = savepointPath;
        return this;
    }

    public Date getBackupTime() {
        return this.backupTime;
    }

    public SavepointBackupEntity setBackupTime(Date backupTime) {
        this.backupTime = backupTime;
        return this;
    }
}
