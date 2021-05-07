package cn.vonce.sql.spring.service;

import cn.vonce.sql.bean.*;
import cn.vonce.sql.config.SqlBeanConfig;
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
public class SpringJdbcSqlBeanServiceImpl<T, ID> extends BaseSqlBeanServiceImpl implements SqlBeanService<T, ID> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private Logger logger = LoggerFactory.getLogger(SpringJdbcSqlBeanServiceImpl.class);

    private SqlBeanProvider sqlBeanProvider;

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
        sqlBeanProvider = new SqlBeanProvider();
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
            return jdbcTemplate.queryForObject(sqlBeanProvider.selectByIdSql(getSqlBeanDB(), clazz, id),
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
            return jdbcTemplate.queryForObject(sqlBeanProvider.selectByIdSql(getSqlBeanDB(), clazz, id),
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
            return jdbcTemplate.queryForObject(sqlBeanProvider.selectByIdsSql(getSqlBeanDB(), clazz, ids),
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
            return jdbcTemplate.queryForObject(sqlBeanProvider.selectByIdsSql(getSqlBeanDB(), clazz, ids),
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
            return jdbcTemplate.queryForObject(sqlBeanProvider.selectSql(getSqlBeanDB(), clazz, select),
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
            return jdbcTemplate.queryForObject(sqlBeanProvider.selectSql(getSqlBeanDB(), clazz, select),
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
            return jdbcTemplate.queryForObject(sqlBeanProvider.selectSql(getSqlBeanDB(), clazz, select),
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
            return jdbcTemplate.queryForObject(sqlBeanProvider.selectByConditionSql(getSqlBeanDB(), clazz, null, where, args),
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
            return jdbcTemplate.queryForObject(sqlBeanProvider.selectByConditionSql(getSqlBeanDB(), clazz, null, where, args),
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
            return jdbcTemplate.query(sqlBeanProvider.selectByConditionSql(getSqlBeanDB(), clazz, null, where, args),
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
            return jdbcTemplate.query(sqlBeanProvider.selectByConditionSql(getSqlBeanDB(), clazz, paging, where, args),
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
            return jdbcTemplate.query(sqlBeanProvider.selectByConditionSql(getSqlBeanDB(), clazz, null, where, args),
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
            return jdbcTemplate.query(sqlBeanProvider.selectByConditionSql(getSqlBeanDB(), clazz, paging, where, args),
                    new SpringJbdcSqlBeanMapper<T>(clazz, clazz));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public long selectCountByCondition(String where, Object... args) {
        return jdbcTemplate.queryForObject(sqlBeanProvider.selectCountByConditionSql(getSqlBeanDB(), clazz, where, args), new SpringJbdcSqlBeanMapper<Long>(clazz, Long.class));
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public long countAll() {
        return jdbcTemplate.queryForObject(sqlBeanProvider.selectCountByConditionSql(getSqlBeanDB(), clazz, null, null), new SpringJbdcSqlBeanMapper<Long>(clazz, Long.class));
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public List<T> selectAll() {
        try {
            return jdbcTemplate.query(sqlBeanProvider.selectAllSql(getSqlBeanDB(), clazz, null),
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
            return jdbcTemplate.query(sqlBeanProvider.selectAllSql(getSqlBeanDB(), clazz, paging),
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
            return jdbcTemplate.query(sqlBeanProvider.selectAllSql(getSqlBeanDB(), clazz, null),
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
            return jdbcTemplate.query(sqlBeanProvider.selectAllSql(getSqlBeanDB(), clazz, paging),
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
            return jdbcTemplate.query(sqlBeanProvider.selectSql(getSqlBeanDB(), clazz, select),
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
            return jdbcTemplate.query(sqlBeanProvider.selectSql(getSqlBeanDB(), clazz, select),
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
            return jdbcTemplate.query(sqlBeanProvider.selectSql(getSqlBeanDB(), clazz, select),
                    new SpringJbdcSqlBeanMapper<T>(clazz, clazz));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public long count(Select select) {
        return jdbcTemplate.queryForObject(sqlBeanProvider.countSql(getSqlBeanDB(), clazz, select), new SpringJbdcSqlBeanMapper<Long>(clazz, Long.class));
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public long count(Class<?> clazz, Select select) {
        return jdbcTemplate.queryForObject(sqlBeanProvider.countSql(getSqlBeanDB(), clazz, select), Long.class);
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public long deleteById(ID... id) {
        return jdbcTemplate.update(sqlBeanProvider.deleteByIdSql(getSqlBeanDB(), clazz, id));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public long deleteByCondition(String where, Object... args) {
        return jdbcTemplate.update(sqlBeanProvider.deleteByConditionSql(getSqlBeanDB(), clazz, where, args));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public long delete(Delete delete) {
        return jdbcTemplate.update(sqlBeanProvider.deleteSql(getSqlBeanDB(), clazz, delete, false));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public long delete(Delete delete, boolean ignore) {
        return jdbcTemplate.update(sqlBeanProvider.deleteSql(getSqlBeanDB(), clazz, delete, ignore));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public long logicallyDeleteById(ID id) {
        return jdbcTemplate.update(sqlBeanProvider.logicallyDeleteByIdSql(getSqlBeanDB(), clazz, id));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public long logicallyDeleteByCondition(String where, Object... args) {
        return jdbcTemplate.update(sqlBeanProvider.logicallyDeleteByConditionSql(getSqlBeanDB(), clazz, where, args));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public long update(Update update) {
        return jdbcTemplate.update(sqlBeanProvider.updateSql(getSqlBeanDB(), update, false));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public long update(Update update, boolean ignore) {
        return jdbcTemplate.update(sqlBeanProvider.updateSql(getSqlBeanDB(), update, ignore));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public long updateById(T bean, ID id, boolean updateNotNull) {
        return jdbcTemplate.update(sqlBeanProvider.updateByIdSql(getSqlBeanDB(), bean, id, updateNotNull, null));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public long updateById(T bean, ID id, boolean updateNotNull, String[] filterFields) {
        return jdbcTemplate.update(sqlBeanProvider.updateByIdSql(getSqlBeanDB(), bean, id, updateNotNull, filterFields));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public long updateByBeanId(T bean, boolean updateNotNull) {
        return jdbcTemplate.update(sqlBeanProvider.updateByBeanIdSql(getSqlBeanDB(), bean, updateNotNull, null));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public long updateByBeanId(T bean, boolean updateNotNull, String[] filterFields) {
        return jdbcTemplate.update(sqlBeanProvider.updateByBeanIdSql(getSqlBeanDB(), bean, updateNotNull, filterFields));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public long updateByCondition(T bean, boolean updateNotNull, String where, Object... args) {
        return jdbcTemplate.update(sqlBeanProvider.updateByConditionSql(getSqlBeanDB(), bean, updateNotNull, null, where, args));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public long updateByCondition(T bean, boolean updateNotNull, String[] filterFields, String where, Object... args) {
        return jdbcTemplate.update(sqlBeanProvider.updateByConditionSql(getSqlBeanDB(), bean, updateNotNull, filterFields, where, args));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public long updateByBeanCondition(T bean, boolean updateNotNull, String where) {
        return jdbcTemplate.update(sqlBeanProvider.updateByBeanConditionSql(getSqlBeanDB(), bean, updateNotNull, null, where));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public long updateByBeanCondition(T bean, boolean updateNotNull, String[] filterFields, String where) {
        return jdbcTemplate.update(sqlBeanProvider.updateByBeanConditionSql(getSqlBeanDB(), bean, updateNotNull, filterFields, where));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public long insert(T... bean) {
        return jdbcTemplate.update(sqlBeanProvider.insertBeanSql(getSqlBeanDB(), bean));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public long insert(List<T> beanList) {
        return jdbcTemplate.update(sqlBeanProvider.insertBeanSql(getSqlBeanDB(), beanList));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public long inset(Insert insert) {
        return jdbcTemplate.update(sqlBeanProvider.insertBeanSql(getSqlBeanDB(), insert));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public String backup() {
        String targetTableName = SqlBeanUtil.getTable(clazz).getName() + "_" + DateUtil.dateToString(new Date(), "yyyyMMddHHmmss");
        try {
            jdbcTemplate.update(sqlBeanProvider.backupSql(getSqlBeanDB(), clazz, targetTableName, null, null));
        } catch (Exception e) {
            return null;
        }
        return targetTableName;
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public void backup(String targetTableName) {
        jdbcTemplate.update(sqlBeanProvider.backupSql(getSqlBeanDB(), clazz, targetTableName, null, null));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public void backup(String targetTableName, Column[] columns, Condition condition) {
        jdbcTemplate.update(sqlBeanProvider.backupSql(getSqlBeanDB(), clazz, targetTableName, columns, condition));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public long copy(String targetTableName, Condition condition) {
        return jdbcTemplate.update(sqlBeanProvider.copySql(getSqlBeanDB(), clazz, targetTableName, null, condition));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public long copy(String targetTableName, Column[] columns, Condition condition) {
        return jdbcTemplate.update(sqlBeanProvider.copySql(getSqlBeanDB(), clazz, targetTableName, columns, condition));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public void dropTable() {
        jdbcTemplate.update(sqlBeanProvider.dropTableSql(clazz));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public void createTable() {
        jdbcTemplate.update(sqlBeanProvider.createTableSql(getSqlBeanDB(), clazz));
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
        return jdbcTemplate.queryForList(sqlBeanProvider.selectTableListSql(getSqlBeanDB()), String.class);
    }
}
