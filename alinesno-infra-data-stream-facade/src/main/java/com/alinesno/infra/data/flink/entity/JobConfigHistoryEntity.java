package com.alinesno.infra.data.flink.entity;

import com.gitee.sunchenbin.mybatis.actable.annotation.ColumnComment;
import com.gitee.sunchenbin.mybatis.actable.annotation.ColumnType;
import com.alinesno.infra.common.facade.mapper.entity.InfraBaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlTypeConstant;
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
	@ColumnType(MySqlTypeConstant.BIGINT)
	@ColumnComment("job_config主表Id")
    private Long jobConfigId;

    // 任务名称
    @TableField("job_name")
	@ColumnType(length= 64)
	@ColumnComment("任务名称")
    private String jobName;

    // 任务描述
    @TableField("job_desc")
	@ColumnType(length= 255)
	@ColumnComment("任务描述")
    private String jobDesc;

    // 提交模式: standalone 、yarn 、yarn-session
    @TableField("deploy_mode")
	@ColumnType(length= 64)
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

    // udf地址及连接器jar 如http://xxx.xxx.com/flink-streaming-udf.jar
    @TableField("ext_jar_path")
	@ColumnType(length=2048)
	@ColumnComment("udf地址及连接器jar")
    private String extJarPath;

    // 更新版本号
    @TableField("version")
	@ColumnType(MySqlTypeConstant.BIGINT)
	@ColumnComment("更新版本号")
    private Long version;

    // 批任务定时调度 如 0/20 * * * * ? 表示每20秒 执行任务
    @TableField("cron")
	@ColumnType(length= 128)
	@ColumnComment("批任务定时调度")
    private String cron;

    // 任务类型 0:sql 1:自定义jar
    @TableField("job_type")
	@ColumnType(MySqlTypeConstant.INT)
	@ColumnComment("任务类型")
    private Integer jobType;
}
