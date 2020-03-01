package cn.vonce.sql.bean;

import cn.vonce.sql.enumerate.SqlLogic;
import cn.vonce.sql.enumerate.SqlOperator;

import java.io.Serializable;

/**
 * Sql where条件形态
 *
 * @author Jovi
 * @version 1.0
 * @email 766255988@qq.com
 * @date 2018年4月16日下午7:15:10
 */
public class SqlCondition implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public SqlCondition() {
        super();
    }

    public SqlCondition(String field, Object value) {
        super();
        this.tableAlias = "";
        this.field = field;
        this.value = value;
    }

    public SqlCondition(String tableAlias, String field, Object value) {
        super();
        this.field = field;
        this.value = value;
    }

    public SqlCondition(String tableAlias, String field, Object value, SqlOperator sqlOperator) {
        super();
        this.field = field;
        this.value = value;
        this.sqlOperator = sqlOperator;
    }

    public SqlCondition(SqlLogic sqlLogic, String tableAlias, String field, Object value, SqlOperator sqlOperator) {
        super();
        this.sqlLogic = sqlLogic;
        this.tableAlias = tableAlias;
        this.field = field;
        this.value = value;
        this.sqlOperator = sqlOperator;
    }

    private String tableAlias;
    private String field;
    private Object value;
    private SqlOperator sqlOperator;
    private SqlLogic sqlLogic;

    public String getTableAlias() {
        return tableAlias;
    }

    public void setTableAlias(String tableAlias) {
        this.tableAlias = tableAlias;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public SqlOperator getSqlOperator() {
        return sqlOperator;
    }

    public void setSqlOperator(SqlOperator sqlOperator) {
        this.sqlOperator = sqlOperator;
    }

    public SqlLogic getSqlLogic() {
        return sqlLogic;
    }

    public void setSqlLogic(SqlLogic sqlLogic) {
        this.sqlLogic = sqlLogic;
    }

}
