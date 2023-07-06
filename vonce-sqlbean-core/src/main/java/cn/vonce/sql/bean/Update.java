package cn.vonce.sql.bean;

import cn.vonce.sql.define.ColumnFun;
import cn.vonce.sql.uitls.LambdaUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 更新
 *
 * @author Jovi
 * @version 1.0
 * @email imjovi@qq.com
 * @date 2017年8月18日上午9:00:19
 */
public class Update<T> extends CommonCondition<Update<T>> implements Serializable {

    public Update() {
        super();
        super.setReturnObj(this);
    }

    /**
     * 更新的实体对象
     */
    private T updateBean = null;
    /**
     * 过滤的字段数组
     */
    private List<Column> filterColumns = new ArrayList<>();
    /**
     * 默认只更新不为空的字段
     */
    private boolean updateNotNull = true;
    /**
     * 是否使用乐观锁
     */
    private boolean optimisticLock = false;

    /**
     * 更新的字段列表
     */
    private List<SetInfo> setInfoList = new ArrayList<>();

    /**
     * 获取更新的实体对象
     *
     * @return
     */
    public T getUpdateBean() {
        return updateBean;
    }

    /**
     * 设置更新的实体对象
     *
     * @return
     */
    public void setUpdateBean(T updateBean) {
        this.updateBean = updateBean;
    }

    /**
     * 设置过滤的列字段
     *
     * @param filterFields
     */
    public Update filterFields(String... filterFields) {
        if (filterFields != null && filterFields.length > 0) {
            for (String filterField : filterFields) {
                this.filterColumns.add(new Column(filterField));
            }
        }
        return this;
    }

    /**
     * 获取过滤的列字段
     *
     * @return
     */
    public List<Column> getFilterColumns() {
        return filterColumns;
    }

    /**
     * 设置过滤的列字段
     *
     * @param filterColumns
     */
    public Update filterFields(Column... filterColumns) {
        if (filterColumns != null && filterColumns.length > 0) {
            for (Column column : filterColumns) {
                this.filterColumns.add(column);
            }
        }
        return this;
    }

    /**
     * 设置过滤的列字段
     *
     * @param columnFuns
     */
    public <R> Update filterFields(ColumnFun<T, R>... columnFuns) {
        if (columnFuns != null && columnFuns.length > 0) {
            for (ColumnFun<T, R> columnFun : columnFuns) {
                this.filterColumns.add(LambdaUtil.getColumn(columnFun));
            }
        }
        return this;
    }

    /**
     * 是否仅更新不为空的字段
     *
     * @return
     */
    public boolean isUpdateNotNull() {
        return updateNotNull;
    }

    /**
     * 设置是否仅更新不为空的字段
     *
     * @param updateNotNull
     */
    public void setUpdateNotNull(boolean updateNotNull) {
        this.updateNotNull = updateNotNull;
    }

    /**
     * 是否使用乐观锁
     *
     * @return
     */
    public boolean isOptimisticLock() {
        return optimisticLock;
    }

    /**
     * 设置是否使用乐观锁
     *
     * @param optimisticLock
     */
    public void setOptimisticLock(boolean optimisticLock) {
        this.optimisticLock = optimisticLock;
    }

    /**
     * 获取更新的字段列表
     *
     * @return
     */
    public List<SetInfo> getSetInfoList() {
        return setInfoList;
    }

    /**
     * 设置更新的字段列表
     *
     * @param setInfoList
     */
    public void setSetInfoList(List<SetInfo> setInfoList) {
        this.setInfoList = setInfoList;
    }

    /**
     * 设置字段值
     *
     * @param columnName 字段名
     * @param value      值
     * @return
     */
    public Update<T> set(String columnName, Object value) {
        setInfoList.add(new SetInfo(columnName, value));
        return this;
    }

    /**
     * 设置字段值
     *
     * @param tableAlias 表别名
     * @param columnName 字段名
     * @param value      值
     * @return
     */
    public Update<T> set(String tableAlias, String columnName, Object value) {
        setInfoList.add(new SetInfo(tableAlias, columnName, value));
        return this;
    }

    /**
     * 设置字段值
     *
     * @param column 字段信息
     * @param value  值
     * @return
     */
    public Update<T> set(Column column, Object value) {
        setInfoList.add(new SetInfo(column.getTableAlias(), column.getName(), value));
        return this;
    }

    /**
     * 设置字段值
     *
     * @param columnFun 字段信息
     * @param value          值
     * @param <R>
     * @return
     */
    public <R> Update<T> set(ColumnFun<T, R> columnFun, Object value) {
        Column column = LambdaUtil.getColumn(columnFun);
        setInfoList.add(new SetInfo(column.getTableAlias(), column.getName(), value));
        return this;
    }

    /**
     * 设置字段值 值相加
     *
     * @param columnName 字段名
     * @param value1     第一个值
     * @param value2     第二个值
     * @return
     */
    public Update<T> setAdd(String columnName, Object value1, Object value2) {
        setInfoList.add(new SetInfo(SetInfo.Operator.ADDITION, columnName, value1, value2));
        return this;
    }

    /**
     * 设置字段值 值相加
     *
     * @param tableAlias 表别名
     * @param columnName 字段名
     * @param value1     第一个值
     * @param value2     第二个值
     * @return
     */
    public Update<T> setAdd(String tableAlias, String columnName, Object value1, Object value2) {
        setInfoList.add(new SetInfo(SetInfo.Operator.ADDITION, tableAlias, columnName, value1, value2));
        return this;
    }

    /**
     * 设置字段值 值相加
     *
     * @param column 字段信息
     * @param value1 第一个值
     * @param value2 第二个值
     * @return
     */
    public Update<T> setAdd(Column column, Object value1, Object value2) {
        setInfoList.add(new SetInfo(SetInfo.Operator.ADDITION, column.getTableAlias(), column.getName(), value1, value2));
        return this;
    }

    /**
     * 设置字段值 值相加
     *
     * @param columnFun 字段信息
     * @param value1         第一个值
     * @param value2         第二个值
     * @return
     */
    public <R> Update<T> setAdd(ColumnFun<T, R> columnFun, Object value1, Object value2) {
        Column column = LambdaUtil.getColumn(columnFun);
        setInfoList.add(new SetInfo(SetInfo.Operator.ADDITION, column.getTableAlias(), column.getName(), value1, value2));
        return this;
    }

    /**
     * 设置字段值 值相加
     *
     * @param columnFun 字段信息
     * @param value1         第一个值（字段信息）
     * @param value2         第二个值
     * @return
     */
    public <k, R> Update<T> setAdd(ColumnFun<T, R> columnFun, ColumnFun<k, R> value1, Object value2) {
        Column column = LambdaUtil.getColumn(columnFun);
        setInfoList.add(new SetInfo(SetInfo.Operator.ADDITION, column.getTableAlias(), column.getName(), value1, value2));
        return this;
    }

    /**
     * 设置字段值 值相减
     *
     * @param columnName 字段名
     * @param value1     第一个值
     * @param value2     第二个值
     * @return
     */
    public Update<T> setSub(String columnName, Object value1, Object value2) {
        setInfoList.add(new SetInfo(SetInfo.Operator.SUBTRACT, columnName, value1, value2));
        return this;
    }

    /**
     * 设置字段值 值相减
     *
     * @param tableAlias 表别名
     * @param columnName 字段名
     * @param value1     第一个值
     * @param value2     第二个值
     * @return
     */
    public Update<T> setSub(String tableAlias, String columnName, Object value1, Object value2) {
        setInfoList.add(new SetInfo(SetInfo.Operator.SUBTRACT, tableAlias, columnName, value1, value2));
        return this;
    }

    /**
     * 设置字段值 值相减
     *
     * @param column 字段信息
     * @param value1 第一个值
     * @param value2 第二个值
     * @return
     */
    public Update<T> setSub(Column column, Object value1, Object value2) {
        setInfoList.add(new SetInfo(SetInfo.Operator.SUBTRACT, column.getTableAlias(), column.getName(), value1, value2));
        return this;
    }

    /**
     * 设置字段值 值相减
     *
     * @param columnFun 字段信息
     * @param value1         第一个值
     * @param value2         第二个值
     * @return
     */
    public <R> Update<T> setSub(ColumnFun<T, R> columnFun, Object value1, Object value2) {
        Column column = LambdaUtil.getColumn(columnFun);
        setInfoList.add(new SetInfo(SetInfo.Operator.SUBTRACT, column.getTableAlias(), column.getName(), value1, value2));
        return this;
    }

    /**
     * 设置字段值 值相减
     *
     * @param columnFun 字段信息
     * @param value1         第一个值（字段信息）
     * @param value2         第二个值
     * @return
     */
    public <k, R> Update<T> setSub(ColumnFun<T, R> columnFun, ColumnFun<k, R> value1, Object value2) {
        Column column = LambdaUtil.getColumn(columnFun);
        setInfoList.add(new SetInfo(SetInfo.Operator.SUBTRACT, column.getTableAlias(), column.getName(), value1, value2));
        return this;
    }

}