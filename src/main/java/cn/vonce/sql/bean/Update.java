package cn.vonce.sql.bean;

import cn.vonce.sql.annotation.SqlBeanTable;

import java.io.Serializable;

public class Update extends Common implements Serializable {

    private boolean updateNotNull = true;//默认只更新不为空的字段
    private String updateTable = null;//更新哪张表
    private Object updateBean = null;//更新的实体对象
    private String[] filterFields = null;//需要过滤的字段

    /**
     * 设置是否只更新不为null的字段
     *
     * @return
     * @author Jovi
     * @date 2017年9月6日下午12:50:54
     */
    public boolean isUpdateNotNull() {
        return updateNotNull;
    }

    /**
     * 设置是否只更新不为null的字段
     *
     * @param updateNotNull
     * @author Jovi
     * @date 2017年9月6日下午12:51:19
     */
    public void setUpdateNotNull(boolean updateNotNull) {
        this.updateNotNull = updateNotNull;
    }

    /**
     * 获取更新表名
     *
     * @return
     * @author Jovi
     * @date 2017年8月18日上午8:56:22
     */
    public String getUpdateTable() {
        return updateTable;
    }

    /**
     * 设置更新表名
     *
     * @param updateTable
     * @author Jovi
     * @date 2017年8月18日上午8:56:13
     */
    public void setUpdateTable(String updateTable) {
        this.updateTable = updateTable;
    }

    /**
     * 设置更新表名
     *
     * @param clazz
     * @author Jovi
     * @date 2018年3月30日上午10:38:39
     */
    public void setUpdateTable(Class<?> clazz) {
        SqlBeanTable sqlBeanTable = clazz.getAnnotation(SqlBeanTable.class);
        if (sqlBeanTable != null) {
            this.updateTable = sqlBeanTable.value();
        } else {
            this.updateTable = clazz.getSimpleName();
        }
    }

    /**
     * 获取更新实体类
     *
     * @return
     * @author Jovi
     * @date 2017年8月18日上午8:56:05
     */
    public Object getUpdateBean() {
        return updateBean;
    }

    /**
     * 设置更新实体类
     *
     * @param updateBean
     * @author Jovi
     * @date 2017年8月18日上午8:55:58
     */
    public void setUpdateBean(Object updateBean) {
        this.updateBean = updateBean;
    }

    /**
     * 获取过滤的字段
     *
     * @return
     * @author Jovi
     * @date 2017年8月18日上午8:59:26
     */
    public String[] getFilterFields() {
        return filterFields;
    }

    /**
     * 设置过滤的字段
     *
     * @param filterField
     * @author Jovi
     * @date 2017年8月18日上午8:59:14
     */
    public void setFilterFields(String... filterField) {
        this.filterFields = filterField;
    }


}