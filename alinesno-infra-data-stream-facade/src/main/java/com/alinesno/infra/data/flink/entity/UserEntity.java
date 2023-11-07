package com.alinesno.infra.data.flink.entity;

import com.alinesno.infra.common.facade.mapper.entity.InfraBaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 功能名：用户
 * 数据表：user
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
@TableName("user")
public class UserEntity extends InfraBaseEntity {

    // 用户帐号
    @TableField("username")
    private String username;

    // 用户姓名
    @TableField("name")
    private String name;

    // 密码
    @TableField("password")
    private String password;

    // 1:启用 0: 停用
    @TableField("status")
    private Integer status;

}
