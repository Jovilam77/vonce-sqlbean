package cn.vonce.sql.enumerate;

import cn.vonce.sql.config.SqlBeanDB;
import cn.vonce.sql.uitls.StringUtil;

import java.math.BigDecimal;

/**
 * Java类型对应的Derby类型枚举类
 *
 * @author Jovi
 * @email imjovi@qq.com
 * @date 2022/8/12 14:42
 */
public enum JavaMapHsqlType {

    INTEGER(new Class[]{int.class, Integer.class}),
    BIGINT(new Class[]{long.class, Long.class}),
    TINYINT(new Class[]{byte.class, Byte.class}),
    SMALLINT(new Class[]{short.class, Short.class}),
    BOOLEAN(new Class[]{boolean.class, Boolean.class}),
    FLOAT(new Class[]{float.class, Float.class}),
    DOUBLE(new Class[]{double.class, Double.class}),
    DECIMAL(new Class[]{BigDecimal.class}),
    CHAR(new Class[]{char.class, Character.class}),
    VARCHAR(new Class[]{String.class}),
    DATE(new Class[]{java.sql.Date.class}),
    TIME(new Class[]{java.sql.Time.class}),
    TIMESTAMP(new Class[]{java.sql.Timestamp.class, java.util.Date.class}),
    CLOB(new Class[]{java.sql.Clob.class}),
    BLOB(new Class[]{java.sql.Blob.class, Object.class}),
    ARRAY(new Class[]{Object[].class});

    JavaMapHsqlType(Class<?>[] classes) {
        this.classes = classes;
    }

    private Class<?>[] classes;

    public static JavaMapHsqlType getType(Class<?> clazz) {
        for (JavaMapHsqlType javaType : values()) {
            for (Class<?> thisClazz : javaType.classes) {
                if (thisClazz == clazz) {
                    return javaType;
                }
            }
        }
        return null;
    }

    public static String getTypeName(Class<?> clazz) {
        return getType(clazz).name();
    }

    /**
     * 获取表数据列表的SQL
     *
     * @param sqlBeanDB
     * @param schema
     * @param tableName
     * @return
     */
    public static String getTableListSql(SqlBeanDB sqlBeanDB, String schema, String tableName) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT t.TABLE_SCHEMA AS schema, t.TABLE_NAME AS name, sc.COMMENT AS remarks ");
        sql.append("FROM information_schema.tables t ");
        sql.append("LEFT JOIN INFORMATION_SCHEMA.SYSTEM_COMMENTS sc ");
        sql.append("ON sc.OBJECT_NAME = t.TABLE_NAME AND sc.OBJECT_TYPE = 'TABLE' ");
        sql.append("WHERE TABLE_TYPE = 'BASE TABLE'");
        sql.append(" AND TABLE_SCHEMA = ");
        if (StringUtil.isNotEmpty(schema)) {
            sql.append("'" + schema + "'");
        } else {
            sql.append("'PUBLIC'");
        }
        if (StringUtil.isNotEmpty(tableName)) {
            sql.append(" AND table_name = '" + tableName + "'");
        }
        return sql.toString();
    }

    /**
     * 获取列数据列表的SQL
     *
     * @param sqlBeanDB
     * @param tableName
     * @return
     */
    public static String getColumnListSql(SqlBeanDB sqlBeanDB, String schema, String tableName) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT cl.ORDINAL_POSITION AS cid, ");
        sql.append("cl.COLUMN_NAME AS name,");
        sql.append("cl.DTD_IDENTIFIER AS type, ");
        sql.append("CASE WHEN cl.IS_NULLABLE  = 'NO' THEN 1 ELSE 0 END AS notnull, ");
        sql.append("cl.COLUMN_DEFAULT AS dflt_value, ");
        sql.append("cl.CHARACTER_MAXIMUM_LENGTH AS length, ");
        sql.append("cl.NUMERIC_SCALE AS scale, ");
        sql.append("CASE WHEN kcu.TABLE_NAME = cl.TABLE_NAME AND kcu.POSITION_IN_UNIQUE_CONSTRAINT is null THEN 1 ELSE 0 END AS pk, ");
        sql.append("CASE WHEN kcu.TABLE_NAME = cl.TABLE_NAME AND kcu.POSITION_IN_UNIQUE_CONSTRAINT = 1 THEN 1 ELSE 0 END AS fk, ");
        sql.append("sc.COMMENT AS remarks ");
        sql.append("FROM INFORMATION_SCHEMA.COLUMNS cl ");
        sql.append("LEFT JOIN INFORMATION_SCHEMA.SYSTEM_COMMENTS sc ");
        sql.append("ON sc.OBJECT_NAME = cl.TABLE_NAME AND sc.COLUMN_NAME = cl.COLUMN_NAME ");
        sql.append("LEFT JOIN INFORMATION_SCHEMA.KEY_COLUMN_USAGE kcu ");
        sql.append("ON kcu.TABLE_NAME = cl.TABLE_NAME AND kcu.COLUMN_NAME = cl.COLUMN_NAME ");
        sql.append("WHERE cl.TABLE_SCHEMA = ");
        if (StringUtil.isNotEmpty(schema)) {
            sql.append("'" + schema + "'");
        } else {
            sql.append("'PUBLIC'");
        }
        sql.append(" AND cl.TABLE_NAME = '");
        sql.append(tableName);
        sql.append("'");
        return sql.toString();
    }

}
