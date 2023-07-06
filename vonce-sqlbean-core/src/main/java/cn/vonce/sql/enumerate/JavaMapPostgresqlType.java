package cn.vonce.sql.enumerate;

import cn.vonce.sql.config.SqlBeanDB;
import cn.vonce.sql.exception.SqlBeanException;
import cn.vonce.sql.uitls.SqlBeanUtil;
import cn.vonce.sql.uitls.StringUtil;

import java.lang.reflect.Field;
import java.math.BigDecimal;

/**
 * Java类型对应的Oracle类型枚举类
 *
 * @author Jovi
 * @email imjovi@qq.com
 * @date 2022/8/22 16:28
 */
public enum JavaMapPostgresqlType {

    INTEGER(new Class[]{int.class, Integer.class}),
    BIGINT(new Class[]{long.class, Long.class}),
    SMALLINT(new Class[]{byte.class, Byte.class, short.class, Short.class}),
    FLOAT(new Class[]{float.class, Float.class}),
    DOUBLE(new Class[]{double.class, Double.class}),
    NUMERIC(new Class[]{BigDecimal.class}),
    CHAR(new Class[]{char.class, Character.class}),
    VARCHAR(new Class[]{String.class}),
    BIT(new Class[]{boolean.class, Boolean.class}),
    DATE(new Class[]{java.sql.Date.class, java.time.LocalDate.class}),
    TIME(new Class[]{java.sql.Time.class, java.time.LocalTime.class}),
    TIMESTAMP(new Class[]{java.sql.Timestamp.class, java.util.Date.class, java.time.LocalDateTime.class}),
    TEXT(new Class[]{java.sql.Clob.class}),
    BYTEA(new Class[]{java.sql.Blob.class, Object.class});


    JavaMapPostgresqlType(Class<?>[] classes) {
        this.classes = classes;
    }

    private Class<?>[] classes;

    public static JavaMapPostgresqlType getType(Field field) {
        Class<?> clazz = SqlBeanUtil.getEntityClassFieldType(field);
        for (JavaMapPostgresqlType javaType : values()) {
            for (Class<?> thisClazz : javaType.classes) {
                if (thisClazz == clazz || thisClazz.isAssignableFrom(clazz)) {
                    return javaType;
                }
            }
        }
        throw new SqlBeanException(field.getDeclaringClass().getName() + "实体类不支持此字段类型：" + clazz.getSimpleName());
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
        sql.append("SELECT pt.tablename AS \"name\", pd.description AS remarks ");
        sql.append("FROM pg_tables pt ");
        sql.append("INNER JOIN pg_class pc ON pc.relname = pt.tablename ");
        sql.append("INNER JOIN pg_namespace pn ON pn.oid = pc.relnamespace AND pn.nspname = pt.schemaname ");
        sql.append("LEFT JOIN pg_description pd ON pd.objoid = pc.oid AND pd.objsubid = 0 ");
        sql.append("WHERE pt.schemaname = ");
        if (StringUtil.isNotEmpty(schema)) {
            sql.append("'" + schema + "'");
        } else {
            sql.append("current_schema()");
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
        sql.append("SELECT cl.ordinal_position as cid, cl.column_name as name, cl.data_type as type, ");
        sql.append("CASE WHEN cl.is_nullable = 'NO' THEN 1 ELSE 0 END as notnull, ");
        sql.append("cl.column_default as dflt_value, cl.character_maximum_length as length, cl.numeric_scale as scale, ");
        sql.append("CASE WHEN tc.constraint_type = 'PRIMARY KEY' THEN 1 ELSE 0 END AS pk, ");
        sql.append("CASE WHEN tc.constraint_type = 'FOREIGN KEY' THEN 1 ELSE 0 END AS fk, ");
        sql.append("(SELECT pd.description ");
        sql.append("FROM pg_description pd ");
        sql.append("INNER JOIN pg_class pc ON pc.oid = pd.objoid ");
        sql.append("INNER JOIN pg_namespace pn ON pn.oid = pc.relnamespace ");
        sql.append("WHERE pn.nspname = cl.table_schema AND pc.relname = cl.table_name AND pd.objsubid = cl.ordinal_position) AS remarks ");
        sql.append("FROM information_schema.columns cl ");
        sql.append("LEFT JOIN information_schema.key_column_usage kcu ON kcu.table_name = cl.table_name AND kcu.column_name = cl.column_name AND kcu.table_schema = cl.table_schema ");
        sql.append("LEFT JOIN information_schema.table_constraints tc ON tc.constraint_name = kcu.constraint_name AND tc.table_schema = cl.table_schema ");
        sql.append("WHERE cl.table_schema = ");
        if (StringUtil.isNotEmpty(schema)) {
            sql.append("'" + schema + "'");
        } else {
            sql.append("current_schema()");
        }
        if (StringUtil.isNotEmpty(tableName)) {
            sql.append(" AND cl.table_name = '" + tableName + "'");
        }
        return sql.toString();
    }

}
