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

}
