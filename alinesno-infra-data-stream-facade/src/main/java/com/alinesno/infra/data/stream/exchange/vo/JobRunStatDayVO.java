package com.alinesno.infra.data.stream.exchange.vo;

/**
 * @author luoxiaodong
 * des:任务每天运行情况统计
 * @data 2023/4/6
 */

public class JobRunStatDayVO {

    /**
     * 日期
     */

    private String curDay;

    /**
     * 当天任务运行数
     */

    private Long jobTotalCount;

    /**
     * 当天任务运行成功数
     */

    private Long runSuccessCount;

    /**
     * 当天任务运行失败数
     */

    private Long runFailCount;

    /**
     * 当天任务运行其他状态数
     */

    private Long otherStatusCount;



    public String getCurDay() {
        return curDay;
    }

    public void setCurDay(String curDay) {
        this.curDay = curDay;
    }

    public Long getJobTotalCount() {
        return jobTotalCount;
    }

    public void setJobTotalCount(Long jobTotalCount) {
        this.jobTotalCount = jobTotalCount;
    }

    public Long getRunSuccessCount() {
        return runSuccessCount;
    }

    public void setRunSuccessCount(Long runSuccessCount) {
        this.runSuccessCount = runSuccessCount;
    }

    public Long getRunFailCount() {
        return runFailCount;
    }

    public void setRunFailCount(Long runFailCount) {
        this.runFailCount = runFailCount;
    }

    public Long getOtherStatusCount() {
        return otherStatusCount;
    }

    public void setOtherStatusCount(Long otherStatusCount) {
        this.otherStatusCount = otherStatusCount;
    }
}
