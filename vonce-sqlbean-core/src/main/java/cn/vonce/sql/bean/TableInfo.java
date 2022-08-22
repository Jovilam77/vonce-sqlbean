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
     * schema
     */
    private String schema;
    /**
     * 名称
     */
    private String name;
    /**
     * 注释
     */
    private String comm;

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComm() {
        return comm;
    }

    public void setComm(String comm) {
        this.comm = comm;
    }
}
