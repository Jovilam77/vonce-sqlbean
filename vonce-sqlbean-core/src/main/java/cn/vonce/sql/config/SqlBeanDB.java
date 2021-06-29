package cn.vonce.sql.config;

import cn.vonce.sql.enumerate.DbType;

/**
 * 数据库信息
 *
 * @author Jovi
 * @version 1.0
 * @email 766255988@qq.com
 */
public class SqlBeanDB {

    /**
     * 数据库类型
     */
    private DbType dbType;
    /**
     * 数据库配置
     */
    private SqlBeanConfig sqlBeanConfig;

    public DbType getDbType() {
        return dbType;
    }

    public void setDbType(DbType dbType) {
        this.dbType = dbType;
    }

    public SqlBeanConfig getSqlBeanConfig() {
        return sqlBeanConfig;
    }

    public void setSqlBeanConfig(SqlBeanConfig sqlBeanConfig) {
        this.sqlBeanConfig = sqlBeanConfig;
    }
}
