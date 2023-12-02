package com.alinesno.infra.data.stream.exchange.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhp
 * @Description:
 * @date 2020-07-15
 * @time 23:24
 */

public class PageModel<E> extends ArrayList<E> {

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

  public List<E> getResult() {
    return this;
  }


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
}
