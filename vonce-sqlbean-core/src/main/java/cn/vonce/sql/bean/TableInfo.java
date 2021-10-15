package cn.vonce.sql.bean;

/**
 * 表信息
 *
 * @author Jovi
 * @version 1.0
 * @email imjovi@qq.com
 * @date 2021-10-15 15:42:22
 */
public class TableInfo {

    /**
     * 名称
     */
    private String name;
    /**
     * 注释
     */
    private String comment;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
