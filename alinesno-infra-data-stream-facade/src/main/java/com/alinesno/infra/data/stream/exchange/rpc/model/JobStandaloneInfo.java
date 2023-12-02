package com.alinesno.infra.data.stream.exchange.rpc.model;

/**
 * @author zhp
 * @Description:
 * @date 2020-09-18
 * @time 23:50
 */

public class JobStandaloneInfo {

  private String jid;

  private String state;

  private String errors;


  public String getJid() {
    return jid;
  }

  public void setJid(String jid) {
    this.jid = jid;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public String getErrors() {
    return errors;
  }

  public void setErrors(String errors) {
    this.errors = errors;
  }

  @Override
  public String toString() {
    return "JobStandaloneInfo{" +
            "jid='" + jid + '\'' +
            ", state='" + state + '\'' +
            ", errors='" + errors + '\'' +
            '}';
  }
}
