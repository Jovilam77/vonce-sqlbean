package cn.vonce.sql.enumerate;

import cn.vonce.sql.bean.Alter;
import cn.vonce.sql.bean.ColumnInfo;
import cn.vonce.sql.bean.Table;
import cn.vonce.sql.config.SqlBeanDB;
import cn.vonce.sql.constant.SqlConstant;
import cn.vonce.sql.exception.SqlBeanException;
import cn.vonce.sql.uitls.SqlBeanUtil;
import cn.vonce.sql.uitls.StringUtil;

import java.lang.reflect.Field;
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

    INT(new Class[]{int.class, Integer.class}),
    BIGINT(new Class[]{long.class, Long.class}),
    SMALLINT(new Class[]{short.class, Short.class}),
    REAL(new Class[]{float.class, Float.class}),
    FLOAT(new Class[]{double.class, Double.class}),
    NUMERIC(new Class[]{BigDecimal.class}),
    NCHAR(new Class[]{char.class, Character.class}), NVARCHAR(new Class[]{String.class}),
    TINYINT(new Class[]{byte.class, Byte.class}),
    BIT(new Class[]{boolean.class, Boolean.class}),
    DATE(new Class[]{java.sql.Date.class, java.time.LocalDate.class}),
    TIME(new Class[]{java.sql.Time.class, java.time.LocalTime.class}),
    DATETIME2(new Class[]{java.sql.Timestamp.class}),
    DATETIME(new Class[]{java.util.Date.class, java.time.LocalDateTime.class}),
    NTEXT(new Class[]{java.sql.Clob.class}),
    IMAGE(new Class[]{java.sql.Blob.class, Object.class});


    JavaMapSqlServerType(Class<?>[] classes) {
        this.classes = classes;
    }

    private Class<?>[] classes;

    public static JavaMapSqlServerType getType(Field field) {
        Class<?> clazz = SqlBeanUtil.getEntityClassFieldType(field);
        for (JavaMapSqlServerType javaType : values()) {
            for (Class<?> thisClazz : javaType.classes) {
                if (thisClazz == clazz) {
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
        sql.append("SELECT syscolumns.id, syscolumns.colid AS cid, syscolumns.name AS name, syscolumns.prec AS length, syscolumns.scale, systypes.name AS type, syscolumns.isnullable AS notnull, '");
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
        StringBuffer sql = new StringBuffer();
        StringBuffer remarksSql = new StringBuffer();
        for (int i = 0; i < alterList.size(); i++) {
            Alter alter = alterList.get(i);
            if (alter.getType() == AlterType.ADD) {
                sql.append(SqlConstant.ALTER_TABLE);
                sql.append(getFullName(alter, alter.getTable(), null));
                sql.append(SqlConstant.ADD);
                sql.append(SqlBeanUtil.addColumn(alter, alter.getColumnInfo(), alter.getAfterColumnName()));
                remarksSql.append(addRemarks(false, alter));
            } else if (alter.getType() == AlterType.CHANGE) {
                //改名
                sql.append(SqlConstant.EXEC_SP_RENAME);
                sql.append(getFullName(alter, alter.getTable(), alter.getOldColumnName()));
                sql.append(SqlConstant.COMMA);
                sql.append(SqlConstant.SINGLE_QUOTATION_MARK);
                sql.append(alter.getColumnInfo().getName());
                sql.append(SqlConstant.SINGLE_QUOTATION_MARK);
                sql.append(SqlConstant.COMMA);
                sql.append(SqlConstant.SINGLE_QUOTATION_MARK);
                sql.append(SqlConstant.COLUMN);
                sql.append(SqlConstant.SINGLE_QUOTATION_MARK);
                sql.append(SqlConstant.SPACES);
                sql.append(SqlConstant.SEMICOLON);
                //先改名后修改信息
                StringBuffer modifySql = modifyColumn(alter);
                if (modifySql.length() > 0) {
                    sql.append(SqlConstant.ALTER_TABLE);
                    sql.append(modifySql);
                }
                remarksSql.append(addRemarks(false, alter));
            } else if (alter.getType() == AlterType.MODIFY) {
                sql.append(SqlConstant.ALTER_TABLE);
                sql.append(modifyColumn(alter));
                remarksSql.append(addRemarks(false, alter));
            } else if (alter.getType() == AlterType.DROP) {
                sql.append(SqlConstant.ALTER_TABLE);
                sql.append(getFullName(alter, alter.getTable(), null));
                sql.append(SqlConstant.DROP);
                sql.append(SqlConstant.COLUMN);
                sql.append(SqlConstant.BEGIN_SQUARE_BRACKETS);
                sql.append(SqlBeanUtil.isToUpperCase(alter) ? alter.getColumnInfo().getName().toUpperCase() : alter.getColumnInfo().getName());
                sql.append(SqlConstant.END_SQUARE_BRACKETS);
            }
            sql.append(SqlConstant.SPACES);
            sql.append(SqlConstant.SEMICOLON);
        }
        sqlList.add(sql.toString());
        sqlList.add(remarksSql.toString());
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
        boolean rename = alter.getType() == AlterType.CHANGE && StringUtil.isNotBlank(columnName);
        if (rename) {
            sql.append(SqlConstant.SINGLE_QUOTATION_MARK);
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
        sql.append(SqlBeanUtil.isToUpperCase(alter) ? table.getName().toUpperCase() : table.getName());
        sql.append(SqlConstant.END_SQUARE_BRACKETS);
        if (rename) {
            sql.append(SqlConstant.POINT);
            sql.append(SqlConstant.BEGIN_SQUARE_BRACKETS);
            sql.append(columnName);
            sql.append(SqlConstant.END_SQUARE_BRACKETS);
            sql.append(SqlConstant.SINGLE_QUOTATION_MARK);
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
        String fullName = getFullName(alter, alter.getTable(), null);
        modifySql.append(fullName);
        modifySql.append(SqlConstant.ALTER);
        modifySql.append(SqlConstant.COLUMN);
        JdbcType jdbcType = JdbcType.getType(columnInfo.getType());
        modifySql.append(SqlBeanUtil.getTableFieldName(alter, columnInfo.getName()));
        modifySql.append(SqlConstant.SPACES);
        modifySql.append(jdbcType.name());
        if (columnInfo.getLength() != null && columnInfo.getLength() > 0) {
            modifySql.append(SqlConstant.BEGIN_BRACKET);
            //字段长度
            modifySql.append(columnInfo.getLength());
            if (jdbcType.isFloat()) {
                modifySql.append(SqlConstant.COMMA);
                modifySql.append(columnInfo.getScale() == null ? 0 : columnInfo.getScale());
            }
            modifySql.append(SqlConstant.END_BRACKET);
        }
        //是否为null
        if ((columnInfo.getNotnull() != null && columnInfo.getNotnull()) || columnInfo.getPk()) {
            modifySql.append(SqlConstant.SPACES);
            modifySql.append(SqlConstant.NOT_NULL);
        } else {
            if (columnInfo.getNotnull() != null && !columnInfo.getNotnull()) {
                modifySql.append(SqlConstant.SPACES);
                modifySql.append(SqlConstant.NULL);
            }
        }
        //是否自增
        if (columnInfo.getAutoIncr() != null && columnInfo.getAutoIncr()) {
            if (alter.getSqlBeanDB().getDbType() == DbType.MySQL || alter.getSqlBeanDB().getDbType() == DbType.MariaDB) {
                modifySql.append(SqlConstant.SPACES);
                modifySql.append(SqlConstant.AUTO_INCREMENT);
            }
        }
//        //默认值
//        if (StringUtil.isNotEmpty(columnInfo.getDfltValue())) {
//            modifySql.append(SqlConstant.SEMICOLON);
//            modifySql.append(SqlConstant.ALTER_TABLE);
//            modifySql.append(fullName);
//            modifySql.append(SqlConstant.ADD);
//            modifySql.append(SqlConstant.DEFAULT);
//            modifySql.append(SqlConstant.SPACES);
//            modifySql.append(columnInfo.getDfltValue());
//            modifySql.append(SqlConstant.SPACES);
//            modifySql.append(SqlConstant.FOR);
//            modifySql.append(SqlConstant.BEGIN_SQUARE_BRACKETS);
//            modifySql.append(SqlBeanUtil.isToUpperCase(alter) ? columnInfo.getName().toUpperCase() : columnInfo.getName());
//            modifySql.append(SqlConstant.END_SQUARE_BRACKETS);
//        }
        return modifySql;
    }

    /**
     * 增加列备注
     *
     * @param isTable
     * @param item
     * @return
     */
    public static String addRemarks(boolean isTable, Alter item) {
        StringBuffer remarksSql = new StringBuffer();
        if (StringUtil.isNotBlank(item.getColumnInfo().getRemarks())) {
            remarksSql.append("IF ((SELECT COUNT(*) FROM ::fn_listextendedproperty(");
            remarksSql.append("'MS_Description', 'SCHEMA', N'dbo', 'TABLE', N'" + item.getTable().getName() + (!isTable ? "', 'COLUMN', N'" + item.getColumnInfo().getName() + "'" : "', NULL, NULL"));
            remarksSql.append(")) > 0)");
            remarksSql.append("\n  EXEC sp_updateextendedproperty ");
            remarksSql.append("'MS_Description', N'" + item.getColumnInfo().getRemarks() + "', 'SCHEMA', N'dbo', 'TABLE', N'" + item.getTable().getName() + "'" + (!isTable ? (", 'COLUMN', N'" + item.getColumnInfo().getName() + "'") : ""));
            remarksSql.append("\nELSE");
            remarksSql.append("\n  EXEC sp_addextendedproperty ");
            remarksSql.append("'MS_Description', N'" + item.getColumnInfo().getRemarks() + "', 'SCHEMA', N'dbo', 'TABLE', N'" + item.getTable().getName() + "'" + (!isTable ? (", 'COLUMN', N'" + item.getColumnInfo().getName() + "'") : ""));
            remarksSql.append(SqlConstant.SEMICOLON);
        }
        return remarksSql.toString();
    }

}
