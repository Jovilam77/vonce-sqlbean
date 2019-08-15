package cn.vonce.sql.bean;

import cn.vonce.sql.enumerate.SqlSort;


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

    /**
     * 获取当前页
     *
     * @return
     * @author jovi
     * @date 2017年3月14日上午11:57:50
     */
    public Integer getPagenum() {
        return pagenum;
    }

    /**
     * 设置当前页
     *
     * @param pagenum
     * @author Jovi
     * @date 2017年7月19日下午5:32:19
     */
    public void setPagenum(Integer pagenum) {
        this.pagenum = pagenum;
    }

    /**
     * 获取每页数量
     *
     * @return
     * @author jovi
     * @date 2017年3月14日上午11:57:55
     */
    public Integer getPagesize() {
        return pagesize;
    }

    /**
     * 设置每页数量
     *
     * @param pagesize
     * @author Jovi
     * @date 2017年7月19日下午5:32:58
     */
    public void setPagesize(Integer pagesize) {
        this.pagesize = pagesize;
    }


    /**
     * 获取排序的字段
     *
     * @return
     * @author jovi
     * @date 2017年5月23日上午11:49:57
     */
    public String[] getSortdatafield() {
        return sortdatafield;
    }

    /**
     * 设置排序的字段
     *
     * @param sortdatafield
     * @author Jovi
     * @date 2017年7月19日下午5:34:23
     */
    public void setSortdatafield(String[] sortdatafield) {
        this.sortdatafield = sortdatafield;
    }

    /**
     * 获取排序的类型
     *
     * @return
     * @author jovi
     * @date 2017年5月23日上午11:50:28
     */
    public String[] getSortorder() {
        return sortorder;
    }

    /**
     * 设置排序类型
     *
     * @param sortorder
     * @author Jovi
     * @date 2017年7月19日下午5:35:02
     */
    public void setSortorder(String[] sortorder) {
        this.sortorder = sortorder;
    }
}
