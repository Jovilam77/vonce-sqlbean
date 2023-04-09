package cn.vonce.sql.helper;

import cn.vonce.sql.bean.Column;
import cn.vonce.sql.bean.ConditionInfo;
import cn.vonce.sql.define.ColumnFunction;
import cn.vonce.sql.enumerate.SqlOperator;
import cn.vonce.sql.uitls.LambdaUtil;

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
        return new Cond(null, field, SqlOperator.EQUAL_TO, value);
    }

    /**
     * 等于
     *
     * @param tableAlias
     * @param field
     * @param value
     * @return
     */
    public static Cond eq(String tableAlias, String field, Object value) {
        return new Cond(tableAlias, field, SqlOperator.EQUAL_TO, value);
    }

    /**
     * 等于
     *
     * @param column
     * @param value
     * @return
     */
    public static Cond eq(Column column, Object value) {
        return new Cond(column, SqlOperator.EQUAL_TO, value);
    }

    /**
     * 等于
     *
     * @param columnFunction
     * @param value
     * @return
     */
    public static <T, R> Cond eq(ColumnFunction<T, R> columnFunction, Object value) {
        return new Cond(LambdaUtil.getColumn(columnFunction), SqlOperator.EQUAL_TO, value);
    }

    /**
     * 等于
     *
     * @param columnFunction
     * @param value
     * @return
     */
    public static <T, K, R> Cond eq(ColumnFunction<T, R> columnFunction, ColumnFunction<K, R> value) {
        return new Cond(LambdaUtil.getColumn(columnFunction), SqlOperator.EQUAL_TO, value);
    }

    /**
     * 不等于
     *
     * @param field
     * @param value
     * @return
     */
    public static Cond notEq(String field, Object value) {
        return new Cond(null, field, SqlOperator.NOT_EQUAL_TO, value);
    }

    /**
     * 不等于
     *
     * @param tableAlias
     * @param field
     * @param value
     * @return
     */
    public static Cond notEq(String tableAlias, String field, Object value) {
        return new Cond(tableAlias, field, SqlOperator.NOT_EQUAL_TO, value);
    }

    /**
     * 不等于
     *
     * @param column
     * @param value
     * @return
     */
    public static Cond notEq(Column column, Object value) {
        return new Cond(column, SqlOperator.NOT_EQUAL_TO, value);
    }

    /**
     * 不等于
     *
     * @param columnFunction
     * @param value
     * @return
     */
    public static <T, R> Cond notEq(ColumnFunction<T, R> columnFunction, Object value) {
        return new Cond(LambdaUtil.getColumn(columnFunction), SqlOperator.NOT_EQUAL_TO, value);
    }

    /**
     * 不等于
     *
     * @param columnFunction
     * @param value
     * @return
     */
    public static <T, K, R> Cond notEq(ColumnFunction<T, R> columnFunction, ColumnFunction<K, R> value) {
        return new Cond(LambdaUtil.getColumn(columnFunction), SqlOperator.NOT_EQUAL_TO, value);
    }

    /**
     * 小于
     *
     * @param field
     * @param value
     * @return
     */
    public static Cond lt(String field, Object value) {
        return new Cond(null, field, SqlOperator.LESS_THAN, value);
    }

    /**
     * 小于
     *
     * @param tableAlias
     * @param field
     * @param value
     * @return
     */
    public static Cond lt(String tableAlias, String field, Object value) {
        return new Cond(tableAlias, field, SqlOperator.LESS_THAN, value);
    }

    /**
     * 小于
     *
     * @param column
     * @param value
     * @return
     */
    public static Cond lt(Column column, Object value) {
        return new Cond(column, SqlOperator.LESS_THAN, value);
    }

    /**
     * 小于
     *
     * @param columnFunction
     * @param value
     * @return
     */
    public static <T, R> Cond lt(ColumnFunction<T, R> columnFunction, Object value) {
        return new Cond(LambdaUtil.getColumn(columnFunction), SqlOperator.LESS_THAN, value);
    }

    /**
     * 小于
     *
     * @param columnFunction
     * @param value
     * @return
     */
    public static <T, K, R> Cond lt(ColumnFunction<T, R> columnFunction, ColumnFunction<K, R> value) {
        return new Cond(LambdaUtil.getColumn(columnFunction), SqlOperator.LESS_THAN, value);
    }

    /**
     * 大于
     *
     * @param field
     * @param value
     * @return
     */
    public static Cond gt(String field, Object value) {
        return new Cond(null, field, SqlOperator.GREATER_THAN, value);
    }

    /**
     * 大于
     *
     * @param tableAlias
     * @param field
     * @param value
     * @return
     */
    public static Cond gt(String tableAlias, String field, Object value) {
        return new Cond(tableAlias, field, SqlOperator.GREATER_THAN, value);
    }

    /**
     * 大于
     *
     * @param column
     * @param value
     * @return
     */
    public static Cond gt(Column column, Object value) {
        return new Cond(column, SqlOperator.GREATER_THAN, value);
    }

    /**
     * 大于
     *
     * @param columnFunction
     * @param value
     * @return
     */
    public static <T, R> Cond gt(ColumnFunction<T, R> columnFunction, Object value) {
        return new Cond(LambdaUtil.getColumn(columnFunction), SqlOperator.GREATER_THAN, value);
    }

    /**
     * 大于
     *
     * @param columnFunction
     * @param value
     * @return
     */
    public static <T, K, R> Cond gt(ColumnFunction<T, R> columnFunction, ColumnFunction<K, R> value) {
        return new Cond(LambdaUtil.getColumn(columnFunction), SqlOperator.GREATER_THAN, value);
    }

    /**
     * 小于等于
     *
     * @param field
     * @param value
     * @return
     */
    public static Cond ltEq(String field, Object value) {
        return new Cond(null, field, SqlOperator.LESS_THAN_OR_EQUAL_TO, value);
    }

    /**
     * 小于等于
     *
     * @param tableAlias
     * @param field
     * @param value
     * @return
     */
    public static Cond ltEq(String tableAlias, String field, Object value) {
        return new Cond(tableAlias, field, SqlOperator.LESS_THAN_OR_EQUAL_TO, value);
    }

    /**
     * 小于等于
     *
     * @param column
     * @param value
     * @return
     */
    public static Cond ltEq(Column column, Object value) {
        return new Cond(column, SqlOperator.LESS_THAN_OR_EQUAL_TO, value);
    }

    /**
     * 小于等于
     *
     * @param columnFunction
     * @param value
     * @return
     */
    public static <T, R> Cond ltEq(ColumnFunction<T, R> columnFunction, Object value) {
        return new Cond(LambdaUtil.getColumn(columnFunction), SqlOperator.LESS_THAN_OR_EQUAL_TO, value);
    }

    /**
     * 小于等于
     *
     * @param columnFunction
     * @param value
     * @return
     */
    public static <T, K, R> Cond ltEq(ColumnFunction<T, R> columnFunction, ColumnFunction<K, R> value) {
        return new Cond(LambdaUtil.getColumn(columnFunction), SqlOperator.LESS_THAN_OR_EQUAL_TO, value);
    }

    /**
     * 大于等于
     *
     * @param field
     * @param value
     * @return
     */
    public static Cond gtEq(String field, Object value) {
        return new Cond(null, field, SqlOperator.GREAT_THAN_OR_EQUAL_TO, value);
    }

    /**
     * 大于等于
     *
     * @param tableAlias
     * @param field
     * @param value
     * @return
     */
    public static Cond gtEq(String tableAlias, String field, Object value) {
        return new Cond(tableAlias, field, SqlOperator.GREAT_THAN_OR_EQUAL_TO, value);
    }

    /**
     * 大于等于
     *
     * @param column
     * @param value
     * @return
     */
    public static Cond gtEq(Column column, Object value) {
        return new Cond(column, SqlOperator.GREAT_THAN_OR_EQUAL_TO, value);
    }

    /**
     * 大于等于
     *
     * @param columnFunction
     * @param value
     * @return
     */
    public static <T, R> Cond gtEq(ColumnFunction<T, R> columnFunction, Object value) {
        return new Cond(LambdaUtil.getColumn(columnFunction), SqlOperator.GREAT_THAN_OR_EQUAL_TO, value);
    }

    /**
     * 大于等于
     *
     * @param columnFunction
     * @param value
     * @return
     */
    public static <T, K, R> Cond gtEq(ColumnFunction<T, R> columnFunction, ColumnFunction<K, R> value) {
        return new Cond(LambdaUtil.getColumn(columnFunction), SqlOperator.GREAT_THAN_OR_EQUAL_TO, value);
    }

    /**
     * 全模糊 包含
     *
     * @param field
     * @param value
     * @return
     */
    public static Cond like(String field, Object value) {
        return new Cond(null, field, SqlOperator.LIKE, value);
    }

    /**
     * 全模糊 包含
     *
     * @param field
     * @param value
     * @return
     */
    public static Cond like(String tableAlias, String field, Object value) {
        return new Cond(tableAlias, field, SqlOperator.LIKE, value);
    }

    /**
     * 全模糊 包含
     *
     * @param column
     * @param value
     * @return
     */
    public static Cond like(Column column, Object value) {
        return new Cond(column, SqlOperator.LIKE, value);
    }

    /**
     * 全模糊 包含
     *
     * @param columnFunction
     * @param value
     * @return
     */
    public static <T, R> Cond like(ColumnFunction<T, R> columnFunction, Object value) {
        return new Cond(LambdaUtil.getColumn(columnFunction), SqlOperator.LIKE, value);
    }

    /**
     * 左模糊 包含
     *
     * @param field
     * @param value
     * @return
     */
    public static Cond likeL(String field, Object value) {
        return new Cond(null, field, SqlOperator.LIKE_L, value);
    }

    /**
     * 左模糊 包含
     *
     * @param tableAlias
     * @param field
     * @param value
     * @return
     */
    public static Cond likeL(String tableAlias, String field, Object value) {
        return new Cond(tableAlias, field, SqlOperator.LIKE_L, value);
    }

    /**
     * 左模糊 包含
     *
     * @param column
     * @param value
     * @return
     */
    public static Cond likeL(Column column, Object value) {
        return new Cond(column, SqlOperator.LIKE_L, value);
    }

    /**
     * 左模糊 包含
     *
     * @param columnFunction
     * @param value
     * @return
     */
    public static <T, R> Cond likeL(ColumnFunction<T, R> columnFunction, Object value) {
        return new Cond(LambdaUtil.getColumn(columnFunction), SqlOperator.LIKE_L, value);
    }

    /**
     * 右模糊 包含
     *
     * @param field
     * @param value
     * @return
     */
    public static Cond likeR(String field, Object value) {
        return new Cond(null, field, SqlOperator.LIKE_R, value);
    }

    /**
     * 右模糊 包含
     *
     * @param tableAlias
     * @param field
     * @param value
     * @return
     */
    public static Cond likeR(String tableAlias, String field, Object value) {
        return new Cond(tableAlias, field, SqlOperator.LIKE_R, value);
    }

    /**
     * 右模糊 包含
     *
     * @param column
     * @param value
     * @return
     */
    public static Cond likeR(Column column, Object value) {
        return new Cond(column, SqlOperator.LIKE_R, value);
    }

    /**
     * 右模糊 包含
     *
     * @param columnFunction
     * @param value
     * @return
     */
    public static <T, R> Cond likeR(ColumnFunction<T, R> columnFunction, Object value) {
        return new Cond(LambdaUtil.getColumn(columnFunction), SqlOperator.LIKE_R, value);
    }

    /**
     * 全模糊 不包含
     *
     * @param field
     * @param value
     * @return
     */
    public static Cond notLike(String field, Object value) {
        return new Cond(null, field, SqlOperator.NOT_LIKE, value);
    }

    /**
     * 全模糊 不包含
     *
     * @param tableAlias
     * @param field
     * @param value
     * @return
     */
    public static Cond notLike(String tableAlias, String field, Object value) {
        return new Cond(tableAlias, field, SqlOperator.NOT_LIKE, value);
    }


    /**
     * 全模糊 不包含
     *
     * @param column
     * @param value
     * @return
     */
    public static Cond notLike(Column column, Object value) {
        return new Cond(column, SqlOperator.NOT_LIKE, value);
    }

    /**
     * 全模糊 不包含
     *
     * @param columnFunction
     * @param value
     * @return
     */
    public static <T, R> Cond notLike(ColumnFunction<T, R> columnFunction, Object value) {
        return new Cond(LambdaUtil.getColumn(columnFunction), SqlOperator.NOT_LIKE, value);
    }

    /**
     * 左模糊 不包含
     *
     * @param field
     * @param value
     * @return
     */
    public static Cond notLikeL(String field, Object value) {
        return new Cond(null, field, SqlOperator.NOT_LIKE_L, value);
    }

    /**
     * 左模糊 不包含
     *
     * @param tableAlias
     * @param field
     * @param value
     * @return
     */
    public static Cond notLikeL(String tableAlias, String field, Object value) {
        return new Cond(tableAlias, field, SqlOperator.NOT_LIKE_L, value);
    }

    /**
     * 左模糊 不包含
     *
     * @param column
     * @param value
     * @return
     */
    public static Cond notLikeL(Column column, Object value) {
        return new Cond(column, SqlOperator.NOT_LIKE_L, value);
    }

    /**
     * 左模糊 不包含
     *
     * @param columnFunction
     * @param value
     * @return
     */
    public static <T, R> Cond notLikeL(ColumnFunction<T, R> columnFunction, Object value) {
        return new Cond(LambdaUtil.getColumn(columnFunction), SqlOperator.NOT_LIKE_L, value);
    }

    /**
     * 右模糊 不包含
     *
     * @param field
     * @param value
     * @return
     */
    public static Cond notLikeR(String field, Object value) {
        return new Cond(null, field, SqlOperator.NOT_LIKE_R, value);
    }

    /**
     * 右模糊 不包含
     *
     * @param tableAlias
     * @param field
     * @param value
     * @return
     */
    public static Cond notLikeR(String tableAlias, String field, Object value) {
        return new Cond(tableAlias, field, SqlOperator.NOT_LIKE_R, value);
    }

    /**
     * 右模糊 不包含
     *
     * @param column
     * @param value
     * @return
     */
    public static Cond notLikeR(Column column, Object value) {
        return new Cond(column, SqlOperator.NOT_LIKE_R, value);
    }

    /**
     * 右模糊 不包含
     *
     * @param columnFunction
     * @param value
     * @return
     */
    public static <T, R> Cond notLikeR(ColumnFunction<T, R> columnFunction, Object value) {
        return new Cond(LambdaUtil.getColumn(columnFunction), SqlOperator.NOT_LIKE_R, value);
    }

    /**
     * 是
     *
     * @param field
     * @param value
     * @return
     */
    public static Cond is(String field, Object value) {
        return new Cond(null, field, SqlOperator.IS, value);
    }

    /**
     * 是
     *
     * @param tableAlias
     * @param field
     * @param value
     * @return
     */
    public static Cond is(String tableAlias, String field, Object value) {
        return new Cond(tableAlias, field, SqlOperator.IS, value);
    }


    /**
     * 是
     *
     * @param column
     * @param value
     * @return
     */
    public static Cond is(Column column, Object value) {
        return new Cond(column, SqlOperator.IS, value);
    }

    /**
     * 是
     *
     * @param columnFunction
     * @param value
     * @return
     */
    public static <T, R> Cond is(ColumnFunction<T, R> columnFunction, Object value) {
        return new Cond(LambdaUtil.getColumn(columnFunction), SqlOperator.IS, value);
    }

    /**
     * 是
     *
     * @param columnFunction
     * @param value
     * @return
     */
    public static <T, K, R> Cond is(ColumnFunction<T, R> columnFunction, ColumnFunction<K, R> value) {
        return new Cond(LambdaUtil.getColumn(columnFunction), SqlOperator.IS, value);
    }

    /**
     * 不是
     *
     * @param field
     * @param value
     * @return
     */
    public static Cond isNot(String field, Object value) {
        return new Cond(null, field, SqlOperator.IS_NOT, value);
    }

    /**
     * 不是
     *
     * @param tableAlias
     * @param field
     * @param value
     * @return
     */
    public static Cond isNot(String tableAlias, String field, Object value) {
        return new Cond(tableAlias, field, SqlOperator.IS_NOT, value);
    }


    /**
     * 不是
     *
     * @param column
     * @param value
     * @return
     */
    public static Cond isNot(Column column, Object value) {
        return new Cond(column, SqlOperator.IS_NOT, value);
    }

    /**
     * 不是
     *
     * @param columnFunction
     * @param value
     * @return
     */
    public static <T, R> Cond isNot(ColumnFunction<T, R> columnFunction, Object value) {
        return new Cond(LambdaUtil.getColumn(columnFunction), SqlOperator.IS_NOT, value);
    }

    /**
     * 不是
     *
     * @param columnFunction
     * @param value
     * @return
     */
    public static <T, K, R> Cond isNot(ColumnFunction<T, R> columnFunction, ColumnFunction<K, R> value) {
        return new Cond(LambdaUtil.getColumn(columnFunction), SqlOperator.IS_NOT, value);
    }

    /**
     * 是null
     *
     * @param field
     * @return
     */
    public static Cond isNull(String field) {
        return new Cond(null, field, SqlOperator.IS_NULL, null);
    }

    /**
     * 是null
     *
     * @param tableAlias
     * @param field
     * @return
     */
    public static Cond isNull(String tableAlias, String field) {
        return new Cond(tableAlias, field, SqlOperator.IS_NULL, null);
    }

    /**
     * 是null
     *
     * @param column
     * @return
     */
    public static Cond isNull(Column column) {
        return new Cond(column, SqlOperator.IS_NULL, null);
    }

    /**
     * 是null
     *
     * @param columnFunction
     * @return
     */
    public static <T, R> Cond isNull(ColumnFunction<T, R> columnFunction) {
        return new Cond(LambdaUtil.getColumn(columnFunction), SqlOperator.IS_NULL, null);
    }

    /**
     * 不是null
     *
     * @param field
     * @return
     */
    public static Cond isNotNull(String field) {
        return new Cond(null, field, SqlOperator.IS_NOT_NULL, null);
    }

    /**
     * 不是null
     *
     * @param tableAlias
     * @param field
     * @return
     */
    public static Cond isNotNull(String tableAlias, String field) {
        return new Cond(tableAlias, field, SqlOperator.IS_NOT_NULL, null);
    }

    /**
     * 不是null
     *
     * @param column
     * @return
     */
    public static Cond isNotNull(Column column) {
        return new Cond(column, SqlOperator.IS_NOT_NULL, null);
    }

    /**
     * 不是null
     *
     * @param columnFunction
     * @return
     */
    public static <T, R> Cond isNotNull(ColumnFunction<T, R> columnFunction) {
        return new Cond(LambdaUtil.getColumn(columnFunction), SqlOperator.IS_NOT_NULL, null);
    }

    /**
     * 包含
     *
     * @param field
     * @param value
     * @return
     */
    public static Cond in(String field, Object value) {
        return new Cond(null, field, SqlOperator.IN, value);
    }

    /**
     * 包含
     *
     * @param tableAlias
     * @param field
     * @param value
     * @return
     */
    public static Cond in(String tableAlias, String field, Object value) {
        return new Cond(tableAlias, field, SqlOperator.IN, value);
    }

    /**
     * 包含
     *
     * @param column
     * @param value
     * @return
     */
    public static Cond in(Column column, Object value) {
        return new Cond(column, SqlOperator.IN, value);
    }

    /**
     * 包含
     *
     * @param columnFunction
     * @param value
     * @return
     */
    public static <T, R> Cond in(ColumnFunction<T, R> columnFunction, Object value) {
        return new Cond(LambdaUtil.getColumn(columnFunction), SqlOperator.IN, value);
    }

    /**
     * 不包含
     *
     * @param field
     * @param value
     * @return
     */
    public static Cond notIn(String field, Object value) {
        return new Cond(null, field, SqlOperator.NOT_IN, value);
    }

    /**
     * 不包含
     *
     * @param tableAlias
     * @param field
     * @param value
     * @return
     */
    public static Cond notIn(String tableAlias, String field, Object value) {
        return new Cond(tableAlias, field, SqlOperator.NOT_IN, value);
    }


    /**
     * 不包含
     *
     * @param column
     * @param value
     * @return
     */
    public static Cond notIn(Column column, Object value) {
        return new Cond(column, SqlOperator.NOT_IN, value);
    }

    /**
     * 不包含
     *
     * @param columnFunction
     * @param value
     * @return
     */
    public static <T, R> Cond notIn(ColumnFunction<T, R> columnFunction, Object value) {
        return new Cond(LambdaUtil.getColumn(columnFunction), SqlOperator.NOT_IN, value);
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
        return new Cond(null, field, SqlOperator.BETWEEN, new Object[]{value1, value2});
    }

    /**
     * 介于
     *
     * @param tableAlias
     * @param field
     * @param value1
     * @param value2
     * @return
     */
    public static Cond between(String tableAlias, String field, Object value1, Object value2) {
        return new Cond(tableAlias, field, SqlOperator.BETWEEN, new Object[]{value1, value2});
    }

    /**
     * 介于
     *
     * @param column
     * @param value1
     * @param value2
     * @return
     */
    public static Cond between(Column column, Object value1, Object value2) {
        return new Cond(column, SqlOperator.BETWEEN, new Object[]{value1, value2});
    }

    /**
     * 介于
     *
     * @param columnFunction
     * @param value1
     * @param value2
     * @return
     */
    public static <T, R> Cond between(ColumnFunction<T, R> columnFunction, Object value1, Object value2) {
        return new Cond(LambdaUtil.getColumn(columnFunction), SqlOperator.BETWEEN, new Object[]{value1, value2});
    }

    public Cond() {
        super();
    }

    public Cond(String tableAlias, String name, SqlOperator sqlOperator, Object value) {
        super(null, tableAlias, name, value, sqlOperator);
    }

    public Cond(Column column, SqlOperator sqlOperator, Object value) {
        super(null, column, value, sqlOperator);
    }

}
