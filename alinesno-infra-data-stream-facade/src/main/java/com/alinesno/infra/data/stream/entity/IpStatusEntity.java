package com.alinesno.infra.data.stream.entity;

import com.alinesno.infra.common.facade.mapper.entity.InfraBaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gitee.sunchenbin.mybatis.actable.annotation.ColumnComment;
import com.gitee.sunchenbin.mybatis.actable.annotation.ColumnType;
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlTypeConstant;
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
	@ColumnType(length=64)
	@ColumnComment("ip")
    private String ip;

    // 1:运行 -1:停止
    @TableField("status")
	@ColumnType(length= 11)
	@ColumnComment("1:运行 -1:停止")
    private Integer status;

    // 最后心跳时间
    @TableField("last_time")
	@ColumnType(MySqlTypeConstant.DATETIME)
	@ColumnComment("最后心跳时间")
    private LocalDateTime lastTime;
}
