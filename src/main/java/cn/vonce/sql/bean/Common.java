package cn.vonce.sql.bean;

import cn.vonce.sql.config.SqlBeanConfig;
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

    private Table table;
    private SqlBeanConfig sqlBeanConfig;

    /**
     * 获取table sql 内容
     *
     * @return
     * @author Jovi
     * @date 2017年8月18日上午8:59:49
     */
    public Table getTable() {
        return table;
    }

    /**
     * 设置table sql 内容
     *
     * @param name
     * @author Jovi
     * @date 2017年8月18日上午8:59:38
     */
    public void setTable(String name) {
        this.table.setName(name);
        this.table.setAlias(name);
    }

    /**
     * 设置table sql 内容
     *
     * @param name
     * @param aliasName
     * @author Jovi
     * @date 2017年8月18日上午8:59:38
     */
    public void setTable(String name, String aliasName) {
        this.table.setName(name);
        this.table.setAlias(aliasName);
    }

    /**
     * 设置table sql 内容
     *
     * @param table
     * @author Jovi
     * @date 2017年8月18日上午8:59:38
     */
    public void setTable(Table table) {
        this.table = table;
    }

    /**
     * 设置table sql 内容
     *
     * @param clazz 表对应的实体类
     * @author Jovi
     * @date 2018年5月14日下午11:54:45
     */
    public void setTable(Class<?> clazz) {
        this.table = SqlBeanUtil.getTable(clazz);
    }

    public SqlBeanConfig getSqlBeanConfig() {
        return sqlBeanConfig;
    }

    public void setSqlBeanConfig(SqlBeanConfig sqlBeanConfig) {
        this.sqlBeanConfig = sqlBeanConfig;
    }
}
