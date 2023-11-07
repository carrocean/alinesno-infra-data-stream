package com.alinesno.infra.data.flink.exchange.to;

/**
 * yarn http基本信息
 *
 * @author zhp
 * @Description:
 * @date 2020-08-06
 * @time 23:14
 */
public class AppTO {

  /**
   * yarn appid
   */
  private String id;

  /**
   * 运行用户 如 hadoop
   */
  private String user;

  /**
   * 运行的任务名称
   */
  private String name;

  /**
   * 队列名称
   */
  private String queue;

  /**
   * 运行状态
   */
  private String state;

  private String finalStatus;

  private Integer progress;

  private String trackingUI;

  /**
   * 连接地址
   */
  private String trackingUrl;


  /**
   * 应用类型
   */
  private String applicationType;


  /**
   * 运行开始时间
   */
  private Long startedTime;


  private Long finishedTime;

  /**
   * AM 容器地址
   */
  private String amContainerLogs;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getUser() {
    return user;
  }

  public void setUser(String user) {
    this.user = user;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getQueue() {
    return queue;
  }

  public void setQueue(String queue) {
    this.queue = queue;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public String getFinalStatus() {
    return finalStatus;
  }

  public void setFinalStatus(String finalStatus) {
    this.finalStatus = finalStatus;
  }

  public Integer getProgress() {
    return progress;
  }

  public void setProgress(Integer progress) {
    this.progress = progress;
  }

  public String getTrackingUI() {
    return trackingUI;
  }

  public void setTrackingUI(String trackingUI) {
    this.trackingUI = trackingUI;
  }

  public String getTrackingUrl() {
    return trackingUrl;
  }

  public void setTrackingUrl(String trackingUrl) {
    this.trackingUrl = trackingUrl;
  }

  public String getApplicationType() {
    return applicationType;
  }

  public void setApplicationType(String applicationType) {
    this.applicationType = applicationType;
  }

  public Long getStartedTime() {
    return startedTime;
  }

  public void setStartedTime(Long startedTime) {
    this.startedTime = startedTime;
  }

  public Long getFinishedTime() {
    return finishedTime;
  }

  public void setFinishedTime(Long finishedTime) {
    this.finishedTime = finishedTime;
  }

  public String getAmContainerLogs() {
    return amContainerLogs;
  }

  public void setAmContainerLogs(String amContainerLogs) {
    this.amContainerLogs = amContainerLogs;
  }
}
