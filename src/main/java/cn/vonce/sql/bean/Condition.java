package cn.vonce.sql.bean;

import cn.vonce.sql.enumerate.SqlLogic;
import cn.vonce.sql.enumerate.SqlOperator;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.ListMultimap;

/**
 * 条件
 *
 * @author Jovi
 * @version 1.0
 * @email 766255988@qq.com
 * @date 2020年3月1日上午10:00:10
 */
public class Condition extends Common {

    private String where = "";//条件
    private Object[] agrs = null;
    private ListMultimap<String, SqlCondition> whereMap = LinkedListMultimap.create();//where条件包含的逻辑

    /**
     * 获取where sql 内容
     *
     * @return
     */
    public String getWhere() {
        return where;
    }

    /**
     * 设置where sql 内容
     *
     * @param where
     */
    public void setWhere(String where) {
        this.where = where;
    }

    /**
     * 设置where sql 内容
     *
     * @param where
     * @param args
     */
    public void setWhere(String where, Object... args) {
        this.where = where;
        this.agrs = args;
    }

    /**
     * 获取where参数
     *
     * @return
     */
    public Object[] getAgrs() {
        return agrs;
    }

    /**
     * 设置where参数
     *
     * @param agrs
     */
    public void setAgrs(Object[] agrs) {
        this.agrs = agrs;
    }

    /**
     * 获取where条件map
     *
     * @return
     */
    public ListMultimap<String, SqlCondition> getWhereMap() {
        return whereMap;
    }

    /**
     * 添加where条件
     *
     * @param field 字段
     * @param value 字段值
     */
    public Condition where(String field, Object value) {
        return where(field, value, SqlOperator.EQUAL_TO);
    }

    /**
     * 添加where条件
     *
     * @param column 字段信息
     * @param value      字段值
     * @author Jovi
     */
    public Condition where(Column column, Object value) {
        return where(column.getTableAlias(), column.name(), value, SqlOperator.EQUAL_TO);
    }


    /**
     * 添加where条件
     *
     * @param field       字段
     * @param value       字段值
     * @param sqlOperator 操作符
     * @return
     */
    public Condition where(String field, Object value, SqlOperator sqlOperator) {
        return where(SqlLogic.AND, "", field, value, sqlOperator);
    }

    /**
     * @param sqlLogic   该条件与下一条件之间的逻辑关系
     * @param tableAlias 表别名
     * @param field      字段
     * @param value      字段值
     * @return
     */
    public Condition where(SqlLogic sqlLogic, String tableAlias, String field, Object value) {
        return where(sqlLogic, tableAlias, field, value, SqlOperator.EQUAL_TO);
    }

    /**
     * 添加where条件
     *
     * @param tableAlias  表别名
     * @param field       字段
     * @param value       字段值
     * @param sqlOperator 操作符
     * @return
     */
    public Condition where(String tableAlias, String field, Object value, SqlOperator sqlOperator) {
        return where(SqlLogic.AND, tableAlias, field, value, sqlOperator);
    }

    /**
     * 添加where条件
     *
     * @param column  字段信息
     * @param value       字段值
     * @param sqlOperator 操作符
     * @return
     * @author Jovi
     */
    public Condition where(Column column, Object value, SqlOperator sqlOperator) {
        return where(SqlLogic.AND, column.getTableAlias(), column.name(), value, sqlOperator);
    }

    /**
     * 添加where条件
     *
     * @param sqlLogic    该条件与下一条件之间的逻辑关系
     * @param column  字段信息
     * @param value       字段值
     * @param sqlOperator 操作符
     * @return
     * @author Jovi
     */
    public Condition where(SqlLogic sqlLogic, Column column, Object value, SqlOperator sqlOperator) {
        return where(sqlLogic, column.getTableAlias(), column.name(), value, sqlOperator);
    }

    /**
     * 添加where条件
     *
     * @param sqlLogic    该条件与下一条件之间的逻辑关系
     * @param tableAlias  表别名
     * @param field       字段
     * @param value       字段值
     * @param sqlOperator 操作符
     * @return
     */
    public Condition where(SqlLogic sqlLogic, String tableAlias, String field, Object value, SqlOperator sqlOperator) {
        whereMap.put(tableAlias + field, new SqlCondition(sqlLogic, tableAlias, field, value, sqlOperator));
        return this;
    }


    /**
     * where 条件 and 方法
     *
     * @param field
     * @param value
     * @return
     */
    public Condition wAND(String field, Object value) {
        return wAND("", field, value, SqlOperator.EQUAL_TO);
    }

    /**
     * where 条件 and 方法
     *
     * @param value
     * @return
     * @author Jovi
     */
    public Condition wAND(Column column, Object value) {
        return wAND(column.getTableAlias(), column.name(), value, SqlOperator.EQUAL_TO);
    }

    /**
     * where 条件 and 方法
     *
     * @param field
     * @param value
     * @param sqlOperator
     * @return
     */
    public Condition wAND(String field, Object value, SqlOperator sqlOperator) {
        return wAND("", field, value, sqlOperator);
    }

    /**
     * where 条件 and 方法
     *
     * @param column
     * @param value
     * @param sqlOperator
     * @return
     * @author Jovi
     */
    public Condition wAND(Column column, Object value, SqlOperator sqlOperator) {
        return wAND(column.getTableAlias(), column.name(), value, sqlOperator);
    }

    /**
     * where 条件 and 方法
     *
     * @param field
     * @param value
     * @param sqlOperator
     * @return
     */
    public Condition wAND(String tableAlias, String field, Object value, SqlOperator sqlOperator) {
        return where(SqlLogic.AND, tableAlias, field, value, sqlOperator);
    }

    /**
     * where 条件 or 方法
     *
     * @param field
     * @param value
     * @return
     */
    public Condition wOR(String field, Object value) {
        return wOR(field, value, SqlOperator.EQUAL_TO);
    }

    /**
     * where 条件 or 方法
     *
     * @param column
     * @param value
     * @return
     * @author Jovi
     */
    public Condition wOR(Column column, Object value) {
        return wOR(column.getTableAlias(), column.name(), value, SqlOperator.EQUAL_TO);
    }

    /**
     * where 条件 or 方法
     *
     * @param field
     * @param value
     * @param sqlOperator
     * @return
     */
    public Condition wOR(String field, Object value, SqlOperator sqlOperator) {
        return wOR("", field, value, sqlOperator);
    }

    /**
     * where 条件 or 方法
     *
     * @param column
     * @param value
     * @param sqlOperator
     * @return
     */
    public Condition wOR(Column column, Object value, SqlOperator sqlOperator) {
        return wOR(column.getTableAlias(), column.name(), value, sqlOperator);
    }

    /**
     * where 条件 or 方法
     *
     * @param tableAlias
     * @param field
     * @param value
     * @param sqlOperator
     * @return
     */
    public Condition wOR(String tableAlias, String field, Object value, SqlOperator sqlOperator) {
        return where(SqlLogic.OR, tableAlias, field, value, sqlOperator);
    }

    /**
     * where 条件 andBracket 方法
     *
     * @param field
     * @param value
     * @return
     */
    public Condition wANDBracket(String field, Object value) {
        return wANDBracket("", field, value, SqlOperator.EQUAL_TO);
    }

    /**
     * where 条件 andBracket 方法
     *
     * @param column
     * @param value
     * @return
     * @author Jovi
     */
    public Condition wANDBracket(Column column, Object value) {
        return wANDBracket(column.getTableAlias(), column.name(), value, SqlOperator.EQUAL_TO);
    }

    /**
     * where 条件 andBracket 方法
     *
     * @param field
     * @param value
     * @param sqlOperator
     * @return
     */
    public Condition wANDBracket(String field, Object value, SqlOperator sqlOperator) {
        return wANDBracket("", field, value, sqlOperator);
    }

    /**
     * where 条件 andBracket 方法
     *
     * @param column
     * @param value
     * @param sqlOperator
     * @return
     */
    public Condition wANDBracket(Column column, Object value, SqlOperator sqlOperator) {
        return wANDBracket(column.getTableAlias(), column.name(), value, sqlOperator);
    }

    /**
     * where 条件 andBracket 方法
     *
     * @param tableAlias
     * @param field
     * @param value
     * @param sqlOperator
     * @return
     */
    public Condition wANDBracket(String tableAlias, String field, Object value, SqlOperator sqlOperator) {
        return where(SqlLogic.ANDBracket, tableAlias, field, value, sqlOperator);
    }

    /**
     * where 条件 orBracket 方法
     *
     * @param field
     * @param value
     * @return
     */
    public Condition wORBracket(String field, Object value) {
        return wORBracket("", field, value, SqlOperator.EQUAL_TO);
    }

    /**
     * where 条件 orBracket 方法
     *
     * @param column
     * @param value
     * @return
     * @author Jovi
     */
    public Condition wORBracket(Column column, Object value) {
        return wORBracket(column.getTableAlias(), column.name(), value, SqlOperator.EQUAL_TO);
    }

    /**
     * where 条件 orBracket 方法
     *
     * @param field
     * @param value
     * @param sqlOperator
     * @return
     */
    public Condition wORBracket(String field, Object value, SqlOperator sqlOperator) {
        return wORBracket("", field, value, sqlOperator);
    }

    /**
     * where 条件 orBracket 方法
     *
     * @param column
     * @param value
     * @param sqlOperator
     * @return
     */
    public Condition wORBracket(Column column, Object value, SqlOperator sqlOperator) {
        return wORBracket(column.getTableAlias(), column.name(), value, sqlOperator);
    }

    /**
     * where 条件 orBracket 方法
     *
     * @param tableAlias
     * @param field
     * @param value
     * @param sqlOperator
     * @return
     */
    public Condition wORBracket(String tableAlias, String field, Object value, SqlOperator sqlOperator) {
        return where(SqlLogic.ORBracket, tableAlias, field, value, sqlOperator);
    }

}
