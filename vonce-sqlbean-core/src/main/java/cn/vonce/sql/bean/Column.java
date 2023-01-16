package cn.vonce.sql.bean;

import cn.vonce.sql.uitls.StringUtil;

import java.io.Serializable;

/**
 * Column
 *
 * @author Jovi
 * @version 1.0
 * @email 766255988@qq.com
 * @date 2020年3月1日上午10:00:00
 */
public class Column implements Serializable {
    public Column() {
        super();
    }

    public Column(String name) {
        this("", name, "");
    }

    public Column(String name, String alias) {
        this("", name, alias);
    }

    public Column(String tableAlias, String name, String alias) {
        this.tableAlias = tableAlias;
        this.name = name;
        this.alias = alias;
    }

    private String tableAlias;
    private String name;
    private String alias;

    public String getTableAlias() {
        return tableAlias;
    }

    public void setTableAlias(String tableAlias) {
        if (StringUtil.isBlank(this.tableAlias)) {
            this.tableAlias = tableAlias;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (StringUtil.isBlank(this.name)) {
            this.name = name;
        }
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        if (StringUtil.isBlank(this.alias)) {
            this.alias = alias;
        }
    }

    @Override
    public String toString() {
        return name;
    }

}
