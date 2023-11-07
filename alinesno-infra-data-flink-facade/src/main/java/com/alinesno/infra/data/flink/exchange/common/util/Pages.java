package com.alinesno.infra.data.flink.exchange.common.util;

/**
 * @author zhp
 * @Description:
 * @date 2020-07-15
 * @time 19:45
 */
public class Pages {

  /**
   * 当前页码
   */
  private int pageNum;
  /**
   * 每页数量
   */
  private int pageSize;
  /**
   * 记录总数
   */
  private long totalSize;
  /**
   * 页码总数
   */
  private int totalPages;

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

  public long getTotalSize() {
    return totalSize;
  }

  public void setTotalSize(long totalSize) {
    this.totalSize = totalSize;
  }

  public int getTotalPages() {
    return totalPages;
  }

  public void setTotalPages(int totalPages) {
    this.totalPages = totalPages;
  }
}
