package cn.vonce.sql.enumerate;

import cn.vonce.sql.config.SqlBeanConfig;

/**
 * 数据库类型
 *
 * @author Jovi
 * @version 1.0
 * @email imjovi@qq.com
 * @date 2019年4月9日下午9:34:20
 */
public enum DbType {

    MySQL, MariaDB, SQLServer, Oracle, PostgreSQL, DB2, H2, Hsql, Derby, SQLite;

    public static SqlBeanConfig getSqlBeanConfig(String productName) {
        SqlBeanConfig sqlBeanConfig = null;
        if (DbType.MySQL.name().toLowerCase().equals(productName.toLowerCase())) {
            sqlBeanConfig = new SqlBeanConfig(DbType.MySQL);
        } else if (DbType.MariaDB.name().toLowerCase().equals(productName.toLowerCase())) {
            sqlBeanConfig = new SqlBeanConfig(DbType.MariaDB);
        } else if (DbType.Oracle.name().toLowerCase().equals(productName.toLowerCase())) {
            sqlBeanConfig = new SqlBeanConfig(DbType.Oracle);
            sqlBeanConfig.setToUpperCase(true);
        } else if (DbType.SQLServer.name().toLowerCase().equals(productName.toLowerCase())) {
            sqlBeanConfig = new SqlBeanConfig(DbType.SQLServer);
        } else if (DbType.PostgreSQL.name().toLowerCase().equals(productName.toLowerCase())) {
            sqlBeanConfig = new SqlBeanConfig(DbType.PostgreSQL);
        } else if (DbType.DB2.name().toLowerCase().equals(productName.toLowerCase())) {
            sqlBeanConfig = new SqlBeanConfig(DbType.DB2);
            sqlBeanConfig.setToUpperCase(true);
        } else if (DbType.Derby.name().toLowerCase().equals(productName.toLowerCase())) {
            sqlBeanConfig = new SqlBeanConfig(DbType.Derby);
            sqlBeanConfig.setToUpperCase(true);
        } else if (DbType.SQLite.name().toLowerCase().equals(productName.toLowerCase())) {
            sqlBeanConfig = new SqlBeanConfig(DbType.SQLite);
        } else if (DbType.Hsql.name().toLowerCase().equals(productName.toLowerCase())) {
            sqlBeanConfig = new SqlBeanConfig(DbType.Hsql);
            sqlBeanConfig.setToUpperCase(true);
        } else if (DbType.H2.name().toLowerCase().equals(productName.toLowerCase())) {
            sqlBeanConfig = new SqlBeanConfig(DbType.H2);
            sqlBeanConfig.setToUpperCase(true);
        }
        System.out.println("数据库产品名称：" + productName);
        return sqlBeanConfig;
    }

}
