package cn.vonce.sql.bean;

import cn.vonce.sql.enumerate.SqlLogic;
import cn.vonce.sql.enumerate.SqlOperator;

import java.io.Serializable;

/**
 * Sql where条件形态
 *
 * @author Jovi
 * @version 1.0
 * @email imjovi@qq.com
 * @date 2018年4月16日下午7:15:10
 */
public class ConditionInfo implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public ConditionInfo() {
        super();
    }

    public ConditionInfo(SqlLogic sqlLogic, String tableAlias, String columnName, Object value, SqlOperator sqlOperator) {
        this.sqlLogic = sqlLogic;
        this.column = new Column(tableAlias, columnName, null);
        this.value = value;
        this.sqlOperator = sqlOperator;
    }

    public ConditionInfo(SqlLogic sqlLogic, Column column, Object value, SqlOperator sqlOperator) {
        this.sqlLogic = sqlLogic;
        this.column = column;
        this.value = value;
        this.sqlOperator = sqlOperator;
    }

    private SqlLogic sqlLogic;
    private Column column;
    private Object value;
    private SqlOperator sqlOperator;

    public SqlLogic getSqlLogic() {
        return sqlLogic;
    }

    public void setSqlLogic(SqlLogic sqlLogic) {
        this.sqlLogic = sqlLogic;
    }

    public Column getColumn() {
        return column;
    }

    public void setColumn(Column column) {
        this.column = column;
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

}
