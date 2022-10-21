package cn.vonce.sql.enumerate;

import cn.vonce.sql.bean.Alter;
import cn.vonce.sql.bean.Table;
import cn.vonce.sql.config.SqlBeanDB;
import cn.vonce.sql.constant.SqlConstant;
import cn.vonce.sql.uitls.SqlBeanUtil;
import cn.vonce.sql.uitls.StringUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Java类型对应的Oracle类型枚举类
 *
 * @author Jovi
 * @email imjovi@qq.com
 * @date 2022/8/22 16:28
 */
public enum JavaMapSqlServerType {

    INTEGER(new Class[]{int.class, Integer.class}), BIGINT(new Class[]{long.class, Long.class}), SMALLINT(new Class[]{short.class, Short.class}), REAL(new Class[]{float.class, Float.class}), FLOAT(new Class[]{double.class, Double.class}), NUMERIC(new Class[]{BigDecimal.class}), NCHAR(new Class[]{char.class, Character.class}), NVARCHAR(new Class[]{String.class}), TINYINT(new Class[]{byte.class, Byte.class}), BIT(new Class[]{boolean.class, Boolean.class}), DATE(new Class[]{java.sql.Date.class}), TIME(new Class[]{java.sql.Time.class}), DATETIME2(new Class[]{java.sql.Timestamp.class}), DATETIME(new Class[]{java.util.Date.class}), NTEXT(new Class[]{java.sql.Clob.class}), IMAGE(new Class[]{java.sql.Blob.class, Object.class});


    JavaMapSqlServerType(Class<?>[] classes) {
        this.classes = classes;
    }

    private Class<?>[] classes;

    public static JavaMapSqlServerType getType(Class<?> clazz) {
        for (JavaMapSqlServerType javaType : values()) {
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
        sql.append("SELECT o.name, p.value AS remarks ");
        sql.append("FROM sysobjects o ");
        sql.append("LEFT JOIN sys.extended_properties p ");
        sql.append("ON p.major_id = o.id AND p.minor_id = 0 ");
        sql.append("WHERE o.xtype='U'");
        if (StringUtil.isNotEmpty(tableName)) {
            sql.append(" AND o.name = '" + tableName + "'");
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
        sql.append("SELECT a.cid, a.name, a.type, (CASE a.notnull WHEN 0 THEN 1 ELSE 0 END) AS notnull, ");
        sql.append("(CASE LEFT(constraint_name, 2) WHEN 'PK' THEN 1 ELSE 0 END) AS pk, ");
        sql.append("(CASE LEFT(constraint_name, 2) WHEN 'FK' THEN 1 ELSE 0 END) AS fk, ");
        sql.append("a.length, a.scale, c.value AS remarks ");
        sql.append("FROM (");
        sql.append("SELECT syscolumns.id, syscolumns.colid AS cid, syscolumns.name AS name, syscolumns.length AS length, syscolumns.scale, systypes.name AS type, syscolumns.isnullable AS notnull, '");
        sql.append(tableName);
        sql.append("' AS table_name ");
        sql.append("FROM syscolumns, systypes ");
        sql.append("WHERE syscolumns.xusertype = systypes.xusertype AND syscolumns.id = object_id('");
        sql.append(tableName);
        sql.append("')) a ");
        sql.append("LEFT JOIN information_schema.key_column_usage b ON a.name = b.column_name AND a.table_name = b.table_name ");
        sql.append("LEFT JOIN sys.extended_properties c ON c.major_id = a.id AND c.minor_id = a.cid ");
        sql.append("ORDER BY a.cid");
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
        Table table = alterList.get(0).getTable();
        for (int i = 0; i < alterList.size(); i++) {
            Alter alter = alterList.get(i);
            StringBuffer sql = new StringBuffer();
            if (alter.getType() == AlterType.ADD) {
                sql.append(getFullName(alter, table, null));
                sql.append(SqlConstant.ADD);
                sql.append(SqlConstant.BEGIN_SQUARE_BRACKETS);
                sql.append(alter.getColumnInfo().getName());
                sql.append(SqlConstant.END_SQUARE_BRACKETS);
                sql.append(alter.getColumnInfo().getName());
                sql.append(SqlConstant.SPACES);
                sql.append(alter.getColumnInfo().getType());
                if (alter.getColumnInfo().getNotnull() != null && alter.getColumnInfo().getNotnull()) {
                    sql.append(SqlConstant.NOT_NULL);
                }
            } else if (alter.getType() == AlterType.MODIFY) {
                sql.append(getFullName(alter, table, null));
                sql.append(SqlConstant.EXEC_SP_RENAME);
                sql.append(SqlConstant.COLUMN);
                sql.append(SqlBeanUtil.addColumn(alter, alter.getColumnInfo(), alter.getAfterColumnName()));
            } else if (alter.getType() == AlterType.CHANGE) {
                sql.append(getFullName(alter, table, null));
                sql.append(SqlConstant.ALTER);
                sql.append(SqlConstant.COLUMN);
                sql.append(alter.getColumnInfo().getName());
                sql.append(alter.getColumnInfo().getType());
                if (alter.getColumnInfo().getNotnull() != null && alter.getColumnInfo().getNotnull()) {
                    sql.append(SqlConstant.NOT_NULL);
                }
            } else if (alter.getType() == AlterType.DROP) {
                sql.append(getFullName(alter, table, null));
                sql.append(SqlConstant.DROP);
                sql.append(SqlConstant.COLUMN);
                sql.append(alter.getColumnInfo().getName());
            }
            sql.append(SqlConstant.SPACES);
            if (i > alterList.size() - 1) {
                sql.append(SqlConstant.GO);
            }
            sqlList.add(sql.toString());
        }
        return sqlList;
    }

    /**
     * 获取全名
     *
     * @param alter
     * @param table
     * @param columnName
     * @return
     */
    private static String getFullName(Alter alter, Table table, String columnName) {
        StringBuffer sql = new StringBuffer();
        if (alter.getType() == AlterType.CHANGE) {
            sql.append(SqlConstant.EXEC_SP_RENAME);
        } else {
            sql.append(SqlConstant.ALTER_TABLE);
        }
        if (StringUtil.isNotBlank(table.getSchema())) {
            sql.append(SqlConstant.BEGIN_SQUARE_BRACKETS);
            sql.append(table.getSchema());
            sql.append(SqlConstant.END_SQUARE_BRACKETS);
            sql.append(SqlConstant.POINT);
        }
        sql.append(SqlConstant.BEGIN_SQUARE_BRACKETS);
        sql.append(SqlConstant.DBO);
        sql.append(SqlConstant.END_SQUARE_BRACKETS);
        sql.append(SqlConstant.POINT);
        sql.append(SqlConstant.BEGIN_SQUARE_BRACKETS);
        sql.append(table.getName());
        sql.append(SqlConstant.END_SQUARE_BRACKETS);
        if (alter.getType() == AlterType.CHANGE && StringUtil.isNotBlank(columnName)) {
            sql.append(SqlConstant.POINT);
            sql.append(SqlConstant.BEGIN_SQUARE_BRACKETS);
            sql.append(columnName);
            sql.append(SqlConstant.END_SQUARE_BRACKETS);
        }
        sql.append(SqlConstant.SPACES);
        return sql.toString();
    }

}
