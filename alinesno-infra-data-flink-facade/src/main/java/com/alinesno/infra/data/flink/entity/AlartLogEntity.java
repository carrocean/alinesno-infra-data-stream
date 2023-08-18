package com.alinesno.infra.data.flink.entity;

import com.alinesno.infra.common.facade.mapper.entity.InfraBaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import cn.afterturn.easypoi.excel.annotation.Excel;

/**
 * 功能名：告警发送情况日志
 * 数据表：alart_log
 * 表备注：告警发送情况日志
 * 
 * 注意事项：
 * - 删除掉 @ColumnComment 和 @Excel 注解
 * - 去掉带有 length 属性的 @TableField 注解
 * - 使用 mybatis-plus 的 @TableName 和 @TableField 注解进行注解
 * - @TableField 不允许重复
 * - @TableField 没有 length 属性
 * - InfraBaseEntity 的路径为 com.alinesno.infra.common.facade.mapper.entity.InfraBaseEntity
 * - 类的字段需要添加中文注释信息
 * - 类需要添加详细的注释信息
 *
 * @author luoxiaodong
 * @version 1.0.0
 */
@TableName("alart_log")
public class AlartLogEntity extends InfraBaseEntity {

    /**
     * job_config 的 id，任务id
     */
    @TableField("job_config_id")
    @Excel(name = "job_config的id，如果0代表的是测试")
    private String jobConfigId;

    /**
     * jobName
     */
    @TableField("job_name")
    @Excel(name = "jobName")
    private String jobName;

    /**
     * 消息内容
     */
    @TableField("message")
    @Excel(name = "消息内容")
    private String message;

    /**
     * 1:钉钉
     */
    @TableField("type")
    @Excel(name = "1:钉钉")
    private Integer type;

    /**
     * 1:成功 0:失败
     */
    @TableField("status")
    @Excel(name = "1:成功 0:失败")
    private Integer status;

    /**
     * 失败原因
     */
    @TableField("fail_log")
    @Excel(name = "失败原因")
    private String failLog;

    // getter and setter methods
}
