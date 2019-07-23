package com.lbx.distribution.manageserver.entity;


import java.util.List;

/**
 * @ClassName: 分页结果
 * @Description: //
 */
public class PageResult {
    //记录总条数
    private long total;

    //当前页数
    private int pageNum;


    //分页的列表
    private List<?> list;

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }


    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<?> getList() {
        return list;
    }

    public void setList(List<?> list) {
        this.list = list;
    }
}
