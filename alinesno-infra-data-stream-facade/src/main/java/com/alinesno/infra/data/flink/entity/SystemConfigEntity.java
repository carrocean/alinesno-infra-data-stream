package com.alinesno.infra.data.flink.entity;

import com.alinesno.infra.common.facade.mapper.entity.InfraBaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 功能名：系统配置
 * 数据表：system_config
<<<<<<< HEAD
 * 表备注：
 *
 * @TableName 表名注解，用于指定数据库表名
 * @TableField 字段注解，用于指定数据库字段名
=======
 * 表备注：系统配置
 * 
 * 此类表示系统配置实体。
 * 
 * 字段说明：
 * - sysKey: key值
 * - sysVal: value
 * - sysType: 类型，如sys、alarm
 * - sysDesc: 描述
 * - orderId: 序号
 * 
 * 该实体类继承自InfraBaseEntity，使用MyBatis-Plus提供的注解进行字段映射。
 * 
 * 注意：由于没有提供字段的中文注释信息，以下字段注释仅供参考。
>>>>>>> e36282d6d5176c7df7c7d05a7ced619f171c8c63
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
