package cn.vonce.sql.spring.service;

import cn.vonce.sql.bean.*;
import cn.vonce.sql.config.SqlBeanConfig;
import cn.vonce.sql.config.SqlBeanDB;
import cn.vonce.sql.define.ColumnFun;
import cn.vonce.sql.enumerate.DbType;
import cn.vonce.sql.exception.SqlBeanException;
import cn.vonce.sql.helper.Wrapper;
import cn.vonce.sql.page.PageHelper;
import cn.vonce.sql.page.ResultData;
import cn.vonce.sql.provider.SqlBeanProvider;
import cn.vonce.sql.service.TableService;
import cn.vonce.sql.spring.annotation.DbSwitch;
import cn.vonce.sql.spring.config.UseMybatis;
import cn.vonce.sql.spring.dao.MybatisSqlBeanDao;
import cn.vonce.sql.service.SqlBeanService;
import cn.vonce.sql.spring.enumerate.DbRole;
import cn.vonce.sql.uitls.DateUtil;
import cn.vonce.sql.uitls.SqlBeanUtil;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.*;

/**
 * 通用的业务实现
 *
 * @param <T>
 * @author Jovi
 * @version 1.0
 * @email 766255988@qq.com
 * @date 2018年5月15日下午3:57:58
 */
@UseMybatis
@Service
public class MybatisSqlBeanServiceImpl<T, ID> extends BaseSqlBeanServiceImpl implements SqlBeanService<T, ID>, TableService<T> {

    private Logger logger = LoggerFactory.getLogger(MybatisSqlBeanServiceImpl.class);

    @Autowired
    private MybatisSqlBeanDao<T> mybatisSqlBeanDao;

    @Autowired(required = false)
    private SqlBeanConfig sqlBeanConfig;

    @Autowired
    private SqlSession sqlSession;

    private boolean initDBInfo;

    private Class<?> clazz;

    public MybatisSqlBeanServiceImpl() {
        Type[] typeArray = new Type[]{getClass().getGenericSuperclass()};
        if (typeArray == null || typeArray.length == 0) {
            typeArray = getClass().getGenericInterfaces();
        }
        for (Type type : typeArray) {
            if (type instanceof ParameterizedType) {
                Class<?> trueTypeClass = (Class<?>) ((ParameterizedType) type).getActualTypeArguments()[0];
                try {
                    clazz = this.getClass().getClassLoader().loadClass(trueTypeClass.getName());
                    return;
                } catch (ClassNotFoundException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
    }

    @Override
    public SqlBeanConfig getSqlBeanConfig() {
        return sqlBeanConfig;
    }

    @Override
    public SqlBeanDB initDBInfo() {
        SqlBeanDB sqlBeanDB = new SqlBeanDB();
        if (!initDBInfo) {
            try {
                Connection connection = sqlSession.getConfiguration().getEnvironment().getDataSource().getConnection();
                DatabaseMetaData metaData = connection.getMetaData();
                super.sqlBeanDBFill(sqlBeanDB, metaData);
                connection.close();
                initDBInfo = true;
            } catch (SQLException e) {
                logger.error(e.getMessage(), e);
            }
        }
        return sqlBeanDB;
    }

    @Override
    public Class<?> getBeanClass() {
        return clazz;
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public T selectById(ID id) {
        if (id == null) {
            return null;
        }
        return mybatisSqlBeanDao.selectById(getSqlBeanDB(), clazz, id);
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public <R> R selectById(Class<R> returnType, ID id) {
        if (id == null) {
            return null;
        }
        return mybatisSqlBeanDao.selectByIdO(getSqlBeanDB(), clazz, returnType, id);
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public List<T> selectByIds(ID... ids) {
        if (ids == null || ids.length == 0) {
            throw new SqlBeanException("selectByIds方法ids参数至少拥有一个值");
        }
        return mybatisSqlBeanDao.selectByIds(getSqlBeanDB(), clazz, ids);
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public <R> List<R> selectByIds(Class<R> returnType, ID... ids) {
        if (ids == null || ids.length == 0) {
            throw new SqlBeanException("selectByIds方法ids参数至少拥有一个值");
        }
        return mybatisSqlBeanDao.selectByIdsO(getSqlBeanDB(), clazz, returnType, ids);
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public T selectOne(Select select) {
        return mybatisSqlBeanDao.selectOne(getSqlBeanDB(), clazz, select);
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public <R> R selectOne(Class<R> returnType, Select select) {
        return mybatisSqlBeanDao.selectOneO(getSqlBeanDB(), clazz, returnType, select);
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public Map<String, Object> selectMap(Select select) {
        return mybatisSqlBeanDao.selectMap(getSqlBeanDB(), clazz, select);
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public T selectOneBy(String where, Object... args) {
        return mybatisSqlBeanDao.selectOneBy(getSqlBeanDB(), clazz, where, args);
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public <R> R selectOneBy(Class<R> returnType, String where, Object... args) {
        return mybatisSqlBeanDao.selectOneByO(getSqlBeanDB(), clazz, returnType, where, args);
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public T selectOneBy(Wrapper wrapper) {
        Select select = new Select();
        select.where(wrapper);
        return mybatisSqlBeanDao.selectOne(getSqlBeanDB(), clazz, select);
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public <R> R selectOneBy(Class<R> returnType, Wrapper wrapper) {
        Select select = new Select();
        select.where(wrapper);
        return mybatisSqlBeanDao.selectOneO(getSqlBeanDB(), clazz, returnType, select);
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public <R> List<R> selectBy(Class<R> returnType, String where, Object... args) {
        return mybatisSqlBeanDao.selectByO(getSqlBeanDB(), clazz, returnType, null, where, args);
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public <R> List<R> selectBy(Class<R> returnType, Wrapper wrapper) {
        Select select = new Select();
        select.where(wrapper);
        return mybatisSqlBeanDao.selectO(getSqlBeanDB(), clazz, returnType, select);
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public <R> List<R> selectBy(Class<R> returnType, Paging paging, String where, Object... args) {
        return mybatisSqlBeanDao.selectByO(getSqlBeanDB(), clazz, returnType, paging, where, args);
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public <R> List<R> selectBy(Class<R> returnType, Paging paging, Wrapper wrapper) {
        Select select = new Select();
        select.where(wrapper);
        select.page(paging.getPagenum(), paging.getPagesize(), paging.getStartByZero());
        select.orderBy(paging.getOrders());
        return mybatisSqlBeanDao.selectO(getSqlBeanDB(), clazz, returnType, select);
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public List<T> selectBy(String where, Object... args) {
        return mybatisSqlBeanDao.selectBy(getSqlBeanDB(), clazz, null, where, args);
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public List<T> selectBy(Wrapper wrapper) {
        Select select = new Select();
        select.where(wrapper);
        return mybatisSqlBeanDao.select(getSqlBeanDB(), clazz, select);
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public List<T> selectBy(Paging paging, String where, Object... args) {
        return mybatisSqlBeanDao.selectBy(getSqlBeanDB(), clazz, paging, where, args);
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public List<T> selectBy(Paging paging, Wrapper wrapper) {
        Select select = new Select();
        select.where(wrapper);
        select.page(paging.getPagenum(), paging.getPagesize(), paging.getStartByZero());
        select.orderBy(paging.getOrders());
        return mybatisSqlBeanDao.select(getSqlBeanDB(), clazz, select);
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public int countBy(String where, Object... args) {
        return mybatisSqlBeanDao.countBy(getSqlBeanDB(), clazz, where, args);
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public int countBy(Wrapper wrapper) {
        Select select = new Select();
        select.where(wrapper);
        return mybatisSqlBeanDao.count(getSqlBeanDB(), clazz, null, select);
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public int count() {
        return mybatisSqlBeanDao.countBy(getSqlBeanDB(), clazz, null, null);
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public List<T> select() {
        return mybatisSqlBeanDao.selectAll(getSqlBeanDB(), clazz, null);
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public List<T> select(Paging paging) {
        return mybatisSqlBeanDao.selectAll(getSqlBeanDB(), clazz, paging);
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public <R> List<R> select(Class<R> returnType) {
        return mybatisSqlBeanDao.selectAllO(getSqlBeanDB(), clazz, returnType, null);
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public <R> List<R> select(Class<R> returnType, Paging paging) {
        return mybatisSqlBeanDao.selectAllO(getSqlBeanDB(), clazz, returnType, paging);
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public List<Map<String, Object>> selectMapList(Select select) {
        return mybatisSqlBeanDao.selectMapList(getSqlBeanDB(), clazz, select);
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public <R> List<R> select(Class<R> returnType, Select select) {
        return mybatisSqlBeanDao.selectO(getSqlBeanDB(), clazz, returnType, select);
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public List<T> select(Select select) {
        return mybatisSqlBeanDao.select(getSqlBeanDB(), clazz, select);
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public int count(Select select) {
        return mybatisSqlBeanDao.count(getSqlBeanDB(), clazz, null, select);
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public int count(Class<?> returnType, Select select) {
        return mybatisSqlBeanDao.count(getSqlBeanDB(), clazz, returnType, select);
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public ResultData<T> paging(Select select, PageHelper<T> pageHelper) {
        pageHelper.paging(select, this);
        return pageHelper.getResultData();
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public ResultData<T> paging(Select select, int pagenum, int pagesize) {
        PageHelper<T> pageHelper = new PageHelper<>(pagenum, pagesize);
        pageHelper.paging(select, this);
        return pageHelper.getResultData();
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public <R> ResultData<R> paging(Class<R> tClazz, Select select, PageHelper<R> pageHelper) {
        pageHelper.paging(tClazz, select, this);
        return pageHelper.getResultData();
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public <R> ResultData<R> paging(Class<R> tClazz, Select select, int pagenum, int pagesize) {
        PageHelper<R> pageHelper = new PageHelper<>(pagenum, pagesize);
        pageHelper.paging(tClazz, select, this);
        return pageHelper.getResultData();
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int deleteById(ID... id) {
        if (id == null || id.length == 0) {
            throw new SqlBeanException("deleteById方法id参数至少拥有一个值");
        }
        return mybatisSqlBeanDao.deleteById(getSqlBeanDB(), clazz, id);
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int deleteBy(String where, Object... args) {
        return mybatisSqlBeanDao.deleteBy(getSqlBeanDB(), clazz, where, args);
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int deleteBy(Wrapper wrapper) {
        Delete delete = new Delete();
        delete.setLogicallyDelete(SqlBeanUtil.checkLogically(clazz));
        delete.where(wrapper);
        return mybatisSqlBeanDao.delete(getSqlBeanDB(), clazz, delete, false);
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int delete(Delete delete) {
        return mybatisSqlBeanDao.delete(getSqlBeanDB(), clazz, delete, false);
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int delete(Delete delete, boolean ignore) {
        return mybatisSqlBeanDao.delete(getSqlBeanDB(), clazz, delete, ignore);
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int logicallyDeleteById(ID... id) {
        if (id == null || id.length == 0) {
            throw new SqlBeanException("logicallyDeleteById方法id参数至少拥有一个值");
        }
        return mybatisSqlBeanDao.logicallyDeleteById(getSqlBeanDB(), clazz, id);
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int logicallyDeleteBy(String where, Object... args) {
        return mybatisSqlBeanDao.logicallyDeleteBy(getSqlBeanDB(), clazz, where, args);
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int logicallyDeleteBy(Wrapper wrapper) {
        return mybatisSqlBeanDao.logicallyDeleteByWrapper(getSqlBeanDB(), clazz, wrapper);
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int update(Update<T> update) {
        return mybatisSqlBeanDao.update(getSqlBeanDB(), clazz, update, false);
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int update(Update<T> update, boolean ignore) {
        return mybatisSqlBeanDao.update(getSqlBeanDB(), clazz, update, ignore);
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int updateById(T bean, ID id) {
        return mybatisSqlBeanDao.updateById(getSqlBeanDB(), clazz, bean, id, true, false, null);
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int updateById(T bean, ID id, boolean updateNotNull, boolean optimisticLock) {
        return mybatisSqlBeanDao.updateById(getSqlBeanDB(), clazz, bean, id, updateNotNull, optimisticLock, null);
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int updateByBeanId(T bean) {
        return mybatisSqlBeanDao.updateByBeanId(getSqlBeanDB(), clazz, bean, true, false, null);
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int updateById(T bean, ID id, boolean updateNotNull, boolean optimisticLock, Column... filterColumns) {
        return mybatisSqlBeanDao.updateById(getSqlBeanDB(), clazz, bean, id, updateNotNull, optimisticLock, filterColumns);
    }

    @Override
    public <R> int updateById(T bean, ID id, boolean updateNotNull, boolean optimisticLock, ColumnFun<T, R>... filterColumns) {
        return mybatisSqlBeanDao.updateById(getSqlBeanDB(), clazz, bean, id, updateNotNull, optimisticLock, SqlBeanUtil.funToColumn(filterColumns));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int updateBy(T bean, String where, Object... args) {
        return mybatisSqlBeanDao.updateByBeanId(getSqlBeanDB(), clazz, bean, true, false, null);
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int updateByBeanId(T bean, boolean updateNotNull, boolean optimisticLock) {
        return mybatisSqlBeanDao.updateByBeanId(getSqlBeanDB(), clazz, bean, updateNotNull, optimisticLock, null);
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int updateByBeanId(T bean, boolean updateNotNull, boolean optimisticLock, Column... filterColumns) {
        return mybatisSqlBeanDao.updateByBeanId(getSqlBeanDB(), clazz, bean, updateNotNull, optimisticLock, filterColumns);
    }

    @Override
    public <R> int updateByBeanId(T bean, boolean updateNotNull, boolean optimisticLock, ColumnFun<T, R>... filterColumns) {
        return mybatisSqlBeanDao.updateByBeanId(getSqlBeanDB(), clazz, bean, updateNotNull, optimisticLock, SqlBeanUtil.funToColumn(filterColumns));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int updateBy(T bean, boolean updateNotNull, boolean optimisticLock, String where, Object... args) {
        return mybatisSqlBeanDao.updateBy(getSqlBeanDB(), clazz, bean, updateNotNull, optimisticLock, null, where, args);
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int updateBy(T bean, Wrapper wrapper) {
        Update update = new Update();
        update.setUpdateBean(bean);
        update.setUpdateNotNull(true);
        update.setOptimisticLock(false);
        update.where(wrapper);
        return mybatisSqlBeanDao.update(getSqlBeanDB(), clazz, update, false);
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int updateBy(T bean, boolean updateNotNull, boolean optimisticLock, Wrapper wrapper) {
        Update update = new Update();
        update.setUpdateBean(bean);
        update.setUpdateNotNull(updateNotNull);
        update.setOptimisticLock(optimisticLock);
        update.where(wrapper);
        return mybatisSqlBeanDao.update(getSqlBeanDB(), clazz, update, false);
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int updateBy(T bean, boolean updateNotNull, boolean optimisticLock, Column[] filterColumns, String where, Object... args) {
        return mybatisSqlBeanDao.updateBy(getSqlBeanDB(), clazz, bean, updateNotNull, optimisticLock, filterColumns, where, args);
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int updateBy(T bean, boolean updateNotNull, boolean optimisticLock, Wrapper wrapper, Column... filterColumns) {
        Update update = new Update();
        update.setUpdateBean(bean);
        update.setUpdateNotNull(updateNotNull);
        update.setOptimisticLock(optimisticLock);
        update.filterFields(filterColumns);
        update.where(wrapper);
        return mybatisSqlBeanDao.update(getSqlBeanDB(), clazz, update, false);
    }

    @Override
    public <R> int updateBy(T bean, boolean updateNotNull, boolean optimisticLock, Wrapper wrapper, ColumnFun<T, R>... filterColumns) {
        return this.updateBy(bean, updateNotNull, optimisticLock, wrapper, SqlBeanUtil.funToColumn(filterColumns));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int updateByBean(T bean, String where) {
        return mybatisSqlBeanDao.updateByBean(getSqlBeanDB(), clazz, bean, true, false, null, null);
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int updateByBean(T bean, boolean updateNotNull, boolean optimisticLock, String where) {
        return mybatisSqlBeanDao.updateByBean(getSqlBeanDB(), clazz, bean, updateNotNull, optimisticLock, null, null);
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int updateByBean(T bean, boolean updateNotNull, boolean optimisticLock, String where, Column... filterColumns) {
        return mybatisSqlBeanDao.updateByBean(getSqlBeanDB(), clazz, bean, updateNotNull, optimisticLock, where, filterColumns);
    }

    @Override
    public <R> int updateByBean(T bean, boolean updateNotNull, boolean optimisticLock, String where, ColumnFun<T, R>... filterColumns) {
        return mybatisSqlBeanDao.updateByBean(getSqlBeanDB(), clazz, bean, updateNotNull, optimisticLock, where, SqlBeanUtil.funToColumn(filterColumns));
    }

    @SuppressWarnings("unchecked")
    @DbSwitch(DbRole.MASTER)
    @Override
    public int insert(T... bean) {
        if (bean == null || bean.length == 0) {
            throw new SqlBeanException("insert方法bean参数至少拥有一个值");
        }
        return mybatisSqlBeanDao.insertBean(getSqlBeanDB(), clazz, Arrays.asList(bean));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int insert(Collection<T> beanList) {
        if (beanList == null || beanList.size() == 0) {
            throw new SqlBeanException("insert方法beanList参数至少拥有一个值");
        }
        return mybatisSqlBeanDao.insertBean(getSqlBeanDB(), clazz, beanList);
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int insert(Insert<T> insert) {
        return mybatisSqlBeanDao.insert(getSqlBeanDB(), clazz, insert);
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public String backup() {
        String targetTableName = SqlBeanUtil.getTable(clazz).getName() + "_" + DateUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS");
        mybatisSqlBeanDao.backup(getSqlBeanDB(), clazz, null, targetTableName, null, null);
        return targetTableName;
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public void backup(String targetTableName) {
        mybatisSqlBeanDao.backup(getSqlBeanDB(), clazz, null, targetTableName, null, null);
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public void backup(String targetSchema, String targetTableName) {
        mybatisSqlBeanDao.backup(getSqlBeanDB(), clazz, null, targetSchema, targetTableName, null);
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public void backup(Wrapper wrapper, String targetTableName, Column... columns) {
        mybatisSqlBeanDao.backup(getSqlBeanDB(), clazz, wrapper, null, targetTableName, columns);
    }

    @Override
    public <R> void backup(Wrapper wrapper, String targetTableName, ColumnFun<T, R>... columns) {
        mybatisSqlBeanDao.backup(getSqlBeanDB(), clazz, wrapper, null, targetTableName, SqlBeanUtil.funToColumn(columns));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public void backup(Wrapper wrapper, String targetSchema, String targetTableName, Column... columns) {
        mybatisSqlBeanDao.backup(getSqlBeanDB(), clazz, wrapper, targetSchema, targetTableName, columns);
    }

    @Override
    public <R> void backup(Wrapper wrapper, String targetSchema, String targetTableName, ColumnFun<T, R>... columns) {
        mybatisSqlBeanDao.backup(getSqlBeanDB(), clazz, wrapper, targetSchema, targetTableName, SqlBeanUtil.funToColumn(columns));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int copy(Wrapper wrapper, String targetTableName) {
        return mybatisSqlBeanDao.copy(getSqlBeanDB(), clazz, wrapper, null, targetTableName, null);
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int copy(Wrapper wrapper, String targetSchema, String targetTableName) {
        return mybatisSqlBeanDao.copy(getSqlBeanDB(), clazz, wrapper, targetSchema, targetTableName, null);
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int copy(Wrapper wrapper, String targetTableName, Column... columns) {
        return mybatisSqlBeanDao.copy(getSqlBeanDB(), clazz, wrapper, null, targetTableName, columns);
    }

    @Override
    public <R> int copy(Wrapper wrapper, String targetTableName, ColumnFun<T, R>... columns) {
        return mybatisSqlBeanDao.copy(getSqlBeanDB(), clazz, wrapper, null, targetTableName, SqlBeanUtil.funToColumn(columns));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int copy(Wrapper wrapper, String targetSchema, String targetTableName, Column... columns) {
        return mybatisSqlBeanDao.copy(getSqlBeanDB(), clazz, wrapper, targetSchema, targetTableName, columns);
    }

    @Override
    public <R> int copy(Wrapper wrapper, String targetSchema, String targetTableName, ColumnFun<T, R>... columns) {
        return mybatisSqlBeanDao.copy(getSqlBeanDB(), clazz, wrapper, targetSchema, targetTableName, SqlBeanUtil.funToColumn(columns));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int alter(Table table, List<ColumnInfo> columnInfoList) {
        List<String> sqlList = SqlBeanProvider.buildAlterSql(getSqlBeanDB(), clazz, columnInfoList);
        int count = 0;
        if (sqlList != null && sqlList.size() > 0) {
            for (String sql : sqlList) {
                count += mybatisSqlBeanDao.executeSql(sql);
            }
        }
        return count;
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int alter(Alter alter) {
        List<Alter> alterList = new ArrayList<>();
        alterList.add(alter);
        return alter(alterList);
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int alter(List<Alter> alterList) {
        List<String> sqlList = SqlBeanProvider.alterSql(getSqlBeanDB().getDbType(), alterList);
        int count = 0;
        if (sqlList != null && sqlList.size() > 0) {
            for (String sql : sqlList) {
                count += mybatisSqlBeanDao.executeSql(sql);
            }
        }
        return count;
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public void dropTable() {
        SqlBeanDB sqlBeanDB = getSqlBeanDB();
        if (sqlBeanDB.getDbType() != DbType.MySQL && sqlBeanDB.getDbType() != DbType.MariaDB && sqlBeanDB.getDbType() != DbType.Postgresql && sqlBeanDB.getDbType() != DbType.SQLServer && sqlBeanDB.getDbType() != DbType.H2) {
            List<TableInfo> nameList = mybatisSqlBeanDao.selectTableList(getSqlBeanDB(), SqlBeanUtil.getTable(clazz).getName());
            if (nameList == null || nameList.isEmpty()) {
                return;
            }
        }
        mybatisSqlBeanDao.drop(sqlBeanDB, clazz);
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public void createTable() {
        mybatisSqlBeanDao.create(getSqlBeanDB(), clazz);
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public void dropAndCreateTable() {
        dropTable();
        createTable();
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public List<TableInfo> getTableList(String tableName) {
        return mybatisSqlBeanDao.selectTableList(getSqlBeanDB(), tableName);
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public List<ColumnInfo> getColumnInfoList(String tableName) {
        List<ColumnInfo> columnInfoList = mybatisSqlBeanDao.selectColumnInfoList(getSqlBeanDB(), tableName);
        super.handleColumnInfo(columnInfoList);
        return columnInfoList;
    }

}
