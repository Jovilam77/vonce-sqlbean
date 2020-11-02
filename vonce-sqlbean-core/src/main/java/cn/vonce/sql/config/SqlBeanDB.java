package cn.vonce.sql.config;

import cn.vonce.sql.enumerate.DbType;

public class SqlBeanDB {

    private DbType dbType;
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
