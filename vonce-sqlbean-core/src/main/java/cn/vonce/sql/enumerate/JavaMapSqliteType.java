package cn.vonce.sql.enumerate;

import cn.vonce.sql.annotation.SqlTable;
import cn.vonce.sql.bean.*;
import cn.vonce.sql.config.SqlBeanDB;
import cn.vonce.sql.constant.SqlConstant;
import cn.vonce.sql.helper.SqlHelper;
import cn.vonce.sql.uitls.DateUtil;
import cn.vonce.sql.uitls.SqlBeanUtil;
import cn.vonce.sql.uitls.StringUtil;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.*;

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
    TEXT(new Class[]{java.sql.Clob.class, char.class, Character.class, String.class, java.sql.Date.class, java.sql.Time.class, java.sql.Timestamp.class, java.util.Date.class, java.time.LocalDateTime.class, java.time.LocalDate.class, java.time.LocalTime.class}),
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
    public static List<String> alterTable(List<Alter> alterList) {
        List<String> sqlList = new ArrayList<>();
        SqlBeanDB sqlBeanDB = alterList.get(0).getSqlBeanDB();
        Table table = alterList.get(0).getTable();
        Class<?> beanClass = alterList.get(0).getBeanClass();
        SqlTable sqlTable = beanClass.getAnnotation(SqlTable.class);
        //备份表
        String oldTableName = SqlConstant.UNDERLINE + table.getName() + SqlConstant.UNDERLINE + "old" + SqlConstant.UNDERLINE + DateUtil.dateToString(new Date(), "yyyyMMddHHmmss");
        StringBuffer alterTableSql = new StringBuffer();
        alterTableSql.append(SqlConstant.ALTER_TABLE);
        alterTableSql.append(SqlConstant.DOUBLE_ESCAPE_CHARACTER);
        alterTableSql.append(table.getName());
        alterTableSql.append(SqlConstant.DOUBLE_ESCAPE_CHARACTER);
        alterTableSql.append(SqlConstant.SPACES);
        alterTableSql.append(SqlConstant.RENAME_TO);
        alterTableSql.append(SqlConstant.DOUBLE_ESCAPE_CHARACTER);
        alterTableSql.append(oldTableName);
        alterTableSql.append(SqlConstant.DOUBLE_ESCAPE_CHARACTER);
        sqlList.add(alterTableSql.toString());
        //创建表
        Create create = new Create();
        create.setSqlBeanDB(sqlBeanDB);
        create.setBeanClass(beanClass);
        create.setTable(beanClass);
        sqlList.add(SqlHelper.buildCreateSql(create));
        //复制表数据
        Map<String, Alter> alterMap = new HashMap<>();
        for (Alter alter : alterList) {
            alterMap.put(alter.getColumnInfo().getName(), alter);
        }
        List<Column> columnList = new ArrayList<>();
        List<Column> targetColumnList = new ArrayList<>();
        List<Field> fieldList = SqlBeanUtil.getBeanAllField(beanClass);
        for (Field field : fieldList) {
            if (SqlBeanUtil.isIgnore(field)) {
                continue;
            }
            String targetColumnName;
            String columnName = SqlBeanUtil.getTableFieldName(field, sqlTable);
            Alter alter = alterMap.get(columnName);
            if (alter != null && (alter.getType() == AlterType.ADD || alter.getType() == AlterType.DROP)) {
                continue;
            }
            //如果CHANGE说明是改字段名
            if (alter != null && alter.getType() == AlterType.CHANGE) {
                targetColumnName = alter.getOldColumnName();
            } else {
                targetColumnName = columnName;
            }
            //新建表的字段
            Column column = new Column();
            column.setTableAlias(table.getAlias());
            column.setName(columnName);
            column.setAlias(column.getName());
            columnList.add(column);
            //备份表的字段
            Column targetColumn = new Column();
            targetColumn.setTableAlias(table.getAlias());
            targetColumn.setName(targetColumnName);
            targetColumn.setAlias(column.getName());
            targetColumnList.add(targetColumn);
        }
        Copy copy = new Copy();
        copy.setSqlBeanDB(sqlBeanDB);
        copy.setBeanClass(beanClass);
        copy.setTable(beanClass);
        copy.setColumns(columnList.toArray(new Column[]{}));
        copy.setTargetTableName(oldTableName);
        copy.setTargetColumns(targetColumnList.toArray(new Column[]{}));
        sqlList.add(SqlHelper.buildCopy(copy));
        return sqlList;
    }

}
