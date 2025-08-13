package cn.vonce.sql.bean;

import cn.vonce.sql.enumerate.SqlSort;
import java.io.Serializable;

/**
 * Order By
 */
public class Order extends Column implements Serializable {

    private static final long serialVersionUID = 1L;

    private Column column;
    private SqlSort sqlSort = SqlSort.ASC;

    public Order() {
        super();
    }

    public Order(String name, SqlSort sqlSort) {
        this("", name, sqlSort);
    }

    public Order(Column column, SqlSort sqlSort) {
        this.column = column;
        this.sqlSort = sqlSort;
    }

    public Order(String tableAlias, String name, SqlSort sqlSort) {
        column = new Column(tableAlias, name, "");
        this.sqlSort = sqlSort;
    }

    public Column getColumn() {
        return column;
    }

    public void setColumn(Column column) {
        this.column = column;
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
