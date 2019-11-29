package cn.vonce.sql.bean;

import cn.vonce.sql.enumerate.SqlSort;

/**
 * 分页
 *
 * @author Jovi
 * @version 1.0
 * @email imjovi@qq.com
 * @date 2017年3月14日上午11:57:50
 */
public class Paging {

    public Paging() {

    }

    public Paging(Integer pagenum, Integer pagesize) {
        this.pagenum = pagenum;
        this.pagesize = pagesize;
    }

    public Paging(Integer pagenum, Integer pagesize, String sortfield, String sortorder) {
        this.pagenum = pagenum;
        this.pagesize = pagesize;
        this.sortdatafield = new String[1];
        this.sortorder = new String[1];
        this.sortdatafield[0] = sortfield;
        this.sortorder[0] = sortorder;
    }

    public Paging(Integer pagenum, Integer pagesize, String sortfield, SqlSort sqlSort) {
        this.pagenum = pagenum;
        this.pagesize = pagesize;
        this.sortdatafield = new String[1];
        this.sortorder = new String[1];
        this.sortdatafield[0] = sortfield;
        if (sqlSort == SqlSort.ASC) {
            this.sortorder[0] = SqlSort.ASC.name();
        } else {
            this.sortorder[0] = SqlSort.DESC.name();
        }
    }

    public Paging(Integer pagenum, Integer pagesize, String[] sortfield, String[] sortorder) {
        this.pagenum = pagenum;
        this.pagesize = pagesize;
        this.sortdatafield = sortfield;
        this.sortorder = sortorder;
    }

    private Integer pagenum;
    private Integer pagesize;
    private String[] sortdatafield;
    private String[] sortorder;


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

    public String[] getSortdatafield() {
        return sortdatafield;
    }

    public void setSortdatafield(String[] sortdatafield) {
        this.sortdatafield = sortdatafield;
    }

    public String[] getSortorder() {
        return sortorder;
    }

    public void setSortorder(String[] sortorder) {
        this.sortorder = sortorder;
    }
}
