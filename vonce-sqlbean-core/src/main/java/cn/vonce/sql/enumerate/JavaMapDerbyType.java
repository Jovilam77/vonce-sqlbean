package cn.vonce.sql.enumerate;

import cn.vonce.sql.bean.Alter;
import cn.vonce.sql.bean.ColumnInfo;
import cn.vonce.sql.bean.Table;
import cn.vonce.sql.config.SqlBeanDB;
import cn.vonce.sql.constant.SqlConstant;
import cn.vonce.sql.exception.SqlBeanException;
import cn.vonce.sql.uitls.SqlBeanUtil;
import cn.vonce.sql.uitls.StringUtil;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
    DATE(new Class[]{java.sql.Date.class, java.time.LocalDate.class}),
    TIME(new Class[]{java.sql.Time.class, java.time.LocalTime.class}),
    TIMESTAMP(new Class[]{java.sql.Timestamp.class, java.util.Date.class, java.time.LocalDateTime.class}),
    CLOB(new Class[]{java.sql.Clob.class}),
    BLOB(new Class[]{java.sql.Blob.class, Object.class});

    JavaMapDerbyType(Class<?>[] classes) {
        this.classes = classes;
    }

    private Class<?>[] classes;

    public static JavaMapDerbyType getType(Class<?> clazz) {
        if (clazz.isEnum() && SqlEnum.class.isAssignableFrom(clazz)) {
            Type[] typeArray = clazz.getGenericInterfaces();
            clazz = SqlBeanUtil.getGenericType(typeArray);
            if (clazz == null || !SqlBeanUtil.isBaseType(clazz)) {
                return JavaMapDerbyType.INTEGER;
            }
        }
        for (JavaMapDerbyType javaType : values()) {
            for (Class<?> thisClazz : javaType.classes) {
                if (thisClazz == clazz) {
                    return javaType;
                }
            }
        }
        throw new SqlBeanException("该字段类型不支持：" + clazz.getName());
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

    /**
     * 更改表结构
     *
     * @param alterList
     * @return
     */
    public static List<String> alterTable(List<Alter> alterList) {
        List<String> sqlList = new ArrayList<>();
        for (int i = 0; i < alterList.size(); i++) {
            Alter alter = alterList.get(i);
            if (alter.getType() == AlterType.ADD) {
                StringBuffer sql = new StringBuffer();
                sql.append(SqlConstant.ALTER_TABLE);
                sql.append(getFullName(alter, alter.getTable(), null));
                sql.append(SqlConstant.ADD);
                sql.append(SqlConstant.COLUMN);
                sql.append(SqlBeanUtil.addColumn(alter, alter.getColumnInfo(), alter.getAfterColumnName()));
                sqlList.add(sql.toString());
            } else if (alter.getType() == AlterType.CHANGE) {
                sqlList.add(changeColumn(alter));
                //先改名后修改信息
                StringBuffer modifySql = modifyColumn(alter);
                if (modifySql.length() > 0) {
                    sqlList.add(SqlConstant.ALTER_TABLE + modifySql);
                }
            } else if (alter.getType() == AlterType.MODIFY) {
                sqlList.add(SqlConstant.ALTER_TABLE + modifyColumn(alter));
            } else if (alter.getType() == AlterType.DROP) {
                StringBuffer sql = new StringBuffer();
                sql.append(SqlConstant.ALTER_TABLE);
                sql.append(getFullName(alter, alter.getTable(), null));
                sql.append(SqlConstant.DROP);
                sql.append(SqlConstant.COLUMN);
                sql.append(SqlBeanUtil.isToUpperCase(alter) ? alter.getColumnInfo().getName().toUpperCase() : alter.getColumnInfo().getName());
                sqlList.add(sql.toString());
            }
        }
        return sqlList;
    }

    /**
     * 获取全名
     *
     * @param alter
     * @param table
     * @return
     */
    private static String getFullName(Alter alter, Table table, String columnName) {
        boolean rename = alter.getType() == AlterType.CHANGE && StringUtil.isNotBlank(columnName);
        boolean toUpperCase = SqlBeanUtil.isToUpperCase(alter);
        StringBuffer sql = new StringBuffer();
        if (StringUtil.isNotBlank(table.getSchema())) {
            sql.append(toUpperCase ? table.getSchema().toUpperCase() : table.getSchema());
            sql.append(SqlConstant.POINT);
        }
        sql.append(toUpperCase ? table.getName().toUpperCase() : table.getName());
        if (rename) {
            sql.append(SqlConstant.POINT);
            sql.append(SqlBeanUtil.isToUpperCase(alter) ? columnName : columnName);
        }
        sql.append(SqlConstant.SPACES);
        return sql.toString();
    }

    /**
     * 更改列信息
     *
     * @param alter
     * @return
     */
    private static StringBuffer modifyColumn(Alter alter) {
        ColumnInfo columnInfo = alter.getColumnInfo();
        StringBuffer modifySql = new StringBuffer();
        modifySql.append(getFullName(alter, alter.getTable(), null));
        modifySql.append(SqlConstant.ALTER);
        modifySql.append(SqlConstant.COLUMN);
        modifySql.append(SqlBeanUtil.getTableFieldName(alter, columnInfo.getName()));
        //是否为null
        if (columnInfo.getNotnull() != null && columnInfo.getNotnull()) {
            modifySql.append(SqlConstant.SPACES);
            modifySql.append(SqlConstant.NOT_NULL);
        } else if (alter.getType() == AlterType.MODIFY && columnInfo.getNotnull() != null && !columnInfo.getNotnull()) {
            modifySql.append(SqlConstant.SPACES);
            modifySql.append(SqlConstant.NULL);
        }
        return modifySql;
    }

    /**
     * 更改字段名
     *
     * @param alter
     * @return
     */
    private static String changeColumn(Alter alter) {
        StringBuffer changeSql = new StringBuffer();
        changeSql.append(SqlConstant.RENAME);
        changeSql.append(SqlConstant.COLUMN);
        changeSql.append(getFullName(alter, alter.getTable(), alter.getOldColumnName()));
        changeSql.append(SqlConstant.TO);
        changeSql.append(SqlBeanUtil.isToUpperCase(alter) ? alter.getColumnInfo().getName().toUpperCase() : alter.getColumnInfo().getName());
        return changeSql.toString();
    }

}
