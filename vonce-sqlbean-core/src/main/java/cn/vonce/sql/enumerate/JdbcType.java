package cn.vonce.sql.enumerate;

import cn.vonce.sql.exception.SqlBeanException;

import java.lang.reflect.Field;

/**
 * JdbcType枚举类
 *
 * @author Jovi
 * @version 1.0
 * @email 766255988@qq.com
 */
public enum JdbcType {

    NOTHING(0, 0),
    TINYINT(0, 0),
    SMALLINT(0, 0),
    INT(0, 0),
    INTEGER(0, 0),
    BOOLEAN(0, 0),
    BIGINT(0, 0),
    LONG(0, 0),
    NUMBER(0, 0),
    FLOAT(0, 0),
    DOUBLE(0, 0),
    DECIMAL(10, 2),
    REAL(10, 2),
    NUMERIC(10, 2),
    MONEY(10, 4),
    SMALLMONEY(10, 4),
    DATE(0, 0),
    TIME(0, 0),
    DATETIME(0, 0),
    DATETIME2(0, 0),
    TIMESTAMP(0, 0),
    CHAR(1, 0),
    NCHAR(1, 0),
    VARCHAR(255, 0),
    VARCHAR2(255, 0),
    NVARCHAR(255, 0),
    TINYTEXT(0, 0),
    TEXT(0, 0),
    NTEXT(0, 0),
    LONGTEXT(0, 0),
    BIT(0, 0),
    CLOB(0, 0),
    NCLOB(0, 0),
    BLOB(0, 0),
    LONGBLOB(0, 0),
    NBLOB(0, 0),
    ARRAY(0, 0),
    IMAGE(0, 0),
    BYTEA(0, 0);


    JdbcType(int length, int scale) {
        this.length = length;
        this.scale = scale;
    }

    private int length;
    private int scale;

    public static JdbcType getType(String name) {
        for (JdbcType jdbcType : values()) {
            if (jdbcType.name().equals(name)) {
                return jdbcType;
            }
        }
        return null;
    }

    public static JdbcType getType(DbType dbType, Field clazz) {
        JdbcType jdbcType;
        switch (dbType) {
            case MySQL:
            case MariaDB:
                jdbcType = JdbcType.getType(JavaMapMySqlType.getType(clazz.getType()).name());
                break;
            case SQLServer:
                jdbcType = JdbcType.getType(JavaMapSqlServerType.getType(clazz.getType()).name());
                break;
            case Oracle:
                jdbcType = JdbcType.getType(JavaMapOracleType.getType(clazz.getType()).name());
                break;
            case Postgresql:
                jdbcType = JdbcType.getType(JavaMapPostgresqlType.getType(clazz.getType()).name());
                break;
            case DB2:
                jdbcType = JdbcType.getType(JavaMapDB2Type.getType(clazz.getType()).name());
                break;
            case H2:
                jdbcType = JdbcType.getType(JavaMapH2Type.getType(clazz.getType()).name());
                break;
            case Hsql:
                jdbcType = JdbcType.getType(JavaMapHsqlType.getType(clazz.getType()).name());
                break;
            case Derby:
                jdbcType = JdbcType.getType(JavaMapDerbyType.getType(clazz.getType()).name());
                break;
            case SQLite:
                jdbcType = JdbcType.getType(JavaMapSqliteType.getType(clazz.getType()).name());
                break;
            default:
                throw new SqlBeanException("请配置正确的数据库");
        }
        return jdbcType;
    }

    public boolean isFloat() {
        if (this.ordinal() == NUMERIC.ordinal() || this.ordinal() == DECIMAL.ordinal() || this.ordinal() == FLOAT.ordinal() || this.ordinal() == DOUBLE.ordinal() || this.ordinal() == MONEY.ordinal() || this.ordinal() == SMALLMONEY.ordinal()) {
            return true;
        }
        return false;
    }

    public int getLength() {
        return length;
    }

    public int getScale() {
        return scale;
    }

    public String getName(Boolean toUpperCase) {
        return toUpperCase != null && toUpperCase ? name() : name().toLowerCase();
    }

}
