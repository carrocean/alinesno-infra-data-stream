package com.alinesno.infra.base.notice.entity;

import com.alinesno.infra.common.facade.mapper.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;

/**
 * web服务运行ip
 * 
 * 数据表：ip_status
 * 表备注：web服务运行ip
 *
 * @author luoxiaodong
 * @version 1.0.0
 */
@TableName("ip_status")
public class IpStatusEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * IP地址
     */
    @TableField("ip")
    private String ip;

    /**
     * 运行状态，1:运行，-1:停止
     */
    @TableField("status")
    private Integer status;

    /**
     * 最后心跳时间
     */
    @TableField("last_time")
    private Date lastTime;

    /**
     * 获取IP地址
     *
     * @return IP地址
     */
    public String getIp() {
        return ip;
    }

    /**
     * 设置IP地址
     *
     * @param ip IP地址
     */
    public void setIp(String ip) {
        this.ip = ip;
    }

    /**
     * 获取运行状态
     *
     * @return 运行状态，1:运行，-1:停止
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置运行状态
     *
     * @param status 运行状态，1:运行，-1:停止
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 获取最后心跳时间
     *
     * @return 最后心跳时间
     */
    public Date getLastTime() {
        return lastTime;
    }

    /**
     * 设置最后心跳时间
     *
     * @param lastTime 最后心跳时间
     */
    public void setLastTime(Date lastTime) {
        this.lastTime = lastTime;
    }
}
