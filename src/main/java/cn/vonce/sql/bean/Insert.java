package cn.vonce.sql.bean;

import cn.vonce.sql.annotation.SqlBeanTable;

import java.io.Serializable;

/**
 * Insert
 *
 * @author Jovi
 * @version 1.0
 * @email 766255988@qq.com
 * @date 2017年8月18日上午9:00:19
 */
public class Insert implements Serializable {

    private String insertTable = null;//插入哪张表
    private Object insertBean = null;//插入的实体对象

    /**
     * 获取插入表名
     *
     * @return
     * @author Jovi
     * @date 2017年8月18日上午8:55:19
     */
    public String getInsertTable() {
        return insertTable;
    }

    /**
     * 设置插入表名
     *
     * @param insertTable
     * @author Jovi
     * @date 2017年8月18日上午8:55:06
     */
    public void setInsertTable(String insertTable) {
        this.insertTable = insertTable;
    }

    /**
     * 设置插入表名
     *
     * @param clazz
     * @author Jovi
     * @date 2018年3月30日上午10:38:39
     */
    public void setUpdateTable(Class<?> clazz) {
        SqlBeanTable sqlBeanTable = clazz.getAnnotation(SqlBeanTable.class);
        if (sqlBeanTable != null) {
            this.insertTable = sqlBeanTable.value();
        } else {
            this.insertTable = clazz.getSimpleName();
        }
    }

    /**
     * 获取插入实体类
     *
     * @return
     * @author Jovi
     * @date 2017年8月18日上午8:55:19
     */
    public Object getInsertBean() {
        return insertBean;
    }

    /**
     * 设置插入实体类
     *
     * @param insertBean
     * @author Jovi
     * @date 2017年8月18日上午8:55:06
     */
    public void setInsertBean(Object insertBean) {
        this.insertBean = insertBean;
    }

}