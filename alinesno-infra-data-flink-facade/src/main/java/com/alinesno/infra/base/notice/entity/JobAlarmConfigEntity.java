package com.alinesno.infra.base.notice.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.alinesno.infra.common.facade.mapper.entity.InfraBaseEntity;

/**
 * 告警辅助配置
 * 
 * 数据表：job_alarm_config
 * 表备注：告警辅助配置表
 *
 * @author luoxiaodong
 * @version 1.0.0
 */
@TableName("job_alarm_config")
public class JobAlarmConfigEntity extends InfraBaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * job_config主表id
     */
    @TableField("job_id")
    private String jobId;

    /**
     * 类型 1:钉钉告警 2:url回调 3:异常自动拉起任务
     */
    @TableField("type")
    private Integer type;

    /**
     * 更新版本号
     */
    @TableField("version")
    private Long version;

    /**
     * 获取job_config主表id
     *
     * @return job_config主表id
     */
    public String getJobId() {
        return jobId;
    }

    /**
     * 设置job_config主表id
     *
     * @param jobId job_config主表id
     */
    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    /**
     * 获取类型
     *
     * @return 类型 1:钉钉告警 2:url回调 3:异常自动拉起任务
     */
    public Integer getType() {
        return type;
    }

    /**
     * 设置类型
     *
     * @param type 类型 1:钉钉告警 2:url回调 3:异常自动拉起任务
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * 获取更新版本号
     *
     * @return 更新版本号
     */
    public Long getVersion() {
        return version;
    }

    /**
     * 设置更新版本号
     *
     * @param version 更新版本号
     */
    public void setVersion(Long version) {
        this.version = version;
    }
}
