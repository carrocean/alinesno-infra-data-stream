package com.alinesno.infra.data.flink.commom.model;


import com.alinesno.infra.data.flink.commom.enums.SqlCommand;

/**
 * @author zhp
 * @Description:
 * @date 2020-06-23
 * @time 02:56
 */

public class SqlCommandCall {

  private SqlCommand sqlCommand;

  private String[] operands;

  public SqlCommandCall(SqlCommand sqlCommand, String[] operands) {
    this.sqlCommand = sqlCommand;
    this.operands = operands;
  }

  public SqlCommandCall(String[] operands) {
    this.operands = operands;
  }

  public SqlCommand getSqlCommand() {
    return sqlCommand;
  }

  public String[] getOperands() {
    return operands;
  }

  public void setSqlCommand(SqlCommand sqlCommand) {
    this.sqlCommand = sqlCommand;
  }

  public void setOperands(String[] operands) {
    this.operands = operands;
  }
}
