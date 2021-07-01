package cn.vonce.sql.enumerate;

import cn.vonce.sql.uitls.StringUtil;

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

    public static DbType getDbType(String productName) {
        if (StringUtil.isNotEmpty(productName)) {
            productName = productName.replace(" ", "").toLowerCase();
            if (productName.indexOf(DbType.MySQL.name().toLowerCase()) >= 0) {
                return DbType.MySQL;
            } else if (productName.indexOf(DbType.MariaDB.name().toLowerCase()) >= 0) {
                return DbType.MariaDB;
            } else if (productName.indexOf(DbType.Oracle.name().toLowerCase()) >= 0) {
                return DbType.Oracle;
            } else if (productName.indexOf(DbType.SQLServer.name().toLowerCase()) >= 0) {
                return DbType.SQLServer;
            } else if (productName.indexOf(DbType.PostgreSQL.name().toLowerCase()) >= 0) {
                return DbType.PostgreSQL;
            } else if (productName.indexOf(DbType.DB2.name().toLowerCase()) >= 0) {
                return DbType.DB2;
            } else if (productName.indexOf(DbType.Derby.name().toLowerCase()) >= 0) {
                return DbType.Derby;
            } else if (productName.indexOf(DbType.SQLite.name().toLowerCase()) >= 0) {
                return DbType.SQLite;
            } else if (productName.indexOf(DbType.Hsql.name().toLowerCase()) >= 0) {
                return DbType.Hsql;
            } else if (productName.indexOf(DbType.H2.name().toLowerCase()) >= 0) {
                return DbType.H2;
            }
        }
        return null;
    }

}
