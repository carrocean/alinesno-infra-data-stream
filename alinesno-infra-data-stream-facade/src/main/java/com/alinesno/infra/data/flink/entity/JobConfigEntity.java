package com.alinesno.infra.data.flink.entity;

import com.gitee.sunchenbin.mybatis.actable.annotation.ColumnComment;
import com.gitee.sunchenbin.mybatis.actable.annotation.ColumnType;
import com.alinesno.infra.common.facade.mapper.entity.InfraBaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlTypeConstant;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 功能名：任务配置
 * 数据表：job_config
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
@TableName("job_config")
public class JobConfigEntity extends InfraBaseEntity {

    // 任务名称
    @TableField("job_name")
	@ColumnType(length= 64)
	@ColumnComment("任务名称")
    private String jobName;

    // 任务描述
    @TableField("job_desc")
	@ColumnType(length=255)
	@ColumnComment("任务描述")
    private String jobDesc;

    // 提交模式: standalone 、yarn 、yarn-session
    @TableField("deploy_mode")
	@ColumnType(length=64)
	@ColumnComment("提交模式: standalone 、yarn 、yarn-session")
    private String deployMode;

    // flink运行配置
    @TableField("flink_run_config")
	@ColumnType(length=512)
	@ColumnComment("flink运行配置")
    private String flinkRunConfig;

    // sql语句
    @TableField("flink_sql")
	@ColumnType(MySqlTypeConstant.LONGTEXT)
	@ColumnComment("sql语句")
    private String flinkSql;

    // checkPoint配置
    @TableField("flink_checkpoint_config")
	@ColumnType(length=512)
	@ColumnComment("checkPoint配置")
    private String flinkCheckpointConfig;

    // 运行后的任务id
    @TableField("job_id")
	@ColumnType(length= 64)
	@ColumnComment("运行后的任务id")
    private String jobId;

    // 1:开启 0: 关闭
    @TableField("is_open")
	@ColumnType(length=11)
	@ColumnComment("1:开启 0: 关闭")
    private Integer isOpen;

    // 1:运行中 0: 停止中 -1:运行失败
    @TableField("status")
	@ColumnType(length= 11)
	@ColumnComment("1:运行中 0: 停止中 -1:运行失败")
    private Integer status;

    // 批任务定时调度 如 0/20 * * * * ? 表示每20秒 执行任务
    @TableField("cron")
	@ColumnType(length=128)
	@ColumnComment("批任务定时调度 如 0/20 * * * * ? 表示每20秒 执行任务")
    private String cron;

    // udf地址已经连接器jar 如http://xxx.xxx.com/flink-streaming-udf.jar
    @TableField("ext_jar_path")
	@ColumnType(length=2048)
	@ColumnComment("udf地址已经连接器jar 如http://xxx.xxx.com/flink-streaming-udf.jar")
    private String extJarPath;

    // 最后一次启动时间
    @TableField("last_start_time")
	@ColumnType(MySqlTypeConstant.DATE)
	@ColumnComment("最后一次启动时间")
    private Date lastStartTime;

    // 最后一次日志
    @TableField("last_run_log_id")
	@ColumnType(length=64)
	@ColumnComment("最后一次日志")
    private String lastRunLogId;

    // 更新版本号 用于乐观锁
    @TableField("version")
	@ColumnType(length=11)
	@ColumnComment("更新版本号")
    private Long version;

    // 任务类型 0:sql 1:自定义jar
    @TableField("job_type")
	@ColumnType(length= 11)
	@ColumnComment("任务类型 0:sql 1:自定义jar")
    private Integer jobType;

    // 启动jar可能需要使用的自定义参数
    @TableField("custom_args")
	@ColumnType(length=128)
	@ColumnComment("启动jar可能需要使用的自定义参数")
    private String customArgs;

    // 程序入口类
    @TableField("custom_main_class")
	@ColumnType(length=128)
	@ColumnComment("程序入口类")
    private String customMainClass;

    // 自定义jar的http地址 如:http://ccblog.cn/xx.jar
    @TableField("custom_jar_url")
	@ColumnType(length=128)
	@ColumnComment("自定义jar的http地址")
    private String customJarUrl;
}
