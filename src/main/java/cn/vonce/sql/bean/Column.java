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
public class Column extends Field implements Serializable {
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
        super(tableAlias, name);
        this.alias = alias;
    }

    private String alias = "";

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

}
