package cn.vonce.sql.spring.service;

import cn.vonce.sql.bean.*;
import cn.vonce.sql.enumerate.DbType;
import cn.vonce.sql.spring.config.UseSpringJdbc;
import cn.vonce.sql.spring.mapper.SpringJbdcSqlBeanMapper;
import cn.vonce.sql.provider.SqlBeanProvider;
import cn.vonce.sql.service.SqlBeanService;
import cn.vonce.sql.service.TableService;
import cn.vonce.sql.uitls.DateUtil;
import cn.vonce.sql.uitls.SqlBeanUtil;
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
public class SpringJdbcSqlBeanServiceImpl<T, ID> extends SqlBeanProvider implements SqlBeanService<T, ID> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private Logger logger = LoggerFactory.getLogger(SpringJdbcSqlBeanServiceImpl.class);

    private TableService tableService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

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
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public String getProductName() {
        try {
            return jdbcTemplate.getDataSource().getConnection().getMetaData().getDatabaseProductName();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    @Override
    public Class<?> getBeanClass() {
        return clazz;
    }

    @Override
    public T selectById(ID id) {
        if (id == null) {
            return null;
        }
        try {
            return jdbcTemplate.queryForObject(super.selectByIdSql(DbType.getSqlBeanConfig(getProductName()), clazz, id),
                    new SpringJbdcSqlBeanMapper<T>(clazz, clazz));
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    @Override
    public <O> O selectById(Class<O> returnType, ID id) {
        if (id == null) {
            return null;
        }
        try {
            if (!SqlBeanUtil.isBaseType(returnType.getName()) && !SqlBeanUtil.isMap(returnType.getName())) {
                clazz = returnType;
            }
            return jdbcTemplate.queryForObject(super.selectByIdSql(DbType.getSqlBeanConfig(getProductName()), clazz, id),
                    new SpringJbdcSqlBeanMapper<O>(clazz, returnType));
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    @Override
    public List<T> selectByIds(ID... ids) {
        if (ids == null || ids.length == 0) {
            return null;
        }
        try {
            return jdbcTemplate.queryForObject(super.selectByIdsSql(DbType.getSqlBeanConfig(getProductName()), clazz, ids),
                    new SpringJbdcSqlBeanMapper<List<T>>(clazz, clazz));
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    @Override
    public <O> List<O> selectByIds(Class<O> returnType, ID... ids) {
        if (ids == null || ids.length == 0) {
            return null;
        }
        try {
            if (!SqlBeanUtil.isBaseType(returnType.getName()) && !SqlBeanUtil.isMap(returnType.getName())) {
                clazz = returnType;
            }
            return jdbcTemplate.queryForObject(super.selectByIdsSql(DbType.getSqlBeanConfig(getProductName()), clazz, ids),
                    new SpringJbdcSqlBeanMapper<List<O>>(clazz, returnType));
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }
    }


    @Override
    public T selectOne(Select select) {
        try {
            return jdbcTemplate.queryForObject(super.selectSql(DbType.getSqlBeanConfig(getProductName()), clazz, select),
                    new SpringJbdcSqlBeanMapper<T>(clazz, clazz));
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }
    }


    @Override
    public <O> O selectOne(Class<O> returnType, Select select) {
        try {
            if (!SqlBeanUtil.isBaseType(returnType.getName()) && !SqlBeanUtil.isMap(returnType.getName())) {
                clazz = returnType;
            }
            return jdbcTemplate.queryForObject(super.selectSql(DbType.getSqlBeanConfig(getProductName()), clazz, select),
                    new SpringJbdcSqlBeanMapper<O>(clazz, returnType));
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    @Override
    public Map<String, Object> selectMap(Select select) {
        try {
            return jdbcTemplate.queryForObject(super.selectSql(DbType.getSqlBeanConfig(getProductName()), clazz, select),
                    new SpringJbdcSqlBeanMapper<Map<String, Object>>(clazz, Map.class));
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    @Override
    public T selectOneByCondition(String where, Object... args) {
        try {
            return jdbcTemplate.queryForObject(super.selectByConditionSql(DbType.getSqlBeanConfig(getProductName()), clazz, null, where, args),
                    new SpringJbdcSqlBeanMapper<T>(clazz, clazz));
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    @Override
    public <O> O selectOneByCondition(Class<O> returnType, String where, Object... args) {
        try {
            if (!SqlBeanUtil.isBaseType(returnType.getName()) && !SqlBeanUtil.isMap(returnType.getName())) {
                clazz = returnType;
            }
            return jdbcTemplate.queryForObject(super.selectByConditionSql(DbType.getSqlBeanConfig(getProductName()), clazz, null, where, args),
                    new SpringJbdcSqlBeanMapper<O>(clazz, returnType));
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    @Override
    public <O> List<O> selectByCondition(Class<O> returnType, String where, Object... args) {
        try {
            if (!SqlBeanUtil.isBaseType(returnType.getName()) && !SqlBeanUtil.isMap(returnType.getName())) {
                clazz = returnType;
            }
            return jdbcTemplate.query(super.selectByConditionSql(DbType.getSqlBeanConfig(getProductName()), clazz, null, where, args),
                    new SpringJbdcSqlBeanMapper<O>(clazz, returnType));
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    @Override
    public <O> List<O> selectByCondition(Class<O> returnType, Paging paging, String where, Object... args) {
        try {
            if (!SqlBeanUtil.isBaseType(returnType.getName()) && !SqlBeanUtil.isMap(returnType.getName())) {
                clazz = returnType;
            }
            return jdbcTemplate.query(super.selectByConditionSql(DbType.getSqlBeanConfig(getProductName()), clazz, paging, where, args),
                    new SpringJbdcSqlBeanMapper<O>(clazz, returnType));
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    @Override
    public List<T> selectByCondition(String where, Object... args) {
        try {
            return jdbcTemplate.query(super.selectByConditionSql(DbType.getSqlBeanConfig(getProductName()), clazz, null, where, args),
                    new SpringJbdcSqlBeanMapper<T>(clazz, clazz));
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    @Override
    public List<T> selectByCondition(Paging paging, String where, Object... args) {
        try {
            return jdbcTemplate.query(super.selectByConditionSql(DbType.getSqlBeanConfig(getProductName()), clazz, paging, where, args),
                    new SpringJbdcSqlBeanMapper<T>(clazz, clazz));
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    public long selectCountByCondition(String where, Object... args) {
        return jdbcTemplate.queryForObject(super.selectCountByConditionSql(DbType.getSqlBeanConfig(getProductName()), clazz, where, args), new SpringJbdcSqlBeanMapper<Long>(clazz, Long.class));
    }

    @Override
    public long countAll() {
        return jdbcTemplate.queryForObject(super.selectCountByConditionSql(DbType.getSqlBeanConfig(getProductName()), clazz, null, null), new SpringJbdcSqlBeanMapper<Long>(clazz, Long.class));
    }

    @Override
    public List<T> selectAll() {
        try {
            return jdbcTemplate.query(super.selectAllSql(DbType.getSqlBeanConfig(getProductName()), clazz, null),
                    new SpringJbdcSqlBeanMapper<T>(clazz, clazz));
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    @Override
    public List<T> selectAll(Paging paging) {
        try {
            return jdbcTemplate.query(super.selectAllSql(DbType.getSqlBeanConfig(getProductName()), clazz, paging),
                    new SpringJbdcSqlBeanMapper<T>(clazz, clazz));
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    @Override
    public <O> List<O> selectAll(Class<O> returnType) {
        try {
            if (!SqlBeanUtil.isBaseType(returnType.getName()) && !SqlBeanUtil.isMap(returnType.getName())) {
                clazz = returnType;
            }
            return jdbcTemplate.query(super.selectAllSql(DbType.getSqlBeanConfig(getProductName()), clazz, null),
                    new SpringJbdcSqlBeanMapper<O>(clazz, returnType));
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    @Override
    public <O> List<O> selectAll(Class<O> returnType, Paging paging) {
        try {
            if (!SqlBeanUtil.isBaseType(returnType.getName()) && !SqlBeanUtil.isMap(returnType.getName())) {
                clazz = returnType;
            }
            return jdbcTemplate.query(super.selectAllSql(DbType.getSqlBeanConfig(getProductName()), clazz, paging),
                    new SpringJbdcSqlBeanMapper<O>(clazz, returnType));
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    @Override
    public List<Map<String, Object>> selectMapList(Select select) {
        try {
            return jdbcTemplate.query(super.selectSql(DbType.getSqlBeanConfig(getProductName()), clazz, select),
                    new SpringJbdcSqlBeanMapper<Map<String, Object>>(clazz, Map.class));
        } catch (
                Exception e) {
            logger.error(e.getMessage());
            return null;
        }

    }

    @Override
    public <O> List<O> select(Class<O> returnType, Select select) {
        try {
            if (!SqlBeanUtil.isBaseType(returnType.getName()) && !SqlBeanUtil.isMap(returnType.getName())) {
                clazz = returnType;
            }
            return jdbcTemplate.query(super.selectSql(DbType.getSqlBeanConfig(getProductName()), clazz, select),
                    new SpringJbdcSqlBeanMapper<O>(clazz, returnType));
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    @Override
    public List<T> select(Select select) {
        try {
            return jdbcTemplate.query(super.selectSql(DbType.getSqlBeanConfig(getProductName()), clazz, select),
                    new SpringJbdcSqlBeanMapper<T>(clazz, clazz));
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    @Override
    public long count(Select select) {
        return jdbcTemplate.queryForObject(super.countSql(DbType.getSqlBeanConfig(getProductName()), clazz, select), new SpringJbdcSqlBeanMapper<Long>(clazz, Long.class));
    }

    @Override
    public long count(Class<?> clazz, Select select) {
        return jdbcTemplate.queryForObject(super.countSql(DbType.getSqlBeanConfig(getProductName()), clazz, select), Long.class);
    }

    @Override
    public long deleteById(ID... id) {
        return jdbcTemplate.update(super.deleteByIdSql(DbType.getSqlBeanConfig(getProductName()), clazz, id));
    }

    @Override
    public long deleteByCondition(String where, Object... args) {
        return jdbcTemplate.update(super.deleteByConditionSql(DbType.getSqlBeanConfig(getProductName()), clazz, where, args));
    }

    @Override
    public long delete(Delete delete) {
        return jdbcTemplate.update(super.deleteSql(DbType.getSqlBeanConfig(getProductName()), clazz, delete, false));
    }

    @Override
    public long delete(Delete delete, boolean ignore) {
        return jdbcTemplate.update(super.deleteSql(DbType.getSqlBeanConfig(getProductName()), clazz, delete, ignore));
    }

    @Override
    public long logicallyDeleteById(ID id) {
        return jdbcTemplate.update(super.logicallyDeleteByIdSql(DbType.getSqlBeanConfig(getProductName()), clazz, id));
    }

    @Override
    public long logicallyDeleteByCondition(String where, Object... args) {
        return jdbcTemplate.update(super.logicallyDeleteByConditionSql(DbType.getSqlBeanConfig(getProductName()), clazz, where, args));
    }

    @Override
    public long update(Update update) {
        return jdbcTemplate.update(super.updateSql(DbType.getSqlBeanConfig(getProductName()), update, false));
    }

    @Override
    public long update(Update update, boolean ignore) {
        return jdbcTemplate.update(super.updateSql(DbType.getSqlBeanConfig(getProductName()), update, ignore));
    }

    @Override
    public long updateById(T bean, ID id, boolean updateNotNull) {
        return jdbcTemplate.update(super.updateByIdSql(DbType.getSqlBeanConfig(getProductName()), bean, id, updateNotNull, null));
    }

    @Override
    public long updateById(T bean, ID id, boolean updateNotNull, String[] filterFields) {
        return jdbcTemplate.update(super.updateByIdSql(DbType.getSqlBeanConfig(getProductName()), bean, id, updateNotNull, filterFields));
    }

    @Override
    public long updateByBeanId(T bean, boolean updateNotNull) {
        return jdbcTemplate.update(super.updateByBeanIdSql(DbType.getSqlBeanConfig(getProductName()), bean, updateNotNull, null));
    }

    @Override
    public long updateByBeanId(T bean, boolean updateNotNull, String[] filterFields) {
        return jdbcTemplate.update(super.updateByBeanIdSql(DbType.getSqlBeanConfig(getProductName()), bean, updateNotNull, filterFields));
    }

    @Override
    public long updateByCondition(T bean, boolean updateNotNull, String where, Object... args) {
        return jdbcTemplate.update(super.updateByConditionSql(DbType.getSqlBeanConfig(getProductName()), bean, updateNotNull, null, where, args));
    }

    @Override
    public long updateByCondition(T bean, boolean updateNotNull, String[] filterFields, String where, Object... args) {
        return jdbcTemplate.update(super.updateByConditionSql(DbType.getSqlBeanConfig(getProductName()), bean, updateNotNull, filterFields, where, args));
    }

    @Override
    public long updateByBeanCondition(T bean, boolean updateNotNull, String where) {
        return jdbcTemplate.update(super.updateByBeanConditionSql(DbType.getSqlBeanConfig(getProductName()), bean, updateNotNull, null, where));
    }

    @Override
    public long updateByBeanCondition(T bean, boolean updateNotNull, String[] filterFields, String where) {
        return jdbcTemplate.update(super.updateByBeanConditionSql(DbType.getSqlBeanConfig(getProductName()), bean, updateNotNull, filterFields, where));
    }

    @Override
    public long insert(T... bean) {
        return jdbcTemplate.update(super.insertBeanSql(DbType.getSqlBeanConfig(getProductName()), bean));
    }

    @Override
    public long insert(List<T> beanList) {
        return jdbcTemplate.update(super.insertBeanSql(DbType.getSqlBeanConfig(getProductName()), beanList));
    }

    @Override
    public long inset(Insert insert) {
        return jdbcTemplate.update(super.insertBeanSql(DbType.getSqlBeanConfig(getProductName()), insert));
    }

    @Override
    public String backup() {
        String targetTableName = SqlBeanUtil.getTable(clazz).getName() + "_" + DateUtil.dateToString(new Date(), "yyyyMMddHHmmss");
        try {
            jdbcTemplate.update(super.backupSql(DbType.getSqlBeanConfig(getProductName()), clazz, targetTableName, null, null));
        } catch (Exception e) {
            return null;
        }
        return targetTableName;
    }

    @Override
    public void backup(String targetTableName) {
        jdbcTemplate.update(super.backupSql(DbType.getSqlBeanConfig(getProductName()), clazz, targetTableName, null, null));
    }

    @Override
    public void backup(String targetTableName, Column[] columns, Condition condition) {
        jdbcTemplate.update(super.backupSql(DbType.getSqlBeanConfig(getProductName()), clazz, targetTableName, columns, condition));
    }

    @Override
    public long copy(String targetTableName, Condition condition) {
        return jdbcTemplate.update(super.copySql(DbType.getSqlBeanConfig(getProductName()), clazz, targetTableName, null, condition));
    }

    @Override
    public long copy(String targetTableName, Column[] columns, Condition condition) {
        return jdbcTemplate.update(super.copySql(DbType.getSqlBeanConfig(getProductName()), clazz, targetTableName, columns, condition));
    }

    @Override
    public TableService getTableService() {
        if (tableService == null) {
            tableService = new TableService() {
                @Override
                public void dropTable() {
                    jdbcTemplate.update(SpringJdbcSqlBeanServiceImpl.super.dropTableSql(clazz));
                }

                @Override
                public void createTable() {
                    jdbcTemplate.update(SpringJdbcSqlBeanServiceImpl.super.createTableSql(DbType.getSqlBeanConfig(getProductName()), clazz));
                }

                @Override
                public void dropAndCreateTable() {
                    dropTable();
                    createTable();
                }

                @Override
                public List<String> getTableList() {
                    return jdbcTemplate.queryForList(SpringJdbcSqlBeanServiceImpl.super.selectTableListSql(DbType.getSqlBeanConfig(getProductName())), null, String.class);
                }

            };
        }
        return tableService;
    }
}
