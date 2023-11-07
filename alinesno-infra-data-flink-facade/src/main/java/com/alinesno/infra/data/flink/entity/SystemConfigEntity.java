package com.alinesno.infra.data.flink.entity;

import com.alinesno.infra.common.facade.mapper.entity.InfraBaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 功能名：系统配置
 * 数据表：system_config
 * 表备注：
 *
 * @TableName 表名注解，用于指定数据库表名
 * @TableField 字段注解，用于指定数据库字段名
 *
 * @author luoxiaodong
 * @version 1.0.0
 */
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("system_config")
public class SystemConfigEntity extends InfraBaseEntity {

    // key值
    @TableField("sys_key")
    private String sysKey;

    // value
    @TableField("sys_val")
    private String sysVal;

    // 类型 如:sys  alarm
    @TableField("sys_type")
    private String sysType;

    // 描述
    @TableField("sys_desc")
    private String sysDesc;

    // 序号
    @TableField("order_id")
    private Integer orderId;

    public SystemConfigEntity(String key, String Val) {
        this.sysKey = key ;
        this.sysVal = Val ;
    }

}
