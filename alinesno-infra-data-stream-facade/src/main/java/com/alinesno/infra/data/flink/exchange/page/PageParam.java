package com.alinesno.infra.data.flink.exchange.page;

/**
 * @author zhp
 * @Description:
 * @date 2020-07-15
 * @time 23:27
 */

public class PageParam {

  /**
   * 当前页码
   */
  private int pageNum = 1;
  /**
   * 每页数量
   */
  private int pageSize = 15;

  public int getPageNum() {
    return pageNum;
  }

  public void setPageNum(int pageNum) {
    this.pageNum = pageNum;
  }

  public int getPageSize() {
    return pageSize;
  }

  public void setPageSize(int pageSize) {
    this.pageSize = pageSize;
  }
}
