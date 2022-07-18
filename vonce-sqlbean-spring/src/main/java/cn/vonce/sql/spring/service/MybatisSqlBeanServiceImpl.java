package cn.vonce.sql.spring.service;

import cn.vonce.sql.bean.*;
import cn.vonce.sql.config.SqlBeanConfig;
import cn.vonce.sql.config.SqlBeanDB;
import cn.vonce.sql.enumerate.DbType;
import cn.vonce.sql.exception.SqlBeanException;
import cn.vonce.sql.helper.Wrapper;
import cn.vonce.sql.page.PageHelper;
import cn.vonce.sql.page.ResultData;
import cn.vonce.sql.service.TableService;
import cn.vonce.sql.spring.annotation.DbSwitch;
import cn.vonce.sql.spring.config.UseMybatis;
import cn.vonce.sql.spring.dao.MybatisSqlBeanDao;
import cn.vonce.sql.service.SqlBeanService;
import cn.vonce.sql.spring.enumerate.DbRole;
import cn.vonce.sql.uitls.DateUtil;
import cn.vonce.sql.uitls.SqlBeanUtil;
import cn.vonce.sql.uitls.StringUtil;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Connection;
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
public class MybatisSqlBeanServiceImpl<T, ID> extends BaseSqlBeanServiceImpl implements SqlBeanService<T, ID>, TableService {

    private Logger logger = LoggerFactory.getLogger(MybatisSqlBeanServiceImpl.class);

    @Autowired
    private MybatisSqlBeanDao<T> mybatisSqlBeanDao;

    @Autowired(required = false)
    private SqlBeanConfig sqlBeanConfig;

    @Autowired
    private SqlSession sqlSession;

    private String productName;

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
    public String getProductName() {
        if (StringUtil.isEmpty(productName)) {
            try {
                Connection connection = sqlSession.getConfiguration().getEnvironment().getDataSource().getConnection();
                productName = connection.getMetaData().getDatabaseProductName();
                connection.close();
            } catch (SQLException e) {
                logger.error(e.getMessage(), e);
            }
        }
        return productName;
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
        if (SqlBeanUtil.isBaseType(returnType.getName()) || SqlBeanUtil.isMap(returnType.getName())) {
            return mybatisSqlBeanDao.selectByIdO(getSqlBeanDB(), clazz, returnType, id);
        }
        return mybatisSqlBeanDao.selectByIdO(getSqlBeanDB(), returnType, returnType, id);
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
        if (SqlBeanUtil.isBaseType(returnType.getName()) || SqlBeanUtil.isMap(returnType.getName())) {
            return mybatisSqlBeanDao.selectByIdsO(getSqlBeanDB(), clazz, returnType, ids);
        }
        return mybatisSqlBeanDao.selectByIdsO(getSqlBeanDB(), returnType, returnType, ids);
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public T selectOne(Select select) {
        return mybatisSqlBeanDao.selectOne(getSqlBeanDB(), clazz, select);
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public <R> R selectOne(Class<R> returnType, Select select) {
        if (SqlBeanUtil.isBaseType(returnType.getName()) || SqlBeanUtil.isMap(returnType.getName())) {
            return mybatisSqlBeanDao.selectOneO(getSqlBeanDB(), clazz, returnType, select);
        }
        return mybatisSqlBeanDao.selectOneO(getSqlBeanDB(), returnType, returnType, select);
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public Map<String, Object> selectMap(Select select) {
        return mybatisSqlBeanDao.selectMap(getSqlBeanDB(), clazz, select);
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public T selectOneBy(String where, Object... args) {
        return mybatisSqlBeanDao.selectOneByCondition(getSqlBeanDB(), clazz, where, args);
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public <R> R selectOneBy(Class<R> returnType, String where, Object... args) {
        if (SqlBeanUtil.isBaseType(returnType.getName()) || SqlBeanUtil.isMap(returnType.getName())) {
            return mybatisSqlBeanDao.selectOneByConditionO(getSqlBeanDB(), clazz, returnType, where, args);
        }
        return mybatisSqlBeanDao.selectOneByConditionO(getSqlBeanDB(), returnType, returnType, where, args);
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
        if (SqlBeanUtil.isBaseType(returnType.getName()) || SqlBeanUtil.isMap(returnType.getName())) {
            return mybatisSqlBeanDao.selectOneO(getSqlBeanDB(), clazz, returnType, select);
        }
        return mybatisSqlBeanDao.selectOneO(getSqlBeanDB(), returnType, returnType, select);
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public <R> List<R> selectBy(Class<R> returnType, String where, Object... args) {
        if (SqlBeanUtil.isBaseType(returnType.getName()) || SqlBeanUtil.isMap(returnType.getName())) {
            return mybatisSqlBeanDao.selectByConditionO(getSqlBeanDB(), clazz, returnType, null, where, args);
        }
        return mybatisSqlBeanDao.selectByConditionO(getSqlBeanDB(), returnType, returnType, null, where, args);
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public <R> List<R> selectBy(Class<R> returnType, Wrapper wrapper) {
        Select select = new Select();
        select.where(wrapper);
        if (SqlBeanUtil.isBaseType(returnType.getName()) || SqlBeanUtil.isMap(returnType.getName())) {
            return mybatisSqlBeanDao.selectO(getSqlBeanDB(), clazz, returnType, select);
        }
        return mybatisSqlBeanDao.selectO(getSqlBeanDB(), returnType, returnType, select);
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public <R> List<R> selectBy(Class<R> returnType, Paging paging, String where, Object... args) {
        if (SqlBeanUtil.isBaseType(returnType.getName()) || SqlBeanUtil.isMap(returnType.getName())) {
            return mybatisSqlBeanDao.selectByConditionO(getSqlBeanDB(), clazz, returnType, paging, where, args);
        }
        return mybatisSqlBeanDao.selectByConditionO(getSqlBeanDB(), returnType, returnType, paging, where, args);
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public <R> List<R> selectBy(Class<R> returnType, Paging paging, Wrapper wrapper) {
        Select select = new Select();
        select.where(wrapper);
        select.page(paging.getPagenum(), paging.getPagesize(), paging.getStartByZero());
        select.orderBy(paging.getOrders());
        if (SqlBeanUtil.isBaseType(returnType.getName()) || SqlBeanUtil.isMap(returnType.getName())) {
            return mybatisSqlBeanDao.selectO(getSqlBeanDB(), clazz, returnType, select);
        }
        return mybatisSqlBeanDao.selectO(getSqlBeanDB(), returnType, returnType, select);
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public List<T> selectBy(String where, Object... args) {
        return mybatisSqlBeanDao.selectByCondition(getSqlBeanDB(), clazz, null, where, args);
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
        return mybatisSqlBeanDao.selectByCondition(getSqlBeanDB(), clazz, paging, where, args);
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
        return mybatisSqlBeanDao.selectCountByCondition(getSqlBeanDB(), clazz, where, args);
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public int countBy(Wrapper wrapper) {
        Select select = new Select();
        select.where(wrapper);
        return mybatisSqlBeanDao.count(getSqlBeanDB(), clazz, select);
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public int count() {
        return mybatisSqlBeanDao.selectCountByCondition(getSqlBeanDB(), clazz, null, null);
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
        if (SqlBeanUtil.isBaseType(returnType.getName()) || SqlBeanUtil.isMap(returnType.getName())) {
            return mybatisSqlBeanDao.selectAllO(getSqlBeanDB(), clazz, returnType, null);
        }
        return mybatisSqlBeanDao.selectAllO(getSqlBeanDB(), returnType, returnType, null);
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public <R> List<R> select(Class<R> returnType, Paging paging) {
        if (SqlBeanUtil.isBaseType(returnType.getName()) || SqlBeanUtil.isMap(returnType.getName())) {
            return mybatisSqlBeanDao.selectAllO(getSqlBeanDB(), clazz, returnType, paging);
        }
        return mybatisSqlBeanDao.selectAllO(getSqlBeanDB(), returnType, returnType, paging);
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public List<Map<String, Object>> selectMapList(Select select) {
        return mybatisSqlBeanDao.selectMapList(getSqlBeanDB(), clazz, select);
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public <R> List<R> select(Class<R> returnType, Select select) {
        if (SqlBeanUtil.isBaseType(returnType.getName()) || SqlBeanUtil.isMap(returnType.getName())) {
            return mybatisSqlBeanDao.selectO(getSqlBeanDB(), clazz, returnType, select);
        }
        return mybatisSqlBeanDao.selectO(getSqlBeanDB(), returnType, returnType, select);
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public List<T> select(Select select) {
        return mybatisSqlBeanDao.select(getSqlBeanDB(), clazz, select);
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public int count(Select select) {
        return mybatisSqlBeanDao.count(getSqlBeanDB(), clazz, select);
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public int count(Class<?> returnType, Select select) {
        if (SqlBeanUtil.isBaseType(returnType.getName()) || SqlBeanUtil.isMap(returnType.getName())) {
            return mybatisSqlBeanDao.count(getSqlBeanDB(), clazz, select);
        }
        return mybatisSqlBeanDao.count(getSqlBeanDB(), returnType, select);
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
        return mybatisSqlBeanDao.deleteByCondition(getSqlBeanDB(), clazz, where, args);
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int deleteBy(Wrapper wrapper) {
        Delete delete = new Delete();
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
        return mybatisSqlBeanDao.logicallyDeleteByCondition(getSqlBeanDB(), clazz, where, args);
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
    public int updateById(T bean, ID id, boolean updateNotNull, boolean optimisticLock, String[] filterFields) {
        return mybatisSqlBeanDao.updateById(getSqlBeanDB(), clazz, bean, id, updateNotNull, optimisticLock, filterFields);
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
    public int updateByBeanId(T bean, boolean updateNotNull, boolean optimisticLock, String[] filterFields) {
        return mybatisSqlBeanDao.updateByBeanId(getSqlBeanDB(), clazz, bean, updateNotNull, optimisticLock, filterFields);
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int updateBy(T bean, boolean updateNotNull, boolean optimisticLock, String where, Object... args) {
        return mybatisSqlBeanDao.updateByCondition(getSqlBeanDB(), clazz, bean, updateNotNull, optimisticLock, null, where, args);
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
    public int updateBy(T bean, boolean updateNotNull, boolean optimisticLock, String[] filterFields, String where, Object... args) {
        return mybatisSqlBeanDao.updateByCondition(getSqlBeanDB(), clazz, bean, updateNotNull, optimisticLock, filterFields, where, args);
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int updateBy(T bean, boolean updateNotNull, boolean optimisticLock, String[] filterFields, Wrapper wrapper) {
        Update update = new Update();
        update.setUpdateBean(bean);
        update.setUpdateNotNull(updateNotNull);
        update.setOptimisticLock(optimisticLock);
        update.setFilterFields(filterFields);
        update.where(wrapper);
        return mybatisSqlBeanDao.update(getSqlBeanDB(), clazz, update, false);
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int updateByBean(T bean, String where) {
        return mybatisSqlBeanDao.updateByBeanCondition(getSqlBeanDB(), clazz, bean, true,
                false, null, where);
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int updateByBean(T bean, boolean updateNotNull, boolean optimisticLock, String where) {
        return mybatisSqlBeanDao.updateByBeanCondition(getSqlBeanDB(), clazz, bean, updateNotNull, optimisticLock, null, where);
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int updateByBean(T bean, boolean updateNotNull, boolean optimisticLock, String[] filterFields, String where) {
        return mybatisSqlBeanDao.updateByBeanCondition(getSqlBeanDB(), clazz, bean, updateNotNull, optimisticLock, filterFields, where);
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
    public int insert(List<T> beanList) {
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
        mybatisSqlBeanDao.backup(getSqlBeanDB(), clazz, targetSchema, targetTableName, null, null);
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public void backup(String targetTableName, Column[] columns, Wrapper wrapper) {
        mybatisSqlBeanDao.backup(getSqlBeanDB(), clazz, null, targetTableName, columns, wrapper);
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public void backup(String targetSchema, String targetTableName, Column[] columns, Wrapper wrapper) {
        mybatisSqlBeanDao.backup(getSqlBeanDB(), clazz, targetSchema, targetTableName, columns, wrapper);
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int copy(String targetTableName, Wrapper wrapper) {
        return mybatisSqlBeanDao.copy(getSqlBeanDB(), clazz, null, targetTableName, null, wrapper);
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int copy(String targetSchema, String targetTableName, Wrapper wrapper) {
        return mybatisSqlBeanDao.copy(getSqlBeanDB(), clazz, targetSchema, targetTableName, null, wrapper);
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int copy(String targetTableName, Column[] columns, Wrapper wrapper) {
        return mybatisSqlBeanDao.copy(getSqlBeanDB(), clazz, null, targetTableName, columns, wrapper);
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int copy(String targetSchema, String targetTableName, Column[] columns, Wrapper wrapper) {
        return mybatisSqlBeanDao.copy(getSqlBeanDB(), clazz, targetSchema, targetTableName, columns, wrapper);
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public void dropTable() {
        SqlBeanDB sqlBeanDB = getSqlBeanDB();
        if (sqlBeanDB.getDbType() != DbType.MySQL && sqlBeanDB.getDbType() != DbType.MariaDB && sqlBeanDB.getDbType() != DbType.PostgreSQL && sqlBeanDB.getDbType() != DbType.SQLServer && sqlBeanDB.getDbType() != DbType.H2) {
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
        return mybatisSqlBeanDao.selectColumnInfoList(getSqlBeanDB(), tableName);
    }

}
