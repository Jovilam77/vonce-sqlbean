package cn.vonce.sql.helper;

import cn.vonce.sql.annotation.*;
import cn.vonce.sql.uitls.ReflectUtil;
import cn.vonce.sql.uitls.StringUtil;
import cn.vonce.sql.bean.*;
import cn.vonce.sql.constant.SqlConstant;
import cn.vonce.sql.enumerate.*;
import cn.vonce.sql.exception.SqlBeanException;
import cn.vonce.sql.uitls.SqlBeanUtil;
import com.google.common.collect.ListMultimap;

import java.lang.reflect.Field;
import java.util.*;

/**
 * SQL 语句助手
 *
 * @author jovi
 * @version 1.0
 * @email 766255988@qq.com
 * @date 2017年6月2日下午5:41:59
 */
public class SqlHelper {

    /**
     * 参数为空抛出异常
     *
     * @param object
     * @param message
     */
    public static void isNull(Object object, String message) {
        if (object == null) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * 检查
     */
    public static void check(Common common) {
        isNull(common.getSqlBeanDB(), "请设置sqlBeanConfig");
        isNull(common.getSqlBeanDB().getDbType(), "请设置sqlBeanConfig -> dbType");
    }

    /**
     * 生成select sql语句
     *
     * @param select
     * @return
     */
    public static String buildSelectSql(Select select) {
        check(select);
        StringBuffer sqlSb = new StringBuffer();
        Integer[] pageParam = null;
        String orderSql = orderBySql(select);
        //SQLServer2008 分页处理
        if (select.getSqlBeanDB().getDbType() == DbType.SQLServer) {
            if (SqlBeanUtil.isUsePage(select)) {
                pageParam = pageParam(select);
                sqlSb.append(SqlConstant.SELECT);
                sqlSb.append(SqlConstant.ALL);
                sqlSb.append(SqlConstant.FROM);
                sqlSb.append(SqlConstant.BEGIN_BRACKET);
            }
        }
        //标准Sql
        sqlSb.append(select.isDistinct() ? SqlConstant.SELECT_DISTINCT : SqlConstant.SELECT);
        //SqlServer 分页处理
        if (select.getSqlBeanDB().getDbType() == DbType.SQLServer) {
            if (SqlBeanUtil.isUsePage(select)) {
                sqlSb.append(SqlConstant.TOP);
                sqlSb.append(pageParam[0]);
                sqlSb.append(SqlConstant.ROW_NUMBER + SqlConstant.OVER + SqlConstant.BEGIN_BRACKET + orderSql + SqlConstant.END_BRACKET + SqlConstant.ROWNUM + SqlConstant.COMMA);
            }
        }
        //标准Sql
        sqlSb.append(column(select));
        sqlSb.append(SqlConstant.FROM);
        sqlSb.append(fromFullName(select));
        sqlSb.append(joinSql(select));
        sqlSb.append(whereSql(select, null));
        String groupBySql = groupBySql(select);
        sqlSb.append(groupBySql);
        sqlSb.append(havingSql(select));
        if (!SqlBeanUtil.isCount(select)) {
            sqlSb.append(orderSql);
        }
        //SQLServer2008 分页处理
        if (select.getSqlBeanDB().getDbType() == DbType.SQLServer) {
            // 主要逻辑 结束
            if (SqlBeanUtil.isUsePage(select)) {
                sqlSb.append(SqlConstant.END_BRACKET);
                sqlSb.append(SqlConstant.T);
                sqlSb.append(SqlConstant.WHERE);
                sqlSb.append(SqlConstant.T + SqlConstant.POINT + SqlConstant.ROWNUM);
                sqlSb.append(SqlConstant.GREATER_THAN);
                sqlSb.append(pageParam[1]);
            }
        }
        //标准Sql
        if (SqlBeanUtil.isCount(select) && StringUtil.isNotEmpty(groupBySql)) {
            sqlSb.insert(0, SqlConstant.SELECT + SqlConstant.COUNT + SqlConstant.BEGIN_BRACKET + SqlConstant.ALL + SqlConstant.END_BRACKET + SqlConstant.FROM + SqlConstant.BEGIN_BRACKET);
            sqlSb.append(SqlConstant.END_BRACKET + SqlConstant.AS + SqlConstant.T);
        }
        //MySQL,MariaDB,H2 分页处理
        if (select.getSqlBeanDB().getDbType() == DbType.MySQL || select.getSqlBeanDB().getDbType() == DbType.MariaDB || select.getSqlBeanDB().getDbType() == DbType.H2) {
            mysqlPageDispose(select, sqlSb);
        }
        //PostgreSQL,SQLite,Hsql 分页处理
        else if (select.getSqlBeanDB().getDbType() == DbType.PostgreSQL || select.getSqlBeanDB().getDbType() == DbType.SQLite || select.getSqlBeanDB().getDbType() == DbType.Hsql) {
            postgreSqlPageDispose(select, sqlSb);
        }
        //Oracle 分页处理
        else if (select.getSqlBeanDB().getDbType() == DbType.Oracle) {
            oraclePageDispose(select, sqlSb);
        }
        //DB2 分页处理
        else if (select.getSqlBeanDB().getDbType() == DbType.DB2) {
            db2PageDispose(select, sqlSb);
        }
        //Derby 分页处理
        else if (select.getSqlBeanDB().getDbType() == DbType.Derby) {
            derbyPageDispose(select, sqlSb);
        }
        return sqlSb.toString();
    }

    /**
     * 生成update sql语句
     *
     * @param update
     * @return
     * @throws SqlBeanException
     */
    public static String buildUpdateSql(Update update) {
        check(update);
        StringBuffer sqlSb = new StringBuffer();
        sqlSb.append(SqlConstant.UPDATE);
        if (update.getSqlBeanDB().getDbType() == DbType.H2 || update.getSqlBeanDB().getDbType() == DbType.Oracle) {
            sqlSb.append(fromFullName(update));
        } else {
            sqlSb.append(getTableName(update.getTable(), update));
        }
        sqlSb.append(SqlConstant.SET);
        sqlSb.append(setSql(update));
        sqlSb.append(whereSql(update, update.getUpdateBean()));
        return sqlSb.toString();
    }

    /**
     * 生成insert sql语句
     *
     * @param insert
     * @return
     */
    @SuppressWarnings("unchecked")
    public static String buildInsertSql(Insert insert) {
        check(insert);
        String sql = null;
        try {
            sql = fieldAndValuesSql(insert, insert.getInsertBean());
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return sql;
    }

    /**
     * 生成delete sql语句
     *
     * @param delete
     * @return
     */
    public static String buildDeleteSql(Delete delete) {
        check(delete);
        StringBuffer sqlSb = new StringBuffer();
        sqlSb.append(SqlConstant.DELETE_FROM);
        if (delete.getSqlBeanDB().getDbType() == DbType.H2 || delete.getSqlBeanDB().getDbType() == DbType.Oracle) {
            sqlSb.append(fromFullName(delete));
        } else {
            sqlSb.append(getTableName(delete.getTable(), delete));
        }
        sqlSb.append(whereSql(delete, null));
        return sqlSb.toString();
    }

    /**
     * 生成create sql语句
     *
     * @param create
     * @return
     */
    public static String buildCreateSql(Create create) {
        check(create);
        StringBuffer sqlSb = new StringBuffer();
        sqlSb.append(SqlConstant.CREATE_TABLE);
        sqlSb.append(getTableName(create.getTable(), create));
        sqlSb.append(SqlConstant.BEGIN_BRACKET);
        Field idField = null;
        List<Field> fieldList = SqlBeanUtil.getBeanAllField(create.getBeanClass());
        SqlTable sqlTable = SqlBeanUtil.getSqlTable(create.getBeanClass());
        String transferred = SqlBeanUtil.getTransferred(create);
        for (int i = 0; i < fieldList.size(); i++) {
            if (SqlBeanUtil.isIgnore(fieldList.get(i))) {
                continue;
            }
            if (idField == null) {
                if (fieldList.get(i).getAnnotation(SqlId.class) != null) {
                    idField = fieldList.get(i);
                }
            }
            SqlColumn sqlColumn = fieldList.get(i).getAnnotation(SqlColumn.class);
            ColumnInfo columnInfo = getColumnInfo(create.getSqlBeanDB().getDbType(), fieldList.get(i).getType(), sqlColumn);
            JdbcType jdbcType = JdbcType.getType(columnInfo.getType());
            String columnName = SqlBeanUtil.getTableFieldName(fieldList.get(i), sqlTable);
            sqlSb.append(transferred);
            sqlSb.append(SqlBeanUtil.isToUpperCase(create) ? columnName.toUpperCase() : columnName);
            sqlSb.append(transferred);
            sqlSb.append(SqlConstant.SPACES);
            sqlSb.append(jdbcType.name());
            if (columnInfo.getLength() > 0) {
                sqlSb.append(SqlConstant.BEGIN_BRACKET);
                //字段长度
                sqlSb.append(columnInfo.getLength());
                if (jdbcType.isFloat()) {
                    sqlSb.append(SqlConstant.COMMA);
                    sqlSb.append(columnInfo.getScale());
                }
                sqlSb.append(SqlConstant.END_BRACKET);
            }
            //是否为null
            if (columnInfo.getNotnull()) {
                sqlSb.append(SqlConstant.NOT_NULL);
            }
            //默认值
            if (StringUtil.isNotEmpty(columnInfo.getDfltValue())) {
                sqlSb.append(SqlConstant.DEFAULT);
                sqlSb.append(SqlBeanUtil.getSqlValue(create, columnInfo.getDfltValue(), jdbcType));
            }
            sqlSb.append(SqlConstant.COMMA);
        }

        //主键
        if (idField != null) {
            String id = SqlBeanUtil.getTableFieldName(idField, sqlTable);
            sqlSb.append(SqlConstant.PRIMARY_KEY);
            sqlSb.append(SqlConstant.BEGIN_BRACKET);
            sqlSb.append(transferred);
            sqlSb.append(SqlBeanUtil.isToUpperCase(create) ? id.toUpperCase() : id);
            sqlSb.append(transferred);
            sqlSb.append(SqlConstant.END_BRACKET);
        } else {
            sqlSb.deleteCharAt(sqlSb.length() - SqlConstant.COMMA.length());
        }
        sqlSb.append(SqlConstant.END_BRACKET);
        return sqlSb.toString();
    }

    /**
     * 生成backup sql语句
     *
     * @param backup
     * @return
     */
    public static String buildBackup(Backup backup) {
        check(backup);
        String targetSchema = backup.getTargetSchema();
        if (StringUtil.isEmpty(targetSchema)) {
            targetSchema = backup.getTable().getSchema();
        }
        StringBuffer backupSql = new StringBuffer();
        //非SQLServer、PostgreSQL数据库则使用：create table A as select * from B
        if (DbType.SQLServer != backup.getSqlBeanDB().getDbType() && DbType.PostgreSQL != backup.getSqlBeanDB().getDbType()) {
            backupSql.append(SqlConstant.CREATE_TABLE);
            backupSql.append(getTableName(targetSchema, backup.getTargetTableName()));
            backupSql.append(SqlConstant.SPACES);
            backupSql.append(SqlConstant.AS);
        }
        backupSql.append(SqlConstant.SELECT);
        if (backup.getColumns() != null && backup.getColumns().length > 0) {
            for (Column column : backup.getColumns()) {
                backupSql.append(column.getName());
                backupSql.append(SqlConstant.COMMA);
            }
            backupSql.delete(backupSql.length() - SqlConstant.COMMA.length(), backupSql.length());
        } else {
            backupSql.append(SqlConstant.ALL);
        }
        //如果是SQLServer、PostgreSQL数据库则需要拼接INTO：select * into A from B
        if (DbType.SQLServer == backup.getSqlBeanDB().getDbType() || DbType.PostgreSQL == backup.getSqlBeanDB().getDbType()) {
            backupSql.append(SqlConstant.INTO);
            backupSql.append(getTableName(targetSchema, backup.getTargetTableName()));
        }
        backupSql.append(SqlConstant.FROM);
        backupSql.append(getTableName(backup.getTable(), backup));
        //如果是Derby数据库，仅支持创建表结构，其他数据库则可通过条件备份数据和是否需要数据
        if (DbType.Derby == backup.getSqlBeanDB().getDbType()) {
            backupSql.append(" WITH NO DATA");
        } else {
            backupSql.append(whereSql(backup, null));
        }
        return backupSql.toString();
    }

    /**
     * 生成copy sql语句
     *
     * @param copy
     * @return
     */
    public static String buildCopy(Copy copy) {
        check(copy);
        String targetSchema = copy.getTargetSchema();
        if (StringUtil.isEmpty(targetSchema)) {
            targetSchema = copy.getTable().getSchema();
        }
        StringBuffer copySql = new StringBuffer();
        StringBuffer columnSql = new StringBuffer();
        copySql.append(SqlConstant.INSERT_INTO);
        copySql.append(getTableName(targetSchema, copy.getTargetTableName()));
        if (copy.getColumns() != null && copy.getColumns().length > 0) {
            for (Column column : copy.getColumns()) {
                columnSql.append(column.getName());
                columnSql.append(SqlConstant.COMMA);
            }
            columnSql.delete(columnSql.length() - SqlConstant.COMMA.length(), columnSql.length());
            copySql.append(SqlConstant.SPACES);
            copySql.append(SqlConstant.BEGIN_BRACKET);
            copySql.append(columnSql);
            copySql.append(SqlConstant.END_BRACKET);
        }
        copySql.append(SqlConstant.SPACES);
        copySql.append(SqlConstant.SELECT);
        if (copy.getColumns() != null && copy.getColumns().length > 0) {
            copySql.append(columnSql);
        } else {
            copySql.append(SqlConstant.ALL);
        }
        copySql.append(SqlConstant.FROM);
        copySql.append(getTableName(copy.getTable(), copy));
        copySql.append(whereSql(copy, null));
        return copySql.toString();
    }

    /**
     * 生成drop sql语句
     *
     * @param drop
     * @return
     */
    public static String buildDrop(Drop drop) {
        StringBuffer dropSql = new StringBuffer();
        String tableName = getTableName(drop.getTable(), drop);
        if (drop.getSqlBeanDB().getDbType() == DbType.MySQL || drop.getSqlBeanDB().getDbType() == DbType.MariaDB || drop.getSqlBeanDB().getDbType() == DbType.PostgreSQL || drop.getSqlBeanDB().getDbType() == DbType.H2) {
            dropSql.append("DROP TABLE IF EXISTS ");
            dropSql.append(tableName);
        } else if (drop.getSqlBeanDB().getDbType() == DbType.MySQL) {
            dropSql.append("IF OBJECT_ID(N'" + tableName + "', N'U') IS NOT NULL ");
            dropSql.append("DROP TABLE " + tableName + " ");
        } else {
            dropSql.append("DROP TABLE ");
            dropSql.append(tableName);
        }
        return dropSql.toString();
    }

    /**
     * 获取列信息
     *
     * @param dbType
     * @param clazz
     * @param sqlColumn
     * @return
     */
    private static ColumnInfo getColumnInfo(DbType dbType, Class<?> clazz, SqlColumn sqlColumn) {
        ColumnInfo columnInfo = new ColumnInfo();
        JdbcType jdbcType;
        if (sqlColumn != null && sqlColumn.type() != JdbcType.NOTHING) {
            jdbcType = JdbcType.getType(sqlColumn.type().name());
            columnInfo.setType(jdbcType.name());
            columnInfo.setNotnull(sqlColumn.notNull());
        } else {
            if (dbType == DbType.SQLite) {
                jdbcType = JdbcType.getType(SQLiteJavaType.getType(clazz).name());
            } else {
                jdbcType = JdbcType.getType(JavaType.getType(clazz).name());
            }
            columnInfo.setType(jdbcType.name());
            columnInfo.setNotnull(false);
        }
        if (sqlColumn != null && sqlColumn.length() != 0) {
            columnInfo.setLength(sqlColumn.length());
            columnInfo.setScale(sqlColumn.scale());
        } else {
            columnInfo.setLength(jdbcType.getLength());
        }
        if (sqlColumn != null && sqlColumn.scale() != 0) {
            columnInfo.setScale(sqlColumn.scale());
        } else {
            columnInfo.setScale(jdbcType.getScale());
        }
        if (sqlColumn != null && StringUtil.isNotEmpty(sqlColumn.def())) {
            columnInfo.setDfltValue(sqlColumn.def());
        }
        return columnInfo;
    }

    /**
     * 返回带转义表名,优先级 tableName第一，注解第二，类名第三
     *
     * @param table
     * @param common
     * @return
     */
    private static String getTableName(Table table, Common common) {
        String tableName = getTableName(table.getSchema(), table.getName());
        return SqlBeanUtil.isToUpperCase(common) ? tableName.toUpperCase() : tableName;
    }

    /**
     * 返回带schema表名
     *
     * @param schema
     * @param tableName
     * @return
     */
    private static String getTableName(String schema, String tableName) {
        if (StringUtil.isNotEmpty(schema)) {
            tableName = schema + SqlConstant.POINT + tableName;
        }
        return tableName;
    }

    /**
     * 返回column语句
     *
     * @param select
     * @return
     */
    private static String column(Select select) {
        StringBuffer columnSql = new StringBuffer();
        if (select.getColumnList() != null && select.getColumnList().size() != 0) {
            for (int i = 0; i < select.getColumnList().size(); i++) {
                String tableAlias = select.getColumnList().get(i).getTableAlias();
                String columnName = select.getColumnList().get(i).getName();
                String transferred = SqlBeanUtil.getTransferred(select);
                if (SqlBeanUtil.isToUpperCase(select)) {
                    columnName = columnName.toUpperCase();
                }
                boolean existAlias = StringUtil.isNotEmpty(select.getColumnList().get(i).getAlias());
                if (existAlias) {
                    columnSql.append(SqlConstant.BEGIN_BRACKET);
                }
                if (StringUtil.isNotEmpty(tableAlias)) {
                    columnSql.append(SqlBeanUtil.getTableFieldFullName(select, tableAlias, columnName));
                } else {
                    columnSql.append(columnName);
                }
                if (existAlias) {
                    columnSql.append(SqlConstant.END_BRACKET);
                    columnSql.append(SqlConstant.AS);
                    columnSql.append(transferred);
                    columnSql.append(select.getColumnList().get(i).getAlias());
                    columnSql.append(transferred);
                }
                columnSql.append(SqlConstant.COMMA);
            }
            columnSql.deleteCharAt(columnSql.length() - SqlConstant.COMMA.length());
        }
        return columnSql.toString();
    }

    /**
     * 返回from的表名包括别名
     *
     * @param common
     * @return
     */
    private static String fromFullName(Common common) {
        String transferred = SqlBeanUtil.getTransferred(common);
        StringBuffer fromSql = new StringBuffer();
        String tableName = common.getTable().getName();
        String schema = common.getTable().getSchema();
        if (SqlBeanUtil.isToUpperCase(common)) {
            tableName = tableName.toUpperCase();
            schema = schema.toUpperCase();
        }
        if (StringUtil.isNotEmpty(schema)) {
            fromSql.append(schema);
            fromSql.append(SqlConstant.POINT);
        }
        fromSql.append(tableName);
        fromSql.append(SqlConstant.SPACES);
        fromSql.append(transferred);
        fromSql.append(common.getTable().getAlias());
        fromSql.append(transferred);
        return fromSql.toString();
    }

    /**
     * 返回innerJoin语句
     *
     * @param select
     * @return
     */
    private static String joinSql(Select select) {
        StringBuffer joinSql = new StringBuffer();
        if (select != null && select.getJoin().size() != 0) {
            for (int i = 0; i < select.getJoin().size(); i++) {
                Join join = select.getJoin().get(i);
                switch (join.getJoinType()) {
                    case INNER_JOIN:
                        joinSql.append(SqlConstant.INNER_JOIN);
                        break;
                    case LEFT_JOIN:
                        joinSql.append(SqlConstant.LEFT_JOIN);
                        break;
                    case RIGHT_JOIN:
                        joinSql.append(SqlConstant.RIGHT_JOIN);
                        break;
                    case FULL_JOIN:
                        joinSql.append(SqlConstant.FULL_JOIN);
                        break;
                }
                String schema = join.getSchema();
                String tableName = join.getTableName();
                String tableAlias = join.getTableAlias();
                String tableKeyword = SqlBeanUtil.getTableFieldFullName(select, tableAlias, join.getTableKeyword());
                String mainKeyword = SqlBeanUtil.getTableFieldFullName(select, select.getTable().getAlias(), join.getMainKeyword());
                if (SqlBeanUtil.isToUpperCase(select)) {
                    schema = schema.toUpperCase();
                    tableName = tableName.toUpperCase();
                    tableAlias = tableAlias.toUpperCase();
                }
                if (StringUtil.isNotEmpty(schema)) {
                    joinSql.append(schema);
                    joinSql.append(SqlConstant.POINT);
                }
                joinSql.append(tableName);
                joinSql.append(SqlConstant.SPACES);
                String transferred = SqlBeanUtil.getTransferred(select);
                joinSql.append(transferred);
                joinSql.append(tableAlias);
                joinSql.append(transferred);
                joinSql.append(SqlConstant.ON);
                if (StringUtil.isNotEmpty(join.getOn())) {
                    joinSql.append(join.getOn());
                } else {
                    joinSql.append(tableKeyword);
                    joinSql.append(SqlConstant.EQUAL_TO);
                    joinSql.append(mainKeyword);
                }
                if (i < select.getJoin().size() - 1) {
                    joinSql.append(SqlConstant.SPACES);
                }
            }
        }
        return joinSql.toString();
    }

    /**
     * 返回field及values语句
     *
     * @param common
     * @param objectList
     * @return
     * @throws IllegalArgumentException
     */
    private static String fieldAndValuesSql(Common common, List objectList) throws IllegalArgumentException {
        String tableName = getTableName(common.getTable(), common);
        StringBuffer fieldSql = new StringBuffer();
        StringBuffer valueSql = new StringBuffer();
        StringBuffer fieldAndValuesSql = new StringBuffer();
        List<String> valueSqlList = new ArrayList<>();
        String transferred = SqlBeanUtil.getTransferred(common);
        SqlTable sqlTable = SqlBeanUtil.getSqlTable(objectList.get(0).getClass());
        //获取sqlbean的全部字段
        List<Field> fieldList = SqlBeanUtil.getBeanAllField(objectList.get(0).getClass());
        if (common.getSqlBeanDB().getDbType() == DbType.Oracle) {
            if (objectList != null && objectList.size() > 1) {
                fieldAndValuesSql.append(SqlConstant.INSERT_ALL_INTO);
            } else {
                fieldAndValuesSql.append(SqlConstant.INSERT_INTO);
            }
        } else {
            fieldAndValuesSql.append(SqlConstant.INSERT_INTO);
        }
        Date date = new Date();
        for (int i = 0; i < objectList.size(); i++) {
            //每次必须清空
            valueSql.delete(0, valueSql.length());
            //只有在循环第一遍的时候才会处理
            if (i == 0) {
                fieldSql.append(SqlConstant.BEGIN_BRACKET);
            }
            valueSql.append(SqlConstant.BEGIN_BRACKET);
            int existId = 0;
            for (Field field : fieldList) {
                if (SqlBeanUtil.isIgnore(field)) {
                    continue;
                }
                SqlId sqlId = field.getAnnotation(SqlId.class);
                SqlDefaultValue sqlDefaultValue = field.getAnnotation(SqlDefaultValue.class);
                if (sqlId != null) {
                    existId++;
                }
                if (existId > 1) {
                    throw new SqlBeanException("请正确的标识id字段，id字段只能标识一个，但我们在'" + field.getDeclaringClass().getName() + "'此实体类或其父类找到了不止一处");
                }
                //只有在循环第一遍的时候才会处理
                if (i == 0) {
                    String tableFieldName = SqlBeanUtil.getTableFieldName(field, sqlTable);
                    //如果此字段非id字段 或者 此字段为id字段但是不是自增的id则生成该字段的insert语句
                    if (sqlId == null || (sqlId != null && sqlId.type() != IdType.AUTO)) {
                        fieldSql.append(transferred + (SqlBeanUtil.isToUpperCase(common) ? tableFieldName.toUpperCase() : tableFieldName) + transferred);
                        fieldSql.append(SqlConstant.COMMA);
                    }
                }
                if (sqlId != null && sqlId.type() == IdType.AUTO) {
                    continue;
                }
                Object value = ReflectUtil.instance().get(objectList.get(i).getClass(), objectList.get(i), field.getName());
                //如果此字段为id且需要生成唯一id
                if (sqlId != null && sqlId.type() != IdType.AUTO && sqlId.type() != IdType.NORMAL) {
                    if (StringUtil.isEmpty(value)) {
                        value = common.getSqlBeanDB().getSqlBeanConfig().getUniqueIdProcessor().uniqueId(sqlId.type());
                        ReflectUtil.instance().set(objectList.get(i).getClass(), objectList.get(i), field.getName(), value);
                    }
                    valueSql.append(SqlBeanUtil.getSqlValue(common, value));
                } else if (value == null && field.isAnnotationPresent(SqlInsertTime.class) && SqlBeanUtil.whatType(field.getType().getName()) == WhatType.DATE_TYPE) {
                    //如果标识插入时间的字段为空则自动填充
                    valueSql.append(SqlBeanUtil.getSqlValue(common, date));
                    ReflectUtil.instance().set(objectList.get(i).getClass(), objectList.get(i), field.getName(), date);
                } else if (value == null && sqlDefaultValue != null && (sqlDefaultValue.with() == FillWith.INSERT || sqlDefaultValue.with() == FillWith.TOGETHER)) {
                    Object defaultValue = SqlBeanUtil.getDefaultValue(field.getType().getName());
                    valueSql.append(SqlBeanUtil.getSqlValue(common, defaultValue));
                    ReflectUtil.instance().set(objectList.get(i).getClass(), objectList.get(i), field.getName(), defaultValue);
                } else if (field.isAnnotationPresent(SqlLogically.class) && value == null) {
                    //如果标识逻辑删除的字段为空则自动填充
                    valueSql.append(0);
                    ReflectUtil.instance().set(objectList.get(i).getClass(), objectList.get(i), field.getName(), field.getType() == Boolean.class || field.getType() == boolean.class ? false : 0);
                } else {
                    valueSql.append(SqlBeanUtil.getSqlValue(common, ReflectUtil.instance().get(objectList.get(i).getClass(), objectList.get(i), field.getName())));
                }
                valueSql.append(SqlConstant.COMMA);
            }
            valueSql.deleteCharAt(valueSql.length() - SqlConstant.COMMA.length());
            valueSql.append(SqlConstant.END_BRACKET);
            valueSqlList.add(valueSql.toString());
            //只有在循环第一遍的时候才会处理
            if (i == 0) {
                fieldSql.deleteCharAt(fieldSql.length() - SqlConstant.COMMA.length());
                fieldSql.append(SqlConstant.END_BRACKET);
            }
        }
        if (common.getSqlBeanDB().getDbType() == DbType.Oracle) {
            for (int k = 0; k < valueSqlList.size(); k++) {
                if (k > 0) {
                    fieldAndValuesSql.append(SqlConstant.INTO);
                }
                fieldAndValuesSql.append(tableName);
                fieldAndValuesSql.append(fieldSql.toString());
                fieldAndValuesSql.append(SqlConstant.VALUES);
                fieldAndValuesSql.append(valueSqlList.get(k));
            }
            if (objectList != null && objectList.size() > 1) {
                fieldAndValuesSql.append(SqlConstant.SELECT_DUAL);
            }
        } else {
            for (int k = 0; k < valueSqlList.size(); k++) {
                if (k == 0) {
                    fieldAndValuesSql.append(tableName);
                    fieldAndValuesSql.append(fieldSql.toString());
                    fieldAndValuesSql.append(SqlConstant.VALUES);
                }
                fieldAndValuesSql.append(valueSqlList.get(k));
                fieldAndValuesSql.append(SqlConstant.COMMA);
            }
            fieldAndValuesSql.deleteCharAt(fieldAndValuesSql.length() - SqlConstant.COMMA.length());
        }
        return fieldAndValuesSql.toString();
    }

    /**
     * 返回set语句
     *
     * @param update
     * @return
     */
    private static String setSql(Update update) {
        StringBuffer setSql = new StringBuffer();
        String transferred = SqlBeanUtil.getTransferred(update);
        String[] filterFields = update.getFilterFields();
        Object bean = update.getUpdateBean();
        SqlTable sqlTable = SqlBeanUtil.getSqlTable(bean.getClass());
        List<Field> fieldList = SqlBeanUtil.getBeanAllField(bean.getClass());
        Date date = new Date();
        for (Field field : fieldList) {
            if (SqlBeanUtil.isIgnore(field)) {
                continue;
            }
            String name = SqlBeanUtil.getTableFieldName(field, sqlTable);
            if (SqlBeanUtil.isFilter(filterFields, name)) {
                continue;
            }
            Object objectValue = ReflectUtil.instance().get(bean.getClass(), bean, field.getName());
            if (update.isUpdateNotNull() && objectValue == null && !field.isAnnotationPresent(SqlUpdateTime.class) && !field.isAnnotationPresent(SqlVersion.class)) {
                continue;
            }
            if (!update.isOptimisticLock() && objectValue == null && field.isAnnotationPresent(SqlVersion.class)) {
                continue;
            }
            Object value = ReflectUtil.instance().get(bean.getClass(), bean, field.getName());
            SqlDefaultValue sqlDefaultValue = field.getAnnotation(SqlDefaultValue.class);
            setSql.append(transferred);
            setSql.append(SqlBeanUtil.isToUpperCase(update) ? name.toUpperCase() : name);
            setSql.append(transferred);
            setSql.append(SqlConstant.EQUAL_TO);
            if (update.isOptimisticLock() && field.isAnnotationPresent(SqlVersion.class)) {
                Object o = SqlBeanUtil.updateVersion(field.getType().getName(), objectValue);
                setSql.append(SqlBeanUtil.getSqlValue(update, o));
            } else if (value == null && field.isAnnotationPresent(SqlUpdateTime.class) && SqlBeanUtil.whatType(field.getType().getName()) == WhatType.DATE_TYPE) {
                setSql.append(SqlBeanUtil.getSqlValue(update, date));
                ReflectUtil.instance().set(bean.getClass(), bean, field.getName(), date);
            } else if (value == null && sqlDefaultValue != null && (sqlDefaultValue.with() == FillWith.UPDATE || sqlDefaultValue.with() == FillWith.TOGETHER)) {
                Object defaultValue = SqlBeanUtil.getDefaultValue(field.getType().getName());
                setSql.append(SqlBeanUtil.getSqlValue(update, defaultValue));
                ReflectUtil.instance().set(bean.getClass(), bean, field.getName(), defaultValue);
            } else {
                setSql.append(SqlBeanUtil.getSqlValue(update, objectValue));
            }
            setSql.append(SqlConstant.COMMA);
        }
        setSql.deleteCharAt(setSql.length() - SqlConstant.COMMA.length());
        return setSql.toString();
    }

    /**
     * 返回where语句
     *
     * @param commonCondition
     * @param bean
     * @return
     */
    @SuppressWarnings("unchecked")
    public static String whereSql(CommonCondition commonCondition, Object bean) {
        return conditionHandle(ConditionType.WHERE, commonCondition, commonCondition.getWhere(), commonCondition.getAgrs(), bean, commonCondition.where(), commonCondition.getWhereMap(), commonCondition.getWhereWrapper());
    }

    /**
     * 返回groupBy语句
     *
     * @param select
     * @return
     */
    private static String groupBySql(Select select) {
        return groupByAndOrderBySql(SqlConstant.GROUP_BY, select);
    }

    /**
     * @param select
     * @return
     */
    private static String havingSql(Select select) {
        return conditionHandle(ConditionType.HAVING, select, select.getHaving(), select.getHavingArgs(), null, select.having(), select.getHavingMap(), select.getHavingWrapper());
    }

    /**
     * 返回orderBy语句
     *
     * @param select
     * @return
     */
    private static String orderBySql(Select select) {
        return groupByAndOrderBySql(SqlConstant.ORDER_BY, select);
    }

    /**
     * 返回orderBy和groupBy语句
     *
     * @param type   SqlHelperCons.ORDER_BY or SqlHelperCons.GROUP_BY
     * @param select
     * @return
     */
    private static String groupByAndOrderBySql(String type, Select select) {
        StringBuffer groupByAndOrderBySql = new StringBuffer();
        Column[] columns;
        if (SqlConstant.ORDER_BY.equals(type)) {
            columns = select.getOrderBy().toArray(new Column[]{});
        } else {
            columns = select.getGroupBy().toArray(new Column[]{});
        }
        if (columns != null && columns.length != 0) {
            groupByAndOrderBySql.append(type);
            for (int i = 0; i < columns.length; i++) {
                Column column = columns[i];
                if (StringUtil.isNotEmpty(column.getTableAlias())) {
                    groupByAndOrderBySql.append(column.getTableAlias());
                    groupByAndOrderBySql.append(SqlConstant.POINT);
                }
                groupByAndOrderBySql.append(column.getName());
                if (SqlConstant.ORDER_BY.equals(type)) {
                    groupByAndOrderBySql.append(SqlConstant.SPACES);
                    groupByAndOrderBySql.append(select.getOrderBy().get(i).getSqlSort().name());
                    groupByAndOrderBySql.append(SqlConstant.SPACES);
                }
                groupByAndOrderBySql.append(SqlConstant.COMMA);
            }
            groupByAndOrderBySql.deleteCharAt(groupByAndOrderBySql.length() - SqlConstant.COMMA.length());
        } else {
            if (SqlConstant.ORDER_BY.equals(type) && select.getSqlBeanDB().getDbType() == DbType.SQLServer && SqlBeanUtil.isUsePage(select) && !SqlBeanUtil.isCount(select)) {
                groupByAndOrderBySql.append(type);
                String tableFieldFullName = SqlBeanUtil.getTableFieldFullName(select, select.getTable().getAlias(), select.getPage().getIdName());
                groupByAndOrderBySql.append(SqlBeanUtil.isToUpperCase(select) ? tableFieldFullName.toUpperCase() : tableFieldFullName);
            }
        }
        return groupByAndOrderBySql.toString();
    }

    /**
     * 条件处理
     *
     * @param conditionType   条件类型（where还是 having）
     * @param common          公共类
     * @param conditionString 条件字符串（优先级1）
     * @param args            条件字符串参数
     * @param condition       简单条件（优先级3）
     * @param wrapper         条件包装器（优先级2）
     * @return
     */
    private static String conditionHandle(ConditionType conditionType, Common common, String conditionString, Object[] args, Object bean, Condition condition, ListMultimap<String, ConditionInfo> conditionMap, Wrapper wrapper) {
        StringBuffer conditionSql = new StringBuffer();
        if (ConditionType.WHERE == conditionType) {
            conditionSql.append(versionCondition(common, bean));
            conditionSql.append(logicallyDeleteCondition(common));
        }
        // 优先级1 使用条件字符串拼接
        if (conditionString != null && !"".equals(conditionString)) {
            if (conditionSql.length() > 0) {
                conditionSql.append(SqlConstant.AND);
            }
            conditionSql.append(SqlConstant.BEGIN_BRACKET);
            if (args != null && args.length > 0) {
                conditionSql.append(SqlBeanUtil.getCondition(common, conditionString, args));
            } else if (conditionString.indexOf("${") > -1 && bean != null) {
                conditionSql.append(SqlBeanUtil.getCondition(common, conditionString, bean));
            } else {
                conditionSql.append(conditionString);
            }
            conditionSql.append(SqlConstant.END_BRACKET);
        }
        // 优先级2 使用条件包装器
        else if (wrapper != null && !wrapper.getDataList().isEmpty()) {
            if (conditionSql.length() > 0) {
                conditionSql.append(SqlConstant.AND);
            }
            conditionSql.append(conditionWrapperHandle(common, wrapper));
        }
        // 优先级3 使用简单的条件
        else if (condition != null && condition.getDataList().size() > 0) {
            if (conditionSql.length() > 0) {
                conditionSql.append(SqlConstant.AND);
            }
            conditionSql.append(SqlConstant.BEGIN_BRACKET);
            // 遍历所有条件
            List<ConditionData> conditionDataList = condition.getDataList();
            for (int i = 0; i < conditionDataList.size(); i++) {
                ConditionInfo conditionInfo = (ConditionInfo) conditionDataList.get(i).getItem();
                // 遍历sql逻辑处理
                if (i != 0 && i < condition.getDataList().size()) {
                    conditionSql.append(getLogic(conditionDataList.get(i).getSqlLogic()));
                }
                if (SqlBeanUtil.isToUpperCase(common)) {
                    conditionInfo.setName(conditionInfo.getName().toUpperCase());
                }
                conditionSql.append(valueOperator(common, conditionInfo));
            }
            conditionSql.append(SqlConstant.END_BRACKET);
        }
        //优先级4 过时，未来版本将会移除
        else if (conditionMap.size() > 0) {
            if (conditionSql.length() > 0) {
                conditionSql.append(SqlConstant.AND);
            }
            conditionSql.append(SqlConstant.BEGIN_BRACKET);
            int i = 0;
            // 遍历所有条件
            Collection<Map.Entry<String, ConditionInfo>> sqlConditionEntryCollection = conditionMap.entries();
            for (Map.Entry<String, ConditionInfo> sqlConditionEntry : sqlConditionEntryCollection) {
                ConditionInfo conditionInfo = sqlConditionEntry.getValue();
                // 遍历sql逻辑处理
                if (i != 0 && i < sqlConditionEntryCollection.size()) {
                    conditionSql.append(getLogic(conditionInfo.getSqlLogic()));
                }
                if (SqlBeanUtil.isToUpperCase(common)) {
                    conditionInfo.setName(conditionInfo.getName().toUpperCase());
                }
                conditionSql.append(valueOperator(common, conditionInfo));
                i++;
            }
            conditionSql.append(SqlConstant.END_BRACKET);
        }
        if (conditionSql.length() != 0) {
            conditionSql.insert(0, ConditionType.WHERE == conditionType ? SqlConstant.WHERE : SqlConstant.HAVING);
        }
        return conditionSql.toString();
    }

    /**
     * 乐观锁处理
     *
     * @param common
     * @param bean
     * @return
     */
    private static String versionCondition(Common common, Object bean) {
        if (!(common instanceof Update) || !((Update) common).isOptimisticLock()) {
            return "";
        }
        StringBuffer versionConditionSql = new StringBuffer();
        SqlTable sqlTable = SqlBeanUtil.getSqlTable(bean.getClass());
        Field versionField = null;
        //更新时乐观锁处理
        if (bean != null) {
            versionField = SqlBeanUtil.getVersionField(bean.getClass());
        }
        if (versionField != null) {
            boolean versionEffectiveness = SqlBeanUtil.versionEffectiveness(versionField.getType().getName());
            if (versionEffectiveness) {
                versionConditionSql.append(SqlConstant.BEGIN_BRACKET);
                versionConditionSql.append(SqlBeanUtil.getTableFieldName(versionField, sqlTable));
                Object versionValue = ReflectUtil.instance().get(bean.getClass(), bean, versionField.getName());
                versionConditionSql.append(versionValue == null ? SqlConstant.IS : SqlConstant.EQUAL_TO);
                versionConditionSql.append(SqlBeanUtil.getSqlValue(common, versionValue));
                versionConditionSql.append(SqlConstant.END_BRACKET);
            }
        }
        return versionConditionSql.toString();
    }

    /**
     * 逻辑删除处理（Select）
     *
     * @param common
     * @return
     */
    private static String logicallyDeleteCondition(Common common) {
        if (common instanceof Select && SqlBeanUtil.checkLogically(common.getBeanClass())) {
            StringBuffer logicallyDeleteSql = new StringBuffer();
            SqlTable sqlTable = SqlBeanUtil.getSqlTable(common.getBeanClass());
            Field logicallyDeleteField = SqlBeanUtil.getLogicallyField(common.getBeanClass());
            if (logicallyDeleteField != null) {
                logicallyDeleteSql.append(SqlConstant.BEGIN_BRACKET);
                logicallyDeleteSql.append(SqlBeanUtil.getTableFieldFullName(common, common.getTable().getAlias(), SqlBeanUtil.getTableFieldName(logicallyDeleteField, sqlTable)));
                logicallyDeleteSql.append(SqlConstant.EQUAL_TO);
                logicallyDeleteSql.append(0);
                logicallyDeleteSql.append(SqlConstant.END_BRACKET);
            }
            return logicallyDeleteSql.toString();
        }
        return "";
    }

    /**
     * 条件包装器解析
     *
     * @param common
     * @param wrapper
     * @return
     */
    private static String conditionWrapperHandle(Common common, Wrapper wrapper) {
        StringBuffer conditionSql = new StringBuffer();
        if (!wrapper.getDataList().isEmpty()) {
            conditionSql.append(SqlConstant.BEGIN_BRACKET);
            for (int i = 0; i < wrapper.getDataList().size(); i++) {
                ConditionData data = wrapper.getDataList().get(i);
                // 遍历sql逻辑处理
                if (i != 0 && i < wrapper.getDataList().size()) {
                    conditionSql.append(getLogic(data.getSqlLogic()));
                }
                Object item = data.getItem();
                if (item instanceof Cond) {
                    conditionSql.append(valueOperator(common, (Cond) item));
                } else {
                    conditionSql.append(conditionWrapperHandle(common, (Wrapper) item));
                }
            }
            conditionSql.append(SqlConstant.END_BRACKET);
        }
        return conditionSql.toString();
    }

    /**
     * 获取操作符
     *
     * @param conditionInfo
     * @return
     */
    private static String getOperator(ConditionInfo conditionInfo) {
        String operator = "";
        // 优先使用枚举类型的操作符
        if (conditionInfo.getSqlOperator() != null) {
            SqlOperator sqlOperator = conditionInfo.getSqlOperator();
            if (sqlOperator == SqlOperator.IS || sqlOperator == SqlOperator.IS_NULL) {
                operator = SqlConstant.IS;
            } else if (sqlOperator == SqlOperator.IS_NOT || sqlOperator == SqlOperator.IS_NOT_NULL) {
                operator = SqlConstant.IS_NOT;
            } else if (sqlOperator == SqlOperator.IN) {
                operator = SqlConstant.IN;
            } else if (sqlOperator == SqlOperator.NOT_IN) {
                operator = SqlConstant.NOT_IN;
            } else if (sqlOperator == SqlOperator.LIKE || sqlOperator == SqlOperator.LIKE_L || sqlOperator == SqlOperator.LIKE_R) {
                operator = SqlConstant.LIKE;
            } else if (sqlOperator == SqlOperator.NOT_LIKE || sqlOperator == SqlOperator.NOT_LIKE_L || sqlOperator == SqlOperator.NOT_LIKE_R) {
                operator = SqlConstant.NOT_LIKE;
            } else if (sqlOperator == SqlOperator.BETWEEN) {
                operator = SqlConstant.BETWEEN;
            } else if (sqlOperator == SqlOperator.GREATER_THAN) {
                operator = SqlConstant.GREATER_THAN;
            } else if (sqlOperator == SqlOperator.GREAT_THAN_OR_EQUAL_TO) {
                operator = SqlConstant.GREAT_THAN_OR_EQUAL_TO;
            } else if (sqlOperator == SqlOperator.LESS_THAN) {
                operator = SqlConstant.LESS_THAN;
            } else if (sqlOperator == SqlOperator.LESS_THAN_OR_EQUAL_TO) {
                operator = SqlConstant.LESS_THAN_OR_EQUAL_TO;
            } else if (sqlOperator == SqlOperator.EQUAL_TO) {
                operator = SqlConstant.EQUAL_TO;
            } else if (sqlOperator == SqlOperator.NOT_EQUAL_TO) {
                operator = SqlConstant.NOT_EQUAL_TO;
            }
        } else {
            operator = SqlConstant.EQUAL_TO;
        }
        return operator;
    }

    /**
     * 获取逻辑
     *
     * @param sqlLogic
     * @return
     */
    private static String getLogic(SqlLogic sqlLogic) {
        String logic = null;
        if (sqlLogic != null && !"".equals(sqlLogic)) {
            switch (sqlLogic) {
                case AND:
                    logic = SqlConstant.AND;
                    break;
                case OR:
                    logic = SqlConstant.OR;
                    break;
                case ORBracket:
                    logic = SqlConstant.OR_BRACKET;
                    break;
                case ANDBracket:
                    logic = SqlConstant.AND_BRACKET;
                    break;
            }
        } else {
            logic = SqlConstant.AND;
        }
        return logic;
    }

    /**
     * 值操作
     *
     * @param common
     * @param conditionInfo
     * @return
     */
    private static StringBuffer valueOperator(Common common, ConditionInfo conditionInfo) {
        StringBuffer sql = new StringBuffer();
        String operator = getOperator(conditionInfo);
        String transferred = SqlBeanUtil.getTransferred(common);
        boolean needEndBracket = false;
        Object[] betweenValues = null;
        Object value = conditionInfo.getValue();
        // 如果操作符为BETWEEN ，IN、NOT IN 则需额外处理
        if (conditionInfo.getSqlOperator() == SqlOperator.BETWEEN) {
            betweenValues = SqlBeanUtil.getObjectArray(value);
            if (betweenValues == null) {
                try {
                    throw new SqlBeanException("between 条件的值必须为Array或ArrayList");
                } catch (SqlBeanException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        } else if (conditionInfo.getSqlOperator() == SqlOperator.IN || conditionInfo.getSqlOperator() == SqlOperator.NOT_IN) {
            needEndBracket = true;
            Object[] in_notInValues = SqlBeanUtil.getObjectArray(value);
            if (in_notInValues == null) {
                in_notInValues = new Object[]{value};
            }
            StringBuffer in_notIn = new StringBuffer();
            if (in_notInValues != null && in_notInValues.length > 0) {
                for (int k = 0; k < in_notInValues.length; k++) {
                    if (in_notInValues[k] instanceof Original) {
                        in_notIn.append(((Original) in_notInValues[k]).getValue());
                    } else {
                        in_notIn.append(SqlBeanUtil.getSqlValue(common, in_notInValues[k]));
                    }
                    in_notIn.append(SqlConstant.COMMA);
                }
                in_notIn.deleteCharAt(in_notIn.length() - SqlConstant.COMMA.length());
                value = in_notIn.toString();
            }
        } else {
            value = conditionInfo.getValue();
            //对like操作符处理
            if (operator.indexOf(SqlConstant.LIKE) > -1) {
                if (conditionInfo.getSqlOperator() == SqlOperator.LIKE || conditionInfo.getSqlOperator() == SqlOperator.LIKE_L || conditionInfo.getSqlOperator() == SqlOperator.NOT_LIKE || conditionInfo.getSqlOperator() == SqlOperator.NOT_LIKE_L) {
                    value = SqlConstant.PERCENT + value;
                }
                if (conditionInfo.getSqlOperator() == SqlOperator.LIKE || conditionInfo.getSqlOperator() == SqlOperator.LIKE_R || conditionInfo.getSqlOperator() == SqlOperator.NOT_LIKE || conditionInfo.getSqlOperator() == SqlOperator.NOT_LIKE_R) {
                    value = value + SqlConstant.PERCENT;
                }
                value = SqlConstant.SINGLE_QUOTATION_MARK + value + SqlConstant.SINGLE_QUOTATION_MARK;
            } else if (value instanceof Original) {
                Original original = (Original) conditionInfo.getValue();
                value = original.getValue();
            } else {
                value = SqlBeanUtil.getSqlValue(common, value);
            }
        }
        //表别名
        if (StringUtil.isNotEmpty(conditionInfo.getTableAlias())) {
            sql.append(transferred);
            sql.append(conditionInfo.getTableAlias());
            sql.append(transferred);
            sql.append(SqlConstant.POINT);
        }
        //字段名
        sql.append(conditionInfo.getName());
        //操作符
        sql.append(operator);
        //值
        if (conditionInfo.getSqlOperator() == SqlOperator.BETWEEN) {
            sql.append(SqlBeanUtil.getSqlValue(common, betweenValues[0]));
            sql.append(SqlConstant.AND);
            sql.append(SqlBeanUtil.getSqlValue(common, betweenValues[1]));
        } else if (conditionInfo.getSqlOperator() == SqlOperator.IS_NULL || conditionInfo.getSqlOperator() == SqlOperator.IS_NOT_NULL) {
            sql.append("NULL ");
        } else {
            sql.append(value);
        }
        // in与not in 额外加结束括号
        if (needEndBracket) {
            sql.append(SqlConstant.END_BRACKET);
        }
        return sql;
    }

    /**
     * 返回MySQL,MariaDB,H2 分页语句
     *
     * @param select
     * @return
     */
    private static void mysqlPageDispose(Select select, StringBuffer sqlSb) {
        if (SqlBeanUtil.isUsePage(select)) {
            Integer[] param = pageParam(select);
            sqlSb.append(SqlConstant.LIMIT);
            sqlSb.append(param[0]);
            sqlSb.append(SqlConstant.COMMA);
            sqlSb.append(param[1]);
        }
    }

    /**
     * 返回PostgreSql,Sqlite,Hsql 分页语句
     *
     * @param select
     * @return
     */
    private static void postgreSqlPageDispose(Select select, StringBuffer sqlSb) {
        if (SqlBeanUtil.isUsePage(select)) {
            Integer[] param = pageParam(select);
            sqlSb.append(SqlConstant.LIMIT);
            sqlSb.append(param[1]);
            sqlSb.append(SqlConstant.OFFSET);
            sqlSb.append(param[0]);
        }
    }

    /**
     * Oracle 分页处理
     *
     * @param select
     * @param sqlSb
     */
    private static void oraclePageDispose(Select select, StringBuffer sqlSb) {
        //oracle 分页语句前缀
        if (SqlBeanUtil.isUsePage(select)) {
            Integer[] param = pageParam(select);
            StringBuffer beginSqlSb = new StringBuffer();
            beginSqlSb.append(SqlConstant.SELECT + SqlConstant.ALL + SqlConstant.FROM + SqlConstant.BEGIN_BRACKET);
            beginSqlSb.append(SqlConstant.SELECT + SqlConstant.TB + SqlConstant.POINT + SqlConstant.ALL + SqlConstant.COMMA + SqlConstant.ROWNUM + SqlConstant.RN + SqlConstant.FROM + SqlConstant.BEGIN_BRACKET);
            sqlSb.insert(0, beginSqlSb);
            StringBuffer endSb = new StringBuffer();
            endSb.append(SqlConstant.END_BRACKET + SqlConstant.TB + SqlConstant.WHERE + SqlConstant.ROWNUM + SqlConstant.LESS_THAN_OR_EQUAL_TO);
            endSb.append(param[1]);
            endSb.append(SqlConstant.END_BRACKET + SqlConstant.WHERE + SqlConstant.RN + SqlConstant.GREATER_THAN);
            endSb.append(param[0]);
            sqlSb.append(endSb);
        }
    }

    /**
     * DB2 分页处理
     *
     * @param select
     * @param sqlSb
     */
    private static void db2PageDispose(Select select, StringBuffer sqlSb) {
        //db2 分页语句前缀
        if (SqlBeanUtil.isUsePage(select)) {
            Integer[] param = pageParam(select);
            StringBuffer beginSqlSb = new StringBuffer();
            beginSqlSb.append(SqlConstant.SELECT + SqlConstant.ALL + SqlConstant.FROM + SqlConstant.BEGIN_BRACKET);
            beginSqlSb.append(SqlConstant.SELECT + SqlConstant.T + SqlConstant.POINT + SqlConstant.ALL + SqlConstant.COMMA + SqlConstant.ROWNUMBER);
            beginSqlSb.append(SqlConstant.OVER + SqlConstant.BEGIN_BRACKET + SqlConstant.SPACES + SqlConstant.END_BRACKET + SqlConstant.AS + SqlConstant.RN + SqlConstant.FROM + SqlConstant.BEGIN_BRACKET);
            sqlSb.insert(0, beginSqlSb);
            StringBuffer endSb = new StringBuffer();
            endSb.append(SqlConstant.END_BRACKET + SqlConstant.T + SqlConstant.SPACES + SqlConstant.END_BRACKET + SqlConstant.TB + SqlConstant.WHERE + SqlConstant.TB + SqlConstant.POINT + SqlConstant.RN + SqlConstant.BETWEEN);
            endSb.append(param[0]);
            endSb.append(SqlConstant.AND);
            endSb.append(param[1]);
            sqlSb.append(endSb);
        }
    }

    /**
     * 返回Derby 分页语句
     *
     * @param select
     * @return
     */
    private static void derbyPageDispose(Select select, StringBuffer sqlSb) {
        if (SqlBeanUtil.isUsePage(select)) {
            Integer[] param = pageParam(select);
            sqlSb.append(SqlConstant.OFFSET);
            sqlSb.append(param[0]);
            sqlSb.append(" ROWS FETCH NEXT ");
            sqlSb.append(param[1]);
            sqlSb.append(" ROWS ONLY");
        }
    }

    /**
     * 各个数据库的分页参数
     *
     * @param select
     * @return
     */
    public static Integer[] pageParam(Select select) {
        //当前页不能小于0
        if (select.getPage().getPagenum() < 0) {
            throw new SqlBeanException("当前页不能小于0");
        }
        //每页数量不能小于0
        if (select.getPage().getPagesize() < 0) {
            throw new SqlBeanException("每页数量不能小于0");
        }
        Integer[] param;
        //SQLServer2008
        if (DbType.SQLServer == select.getSqlBeanDB().getDbType()) {
            int pagenum = select.getPage().getStartByZero() ? select.getPage().getPagenum() + 1 : select.getPage().getPagenum();
            int top = pagenum * select.getPage().getPagesize();
            int begin = top - select.getPage().getPagesize();
            param = new Integer[]{top, begin};
        }
        //Oracle,DB2
        else if (DbType.Oracle == select.getSqlBeanDB().getDbType() || DbType.DB2 == select.getSqlBeanDB().getDbType()) {
            //startIndex = (当前页 * 每页显示的数量)，例如：(0 * 10)
            //endIndex = (当前页 * 每页显示的数量) + 每页显示的数量，例如：10 = (0 * 10) + 10
            //那么如果startIndex=0，endIndex=10，就是查询第0到10条数据
            int pagenum = select.getPage().getStartByZero() ? select.getPage().getPagenum() : select.getPage().getPagenum() > 0 ? select.getPage().getPagenum() - 1 : select.getPage().getPagenum();
            int startIndex = pagenum * select.getPage().getPagesize();
            int endIndex = (pagenum * select.getPage().getPagesize()) + select.getPage().getPagesize();
            param = new Integer[]{startIndex, endIndex};
        }
        //Mysql,MariaDB,PostgreSQL,Sqlite,Hsql
        else {
            int pagenum = select.getPage().getStartByZero() ? select.getPage().getPagenum() : select.getPage().getPagenum() > 0 ? select.getPage().getPagenum() - 1 : select.getPage().getPagenum();
            int limitOffset = pagenum * select.getPage().getPagesize();
            int limitAmount = select.getPage().getPagesize();
            param = new Integer[]{limitOffset, limitAmount};
        }
        return param;
    }


}
