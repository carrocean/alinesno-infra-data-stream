package com.alinesno.infra.data.flink.exchange.vo;

public class jobTypeRunCount {

    //任务类型ID
    private Integer jobType;

    //任务类型名称
    private String jobTypeName;

    //任务类型运行次数
    private Integer runNum;

    public Integer getJobType() {
        return jobType;
    }

    public void setJobType(Integer jobType) {
        this.jobType = jobType;
    }

    public String getJobTypeName() {
        return jobTypeName;
    }

    public void setJobTypeName(String jobTypeName) {
        this.jobTypeName = jobTypeName;
    }

    public Integer getRunNum() {
        return runNum;
    }

    public void setRunNum(Integer runNum) {
        this.runNum = runNum;
    }
}
