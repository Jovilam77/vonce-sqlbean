package cn.vonce.sql.spring.service;

import cn.vonce.sql.bean.*;
import cn.vonce.sql.config.SqlBeanConfig;
import cn.vonce.sql.config.SqlBeanDB;
import cn.vonce.sql.enumerate.DbType;
import cn.vonce.sql.exception.SqlBeanException;
import cn.vonce.sql.helper.Wrapper;
import cn.vonce.sql.service.TableService;
import cn.vonce.sql.spring.annotation.DbSwitch;
import cn.vonce.sql.spring.config.UseSpringJdbc;
import cn.vonce.sql.spring.enumerate.DbRole;
import cn.vonce.sql.spring.mapper.SpringJbdcSqlBeanMapper;
import cn.vonce.sql.provider.SqlBeanProvider;
import cn.vonce.sql.service.SqlBeanService;
import cn.vonce.sql.uitls.DateUtil;
import cn.vonce.sql.uitls.SqlBeanUtil;
import cn.vonce.sql.uitls.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 通用的业务实现
 *
 * @param <T>
 * @author Jovi
 * @version 1.0
 * @email 766255988@qq.com
 * @date 2019年5月22日下午16:20:12
 */
@UseSpringJdbc
@Service
public class SpringJdbcSqlBeanServiceImpl<T, ID> extends BaseSqlBeanServiceImpl implements SqlBeanService<T, ID>, TableService {

    private Logger logger = LoggerFactory.getLogger(SpringJdbcSqlBeanServiceImpl.class);

    @Autowired(required = false)
    private SqlBeanConfig sqlBeanConfig;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private String productName;

    private Class<?> clazz;

    public SpringJdbcSqlBeanServiceImpl() {
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
                    logger.error(e.getMessage());
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
                productName = jdbcTemplate.getDataSource().getConnection().getMetaData().getDatabaseProductName();
            } catch (SQLException e) {
                logger.error(e.getMessage());
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
        return singleResult(jdbcTemplate.query(SqlBeanProvider.selectByIdSql(getSqlBeanDB(), clazz, id),
                new SpringJbdcSqlBeanMapper<T>(clazz, clazz)));
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public <O> O selectById(Class<O> returnType, ID id) {
        if (id == null) {
            return null;
        }
        if (!SqlBeanUtil.isBaseType(returnType.getName()) && !SqlBeanUtil.isMap(returnType.getName())) {
            clazz = returnType;
        }
        return singleResult(jdbcTemplate.query(SqlBeanProvider.selectByIdSql(getSqlBeanDB(), clazz, id),
                new SpringJbdcSqlBeanMapper<O>(clazz, returnType)));
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public List<T> selectByIds(ID... ids) {
        if (ids == null || ids.length == 0) {
            return null;
        }
        return jdbcTemplate.query(SqlBeanProvider.selectByIdsSql(getSqlBeanDB(), clazz, ids),
                new SpringJbdcSqlBeanMapper<T>(clazz, clazz));
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public <O> List<O> selectByIds(Class<O> returnType, ID... ids) {
        if (ids == null || ids.length == 0) {
            return null;
        }
        if (!SqlBeanUtil.isBaseType(returnType.getName()) && !SqlBeanUtil.isMap(returnType.getName())) {
            clazz = returnType;
        }
        return jdbcTemplate.query(SqlBeanProvider.selectByIdsSql(getSqlBeanDB(), clazz, ids),
                new SpringJbdcSqlBeanMapper<O>(clazz, returnType));
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public T selectOne(Select select) {
        return singleResult(jdbcTemplate.query(SqlBeanProvider.selectSql(getSqlBeanDB(), clazz, select),
                new SpringJbdcSqlBeanMapper<T>(clazz, clazz)));
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public <O> O selectOne(Class<O> returnType, Select select) {
        if (!SqlBeanUtil.isBaseType(returnType.getName()) && !SqlBeanUtil.isMap(returnType.getName())) {
            clazz = returnType;
        }
        return singleResult(jdbcTemplate.query(SqlBeanProvider.selectSql(getSqlBeanDB(), clazz, select),
                new SpringJbdcSqlBeanMapper<O>(clazz, returnType)));
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public Map<String, Object> selectMap(Select select) {
        return singleResult(jdbcTemplate.query(SqlBeanProvider.selectSql(getSqlBeanDB(), clazz, select),
                new SpringJbdcSqlBeanMapper<Map<String, Object>>(clazz, Map.class)));
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public T selectOneByCondition(String where, Object... args) {
        return singleResult(jdbcTemplate.query(SqlBeanProvider.selectByConditionSql(getSqlBeanDB(), clazz, null, where, args),
                new SpringJbdcSqlBeanMapper<T>(clazz, clazz)));
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public <O> O selectOneByCondition(Class<O> returnType, String where, Object... args) {
        if (!SqlBeanUtil.isBaseType(returnType.getName()) && !SqlBeanUtil.isMap(returnType.getName())) {
            clazz = returnType;
        }
        return singleResult(jdbcTemplate.query(SqlBeanProvider.selectByConditionSql(getSqlBeanDB(), clazz, null, where, args),
                new SpringJbdcSqlBeanMapper<O>(clazz, returnType)));
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public T selectOneByCondition(Wrapper where) {
        Select select = new Select();
        select.setWhere(where);
        return singleResult(jdbcTemplate.query(SqlBeanProvider.selectSql(getSqlBeanDB(), clazz, select),
                new SpringJbdcSqlBeanMapper<T>(clazz, clazz)));
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public <O> O selectOneByCondition(Class<O> returnType, Wrapper where) {
        Select select = new Select();
        select.setWhere(where);
        if (!SqlBeanUtil.isBaseType(returnType.getName()) && !SqlBeanUtil.isMap(returnType.getName())) {
            clazz = returnType;
        }
        return singleResult(jdbcTemplate.query(SqlBeanProvider.selectSql(getSqlBeanDB(), clazz, select),
                new SpringJbdcSqlBeanMapper<O>(clazz, returnType)));
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public <O> List<O> selectByCondition(Class<O> returnType, String where, Object... args) {
        if (!SqlBeanUtil.isBaseType(returnType.getName()) && !SqlBeanUtil.isMap(returnType.getName())) {
            clazz = returnType;
        }
        return jdbcTemplate.query(SqlBeanProvider.selectByConditionSql(getSqlBeanDB(), clazz, null, where, args),
                new SpringJbdcSqlBeanMapper<O>(clazz, returnType));
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public <O> List<O> selectByCondition(Class<O> returnType, Wrapper where) {
        Select select = new Select();
        select.setWhere(where);
        if (!SqlBeanUtil.isBaseType(returnType.getName()) && !SqlBeanUtil.isMap(returnType.getName())) {
            clazz = returnType;
        }
        return jdbcTemplate.query(SqlBeanProvider.selectSql(getSqlBeanDB(), clazz, select),
                new SpringJbdcSqlBeanMapper<O>(clazz, returnType));
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public <O> List<O> selectByCondition(Class<O> returnType, Paging paging, String where, Object... args) {
        if (!SqlBeanUtil.isBaseType(returnType.getName()) && !SqlBeanUtil.isMap(returnType.getName())) {
            clazz = returnType;
        }
        return jdbcTemplate.query(SqlBeanProvider.selectByConditionSql(getSqlBeanDB(), clazz, paging, where, args),
                new SpringJbdcSqlBeanMapper<O>(clazz, returnType));
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public <O> List<O> selectByCondition(Class<O> returnType, Paging paging, Wrapper where) {
        Select select = new Select();
        select.setWhere(where);
        select.setPage(paging.getPagenum(), paging.getPagesize());
        select.orderBy(paging.getOrders());
        if (!SqlBeanUtil.isBaseType(returnType.getName()) && !SqlBeanUtil.isMap(returnType.getName())) {
            clazz = returnType;
        }
        return jdbcTemplate.query(SqlBeanProvider.selectSql(getSqlBeanDB(), clazz, select),
                new SpringJbdcSqlBeanMapper<O>(clazz, returnType));
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public List<T> selectByCondition(String where, Object... args) {
        return jdbcTemplate.query(SqlBeanProvider.selectByConditionSql(getSqlBeanDB(), clazz, null, where, args),
                new SpringJbdcSqlBeanMapper<T>(clazz, clazz));
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public List<T> selectByCondition(Wrapper where) {
        Select select = new Select();
        select.setWhere(where);
        return jdbcTemplate.query(SqlBeanProvider.selectSql(getSqlBeanDB(), clazz, select),
                new SpringJbdcSqlBeanMapper<T>(clazz, clazz));
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public List<T> selectByCondition(Paging paging, String where, Object... args) {
        return jdbcTemplate.query(SqlBeanProvider.selectByConditionSql(getSqlBeanDB(), clazz, paging, where, args),
                new SpringJbdcSqlBeanMapper<T>(clazz, clazz));
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public List<T> selectByCondition(Paging paging, Wrapper where) {
        Select select = new Select();
        select.setWhere(where);
        select.setPage(paging.getPagenum(), paging.getPagesize());
        select.orderBy(paging.getOrders());
        return jdbcTemplate.query(SqlBeanProvider.selectSql(getSqlBeanDB(), clazz, select),
                new SpringJbdcSqlBeanMapper<T>(clazz, clazz));
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public int selectCountByCondition(String where, Object... args) {
        return jdbcTemplate.queryForObject(SqlBeanProvider.selectCountByConditionSql(getSqlBeanDB(), clazz, where, args), new SpringJbdcSqlBeanMapper<Integer>(clazz, Integer.class));
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public int selectCountByCondition(Wrapper where) {
        Select select = new Select();
        select.setWhere(where);
        return jdbcTemplate.queryForObject(SqlBeanProvider.countSql(getSqlBeanDB(), clazz, select), new SpringJbdcSqlBeanMapper<Integer>(clazz, Integer.class));
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public int countAll() {
        return jdbcTemplate.queryForObject(SqlBeanProvider.selectCountByConditionSql(getSqlBeanDB(), clazz, null, null), new SpringJbdcSqlBeanMapper<Integer>(clazz, Integer.class));
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public List<T> selectAll() {
        return jdbcTemplate.query(SqlBeanProvider.selectAllSql(getSqlBeanDB(), clazz, null),
                new SpringJbdcSqlBeanMapper<T>(clazz, clazz));
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public List<T> selectAll(Paging paging) {
        return jdbcTemplate.query(SqlBeanProvider.selectAllSql(getSqlBeanDB(), clazz, paging),
                new SpringJbdcSqlBeanMapper<T>(clazz, clazz));
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public <O> List<O> selectAll(Class<O> returnType) {
        if (!SqlBeanUtil.isBaseType(returnType.getName()) && !SqlBeanUtil.isMap(returnType.getName())) {
            clazz = returnType;
        }
        return jdbcTemplate.query(SqlBeanProvider.selectAllSql(getSqlBeanDB(), clazz, null),
                new SpringJbdcSqlBeanMapper<O>(clazz, returnType));
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public <O> List<O> selectAll(Class<O> returnType, Paging paging) {
        if (!SqlBeanUtil.isBaseType(returnType.getName()) && !SqlBeanUtil.isMap(returnType.getName())) {
            clazz = returnType;
        }
        return jdbcTemplate.query(SqlBeanProvider.selectAllSql(getSqlBeanDB(), clazz, paging),
                new SpringJbdcSqlBeanMapper<O>(clazz, returnType));
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public List<Map<String, Object>> selectMapList(Select select) {
        return jdbcTemplate.query(SqlBeanProvider.selectSql(getSqlBeanDB(), clazz, select),
                new SpringJbdcSqlBeanMapper<Map<String, Object>>(clazz, Map.class));
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public <O> List<O> select(Class<O> returnType, Select select) {
        if (!SqlBeanUtil.isBaseType(returnType.getName()) && !SqlBeanUtil.isMap(returnType.getName())) {
            clazz = returnType;
        }
        return jdbcTemplate.query(SqlBeanProvider.selectSql(getSqlBeanDB(), clazz, select),
                new SpringJbdcSqlBeanMapper<O>(clazz, returnType));
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public List<T> select(Select select) {
        return jdbcTemplate.query(SqlBeanProvider.selectSql(getSqlBeanDB(), clazz, select),
                new SpringJbdcSqlBeanMapper<T>(clazz, clazz));
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public int count(Select select) {
        return jdbcTemplate.queryForObject(SqlBeanProvider.countSql(getSqlBeanDB(), clazz, select), new SpringJbdcSqlBeanMapper<Integer>(clazz, Integer.class));
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public int count(Class<?> clazz, Select select) {
        return jdbcTemplate.queryForObject(SqlBeanProvider.countSql(getSqlBeanDB(), clazz, select), Integer.class);
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int deleteById(ID... id) {
        return jdbcTemplate.update(SqlBeanProvider.deleteByIdSql(getSqlBeanDB(), clazz, id));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int deleteByCondition(String where, Object... args) {
        return jdbcTemplate.update(SqlBeanProvider.deleteByConditionSql(getSqlBeanDB(), clazz, where, args));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int deleteByCondition(Wrapper where) {
        Delete delete = new Delete();
        delete.setWhere(where);
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
        return jdbcTemplate.update(SqlBeanProvider.logicallyDeleteByIdSql(getSqlBeanDB(), clazz, id));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int logicallyDeleteByCondition(String where, Object... args) {
        return jdbcTemplate.update(SqlBeanProvider.logicallyDeleteByConditionSql(getSqlBeanDB(), clazz, where, args));
    }

    @Override
    public int logicallyDeleteByCondition(Wrapper where) {
        return jdbcTemplate.update(SqlBeanProvider.logicallyDeleteByConditionSql(getSqlBeanDB(), clazz, where));
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
    public int updateById(T bean, ID id, boolean updateNotNull, boolean optimisticLock) {
        return jdbcTemplate.update(SqlBeanProvider.updateByIdSql(getSqlBeanDB(), clazz, bean, id, updateNotNull, optimisticLock, null));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int updateById(T bean, ID id, boolean updateNotNull, boolean optimisticLock, String[] filterFields) {
        return jdbcTemplate.update(SqlBeanProvider.updateByIdSql(getSqlBeanDB(), clazz, bean, id, updateNotNull, optimisticLock, filterFields));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int updateByBeanId(T bean, boolean updateNotNull, boolean optimisticLock) {
        return jdbcTemplate.update(SqlBeanProvider.updateByBeanIdSql(getSqlBeanDB(), clazz, bean, updateNotNull, optimisticLock, null));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int updateByBeanId(T bean, boolean updateNotNull, boolean optimisticLock, String[] filterFields) {
        return jdbcTemplate.update(SqlBeanProvider.updateByBeanIdSql(getSqlBeanDB(), clazz, bean, updateNotNull, optimisticLock, filterFields));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int updateByCondition(T bean, boolean updateNotNull, boolean optimisticLock, String where, Object... args) {
        return jdbcTemplate.update(SqlBeanProvider.updateByConditionSql(getSqlBeanDB(), clazz, bean, updateNotNull, optimisticLock, null, where, args));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int updateByCondition(T bean, boolean updateNotNull, boolean optimisticLock, Wrapper where) {
        Update update = new Update();
        update.setUpdateBean(bean);
        update.setUpdateNotNull(updateNotNull);
        update.setOptimisticLock(optimisticLock);
        update.setWhere(where);
        return jdbcTemplate.update(SqlBeanProvider.updateSql(getSqlBeanDB(), clazz, update, false));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int updateByCondition(T bean, boolean updateNotNull, boolean optimisticLock, String[] filterFields, String where, Object... args) {
        return jdbcTemplate.update(SqlBeanProvider.updateByConditionSql(getSqlBeanDB(), clazz, bean, updateNotNull, optimisticLock, filterFields, where, args));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int updateByCondition(T bean, boolean updateNotNull, boolean optimisticLock, String[] filterFields, Wrapper where) {
        Update update = new Update();
        update.setUpdateBean(bean);
        update.setUpdateNotNull(updateNotNull);
        update.setOptimisticLock(optimisticLock);
        update.setFilterFields(filterFields);
        update.setWhere(where);
        return jdbcTemplate.update(SqlBeanProvider.updateSql(getSqlBeanDB(), clazz, update, false));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int updateByBeanCondition(T bean, boolean updateNotNull, boolean optimisticLock, String where) {
        return jdbcTemplate.update(SqlBeanProvider.updateByBeanConditionSql(getSqlBeanDB(), clazz, bean, updateNotNull, optimisticLock, null, where));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int updateByBeanCondition(T bean, boolean updateNotNull, boolean optimisticLock, String[] filterFields, String where) {
        return jdbcTemplate.update(SqlBeanProvider.updateByBeanConditionSql(getSqlBeanDB(), clazz, bean, updateNotNull, optimisticLock, filterFields, where));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int insert(T... bean) {
        return jdbcTemplate.update(SqlBeanProvider.insertBeanSql(getSqlBeanDB(), clazz, bean));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int insert(List<T> beanList) {
        return jdbcTemplate.update(SqlBeanProvider.insertBeanSql(getSqlBeanDB(), clazz, beanList));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int insert(Insert insert) {
        return jdbcTemplate.update(SqlBeanProvider.insertBeanSql(getSqlBeanDB(), clazz, insert));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public String backup() {
        String targetTableName = SqlBeanUtil.getTable(clazz).getName() + "_" + DateUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS");
        jdbcTemplate.update(SqlBeanProvider.backupSql(getSqlBeanDB(), clazz, null, targetTableName, null, null));
        return targetTableName;
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public void backup(String targetTableName) {
        jdbcTemplate.update(SqlBeanProvider.backupSql(getSqlBeanDB(), clazz, null, targetTableName, null, null));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public void backup(String targetSchema, String targetTableName) {
        jdbcTemplate.update(SqlBeanProvider.backupSql(getSqlBeanDB(), clazz, targetSchema, targetTableName, null, null));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public void backup(String targetTableName, Column[] columns, Wrapper wrapper) {
        jdbcTemplate.update(SqlBeanProvider.backupSql(getSqlBeanDB(), clazz, null, targetTableName, columns, wrapper));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public void backup(String targetSchema, String targetTableName, Column[] columns, Wrapper wrapper) {
        jdbcTemplate.update(SqlBeanProvider.backupSql(getSqlBeanDB(), clazz, targetSchema, targetTableName, columns, wrapper));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int copy(String targetTableName, Wrapper wrapper) {
        return jdbcTemplate.update(SqlBeanProvider.copySql(getSqlBeanDB(), clazz, null, targetTableName, null, wrapper));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int copy(String targetSchema, String targetTableName, Wrapper wrapper) {
        return jdbcTemplate.update(SqlBeanProvider.copySql(getSqlBeanDB(), clazz, targetSchema, targetTableName, null, wrapper));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int copy(String targetTableName, Column[] columns, Wrapper wrapper) {
        return jdbcTemplate.update(SqlBeanProvider.copySql(getSqlBeanDB(), clazz, null, targetTableName, columns, wrapper));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int copy(String targetSchema, String targetTableName, Column[] columns, Wrapper wrapper) {
        return jdbcTemplate.update(SqlBeanProvider.copySql(getSqlBeanDB(), clazz, targetSchema, targetTableName, columns, wrapper));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public void dropTable() {
        SqlBeanDB sqlBeanDB = getSqlBeanDB();
        if (sqlBeanDB.getDbType() != DbType.MySQL && sqlBeanDB.getDbType() != DbType.MariaDB && sqlBeanDB.getDbType() != DbType.PostgreSQL && sqlBeanDB.getDbType() != DbType.SQLServer && sqlBeanDB.getDbType() != DbType.H2) {
            List<String> nameList = jdbcTemplate.queryForList(SqlBeanProvider.selectTableListSql(sqlBeanDB, SqlBeanUtil.getTable(clazz).getName()), String.class);
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
    public List<String> getTableList() {
        return jdbcTemplate.queryForList(SqlBeanProvider.selectTableListSql(getSqlBeanDB(), null), String.class);
    }

    @Override
    public List<Map<String, Object>> getColumnInfoList(String tableName) {
        return jdbcTemplate.queryForList(SqlBeanProvider.selectColumnListSql(getSqlBeanDB(), tableName));
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
