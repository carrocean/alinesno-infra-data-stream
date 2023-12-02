package com.alinesno.infra.data.stream.exchange.vo;



/**
 * @author zhp
 * @Description:
 * @date 2020-08-05
 * @time 22:43
 */

public class PageVO<T> {

  /**
   * 当前页码
   */
  private int pageNum;

  /**
   * 每页数量
   */
  private int pageSize;

  /**
   * 页码
   */
  private int pages;

  /**
   * 总条数
   */
  private long total;


  private T data;

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

  public int getPages() {
    return pages;
  }

  public void setPages(int pages) {
    this.pages = pages;
  }

  public long getTotal() {
    return total;
  }

  public void setTotal(long total) {
    this.total = total;
  }

  public T getData() {
    return data;
  }

  public void setData(T data) {
    this.data = data;
  }
}
