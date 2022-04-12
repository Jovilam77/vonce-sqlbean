package cn.vonce.sql.bean;

import cn.vonce.sql.constant.SqlConstant;
import cn.vonce.sql.enumerate.SqlLogic;
import cn.vonce.sql.enumerate.SqlOperator;
import cn.vonce.sql.helper.SqlHelper;
import cn.vonce.sql.helper.Wrapper;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.ListMultimap;

/**
 * where条件
 *
 * @author Jovi
 * @version 1.0
 * @email 766255988@qq.com
 * @date 2020年3月1日上午10:00:10
 */
public class CommonCondition<T> extends Common {

    /**
     * 链式返回对象
     */
    private T returnObj;
    /**
     * where 条件表达式 优先级一
     */
    private String where = "";
    /**
     * where 条件表达式参数
     */
    private Object[] agrs = null;
    /**
     * where 条件包装器 优先级二
     */
    private Wrapper whereWrapper;
    /**
     * where 条件 优先级三
     */
    private Condition<T> whereCondition;
    /**
     * where 条件，过时，未来版本将移除
     */
    @Deprecated
    private ListMultimap<String, ConditionInfo> whereMap = LinkedListMultimap.create();

    protected void setReturnObj(T returnObj) {
        this.returnObj = returnObj;
        whereCondition = new Condition<>(returnObj);
    }

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
    @Deprecated
    public ListMultimap<String, ConditionInfo> getWhereMap() {
        return whereMap;
    }

    /**
     * 添加where条件
     *
     * @param field 字段
     * @param value 字段值
     */
    @Deprecated
    public CommonCondition where(String field, Object value) {
        return where(field, value, SqlOperator.EQUAL_TO);
    }

    /**
     * 添加where条件
     *
     * @param column 字段信息
     * @param value  字段值
     * @author Jovi
     */
    @Deprecated
    public CommonCondition where(Column column, Object value) {
        return where(SqlLogic.AND, column.getTableAlias(), column.getName(), value, SqlOperator.EQUAL_TO);
    }


    /**
     * 添加where条件
     *
     * @param field       字段
     * @param value       字段值
     * @param sqlOperator 操作符
     * @return
     */
    @Deprecated
    public CommonCondition where(String field, Object value, SqlOperator sqlOperator) {
        return where(SqlLogic.AND, "", field, value, sqlOperator);
    }

    /**
     * @param sqlLogic   该条件与下一条件之间的逻辑关系
     * @param tableAlias 表别名
     * @param field      字段
     * @param value      字段值
     * @return
     */
    @Deprecated
    public CommonCondition where(SqlLogic sqlLogic, String tableAlias, String field, Object value) {
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
    @Deprecated
    public CommonCondition where(String tableAlias, String field, Object value, SqlOperator sqlOperator) {
        return where(SqlLogic.AND, tableAlias, field, value, sqlOperator);
    }

    /**
     * 添加where条件
     *
     * @param column      字段信息
     * @param value       字段值
     * @param sqlOperator 操作符
     * @return
     * @author Jovi
     */
    @Deprecated
    public CommonCondition where(Column column, Object value, SqlOperator sqlOperator) {
        return where(SqlLogic.AND, column.getTableAlias(), column.getName(), value, sqlOperator);
    }

    /**
     * 添加where条件
     *
     * @param sqlLogic    该条件与下一条件之间的逻辑关系
     * @param column      字段信息
     * @param value       字段值
     * @param sqlOperator 操作符
     * @return
     * @author Jovi
     */
    @Deprecated
    public CommonCondition where(SqlLogic sqlLogic, Column column, Object value, SqlOperator sqlOperator) {
        return where(sqlLogic, column.getTableAlias(), column.getName(), value, sqlOperator);
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
    @Deprecated
    public CommonCondition where(SqlLogic sqlLogic, String tableAlias, String field, Object value, SqlOperator sqlOperator) {
        if (value instanceof Select) {
            if (sqlOperator == SqlOperator.IN || sqlOperator == SqlOperator.NOT_IN) {
                value = new Original(SqlHelper.buildSelectSql((Select) value));
            } else {
                value = new Original(SqlConstant.BEGIN_BRACKET + SqlHelper.buildSelectSql((Select) value) + SqlConstant.END_BRACKET);
            }
        }
        whereMap.put(tableAlias + field, new ConditionInfo(sqlLogic, tableAlias, field, value, sqlOperator));
        return this;
    }


    /**
     * where 条件 and 方法
     *
     * @param field
     * @param value
     * @return
     */
    @Deprecated
    public CommonCondition wAND(String field, Object value) {
        return wAND(field, value, SqlOperator.EQUAL_TO);
    }

    /**
     * where 条件 and 方法
     *
     * @param value
     * @return
     * @author Jovi
     */
    @Deprecated
    public CommonCondition wAND(Column column, Object value) {
        return wAND(column.getTableAlias(), column.getName(), value, SqlOperator.EQUAL_TO);
    }

    /**
     * where 条件 and 方法
     *
     * @param field
     * @param value
     * @param sqlOperator
     * @return
     */
    @Deprecated
    public CommonCondition wAND(String field, Object value, SqlOperator sqlOperator) {
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
    @Deprecated
    public CommonCondition wAND(Column column, Object value, SqlOperator sqlOperator) {
        return wAND(column.getTableAlias(), column.getName(), value, sqlOperator);
    }

    /**
     * where 条件 and 方法
     *
     * @param tableAlias
     * @param field
     * @param value
     * @param sqlOperator
     * @return
     */
    @Deprecated
    public CommonCondition wAND(String tableAlias, String field, Object value, SqlOperator sqlOperator) {
        return where(SqlLogic.AND, tableAlias, field, value, sqlOperator);
    }

    /**
     * where 条件 or 方法
     *
     * @param field
     * @param value
     * @return
     */
    @Deprecated
    public CommonCondition wOR(String field, Object value) {
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
    @Deprecated
    public CommonCondition wOR(Column column, Object value) {
        return wOR(column.getTableAlias(), column.getName(), value, SqlOperator.EQUAL_TO);
    }

    /**
     * where 条件 or 方法
     *
     * @param field
     * @param value
     * @param sqlOperator
     * @return
     */
    @Deprecated
    public CommonCondition wOR(String field, Object value, SqlOperator sqlOperator) {
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
    @Deprecated
    public CommonCondition wOR(Column column, Object value, SqlOperator sqlOperator) {
        return wOR(column.getTableAlias(), column.getName(), value, sqlOperator);
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
    @Deprecated
    public CommonCondition wOR(String tableAlias, String field, Object value, SqlOperator sqlOperator) {
        return where(SqlLogic.OR, tableAlias, field, value, sqlOperator);
    }

    /**
     * where 条件 andBracket 方法
     *
     * @param field
     * @param value
     * @return
     */
    @Deprecated
    public CommonCondition wANDBracket(String field, Object value) {
        return wANDBracket(field, value, SqlOperator.EQUAL_TO);
    }

    /**
     * where 条件 andBracket 方法
     *
     * @param column
     * @param value
     * @return
     * @author Jovi
     */
    @Deprecated
    public CommonCondition wANDBracket(Column column, Object value) {
        return wANDBracket(column.getTableAlias(), column.getName(), value, SqlOperator.EQUAL_TO);
    }

    /**
     * where 条件 andBracket 方法
     *
     * @param field
     * @param value
     * @param sqlOperator
     * @return
     */
    @Deprecated
    public CommonCondition wANDBracket(String field, Object value, SqlOperator sqlOperator) {
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
    @Deprecated
    public CommonCondition wANDBracket(Column column, Object value, SqlOperator sqlOperator) {
        return wANDBracket(column.getTableAlias(), column.getName(), value, sqlOperator);
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
    @Deprecated
    public CommonCondition wANDBracket(String tableAlias, String field, Object value, SqlOperator sqlOperator) {
        return where(SqlLogic.ANDBracket, tableAlias, field, value, sqlOperator);
    }

    /**
     * where 条件 orBracket 方法
     *
     * @param field
     * @param value
     * @return
     */
    @Deprecated
    public CommonCondition wORBracket(String field, Object value) {
        return wORBracket(field, value, SqlOperator.EQUAL_TO);
    }

    /**
     * where 条件 orBracket 方法
     *
     * @param column
     * @param value
     * @return
     * @author Jovi
     */
    @Deprecated
    public CommonCondition wORBracket(Column column, Object value) {
        return wORBracket(column.getTableAlias(), column.getName(), value, SqlOperator.EQUAL_TO);
    }

    /**
     * where 条件 orBracket 方法
     *
     * @param field
     * @param value
     * @param sqlOperator
     * @return
     */
    @Deprecated
    public CommonCondition wORBracket(String field, Object value, SqlOperator sqlOperator) {
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
    @Deprecated
    public CommonCondition wORBracket(Column column, Object value, SqlOperator sqlOperator) {
        return wORBracket(column.getTableAlias(), column.getName(), value, sqlOperator);
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
    @Deprecated
    public CommonCondition wORBracket(String tableAlias, String field, Object value, SqlOperator sqlOperator) {
        return where(SqlLogic.ORBracket, tableAlias, field, value, sqlOperator);
    }

    public Condition<T> where() {
        return whereCondition;
    }

    /**
     * 获得where包装器
     *
     * @return
     */
    public Wrapper getWhereWrapper() {
        return whereWrapper;
    }

    /**
     * 设置Where条件包装器
     *
     * @param wrapper
     */
    public T setWhere(Wrapper wrapper) {
        this.whereWrapper = wrapper;
        return returnObj;
    }

}
