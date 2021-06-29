package cn.vonce.sql.bean;

import cn.vonce.sql.config.SqlBeanDB;
import cn.vonce.sql.uitls.SqlBeanUtil;

import java.io.Serializable;

/**
 * Common
 *
 * @author Jovi
 * @version 1.0
 * @email 766255988@qq.com
 * @date 2017年8月18日上午9:00:19
 */
public class Common implements Serializable {

    private static final long serialVersionUID = 1L;

    private SqlBeanDB sqlBeanDB = null;
    private Table table = new Table();
//    private Class<?> beanClass;

    public SqlBeanDB getSqlBeanDB() {
        return sqlBeanDB;
    }

    public void setSqlBeanDB(SqlBeanDB sqlBeanDB) {
        this.sqlBeanDB = sqlBeanDB;
    }

    /**
     * 获取table
     *
     * @return
     */
    public Table getTable() {
        return table;
    }

    /**
     * 设置table
     *
     * @param name
     */
    public void setTable(String name) {
        this.table.setName(name);
        this.table.setAlias(name);
    }

    /**
     * 设置table
     *
     * @param name
     * @param aliasName
     */
    public void setTable(String name, String aliasName) {
        this.table.setName(name);
        this.table.setAlias(aliasName);
    }

    /**
     * 设置table
     *
     * @param name
     * @param aliasName
     */
    public void setTable(String schema, String name, String aliasName) {
        this.table.setSchema(schema);
        this.table.setName(name);
        this.table.setAlias(aliasName);
    }

    /**
     * 设置table
     *
     * @param table
     */
    public void setTable(Table table) {
        this.table = table;
    }

    /**
     * 设置table sql 内容
     *
     * @param clazz 表对应的实体类
     */
    public void setTable(Class<?> clazz) {
        this.table = SqlBeanUtil.getTable(clazz);
//        this.beanClass = clazz;
    }

//    /**
//     * 获得当前操作的关联实体类
//     *
//     * @return
//     */
//    public Class<?> getBeanClass() {
//        return beanClass;
//    }
//
//    /**
//     * 设置当前操作的关联实体类
//     *
//     * @param beanClass
//     */
//    public void setBeanClass(Class<?> beanClass) {
//        this.beanClass = beanClass;
//    }

}
