package cn.vonce.sql.provider;

import cn.vonce.sql.bean.*;
import cn.vonce.sql.config.SqlBeanDB;
import cn.vonce.sql.constant.SqlHelperCons;
import cn.vonce.sql.enumerate.DbType;
import cn.vonce.sql.enumerate.SqlOperator;
import cn.vonce.sql.exception.SqlBeanException;
import cn.vonce.sql.helper.SqlHelper;
import cn.vonce.sql.helper.Wrapper;
import cn.vonce.sql.uitls.ReflectUtil;
import cn.vonce.sql.uitls.SqlBeanUtil;
import cn.vonce.sql.uitls.StringUtil;

import java.lang.reflect.Field;

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
        if (ids.length > 1) {
            select.where(SqlBeanUtil.getTable(clazz).getAlias(), SqlBeanUtil.getTableFieldName(idField), ids, SqlOperator.IN);
        } else {
            select.where(SqlBeanUtil.getTable(clazz).getAlias(), SqlBeanUtil.getTableFieldName(idField), ids[0], SqlOperator.EQUAL_TO);
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
        select.setWhere(where, args);
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
    public static String selectCountByConditionSql(SqlBeanDB sqlBeanDB, Class<?> clazz, String where, Object[] args) {
        Select select = newSelect(sqlBeanDB, clazz, true);
        select.setWhere(where, args);
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
                select.setColumnList(SqlBeanUtil.getSelectColumns(clazz, select.getFilterFields()));
                if (select.getPage() != null && select.getSqlBeanDB().getDbType() == DbType.SQLServer) {
                    select.getPage().setIdName(SqlBeanUtil.getTableFieldName(SqlBeanUtil.getIdField(clazz)));
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
            select.column(SqlHelperCons.COUNT + SqlHelperCons.BEGIN_BRACKET + SqlHelperCons.ALL + SqlHelperCons.END_BRACKET);
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
        setSchema(delete);
        Field idField;
        try {
            idField = SqlBeanUtil.getIdField(clazz);
        } catch (SqlBeanException e) {
            e.printStackTrace();
            return null;
        }
        delete.where("", SqlBeanUtil.getTableFieldName(idField), id, SqlOperator.IN);
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
        Delete delete = new Delete();
        delete.setSqlBeanDB(sqlBeanDB);
        delete.setTable(clazz);
        delete.setWhere(where, args);
        setSchema(delete);
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
        if (delete.getSqlBeanDB() == null) {
            delete.setSqlBeanDB(sqlBeanDB);
        }
        if (delete.getTable() == null || StringUtil.isEmpty(delete.getTable().getName())) {
            delete.setTable(clazz);
        }
        setSchema(delete);
        if (ignore || (!delete.getWhereMap().isEmpty() || StringUtil.isNotEmpty(delete.getWhere()) || !delete.getWhereWrapper().getDataList().isEmpty())) {
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
        update.setSqlBeanDB(sqlBeanDB);
        Object bean;
        try {
            bean = newLogicallyDeleteBean(clazz);
            update.setUpdateBean(bean);
            Field idField = SqlBeanUtil.getIdField(bean.getClass());
            update.where(SqlBeanUtil.getTableFieldName(idField), id);
            setSchema(update);
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
        update.setSqlBeanDB(sqlBeanDB);
        try {
            update.setUpdateBean(newLogicallyDeleteBean(clazz));
            update.setWhere(where, args);
            setSchema(update);
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
        update.setSqlBeanDB(sqlBeanDB);
        try {
            update.setUpdateBean(newLogicallyDeleteBean(clazz));
            update.setWhere(wrapper);
            setSchema(update);
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
     * @param update
     * @param ignore
     * @return
     */
    public static String updateSql(SqlBeanDB sqlBeanDB, Update update, boolean ignore) {
        if (update.getSqlBeanDB() == null) {
            update.setSqlBeanDB(sqlBeanDB);
        }
        setSchema(update);
        if (ignore || (!update.getWhereMap().isEmpty() || StringUtil.isNotEmpty(update.getWhere()) || !update.getWhereWrapper().getDataList().isEmpty())) {
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
     * @param bean
     * @param updateNotNull
     * @param filterFields
     * @return
     */
    public static String updateByIdSql(SqlBeanDB sqlBeanDB, Object bean, Object id, boolean updateNotNull, String[] filterFields) {
        if (StringUtil.isEmpty(id)) {
            try {
                throw new SqlBeanException("updateByIdSql id不能为空");
            } catch (SqlBeanException e) {
                e.printStackTrace();
                return null;
            }
        }
        Update update = newUpdate(sqlBeanDB, bean, updateNotNull);
        update.setFilterFields(filterFields);
        Field idField;
        try {
            idField = SqlBeanUtil.getIdField(bean.getClass());
        } catch (SqlBeanException e) {
            e.printStackTrace();
            return null;
        }
        update.where(SqlBeanUtil.getTableFieldName(idField), id);
        return SqlHelper.buildUpdateSql(update);
    }

    /**
     * 根据实体类id条件更新
     *
     * @param bean
     * @param updateNotNull
     * @param filterFields
     * @return
     */
    public static String updateByBeanIdSql(SqlBeanDB sqlBeanDB, Object bean, boolean updateNotNull, String[] filterFields) {
        Update update = newUpdate(sqlBeanDB, bean, updateNotNull);
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
            update.where(SqlBeanUtil.getTableFieldName(idField), id);
        } catch (SqlBeanException e) {
            e.printStackTrace();
            return null;
        }
        return SqlHelper.buildUpdateSql(update);
    }

    /**
     * 根据条件更新
     *
     * @param bean
     * @param updateNotNull
     * @param filterFields
     * @param where
     * @param args
     * @return
     */
    public static String updateByConditionSql(SqlBeanDB sqlBeanDB, Object bean, boolean updateNotNull, String[] filterFields, String where, Object[] args) {
        Update update = newUpdate(sqlBeanDB, bean, updateNotNull);
        update.setFilterFields(filterFields);
        update.setWhere(where, args);
        return SqlHelper.buildUpdateSql(update);
    }

    /**
     * 根据实体类字段条件更新
     *
     * @param bean
     * @param updateNotNull
     * @param filterFields
     * @param where
     * @return
     */
    public static String updateByBeanConditionSql(SqlBeanDB sqlBeanDB, Object bean, boolean updateNotNull, String[] filterFields, String where) {
        Update update = newUpdate(sqlBeanDB, bean, updateNotNull);
        update.setFilterFields(filterFields);
        update.setWhere(where, null);
        return SqlHelper.buildUpdateSql(update);
    }

    /**
     * 插入数据
     *
     * @param bean
     * @return
     */
    public static String insertBeanSql(SqlBeanDB sqlBeanDB, Object bean) {
        Insert insert = new Insert();
        insert.setSqlBeanDB(sqlBeanDB);
        insert.setInsertBean(bean);
        setSchema(insert);
        return SqlHelper.buildInsertSql(insert);
    }

    /**
     * 插入数据
     *
     * @param sqlBeanDB
     * @param insert
     * @return
     */
    public static String insertSql(SqlBeanDB sqlBeanDB, Insert insert) {
        if (insert.getSqlBeanDB() == null) {
            insert.setSqlBeanDB(sqlBeanDB);
        }
        setSchema(insert);
        return SqlHelper.buildInsertSql(insert);
    }

    /**
     * 删除表
     *
     * @param clazz
     * @return
     */
    public static String dropTableSql(Class<?> clazz) {
        return "DROP TABLE IF EXISTS " + SqlBeanUtil.getTable(clazz).getName();
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
        return SqlHelper.buildCreateSql(create);
    }

    /**
     * 获取表名列表
     *
     * @param sqlBeanDB
     * @return
     */
    public static String selectTableListSql(SqlBeanDB sqlBeanDB) {
        switch (sqlBeanDB.getDbType()) {
            case MySQL:
            case MariaDB:
                return "select table_name as `name` from information_schema.tables where table_schema = database() and table_type = 'BASE TABLE'";
            case SQLServer:
                return "select name from sysobjects where xtype='U'";
            case Oracle:
                return "select table_name as \"name\" from user_tables";
            case PostgreSQL:
                return "select tablename as \"name\" from pg_tables where schemaname = 'public'";
            case DB2:
//                return "select tabname AS \"table\" from syscat.tables where tabschema = current schema";
                return "select name from sysibm.systables where type = 'T' and creator = current user";
            case H2:
                return "select table_name as \"name\" from information_schema.tables where table_type = 'TABLE'";
            case Hsql:
                return "select table_name as \"name\" from information_schema.tables where table_type = 'BASE TABLE'";
            case Derby:
                return "select tablename as \"name\" from SYS.systables where tabletype = 'T'";
            case SQLite:
                return "select name from sqlite_master where type='table'";
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
    public static String backupSql(SqlBeanDB sqlBeanDB, Class<?> clazz, String targetTableName, Column[] columns, Wrapper wrapper) {
        Backup backup = new Backup();
        backup.setSqlBeanDB(sqlBeanDB);
        backup.setTable(clazz);
        backup.setColumns(columns);
        backup.setTargetTableName(targetTableName);
        backup.setWhere(wrapper);
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
    public static String copySql(SqlBeanDB sqlBeanDB, Class<?> clazz, String targetTableName, Column[] columns, Wrapper wrapper) {
        Copy copy = new Copy();
        copy.setSqlBeanDB(sqlBeanDB);
        copy.setTable(clazz);
        copy.setColumns(columns);
        copy.setTargetTableName(targetTableName);
        copy.setWhere(wrapper);
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
        try {
            if (isCount) {
                select.column(SqlHelperCons.COUNT + SqlHelperCons.BEGIN_BRACKET + SqlHelperCons.ALL + SqlHelperCons.END_BRACKET);
            } else {
                select.setColumnList(SqlBeanUtil.getSelectColumns(clazz, select.getFilterFields()));
            }
            SqlBeanUtil.setJoin(select, clazz);
            setSchema(select);
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
        setSchema(select);
        try {
            SqlBeanUtil.setJoin(select, clazz);
        } catch (SqlBeanException e) {
            e.printStackTrace();
            return null;
        }
        if (!select.getOrderBy().isEmpty()) {
            for (Order order : select.getOrderBy()) {
                if (StringUtil.isEmpty(order.getTableAlias())) {
                    order.setTableAlias(select.getTable().getAlias());
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
        ReflectUtil.instance().set(bean.getClass(), bean, field.getName(), true);
        return bean;
    }

    /**
     * 实例化Update
     *
     * @param bean
     * @param updateNotNull
     * @return
     * @throws SqlBeanException
     */
    private static Update newUpdate(SqlBeanDB sqlBeanDB, Object bean, boolean updateNotNull) {
        Update update = new Update();
        update.setSqlBeanDB(sqlBeanDB);
        update.setUpdateBean(bean);
        update.setUpdateNotNull(updateNotNull);
        setSchema(update);
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
                    select.setPage(SqlBeanUtil.getTableFieldName(SqlBeanUtil.getIdField(clazz)), paging.getPagenum(), paging.getPagesize());
                } catch (SqlBeanException e) {
                    e.printStackTrace();
                }
            } else {
                select.setPage(null, paging.getPagenum(), paging.getPagesize());
            }
            select.orderBy(paging.getOrders());
        }
    }

    /**
     * 统一设置Schema
     */
    private static void setSchema(Common common) {
        //自主设置优先级高
        if (StringUtil.isEmpty(common.getTable().getSchema())) {
            String schema = SchemaContextHolder.getSchema();
            if (StringUtil.isNotEmpty(schema)) {
                common.getTable().setSchema(schema);
            }
        }
    }

}
