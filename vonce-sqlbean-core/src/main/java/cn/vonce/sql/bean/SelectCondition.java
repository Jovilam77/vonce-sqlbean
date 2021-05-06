package cn.vonce.sql.bean;


import cn.vonce.sql.enumerate.SqlLogic;
import cn.vonce.sql.enumerate.SqlOperator;
import cn.vonce.sql.helper.Wrapper;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.ListMultimap;

/**
 * select条件（包含where和having）
 */
public class SelectCondition extends Condition {

    private String having = null;
    private Object[] havingArgs = null;
    private ListMultimap<String, ConditionInfo> havingMap = LinkedListMultimap.create();//having条件包含的逻辑
    private Wrapper havingWrapper = new Wrapper();


    /**
     * 获取where sql 内容
     *
     * @return
     */
    public String getHaving() {
        return having;
    }

    /**
     * 设置having sql 内容
     *
     * @param having
     */
    public void setHaving(String having) {
        this.having = having;
    }

    /**
     * 设置having sql 内容
     *
     * @param having
     * @param args
     */
    public void setHaving(String having, Object... args) {
        this.having = having;
        this.havingArgs = args;
    }

    /**
     * 获取Having
     *
     * @return
     */
    public Object[] getHavingArgs() {
        return havingArgs;
    }

    /**
     * 设置Having
     *
     * @param havingArgs
     */
    public void setHavingArgs(Object[] havingArgs) {
        this.havingArgs = havingArgs;
    }

    /**
     * 获取having条件值映射
     *
     * @return
     */
    public ListMultimap<String, ConditionInfo> getHavingMap() {
        return havingMap;
    }

    /**
     * 添加having条件
     *
     * @param field 列字段
     * @param value 列字段值
     * @return
     */
    public SelectCondition having(String field, Object value) {
        return having(field, value, SqlOperator.EQUAL_TO);
    }

    /**
     * 添加having条件
     *
     * @param column 列字段信息
     * @param value  列字段值
     * @return
     */
    public SelectCondition having(Column column, Object value) {
        return having(SqlLogic.AND, column.getSchema(), column.getTableAlias(), column.name(), value, SqlOperator.EQUAL_TO);
    }


    /**
     * 添加having条件
     *
     * @param field       列字段
     * @param value       列字段值
     * @param sqlOperator 操作符
     * @return
     */
    public SelectCondition having(String field, Object value, SqlOperator sqlOperator) {
        return having(SqlLogic.AND, "", "", field, value, sqlOperator);
    }

    /**
     * @param sqlLogic   该条件与下一条件之间的逻辑关系
     * @param tableAlias 表别名
     * @param field      列字段
     * @param value      列字段值
     * @return
     */
    public SelectCondition having(SqlLogic sqlLogic, String tableAlias, String field, Object value) {
        return having(sqlLogic, "", tableAlias, field, value, SqlOperator.EQUAL_TO);
    }

    /**
     * 添加having条件
     *
     * @param tableAlias  表别名
     * @param field       列字段
     * @param value       列字段值
     * @param sqlOperator 操作符
     * @return
     */
    public SelectCondition having(String tableAlias, String field, Object value, SqlOperator sqlOperator) {
        return having(SqlLogic.AND, "", tableAlias, field, value, sqlOperator);
    }

    /**
     * 添加having条件
     *
     * @param column      列字段信息
     * @param value       列字段值
     * @param sqlOperator 操作符
     * @return
     */
    public SelectCondition having(Column column, Object value, SqlOperator sqlOperator) {
        return having(SqlLogic.AND, column.getSchema(), column.getTableAlias(), column.name(), value, sqlOperator);
    }

    /**
     * 添加having条件
     *
     * @param sqlLogic    该条件与下一条件之间的逻辑关系
     * @param column      列字段信息
     * @param value       列字段值
     * @param sqlOperator 操作符
     * @return
     */
    public SelectCondition having(SqlLogic sqlLogic, Column column, Object value, SqlOperator sqlOperator) {
        return having(sqlLogic, column.getSchema(), column.getTableAlias(), column.name(), value, sqlOperator);
    }

    /**
     * 添加having条件
     *
     * @param sqlLogic    该条件与下一条件之间的逻辑关系
     * @param schema      schema
     * @param tableAlias  表别名
     * @param field       列字段
     * @param value       列字段值
     * @param sqlOperator 操作符
     * @return
     */
    public SelectCondition having(SqlLogic sqlLogic, String schema, String tableAlias, String field, Object value, SqlOperator sqlOperator) {
        havingMap.put(tableAlias + field, new ConditionInfo(sqlLogic, schema, tableAlias, field, value, sqlOperator));
        return this;
    }

    /**
     * 获得Having包装器
     *
     * @return
     */
    public Wrapper getHavingWrapper() {
        return havingWrapper;
    }

    /**
     * 设置Having条件包装器
     *
     * @param wrapper
     */
    public void setHaving(Wrapper wrapper) {
        this.havingWrapper = wrapper;
    }

}
