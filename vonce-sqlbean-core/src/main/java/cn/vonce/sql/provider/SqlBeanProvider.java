package cn.vonce.sql.provider;

import cn.vonce.sql.annotation.SqlTable;
import cn.vonce.sql.bean.*;
import cn.vonce.sql.config.SqlBeanDB;
import cn.vonce.sql.constant.SqlConstant;
import cn.vonce.sql.enumerate.DbType;
import cn.vonce.sql.exception.SqlBeanException;
import cn.vonce.sql.helper.SqlHelper;
import cn.vonce.sql.helper.Wrapper;
import cn.vonce.sql.uitls.ReflectUtil;
import cn.vonce.sql.uitls.SqlBeanUtil;
import cn.vonce.sql.uitls.StringUtil;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

/**
 * 通用的数据库操作sql语句生成
 *
 * @author Jovi
 * @version 1.0
 * @email 766255988@qq.com
 * @date 2018年5月15日下午2:23:47
 */
public class SqlBeanProvider {

    /**
     * 根据id条件查询
     *
     * @param clazz
     * @param id
     * @return
     */
    public static String selectByIdSql(SqlBeanDB sqlBeanDB, Class<?> clazz, Object id) {
        return selectByIdsSql(sqlBeanDB, clazz, new Object[]{id});
    }

    /**
     * 根据ids条件查询
     *
     * @param clazz
     * @param ids
     * @return
     */
    public static String selectByIdsSql(SqlBeanDB sqlBeanDB, Class<?> clazz, Object... ids) {
        Select select;
        Field idField;
        try {
            select = newSelect(sqlBeanDB, clazz, false);
            idField = SqlBeanUtil.getIdField(clazz);
        } catch (SqlBeanException e) {
            e.printStackTrace();
            return null;
        }
        SqlTable sqlTable = SqlBeanUtil.getSqlTable(clazz);
        if (ids.length > 1) {
            select.where().in(SqlBeanUtil.getTableFieldFullName(select, select.getTable().getAlias(), idField, sqlTable), ids);
        } else {
            select.where().eq(SqlBeanUtil.getTableFieldFullName(select, select.getTable().getAlias(), idField, sqlTable), ids[0]);
        }
        return SqlHelper.buildSelectSql(select);
    }

    /**
     * 根据条件查询
     *
     * @param clazz
     * @param paging
     * @param where
     * @param args
     * @return
     */
    public static String selectByConditionSql(SqlBeanDB sqlBeanDB, Class<?> clazz, Paging paging, String where, Object... args) {
        Select select = newSelect(sqlBeanDB, clazz, false);
        select.where(where, args);
        setPaging(select, paging, clazz);
        return SqlHelper.buildSelectSql(select);
    }

    /**
     * 根据条件查询统计
     *
     * @param clazz
     * @param where
     * @param args
     * @return
     */
    public static String countByConditionSql(SqlBeanDB sqlBeanDB, Class<?> clazz, String where, Object[] args) {
        Select select = newSelect(sqlBeanDB, clazz, true);
        select.where(where, args);
        return SqlHelper.buildSelectSql(select);
    }

    /**
     * 查询全部
     *
     * @param clazz
     * @return
     */
    public static String selectAllSql(SqlBeanDB sqlBeanDB, Class<?> clazz, Paging paging) {
        Select select = newSelect(sqlBeanDB, clazz, false);
        setPaging(select, paging, clazz);
        return SqlHelper.buildSelectSql(select);
    }

    /**
     * 根据自定义条件查询（可自动分页）
     *
     * @param sqlBeanDB
     * @param clazz
     * @param select
     * @return
     */
    public static String selectSql(SqlBeanDB sqlBeanDB, Class<?> clazz, Select select) {
        if (select.getSqlBeanDB() == null) {
            select.setSqlBeanDB(sqlBeanDB);
        }
        if (select.getColumnList().isEmpty()) {
            try {
                SqlTable sqlTable = SqlBeanUtil.getSqlTable(clazz);
                select.setColumnList(SqlBeanUtil.getSelectColumns(clazz, select.getFilterFields()));
                if (select.getPage() != null && select.getSqlBeanDB().getDbType() == DbType.SQLServer) {
                    select.getPage().setIdName(SqlBeanUtil.getTableFieldFullName(select, select.getTable().getAlias(), SqlBeanUtil.getIdField(clazz), sqlTable));
                }
            } catch (SqlBeanException e) {
                e.printStackTrace();
                return null;
            }
        }
        return setSelectAndBuild(clazz, select);
    }

    /**
     * 根据自定义条件统计
     *
     * @param sqlBeanDB
     * @param clazz
     * @param select
     * @return
     */
    public static String countSql(SqlBeanDB sqlBeanDB, Class<?> clazz, Select select) {
        if (select.getSqlBeanDB() == null) {
            select.setSqlBeanDB(sqlBeanDB);
        }
        if (select.getColumnList() == null || select.getColumnList().isEmpty()) {
            select.column(SqlConstant.COUNT + SqlConstant.BEGIN_BRACKET + SqlConstant.ALL + SqlConstant.END_BRACKET);
        } else if (select.getColumnList().size() > 1 || select.getColumnList().get(0).getName().toLowerCase().indexOf("count") == -1) {
            try {
                select = select.copy();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            select.getColumnList().clear();
            select.column(SqlConstant.COUNT + SqlConstant.BEGIN_BRACKET + SqlConstant.ALL + SqlConstant.END_BRACKET);
        }
        return setSelectAndBuild(clazz, select);
    }

    /**
     * 根据id条件删除
     *
     * @param clazz
     * @param id
     * @return
     */
    public static String deleteByIdSql(SqlBeanDB sqlBeanDB, Class<?> clazz, Object id) {
        //如果是逻辑删除那么将Delete对象转为Update对象
        if (SqlBeanUtil.checkLogically(clazz)) {
            return logicallyDeleteByIdSql(sqlBeanDB, clazz, id);
        }
        if (StringUtil.isEmpty(id)) {
            try {
                throw new SqlBeanException("deleteByIdSql id不能为空");
            } catch (SqlBeanException e) {
                e.printStackTrace();
                return null;
            }
        }
        Delete delete = new Delete();
        delete.setSqlBeanDB(sqlBeanDB);
        delete.setTable(clazz);
        delete.setBeanClass(clazz);
        setSchema(delete, clazz);
        Field idField;
        try {
            idField = SqlBeanUtil.getIdField(clazz);
        } catch (SqlBeanException e) {
            e.printStackTrace();
            return null;
        }
        SqlTable sqlTable = SqlBeanUtil.getSqlTable(clazz);
        delete.where().in(SqlBeanUtil.getTableFieldName(idField, sqlTable), id);
        return SqlHelper.buildDeleteSql(delete);
    }

    /**
     * 根据条件删除
     *
     * @param clazz
     * @param where
     * @param args
     * @return
     */
    public static String deleteByConditionSql(SqlBeanDB sqlBeanDB, Class<?> clazz, String where, Object[] args) {
        //如果是逻辑删除那么将Delete对象转为Update对象
        if (SqlBeanUtil.checkLogically(clazz)) {
            return logicallyDeleteByConditionSql(sqlBeanDB, clazz, where, args);
        }
        Delete delete = new Delete();
        delete.setSqlBeanDB(sqlBeanDB);
        delete.setTable(clazz);
        delete.where(where, args);
        setSchema(delete, clazz);
        return SqlHelper.buildDeleteSql(delete);
    }

    /**
     * 删除
     *
     * @param clazz
     * @param delete
     * @param ignore
     * @return
     */
    public static String deleteSql(SqlBeanDB sqlBeanDB, Class<?> clazz, Delete delete, boolean ignore) {
        //如果是逻辑删除那么将Delete对象转为Update对象
        if (delete.isLogicallyDelete()) {
            Update<Object> update = new Update();
            update.setSqlBeanDB(sqlBeanDB);
            update.setTable(clazz);
            update.setUpdateBean(newLogicallyDeleteBean(clazz));
            update.where(delete.getWhereWrapper());
            update.where(delete.getWhere(), delete.getAgrs());
            update.where().setDataList(delete.where().getDataList());
            return updateSql(sqlBeanDB, clazz, update, ignore);
        }
        if (delete.getSqlBeanDB() == null) {
            delete.setSqlBeanDB(sqlBeanDB);
        }
        if (delete.getTable().isNotSet()) {
            delete.setTable(clazz);
        }
        if (delete.getBeanClass() == null) {
            delete.setBeanClass(clazz);
        }
        setSchema(delete, clazz);
        if (ignore || (!delete.where().getDataList().isEmpty() || StringUtil.isNotEmpty(delete.getWhere()) || !delete.getWhereWrapper().getDataList().isEmpty())) {
            return SqlHelper.buildDeleteSql(delete);
        } else {
            try {
                throw new SqlBeanException("该delete sql未设置where条件，如果确实不需要where条件，请使用delete(Select select, boolean ignore)");
            } catch (SqlBeanException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    /**
     * 逻辑删除
     *
     * @param clazz
     * @param id
     * @return
     */
    public static String logicallyDeleteByIdSql(SqlBeanDB sqlBeanDB, Class<?> clazz, Object id) {
        Update update = new Update();
        try {
            update.setSqlBeanDB(sqlBeanDB);
            update.setTable(clazz);
            update.setUpdateBean(newLogicallyDeleteBean(clazz));
            Field idField = SqlBeanUtil.getIdField(clazz);
            SqlTable sqlTable = SqlBeanUtil.getSqlTable(clazz);
            update.where().in(SqlBeanUtil.getTableFieldName(idField, sqlTable), id);
            setSchema(update, clazz);
        } catch (SqlBeanException e) {
            e.printStackTrace();
            return null;
        }
        return SqlHelper.buildUpdateSql(update);
    }

    /**
     * 逻辑删除
     *
     * @param clazz
     * @param where
     * @param args
     * @return
     */
    public static String logicallyDeleteByConditionSql(SqlBeanDB sqlBeanDB, Class<?> clazz, String where, Object[] args) {
        Update update = new Update();
        try {
            update.setSqlBeanDB(sqlBeanDB);
            update.setTable(clazz);
            update.setUpdateBean(newLogicallyDeleteBean(clazz));
            update.where(where, args);
            setSchema(update, clazz);
        } catch (SqlBeanException e) {
            e.printStackTrace();
            return null;
        }
        return SqlHelper.buildUpdateSql(update);
    }

    /**
     * 逻辑删除
     *
     * @param clazz
     * @param wrapper
     * @return
     */
    public static String logicallyDeleteByConditionSql(SqlBeanDB sqlBeanDB, Class<?> clazz, Wrapper wrapper) {
        Update update = new Update();
        try {
            update.setSqlBeanDB(sqlBeanDB);
            update.setTable(clazz);
            update.setUpdateBean(newLogicallyDeleteBean(clazz));
            update.where(wrapper);
            setSchema(update, clazz);
        } catch (SqlBeanException e) {
            e.printStackTrace();
            return null;
        }
        return SqlHelper.buildUpdateSql(update);
    }

    /**
     * 更新
     *
     * @param sqlBeanDB
     * @param clazz
     * @param update
     * @param ignore
     * @return
     */
    public static String updateSql(SqlBeanDB sqlBeanDB, Class<?> clazz, Update update, boolean ignore) {
        if (update.getSqlBeanDB() == null) {
            update.setSqlBeanDB(sqlBeanDB);
        }
        if (update.getTable().isNotSet()) {
            update.setTable(clazz);
        }
        if (update.getBeanClass() == null) {
            update.setBeanClass(clazz);
        }
        setSchema(update, clazz);
        if (ignore || (!update.where().getDataList().isEmpty() || StringUtil.isNotEmpty(update.getWhere()) || !update.getWhereWrapper().getDataList().isEmpty())) {
            return SqlHelper.buildUpdateSql(update);
        } else {
            try {
                throw new SqlBeanException("该update sql未设置where条件，如果确实不需要where条件，请使用update(Select select, boolean ignore)");
            } catch (SqlBeanException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    /**
     * 根据实体类id条件更新
     *
     * @param sqlBeanDB
     * @param clazz
     * @param bean
     * @param updateNotNull
     * @param optimisticLock
     * @param filterFields
     * @return
     */
    public static String updateByIdSql(SqlBeanDB sqlBeanDB, Class<?> clazz, Object bean, Object id, boolean updateNotNull, boolean optimisticLock, String[] filterFields) {
        if (StringUtil.isEmpty(id)) {
            try {
                throw new SqlBeanException("updateByIdSql id不能为空");
            } catch (SqlBeanException e) {
                e.printStackTrace();
                return null;
            }
        }
        Update update = newUpdate(sqlBeanDB, clazz, bean, updateNotNull, optimisticLock);
        update.setFilterFields(filterFields);
        Field idField;
        try {
            idField = SqlBeanUtil.getIdField(bean.getClass());
        } catch (SqlBeanException e) {
            e.printStackTrace();
            return null;
        }
        SqlTable sqlTable = SqlBeanUtil.getSqlTable(clazz);
        update.where().eq(SqlBeanUtil.getTableFieldName(idField, sqlTable), id);
        return SqlHelper.buildUpdateSql(update);
    }

    /**
     * 根据实体类id条件更新
     *
     * @param sqlBeanDB
     * @param clazz
     * @param bean
     * @param updateNotNull
     * @param optimisticLock
     * @param filterFields
     * @return
     */
    public static String updateByBeanIdSql(SqlBeanDB sqlBeanDB, Class<?> clazz, Object bean, boolean updateNotNull, boolean optimisticLock, String[] filterFields) {
        Update update = newUpdate(sqlBeanDB, clazz, bean, updateNotNull, optimisticLock);
        update.setFilterFields(filterFields);
        Field idField;
        try {
            idField = SqlBeanUtil.getIdField(bean.getClass());
            Object id = ReflectUtil.instance().get(bean.getClass(), bean, idField.getName());
            if (StringUtil.isEmpty(id)) {
                try {
                    throw new SqlBeanException("updateByBeanIdSql id不能为空");
                } catch (SqlBeanException e) {
                    e.printStackTrace();
                    return null;
                }
            }
            SqlTable sqlTable = SqlBeanUtil.getSqlTable(clazz);
            update.where().eq(SqlBeanUtil.getTableFieldName(idField, sqlTable), id);
        } catch (SqlBeanException e) {
            e.printStackTrace();
            return null;
        }
        return SqlHelper.buildUpdateSql(update);
    }

    /**
     * 根据条件更新
     *
     * @param sqlBeanDB
     * @param clazz
     * @param bean
     * @param updateNotNull
     * @param optimisticLock
     * @param filterFields
     * @param where
     * @param args
     * @return
     */
    public static String updateByConditionSql(SqlBeanDB sqlBeanDB, Class<?> clazz, Object bean, boolean updateNotNull, boolean optimisticLock, String[] filterFields, String where, Object[] args) {
        Update update = newUpdate(sqlBeanDB, clazz, bean, updateNotNull, optimisticLock);
        update.setFilterFields(filterFields);
        update.where(where, args);
        return SqlHelper.buildUpdateSql(update);
    }

    /**
     * 根据实体类字段条件更新
     *
     * @param sqlBeanDB
     * @param clazz
     * @param bean
     * @param updateNotNull
     * @param optimisticLock
     * @param filterFields
     * @param where
     * @return
     */
    public static String updateByBeanConditionSql(SqlBeanDB sqlBeanDB, Class<?> clazz, Object bean, boolean updateNotNull, boolean optimisticLock, String[] filterFields, String where) {
        Update update = newUpdate(sqlBeanDB, clazz, bean, updateNotNull, optimisticLock);
        update.setFilterFields(filterFields);
        update.where(where, null);
        return SqlHelper.buildUpdateSql(update);
    }

    /**
     * 插入数据
     *
     * @param bean
     * @return
     */
    public static String insertBeanSql(SqlBeanDB sqlBeanDB, Class<?> clazz, Object bean) {
        Insert insert = new Insert();
        insert.setSqlBeanDB(sqlBeanDB);
        insert.setTable(clazz);
        insert.setBeanClass(clazz);
        insert.setInsertBean(SqlBeanUtil.getObjectArray(bean));
        setSchema(insert, clazz);
        return SqlHelper.buildInsertSql(insert);
    }

    /**
     * 插入数据
     *
     * @param sqlBeanDB
     * @param clazz
     * @param insert
     * @return
     */
    public static String insertSql(SqlBeanDB sqlBeanDB, Class<?> clazz, Insert insert) {
        if (insert.getSqlBeanDB() == null) {
            insert.setSqlBeanDB(sqlBeanDB);
        }
        if (insert.getTable().isNotSet()) {
            insert.setTable(clazz);
        }
        if (insert.getBeanClass() == null) {
            insert.setBeanClass(clazz);
        }
        setSchema(insert, clazz);
        return SqlHelper.buildInsertSql(insert);
    }

    /**
     * 删除表
     *
     * @param sqlBeanDB
     * @param clazz
     * @return
     */
    public static String dropTableSql(SqlBeanDB sqlBeanDB, Class<?> clazz) {
        Drop drop = new Drop();
        drop.setSqlBeanDB(sqlBeanDB);
        drop.setTable(clazz);
        setSchema(drop, clazz);
        return SqlHelper.buildDrop(drop);
    }

    /**
     * 创建表
     *
     * @param sqlBeanDB
     * @param clazz
     * @return
     */
    public static String createTableSql(SqlBeanDB sqlBeanDB, Class<?> clazz) {
        Create create = new Create();
        create.setSqlBeanDB(sqlBeanDB);
        create.setTable(clazz);
        create.setBeanClass(clazz);
        setSchema(create, clazz);
        return SqlHelper.buildCreateSql(create);
    }

    /**
     * 获取表名列表
     *
     * @param sqlBeanDB
     * @return
     */
    public static String selectTableListSql(SqlBeanDB sqlBeanDB, String name) {
        if (sqlBeanDB.getSqlBeanConfig().getToUpperCase() != null && sqlBeanDB.getSqlBeanConfig().getToUpperCase() && StringUtil.isNotEmpty(name)) {
            name = name.toUpperCase();
        }
        switch (sqlBeanDB.getDbType()) {
            case MySQL:
            case MariaDB:
                return "SELECT table_name AS `name`, table_comment AS comm FROM information_schema.tables WHERE table_schema = database() AND table_type = 'BASE TABLE'" + (StringUtil.isNotEmpty(name) ? " AND table_name = '" + name + "'" : "");
            case SQLServer:
                return "SELECT o.name, p.value AS comm FROM sysobjects o LEFT JOIN sys.extended_properties p ON p.major_id = o.id AND p.minor_id = 0 WHERE o.xtype='U'" + (StringUtil.isNotEmpty(name) ? " AND o.name = '" + name + "'" : "");
            case Oracle:
                return "SELECT t.table_name AS \"name\", c.comments AS \"comm\"  FROM user_tables t LEFT JOIN user_tab_comments c ON c.table_name = t.table_name" + (StringUtil.isNotEmpty(name) ? " WHERE t.table_name = '" + name + "'" : "");
            case PostgreSQL:
                return "SELECT tablename AS \"name\" FROM pg_tables WHERE schemaname = 'public'" + (StringUtil.isNotEmpty(name) ? " AND tablename = '" + name + "'" : "");
            case DB2:
//                return "select tabname AS \"name\" from syscat.tables where tabschema = current schema" + (StringUtil.isNotEmpty(name) ? " and tabname = '" + name + "'" : "");
                return "SELECT name FROM sysibm.systables WHERE type = 'T' AND creator = current user" + (StringUtil.isNotEmpty(name) ? " AND name = '" + name + "'" : "") + (StringUtil.isNotEmpty(name) ? " AND table_name = '" + name + "'" : "");
            case H2:
                return "SELECT table_name AS \"name\" FROM information_schema.tables WHERE table_type = 'TABLE'" + (StringUtil.isNotEmpty(name) ? " AND table_name = '" + name + "'" : "");
            case Hsql:
                return "SELECT table_name AS \"name\" FROM information_schema.tables WHERE table_type = 'BASE TABLE'" + (StringUtil.isNotEmpty(name) ? " AND table_name = '" + name + "'" : "");
            case Derby:
                return "SELECT tablename AS \"name\" FROM SYS.systables WHERE tabletype = 'T'" + (StringUtil.isNotEmpty(name) ? " AND tablename = '" + name + "'" : "");
            case SQLite:
                return "SELECT name FROM sqlite_master WHERE type='table'" + (StringUtil.isNotEmpty(name) ? " AND name = '" + name + "'" : "");
            default:
                throw new SqlBeanException("请配置正确的数据库");
        }
    }

    /**
     * 获取列信息列表
     *
     * @param sqlBeanDB
     * @return
     */
    public static String selectColumnListSql(SqlBeanDB sqlBeanDB, String name) {
//        if (sqlBeanDB.getSqlBeanConfig().getToUpperCase() != null && sqlBeanDB.getSqlBeanConfig().getToUpperCase() && StringUtil.isNotEmpty(name)) {
//            name = name.toUpperCase();
//        }
        switch (sqlBeanDB.getDbType()) {
            case MySQL:
            case MariaDB:
                return mysqlColumnInfoSql(name);
            case SQLServer:
                return sqlServerColumnInfoSql(name);
            case Oracle:
                return oracleColumnInfoSql(name);
            case PostgreSQL:
            case DB2:
            case H2:
            case Hsql:
            case Derby:
                return "";
            case SQLite:
                return sqliteColumnInfoSql(name);
            default:
                throw new SqlBeanException("请配置正确的数据库");
        }
    }

    private static String mysqlColumnInfoSql(String tableName) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT ordinal_position AS cid, column_name AS name, data_type AS type, ");
        sql.append("(CASE is_nullable WHEN 'NO' THEN 1 ELSE 0 END) AS notnull, column_default AS dflt_value, ");
        sql.append("(CASE column_key WHEN 'PRI' THEN 1 ELSE 0 END) AS pk, ");
        sql.append("(CASE column_key WHEN 'MUL' THEN 1 ELSE 0 END) AS fk, ");
        sql.append("(CASE WHEN data_type = 'bit' OR data_type = 'tinyint' OR data_type = 'mediumint' OR data_type = 'int' OR data_type = 'bigint' OR ");
        sql.append("data_type = 'float' OR data_type = 'double' OR data_type = 'decimal' THEN numeric_precision ELSE character_maximum_length END) AS length, ");
        sql.append("numeric_scale AS scale, ");
        sql.append("column_comment AS comm ");
        sql.append("FROM information_schema.columns ");
        sql.append("WHERE table_schema = database() AND table_name = '");
        sql.append(tableName);
        sql.append("'");
        return sql.toString();
    }

    private static String sqlServerColumnInfoSql(String tableName) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT a.cid, a.name, a.type, (CASE a.notnull WHEN 0 THEN 1 ELSE 0 END) AS notnull, ");
        sql.append("(CASE LEFT(constraint_name, 2) WHEN 'PK' THEN 1 ELSE 0 END) AS pk, ");
        sql.append("(CASE LEFT(constraint_name, 2) WHEN 'FK' THEN 1 ELSE 0 END) AS fk, ");
        sql.append("a.length, a.scale, c.value AS comm ");
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

    private static String oracleColumnInfoSql(String tableName) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT col.column_id AS cid, col.column_name AS name, col.data_type AS type, ");
        sql.append("(CASE col.nullable WHEN 'N' THEN '1' ELSE '0' END) AS notnull, col.data_default AS dflt_value, ");
        sql.append("(CASE uc1.constraint_type WHEN 'P' THEN '1' ELSE '0' END) AS pk, ");
        sql.append("(CASE uc2.constraint_type WHEN 'R' THEN '1' ELSE '0' END) AS fk, ");
        sql.append("(CASE WHEN col.data_type = 'FLOAT' OR col.data_type = 'DOUBLE' OR col.data_type = 'DECIMAL' OR col.data_type = 'NUMBER' THEN col.data_precision ELSE col.char_length END) AS length, ");
        sql.append("col.data_scale AS scale, ");
        sql.append("user_col_comments.comments AS comm ");
        sql.append("FROM user_tab_columns col ");
        sql.append("LEFT JOIN user_cons_columns ucc ON ucc.table_name = col.table_name AND ucc.column_name = col.column_name AND ucc.position IS NOT NULL ");
        sql.append("LEFT JOIN user_constraints uc1 ON uc1.constraint_name = ucc.constraint_name AND uc1.constraint_type = 'P' ");
        sql.append("LEFT JOIN user_constraints uc2 ON uc2.constraint_name = ucc.constraint_name AND uc2.constraint_type = 'R' ");
        sql.append("INNER JOIN user_col_comments ON user_col_comments.table_name = col.table_name AND user_col_comments.column_name = col.column_name ");
        sql.append("WHERE col.table_name = '");
        sql.append(tableName);
        sql.append("'");
        return sql.toString();
    }

    private static String sqliteColumnInfoSql(String tableName) {
        StringBuffer sql = new StringBuffer();
        sql.append("pragma table_info('");
        sql.append(tableName);
        sql.append("')");
        return sql.toString();
    }

    /**
     * 备份表和数据
     *
     * @param sqlBeanDB
     * @param clazz
     * @param targetTableName
     * @param columns
     * @param wrapper
     * @return
     */
    public static String backupSql(SqlBeanDB sqlBeanDB, Class<?> clazz, String targetSchema, String targetTableName, Column[] columns, Wrapper wrapper) {
        Backup backup = new Backup();
        backup.setSqlBeanDB(sqlBeanDB);
        backup.setTable(clazz);
        backup.setColumns(columns);
        backup.setTargetSchema(targetSchema);
        backup.setTargetTableName(targetTableName);
        backup.where(wrapper);
        setSchema(backup, clazz);
        return SqlHelper.buildBackup(backup);
    }

    /**
     * 复制数据到指定表
     *
     * @param sqlBeanDB
     * @param clazz
     * @param targetTableName
     * @param columns
     * @param wrapper
     * @return
     */
    public static String copySql(SqlBeanDB sqlBeanDB, Class<?> clazz, String targetSchema, String targetTableName, Column[] columns, Wrapper wrapper) {
        Copy copy = new Copy();
        copy.setSqlBeanDB(sqlBeanDB);
        copy.setTable(clazz);
        copy.setColumns(columns);
        copy.setTargetSchema(targetSchema);
        copy.setTargetTableName(targetTableName);
        copy.where(wrapper);
        return SqlHelper.buildCopy(copy);
    }

    /**
     * 实例化Select
     *
     * @param clazz
     * @param isCount
     * @return
     * @throws SqlBeanException
     */
    private static Select newSelect(SqlBeanDB sqlBeanDB, Class<?> clazz, boolean isCount) {
        Select select = new Select();
        select.setSqlBeanDB(sqlBeanDB);
        select.setTable(clazz);
        select.setBeanClass(clazz);
        try {
            if (isCount) {
                select.column(SqlConstant.COUNT + SqlConstant.BEGIN_BRACKET + SqlConstant.ALL + SqlConstant.END_BRACKET);
            } else {
                select.setColumnList(SqlBeanUtil.getSelectColumns(clazz, select.getFilterFields()));
            }
            SqlBeanUtil.setJoin(select, clazz);
            setSchema(select, clazz);
        } catch (SqlBeanException e) {
            e.printStackTrace();
            return null;
        }
        return select;
    }

    /**
     * 设置SqlBean中的Select 并生成select sql
     *
     * @param clazz
     * @param select
     * @return
     * @throws SqlBeanException
     */
    private static String setSelectAndBuild(Class<?> clazz, Select select) {
        if (StringUtil.isEmpty(select.getTable().getName())) {
            Table table = SqlBeanUtil.getTable(clazz);
            select.getTable().setName(table.getName());
            if (StringUtil.isEmpty(select.getTable().getAlias())) {
                select.getTable().setAlias(table.getAlias());
            }
        }
        if (select.getBeanClass() == null) {
            select.setBeanClass(clazz);
        }
        setSchema(select, clazz);
        try {
            SqlBeanUtil.setJoin(select, clazz);
        } catch (SqlBeanException e) {
            e.printStackTrace();
            return null;
        }
        if (!select.getOrderBy().isEmpty()) {
            for (Order order : select.getOrderBy()) {
                if (StringUtil.isEmpty(order.getTableAlias())) {
                    List<Field> fieldList = SqlBeanUtil.getBeanAllField(clazz);
                    Field field = SqlBeanUtil.getFieldByTableFieldName(fieldList, order.getName());
                    if (field != null) {
                        order.setTableAlias(select.getTable().getAlias());
                    }
                }
            }
        }
        return SqlHelper.buildSelectSql(select);
    }

    /**
     * 逻辑删除需要的对象
     *
     * @param clazz
     * @return
     * @throws IllegalAccessException
     */
    private static Object newLogicallyDeleteBean(Class<?> clazz) throws SqlBeanException {
        Object bean = null;
        try {
            bean = clazz.newInstance();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        Field field = SqlBeanUtil.getLogicallyField(clazz);
        ReflectUtil.instance().set(bean.getClass(), bean, field.getName(), field.getType() == Boolean.class || field.getType() == boolean.class ? true : 1);
        return bean;
    }

    /**
     * 实例化Update
     *
     * @param bean
     * @param clazz
     * @param updateNotNull
     * @param optimisticLock
     * @return
     * @throws SqlBeanException
     */
    private static Update newUpdate(SqlBeanDB sqlBeanDB, Class<?> clazz, Object bean, boolean updateNotNull, boolean optimisticLock) {
        Update update = new Update();
        update.setSqlBeanDB(sqlBeanDB);
        update.setTable(clazz);
        update.setBeanClass(clazz);
        update.setUpdateBean(bean);
        update.setUpdateNotNull(updateNotNull);
        update.setOptimisticLock(optimisticLock);
        setSchema(update, clazz);
        return update;
    }

    /**
     * 设置分页参数
     *
     * @param select
     * @param paging
     * @param clazz
     */
    private static void setPaging(Select select, Paging paging, Class<?> clazz) {
        if (paging != null) {
            if (select.getSqlBeanDB().getDbType() == DbType.SQLServer) {
                try {
                    SqlTable sqlTable = SqlBeanUtil.getSqlTable(clazz);
                    select.page(SqlBeanUtil.getTableFieldFullName(select, select.getTable().getAlias(), SqlBeanUtil.getIdField(clazz), sqlTable), paging.getPagenum(), paging.getPagesize(), paging.getStartByZero());
                } catch (SqlBeanException e) {
                    e.printStackTrace();
                }
            } else {
                select.page(null, paging.getPagenum(), paging.getPagesize(), paging.getStartByZero());
            }
            select.orderBy(paging.getOrders());
        }
    }

    /**
     * 统一设置Schema
     *
     * @param common
     * @param clazz
     */
    private static void setSchema(Common common, Class<?> clazz) {
        //自主设置优先级高
        if (StringUtil.isEmpty(common.getTable().getSchema())) {
            String schema = DynSchemaContextHolder.getSchema();
            if (StringUtil.isNotEmpty(schema)) {
                common.getTable().setSchema(schema);
            } else {
                common.getTable().setSchema(SqlBeanUtil.getTable(clazz).getSchema());
            }
        }
    }

}
