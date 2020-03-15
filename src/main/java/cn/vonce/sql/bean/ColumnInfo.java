package cn.vonce.sql.bean;

import cn.vonce.common.utils.StringUtil;
import cn.vonce.sql.constant.SqlHelperCons;

/**
 * 列字段信息
 */
public class ColumnInfo {

    public ColumnInfo() {
    }

    public ColumnInfo(String tableAlias, String name) {
        this.tableAlias = tableAlias;
        this.name = name;
    }

    private String tableAlias;
    private String name;

    public String tableAlias() {
        return tableAlias;
    }

    public String fullName() {
        if (StringUtil.isNotEmpty(tableAlias)) {
            return tableAlias + SqlHelperCons.POINT + name;
        }
        return name;
    }

    public String secFullName() {
        if (StringUtil.isNotEmpty(tableAlias)) {
            return SqlHelperCons.SINGLE_ESCAPE_CHARACTER + tableAlias + SqlHelperCons.POINT + name + SqlHelperCons.SINGLE_ESCAPE_CHARACTER;
        }
        return SqlHelperCons.SINGLE_ESCAPE_CHARACTER + name + SqlHelperCons.SINGLE_ESCAPE_CHARACTER;
    }

    public String decFullName() {
        if (StringUtil.isNotEmpty(tableAlias)) {
            return SqlHelperCons.DOUBLE_ESCAPE_CHARACTER + tableAlias + SqlHelperCons.POINT + name + SqlHelperCons.DOUBLE_ESCAPE_CHARACTER;
        }
        return SqlHelperCons.DOUBLE_ESCAPE_CHARACTER + name + SqlHelperCons.DOUBLE_ESCAPE_CHARACTER;
    }

    public String name() {
        return name;
    }

    public String secName() {
        return SqlHelperCons.SINGLE_ESCAPE_CHARACTER + name + SqlHelperCons.SINGLE_ESCAPE_CHARACTER;
    }

    public String decName() {
        return SqlHelperCons.DOUBLE_ESCAPE_CHARACTER + name + SqlHelperCons.DOUBLE_ESCAPE_CHARACTER;
    }

    public String count() {
        return "COUNT(" + fullName() + ")";
    }

    public String avg() {
        return "AVG(" + fullName() + ")";
    }

    public String max() {
        return "MAX(" + fullName() + ")";
    }

    public String min() {
        return "MIN(" + fullName() + ")";
    }

    public String sum() {
        return "SUM(" + fullName() + ")";
    }

    @Override
    public String toString() {
        return fullName();
    }
}
