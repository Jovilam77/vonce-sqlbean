package cn.vonce.sql.bean;

import cn.vonce.sql.enumerate.SqlLogic;
import cn.vonce.sql.enumerate.SqlOperator;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 简单的条件
 *
 * @author Jovi
 * @version 1.0
 * @email 766255988@qq.com
 * @date 2022年3月15日上午10:00:10
 */
public class SimpleCondition<Action> implements Serializable {

    private Action action;
    private SqlLogic sqlLogic;
    private Logic<Action> logic = new Logic(this);
    private List<ConditionData> dataList = new ArrayList<>();

    protected SimpleCondition(Action action){
        this.action = action;
    }

    protected Action getAction() {
        return action;
    }

    protected SqlLogic getSqlLogic() {
        return sqlLogic;
    }

    protected void setSqlLogic(SqlLogic sqlLogic) {
        this.sqlLogic = sqlLogic;
    }

    /**
     * 获得条件模型列表
     *
     * @return
     */
    public List<ConditionData> getDataList() {
        return this.dataList;
    }

    /**
     * 等于
     *
     * @param field
     * @param value
     * @return
     */
    public Logic<Action> eq(String field, Object value) {
        dataList.add(new ConditionData(sqlLogic, newConditionInfo(field, SqlOperator.EQUAL_TO, value)));
        return logic;
    }

    /**
     * 等于
     *
     * @param column
     * @param value
     * @return
     */
    public Logic<Action> eq(Column column, Object value) {
        dataList.add(new ConditionData(sqlLogic, newConditionInfo(column, SqlOperator.EQUAL_TO, value)));
        return logic;
    }

    /**
     * 不等于
     *
     * @param field
     * @param value
     * @return
     */
    public Logic<Action> notEq(String field, Object value) {
        dataList.add(new ConditionData(sqlLogic, newConditionInfo(field, SqlOperator.EQUAL_TO, value)));
        return logic;
    }

    /**
     * 不等于
     *
     * @param column
     * @param value
     * @return
     */
    public Logic<Action> notEq(Column column, Object value) {
        dataList.add(new ConditionData(sqlLogic, newConditionInfo(column, SqlOperator.EQUAL_TO, value)));
        return logic;
    }

    /**
     * 小于
     *
     * @param field
     * @param value
     * @return
     */
    public Logic<Action> lt(String field, Object value) {
        dataList.add(new ConditionData(sqlLogic, newConditionInfo(field, SqlOperator.EQUAL_TO, value)));
        return logic;
    }

    /**
     * 小于
     *
     * @param column
     * @param value
     * @return
     */
    public Logic<Action> lt(Column column, Object value) {
        dataList.add(new ConditionData(sqlLogic, newConditionInfo(column, SqlOperator.EQUAL_TO, value)));
        return logic;
    }

    /**
     * 大于
     *
     * @param field
     * @param value
     * @return
     */
    public Logic<Action> gt(String field, Object value) {
        dataList.add(new ConditionData(sqlLogic, newConditionInfo(field, SqlOperator.EQUAL_TO, value)));
        return logic;
    }

    /**
     * 大于
     *
     * @param column
     * @param value
     * @return
     */
    public Logic<Action> gt(Column column, Object value) {
        dataList.add(new ConditionData(sqlLogic, newConditionInfo(column, SqlOperator.EQUAL_TO, value)));
        return logic;
    }

    /**
     * 小于等于
     *
     * @param field
     * @param value
     * @return
     */
    public Logic<Action> ltEq(String field, Object value) {
        dataList.add(new ConditionData(sqlLogic, newConditionInfo(field, SqlOperator.EQUAL_TO, value)));
        return logic;
    }

    /**
     * 小于等于
     *
     * @param column
     * @param value
     * @return
     */
    public Logic<Action> ltEq(Column column, Object value) {
        dataList.add(new ConditionData(sqlLogic, newConditionInfo(column, SqlOperator.EQUAL_TO, value)));
        return logic;
    }

    /**
     * 大于等于
     *
     * @param field
     * @param value
     * @return
     */
    public Logic<Action> gtEq(String field, Object value) {
        dataList.add(new ConditionData(sqlLogic, newConditionInfo(field, SqlOperator.EQUAL_TO, value)));
        return logic;
    }

    /**
     * 大于等于
     *
     * @param column
     * @param value
     * @return
     */
    public Logic<Action> gtEq(Column column, Object value) {
        dataList.add(new ConditionData(sqlLogic, newConditionInfo(column, SqlOperator.EQUAL_TO, value)));
        return logic;
    }

    /**
     * 全模糊 包含
     *
     * @param field
     * @param value
     * @return
     */
    public Logic<Action> like(String field, Object value) {
        dataList.add(new ConditionData(sqlLogic, newConditionInfo(field, SqlOperator.EQUAL_TO, value)));
        return logic;
    }

    /**
     * 全模糊 包含
     *
     * @param column
     * @param value
     * @return
     */
    public Logic<Action> like(Column column, Object value) {
        dataList.add(new ConditionData(sqlLogic, newConditionInfo(column, SqlOperator.EQUAL_TO, value)));
        return logic;
    }

    /**
     * 左模糊 包含
     *
     * @param field
     * @param value
     * @return
     */
    public Logic<Action> likeL(String field, Object value) {
        dataList.add(new ConditionData(sqlLogic, newConditionInfo(field, SqlOperator.EQUAL_TO, value)));
        return logic;
    }

    /**
     * 左模糊 包含
     *
     * @param column
     * @param value
     * @return
     */
    public Logic<Action> likeL(Column column, Object value) {
        dataList.add(new ConditionData(sqlLogic, newConditionInfo(column, SqlOperator.EQUAL_TO, value)));
        return logic;
    }

    /**
     * 右模糊 包含
     *
     * @param field
     * @param value
     * @return
     */
    public Logic<Action> likeR(String field, Object value) {
        dataList.add(new ConditionData(sqlLogic, newConditionInfo(field, SqlOperator.EQUAL_TO, value)));
        return logic;
    }

    /**
     * 右模糊 包含
     *
     * @param column
     * @param value
     * @return
     */
    public Logic<Action> likeR(Column column, Object value) {
        dataList.add(new ConditionData(sqlLogic, newConditionInfo(column, SqlOperator.EQUAL_TO, value)));
        return logic;
    }

    /**
     * 全模糊 不包含
     *
     * @param field
     * @param value
     * @return
     */
    public Logic<Action> notLike(String field, Object value) {
        dataList.add(new ConditionData(sqlLogic, newConditionInfo(field, SqlOperator.EQUAL_TO, value)));
        return logic;
    }

    /**
     * 全模糊 不包含
     *
     * @param column
     * @param value
     * @return
     */
    public Logic<Action> notLike(Column column, Object value) {
        dataList.add(new ConditionData(sqlLogic, newConditionInfo(column, SqlOperator.EQUAL_TO, value)));
        return logic;
    }

    /**
     * 左模糊 不包含
     *
     * @param field
     * @param value
     * @return
     */
    public Logic<Action> notLikeL(String field, Object value) {
        dataList.add(new ConditionData(sqlLogic, newConditionInfo(field, SqlOperator.EQUAL_TO, value)));
        return logic;
    }

    /**
     * 左模糊 不包含
     *
     * @param column
     * @param value
     * @return
     */
    public Logic<Action> notLikeL(Column column, Object value) {
        dataList.add(new ConditionData(sqlLogic, newConditionInfo(column, SqlOperator.EQUAL_TO, value)));
        return logic;
    }

    /**
     * 右模糊 不包含
     *
     * @param field
     * @param value
     * @return
     */
    public Logic<Action> notLikeR(String field, Object value) {
        dataList.add(new ConditionData(sqlLogic, newConditionInfo(field, SqlOperator.EQUAL_TO, value)));
        return logic;
    }

    /**
     * 右模糊 不包含
     *
     * @param column
     * @param value
     * @return
     */
    public Logic<Action> notLikeR(Column column, Object value) {
        dataList.add(new ConditionData(sqlLogic, newConditionInfo(column, SqlOperator.EQUAL_TO, value)));
        return logic;
    }

    /**
     * 是
     *
     * @param field
     * @param value
     * @return
     */
    public Logic<Action> is(String field, Object value) {
        dataList.add(new ConditionData(sqlLogic, newConditionInfo(field, SqlOperator.EQUAL_TO, value)));
        return logic;
    }

    /**
     * 是
     *
     * @param column
     * @param value
     * @return
     */
    public Logic<Action> is(Column column, Object value) {
        dataList.add(new ConditionData(sqlLogic, newConditionInfo(column, SqlOperator.EQUAL_TO, value)));
        return logic;
    }

    /**
     * 不是
     *
     * @param field
     * @param value
     * @return
     */
    public Logic<Action> isNot(String field, Object value) {
        dataList.add(new ConditionData(sqlLogic, newConditionInfo(field, SqlOperator.EQUAL_TO, value)));
        return logic;
    }

    /**
     * 不是
     *
     * @param column
     * @param value
     * @return
     */
    public Logic<Action> isNot(Column column, Object value) {
        dataList.add(new ConditionData(sqlLogic, newConditionInfo(column, SqlOperator.EQUAL_TO, value)));
        return logic;
    }

    /**
     * 包含
     *
     * @param field
     * @param value
     * @return
     */
    public Logic<Action> in(String field, Object value) {
        dataList.add(new ConditionData(sqlLogic, newConditionInfo(field, SqlOperator.EQUAL_TO, value)));
        return logic;
    }

    /**
     * 包含
     *
     * @param column
     * @param value
     * @return
     */
    public Logic<Action> in(Column column, Object value) {
        dataList.add(new ConditionData(sqlLogic, newConditionInfo(column, SqlOperator.EQUAL_TO, value)));
        return logic;
    }

    /**
     * 不包含
     *
     * @param field
     * @param value
     * @return
     */
    public Logic<Action> notIn(String field, Object value) {
        dataList.add(new ConditionData(sqlLogic, newConditionInfo(field, SqlOperator.EQUAL_TO, value)));
        return logic;
    }

    /**
     * 不包含
     *
     * @param column
     * @param value
     * @return
     */
    public Logic<Action> notIn(Column column, Object value) {
        dataList.add(new ConditionData(sqlLogic, newConditionInfo(column, SqlOperator.EQUAL_TO, value)));
        return logic;
    }

    /**
     * 介于
     *
     * @param field
     * @param value1
     * @param value2
     * @return
     */
    public Logic<Action> between(String field, Object value1, Object value2) {
        dataList.add(new ConditionData(sqlLogic, newConditionInfo(field, SqlOperator.EQUAL_TO, new Object[]{value1, value2})));
        return logic;
    }

    /**
     * 介于
     *
     * @param column
     * @param value1
     * @param value2
     * @return
     */
    public Logic<Action> between(Column column, Object value1, Object value2) {
        dataList.add(new ConditionData(sqlLogic, newConditionInfo(column, SqlOperator.EQUAL_TO, new Object[]{value1, value2})));
        return logic;
    }

    private ConditionInfo newConditionInfo(String name, SqlOperator sqlOperator, Object value) {
        return new ConditionInfo(null, null, name, value, sqlOperator);
    }

    private ConditionInfo newConditionInfo(Column column, SqlOperator sqlOperator, Object value) {
        return new ConditionInfo(null, column.getTableAlias(), column.getName(), value, sqlOperator);
    }

}
