package cn.vonce.sql.provider;

import cn.vonce.sql.annotation.SqlTable;
import cn.vonce.sql.bean.*;
import cn.vonce.sql.config.SqlBeanDB;
import cn.vonce.sql.constant.SqlConstant;
import cn.vonce.sql.enumerate.*;
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
     * @param sqlBeanDB
     * @param clazz
     * @param returnType
     * @param id
     * @return
     */
    public static String selectByIdSql(SqlBeanDB sqlBeanDB, Class<?> clazz, Class<?> returnType, Object id) {
        return selectByIdsSql(sqlBeanDB, clazz, returnType, new Object[]{id});
    }

    /**
     * 根据ids条件查询
     *
     * @param sqlBeanDB
     * @param clazz
     * @param returnType
     * @param ids
     * @return
     */
    public static String selectByIdsSql(SqlBeanDB sqlBeanDB, Class<?> clazz, Class<?> returnType, Object... ids) {
        //如果returnType类与clazz的SqlTable一致，说明这两个对象指向的都是同一张表，那么returnType则继承于clazz并包装了额外的属性
        if (returnType != null && SqlBeanUtil.sqlTableIsConsistent(clazz, returnType)) {
            clazz = returnType;
        }
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
     * @param sqlBeanDB
     * @param clazz
     * @param returnType
     * @param paging
     * @param where
     * @param args
     * @return
     */
    public static String selectBySql(SqlBeanDB sqlBeanDB, Class<?> clazz, Class<?> returnType, Paging paging, String where, Object... args) {
        //如果returnType类与clazz的SqlTable一致，说明这两个对象指向的都是同一张表，那么returnType则继承于clazz并包装了额外的属性
        if (returnType != null && SqlBeanUtil.sqlTableIsConsistent(clazz, returnType)) {
            clazz = returnType;
        }
        Select select = newSelect(sqlBeanDB, clazz, false);
        select.where(where, args);
        setPaging(select, paging, clazz);
        return SqlHelper.buildSelectSql(select);
    }

    /**
     * 根据条件查询统计
     *
     * @param sqlBeanDB
     * @param clazz
     * @param where
     * @param args
     * @return
     */
    public static String countBySql(SqlBeanDB sqlBeanDB, Class<?> clazz, String where, Object[] args) {
        Select select = newSelect(sqlBeanDB, clazz, true);
        select.where(where, args);
        return SqlHelper.buildSelectSql(select);
    }

    /**
     * 查询全部
     *
     * @param sqlBeanDB
     * @param clazz
     * @param returnType
     * @param paging
     * @return
     */
    public static String selectAllSql(SqlBeanDB sqlBeanDB, Class<?> clazz, Class<?> returnType, Paging paging) {
        //如果returnType类与clazz的SqlTable一致，说明这两个对象指向的都是同一张表，那么returnType则继承于clazz并包装了额外的属性
        if (returnType != null && SqlBeanUtil.sqlTableIsConsistent(clazz, returnType)) {
            clazz = returnType;
        }
        Select select = newSelect(sqlBeanDB, clazz, false);
        setPaging(select, paging, clazz);
        return SqlHelper.buildSelectSql(select);
    }

    /**
     * 根据自定义条件查询（可自动分页）
     *
     * @param sqlBeanDB
     * @param clazz
     * @param returnType
     * @param select
     * @return
     */
    public static String selectSql(SqlBeanDB sqlBeanDB, Class<?> clazz, Class<?> returnType, Select select) {
        //如果returnType类与clazz的SqlTable一致，说明这两个对象指向的都是同一张表，那么returnType则继承于clazz并包装了额外的属性
        if (returnType != null && SqlBeanUtil.sqlTableIsConsistent(clazz, returnType)) {
            clazz = returnType;
        }
        if (select.getSqlBeanDB() == null) {
            select.setSqlBeanDB(sqlBeanDB);
        }
        if (select.getColumnList().isEmpty()) {
            try {
                SqlTable sqlTable = SqlBeanUtil.getSqlTable(clazz);
                select.setColumnList(SqlBeanUtil.getSelectColumns(clazz, select.getFilterFields()));
                if (select.getPage() != null && select.getSqlBeanDB().getDbType() == DbType.SQLServer) {
                    select.getPage().setIdName(SqlBeanUtil.getTableFieldName(SqlBeanUtil.getIdField(clazz), sqlTable));
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
     * @param returnType
     * @param select
     * @return
     */
    public static String countSql(SqlBeanDB sqlBeanDB, Class<?> clazz, Class<?> returnType, Select select) {
        //如果returnType类与clazz的SqlTable一致，说明这两个对象指向的都是同一张表，那么returnType则继承于clazz并包装了额外的属性
        if (returnType != null && SqlBeanUtil.sqlTableIsConsistent(clazz, returnType)) {
            clazz = returnType;
        }
        if (select.getSqlBeanDB() == null) {
            select.setSqlBeanDB(sqlBeanDB);
        }
        select.setCount(true);
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
        delete.where().in(SqlBeanUtil.getTableFieldName(delete, idField, sqlTable), id);
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
    public static String deleteBySql(SqlBeanDB sqlBeanDB, Class<?> clazz, String where, Object[] args) {
        //如果是逻辑删除那么将Delete对象转为Update对象
        if (SqlBeanUtil.checkLogically(clazz)) {
            return logicallyDeleteBySql(sqlBeanDB, clazz, where, args);
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
            update.where().in(SqlBeanUtil.getTableFieldName(update, idField, sqlTable), id);
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
    public static String logicallyDeleteBySql(SqlBeanDB sqlBeanDB, Class<?> clazz, String where, Object[] args) {
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
    public static String logicallyDeleteBySql(SqlBeanDB sqlBeanDB, Class<?> clazz, Wrapper wrapper) {
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
        update.where().eq(SqlBeanUtil.getTableFieldName(update, idField, sqlTable), id);
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
            update.where().eq(SqlBeanUtil.getTableFieldName(update, idField, sqlTable), id);
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
    public static String updateBySql(SqlBeanDB sqlBeanDB, Class<?> clazz, Object bean, boolean updateNotNull, boolean optimisticLock, String[] filterFields, String where, Object[] args) {
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
    public static String updateByBeanSql(SqlBeanDB sqlBeanDB, Class<?> clazz, Object bean, boolean updateNotNull, boolean optimisticLock, String[] filterFields, String where) {
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
    public static String selectTableListSql(SqlBeanDB sqlBeanDB, String schema, String name) {
        if (sqlBeanDB.getSqlBeanConfig().getToUpperCase() != null && sqlBeanDB.getSqlBeanConfig().getToUpperCase() && StringUtil.isNotEmpty(name)) {
            name = name.toUpperCase();
        }
        switch (sqlBeanDB.getDbType()) {
            case MySQL:
            case MariaDB:
                return JavaMapMySqlType.getTableListSql(sqlBeanDB, schema, name);
            case SQLServer:
                return JavaMapSqlServerType.getTableListSql(sqlBeanDB, schema, name);
            case Oracle:
                return JavaMapOracleType.getTableListSql(sqlBeanDB, schema, name);
            case Postgresql:
                return JavaMapPostgresqlType.getTableListSql(sqlBeanDB, schema, name);
            case DB2:
                return JavaMapDB2Type.getTableListSql(sqlBeanDB, schema, name);
            case H2:
                return JavaMapH2Type.getTableListSql(sqlBeanDB, schema, name);
            case Hsql:
                return JavaMapHsqlType.getTableListSql(sqlBeanDB, schema, name);
            case Derby:
                return JavaMapDerbyType.getTableListSql(sqlBeanDB, schema, name);
            case SQLite:
                return JavaMapSqliteType.getTableListSql(sqlBeanDB, schema, name);
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
        switch (sqlBeanDB.getDbType()) {
            case MySQL:
            case MariaDB:
                return JavaMapMySqlType.getColumnListSql(sqlBeanDB, null, name);
            case SQLServer:
                return JavaMapSqlServerType.getColumnListSql(sqlBeanDB, null, name);
            case Oracle:
                return JavaMapOracleType.getColumnListSql(sqlBeanDB, null, name);
            case Postgresql:
                return JavaMapPostgresqlType.getColumnListSql(sqlBeanDB, null, name);
            case DB2:
                return JavaMapDB2Type.getColumnListSql(sqlBeanDB, null, name);
            case H2:
                return JavaMapH2Type.getColumnListSql(sqlBeanDB, null, name);
            case Hsql:
                return JavaMapHsqlType.getColumnListSql(sqlBeanDB, null, name);
            case Derby:
                return JavaMapDerbyType.getColumnListSql(sqlBeanDB, null, name);
            case SQLite:
                return JavaMapSqliteType.getColumnListSql(sqlBeanDB, null, name);
            default:
                throw new SqlBeanException("请配置正确的数据库");
        }
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
        select.setCount(isCount);
        try {
            if (!isCount) {
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
                    select.page(SqlBeanUtil.getTableFieldName(SqlBeanUtil.getIdField(clazz), sqlTable), paging.getPagenum(), paging.getPagesize(), paging.getStartByZero());
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
