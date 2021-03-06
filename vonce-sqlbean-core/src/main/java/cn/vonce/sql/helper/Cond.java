package cn.vonce.sql.helper;

import cn.vonce.sql.bean.ConditionInfo;
import cn.vonce.sql.bean.SqlField;
import cn.vonce.sql.enumerate.SqlOperator;

/**
 * 条件包装器内容
 *
 * @author Jovi
 * @version 1.0
 * @email 766255988@qq.com
 * @date 2021年4月28日下午5:49:00
 */
public class Cond extends ConditionInfo {

    /**
     * 等于
     *
     * @param field
     * @param value
     * @return
     */
    public static Cond eq(String field, Object value) {
        return new Cond(field, SqlOperator.EQUAL_TO, value);
    }

    /**
     * 等于
     *
     * @param sqlField
     * @param value
     * @return
     */
    public static Cond eq(SqlField sqlField, Object value) {
        return new Cond(sqlField, SqlOperator.EQUAL_TO, value);
    }

    /**
     * 不等于
     *
     * @param field
     * @param value
     * @return
     */
    public static Cond notEq(String field, Object value) {
        return new Cond(field, SqlOperator.NOT_EQUAL_TO, value);
    }

    /**
     * 不等于
     *
     * @param sqlField
     * @param value
     * @return
     */
    public static Cond notEq(SqlField sqlField, Object value) {
        return new Cond(sqlField, SqlOperator.NOT_EQUAL_TO, value);
    }

    /**
     * 小于
     *
     * @param field
     * @param value
     * @return
     */
    public static Cond lt(String field, Object value) {
        return new Cond(field, SqlOperator.LESS_THAN, value);
    }

    /**
     * 小于
     *
     * @param sqlField
     * @param value
     * @return
     */
    public static Cond lt(SqlField sqlField, Object value) {
        return new Cond(sqlField, SqlOperator.LESS_THAN, value);
    }

    /**
     * 大于
     *
     * @param field
     * @param value
     * @return
     */
    public static Cond gt(String field, Object value) {
        return new Cond(field, SqlOperator.GREATER_THAN, value);
    }

    /**
     * 大于
     *
     * @param sqlField
     * @param value
     * @return
     */
    public static Cond gt(SqlField sqlField, Object value) {
        return new Cond(sqlField, SqlOperator.GREATER_THAN, value);
    }

    /**
     * 小于等于
     *
     * @param field
     * @param value
     * @return
     */
    public static Cond ltEq(String field, Object value) {
        return new Cond(field, SqlOperator.LESS_THAN_OR_EQUAL_TO, value);
    }

    /**
     * 小于等于
     *
     * @param sqlField
     * @param value
     * @return
     */
    public static Cond ltEq(SqlField sqlField, Object value) {
        return new Cond(sqlField, SqlOperator.LESS_THAN_OR_EQUAL_TO, value);
    }

    /**
     * 大于等于
     *
     * @param field
     * @param value
     * @return
     */
    public static Cond gtEq(String field, Object value) {
        return new Cond(field, SqlOperator.GREAT_THAN_OR_EQUAL_TO, value);
    }

    /**
     * 大于等于
     *
     * @param sqlField
     * @param value
     * @return
     */
    public static Cond gtEq(SqlField sqlField, Object value) {
        return new Cond(sqlField, SqlOperator.GREAT_THAN_OR_EQUAL_TO, value);
    }

    /**
     * 全模糊 包含
     *
     * @param field
     * @param value
     * @return
     */
    public static Cond like(String field, Object value) {
        return new Cond(field, SqlOperator.LIKE, value);
    }

    /**
     * 全模糊 包含
     *
     * @param sqlField
     * @param value
     * @return
     */
    public static Cond like(SqlField sqlField, Object value) {
        return new Cond(sqlField, SqlOperator.LIKE, value);
    }

    /**
     * 左模糊 包含
     *
     * @param field
     * @param value
     * @return
     */
    public static Cond likeL(String field, Object value) {
        return new Cond(field, SqlOperator.LIKE_L, value);
    }

    /**
     * 左模糊 包含
     *
     * @param sqlField
     * @param value
     * @return
     */
    public static Cond likeL(SqlField sqlField, Object value) {
        return new Cond(sqlField, SqlOperator.LIKE_L, value);
    }

    /**
     * 右模糊 包含
     *
     * @param field
     * @param value
     * @return
     */
    public static Cond likeR(String field, Object value) {
        return new Cond(field, SqlOperator.LIKE_R, value);
    }

    /**
     * 右模糊 包含
     *
     * @param sqlField
     * @param value
     * @return
     */
    public static Cond likeR(SqlField sqlField, Object value) {
        return new Cond(sqlField, SqlOperator.LIKE_R, value);
    }

    /**
     * 全模糊 不包含
     *
     * @param field
     * @param value
     * @return
     */
    public static Cond notLike(String field, Object value) {
        return new Cond(field, SqlOperator.NOT_LIKE, value);
    }

    /**
     * 全模糊 不包含
     *
     * @param sqlField
     * @param value
     * @return
     */
    public static Cond notLike(SqlField sqlField, Object value) {
        return new Cond(sqlField, SqlOperator.NOT_LIKE, value);
    }

    /**
     * 左模糊 不包含
     *
     * @param field
     * @param value
     * @return
     */
    public static Cond notLikeL(String field, Object value) {
        return new Cond(field, SqlOperator.NOT_LIKE_L, value);
    }

    /**
     * 左模糊 不包含
     *
     * @param sqlField
     * @param value
     * @return
     */
    public static Cond notLikeL(SqlField sqlField, Object value) {
        return new Cond(sqlField, SqlOperator.NOT_LIKE_L, value);
    }

    /**
     * 右模糊 不包含
     *
     * @param field
     * @param value
     * @return
     */
    public static Cond notLikeR(String field, Object value) {
        return new Cond(field, SqlOperator.NOT_LIKE_R, value);
    }

    /**
     * 右模糊 不包含
     *
     * @param sqlField
     * @param value
     * @return
     */
    public static Cond notLikeR(SqlField sqlField, Object value) {
        return new Cond(sqlField, SqlOperator.NOT_LIKE_R, value);
    }

    /**
     * 是
     *
     * @param field
     * @param value
     * @return
     */
    public static Cond is(String field, Object value) {
        return new Cond(field, SqlOperator.IS, value);
    }

    /**
     * 是
     *
     * @param sqlField
     * @param value
     * @return
     */
    public static Cond is(SqlField sqlField, Object value) {
        return new Cond(sqlField, SqlOperator.IS, value);
    }

    /**
     * 不是
     *
     * @param field
     * @param value
     * @return
     */
    public static Cond isNot(String field, Object value) {
        return new Cond(field, SqlOperator.IS_NOT, value);
    }

    /**
     * 不是
     *
     * @param sqlField
     * @param value
     * @return
     */
    public static Cond isNot(SqlField sqlField, Object value) {
        return new Cond(sqlField, SqlOperator.IS_NOT, value);
    }

    /**
     * 包含
     *
     * @param field
     * @param value
     * @return
     */
    public static Cond in(String field, Object value) {
        return new Cond(field, SqlOperator.IN, value);
    }

    /**
     * 包含
     *
     * @param sqlField
     * @param value
     * @return
     */
    public static Cond in(SqlField sqlField, Object value) {
        return new Cond(sqlField, SqlOperator.IN, value);
    }

    /**
     * 不包含
     *
     * @param field
     * @param value
     * @return
     */
    public static Cond notIn(String field, Object value) {
        return new Cond(field, SqlOperator.NOT_IN, value);
    }

    /**
     * 不包含
     *
     * @param sqlField
     * @param value
     * @return
     */
    public static Cond notIn(SqlField sqlField, Object value) {
        return new Cond(sqlField, SqlOperator.NOT_IN, value);
    }

    /**
     * 介于
     *
     * @param field
     * @param value1
     * @param value2
     * @return
     */
    public static Cond between(String field, Object value1, Object value2) {
        return new Cond(field, SqlOperator.BETWEEN, new Object[]{value1, value2});
    }

    /**
     * 介于
     *
     * @param sqlField
     * @param value1
     * @param value2
     * @return
     */
    public static Cond between(SqlField sqlField, Object value1, Object value2) {
        return new Cond(sqlField, SqlOperator.BETWEEN, new Object[]{value1, value2});
    }

    public Cond() {
        super();
    }

    public Cond(String name, SqlOperator sqlOperator, Object value) {
        super(null, null, null, name, value, sqlOperator);
    }

    public Cond(SqlField sqlField, SqlOperator sqlOperator, Object value) {
        super(null, sqlField.getSchema(), sqlField.getTableAlias(), sqlField.getName(), value, sqlOperator);
    }

}
