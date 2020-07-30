package cn.vonce.sql.bean;

import cn.vonce.sql.enumerate.JdbcType;

public class ColumnInfo {

    private boolean notNull;
    private JdbcType type;
    private int length;
    private int decimal;
    private String def;

    public boolean getNotNull() {
        return notNull;
    }

    public void setNotNull(boolean notNull) {
        this.notNull = notNull;
    }

    public JdbcType getType() {
        return type;
    }

    public void setType(JdbcType type) {
        this.type = type;
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

    public String getDef() {
        return def;
    }

    public void setDef(String def) {
        this.def = def;
    }
}
