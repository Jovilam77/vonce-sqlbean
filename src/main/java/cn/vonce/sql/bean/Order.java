package cn.vonce.sql.bean;

import cn.vonce.sql.enumerate.SqlSort;

import java.io.Serializable;

/**
 * Order By
 */
public class Order extends SqlColumn implements Serializable {

    private static final long serialVersionUID = 1L;

    private SqlSort sqlSort = SqlSort.ASC;

    public Order() {
        super();
    }

    public Order(String name, SqlSort sqlSort) {
        this("", "", name, sqlSort);
    }

    public Order(SqlColumn sqlColumn, SqlSort sqlSort) {
        this(sqlColumn.getSchema(), sqlColumn.getTableAlias(), sqlColumn.getName(), sqlSort);
    }

    public Order(String schema, String tableAlias, String name, SqlSort sqlSort) {
        super(schema, tableAlias, name);
        this.sqlSort = sqlSort;
    }

    public SqlSort getSqlSort() {
        return sqlSort;
    }

    public void setSqlSort(SqlSort sqlSort) {
        this.sqlSort = sqlSort;
    }

    @Override
    public String toString() {
        return "Order{" +
                "sqlSort=" + sqlSort +
                '}';
    }
}
