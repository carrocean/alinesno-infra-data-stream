package com.alinesno.infra.data.flink.exchange.vo;

/**
 * @author luoxiaodong
 * des:任务状态统计
 * @data 2023/6/30
 */

public class JobStatusStatVO {

    /*  JobStatusEnum 对应关系
    RUN(1, "运行中")        => 运行中
    STARTING(2, "启动中")   => 运行中
    STOP(0, "停止"),        => 运行结束
    FAIL(-1, "失败"),       => 运行异常
    SUCCESS(3, "提交成功")   => 提交成功
    UNKNOWN(-2, "未知");    => 提交异常
     */


    //状态标识
    private Integer status = 0;

    /**
     * 状态标签
     */

    private String statusLabel;

    /**
     * 状态记录数统计
     */

    private Integer statusCnt = 0;


    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getStatusLabel() {
        return statusLabel;
    }

    public void setStatusLabel(String statusLabel) {
        this.statusLabel = statusLabel;
    }

    public Integer getStatusCnt() {
        return statusCnt;
    }

    public void setStatusCnt(Integer statusCnt) {
        this.statusCnt = statusCnt;
    }
}
