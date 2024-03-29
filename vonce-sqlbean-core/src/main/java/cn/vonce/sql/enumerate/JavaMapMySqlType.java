package cn.vonce.sql.enumerate;

import cn.vonce.sql.bean.Alter;
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
public enum JavaMapMySqlType {

    INT(new Class[]{int.class, Integer.class}),
    BIGINT(new Class[]{long.class, Long.class}),
    SMALLINT(new Class[]{short.class, Short.class}),
    FLOAT(new Class[]{float.class, Float.class}),
    DOUBLE(new Class[]{double.class, Double.class}),
    DECIMAL(new Class[]{BigDecimal.class}),
    CHAR(new Class[]{char.class, Character.class}),
    VARCHAR(new Class[]{String.class}),
    TINYINT(new Class[]{byte.class, Byte.class}),
    BIT(new Class[]{boolean.class, Boolean.class}),
    DATE(new Class[]{java.sql.Date.class, java.time.LocalDate.class}),
    TIME(new Class[]{java.sql.Time.class, java.time.LocalTime.class}),
    TIMESTAMP(new Class[]{java.sql.Timestamp.class}),
    DATETIME(new Class[]{java.util.Date.class, java.time.LocalDateTime.class}),
    CLOB(new Class[]{java.sql.Clob.class}),
    BLOB(new Class[]{java.sql.Blob.class, Object.class});


    JavaMapMySqlType(Class<?>[] classes) {
        this.classes = classes;
    }

    private Class<?>[] classes;

    public static JavaMapMySqlType getType(Field field) {
        Class<?> clazz = SqlBeanUtil.getEntityClassFieldType(field);
        for (JavaMapMySqlType javaType : values()) {
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
        sql.append("SELECT table_schema AS `schema`, table_name AS `name`, table_comment AS `remarks` ");
        sql.append("FROM information_schema.tables ");
        sql.append("WHERE table_type = 'BASE TABLE' AND table_schema = ");
        if (StringUtil.isNotEmpty(schema)) {
            sql.append("'" + schema + "'");
        } else {
            sql.append("database()");
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
        sql.append("SELECT ordinal_position AS cid, column_name AS name, data_type AS type, ");
        sql.append("(CASE is_nullable WHEN 'NO' THEN 1 ELSE 0 END) AS notnull, column_default AS dflt_value, ");
        sql.append("(CASE column_key WHEN 'PRI' THEN 1 ELSE 0 END) AS pk, ");
        sql.append("(CASE column_key WHEN 'MUL' THEN 1 ELSE 0 END) AS fk, ");
        sql.append("(CASE extra WHEN 'auto_increment' THEN 1 ELSE 0 END) AS auto_incr, ");
        sql.append("(CASE WHEN data_type = 'bit' OR data_type = 'tinyint' OR data_type = 'smallint' OR data_type = 'mediumint' OR data_type = 'int' OR data_type = 'bigint' ");
        sql.append("THEN REPLACE ( SUBSTRING( column_type, INSTR( column_type, '(' )+ 1 ), ')', '' ) ");
        sql.append("WHEN data_type = 'float' OR data_type = 'double' OR data_type = 'decimal' ");
        sql.append("THEN numeric_precision ELSE character_maximum_length END ) AS length, ");
        sql.append("numeric_scale AS scale, ");
        sql.append("column_comment AS remarks ");
        sql.append("FROM information_schema.columns ");
        sql.append("WHERE table_schema = ");
        if (StringUtil.isNotEmpty(schema)) {
            sql.append("'" + schema + "'");
        } else {
            sql.append("database()");
        }
        sql.append(" AND table_name = '");
        sql.append(tableName);
        sql.append("'");
        return sql.toString();
    }

    /**
     * 更改表结构
     *
     * @param alterList
     * @return
     */
    public static List<String> alterTable(List<Alter> alterList) {
        String transferred = SqlBeanUtil.getTransferred(alterList.get(0));
        Table table = alterList.get(0).getTable();
        StringBuffer sql = new StringBuffer();
        sql.append(SqlConstant.ALTER_TABLE);
        if (StringUtil.isNotBlank(table.getSchema())) {
            sql.append(transferred);
            sql.append(table.getSchema());
            sql.append(transferred);
            sql.append(SqlConstant.POINT);
        }
        sql.append(transferred);
        sql.append(table.getName());
        sql.append(transferred);
        sql.append(SqlConstant.SPACES);
        for (int i = 0; i < alterList.size(); i++) {
            Alter alter = alterList.get(i);
            if (alter.getType() == AlterType.ADD) {
                sql.append(SqlConstant.ADD);
                sql.append(SqlConstant.COLUMN);
                sql.append(SqlBeanUtil.addColumn(alter, alter.getColumnInfo(), alter.getAfterColumnName()));
            } else if (alter.getType() == AlterType.MODIFY) {
                sql.append(SqlConstant.MODIFY);
                sql.append(SqlConstant.COLUMN);
                sql.append(SqlBeanUtil.addColumn(alter, alter.getColumnInfo(), alter.getAfterColumnName()));
            } else if (alter.getType() == AlterType.CHANGE) {
                sql.append(SqlConstant.CHANGE);
                sql.append(SqlConstant.COLUMN);
                sql.append(SqlBeanUtil.isToUpperCase(alter) ? alter.getOldColumnName().toUpperCase() : alter.getOldColumnName());
                sql.append(SqlBeanUtil.addColumn(alter, alter.getColumnInfo(), alter.getAfterColumnName()));
            } else if (alter.getType() == AlterType.DROP) {
                sql.append(SqlConstant.DROP);
                sql.append(SqlConstant.COLUMN);
                sql.append(SqlBeanUtil.isToUpperCase(alter) ? alter.getColumnInfo().getName().toUpperCase() : alter.getColumnInfo().getName());
            }
            sql.append(SqlConstant.SPACES);
            if (i < alterList.size() - 1) {
                sql.append(SqlConstant.COMMA);
            }
        }
        List<String> sqlList = new ArrayList<>();
        sqlList.add(sql.toString());
        return sqlList;
    }

    /**
     * 增加列备注
     *
     * @param item
     * @param transferred
     * @return
     */
    public static String addRemarks(Alter item, String transferred) {
        StringBuffer remarksSql = new StringBuffer();
        remarksSql.append(SqlConstant.ALTER_TABLE);
        if (StringUtil.isNotBlank(item.getTable().getSchema())) {
            remarksSql.append(transferred);
            remarksSql.append(item.getTable().getSchema());
            remarksSql.append(transferred);
            remarksSql.append(SqlConstant.POINT);
        }
        remarksSql.append(transferred);
        remarksSql.append(item.getTable().getName());
        remarksSql.append(transferred);
        remarksSql.append(SqlConstant.COMMENT);
        remarksSql.append(SqlConstant.EQUAL_TO);
        remarksSql.append(SqlConstant.SINGLE_QUOTATION_MARK);
        remarksSql.append(StringUtil.isNotBlank(item.getColumnInfo().getRemarks()) ? item.getColumnInfo().getRemarks() : "''");
        remarksSql.append(SqlConstant.SINGLE_QUOTATION_MARK);
        return remarksSql.toString();
    }

}
