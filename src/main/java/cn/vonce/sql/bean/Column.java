package cn.vonce.sql.bean;

import cn.vonce.common.utils.StringUtil;
import cn.vonce.sql.constant.SqlHelperCons;
import cn.vonce.sql.enumerate.EscapeChar;
import java.io.Serializable;

/**
 * Common
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
        this("", tableAlias, name, alias);
    }

    public Column(String schema, String tableAlias, String name, String alias) {
        super(schema, tableAlias, name);
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

    public String fullName() {
        return fullName(null);
    }

    public String fullName(EscapeChar escapeChar) {
        String ec = es(escapeChar);
        if (StringUtil.isNotEmpty(super.getTableAlias())) {
            return ec + super.getTableAlias() + ec + SqlHelperCons.POINT + ec + name() + ec;
        }
        return name(escapeChar);
    }


    public String count() {
        return count(null);
    }

    public String count(EscapeChar escapeChar) {
        String ec = es(escapeChar);
        return "COUNT(" + ec + fullName() + ec + ")";
    }

    public String avg() {
        return avg(null);
    }

    public String avg(EscapeChar escapeChar) {
        String ec = es(escapeChar);
        return "AVG(" + ec + fullName() + ec + ")";
    }

    public String max() {
        return max(null);
    }

    public String max(EscapeChar escapeChar) {
        String ec = es(escapeChar);
        return "MAX(" + ec + fullName() + ec + ")";
    }

    public String min() {
        return min(null);
    }

    public String min(EscapeChar escapeChar) {
        String ec = es(escapeChar);
        return "MIN(" + ec + fullName() + ec + ")";
    }

    public String sum() {
        return sum(null);
    }

    public String sum(EscapeChar escapeChar) {
        String ec = es(escapeChar);
        return "SUM(" + ec + fullName() + ec + ")";
    }

    private String es(EscapeChar escapeChar) {
        if (escapeChar == null) {
            return "";
        }
        String ec = SqlHelperCons.DOUBLE_ESCAPE_CHARACTER;
        if (EscapeChar.SINGLE == escapeChar) {
            ec = SqlHelperCons.SINGLE_ESCAPE_CHARACTER;
        }
        return ec;
    }

    @Override
    public String toString() {
        return fullName();
    }

}
