package cn.vonce.sql.bean;


import cn.vonce.sql.enumerate.SqlSort;

import java.io.Serializable;

/**
 * 分页
 *
 * @author Jovi
 * @version 1.0
 * @email imjovi@qq.com
 * @date 2017年3月14日上午11:57:50
 */
public class Paging implements Serializable {

    public Paging() {

    }

    public Paging(Integer pagenum, Integer pagesize) {
        this.pagenum = pagenum;
        this.pagesize = pagesize;
    }

    public Paging(Integer pagenum, Integer pagesize, Order order) {
        this(pagenum, pagesize, new Order[]{order});
    }

    public Paging(Integer pagenum, Integer pagesize, String field, SqlSort sqlSort) {
        this(pagenum, pagesize, new Order[]{new Order(field, sqlSort)});
    }

    public Paging(Integer pagenum, Integer pagesize, SqlColumn sqlColumn, SqlSort sqlSort) {
        this(pagenum, pagesize, new Order[]{new Order(sqlColumn, sqlSort)});
    }

    public Paging(Integer pagenum, Integer pagesize, Order[] orders) {
        this.pagenum = pagenum;
        this.pagesize = pagesize;
        this.orders = orders;
    }


    private Integer pagenum;
    private Integer pagesize;
    private Order[] orders;


    public Integer getPagenum() {
        return pagenum;
    }

    public void setPagenum(Integer pagenum) {
        this.pagenum = pagenum;
    }

    public Integer getPagesize() {
        return pagesize;
    }

    public void setPagesize(Integer pagesize) {
        this.pagesize = pagesize;
    }

    public Order[] getOrders() {
        return orders;
    }

    public Paging setOrders(Order[] orders) {
        this.orders = orders;
        return this;
    }

}
