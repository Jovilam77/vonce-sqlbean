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
     * @author Jovi
     * @date 2017年8月18日上午8:58:33
     */
    public String getWhere() {
        return where;
    }

    /**
     * 设置where sql 内容
     *
     * @param where
     * @author Jovi
     * @date 2017年8月18日上午8:58:11
     */
    public void setWhere(String where) {
        this.where = where;
    }

    /**
     * 设置where sql 内容
     *
     * @param where
     * @param args
     * @author Jovi
     * @date 2018年9月13日下午15:34:45
     */
    public void setWhere(String where, Object... args) {
//        this.where = SqlBeanUtil.getCondition(this, args);
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
     * @author Jovi
     * @date 2017年8月18日上午8:54:32
     */
    public ListMultimap<String, SqlCondition> getWhereMap() {
        return whereMap;
    }

    /**
     * 添加where条件
     *
     * @param field 字段
     * @param value 字段值
     * @author Jovi
     * @date 2017年8月18日上午8:53:11
     */
    public Condition where(String field, Object value) {
        return where("", field, value);
    }

    /**
     * 添加where条件
     *
     * @param tableAlias 表别名
     * @param field      字段
     * @param value      字段值
     */
    public Condition where(String tableAlias, String field, Object value) {
        return where(SqlLogic.AND, tableAlias, field, value);
    }

    /**
     * @param sqlLogic   该条件与下一条件之间的逻辑关系
     * @param tableAlias 表别名
     * @param field      字段
     * @param value      字段值
     * @return
     * @author Jovi
     * @date 2017年8月18日下午4:08:28
     * @author Jovi
     * @date 2017年8月18日上午8:53:13
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
     * @author Jovi
     * @date 2017年8月30日上午11:37:56
     */
    public Condition where(String tableAlias, String field, Object value, SqlOperator sqlOperator) {
        return where(SqlLogic.AND, tableAlias, field, value, sqlOperator);
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
     * @author Jovi
     * @date 2017年8月30日上午11:43:15
     */
    public Condition where(SqlLogic sqlLogic, String tableAlias, String field, Object value, SqlOperator sqlOperator) {
        if (field != null && value != null) {
            whereMap.put(tableAlias + field, new SqlCondition(sqlLogic, tableAlias, field, value, sqlOperator));
        }
        return this;
    }

    /**
     * where 条件 and 方法
     *
     * @param field
     * @param value
     * @return
     * @author Jovi
     * @date 2018年4月16日上午11:33:56
     */
    public Condition wAND(String field, Object value) {
        where(SqlLogic.AND, "", field, value, SqlOperator.EQUAL_TO);
        return this;
    }

    /**
     * where 条件 and 方法
     *
     * @param tableAlias
     * @param field
     * @param value
     * @return
     * @author Jovi
     * @date 2018年4月16日上午11:33:56
     */
    public Condition wAND(String tableAlias, String field, Object value) {
        where(SqlLogic.AND, tableAlias, field, value, SqlOperator.EQUAL_TO);
        return this;
    }

    /**
     * where 条件 and 方法
     *
     * @param field
     * @param value
     * @param sqlOperator
     * @return
     * @author Jovi
     * @date 2018年4月16日上午11:34:03
     */
    public Condition wAND(String field, Object value, SqlOperator sqlOperator) {
        where(SqlLogic.AND, "", field, value, sqlOperator);
        return this;
    }

    /**
     * where 条件 and 方法
     *
     * @param field
     * @param value
     * @param sqlOperator
     * @return
     * @author Jovi
     * @date 2018年4月16日上午11:34:03
     */
    public Condition wAND(String tableAlias, String field, Object value, SqlOperator sqlOperator) {
        where(SqlLogic.AND, tableAlias, field, value, sqlOperator);
        return this;
    }

    /**
     * where 条件 or 方法
     *
     * @param field
     * @param value
     * @return
     * @author Jovi
     * @date 2018年4月16日上午11:34:29
     */
    public Condition wOR(String field, Object value) {
        where(SqlLogic.OR, "", field, value, SqlOperator.EQUAL_TO);
        return this;
    }

    /**
     * where 条件 or 方法
     *
     * @param tableAlias
     * @param field
     * @param value
     * @return
     */
    public Condition wOR(String tableAlias, String field, Object value) {
        where(SqlLogic.OR, tableAlias, field, value, SqlOperator.EQUAL_TO);
        return this;
    }

    /**
     * where 条件 or 方法
     *
     * @param field
     * @param value
     * @param sqlOperator
     * @return
     * @author Jovi
     * @date 2018年4月16日上午11:34:40
     */
    public Condition wOR(String field, Object value, SqlOperator sqlOperator) {
        where(SqlLogic.OR, "", field, value, sqlOperator);
        return this;
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
        where(SqlLogic.OR, tableAlias, field, value, sqlOperator);
        return this;
    }

    /**
     * where 条件 andBracket 方法
     *
     * @param field
     * @param value
     * @return
     * @author Jovi
     * @date 2018年4月16日上午11:35:03
     */
    public Condition wANDBracket(String field, Object value) {
        where(SqlLogic.ANDBracket, "", field, value, SqlOperator.EQUAL_TO);
        return this;
    }

    /**
     * where 条件 andBracket 方法
     *
     * @param tableAlias
     * @param field
     * @param value
     * @return
     */
    public Condition wANDBracket(String tableAlias, String field, Object value) {
        where(SqlLogic.ANDBracket, tableAlias, field, value, SqlOperator.EQUAL_TO);
        return this;
    }

    /**
     * where 条件 andBracket 方法
     *
     * @param field
     * @param value
     * @param sqlOperator
     * @return
     * @author Jovi
     * @date 2018年4月16日上午11:35:20
     */
    public Condition wANDBracket(String field, Object value, SqlOperator sqlOperator) {
        where(SqlLogic.ANDBracket, "", field, value, sqlOperator);
        return this;
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
        where(SqlLogic.ANDBracket, tableAlias, field, value, sqlOperator);
        return this;
    }

    /**
     * where 条件 orBracket 方法
     *
     * @param field
     * @param value
     * @return
     * @author Jovi
     * @date 2018年4月16日上午11:34:44
     */
    public Condition wORBracket(String field, Object value) {
        where(SqlLogic.ORBracket, "", field, value, SqlOperator.EQUAL_TO);
        return this;
    }

    /**
     * where 条件 orBracket 方法
     *
     * @param tableAlias
     * @param field
     * @param value
     * @return
     */
    public Condition wORBracket(String tableAlias, String field, Object value) {
        where(SqlLogic.ORBracket, tableAlias, field, value, SqlOperator.EQUAL_TO);
        return this;
    }

    /**
     * where 条件 orBracket 方法
     *
     * @param field
     * @param value
     * @param sqlOperator
     * @return
     * @author Jovi
     * @date 2018年4月16日上午11:35:00
     */
    public Condition wORBracket(String field, Object value, SqlOperator sqlOperator) {
        where(SqlLogic.ORBracket, "", field, value, sqlOperator);
        return this;
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
        where(SqlLogic.ORBracket, tableAlias, field, value, sqlOperator);
        return this;
    }

}
