package cn.vonce.sql.bean;

import java.io.Serializable;

/**
 * Common
 *
 * @author Jovi
 * @version 1.0
 * @email 766255988@qq.com
 * @date 2020年3月1日上午10:00:00
 */
public class Column implements Serializable {
    public Column() {
    }

    public Column(String name) {
        this.name = name;
        this.alias = "";
    }

    public Column(String name, String alias) {
        this.name = name;
        this.alias = alias;
    }

    public Column(String tableAlias, String name, String alias) {
        this.tableAlias = tableAlias;
        this.name = name;
        this.alias = alias;
    }

    private String tableAlias = "";

    private String name = "";

    private String alias = "";

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

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

}
