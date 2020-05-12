package cn.vonce.sql.bean;

import java.io.Serializable;

public class SqlColumn implements Serializable {

    public SqlColumn() {
    }

    public SqlColumn(String tableAlias, String name) {
        this.tableAlias = tableAlias;
        this.name = name;
    }

    public SqlColumn(String schema, String tableAlias, String name) {
        this.schema = schema;
        this.tableAlias = tableAlias;
        this.name = name;
    }

    private String schema = "";
    private String tableAlias = "";
    private String name = "";

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

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
        return "SqlField{" +
                "schema='" + schema + '\'' +
                ", tableAlias='" + tableAlias + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

}
