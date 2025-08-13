package cn.vonce.sql.enumerate;

import cn.vonce.sql.dialect.*;
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

    MySQL(new MysqlDialect()),
    MariaDB(new MysqlDialect()),
    SQLServer(new SqlServerDialect()),
    Oracle(new OracleDialect()),
    Postgresql(new PostgresqlDialect()),
    DB2(new DB2Dialect()),
    H2(new H2Dialect()),
    Hsql(new HsqlDialect()),
    Derby(new DerbyDialect()),
    SQLite(new SqliteDialect());

    private SqlDialect sqlDialect;

    DbType(SqlDialect sqlDialect) {
        this.sqlDialect = sqlDialect;
    }

    public SqlDialect getSqlDialect() {
        return sqlDialect;
    }

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
            } else if (productName.indexOf(DbType.Postgresql.name().toLowerCase()) >= 0) {
                return DbType.Postgresql;
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
