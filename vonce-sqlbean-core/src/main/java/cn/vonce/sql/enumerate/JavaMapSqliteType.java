package cn.vonce.sql.enumerate;

import cn.vonce.sql.bean.Alter;
import cn.vonce.sql.bean.Table;
import cn.vonce.sql.config.SqlBeanDB;
import cn.vonce.sql.constant.SqlConstant;
import cn.vonce.sql.uitls.StringUtil;

import java.math.BigDecimal;
import java.util.List;

/**
 * Java类型对应的SQLite类型枚举类
 *
 * @author Jovi
 * @version 1.0
 * @email 766255988@qq.com
 */
public enum JavaMapSqliteType {

    NULL(new Class[]{}),
    INTEGER(new Class[]{boolean.class, Boolean.class, byte.class, Byte.class, short.class, Short.class, int.class, Integer.class, long.class, Long.class}),
    REAL(new Class[]{float.class, Float.class, double.class, Double.class, BigDecimal.class}),
    TEXT(new Class[]{java.sql.Clob.class, char.class, Character.class, String.class, java.sql.Date.class, java.sql.Time.class, java.sql.Timestamp.class, java.util.Date.class}),
    BLOB(new Class[]{java.sql.Blob.class, Object.class});

    JavaMapSqliteType(Class<?>[] classes) {
        this.classes = classes;
    }

    private Class<?>[] classes;

    public static JavaMapSqliteType getType(Class<?> clazz) {
        for (JavaMapSqliteType javaType : values()) {
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
        sql.append("SELECT name ");
        sql.append("FROM sqlite_master ");
        sql.append("WHERE type='table'");
        if (StringUtil.isNotEmpty(tableName)) {
            sql.append(" AND name = '" + tableName + "'");
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
        sql.append("pragma table_info('");
        sql.append(tableName);
        sql.append("')");
        return sql.toString();
    }

    /**
     * 更改表结构
     *
     * @param alterList
     * @return
     */
    public static String[] alterTable(List<Alter> alterList) {
        Table table = alterList.get(0).getTable();
        Class<?> beanClass = alterList.get(0).getBeanClass();
        StringBuffer sql = new StringBuffer();
        sql.append(SqlConstant.ALTER_TABLE);
        sql.append(SqlConstant.DOUBLE_ESCAPE_CHARACTER);
        sql.append(table.getName());
        sql.append(SqlConstant.DOUBLE_ESCAPE_CHARACTER);
        sql.append(SqlConstant.SPACES);
        sql.append(SqlConstant.RENAME);
        sql.append(SqlConstant.COLUMN);
        return null;
    }

}
