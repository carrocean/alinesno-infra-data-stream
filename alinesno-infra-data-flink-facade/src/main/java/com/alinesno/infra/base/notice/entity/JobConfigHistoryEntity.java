package com.alinesno.infra.base.notice.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.alinesno.infra.common.facade.mapper.entity.InfraBaseEntity;

/**
 * 功能名：flink任务配置历史变更
 * 数据表：job_config_history
 * 表备注：flink任务配置历史变更表
 * 
 * 注意：该类使用了mybatis-plus的注解，字段的注释信息已添加中文注释
 * 
 * @TableName 注解用于指定数据库表名
 * @TableField 注解用于指定数据库字段名
 * 
 * 详细注释请参考每个字段的注释信息
 * 
 * 该类继承自InfraBaseEntity，路径为com.alinesno.infra.common.facade.mapper.entity.InfraBaseEntity
 *
 * @author luoxiaodong
 * @version 1.0.0
 */
@TableName("job_config_history")
public class JobConfigHistoryEntity extends InfraBaseEntity {
    private static final long serialVersionUID = 1L;
    
    // job_config主表Id
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
    
    // flink运行配置
    @TableField("flink_run_config")
    private String flinkRunConfig;
    
    // sql语句
    @TableField("flink_sql")
    private String flinkSql;
    
    // checkPoint配置
    @TableField("flink_checkpoint_config")
    private String flinkCheckpointConfig;
    
    // udf地址及连接器jar 如http://xxx.xxx.com/flink-streaming-udf.jar
    @TableField("ext_jar_path")
    private String extJarPath;
    
    // 更新版本号
    @TableField("version")
    private Integer version;
    
    // 批任务定时调度 如 0/20 * * * * ? 表示每20秒 执行任务
    @TableField("cron")
    private String cron;
    
    // 任务类型 0:sql 1:自定义jar
    @TableField("job_type")
    private Integer jobType;
    
    // getter and setter
    public String getJobConfigId() {
        return this.jobConfigId;
    }
    
    public void setJobConfigId(String jobConfigId) {
        this.jobConfigId = jobConfigId;
    }
    
    public String getJobName() {
        return this.jobName;
    }
    
    public void setJobName(String jobName) {
        this.jobName = jobName;
    }
    
    public String getJobDesc() {
        return this.jobDesc;
    }
    
    public void setJobDesc(String jobDesc) {
        this.jobDesc = jobDesc;
    }
    
    public String getDeployMode() {
        return this.deployMode;
    }
    
    public void setDeployMode(String deployMode) {
        this.deployMode = deployMode;
    }
    
    public String getFlinkRunConfig() {
        return this.flinkRunConfig;
    }
    
    public void setFlinkRunConfig(String flinkRunConfig) {
        this.flinkRunConfig = flinkRunConfig;
    }
    
    public String getFlinkSql() {
        return this.flinkSql;
    }
    
    public void setFlinkSql(String flinkSql) {
        this.flinkSql = flinkSql;
    }
    
    public String getFlinkCheckpointConfig() {
        return this.flinkCheckpointConfig;
    }
    
    public void setFlinkCheckpointConfig(String flinkCheckpointConfig) {
        this.flinkCheckpointConfig = flinkCheckpointConfig;
    }
    
    public String getExtJarPath() {
        return this.extJarPath;
    }
    
    public void setExtJarPath(String extJarPath) {
        this.extJarPath = extJarPath;
    }
    
    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
    
    public String getCron() {
        return this.cron;
    }
    
    public void setCron(String cron) {
        this.cron = cron;
    }
    
    public Integer getJobType() {
        return this.jobType;
    }
    
    public void setJobType(Integer jobType) {
        this.jobType = jobType;
    }
}
