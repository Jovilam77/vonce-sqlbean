package cn.vonce.sql.bean;

import cn.vonce.sql.enumerate.SqlLogic;
import cn.vonce.sql.enumerate.SqlOperator;
import cn.vonce.sql.uitls.SqlBeanUtil;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.ListMultimap;

import java.io.Serializable;

/**
 * Common
 *
 * @author Jovi
 * @version 1.0
 * @email 766255988@qq.com
 * @date 2017年8月18日上午9:00:19
 */
public abstract class Common implements Serializable {

    private static final long serialVersionUID = 1L;

    private String where = null;//条件
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
        this.where = SqlBeanUtil.getWhere(where, args);
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
    public Common where(String field, Object value) {
        if (field != null && value != null) {
            where(SqlLogic.AND, field, value);
        }
        return this;
    }

    /**
     * 添加where条件
     *
     * @param field    字段
     * @param value    字段值
     * @param operator 操作符
     * @author Jovi
     * @date 2017年8月18日上午8:53:13
     */
    public Common where(String field, Object value, String operator) {
        if (field != null && value != null) {
            where(SqlLogic.AND, field, value, operator);
        }
        return this;
    }

    /**
     * @param sqlLogic 该条件与下一条件之间的逻辑关系
     * @param field    字段
     * @param value    字段值
     * @return
     * @author Jovi
     * @date 2017年8月18日下午4:08:28
     * @author Jovi
     * @date 2017年8月18日上午8:53:13
     */
    public Common where(SqlLogic sqlLogic, String field, Object value) {
        return where(sqlLogic, field, value, "");
    }

    /**
     * @param sqlLogic 该条件与下一条件之间的逻辑关系
     * @param field    字段
     * @param value    字段值
     * @param operator 操作符
     * @return
     * @author Jovi
     * @date 2017年8月18日下午4:08:28
     * @author Jovi
     * @date 2017年8月18日上午8:53:13
     */
    public Common where(SqlLogic sqlLogic, String field, Object value, String operator) {
        if (field != null && value != null) {
            whereMap.put(replace(field), new SqlCondition(sqlLogic, field, value, operator));
        }
        return this;
    }

    /**
     * 添加where条件
     *
     * @param field       字段
     * @param value       字段值
     * @param sqlOperator 操作符
     * @return
     * @author Jovi
     * @date 2017年8月30日上午11:37:56
     */
    public Common where(String field, Object value, SqlOperator sqlOperator) {
        if (field != null && value != null) {
            where(SqlLogic.AND, field, value, sqlOperator);
        }
        return this;
    }

    /**
     * @param field       字段
     * @param value       字段值
     * @param sqlOperator 操作符
     * @param sqlLogic
     * @return
     * @author Jovi
     * @date 2017年8月30日上午11:43:15
     */
    public Common where(SqlLogic sqlLogic, String field, Object value, SqlOperator sqlOperator) {
        if (field != null && value != null) {
            whereMap.put(replace(field), new SqlCondition(sqlLogic, field, value, sqlOperator));
        }
        return this;
    }

    /**
     * 将“.”替换为“*”
     *
     * @param field
     * @return
     */
    private String replace(String field) {
        return field.replaceAll("\\.", "*");
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
    public Common wAND(String field, Object value) {
        where(SqlLogic.AND, field, value, SqlOperator.EQUAL_TO);
        return this;
    }

    /**
     * where 条件 and 方法
     *
     * @param field
     * @param value
     * @param operator
     * @return
     * @author Jovi
     * @date 2018年4月16日上午11:34:00
     */
    public Common wAND(String field, Object value, String operator) {
        where(SqlLogic.AND, field, value, operator);
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
    public Common wAND(String field, Object value, SqlOperator sqlOperator) {
        where(SqlLogic.AND, field, value, sqlOperator);
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
    public Common wOR(String field, Object value) {
        where(SqlLogic.OR, field, value, SqlOperator.EQUAL_TO);
        return this;
    }

    /**
     * where 条件 or 方法
     *
     * @param field
     * @param value
     * @param operator
     * @return
     * @author Jovi
     * @date 2018年4月16日上午11:34:37
     */
    public Common wOR(String field, Object value, String operator) {
        where(SqlLogic.OR, field, value, operator);
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
    public Common wOR(String field, Object value, SqlOperator sqlOperator) {
        where(SqlLogic.OR, field, value, sqlOperator);
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
    public Common wANDBracket(String field, Object value) {
        where(SqlLogic.ANDBracket, field, value, SqlOperator.EQUAL_TO);
        return this;
    }

    /**
     * where 条件 andBracket 方法
     *
     * @param field
     * @param value
     * @param operator
     * @return
     * @author Jovi
     * @date 2018年4月16日上午11:35:17
     */
    public Common wANDBracket(String field, Object value, String operator) {
        where(SqlLogic.ANDBracket, field, value, operator);
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
    public Common wANDBracket(String field, Object value, SqlOperator sqlOperator) {
        where(SqlLogic.ANDBracket, field, value, sqlOperator);
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
    public Common wORBracket(String field, Object value) {
        where(SqlLogic.ORBracket, field, value, SqlOperator.EQUAL_TO);
        return this;
    }

    /**
     * where 条件 orBracket 方法
     *
     * @param field
     * @param value
     * @param operator
     * @return
     * @author Jovi
     * @date 2018年4月16日上午11:34:57
     */
    public Common wORBracket(String field, Object value, String operator) {
        where(SqlLogic.ORBracket, field, value, operator);
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
    public Common wORBracket(String field, Object value, SqlOperator sqlOperator) {
        where(SqlLogic.ORBracket, field, value, sqlOperator);
        return this;
    }

}
