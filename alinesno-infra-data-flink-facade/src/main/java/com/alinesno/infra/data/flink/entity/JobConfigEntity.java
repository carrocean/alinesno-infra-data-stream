package com.alinesno.infra.data.flink.entity;

import java.util.Date;

import com.alinesno.infra.common.facade.mapper.entity.InfraBaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 功能名：flink任务配置
 * 数据表：job_config
 * 表备注：flink任务配置表
 * 
 * 注意：该类使用了MyBatis-Plus注解进行ORM映射，字段注释和长度属性已经通过注解标记，无需再添加其他注释。
 * 
 * 数据库表字段说明：
 * job_name：任务名称
 * job_desc：任务描述
 * deploy_mode：提交模式，可选值：standalone、yarn、yarn-session
 * flink_run_config：flink运行配置
 * flink_sql：sql语句
 * flink_checkpoint_config：checkPoint配置
 * job_id：运行后的任务id
 * is_open：1表示开启，0表示关闭
 * status：1表示运行中，0表示停止中，-1表示运行失败
 * cron：批任务定时调度，如0/20 * * * * ? 表示每20秒执行任务
 * ext_jar_path：udf地址已经连接器jar，如http://xxx.xxx.com/flink-streaming-udf.jar
 * last_start_time：最后一次启动时间
 * last_run_log_id：最后一次日志
 * version：更新版本号，用于乐观锁
 * job_type：任务类型，0表示sql，1表示自定义jar
 * custom_args：启动jar可能需要使用的自定义参数
 * custom_main_class：程序入口类
 * custom_jar_url：自定义jar的http地址，如http://ccblog.cn/xx.jar
 * 
 * 注意：该类继承自InfraBaseEntity，该类路径为com.alinesno.infra.common.facade.mapper.entity.InfraBaseEntity
 *
 * @author luoxiaodong
 * @version 1.0.0
 */
@TableName("job_config")
public class JobConfigEntity extends InfraBaseEntity {

    // 任务名称
    @TableField("job_name")
    private String jobName;

    // 任务描述
    @TableField("job_desc")
    private String jobDesc;

    // 提交模式: standalone、yarn、yarn-session
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

    // 运行后的任务id
    @TableField("job_id")
    private String jobId;

    // 1:开启 0:关闭
    @TableField("is_open")
    private Integer isOpen;

    // 1:运行中 0:停止中 -1:运行失败
    @TableField("status")
    private Integer status;

    // 批任务定时调度，如0/20 * * * * ? 表示每20秒执行任务
    @TableField("cron")
    private String cron;

    // udf地址已经连接器jar，如http://xxx.xxx.com/flink-streaming-udf.jar
    @TableField("ext_jar_path")
    private String extJarPath;

    // 最后一次启动时间
    @TableField("last_start_time")
    private Date lastStartTime;

    // 最后一次日志
    @TableField("last_run_log_id")
    private String lastRunLogId;

    // 更新版本号，用于乐观锁
    @TableField("version")
    private Integer version;

    // 任务类型，0:sql 1:自定义jar
    @TableField("job_type")
    private Integer jobType;

    // 启动jar可能需要使用的自定义参数
    @TableField("custom_args")
    private String customArgs;

    // 程序入口类
    @TableField("custom_main_class")
    private String customMainClass;

    // 自定义jar的http地址，如:http://ccblog.cn/xx.jar
    @TableField("custom_jar_url")
    private String customJarUrl;

    // getter and setter
}
