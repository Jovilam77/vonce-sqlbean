package cn.vonce.sql.bean;

import java.io.Serializable;

/**
 * Column
 *
 * @author Jovi
 * @version 1.0
 * @email imjovi@qq.com
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
        this(false, tableAlias, name, alias, "", true);
    }

    public Column(String tableAlias, String name, String alias, boolean nameEscape) {
        this(false, tableAlias, name, alias, "", nameEscape);
    }

    public Column(boolean immutable, String tableAlias, String name, String alias) {
        this(immutable, tableAlias, name, alias, "", true);
    }

    public Column(boolean immutable, String tableAlias, String name, String alias, String remarks) {
        this(immutable, tableAlias, name, alias, remarks, true);
    }

    public Column(boolean immutable, String tableAlias, String name, String alias, String remarks, boolean nameEscape) {
        this.immutable = immutable;
        this.tableAlias = tableAlias;
        this.name = name;
        this.alias = alias;
        this.remarks = remarks;
        this.nameEscape = nameEscape;
    }

    private boolean immutable;
    private String tableAlias;
    private String name;
    private String alias;
    private String remarks;
    private boolean nameEscape;

    public String getTableAlias() {
        return tableAlias;
    }

    public void setTableAlias(String tableAlias) {
        if (immutable) {
            return;
        }
        this.tableAlias = tableAlias;
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
        if (immutable) {
            return;
        }
        this.name = name;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        if (immutable) {
            return;
        }
        this.alias = alias;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        if (immutable) {
            return;
        }
        this.remarks = remarks;
    }

    public boolean isImmutable() {
        return immutable;
    }

    public boolean isNameEscape() {
        return nameEscape;
    }

    @Override
    public String toString() {
        return name;
    }

}
