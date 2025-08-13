package cn.vonce.sql.bean;

import java.io.Serializable;

/**
 * SqlBean 分页
 *
 * @author Jovi
 * @version 1.0
 * @email imjovi@qq.com
 * @date 2019/8/13 18:33
 */
public class Page implements Serializable {

    public Page() {

    }

    public Page(Integer pagenum, Integer pagesize) {
        this(null, pagenum, pagesize, false);
    }

    public Page(Integer pagenum, Integer pagesize, boolean startByZero) {
        this(null, pagenum, pagesize, startByZero);
    }

    public Page(String idName, Integer pagenum, Integer pagesize) {
        this(idName, pagenum, pagesize, false);
    }

    public Page(String idName, Integer pagenum, Integer pagesize, boolean startByZero) {
        this.idName = idName;
        this.pagenum = pagenum;
        this.pagesize = pagesize;
        this.startByZero = startByZero;
    }

    /**
     * SqlServer分页需要主键名
     */
    private String idName;
    /**
     * 当前页数
     */
    private Integer pagenum;
    /**
     * 每页显示数
     */
    private Integer pagesize;
    /**
     * 页数从0开始
     */
    private boolean startByZero = true;

    public String getIdName() {
        return idName;
    }

    public void setIdName(String idName) {
        this.idName = idName;
    }

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

    @Override
    public String toString() {
        return "Page{" +
                "idName='" + idName + '\'' +
                ", pagenum=" + pagenum +
                ", pagesize=" + pagesize +
                '}';
    }
}
