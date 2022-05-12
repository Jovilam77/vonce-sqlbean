package cn.vonce.sql.page;

import java.util.List;

/**
 * 用于返回分页数据结果集(Result Set)
 *
 * @author jovi
 * @version 1.0
 * @email 766255988@qq.com
 * @date 2017年5月24日上午11:32:38
 */
public class ResultData<T> {

    private List<T> data;
    private Integer pagenum;
    private Integer pagesize;
    private Integer totalRecords;
    private Integer totalPage;
    private String timestamp;

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
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

    public Integer getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(Integer totalRecords) {
        this.totalRecords = totalRecords;
    }

    public Integer getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "ResultData{" +
                "data=" + data +
                ", pagenum=" + pagenum +
                ", pagesize=" + pagesize +
                ", totalRecords=" + totalRecords +
                ", totalPage=" + totalPage +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }
}
