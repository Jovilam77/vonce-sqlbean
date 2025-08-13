package cn.vonce.sql.bean;

import cn.vonce.sql.uitls.StringUtil;

import java.io.Serializable;

/**
 * 表名
 *
 * @author Jovi
 * @version 1.0
 * @email imjovi@qq.com
 * @date 2019/9/27 11:49
 */
public class Table implements Serializable {

    public Table() {

    }

    public Table(String name) {
        this.name = name;
    }

    public Table(String name, String alias) {
        this("", name, alias);
    }

    public Table(String schema, String name, String alias) {
        this.schema = schema;
        this.name = name;
        this.alias = alias;
    }

    private String schema = "";
    private String name = "";
    private String alias = "";

    public String getSchema() {
        return schema;
    }

    public String getSchema(boolean toUpperCase) {
        if (toUpperCase) {
            return schema.toUpperCase();
        }
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public String getName() {
        return name;
    }

    public String getName(boolean toUpperCase) {
        if (toUpperCase) {
            return name.toUpperCase();
        }
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

    public boolean isNotSet() {
        if (StringUtil.isEmpty(getSchema()) && StringUtil.isEmpty(getName()) && StringUtil.isEmpty(getAlias())) {
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "Table{" +
                "schema='" + schema + '\'' +
                ", name='" + name + '\'' +
                ", alias='" + alias + '\'' +
                '}';
    }
}
