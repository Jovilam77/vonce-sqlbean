package cn.vonce.sql.bean;

import cn.vonce.sql.constant.SqlConstant;
import cn.vonce.sql.enumerate.EscapeChar;
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
public class Column extends SqlField implements Serializable {
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

    public String name() {
        return super.getName();
    }

    public String name(EscapeChar EscapeChar) {
        String ec = es(EscapeChar);
        return ec + name() + ec;
    }

    public String fullName(EscapeChar escapeChar) {
        String ec = es(escapeChar);
        if (StringUtil.isNotEmpty(super.getTableAlias())) {
            return ec + super.getTableAlias() + ec + SqlConstant.POINT + ec + name() + ec;
        }
        return name(escapeChar);
    }


    public String count() {
        return count(null);
    }

    public String count(EscapeChar escapeChar) {
        String ec = es(escapeChar);
        String name = escapeChar == null ? name() : fullName(escapeChar);
        return "COUNT(" + ec + name + ec + ")";
    }

    public String avg() {
        return avg(null);
    }

    public String avg(EscapeChar escapeChar) {
        String ec = es(escapeChar);
        String name = escapeChar == null ? name() : fullName(escapeChar);
        return "AVG(" + ec + name + ec + ")";
    }

    public String max() {
        return max(null);
    }

    public String max(EscapeChar escapeChar) {
        String ec = es(escapeChar);
        String name = escapeChar == null ? name() : fullName(escapeChar);
        return "MAX(" + ec + name + ec + ")";
    }

    public String min() {
        return min(null);
    }

    public String min(EscapeChar escapeChar) {
        String ec = es(escapeChar);
        String name = escapeChar == null ? name() : fullName(escapeChar);
        return "MIN(" + ec + name + ec + ")";
    }

    public String sum() {
        return sum(null);
    }

    public String sum(EscapeChar escapeChar) {
        String ec = es(escapeChar);
        String name = escapeChar == null ? name() : fullName(escapeChar);
        return "SUM(" + ec + name + ec + ")";
    }

    private String es(EscapeChar escapeChar) {
        if (escapeChar == null) {
            return "";
        }
        String ec = SqlConstant.DOUBLE_ESCAPE_CHARACTER;
        if (EscapeChar.SINGLE == escapeChar) {
            ec = SqlConstant.SINGLE_ESCAPE_CHARACTER;
        }
        return ec;
    }

    @Override
    public String toString() {
        return name();
    }

}
