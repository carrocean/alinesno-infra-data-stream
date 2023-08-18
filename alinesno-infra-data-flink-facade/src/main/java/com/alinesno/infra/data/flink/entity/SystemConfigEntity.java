package com.alinesno.infra.data.flink.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.alinesno.infra.common.facade.mapper.entity.InfraBaseEntity;

/**
 * 功能名：系统配置
 * 数据表：system_config
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
 *
 * @author luoxiaodong
 * @version 1.0.0
 */
@TableName("system_config")
public class SystemConfigEntity extends InfraBaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * key值
     */
    @TableField("sys_key")
    private String sysKey;

    /**
     * value
     */
    @TableField("sys_val")
    private String sysVal;

    /**
     * 类型，如sys、alarm
     */
    @TableField("sys_type")
    private String sysType;

    /**
     * 描述
     */
    @TableField("sys_desc")
    private String sysDesc;

    /**
     * 序号
     */
    @TableField("order_id")
    private int orderId;

    // getter and setter methods

    public String getSysKey() {
        return sysKey;
    }

    public void setSysKey(String sysKey) {
        this.sysKey = sysKey;
    }

    public String getSysVal() {
        return sysVal;
    }

    public void setSysVal(String sysVal) {
        this.sysVal = sysVal;
    }

    public String getSysType() {
        return sysType;
    }

    public void setSysType(String sysType) {
        this.sysType = sysType;
    }

    public String getSysDesc() {
        return sysDesc;
    }

    public void setSysDesc(String sysDesc) {
        this.sysDesc = sysDesc;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }
}
