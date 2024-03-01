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
import cn.vonce.sql.service.TableService;
import cn.vonce.sql.spring.annotation.DbSwitch;
import cn.vonce.sql.spring.annotation.DbTransactional;
import cn.vonce.sql.spring.config.UseSpringJdbc;
import cn.vonce.sql.spring.enumerate.DbRole;
import cn.vonce.sql.spring.mapper.SpringJbdcSqlBeanMapper;
import cn.vonce.sql.provider.SqlBeanProvider;
import cn.vonce.sql.service.SqlBeanService;
import cn.vonce.sql.uitls.DateUtil;
import cn.vonce.sql.uitls.SqlBeanUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
 * @email imjovi@qq.com
 * @date 2019年5月22日下午16:20:12
 */
@UseSpringJdbc
@Service
public class SpringJdbcSqlBeanServiceImpl<T, ID> extends BaseSqlBeanServiceImpl implements SqlBeanService<T, ID>, TableService<T> {

    private Logger logger = LoggerFactory.getLogger(SpringJdbcSqlBeanServiceImpl.class);

    @Autowired(required = false)
    private SqlBeanConfig sqlBeanConfig;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private boolean initDBInfo;

    private Class<?> clazz;

    public SpringJdbcSqlBeanServiceImpl() {
        clazz = SqlBeanUtil.getGenericType(this.getClass());
    }

    @Override
    SqlBeanConfig getSqlBeanConfig() {
        return sqlBeanConfig;
    }

    @Override
    SqlBeanDB initDBInfo() {
        SqlBeanDB sqlBeanDB = new SqlBeanDB();
        if (!initDBInfo) {
            try {
                Connection connection = jdbcTemplate.getDataSource().getConnection();
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
    Long getAutoIncrId() {
        return singleResult(jdbcTemplate.query(SqlBeanProvider.lastInsertIdSql(), new SpringJbdcSqlBeanMapper<Long>(clazz, Long.class)));
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
        return singleResult(jdbcTemplate.query(SqlBeanProvider.selectByIdSql(getSqlBeanDB(), clazz, null, id), new SpringJbdcSqlBeanMapper<T>(clazz, clazz)));
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public <R> R selectById(Class<R> returnType, ID id) {
        if (id == null) {
            return null;
        }
        return singleResult(jdbcTemplate.query(SqlBeanProvider.selectByIdSql(getSqlBeanDB(), clazz, returnType, id), new SpringJbdcSqlBeanMapper<R>(clazz, returnType)));
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public List<T> selectByIds(ID... ids) {
        if (ids == null || ids.length == 0) {
            throw new SqlBeanException("selectByIds方法ids参数必须拥有一个值");
        }
        return jdbcTemplate.query(SqlBeanProvider.selectByIdsSql(getSqlBeanDB(), clazz, null, ids), new SpringJbdcSqlBeanMapper<T>(clazz, clazz));
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public <R> List<R> selectByIds(Class<R> returnType, ID... ids) {
        if (ids == null || ids.length == 0) {
            throw new SqlBeanException("selectByIds方法ids参数必须拥有一个值");
        }
        return jdbcTemplate.query(SqlBeanProvider.selectByIdsSql(getSqlBeanDB(), clazz, returnType, ids), new SpringJbdcSqlBeanMapper<R>(clazz, returnType));
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public T selectOne(Select select) {
        return singleResult(jdbcTemplate.query(SqlBeanProvider.selectSql(getSqlBeanDB(), clazz, null, select), new SpringJbdcSqlBeanMapper<T>(clazz, clazz)));
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public <R> R selectOne(Class<R> returnType, Select select) {
        return singleResult(jdbcTemplate.query(SqlBeanProvider.selectSql(getSqlBeanDB(), clazz, returnType, select), new SpringJbdcSqlBeanMapper<R>(clazz, returnType)));
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public Map<String, Object> selectMap(Select select) {
        return singleResult(jdbcTemplate.query(SqlBeanProvider.selectSql(getSqlBeanDB(), clazz, null, select), new SpringJbdcSqlBeanMapper<Map<String, Object>>(clazz, Map.class)));
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public T selectOneBy(String where, Object... args) {
        return singleResult(jdbcTemplate.query(SqlBeanProvider.selectBySql(getSqlBeanDB(), clazz, null, null, where, args), new SpringJbdcSqlBeanMapper<T>(clazz, clazz)));
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public <R> R selectOneBy(Class<R> returnType, String where, Object... args) {
        return singleResult(jdbcTemplate.query(SqlBeanProvider.selectBySql(getSqlBeanDB(), clazz, returnType, null, where, args), new SpringJbdcSqlBeanMapper<>(clazz, returnType)));
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public T selectOneBy(Wrapper wrapper) {
        Select select = new Select();
        select.where(wrapper);
        return singleResult(jdbcTemplate.query(SqlBeanProvider.selectSql(getSqlBeanDB(), clazz, null, select), new SpringJbdcSqlBeanMapper<>(clazz, clazz)));
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public <R> R selectOneBy(Class<R> returnType, Wrapper wrapper) {
        Select select = new Select();
        select.where(wrapper);
        return singleResult(jdbcTemplate.query(SqlBeanProvider.selectSql(getSqlBeanDB(), clazz, returnType, select), new SpringJbdcSqlBeanMapper<>(clazz, returnType)));
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public <R> List<R> selectBy(Class<R> returnType, String where, Object... args) {
        return jdbcTemplate.query(SqlBeanProvider.selectBySql(getSqlBeanDB(), clazz, returnType, null, where, args), new SpringJbdcSqlBeanMapper<>(clazz, returnType));
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public <R> List<R> selectBy(Class<R> returnType, Wrapper wrapper) {
        Select select = new Select();
        select.where(wrapper);
        return jdbcTemplate.query(SqlBeanProvider.selectSql(getSqlBeanDB(), clazz, returnType, select), new SpringJbdcSqlBeanMapper<>(clazz, returnType));
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public <R> List<R> selectBy(Class<R> returnType, Paging paging, String where, Object... args) {
        return jdbcTemplate.query(SqlBeanProvider.selectBySql(getSqlBeanDB(), clazz, returnType, paging, where, args), new SpringJbdcSqlBeanMapper<>(clazz, returnType));
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public <R> List<R> selectBy(Class<R> returnType, Paging paging, Wrapper wrapper) {
        Select select = new Select();
        select.where(wrapper);
        select.page(paging.getPagenum(), paging.getPagesize(), paging.getStartByZero());
        select.orderBy(paging.getOrders());
        return jdbcTemplate.query(SqlBeanProvider.selectSql(getSqlBeanDB(), clazz, returnType, select), new SpringJbdcSqlBeanMapper<>(clazz, returnType));
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public List<T> selectBy(String where, Object... args) {
        return jdbcTemplate.query(SqlBeanProvider.selectBySql(getSqlBeanDB(), clazz, null, null, where, args), new SpringJbdcSqlBeanMapper<>(clazz, clazz));
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public List<T> selectBy(Wrapper wrapper) {
        Select select = new Select();
        select.where(wrapper);
        return jdbcTemplate.query(SqlBeanProvider.selectSql(getSqlBeanDB(), clazz, null, select), new SpringJbdcSqlBeanMapper<>(clazz, clazz));
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public List<T> selectBy(Paging paging, String where, Object... args) {
        return jdbcTemplate.query(SqlBeanProvider.selectBySql(getSqlBeanDB(), clazz, null, paging, where, args), new SpringJbdcSqlBeanMapper<>(clazz, clazz));
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public List<T> selectBy(Paging paging, Wrapper wrapper) {
        Select select = new Select();
        select.where(wrapper);
        select.page(paging.getPagenum(), paging.getPagesize(), paging.getStartByZero());
        select.orderBy(paging.getOrders());
        return jdbcTemplate.query(SqlBeanProvider.selectSql(getSqlBeanDB(), clazz, null, select), new SpringJbdcSqlBeanMapper<>(clazz, clazz));
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public int countBy(String where, Object... args) {
        return jdbcTemplate.queryForObject(SqlBeanProvider.countBySql(getSqlBeanDB(), clazz, where, args), new SpringJbdcSqlBeanMapper<>(clazz, Integer.class));
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public int countBy(Wrapper wrapper) {
        Select select = new Select();
        select.where(wrapper);
        return jdbcTemplate.queryForObject(SqlBeanProvider.countSql(getSqlBeanDB(), clazz, null, select), new SpringJbdcSqlBeanMapper<>(clazz, Integer.class));
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public int count() {
        return jdbcTemplate.queryForObject(SqlBeanProvider.countBySql(getSqlBeanDB(), clazz, null, null), new SpringJbdcSqlBeanMapper<>(clazz, Integer.class));
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public List<T> select() {
        return jdbcTemplate.query(SqlBeanProvider.selectAllSql(getSqlBeanDB(), clazz, null, null), new SpringJbdcSqlBeanMapper<T>(clazz, clazz));
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public List<T> select(Paging paging) {
        return jdbcTemplate.query(SqlBeanProvider.selectAllSql(getSqlBeanDB(), clazz, null, paging), new SpringJbdcSqlBeanMapper<T>(clazz, clazz));
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public <R> List<R> select(Class<R> returnType) {
        return jdbcTemplate.query(SqlBeanProvider.selectAllSql(getSqlBeanDB(), clazz, returnType, null), new SpringJbdcSqlBeanMapper<R>(clazz, returnType));
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public <R> List<R> select(Class<R> returnType, Paging paging) {
        return jdbcTemplate.query(SqlBeanProvider.selectAllSql(getSqlBeanDB(), clazz, returnType, paging), new SpringJbdcSqlBeanMapper<R>(clazz, returnType));
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public List<Map<String, Object>> selectMapList(Select select) {
        return jdbcTemplate.query(SqlBeanProvider.selectSql(getSqlBeanDB(), clazz, null, select), new SpringJbdcSqlBeanMapper<Map<String, Object>>(clazz, Map.class));
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public <R> List<R> select(Class<R> returnType, Select select) {
        return jdbcTemplate.query(SqlBeanProvider.selectSql(getSqlBeanDB(), clazz, returnType, select), new SpringJbdcSqlBeanMapper<R>(clazz, returnType));
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public List<T> select(Select select) {
        return jdbcTemplate.query(SqlBeanProvider.selectSql(getSqlBeanDB(), clazz, null, select), new SpringJbdcSqlBeanMapper<T>(clazz, clazz));
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public int count(Select select) {
        return jdbcTemplate.queryForObject(SqlBeanProvider.countSql(getSqlBeanDB(), clazz, null, select), new SpringJbdcSqlBeanMapper<Integer>(clazz, Integer.class));
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public int count(Class<?> returnType, Select select) {
        return jdbcTemplate.queryForObject(SqlBeanProvider.countSql(getSqlBeanDB(), clazz, returnType, select), Integer.class);
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
    public <R> ResultData<R> paging(Class<R> returnType, Select select, PageHelper<R> pageHelper) {
        pageHelper.paging(returnType, select, this);
        return pageHelper.getResultData();
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public <R> ResultData<R> paging(Class<R> returnType, Select select, int pagenum, int pagesize) {
        PageHelper<R> pageHelper = new PageHelper<>(pagenum, pagesize);
        pageHelper.paging(returnType, select, this);
        return pageHelper.getResultData();
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int deleteById(ID... id) {
        if (id == null || id.length == 0) {
            throw new SqlBeanException("deleteById方法id参数必须拥有一个值");
        }
        return jdbcTemplate.update(SqlBeanProvider.deleteByIdSql(getSqlBeanDB(), clazz, id));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int deleteBy(String where, Object... args) {
        return jdbcTemplate.update(SqlBeanProvider.deleteBySql(getSqlBeanDB(), clazz, where, args));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int deleteBy(Wrapper wrapper) {
        Delete delete = new Delete();
        delete.setLogicallyDelete(SqlBeanUtil.checkLogically(clazz));
        delete.where(wrapper);
        return jdbcTemplate.update(SqlBeanProvider.deleteSql(getSqlBeanDB(), clazz, delete, false));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int delete(Delete delete) {
        return jdbcTemplate.update(SqlBeanProvider.deleteSql(getSqlBeanDB(), clazz, delete, false));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int delete(Delete delete, boolean ignore) {
        return jdbcTemplate.update(SqlBeanProvider.deleteSql(getSqlBeanDB(), clazz, delete, ignore));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int logicallyDeleteById(ID... id) {
        if (id == null || id.length == 0) {
            throw new SqlBeanException("logicallyDeleteById方法id参数必须拥有一个值");
        }
        return jdbcTemplate.update(SqlBeanProvider.logicallyDeleteByIdSql(getSqlBeanDB(), clazz, id));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int logicallyDeleteBy(String where, Object... args) {
        return jdbcTemplate.update(SqlBeanProvider.logicallyDeleteBySql(getSqlBeanDB(), clazz, where, args));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int logicallyDeleteBy(Wrapper wrapper) {
        return jdbcTemplate.update(SqlBeanProvider.logicallyDeleteBySql(getSqlBeanDB(), clazz, wrapper));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int update(Update update) {
        return jdbcTemplate.update(SqlBeanProvider.updateSql(getSqlBeanDB(), clazz, update, false));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int update(Update update, boolean ignore) {
        return jdbcTemplate.update(SqlBeanProvider.updateSql(getSqlBeanDB(), clazz, update, ignore));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int updateById(T bean, ID id) {
        return jdbcTemplate.update(SqlBeanProvider.updateByIdSql(getSqlBeanDB(), clazz, bean, id, true, false, null));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int updateById(T bean, ID id, boolean updateNotNull, boolean optimisticLock) {
        return jdbcTemplate.update(SqlBeanProvider.updateByIdSql(getSqlBeanDB(), clazz, bean, id, updateNotNull, optimisticLock, null));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int updateByBeanId(T bean) {
        return jdbcTemplate.update(SqlBeanProvider.updateByBeanIdSql(getSqlBeanDB(), clazz, bean, true, false, null));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int updateById(T bean, ID id, boolean updateNotNull, boolean optimisticLock, Column... filterColumns) {
        return jdbcTemplate.update(SqlBeanProvider.updateByIdSql(getSqlBeanDB(), clazz, bean, id, updateNotNull, optimisticLock, filterColumns));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public <R> int updateById(T bean, ID id, boolean updateNotNull, boolean optimisticLock, ColumnFun<T, R>... filterColumns) {
        return jdbcTemplate.update(SqlBeanProvider.updateByIdSql(getSqlBeanDB(), clazz, bean, id, updateNotNull, optimisticLock, SqlBeanUtil.funToColumn(filterColumns)));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int updateBy(T bean, String where, Object... args) {
        return jdbcTemplate.update(SqlBeanProvider.updateBySql(getSqlBeanDB(), clazz, bean, true, false, null, where, args));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int updateByBeanId(T bean, boolean updateNotNull, boolean optimisticLock) {
        return jdbcTemplate.update(SqlBeanProvider.updateByBeanIdSql(getSqlBeanDB(), clazz, bean, updateNotNull, optimisticLock, null));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int updateByBeanId(T bean, boolean updateNotNull, boolean optimisticLock, Column... filterColumns) {
        return jdbcTemplate.update(SqlBeanProvider.updateByBeanIdSql(getSqlBeanDB(), clazz, bean, updateNotNull, optimisticLock, filterColumns));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public <R> int updateByBeanId(T bean, boolean updateNotNull, boolean optimisticLock, ColumnFun<T, R>... filterColumns) {
        return jdbcTemplate.update(SqlBeanProvider.updateByBeanIdSql(getSqlBeanDB(), clazz, bean, updateNotNull, optimisticLock, SqlBeanUtil.funToColumn(filterColumns)));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int updateBy(T bean, boolean updateNotNull, boolean optimisticLock, String where, Object... args) {
        return jdbcTemplate.update(SqlBeanProvider.updateBySql(getSqlBeanDB(), clazz, bean, updateNotNull, optimisticLock, null, where, args));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int updateBy(T bean, Wrapper wrapper) {
        Update update = new Update();
        update.setUpdateBean(bean);
        update.setUpdateNotNull(true);
        update.setOptimisticLock(false);
        update.where(wrapper);
        return jdbcTemplate.update(SqlBeanProvider.updateSql(getSqlBeanDB(), clazz, update, false));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int updateBy(T bean, boolean updateNotNull, boolean optimisticLock, Wrapper wrapper) {
        Update update = new Update();
        update.setUpdateBean(bean);
        update.setUpdateNotNull(updateNotNull);
        update.setOptimisticLock(optimisticLock);
        update.where(wrapper);
        return jdbcTemplate.update(SqlBeanProvider.updateSql(getSqlBeanDB(), clazz, update, false));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int updateBy(T bean, boolean updateNotNull, boolean optimisticLock, Column[] filterColumns, String where, Object... args) {
        return jdbcTemplate.update(SqlBeanProvider.updateBySql(getSqlBeanDB(), clazz, bean, updateNotNull, optimisticLock, filterColumns, where, args));
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
        return jdbcTemplate.update(SqlBeanProvider.updateSql(getSqlBeanDB(), clazz, update, false));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public <R> int updateBy(T bean, boolean updateNotNull, boolean optimisticLock, Wrapper wrapper, ColumnFun<T, R>... filterColumns) {
        return this.updateBy(bean, updateNotNull, optimisticLock, wrapper, SqlBeanUtil.funToColumn(filterColumns));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int updateByBean(T bean, String where) {
        return jdbcTemplate.update(SqlBeanProvider.updateByBeanSql(getSqlBeanDB(), clazz, bean, true, false, where, null));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int updateByBean(T bean, boolean updateNotNull, boolean optimisticLock, String where) {
        return jdbcTemplate.update(SqlBeanProvider.updateByBeanSql(getSqlBeanDB(), clazz, bean, updateNotNull, optimisticLock, where, null));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int updateByBean(T bean, boolean updateNotNull, boolean optimisticLock, String where, Column... filterColumns) {
        return jdbcTemplate.update(SqlBeanProvider.updateByBeanSql(getSqlBeanDB(), clazz, bean, updateNotNull, optimisticLock, where, filterColumns));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public <R> int updateByBean(T bean, boolean updateNotNull, boolean optimisticLock, String where, ColumnFun<T, R>... filterColumns) {
        return jdbcTemplate.update(SqlBeanProvider.updateByBeanSql(getSqlBeanDB(), clazz, bean, updateNotNull, optimisticLock, where, SqlBeanUtil.funToColumn(filterColumns)));
    }

    @Transactional
    @DbTransactional
    @DbSwitch(DbRole.MASTER)
    @Override
    public int insert(T... bean) {
        if (bean == null || bean.length == 0) {
            throw new SqlBeanException("insert方法bean参数必须拥有一个值");
        }
        int count = jdbcTemplate.update(SqlBeanProvider.insertBeanSql(getSqlBeanDB(), clazz, bean));
        super.setAutoIncrId(clazz, Arrays.asList(bean));
        return count;
    }

    @Transactional
    @DbTransactional
    @DbSwitch(DbRole.MASTER)
    @Override
    public int insert(Collection<T> beanList) {
        if (beanList == null || beanList.size() == 0) {
            throw new SqlBeanException("insert方法beanList参数至少拥有一个值");
        }
        int count = jdbcTemplate.update(SqlBeanProvider.insertBeanSql(getSqlBeanDB(), clazz, beanList));
        super.setAutoIncrId(clazz, beanList);
        return count;
    }

    @Transactional
    @DbTransactional
    @DbSwitch(DbRole.MASTER)
    @Override
    public int insert(Insert insert) {
        int count = jdbcTemplate.update(SqlBeanProvider.insertBeanSql(getSqlBeanDB(), clazz, insert));
        super.setAutoIncrId(clazz, insert.getInsertBean());
        return count;
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public String backup() {
        String targetTableName = SqlBeanUtil.getTable(clazz).getName() + "_" + DateUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS");
        jdbcTemplate.update(SqlBeanProvider.backupSql(getSqlBeanDB(), clazz, null, null, targetTableName, null));
        return targetTableName;
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public void backup(String targetTableName) {
        jdbcTemplate.update(SqlBeanProvider.backupSql(getSqlBeanDB(), clazz, null, null, targetTableName, null));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public void backup(String targetSchema, String targetTableName) {
        jdbcTemplate.update(SqlBeanProvider.backupSql(getSqlBeanDB(), clazz, null, targetSchema, targetTableName, null));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public void backup(Wrapper wrapper, String targetSchema, String targetTableName) {
        jdbcTemplate.update(SqlBeanProvider.backupSql(getSqlBeanDB(), clazz, wrapper, targetSchema, targetTableName, null));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public void backup(Wrapper wrapper, String targetTableName, Column... columns) {
        jdbcTemplate.update(SqlBeanProvider.backupSql(getSqlBeanDB(), clazz, wrapper, null, targetTableName, columns));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public <R> void backup(Wrapper wrapper, String targetTableName, ColumnFun<T, R>... columns) {
        jdbcTemplate.update(SqlBeanProvider.backupSql(getSqlBeanDB(), clazz, wrapper, null, targetTableName, SqlBeanUtil.funToColumn(columns)));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public void backup(Wrapper wrapper, String targetSchema, String targetTableName, Column... columns) {
        jdbcTemplate.update(SqlBeanProvider.backupSql(getSqlBeanDB(), clazz, wrapper, targetSchema, targetTableName, columns));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public <R> void backup(Wrapper wrapper, String targetSchema, String targetTableName, ColumnFun<T, R>... columns) {
        jdbcTemplate.update(SqlBeanProvider.backupSql(getSqlBeanDB(), clazz, wrapper, targetSchema, targetTableName, SqlBeanUtil.funToColumn(columns)));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int copy(Wrapper wrapper, String targetTableName) {
        return jdbcTemplate.update(SqlBeanProvider.copySql(getSqlBeanDB(), clazz, wrapper, null, targetTableName, null));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int copy(Wrapper wrapper, String targetSchema, String targetTableName) {
        return jdbcTemplate.update(SqlBeanProvider.copySql(getSqlBeanDB(), clazz, wrapper, targetSchema, targetTableName, null));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int copy(Wrapper wrapper, String targetTableName, Column... columns) {
        return jdbcTemplate.update(SqlBeanProvider.copySql(getSqlBeanDB(), clazz, wrapper, null, targetTableName, columns));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public <R> int copy(Wrapper wrapper, String targetTableName, ColumnFun<T, R>... columns) {
        return jdbcTemplate.update(SqlBeanProvider.copySql(getSqlBeanDB(), clazz, wrapper, null, targetTableName, SqlBeanUtil.funToColumn(columns)));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int copy(Wrapper wrapper, String targetSchema, String targetTableName, Column... columns) {
        return jdbcTemplate.update(SqlBeanProvider.copySql(getSqlBeanDB(), clazz, wrapper, targetSchema, targetTableName, columns));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public <R> int copy(Wrapper wrapper, String targetSchema, String targetTableName, ColumnFun<T, R>... columns) {
        return jdbcTemplate.update(SqlBeanProvider.copySql(getSqlBeanDB(), clazz, wrapper, targetSchema, targetTableName, SqlBeanUtil.funToColumn(columns)));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int alter(Table table, List<ColumnInfo> columnInfoList) {
        List<String> sqlList = SqlBeanProvider.buildAlterSql(getSqlBeanDB(), clazz, columnInfoList);
        int count = 0;
        if (sqlList != null && sqlList.size() > 0) {
            for (String sql : sqlList) {
                count += jdbcTemplate.update(sql);
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
                count += jdbcTemplate.update(sql);
            }
        }
        return count;
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int alterRemarks(String remarks) {
        if (getSqlBeanDB().getDbType() == DbType.SQLite || getSqlBeanDB().getDbType() == DbType.Derby) {
            return 0;
        }
        String sql = SqlBeanProvider.alterRemarksSql(getSqlBeanDB(), clazz, remarks);
        return jdbcTemplate.update(sql);
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public void dropTable() {
        SqlBeanDB sqlBeanDB = getSqlBeanDB();
        if (sqlBeanDB.getDbType() != DbType.MySQL && sqlBeanDB.getDbType() != DbType.MariaDB && sqlBeanDB.getDbType() != DbType.Postgresql && sqlBeanDB.getDbType() != DbType.SQLServer && sqlBeanDB.getDbType() != DbType.H2) {
            List<TableInfo> nameList = jdbcTemplate.queryForList(SqlBeanProvider.selectTableListSql(sqlBeanDB, SqlBeanUtil.getTable(clazz).getSchema(), SqlBeanUtil.getTable(clazz).getName()), TableInfo.class);
            if (nameList == null || nameList.isEmpty()) {
                return;
            }
        }
        jdbcTemplate.update(SqlBeanProvider.dropTableSql(sqlBeanDB, clazz));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public void createTable() {
        jdbcTemplate.update(SqlBeanProvider.createTableSql(getSqlBeanDB(), clazz));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public void dropAndCreateTable() {
        dropTable();
        createTable();
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public List<TableInfo> getTableList() {
        return this.getTableList(SqlBeanUtil.getTable(clazz).getSchema(), null);
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public List<TableInfo> getTableList(String tableName) {
        return this.getTableList(SqlBeanUtil.getTable(clazz).getSchema(), tableName);
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public List<TableInfo> getTableList(String schema, String tableName) {
        return jdbcTemplate.query(SqlBeanProvider.selectTableListSql(getSqlBeanDB(), schema, tableName), new SpringJbdcSqlBeanMapper<TableInfo>(TableInfo.class, TableInfo.class));
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public List<ColumnInfo> getColumnInfoList() {
        Table table = SqlBeanUtil.getTable(clazz);
        return this.getColumnInfoList(table.getSchema(), table.getName());
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public List<ColumnInfo> getColumnInfoList(String tableName) {
        return this.getColumnInfoList(SqlBeanUtil.getTable(clazz).getSchema(), tableName);
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public List<ColumnInfo> getColumnInfoList(String schema, String tableName) {
        List<ColumnInfo> columnInfoList = jdbcTemplate.query(SqlBeanProvider.selectColumnListSql(getSqlBeanDB(), schema, tableName), new SpringJbdcSqlBeanMapper<ColumnInfo>(ColumnInfo.class, ColumnInfo.class));
        super.handleColumnInfo(columnInfoList);
        return columnInfoList;
    }

    @Override
    public TableService<T> operation() {
        return this;
    }

    private static <T> T singleResult(Collection<T> results) {
        int size = results != null ? results.size() : 0;
        if (size == 0) {
            return null;
        } else if (results.size() > 1) {
            throw new SqlBeanException("执行返回的结果不止一条, size: " + results.size());
        } else {
            return results.iterator().next();
        }
    }

}
