package cn.vonce.sql.helper;

import cn.vonce.sql.annotation.*;
import cn.vonce.sql.config.SqlBeanDB;
import cn.vonce.sql.uitls.ReflectUtil;
import cn.vonce.sql.uitls.StringUtil;
import com.google.common.collect.ListMultimap;

import cn.vonce.sql.bean.*;
import cn.vonce.sql.constant.SqlHelperCons;
import cn.vonce.sql.enumerate.*;
import cn.vonce.sql.exception.SqlBeanException;
import cn.vonce.sql.uitls.SqlBeanUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
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
                sqlSb.append(SqlHelperCons.SELECT);
                sqlSb.append(SqlHelperCons.ALL);
                sqlSb.append(SqlHelperCons.FROM);
                sqlSb.append(SqlHelperCons.BEGIN_BRACKET);
            }
        }
        //标准Sql
        sqlSb.append(select.isUseDistinct() ? SqlHelperCons.SELECT_DISTINCT : SqlHelperCons.SELECT);
        //SqlServer 分页处理
        if (select.getSqlBeanDB().getDbType() == DbType.SQLServer) {
            if (SqlBeanUtil.isUsePage(select)) {
                sqlSb.append(SqlHelperCons.TOP);
                sqlSb.append(pageParam[0]);
                sqlSb.append(SqlHelperCons.ROW_NUMBER + SqlHelperCons.OVER + SqlHelperCons.BEGIN_BRACKET + orderSql + SqlHelperCons.END_BRACKET + SqlHelperCons.ROWNUM + SqlHelperCons.COMMA);
            }
        }
        //标准Sql
        sqlSb.append(column(select));
        sqlSb.append(SqlHelperCons.FROM);
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
                sqlSb.append(SqlHelperCons.END_BRACKET);
                sqlSb.append(SqlHelperCons.T);
                sqlSb.append(SqlHelperCons.WHERE);
                sqlSb.append(SqlHelperCons.T + SqlHelperCons.POINT + SqlHelperCons.ROWNUM);
                sqlSb.append(SqlHelperCons.GREATER_THAN);
                sqlSb.append(pageParam[1]);
            }
        }
        //标准Sql
        if (SqlBeanUtil.isCount(select) && StringUtil.isNotEmpty(groupBySql)) {
            sqlSb.insert(0, SqlHelperCons.SELECT + SqlHelperCons.COUNT + SqlHelperCons.BEGIN_BRACKET + SqlHelperCons.ALL + SqlHelperCons.END_BRACKET + SqlHelperCons.FROM + SqlHelperCons.BEGIN_BRACKET);
            sqlSb.append(SqlHelperCons.END_BRACKET + SqlHelperCons.AS + SqlHelperCons.T);
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
        sqlSb.append(SqlHelperCons.UPDATE);
        sqlSb.append(getTableName(SqlBeanUtil.getTable(update.getUpdateBean().getClass()), update));
        sqlSb.append(SqlHelperCons.SET);
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
            sql = fieldAndValuesSql(insert, SqlBeanUtil.getObjectArray(insert.getInsertBean()));
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
        sqlSb.append(SqlHelperCons.DELETE_FROM);
        sqlSb.append(getTableName(delete.getTable(), delete));
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
        sqlSb.append(SqlHelperCons.CREATE_TABLE);
        sqlSb.append(getTableName(create.getTable(), create));
        sqlSb.append(SqlHelperCons.BEGIN_BRACKET);
        Field idField = null;
        Field[] fields = create.getBeanClass().getDeclaredFields();
        String transferred = SqlBeanUtil.getTransferred(create);
        for (Field field : fields) {
            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }
            if (idField == null) {
                if (field.getAnnotation(SqlId.class) != null) {
                    idField = field;
                }
            }
            SqlColumn sqlColumn = field.getAnnotation(SqlColumn.class);
            if (sqlColumn != null && sqlColumn.ignore()) {
                continue;
            }
            ColumnInfo columnInfo = getColumnInfo(create.getSqlBeanDB().getDbType(), field.getType(), sqlColumn);
            String columnName = field.getName();
            if (sqlColumn != null) {
                columnName = sqlColumn.value();
            }
            sqlSb.append(transferred);
            sqlSb.append(columnName);
            sqlSb.append(transferred);
            sqlSb.append(SqlHelperCons.SPACES);
            sqlSb.append(columnInfo.getType().name());
            if (columnInfo.getLength() > 0) {
                sqlSb.append(SqlHelperCons.BEGIN_BRACKET);
                //字段长度
                sqlSb.append(columnInfo.getLength());
                if (columnInfo.getType().isFloat()) {
                    sqlSb.append(SqlHelperCons.COMMA);
                    sqlSb.append(columnInfo.getDecimal());
                }
                sqlSb.append(SqlHelperCons.END_BRACKET);
            }
            //是否为null
            if (columnInfo.getNotNull()) {
                sqlSb.append(SqlHelperCons.NOT_NULL);
            }
            //默认值
            if (StringUtil.isNotEmpty(columnInfo.getDef())) {
                sqlSb.append(SqlHelperCons.DEFAULT);
                sqlSb.append(SqlBeanUtil.getSqlValue(create, columnInfo.getDef(), columnInfo.getType()));
            }
            sqlSb.append(SqlHelperCons.COMMA);
        }
        //主键
        if (idField != null) {
            sqlSb.append(SqlHelperCons.PRIMARY_KEY);
            sqlSb.append(SqlHelperCons.BEGIN_BRACKET);
            sqlSb.append(SqlBeanUtil.getTableFieldName(idField));
            sqlSb.append(SqlHelperCons.END_BRACKET);
        } else {
            sqlSb.deleteCharAt(sqlSb.length() - 1);
        }
        sqlSb.append(SqlHelperCons.END_BRACKET);
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
            backupSql.append(SqlHelperCons.CREATE_TABLE);
            backupSql.append(getTableName(targetSchema, backup.getTargetTableName()));
            backupSql.append(SqlHelperCons.SPACES);
            backupSql.append(SqlHelperCons.AS);
        }
        backupSql.append(SqlHelperCons.SELECT);
        if (backup.getColumns() != null && backup.getColumns().length > 0) {
            for (Column column : backup.getColumns()) {
                backupSql.append(column.getName());
                backupSql.append(SqlHelperCons.COMMA);
            }
            backupSql.delete(backupSql.length() - SqlHelperCons.COMMA.length(), backupSql.length());
        } else {
            backupSql.append(SqlHelperCons.ALL);
        }
        //如果是SQLServer、PostgreSQL数据库则需要拼接INTO：select * into A from B
        if (DbType.SQLServer == backup.getSqlBeanDB().getDbType() || DbType.PostgreSQL == backup.getSqlBeanDB().getDbType()) {
            backupSql.append(SqlHelperCons.INTO);
            backupSql.append(getTableName(targetSchema, backup.getTargetTableName()));
        }
        backupSql.append(SqlHelperCons.FROM);
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
        copySql.append(SqlHelperCons.INSERT_INTO);
        copySql.append(getTableName(targetSchema, copy.getTargetTableName()));
        if (copy.getColumns() != null && copy.getColumns().length > 0) {
            for (Column column : copy.getColumns()) {
                columnSql.append(column.getName());
                columnSql.append(SqlHelperCons.COMMA);
            }
            columnSql.delete(columnSql.length() - SqlHelperCons.COMMA.length(), columnSql.length());
            copySql.append(SqlHelperCons.SPACES);
            copySql.append(SqlHelperCons.BEGIN_BRACKET);
            copySql.append(columnSql);
            copySql.append(SqlHelperCons.END_BRACKET);
        }
        copySql.append(SqlHelperCons.SPACES);
        copySql.append(SqlHelperCons.SELECT);
        if (copy.getColumns() != null && copy.getColumns().length > 0) {
            copySql.append(columnSql);
        } else {
            copySql.append(SqlHelperCons.ALL);
        }
        copySql.append(SqlHelperCons.FROM);
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
        dropSql.append("DROP TABLE IF EXISTS ");
        dropSql.append(getTableName(drop.getTable(), drop));
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
        if (sqlColumn != null && sqlColumn.type() != JdbcType.NOTHING) {
            columnInfo.setType(sqlColumn.type());
            columnInfo.setNotNull(sqlColumn.notNull());
        } else {
            if (dbType == DbType.SQLite) {
                columnInfo.setType(JdbcType.getType(SQLiteJavaType.getType(clazz).name()));
            } else {
                columnInfo.setType(JdbcType.getType(JavaType.getType(clazz).name()));
            }
            columnInfo.setNotNull(false);
        }
        if (sqlColumn != null && sqlColumn.length() != 0) {
            columnInfo.setLength(sqlColumn.length());
            columnInfo.setDecimal(sqlColumn.decimal());
        } else {
            columnInfo.setLength(columnInfo.getType().getLength());
        }
        if (sqlColumn != null && sqlColumn.decimal() != 0) {
            columnInfo.setDecimal(sqlColumn.decimal());
        } else {
            columnInfo.setDecimal(columnInfo.getType().getDecimal());
        }
        if (sqlColumn != null && StringUtil.isNotEmpty(sqlColumn.def())) {
            columnInfo.setDef(sqlColumn.def());
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
            tableName = schema + SqlHelperCons.POINT + tableName;
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
                String schema = select.getColumnList().get(i).getSchema();
                String tableAlias = select.getColumnList().get(i).getTableAlias();
                String columnName = select.getColumnList().get(i).getName();
                String transferred = SqlBeanUtil.getTransferred(select);
                if (SqlBeanUtil.isToUpperCase(select)) {
                    schema = schema.toUpperCase();
                    columnName = columnName.toUpperCase();
                }
                boolean existAlias = StringUtil.isNotEmpty(select.getColumnList().get(i).getAlias());
                if (existAlias) {
                    columnSql.append(SqlHelperCons.BEGIN_BRACKET);
                }
                if (StringUtil.isNotEmpty(tableAlias)) {
                    columnSql.append(SqlBeanUtil.getTableFieldFullName(select, schema, tableAlias, columnName));
                } else {
                    columnSql.append(columnName);
                }
                if (existAlias) {
                    columnSql.append(SqlHelperCons.END_BRACKET);
                    columnSql.append(SqlHelperCons.AS);
                    columnSql.append(transferred);
                    columnSql.append(select.getColumnList().get(i).getAlias());
                    columnSql.append(transferred);
                }
                columnSql.append(SqlHelperCons.COMMA);
            }
            columnSql.deleteCharAt(columnSql.length() - SqlHelperCons.COMMA.length());
        }
        return columnSql.toString();
    }

    /**
     * 返回from的表名包括别名
     *
     * @param select
     * @return
     */
    private static String fromFullName(Select select) {
        String transferred = SqlBeanUtil.getTransferred(select);
        StringBuffer fromSql = new StringBuffer();
        String tableName = select.getTable().getName();
        String schema = select.getTable().getSchema();
        if (SqlBeanUtil.isToUpperCase(select)) {
            tableName = tableName.toUpperCase();
            schema = schema.toUpperCase();
        }
        if (StringUtil.isNotEmpty(schema)) {
            fromSql.append(schema);
            fromSql.append(SqlHelperCons.POINT);
        }
        fromSql.append(tableName);
        fromSql.append(SqlHelperCons.SPACES);
        fromSql.append(transferred);
        fromSql.append(select.getTable().getAlias());
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
                        joinSql.append(SqlHelperCons.INNER_JOIN);
                        break;
                    case LEFT_JOIN:
                        joinSql.append(SqlHelperCons.LEFT_JOIN);
                        break;
                    case RIGHT_JOIN:
                        joinSql.append(SqlHelperCons.RIGHT_JOIN);
                        break;
                    case FULL_JOIN:
                        joinSql.append(SqlHelperCons.FULL_JOIN);
                        break;
                }
                String schema = join.getSchema();
                String tableName = join.getTableName();
                String tableAlias = join.getTableAlias();
                String tableKeyword = SqlBeanUtil.getTableFieldFullName(select, schema, tableAlias, join.getTableKeyword());
                String mainKeyword = SqlBeanUtil.getTableFieldFullName(select, select.getTable().getSchema(), select.getTable().getAlias(), join.getMainKeyword());
                if (SqlBeanUtil.isToUpperCase(select)) {
                    schema = schema.toUpperCase();
                    tableName = tableName.toUpperCase();
                    tableAlias = tableAlias.toUpperCase();
//                    tableKeyword = tableKeyword.toUpperCase();
//                    mainKeyword = mainKeyword.toUpperCase();
                }
                if (StringUtil.isNotEmpty(schema)) {
                    joinSql.append(schema);
                    joinSql.append(SqlHelperCons.POINT);
                }
                joinSql.append(tableName);
                joinSql.append(SqlHelperCons.SPACES);
                String transferred = SqlBeanUtil.getTransferred(select);
                joinSql.append(transferred);
                joinSql.append(tableAlias);
                joinSql.append(transferred);
                joinSql.append(SqlHelperCons.ON);
                if (StringUtil.isNotEmpty(join.getOn())) {
                    joinSql.append(join.getOn());
                } else {
                    joinSql.append(tableKeyword);
                    joinSql.append(SqlHelperCons.EQUAL_TO);
                    joinSql.append(mainKeyword);
                }
                if (i < select.getJoin().size() - 1) {
                    joinSql.append(SqlHelperCons.SPACES);
                }
            }
        }
        return joinSql.toString();
    }

    /**
     * 返回field及values语句
     *
     * @param common
     * @param objects
     * @return
     * @throws IllegalArgumentException
     */
    private static String fieldAndValuesSql(Common common, Object[] objects) throws IllegalArgumentException {
        String tableName = getTableName(common.getTable(), common);
        StringBuffer fieldSql = new StringBuffer();
        StringBuffer valueSql = new StringBuffer();
        StringBuffer fieldAndValuesSql = new StringBuffer();
        List<String> valueSqlList = new ArrayList<>();
        String transferred = SqlBeanUtil.getTransferred(common);
        //获取sqlbean的全部字段
        Field[] fields;
        if (objects[0].getClass().getAnnotation(SqlUnion.class) != null) {
            fields = objects[0].getClass().getSuperclass().getDeclaredFields();
        } else {
            fields = objects[0].getClass().getDeclaredFields();
        }
        if (common.getSqlBeanDB().getDbType() == DbType.Oracle) {
            if (objects != null && objects.length > 1) {
                fieldAndValuesSql.append(SqlHelperCons.INSERT_ALL_INTO);
            } else {
                fieldAndValuesSql.append(SqlHelperCons.INSERT_INTO);
            }
        } else {
            fieldAndValuesSql.append(SqlHelperCons.INSERT_INTO);
        }
        Date date = new Date();
        for (int i = 0; i < objects.length; i++) {
            //每次必须清空
            valueSql.delete(0, valueSql.length());
            //只有在循环第一遍的时候才会处理
            if (i == 0) {
                fieldSql.append(SqlHelperCons.BEGIN_BRACKET);
            }
            valueSql.append(SqlHelperCons.BEGIN_BRACKET);
            int existId = 0;
            for (Field field : fields) {
                if (Modifier.isStatic(field.getModifiers())) {
                    continue;
                }
                if (SqlBeanUtil.isIgnore(field)) {
                    continue;
                }
                SqlId sqlId = field.getAnnotation(SqlId.class);
                if (sqlId != null) {
                    existId++;
                }
                if (existId > 1) {
                    throw new SqlBeanException("请正确的标识id字段，id字段只能标识一个，但我们在'" + field.getDeclaringClass().getName() + "'此实体类或其父类找到了不止一处");
                }
                //只有在循环第一遍的时候才会处理
                if (i == 0) {
                    String tableFieldName = SqlBeanUtil.getTableFieldName(field);
                    //如果此字段非id字段 或者 此字段为id字段但是不是自增的id则生成该字段的insert语句
                    if (sqlId == null || (sqlId != null && sqlId.type() != IdType.AUTO)) {
                        fieldSql.append(transferred + (SqlBeanUtil.isToUpperCase(common) ? tableFieldName.toUpperCase() : tableFieldName) + transferred);
                        fieldSql.append(SqlHelperCons.COMMA);
                    }
                }
                Object value = ReflectUtil.instance().get(objects[i].getClass(), objects[i], field.getName());
                //如果此字段为id且需要生成唯一id
                if (sqlId != null && sqlId.type() != IdType.AUTO && sqlId.type() != IdType.NORMAL) {
                    if (StringUtil.isEmpty(value)) {
                        value = common.getSqlBeanDB().getSqlBeanConfig().getUniqueIdProcessor().uniqueId(sqlId.type());
                    }
                    valueSql.append(SqlBeanUtil.getSqlValue(common, value));
                } else if (field.isAnnotationPresent(SqlInsertTime.class) && SqlBeanUtil.whatType(field.getType().getName()) == WhatType.DATE_TYPE && value == null) {
                    valueSql.append(SqlBeanUtil.getSqlValue(common, date));
                } else {
                    valueSql.append(SqlBeanUtil.getSqlValue(common, ReflectUtil.instance().get(objects[i].getClass(), objects[i], field.getName())));
                }
                valueSql.append(SqlHelperCons.COMMA);
            }
            valueSql.deleteCharAt(valueSql.length() - SqlHelperCons.COMMA.length());
            valueSql.append(SqlHelperCons.END_BRACKET);
            valueSqlList.add(valueSql.toString());
            //只有在循环第一遍的时候才会处理
            if (i == 0) {
                fieldSql.deleteCharAt(fieldSql.length() - SqlHelperCons.COMMA.length());
                fieldSql.append(SqlHelperCons.END_BRACKET);
            }
        }
        if (common.getSqlBeanDB().getDbType() == DbType.Oracle) {
            for (int k = 0; k < valueSqlList.size(); k++) {
                if (k > 0) {
                    fieldAndValuesSql.append(SqlHelperCons.INTO);
                }
                fieldAndValuesSql.append(tableName);
                fieldAndValuesSql.append(fieldSql.toString());
                fieldAndValuesSql.append(SqlHelperCons.VALUES);
                fieldAndValuesSql.append(valueSqlList.get(k));
            }
            if (objects != null && objects.length > 1) {
                fieldAndValuesSql.append(SqlHelperCons.SELECT_DUAL);
            }
        } else {
            for (int k = 0; k < valueSqlList.size(); k++) {
                if (k == 0) {
                    fieldAndValuesSql.append(tableName);
                    fieldAndValuesSql.append(fieldSql.toString());
                    fieldAndValuesSql.append(SqlHelperCons.VALUES);
                }
                fieldAndValuesSql.append(valueSqlList.get(k));
                fieldAndValuesSql.append(SqlHelperCons.COMMA);
            }
            fieldAndValuesSql.deleteCharAt(fieldAndValuesSql.length() - SqlHelperCons.COMMA.length());
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
        Field[] fields;
        if (bean.getClass().getAnnotation(SqlUnion.class) != null) {
            fields = bean.getClass().getSuperclass().getDeclaredFields();
        } else {
            fields = bean.getClass().getDeclaredFields();
        }
        List<Field> filterAfterList = new ArrayList<>();
        for (int i = 0; i < fields.length; i++) {
            if (Modifier.isStatic(fields[i].getModifiers())) {
                continue;
            }
            String name = SqlBeanUtil.getTableFieldName(fields[i]);
            Object objectValue = ReflectUtil.instance().get(bean.getClass(), bean, fields[i].getName());
            if (update.isUpdateNotNull()) {
                if (objectValue == null) {
                    continue;
                }
            }
            if (SqlBeanUtil.isIgnore(fields[i])) {
                continue;
            }
            if (SqlBeanUtil.isFilter(filterFields, name)) {
                continue;
            }
            filterAfterList.add(fields[i]);
        }
        Date date = new Date();
        for (int i = 0; i < filterAfterList.size(); i++) {
            String name = SqlBeanUtil.getTableFieldName(filterAfterList.get(i));
            Object objectValue = ReflectUtil.instance().get(bean.getClass(), bean, filterAfterList.get(i).getName());
            setSql.append(transferred);
            setSql.append(SqlBeanUtil.isToUpperCase(update) ? name.toUpperCase() : name);
            setSql.append(transferred);
            setSql.append(SqlHelperCons.EQUAL_TO);
            if (filterAfterList.get(i).isAnnotationPresent(SqlVersion.class)) {
                Object o = SqlBeanUtil.updateVersion(filterAfterList.get(i).getType().getName(), objectValue);
                setSql.append(SqlBeanUtil.getSqlValue(update, o));
            } else if (filterAfterList.get(i).isAnnotationPresent(SqlUpdateTime.class) && SqlBeanUtil.whatType(filterAfterList.get(i).getType().getName()) == WhatType.DATE_TYPE) {
                setSql.append(SqlBeanUtil.getSqlValue(update, date));
            } else {
                setSql.append(SqlBeanUtil.getSqlValue(update, objectValue));
            }
            setSql.append(SqlHelperCons.COMMA);
        }
        setSql.deleteCharAt(setSql.length() - SqlHelperCons.COMMA.length());
        return setSql.toString();
    }

    /**
     * 返回where语句
     *
     * @param condition
     * @param bean
     * @return
     */
    @SuppressWarnings("unchecked")
    public static String whereSql(Condition condition, Object bean) {
        return conditionHandle(ConditionType.WHERE, condition, condition.getWhere(), condition.getAgrs(), bean, condition.getWhereMap(), condition.getWhereWrapper());
    }

    /**
     * 返回groupBy语句
     *
     * @param select
     * @return
     */
    private static String groupBySql(Select select) {
        return groupByAndOrderBySql(SqlHelperCons.GROUP_BY, select);
    }

    /**
     * @param select
     * @return
     */
    private static String havingSql(Select select) {
        return conditionHandle(ConditionType.HAVING, select, select.getHaving(), select.getHavingArgs(), null, select.getHavingMap(), select.getHavingWrapper());
    }

    /**
     * 返回orderBy语句
     *
     * @param select
     * @return
     */
    private static String orderBySql(Select select) {
        return groupByAndOrderBySql(SqlHelperCons.ORDER_BY, select);
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
        SqlField[] sqlFields;
        if (SqlHelperCons.ORDER_BY.equals(type)) {
            sqlFields = select.getOrderBy().toArray(new SqlField[]{});
        } else {
            sqlFields = select.getGroupBy().toArray(new SqlField[]{});
        }
        if (sqlFields != null && sqlFields.length != 0) {
            groupByAndOrderBySql.append(type);
            for (int i = 0; i < sqlFields.length; i++) {
                SqlField sqlField = sqlFields[i];
                if (StringUtil.isNotEmpty(sqlField.getTableAlias())) {
                    if (StringUtil.isNotEmpty(sqlField.getSchema())) {
                        groupByAndOrderBySql.append(sqlField.getSchema());
                        groupByAndOrderBySql.append(SqlHelperCons.POINT);
                    }
                    groupByAndOrderBySql.append(sqlField.getTableAlias());
                    groupByAndOrderBySql.append(SqlHelperCons.POINT);
                }
                groupByAndOrderBySql.append(sqlField.getName());
                if (SqlHelperCons.ORDER_BY.equals(type)) {
                    groupByAndOrderBySql.append(SqlHelperCons.SPACES);
                    groupByAndOrderBySql.append(select.getOrderBy().get(i).getSqlSort().name());
                    groupByAndOrderBySql.append(SqlHelperCons.SPACES);
                }
                groupByAndOrderBySql.append(SqlHelperCons.COMMA);
            }
            groupByAndOrderBySql.deleteCharAt(groupByAndOrderBySql.length() - SqlHelperCons.COMMA.length());
        } else {
            if (SqlHelperCons.ORDER_BY.equals(type) && select.getSqlBeanDB().getDbType() == DbType.SQLServer && SqlBeanUtil.isUsePage(select) && !SqlBeanUtil.isCount(select)) {
                groupByAndOrderBySql.append(type);
                String tableFieldFullName = SqlBeanUtil.getTableFieldFullName(select, select.getTable().getSchema(), select.getTable().getAlias(), select.getPage().getIdName());
                groupByAndOrderBySql.append(SqlBeanUtil.isToUpperCase(select) ? tableFieldFullName.toUpperCase() : tableFieldFullName);
            }
        }
        return groupByAndOrderBySql.toString();
    }

    /**
     * 条件处理
     *
     * @param conditionType 条件类型（where还是 having）
     * @param common        公共类
     * @param condition     条件字符串（优先级1）
     * @param args          条件字符串参数
     * @param conditionMap  条件Map（优先级3）
     * @param wrapper       条件包装器（优先级2）
     * @return
     */
    private static String conditionHandle(ConditionType conditionType, Common common, String condition, Object[]
            args, Object bean, ListMultimap<String, ConditionInfo> conditionMap, Wrapper wrapper) {
        StringBuffer conditionSql = new StringBuffer();
        // 优先级1 使用条件字符串拼接
        if (condition != null && !"".equals(condition)) {
            conditionSql.append(ConditionType.WHERE == conditionType ? SqlHelperCons.WHERE : SqlHelperCons.HAVING);
            conditionSql.append(versionCondition(common, bean));
            conditionSql.append(SqlHelperCons.BEGIN_BRACKET);
            if (args != null && args.length > 0) {
                conditionSql.append(SqlBeanUtil.getCondition(common, condition, args));
            } else if (condition.indexOf("${") > -1 && bean != null) {
                conditionSql.append(SqlBeanUtil.getCondition(common, condition, bean));
            } else {
                conditionSql.append(condition);
            }
            conditionSql.append(SqlHelperCons.END_BRACKET);
        }
        // 优先级2 使用条件包装器
        else if (wrapper != null && !wrapper.getDataList().isEmpty()) {
            conditionSql.append(ConditionType.WHERE == conditionType ? SqlHelperCons.WHERE : SqlHelperCons.HAVING);
            conditionSql.append(versionCondition(common, bean));
            conditionSql.append(conditionWrapperHandle(common, wrapper));
        } else {
            conditionSql.append(versionCondition(common, bean));
            if (conditionMap.size() > 0) {
                conditionSql.append(SqlHelperCons.BEGIN_BRACKET);
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
                conditionSql.append(SqlHelperCons.END_BRACKET);
            }
            if (conditionSql.length() != 0) {
                conditionSql.insert(0, ConditionType.WHERE == conditionType ? SqlHelperCons.WHERE : SqlHelperCons.HAVING);
            }
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
        if (common instanceof Update && ((Update) common).isLogicallyDelete()) {
            return "";
        }
        StringBuffer versionConditionSql = new StringBuffer();
        Field versionField = null;
        //更新时乐观锁处理
        if (common instanceof Update) {
            if (bean != null) {
                versionField = SqlBeanUtil.getVersionField(bean.getClass());
            }
            if (versionField != null) {
                boolean versionEffectiveness = SqlBeanUtil.versionEffectiveness(versionField.getType().getName());
                if (versionEffectiveness) {
                    versionConditionSql.append(SqlHelperCons.BEGIN_BRACKET);
                    versionConditionSql.append(SqlBeanUtil.getTableFieldName(versionField));
                    Object versionValue = ReflectUtil.instance().get(bean.getClass(), bean, versionField.getName());
                    versionConditionSql.append(versionValue == null ? SqlHelperCons.IS : SqlHelperCons.EQUAL_TO);
                    versionConditionSql.append(SqlBeanUtil.getSqlValue(common, versionValue));
                    versionConditionSql.append(SqlHelperCons.END_BRACKET);
                    versionConditionSql.append(SqlHelperCons.AND);
                }
            }
        }
        return versionConditionSql.toString();
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
            conditionSql.append(SqlHelperCons.BEGIN_BRACKET);
            for (int i = 0; i < wrapper.getDataList().size(); i++) {
                Wrapper.Data data = wrapper.getDataList().get(i);
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
            conditionSql.append(SqlHelperCons.END_BRACKET);
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
            if (sqlOperator == SqlOperator.IS) {
                operator = SqlHelperCons.IS;
            } else if (sqlOperator == SqlOperator.IS_NOT) {
                operator = SqlHelperCons.IS_NOT;
            } else if (sqlOperator == SqlOperator.IN) {
                operator = SqlHelperCons.IN;
            } else if (sqlOperator == SqlOperator.NOT_IN) {
                operator = SqlHelperCons.NOT_IN;
            } else if (sqlOperator == SqlOperator.LIKE || sqlOperator == SqlOperator.LIKE_L || sqlOperator == SqlOperator.LIKE_R) {
                operator = SqlHelperCons.LIKE;
            } else if (sqlOperator == SqlOperator.NOT_LIKE || sqlOperator == SqlOperator.NOT_LIKE_L || sqlOperator == SqlOperator.NOT_LIKE_R) {
                operator = SqlHelperCons.NOT_LIKE;
            } else if (sqlOperator == SqlOperator.BETWEEN) {
                operator = SqlHelperCons.BETWEEN;
            } else if (sqlOperator == SqlOperator.GREATER_THAN) {
                operator = SqlHelperCons.GREATER_THAN;
            } else if (sqlOperator == SqlOperator.GREAT_THAN_OR_EQUAL_TO) {
                operator = SqlHelperCons.GREAT_THAN_OR_EQUAL_TO;
            } else if (sqlOperator == SqlOperator.LESS_THAN) {
                operator = SqlHelperCons.LESS_THAN;
            } else if (sqlOperator == SqlOperator.LESS_THAN_OR_EQUAL_TO) {
                operator = SqlHelperCons.LESS_THAN_OR_EQUAL_TO;
            } else if (sqlOperator == SqlOperator.EQUAL_TO) {
                operator = SqlHelperCons.EQUAL_TO;
            } else if (sqlOperator == SqlOperator.NOT_EQUAL_TO) {
                operator = SqlHelperCons.NOT_EQUAL_TO;
            }
        } else {
            operator = SqlHelperCons.EQUAL_TO;
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
                    logic = SqlHelperCons.AND;
                    break;
                case OR:
                    logic = SqlHelperCons.OR;
                    break;
                case ORBracket:
                    logic = SqlHelperCons.OR_BRACKET;
                    break;
                case ANDBracket:
                    logic = SqlHelperCons.AND_BRACKET;
                    break;
            }
        } else {
            logic = SqlHelperCons.AND;
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
                        Original original = (Original) in_notInValues[k];
                        in_notIn.append(original.getValue());
                    } else {
                        in_notIn.append(SqlBeanUtil.getSqlValue(common, in_notInValues[k]));
                    }
                    in_notIn.append(SqlHelperCons.COMMA);
                }
                in_notIn.deleteCharAt(in_notIn.length() - SqlHelperCons.COMMA.length());
                value = in_notIn.toString();
            }
        } else {
            value = conditionInfo.getValue();
            //对like操作符处理
            if (operator.indexOf(SqlHelperCons.LIKE) > -1) {
                if (conditionInfo.getSqlOperator() == SqlOperator.LIKE || conditionInfo.getSqlOperator() == SqlOperator.LIKE_L || conditionInfo.getSqlOperator() == SqlOperator.NOT_LIKE || conditionInfo.getSqlOperator() == SqlOperator.NOT_LIKE_L) {
                    value = SqlHelperCons.PERCENT + value;
                }
                if (conditionInfo.getSqlOperator() == SqlOperator.LIKE || conditionInfo.getSqlOperator() == SqlOperator.LIKE_R || conditionInfo.getSqlOperator() == SqlOperator.NOT_LIKE || conditionInfo.getSqlOperator() == SqlOperator.NOT_LIKE_R) {
                    value = value + SqlHelperCons.PERCENT;
                }
                value = SqlHelperCons.SINGLE_QUOTATION_MARK + value + SqlHelperCons.SINGLE_QUOTATION_MARK;
            } else if (value instanceof Original) {
                Original original = (Original) conditionInfo.getValue();
                value = original.getValue();
            } else {
                value = SqlBeanUtil.getSqlValue(common, value);
            }
        }
        //表别名
        if (StringUtil.isNotEmpty(conditionInfo.getTableAlias())) {
            //schema
            if (StringUtil.isNotEmpty(conditionInfo.getSchema())) {
                sql.append(conditionInfo.getSchema());
                sql.append(SqlHelperCons.POINT);
            }
            sql.append(transferred);
            sql.append(conditionInfo.getTableAlias());
            sql.append(transferred);
            sql.append(SqlHelperCons.POINT);
        }
        //字段名
        sql.append(conditionInfo.getName());
        //操作符
        sql.append(operator);
        //值
        if (conditionInfo.getSqlOperator() == SqlOperator.BETWEEN) {
            sql.append(SqlBeanUtil.getSqlValue(common, betweenValues[0]));
            sql.append(SqlHelperCons.AND);
            sql.append(SqlBeanUtil.getSqlValue(common, betweenValues[1]));
        } else {
            sql.append(value);
        }
        // in与not in 额外加结束括号
        if (needEndBracket) {
            sql.append(SqlHelperCons.END_BRACKET);
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
            sqlSb.append(SqlHelperCons.LIMIT);
            sqlSb.append(param[0]);
            sqlSb.append(SqlHelperCons.COMMA);
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
            sqlSb.append(SqlHelperCons.LIMIT);
            sqlSb.append(param[1]);
            sqlSb.append(SqlHelperCons.OFFSET);
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
            beginSqlSb.append(SqlHelperCons.SELECT + SqlHelperCons.ALL + SqlHelperCons.FROM + SqlHelperCons.BEGIN_BRACKET);
            beginSqlSb.append(SqlHelperCons.SELECT + SqlHelperCons.TB + SqlHelperCons.POINT + SqlHelperCons.ALL + SqlHelperCons.COMMA + SqlHelperCons.ROWNUM + SqlHelperCons.RN + SqlHelperCons.FROM + SqlHelperCons.BEGIN_BRACKET);
            sqlSb.insert(0, beginSqlSb);
            StringBuffer endSb = new StringBuffer();
            endSb.append(SqlHelperCons.END_BRACKET + SqlHelperCons.TB + SqlHelperCons.WHERE + SqlHelperCons.ROWNUM + SqlHelperCons.LESS_THAN_OR_EQUAL_TO);
            endSb.append(param[1]);
            endSb.append(SqlHelperCons.END_BRACKET + SqlHelperCons.WHERE + SqlHelperCons.RN + SqlHelperCons.GREATER_THAN);
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
            beginSqlSb.append(SqlHelperCons.SELECT + SqlHelperCons.ALL + SqlHelperCons.FROM + SqlHelperCons.BEGIN_BRACKET);
            beginSqlSb.append(SqlHelperCons.SELECT + SqlHelperCons.T + SqlHelperCons.POINT + SqlHelperCons.ALL + SqlHelperCons.COMMA + SqlHelperCons.ROWNUMBER);
            beginSqlSb.append(SqlHelperCons.OVER + SqlHelperCons.BEGIN_BRACKET + SqlHelperCons.SPACES + SqlHelperCons.END_BRACKET + SqlHelperCons.AS + SqlHelperCons.RN + SqlHelperCons.FROM + SqlHelperCons.BEGIN_BRACKET);
            sqlSb.insert(0, beginSqlSb);
            StringBuffer endSb = new StringBuffer();
            endSb.append(SqlHelperCons.END_BRACKET + SqlHelperCons.T + SqlHelperCons.SPACES + SqlHelperCons.END_BRACKET + SqlHelperCons.TB + SqlHelperCons.WHERE + SqlHelperCons.TB + SqlHelperCons.POINT + SqlHelperCons.RN + SqlHelperCons.BETWEEN);
            endSb.append(param[0]);
            endSb.append(SqlHelperCons.AND);
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
            sqlSb.append(SqlHelperCons.OFFSET);
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
        Integer[] param;
        //SQLServer2008
        if (DbType.SQLServer == select.getSqlBeanDB().getDbType()) {
            int top = (select.getPage().getPagenum() + 1) * select.getPage().getPagesize();
            int begin = top - select.getPage().getPagesize();
            param = new Integer[]{top, begin};
        }
        //Oracle,DB2
        else if (DbType.Oracle == select.getSqlBeanDB().getDbType() || DbType.DB2 == select.getSqlBeanDB().getDbType()) {
            //startIndex = (当前页 * 每页显示的数量)，例如：(0 * 10)
            //endIndex = (当前页 * 每页显示的数量) + 每页显示的数量，例如：10 = (0 * 10) + 10
            //那么如果startIndex=0，endIndex=10，就是查询第0到10条数据
            int startIndex = select.getPage().getPagenum() * select.getPage().getPagesize();
            int endIndex = (select.getPage().getPagenum() * select.getPage().getPagesize()) + select.getPage().getPagesize();
            param = new Integer[]{startIndex, endIndex};
        }
        //Mysql,MariaDB,PostgreSQL,Sqlite,Hsql
        else {
            int limitOffset = select.getPage().getPagenum() * select.getPage().getPagesize();
            int limitAmount = select.getPage().getPagesize();
            param = new Integer[]{limitOffset, limitAmount};
        }
        return param;
    }


}
