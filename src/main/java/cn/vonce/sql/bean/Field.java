package cn.vonce.sql.bean;

import java.io.Serializable;

public class Field implements Serializable {

    public Field() {
    }

    public Field(String tableAlias, String name) {
        this.tableAlias = tableAlias;
        this.name = name;
    }

    private String tableAlias = "";

    private String name = "";

    public String getTableAlias() {
        return tableAlias;
    }

    public void setTableAlias(String tableAlias) {
        this.tableAlias = tableAlias;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Field{" +
                "tableAlias='" + tableAlias + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
