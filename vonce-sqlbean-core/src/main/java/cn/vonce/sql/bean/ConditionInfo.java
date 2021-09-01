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
public class ConditionInfo extends Column implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public ConditionInfo() {
        super();
    }

    public ConditionInfo(String columnName, Object value) {
        this(null, "", columnName, value, null);
    }

    public ConditionInfo(String tableAlias, String columnName, Object value) {
        this(null, tableAlias, columnName, value, null);
    }

    public ConditionInfo(String tableAlias, String columnName, Object value, SqlOperator sqlOperator) {
        this(null, tableAlias, columnName, value, sqlOperator);
    }

    public ConditionInfo(SqlLogic sqlLogic, String tableAlias, String columnName, Object value, SqlOperator sqlOperator) {
        super(tableAlias, columnName, "");
        this.sqlLogic = sqlLogic;
        this.value = value;
        this.sqlOperator = sqlOperator;
    }

    private SqlLogic sqlLogic;
    private Object value;
    private SqlOperator sqlOperator;


    public Object getValue() {
        return value;
    }

    private void setValue(Object value) {
        this.value = value;
    }

    public SqlOperator getSqlOperator() {
        return sqlOperator;
    }

    private void setSqlOperator(SqlOperator sqlOperator) {
        this.sqlOperator = sqlOperator;
    }

    public SqlLogic getSqlLogic() {
        return sqlLogic;
    }

    private void setSqlLogic(SqlLogic sqlLogic) {
        this.sqlLogic = sqlLogic;
    }

}
