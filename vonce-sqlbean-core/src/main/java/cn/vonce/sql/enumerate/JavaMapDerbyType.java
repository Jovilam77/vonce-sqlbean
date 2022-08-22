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
public enum JavaMapDerbyType {

    INTEGER(new Class[]{int.class, Integer.class}),
    BIGINT(new Class[]{long.class, Long.class}),
    SMALLINT(new Class[]{byte.class, Byte.class, short.class, Short.class, boolean.class, Boolean.class}),
    FLOAT(new Class[]{float.class, Float.class}),
    DOUBLE(new Class[]{double.class, Double.class}),
    NUMERIC(new Class[]{BigDecimal.class}),
    CHAR(new Class[]{char.class, Character.class}),
    VARCHAR(new Class[]{String.class}),
    DATE(new Class[]{java.sql.Date.class}),
    TIME(new Class[]{java.sql.Time.class}),
    TIMESTAMP(new Class[]{java.sql.Timestamp.class, java.util.Date.class}),
    CLOB(new Class[]{java.sql.Clob.class}),
    BLOB(new Class[]{java.sql.Blob.class, Object.class});

    JavaMapDerbyType(Class<?>[] classes) {
        this.classes = classes;
    }

    private Class<?>[] classes;

    public static JavaMapDerbyType getType(Class<?> clazz) {
        for (JavaMapDerbyType javaType : values()) {
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
        sql.append("SELECT tb.TABLENAME AS \"name\", sc.SCHEMANAME AS \"schema\" ");
        sql.append("FROM SYS.SYSTABLES tb ");
        sql.append("INNER JOIN SYS.SYSSCHEMAS sc ");
        sql.append("ON sc.SCHEMAID = tb.SCHEMAID ");
        sql.append("WHERE TABLETYPE = 'T' AND sc.SCHEMANAME = ");
        if (StringUtil.isNotEmpty(schema)) {
            sql.append("'" + schema + "'");
        } else {
            sql.append("'SA'");
        }
        if (StringUtil.isNotEmpty(tableName)) {
            sql.append(" AND tablename = '" + tableName + "'");
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
        sql.append("SELECT cl.COLUMNNUMBER AS cid, cl.COLUMNNAME AS name, cl.COLUMNDATATYPE AS type, cl.COLUMNDEFAULT AS dflt_value, ");
        //由于Derby的缺陷无法查询是否为主键和外键，所以默认第一个字段为主键，其余字段都不是外键
        sql.append("(CASE WHEN cl.COLUMNNUMBER = 1 THEN '1' ELSE '0' END) AS pk, 0 AS pk ");
        sql.append("FROM SYS.SYSTABLES AS tb ");
        sql.append("INNER JOIN SYS.SYSCOLUMNS cl ");
        sql.append("ON cl.REFERENCEID = tb.TABLEID ");
        sql.append("INNER JOIN SYS.SYSSCHEMAS sc ");
        sql.append("ON sc.SCHEMAID = tb.SCHEMAID ");
        sql.append("WHERE sc.SCHEMANAME = ");
        if (StringUtil.isNotEmpty(schema)) {
            sql.append("'" + schema + "'");
        } else {
            sql.append("'SA'");
        }
        sql.append(" AND tb.TABLENAME = '");
        sql.append(tableName);
        sql.append("' ORDER BY cl.COLUMNNUMBER");
        return sql.toString();
    }

}
