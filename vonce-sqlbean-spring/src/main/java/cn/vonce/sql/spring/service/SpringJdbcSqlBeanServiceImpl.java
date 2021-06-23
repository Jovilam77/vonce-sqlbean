package cn.vonce.sql.spring.service;

import cn.vonce.sql.bean.*;
import cn.vonce.sql.config.SqlBeanConfig;
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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.SQLException;
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
                productName = jdbcTemplate.getDataSource().getConnection().getMetaData().getDatabaseProductName();
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
        try {
            return jdbcTemplate.queryForObject(SqlBeanProvider.selectByIdSql(getSqlBeanDB(), clazz, id),
                    new SpringJbdcSqlBeanMapper<T>(clazz, clazz));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public <O> O selectById(Class<O> returnType, ID id) {
        if (id == null) {
            return null;
        }
        try {
            if (!SqlBeanUtil.isBaseType(returnType.getName()) && !SqlBeanUtil.isMap(returnType.getName())) {
                clazz = returnType;
            }
            return jdbcTemplate.queryForObject(SqlBeanProvider.selectByIdSql(getSqlBeanDB(), clazz, id),
                    new SpringJbdcSqlBeanMapper<O>(clazz, returnType));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public List<T> selectByIds(ID... ids) {
        if (ids == null || ids.length == 0) {
            return null;
        }
        try {
            return jdbcTemplate.queryForObject(SqlBeanProvider.selectByIdsSql(getSqlBeanDB(), clazz, ids),
                    new SpringJbdcSqlBeanMapper<List<T>>(clazz, clazz));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public <O> List<O> selectByIds(Class<O> returnType, ID... ids) {
        if (ids == null || ids.length == 0) {
            return null;
        }
        try {
            if (!SqlBeanUtil.isBaseType(returnType.getName()) && !SqlBeanUtil.isMap(returnType.getName())) {
                clazz = returnType;
            }
            return jdbcTemplate.queryForObject(SqlBeanProvider.selectByIdsSql(getSqlBeanDB(), clazz, ids),
                    new SpringJbdcSqlBeanMapper<List<O>>(clazz, returnType));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public T selectOne(Select select) {
        try {
            return jdbcTemplate.queryForObject(SqlBeanProvider.selectSql(getSqlBeanDB(), clazz, select),
                    new SpringJbdcSqlBeanMapper<T>(clazz, clazz));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public <O> O selectOne(Class<O> returnType, Select select) {
        try {
            if (!SqlBeanUtil.isBaseType(returnType.getName()) && !SqlBeanUtil.isMap(returnType.getName())) {
                clazz = returnType;
            }
            return jdbcTemplate.queryForObject(SqlBeanProvider.selectSql(getSqlBeanDB(), clazz, select),
                    new SpringJbdcSqlBeanMapper<O>(clazz, returnType));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public Map<String, Object> selectMap(Select select) {
        try {
            return jdbcTemplate.queryForObject(SqlBeanProvider.selectSql(getSqlBeanDB(), clazz, select),
                    new SpringJbdcSqlBeanMapper<Map<String, Object>>(clazz, Map.class));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public T selectOneByCondition(String where, Object... args) {
        try {
            return jdbcTemplate.queryForObject(SqlBeanProvider.selectByConditionSql(getSqlBeanDB(), clazz, null, where, args),
                    new SpringJbdcSqlBeanMapper<T>(clazz, clazz));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public <O> O selectOneByCondition(Class<O> returnType, String where, Object... args) {
        try {
            if (!SqlBeanUtil.isBaseType(returnType.getName()) && !SqlBeanUtil.isMap(returnType.getName())) {
                clazz = returnType;
            }
            return jdbcTemplate.queryForObject(SqlBeanProvider.selectByConditionSql(getSqlBeanDB(), clazz, null, where, args),
                    new SpringJbdcSqlBeanMapper<O>(clazz, returnType));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public T selectOneByCondition(Wrapper where) {
        try {
            Select select = new Select();
            select.setWhere(where);
            return jdbcTemplate.queryForObject(SqlBeanProvider.selectSql(getSqlBeanDB(), clazz, select),
                    new SpringJbdcSqlBeanMapper<T>(clazz, clazz));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public <O> O selectOneByCondition(Class<O> returnType, Wrapper where) {
        try {
            Select select = new Select();
            select.setWhere(where);
            if (!SqlBeanUtil.isBaseType(returnType.getName()) && !SqlBeanUtil.isMap(returnType.getName())) {
                clazz = returnType;
            }
            return jdbcTemplate.queryForObject(SqlBeanProvider.selectSql(getSqlBeanDB(), clazz, select),
                    new SpringJbdcSqlBeanMapper<O>(clazz, returnType));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public <O> List<O> selectByCondition(Class<O> returnType, String where, Object... args) {
        try {
            if (!SqlBeanUtil.isBaseType(returnType.getName()) && !SqlBeanUtil.isMap(returnType.getName())) {
                clazz = returnType;
            }
            return jdbcTemplate.query(SqlBeanProvider.selectByConditionSql(getSqlBeanDB(), clazz, null, where, args),
                    new SpringJbdcSqlBeanMapper<O>(clazz, returnType));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public <O> List<O> selectByCondition(Class<O> returnType, Wrapper where) {
        try {
            Select select = new Select();
            select.setWhere(where);
            if (!SqlBeanUtil.isBaseType(returnType.getName()) && !SqlBeanUtil.isMap(returnType.getName())) {
                clazz = returnType;
            }
            return jdbcTemplate.query(SqlBeanProvider.selectSql(getSqlBeanDB(), clazz, select),
                    new SpringJbdcSqlBeanMapper<O>(clazz, returnType));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public <O> List<O> selectByCondition(Class<O> returnType, Paging paging, String where, Object... args) {
        try {
            if (!SqlBeanUtil.isBaseType(returnType.getName()) && !SqlBeanUtil.isMap(returnType.getName())) {
                clazz = returnType;
            }
            return jdbcTemplate.query(SqlBeanProvider.selectByConditionSql(getSqlBeanDB(), clazz, paging, where, args),
                    new SpringJbdcSqlBeanMapper<O>(clazz, returnType));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public <O> List<O> selectByCondition(Class<O> returnType, Paging paging, Wrapper where) {
        try {
            Select select = new Select();
            select.setWhere(where);
            select.setPage(paging.getPagenum(), paging.getPagesize());
            select.orderBy(paging.getOrders());
            if (!SqlBeanUtil.isBaseType(returnType.getName()) && !SqlBeanUtil.isMap(returnType.getName())) {
                clazz = returnType;
            }
            return jdbcTemplate.query(SqlBeanProvider.selectSql(getSqlBeanDB(), clazz, select),
                    new SpringJbdcSqlBeanMapper<O>(clazz, returnType));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public List<T> selectByCondition(String where, Object... args) {
        try {
            return jdbcTemplate.query(SqlBeanProvider.selectByConditionSql(getSqlBeanDB(), clazz, null, where, args),
                    new SpringJbdcSqlBeanMapper<T>(clazz, clazz));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public List<T> selectByCondition(Wrapper where) {
        try {
            Select select = new Select();
            select.setWhere(where);
            return jdbcTemplate.query(SqlBeanProvider.selectSql(getSqlBeanDB(), clazz, select),
                    new SpringJbdcSqlBeanMapper<T>(clazz, clazz));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public List<T> selectByCondition(Paging paging, String where, Object... args) {
        try {
            return jdbcTemplate.query(SqlBeanProvider.selectByConditionSql(getSqlBeanDB(), clazz, paging, where, args),
                    new SpringJbdcSqlBeanMapper<T>(clazz, clazz));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public List<T> selectByCondition(Paging paging, Wrapper where) {
        try {
            Select select = new Select();
            select.setWhere(where);
            select.setPage(paging.getPagenum(), paging.getPagesize());
            select.orderBy(paging.getOrders());
            return jdbcTemplate.query(SqlBeanProvider.selectSql(getSqlBeanDB(), clazz, select),
                    new SpringJbdcSqlBeanMapper<T>(clazz, clazz));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }
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
        try {
            return jdbcTemplate.query(SqlBeanProvider.selectAllSql(getSqlBeanDB(), clazz, null),
                    new SpringJbdcSqlBeanMapper<T>(clazz, clazz));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public List<T> selectAll(Paging paging) {
        try {
            return jdbcTemplate.query(SqlBeanProvider.selectAllSql(getSqlBeanDB(), clazz, paging),
                    new SpringJbdcSqlBeanMapper<T>(clazz, clazz));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public <O> List<O> selectAll(Class<O> returnType) {
        try {
            if (!SqlBeanUtil.isBaseType(returnType.getName()) && !SqlBeanUtil.isMap(returnType.getName())) {
                clazz = returnType;
            }
            return jdbcTemplate.query(SqlBeanProvider.selectAllSql(getSqlBeanDB(), clazz, null),
                    new SpringJbdcSqlBeanMapper<O>(clazz, returnType));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public <O> List<O> selectAll(Class<O> returnType, Paging paging) {
        try {
            if (!SqlBeanUtil.isBaseType(returnType.getName()) && !SqlBeanUtil.isMap(returnType.getName())) {
                clazz = returnType;
            }
            return jdbcTemplate.query(SqlBeanProvider.selectAllSql(getSqlBeanDB(), clazz, paging),
                    new SpringJbdcSqlBeanMapper<O>(clazz, returnType));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public List<Map<String, Object>> selectMapList(Select select) {
        try {
            return jdbcTemplate.query(SqlBeanProvider.selectSql(getSqlBeanDB(), clazz, select),
                    new SpringJbdcSqlBeanMapper<Map<String, Object>>(clazz, Map.class));
        } catch (
                Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }

    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public <O> List<O> select(Class<O> returnType, Select select) {
        try {
            if (!SqlBeanUtil.isBaseType(returnType.getName()) && !SqlBeanUtil.isMap(returnType.getName())) {
                clazz = returnType;
            }
            return jdbcTemplate.query(SqlBeanProvider.selectSql(getSqlBeanDB(), clazz, select),
                    new SpringJbdcSqlBeanMapper<O>(clazz, returnType));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public List<T> select(Select select) {
        try {
            return jdbcTemplate.query(SqlBeanProvider.selectSql(getSqlBeanDB(), clazz, select),
                    new SpringJbdcSqlBeanMapper<T>(clazz, clazz));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }
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
    public int logicallyDeleteById(ID id) {
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
    public int updateById(T bean, ID id, boolean updateNotNull) {
        return jdbcTemplate.update(SqlBeanProvider.updateByIdSql(getSqlBeanDB(), clazz, bean, id, updateNotNull, null));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int updateById(T bean, ID id, boolean updateNotNull, String[] filterFields) {
        return jdbcTemplate.update(SqlBeanProvider.updateByIdSql(getSqlBeanDB(), clazz, bean, id, updateNotNull, filterFields));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int updateByBeanId(T bean, boolean updateNotNull) {
        return jdbcTemplate.update(SqlBeanProvider.updateByBeanIdSql(getSqlBeanDB(), clazz, bean, updateNotNull, null));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int updateByBeanId(T bean, boolean updateNotNull, String[] filterFields) {
        return jdbcTemplate.update(SqlBeanProvider.updateByBeanIdSql(getSqlBeanDB(), clazz, bean, updateNotNull, filterFields));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int updateByCondition(T bean, boolean updateNotNull, String where, Object... args) {
        return jdbcTemplate.update(SqlBeanProvider.updateByConditionSql(getSqlBeanDB(), clazz, bean, updateNotNull, null, where, args));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int updateByCondition(T bean, boolean updateNotNull, Wrapper where) {
        Update update = new Update();
        update.setUpdateBean(bean);
        update.setWhere(where);
        return jdbcTemplate.update(SqlBeanProvider.updateSql(getSqlBeanDB(), clazz, update, false));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int updateByCondition(T bean, boolean updateNotNull, String[] filterFields, String where, Object... args) {
        return jdbcTemplate.update(SqlBeanProvider.updateByConditionSql(getSqlBeanDB(), clazz, bean, updateNotNull, filterFields, where, args));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int updateByCondition(T bean, boolean updateNotNull, String[] filterFields, Wrapper where) {
        Update update = new Update();
        update.setUpdateBean(bean);
        update.setUpdateNotNull(updateNotNull);
        update.setFilterFields(filterFields);
        update.setWhere(where);
        return jdbcTemplate.update(SqlBeanProvider.updateSql(getSqlBeanDB(), clazz, update, false));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int updateByBeanCondition(T bean, boolean updateNotNull, String where) {
        return jdbcTemplate.update(SqlBeanProvider.updateByBeanConditionSql(getSqlBeanDB(), clazz, bean, updateNotNull, null, where));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int updateByBeanCondition(T bean, boolean updateNotNull, String[] filterFields, String where) {
        return jdbcTemplate.update(SqlBeanProvider.updateByBeanConditionSql(getSqlBeanDB(), clazz, bean, updateNotNull, filterFields, where));
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
    public int inset(Insert insert) {
        return jdbcTemplate.update(SqlBeanProvider.insertBeanSql(getSqlBeanDB(), clazz, insert));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public String backup() {
        String targetTableName = SqlBeanUtil.getTable(clazz).getName() + "_" + DateUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS");
        try {
            jdbcTemplate.update(SqlBeanProvider.backupSql(getSqlBeanDB(), clazz, targetTableName, null, null));
        } catch (Exception e) {
            return null;
        }
        return targetTableName;
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public void backup(String targetTableName) {
        jdbcTemplate.update(SqlBeanProvider.backupSql(getSqlBeanDB(), clazz, targetTableName, null, null));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public void backup(String targetTableName, Column[] columns, Wrapper wrapper) {
        jdbcTemplate.update(SqlBeanProvider.backupSql(getSqlBeanDB(), clazz, targetTableName, columns, wrapper));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int copy(String targetTableName, Wrapper wrapper) {
        return jdbcTemplate.update(SqlBeanProvider.copySql(getSqlBeanDB(), clazz, targetTableName, null, wrapper));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int copy(String targetTableName, Column[] columns, Wrapper wrapper) {
        return jdbcTemplate.update(SqlBeanProvider.copySql(getSqlBeanDB(), clazz, targetTableName, columns, wrapper));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public void dropTable() {
        jdbcTemplate.update(SqlBeanProvider.dropTableSql(getSqlBeanDB(), clazz));
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
        return jdbcTemplate.queryForList(SqlBeanProvider.selectTableListSql(getSqlBeanDB()), String.class);
    }
}
