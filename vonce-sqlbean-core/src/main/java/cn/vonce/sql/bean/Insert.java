package cn.vonce.sql.bean;

import cn.vonce.sql.define.ColumnFun;
import cn.vonce.sql.uitls.LambdaUtil;
import cn.vonce.sql.uitls.SqlBeanUtil;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 插入
 *
 * @author Jovi
 * @version 1.0
 * @email imjovi@qq.com
 * @date 2017年8月18日上午9:00:19
 */
public class Insert<T> extends Common implements Serializable {

    private List<T> insertBean = null;//插入的实体对象
    private List<Column> columnList = new ArrayList<>();//列字段对象列表
    private List<List<Object>> valuesList = new ArrayList<>();//值对象列表

    /**
     * 获取插入实体类
     *
     * @return
     */
    public List<T> getInsertBean() {
        return insertBean;
    }

    /**
     * 设置插入实体类
     *
     * @param beanList
     */
    public void setInsertBean(List<T> beanList) {
        this.insertBean = beanList;
    }

    /**
     * 设置插入实体类
     *
     * @param bean
     */
    public void setInsertBean(T bean) {
        if (bean == null) {
            return;
        }
        if (this.insertBean == null) {
            this.insertBean = new ArrayList<>();
        }
        if (bean.getClass().isArray()) {
            T[] arrays = (T[]) bean;
            for (T item : arrays) {
                this.insertBean.add(item);
            }
        } else {
            this.insertBean.add(bean);
        }
    }

    /**
     * 获取列字段对象列表
     *
     * @return
     */
    public List<Column> getColumnList() {
        return columnList;
    }

    /**
     * 获取值对象列表
     *
     * @return
     */
    public List<List<Object>> getValuesList() {
        return valuesList;
    }

    /**
     * 指定字段
     *
     * @param clazz 对象信息
     * @return
     */
    public Insert<T> column(Class<T> clazz) {
        if (clazz != null) {
            List<Field> fieldList = SqlBeanUtil.getBeanAllField(clazz);
            if (fieldList != null && fieldList.size() > 0) {
                columnList.clear();
                for (Field field : fieldList) {
                    columnList.add(SqlBeanUtil.getColumnByField(field));
                }
            }
        }
        return this;
    }

    /**
     * 指定字段
     *
     * @param column 字段信息
     * @return
     */
    public Insert<T> column(Column... column) {
        if (column != null && column.length > 0) {
            columnList.clear();
            columnList.addAll(Arrays.asList(column));
        }
        return this;
    }

    /**
     * 指定字段
     *
     * @param columnFun 字段信息
     * @param <R>
     * @return
     */
    public <R> Insert<T> column(ColumnFun<T, R>... columnFun) {
        if (columnFun != null && columnFun.length > 0) {
            columnList.clear();
            for (ColumnFun<T, R> trColumnFun : columnFun) {
                columnList.add(LambdaUtil.getColumn(trColumnFun));
            }
        }
        return this;
    }

    /**
     * 设置字段值
     *
     * @param value 字段信息
     * @return
     */
    public Insert<T> values(Object... value) {
        if (value != null && value.length > 0) {
            valuesList.add(Arrays.asList(value));
        }
        return this;
    }

}