package com.alinesno.infra.data.flink.entity;

import com.alinesno.infra.common.facade.mapper.entity.InfraBaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 功能名：IP状态
 * 数据表：ip_status
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
@TableName("ip_status")
public class IpStatusEntity extends InfraBaseEntity {

    // ip
    @TableField("ip")
    private String ip;

    // 1:运行 -1:停止
    @TableField("status")
    private Integer status;

    // 最后心跳时间
    @TableField("last_time")
    private LocalDateTime lastTime;

}
