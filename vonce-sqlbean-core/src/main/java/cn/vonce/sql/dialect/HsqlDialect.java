package cn.vonce.sql.dialect;

import cn.vonce.sql.bean.Alter;
import cn.vonce.sql.bean.Common;
import cn.vonce.sql.bean.Table;
import cn.vonce.sql.config.SqlBeanDB;
import cn.vonce.sql.constant.SqlConstant;
import cn.vonce.sql.enumerate.AlterType;
import cn.vonce.sql.enumerate.JavaMapHsqlType;
import cn.vonce.sql.enumerate.JdbcType;
import cn.vonce.sql.exception.SqlBeanException;
import cn.vonce.sql.uitls.SqlBeanUtil;
import cn.vonce.sql.uitls.StringUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Hsql方言
 *
 * @author Jovi
 * @email imjovi@qq.com
 * @date 2024/4/16 10:19
 */
public class HsqlDialect implements SqlDialect<JavaMapHsqlType> {

    @Override
    public JavaMapHsqlType getType(Field field) {
        Class<?> clazz = SqlBeanUtil.getEntityClassFieldType(field);
        for (JavaMapHsqlType javaType : JavaMapHsqlType.values()) {
            for (Class<?> thisClazz : javaType.getClasses()) {
                if (thisClazz == clazz) {
                    return javaType;
                }
            }
        }
        throw new SqlBeanException(field.getDeclaringClass().getName() + "实体类不支持此字段类型：" + clazz.getSimpleName());
    }

    @Override
    public JdbcType getJdbcType(Field field) {
        return JdbcType.getType(getType(field).name());
    }

    @Override
    public String getTableListSql(SqlBeanDB sqlBeanDB, String schema, String tableName) {
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

    @Override
    public String getColumnListSql(SqlBeanDB sqlBeanDB, String schema, String tableName) {
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

    @Override
    public List<String> alterTable(List<Alter> alterList) {
        List<String> sqlList = new ArrayList<>();
        String transferred = SqlBeanUtil.getTransferred(alterList.get(0));
        StringBuffer sql = new StringBuffer();
        StringBuffer remarksSql = new StringBuffer();
        for (int i = 0; i < alterList.size(); i++) {
            Alter alter = alterList.get(i);
            if (alter.getType() == AlterType.ADD) {
                sql.append(SqlConstant.ALTER_TABLE);
                sql.append(getFullName(alter, alter.getTable()));
                sql.append(SqlConstant.ADD);
                sql.append(SqlBeanUtil.addColumn(alter, alter.getColumnInfo(), alter.getAfterColumnName()));
                remarksSql.append(addRemarks(false, alter, transferred));
            } else if (alter.getType() == AlterType.CHANGE) {
                sql.append(changeColumn(alter));
                sql.append(SqlConstant.SEMICOLON);
                //先改名后修改信息
                StringBuffer modifySql = modifyColumn(alter);
                if (modifySql.length() > 0) {
                    sql.append(SqlConstant.ALTER_TABLE);
                    sql.append(modifySql);
                }
                remarksSql.append(addRemarks(false, alter, transferred));
            } else if (alter.getType() == AlterType.MODIFY) {
                sql.append(SqlConstant.ALTER_TABLE);
                sql.append(modifyColumn(alter));
                remarksSql.append(addRemarks(false, alter, transferred));
            } else if (alter.getType() == AlterType.DROP) {
                sql.append(SqlConstant.ALTER_TABLE);
                sql.append(getFullName(alter, alter.getTable()));
                sql.append(SqlConstant.DROP);
                sql.append(SqlConstant.COLUMN);
                sql.append(SqlBeanUtil.isToUpperCase(alter) ? alter.getColumnInfo().getName().toUpperCase() : alter.getColumnInfo().getName());
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
     * @param common
     * @param table
     * @return
     */
    private String getFullName(Common common, Table table) {
        String transferred = SqlBeanUtil.getTransferred(common);
        boolean toUpperCase = SqlBeanUtil.isToUpperCase(common);
        StringBuffer sql = new StringBuffer();
        if (StringUtil.isNotBlank(table.getSchema())) {
            sql.append(transferred);
            sql.append(toUpperCase ? table.getSchema().toUpperCase() : table.getSchema());
            sql.append(transferred);
            sql.append(SqlConstant.POINT);
        }
        sql.append(transferred);
        sql.append(toUpperCase ? table.getName().toUpperCase() : table.getName());
        sql.append(transferred);
        sql.append(SqlConstant.SPACES);
        return sql.toString();
    }

    /**
     * 更改列信息
     *
     * @param alter
     * @return
     */
    private StringBuffer modifyColumn(Alter alter) {
        StringBuffer modifySql = new StringBuffer();
        modifySql.append(getFullName(alter, alter.getTable()));
        modifySql.append(SqlConstant.ALTER);
        modifySql.append(SqlConstant.COLUMN);
        modifySql.append(SqlBeanUtil.addColumn(alter, alter.getColumnInfo(), alter.getAfterColumnName()));
        return modifySql;
    }

    /**
     * 更改字段名
     *
     * @param alter
     * @return
     */
    private String changeColumn(Alter alter) {
        StringBuffer changeSql = new StringBuffer();
        changeSql.append(SqlConstant.ALTER_TABLE);
        changeSql.append(getFullName(alter, alter.getTable()));
        changeSql.append(SqlConstant.RENAME);
        changeSql.append(SqlConstant.COLUMN);
        changeSql.append(SqlBeanUtil.isToUpperCase(alter) ? alter.getOldColumnName().toUpperCase() : alter.getOldColumnName());
        changeSql.append(SqlConstant.TO);
        changeSql.append(SqlBeanUtil.isToUpperCase(alter) ? alter.getColumnInfo().getName().toUpperCase() : alter.getColumnInfo().getName());
        return changeSql.toString();
    }

    @Override
    public String addRemarks(boolean isTable, Alter item, String transferred) {
        StringBuffer remarksSql = new StringBuffer();
        remarksSql.append(SqlConstant.COMMENT);
        remarksSql.append(SqlConstant.ON);
        remarksSql.append(isTable ? SqlConstant.TABLE : SqlConstant.COLUMN);
        remarksSql.append(getFullName(item, item.getTable()));
        if (!isTable) {
            remarksSql.append(SqlConstant.POINT);
            remarksSql.append(transferred);
            remarksSql.append(item.getColumnInfo().getName());
            remarksSql.append(transferred);
        }
        remarksSql.append(SqlConstant.IS);
        remarksSql.append(SqlConstant.SINGLE_QUOTATION_MARK);
        remarksSql.append(StringUtil.isNotBlank(item.getColumnInfo().getRemarks()) ? item.getColumnInfo().getRemarks() : "''");
        remarksSql.append(SqlConstant.SINGLE_QUOTATION_MARK);
        return remarksSql.toString();
    }

    @Override
    public String getDatabaseSql(SqlBeanDB sqlBeanDB, String name) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT SCHEMA_NAME as \"name\" FROM INFORMATION_SCHEMA.SCHEMATA ");
        if (StringUtil.isNotEmpty(name)) {
            sql.append("WHERE SCHEMA_NAME = ");
            sql.append("'" + this.getSchemaName(sqlBeanDB, name) + "'");
        }
        return sql.toString();
    }

    @Override
    public String getCreateDatabaseSql(SqlBeanDB sqlBeanDB, String name) {
        return "CREATE SCHEMA IF NOT EXISTS " + this.getSchemaName(sqlBeanDB, name);
    }

    @Override
    public String getDropDatabaseSql(SqlBeanDB sqlBeanDB, String name) {
        return "DROP SCHEMA IF EXISTS " + this.getSchemaName(sqlBeanDB, name);
    }

}
