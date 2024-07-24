package cn.vonce.sql.bean;


//import cn.vonce.sql.define.ColumnFun;
import cn.vonce.sql.enumerate.SqlSort;
//import cn.vonce.sql.uitls.LambdaUtil;

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

    public Paging(Integer pagenum, Integer pagesize, boolean startByZero) {
        this.pagenum = pagenum;
        this.pagesize = pagesize;
        this.startByZero = startByZero;
    }

    public Paging(Integer pagenum, Integer pagesize, Order order) {
        this(pagenum, pagesize, true, new Order[]{order});
    }

    public Paging(Integer pagenum, Integer pagesize, boolean startByZero, Order order) {
        this(pagenum, pagesize, startByZero, new Order[]{order});
    }

    public Paging(Integer pagenum, Integer pagesize, String field, SqlSort sqlSort) {
        this(pagenum, pagesize, true, new Order[]{new Order(field, sqlSort)});
    }

    public Paging(Integer pagenum, Integer pagesize, boolean startByZero, String field, SqlSort sqlSort) {
        this(pagenum, pagesize, startByZero, new Order[]{new Order(field, sqlSort)});
    }

    public Paging(Integer pagenum, Integer pagesize, Column column, SqlSort sqlSort) {
        this(pagenum, pagesize, true, new Order[]{new Order(column, sqlSort)});
    }

//    public <T, R> Paging(Integer pagenum, Integer pagesize, ColumnFun<T, R> columnFun, SqlSort sqlSort) {
//        this(pagenum, pagesize, true, new Order[]{new Order(LambdaUtil.getColumn(columnFun), sqlSort)});
//    }

    public Paging(Integer pagenum, Integer pagesize, boolean startByZero, Column column, SqlSort sqlSort) {
        this(pagenum, pagesize, startByZero, new Order[]{new Order(column, sqlSort)});
    }

    public Paging(Integer pagenum, Integer pagesize, boolean startByZero, Order[] orders) {
        this.pagenum = pagenum;
        this.pagesize = pagesize;
        this.startByZero = startByZero;
        this.orders = orders;
    }

    private Integer pagenum;
    private Integer pagesize;
    private boolean startByZero = true;
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

    public boolean getStartByZero() {
        return startByZero;
    }

    public void setStartByZero(boolean startByZero) {
        this.startByZero = startByZero;
    }

    public Order[] getOrders() {
        return orders;
    }

    public Paging setOrders(Order[] orders) {
        this.orders = orders;
        return this;
    }

}
