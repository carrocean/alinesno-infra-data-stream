package com.alinesno.infra.data.stream.exchange.rpc.model;


/**
 * @author zhp
 * @Description:
 * @date 2020-09-18
 * @time 23:50
 */

public class JobInfo {

  private String id;

  private String status;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  @Override
  public String toString() {
    return "JobInfo{" +
            "id='" + id + '\'' +
            ", status='" + status + '\'' +
            '}';
  }
}
