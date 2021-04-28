package cn.vonce.sql.helper;

import cn.vonce.sql.bean.Column;
import cn.vonce.sql.bean.ConditionInfo;
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

    public static Cond eq(String field, Object value) {
        return new Cond(field, SqlOperator.EQUAL_TO, value);
    }

    public static Cond notEq(String field, Object value) {
        return new Cond(field, SqlOperator.NOT_EQUAL_TO, value);
    }

    public static Cond lt(String field, Object value) {
        return new Cond(field, SqlOperator.LESS_THAN, value);
    }

    public static Cond gt(String field, Object value) {
        return new Cond(field, SqlOperator.GREATER_THAN, value);
    }

    public static Cond ltEq(String field, Object value) {
        return new Cond(field, SqlOperator.LESS_THAN_OR_EQUAL_TO, value);
    }

    public static Cond gtEq(String field, Object value) {
        return new Cond(field, SqlOperator.GREAT_THAN_OR_EQUAL_TO, value);
    }

    public static Cond like(String field, Object value) {
        return new Cond(field, SqlOperator.LIKE, value);
    }

    public static Cond likeL(String field, Object value) {
        return new Cond(field, SqlOperator.LIKE_L, value);
    }

    public static Cond likeR(String field, Object value) {
        return new Cond(field, SqlOperator.LIKE_R, value);
    }

    public static Cond notLike(String field, Object value) {
        return new Cond(field, SqlOperator.NOT_LIKE, value);
    }

    public static Cond notLikeL(String field, Object value) {
        return new Cond(field, SqlOperator.NOT_LIKE_L, value);
    }

    public static Cond notLikeR(String field, Object value) {
        return new Cond(field, SqlOperator.NOT_LIKE_R, value);
    }

    public static Cond is(String field, Object value) {
        return new Cond(field, SqlOperator.IS, value);
    }

    public static Cond isNot(String field, Object value) {
        return new Cond(field, SqlOperator.IS_NOT, value);
    }

    public static Cond in(String field, Object value) {
        return new Cond(field, SqlOperator.IN, value);
    }

    public static Cond notIn(String field, Object value) {
        return new Cond(field, SqlOperator.NOT_IN, value);
    }

    public static Cond between(String field, Object value1, Object value2) {
        return new Cond(field, SqlOperator.BETWEEN, new Object[]{value1, value2});
    }

//    private String name;
//    private SqlOperator sqlOperator;
//    private Object value;
//
    public Cond() {
        super();
    }

    public Cond(String name, SqlOperator sqlOperator, Object value) {
        super(null, null, null, name, value, sqlOperator);
    }

    public Cond(Column column, SqlOperator sqlOperator, Object value){
        super(null, column.getSchema(), column.getTableAlias(), column.getName(), value, sqlOperator);
    }
//
//    public String getName() {
//        return name;
//    }
//
//    private void setName(String name) {
//        this.name = name;
//    }
//
//    public SqlOperator getSqlOperator() {
//        return sqlOperator;
//    }
//
//    private void setSqlOperator(SqlOperator sqlOperator) {
//        this.sqlOperator = sqlOperator;
//    }
//
//    public Object getValue() {
//        return value;
//    }
//
//    private void setValue(Object value) {
//        this.value = value;
//    }


}
