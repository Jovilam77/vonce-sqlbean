package cn.vonce.sql.bean;

import cn.vonce.sql.enumerate.AlterType;

/**
 * 改变表结构
 *
 * @author Jovi
 * @email imjovi@qq.com
 * @date 2022/9/22 16:46
 */
public class Alter extends Common {

    private AlterType type;
    private ColumnInfo columnInfo;
    private String oldColumnName;
    private String afterColumnName;

    public AlterType getType() {
        return type;
    }

    public void setType(AlterType type) {
        this.type = type;
    }

    public ColumnInfo getColumnInfo() {
        return columnInfo;
    }

    public void setColumnInfo(ColumnInfo columnInfo) {
        this.columnInfo = columnInfo;
    }

    public String getOldColumnName() {
        return oldColumnName;
    }

    public void setOldColumnName(String oldColumnName) {
        this.oldColumnName = oldColumnName;
    }

    public String getAfterColumnName() {
        return afterColumnName;
    }

    public void setAfterColumnName(String afterColumnName) {
        this.afterColumnName = afterColumnName;
    }

}
