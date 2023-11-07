package com.alinesno.infra.data.flink.entity;

import com.alinesno.infra.common.facade.mapper.entity.InfraBaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 功能名：任务配置历史记录
 * 数据表：job_config_history
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
@TableName("job_config_history")
public class JobConfigHistoryEntity extends InfraBaseEntity {

    // job_config主表Id
    @TableField("job_config_id")
    private Long jobConfigId;

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
    private Long version;

    // 批任务定时调度 如 0/20 * * * * ? 表示每20秒 执行任务
    @TableField("cron")
    private String cron;

    // 任务类型 0:sql 1:自定义jar
    @TableField("job_type")
    private Integer jobType;

}
