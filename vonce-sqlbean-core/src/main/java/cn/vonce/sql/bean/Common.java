package cn.vonce.sql.bean;

import cn.vonce.sql.config.SqlBeanDB;

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

    public SqlBeanDB getSqlBeanDB() {
        return sqlBeanDB;
    }

    public void setSqlBeanDB(SqlBeanDB sqlBeanDB) {
        this.sqlBeanDB = sqlBeanDB;
    }

}
