package cn.vonce.sql.bean;

/**
 * 列字段信息
 *
 * @author Jovi
 * @version 1.0
 * @email 766255988@qq.com
 */
public class ColumnInfo {

    /**
     * 列id
     */
    private Integer cid;
    /**
     * 列名
     */
    private String name;
    /**
     * 类型
     */
    private String type;
    /**
     * 是否可为null
     */
    private Boolean notnull;
    /**
     * 默认值
     */
    private String dfltValue;
    /**
     * 是否为主键
     */
    private Boolean pk;
    /**
     * 是否为外键
     */
    private Boolean fk;
    /**
     * 字段长度
     */
    private int length;
    /**
     * 字段小数点精度
     */
    private int decimal;
    /**
     * 注释
     */
    private String comment;

    public Integer getCid() {
        return cid;
    }

    public void setCid(Integer cid) {
        this.cid = cid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getNotnull() {
        return notnull;
    }

    public void setNotnull(Boolean notnull) {
        this.notnull = notnull;
    }

    public String getDfltValue() {
        return dfltValue;
    }

    public void setDfltValue(String dfltValue) {
        this.dfltValue = dfltValue;
    }

    public Boolean getPk() {
        return pk;
    }

    public void setPk(Boolean pk) {
        this.pk = pk;
    }

    public Boolean getFk() {
        return fk;
    }

    public void setFk(Boolean fk) {
        this.fk = fk;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getDecimal() {
        return decimal;
    }

    public void setDecimal(int decimal) {
        this.decimal = decimal;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
