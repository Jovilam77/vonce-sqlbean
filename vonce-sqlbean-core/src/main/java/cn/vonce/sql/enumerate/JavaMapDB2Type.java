package cn.vonce.sql.enumerate;

import cn.vonce.sql.config.SqlBeanDB;
import cn.vonce.sql.uitls.StringUtil;

import java.math.BigDecimal;

/**
 * Java类型对应的Oracle类型枚举类
 *
 * @author Jovi
 * @email imjovi@qq.com
 * @date 2022/8/22 16:28
 */
public enum JavaMapDB2Type {

    INTEGER(new Class[]{int.class, Integer.class}),
    BIGINT(new Class[]{long.class, Long.class}),
    SMALLINT(new Class[]{boolean.class, Boolean.class, byte.class, Byte.class, short.class, Short.class}),
    REAL(new Class[]{float.class, Float.class}),
    DOUBLE(new Class[]{double.class, Double.class}),
    DECIMAL(new Class[]{BigDecimal.class}),
    CHAR(new Class[]{char.class, Character.class}),
    VARCHAR(new Class[]{String.class}),
    DATE(new Class[]{java.sql.Date.class}),
    TIME(new Class[]{java.sql.Time.class}),
    TIMESTAMP(new Class[]{java.sql.Timestamp.class, java.util.Date.class}),
    CLOB(new Class[]{java.sql.Clob.class}),
    BLOB(new Class[]{java.sql.Blob.class, Object.class});


    JavaMapDB2Type(Class<?>[] classes) {
        this.classes = classes;
    }

    private Class<?>[] classes;

    public static JavaMapDB2Type getType(Class<?> clazz) {
        for (JavaMapDB2Type javaType : values()) {
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
     * select tabname AS \"name\" from syscat.tables where tabschema = current schema and tabname = 'TEST'
     *
     * @param sqlBeanDB
     * @param schema
     * @param tableName
     * @return
     */
    public static String getTableListSql(SqlBeanDB sqlBeanDB, String schema, String tableName) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT name ");
        sql.append("FROM sysibm.systables ");
        sql.append("WHERE type = 'T' ");
        sql.append("AND creator = current user");
        if (StringUtil.isNotEmpty(tableName)) {
            sql.append(" AND name = '" + tableName + "'");
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
        sql.append("SELECT col.column_id AS cid, col.column_name AS name, col.data_type AS type, ");
        sql.append("(CASE col.nullable WHEN 'N' THEN '1' ELSE '0' END) AS notnull, col.data_default AS dflt_value, ");
        sql.append("(CASE uc1.constraint_type WHEN 'P' THEN '1' ELSE '0' END) AS pk, ");
        sql.append("(CASE uc2.constraint_type WHEN 'R' THEN '1' ELSE '0' END) AS fk, ");
        sql.append("(CASE WHEN col.data_type = 'FLOAT' OR col.data_type = 'DOUBLE' OR col.data_type = 'DECIMAL' OR col.data_type = 'NUMBER' THEN col.data_precision ELSE col.char_length END) AS length, ");
        sql.append("col.data_scale AS scale, ");
        sql.append("user_col_comments.comments AS comm ");
        sql.append("FROM user_tab_columns col ");
        sql.append("LEFT JOIN user_cons_columns ucc ON ucc.table_name = col.table_name AND ucc.column_name = col.column_name AND ucc.position IS NOT NULL ");
        sql.append("LEFT JOIN user_constraints uc1 ON uc1.constraint_name = ucc.constraint_name AND uc1.constraint_type = 'P' ");
        sql.append("LEFT JOIN user_constraints uc2 ON uc2.constraint_name = ucc.constraint_name AND uc2.constraint_type = 'R' ");
        sql.append("INNER JOIN user_col_comments ON user_col_comments.table_name = col.table_name AND user_col_comments.column_name = col.column_name ");
        sql.append("WHERE col.table_name = '");
        sql.append(tableName);
        sql.append("'");
        return sql.toString();
    }

}
