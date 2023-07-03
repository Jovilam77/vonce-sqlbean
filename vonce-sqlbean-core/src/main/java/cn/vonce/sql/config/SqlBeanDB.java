package cn.vonce.sql.config;

import cn.vonce.sql.enumerate.DbType;

import java.io.Serializable;

/**
 * 数据库信息
 *
 * @author Jovi
 * @version 1.0
 * @email imjovi@qq.com
 */
public class SqlBeanDB implements Serializable {

    /**
     * 数据库类型
     */
    private DbType dbType;
    /**
     * 数据库名称
     */
    private String productName;
    /**
     * 数据库主版本
     */
    private int databaseMajorVersion;
    /**
     * 数据库小版本
     */
    private int databaseMinorVersion;
    /**
     * 数据库产品版本
     */
    private String databaseProductVersion;
    /**
     * jdbc驱动主版本
     */
    private int jdbcMajorVersion;
    /**
     * jdbc驱动小版本
     */
    private int jdbcMinorVersion;
    /**
     * 驱动
     */
    private int driverMajorVersion;
    /**
     * 驱动
     */
    private int driverMinorVersion;
    /**
     * 驱动版本
     */
    private String driverVersion;
    /**
     * 驱动名称
     */
    private String driverName;
    /**
     * 数据库配置
     */
    private SqlBeanConfig sqlBeanConfig;

    public DbType getDbType() {
        return dbType;
    }

    public void setDbType(DbType dbType) {
        if (this.dbType == null) {
            this.dbType = dbType;
        }
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getDatabaseMajorVersion() {
        return databaseMajorVersion;
    }

    public void setDatabaseMajorVersion(int databaseMajorVersion) {
        this.databaseMajorVersion = databaseMajorVersion;
    }

    public int getDatabaseMinorVersion() {
        return databaseMinorVersion;
    }

    public void setDatabaseMinorVersion(int databaseMinorVersion) {
        this.databaseMinorVersion = databaseMinorVersion;
    }

    public String getDatabaseProductVersion() {
        return databaseProductVersion;
    }

    public void setDatabaseProductVersion(String databaseProductVersion) {
        this.databaseProductVersion = databaseProductVersion;
    }

    public int getJdbcMajorVersion() {
        return jdbcMajorVersion;
    }

    public void setJdbcMajorVersion(int jdbcMajorVersion) {
        this.jdbcMajorVersion = jdbcMajorVersion;
    }

    public int getJdbcMinorVersion() {
        return jdbcMinorVersion;
    }

    public void setJdbcMinorVersion(int jdbcMinorVersion) {
        this.jdbcMinorVersion = jdbcMinorVersion;
    }

    public int getDriverMajorVersion() {
        return driverMajorVersion;
    }

    public void setDriverMajorVersion(int driverMajorVersion) {
        this.driverMajorVersion = driverMajorVersion;
    }

    public int getDriverMinorVersion() {
        return driverMinorVersion;
    }

    public void setDriverMinorVersion(int driverMinorVersion) {
        this.driverMinorVersion = driverMinorVersion;
    }

    public String getDriverVersion() {
        return driverVersion;
    }

    public void setDriverVersion(String driverVersion) {
        this.driverVersion = driverVersion;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public SqlBeanConfig getSqlBeanConfig() {
        return sqlBeanConfig;
    }

    public void setSqlBeanConfig(SqlBeanConfig sqlBeanConfig) {
        if (this.sqlBeanConfig == null) {
            this.sqlBeanConfig = sqlBeanConfig;
        }
    }

    @Override
    public String toString() {
        return "SqlBeanDB{" +
                "dbType=" + dbType +
                ", productName='" + productName + '\'' +
                ", databaseMajorVersion=" + databaseMajorVersion +
                ", databaseMinorVersion=" + databaseMinorVersion +
                ", databaseProductVersion='" + databaseProductVersion + '\'' +
                ", jdbcMajorVersion=" + jdbcMajorVersion +
                ", jdbcMinorVersion=" + jdbcMinorVersion +
                ", driverMajorVersion=" + driverMajorVersion +
                ", driverMinorVersion=" + driverMinorVersion +
                ", driverVersion='" + driverVersion + '\'' +
                ", driverName='" + driverName + '\'' +
                ", sqlBeanConfig=" + sqlBeanConfig +
                '}';
    }

}
