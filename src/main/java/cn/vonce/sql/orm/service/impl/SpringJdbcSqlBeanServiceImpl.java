package cn.vonce.sql.orm.service.impl;

import cn.vonce.sql.bean.*;
import cn.vonce.sql.config.UseSpringJdbc;
import cn.vonce.sql.orm.mapper.SpringJbdcSqlBeanMapper;
import cn.vonce.sql.orm.service.SqlBeanService;
import cn.vonce.sql.orm.provider.SqlBeanProvider;
import cn.vonce.sql.uitls.SqlBeanUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
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
public class SpringJdbcSqlBeanServiceImpl<T> extends SqlBeanProvider implements SqlBeanService<T> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private Logger logger = LoggerFactory.getLogger(SpringJdbcSqlBeanServiceImpl.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Class<?> clazz;

    public SpringJdbcSqlBeanServiceImpl() {
        Type[] typeArray = getClass().getGenericInterfaces();
        if (typeArray == null || typeArray.length == 0) {
            typeArray = new Type[]{getClass().getGenericSuperclass()};
        }
        for (Type type : typeArray) {
            if (type instanceof ParameterizedType) {
                Class<?> trueTypeClass = (Class<?>) ((ParameterizedType) type).getActualTypeArguments()[0];
                try {
                    clazz = this.getClass().getClassLoader().loadClass(trueTypeClass.getName());
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public T selectById(Object id) {
        try {
            return jdbcTemplate.queryForObject(super.selectByIdSql(clazz, id),
                    new SpringJbdcSqlBeanMapper<T>(clazz, clazz));
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    @Override
    public <O> O selectById(Class<O> returnType, Object id) {
        try {
            if (!SqlBeanUtil.isBaseType(returnType.getName()) && !SqlBeanUtil.isMap(returnType.getName())) {
                clazz = returnType;
            }
            return jdbcTemplate.queryForObject(super.selectByIdSql(clazz, id),
                    new SpringJbdcSqlBeanMapper<O>(clazz, returnType));
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    @Override
    public List<T> selectByIds(Object[] ids) {
        try {
            return jdbcTemplate.queryForObject(super.selectByIdsSql(clazz, ids),
                    new SpringJbdcSqlBeanMapper<List<T>>(clazz, clazz));
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    @Override
    public <O> List<O> selectByIds(Class<O> returnType, Object[] ids) {
        try {
            if (!SqlBeanUtil.isBaseType(returnType.getName()) && !SqlBeanUtil.isMap(returnType.getName())) {
                clazz = returnType;
            }
            return jdbcTemplate.queryForObject(super.selectByIdsSql(clazz, ids),
                    new SpringJbdcSqlBeanMapper<List<O>>(clazz, returnType));
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }
    }


    @Override
    public T selectOne(Select select) {
        try {
            return jdbcTemplate.queryForObject(super.selectSql(clazz, select),
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
            return jdbcTemplate.queryForObject(super.selectSql(clazz, select),
                    new SpringJbdcSqlBeanMapper<O>(clazz, returnType));
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    @Override
    public Map<String, Object> selectMap(Select select) {
        try {
            return jdbcTemplate.queryForObject(super.selectSql(clazz, select),
                    new SpringJbdcSqlBeanMapper<Map<String, Object>>(clazz, Map.class));
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }
    }


    @Override
    public T selectOneByCondition(String where, Object... args) {
        try {
            return jdbcTemplate.queryForObject(super.selectByConditionSql(clazz, null, where, args),
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
            return jdbcTemplate.queryForObject(super.selectByConditionSql(clazz, null, where, args),
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
            return jdbcTemplate.query(super.selectByConditionSql(clazz, null, where, args),
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
            return jdbcTemplate.query(super.selectByConditionSql(clazz, paging, where, args),
                    new SpringJbdcSqlBeanMapper<O>(clazz, returnType));
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    @Override
    public List<T> selectByCondition(String where, Object... args) {
        try {
            return jdbcTemplate.query(super.selectByConditionSql(clazz, null, where, args),
                    new SpringJbdcSqlBeanMapper<T>(clazz, clazz));
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    @Override
    public List<T> selectByCondition(Paging paging, String where, Object... args) {
        try {
            return jdbcTemplate.query(super.selectByConditionSql(clazz, paging, where, args),
                    new SpringJbdcSqlBeanMapper<T>(clazz, clazz));
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    public long selectCountByCondition(String where, Object... args) {
        return jdbcTemplate.queryForObject(super.selectCountByConditionSql(clazz, where, args), new SpringJbdcSqlBeanMapper<Long>(clazz, Long.class));
    }

    @Override
    public long countAll() {
        return jdbcTemplate.queryForObject(super.selectCountByConditionSql(clazz, null, null), new SpringJbdcSqlBeanMapper<Long>(clazz, Long.class));
    }

    @Override
    public List<T> selectAll() {
        try {
            return jdbcTemplate.query(super.selectAllSql(clazz, null),
                    new SpringJbdcSqlBeanMapper<T>(clazz, clazz));
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    @Override
    public List<T> selectAll(Paging paging) {
        try {
            return jdbcTemplate.query(super.selectAllSql(clazz, paging),
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
            return jdbcTemplate.query(super.selectAllSql(clazz, null),
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
            return jdbcTemplate.query(super.selectAllSql(clazz, paging),
                    new SpringJbdcSqlBeanMapper<O>(clazz, returnType));
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    @Override
    public List<Map<String, Object>> selectMapList(Select select) {
        try {
            return jdbcTemplate.query(super.selectSql(clazz, select),
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
            return jdbcTemplate.query(super.selectSql(clazz, select),
                    new SpringJbdcSqlBeanMapper<O>(clazz, returnType));
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    @Override
    public List<T> select(Select select) {
        try {
            return jdbcTemplate.query(super.selectSql(clazz, select),
                    new SpringJbdcSqlBeanMapper<T>(clazz, clazz));
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    @Override
    public long count(Select select) {
        return jdbcTemplate.queryForObject(super.countSql(clazz, select), new SpringJbdcSqlBeanMapper<Long>(clazz, Long.class));
    }

    @Override
    public long count(Class<?> clazz, Select select) {
        return jdbcTemplate.queryForObject(super.countSql(clazz, select), Long.class);
    }

    @Override
    public long deleteById(Object... id) {
        return jdbcTemplate.update(super.deleteByIdSql(clazz, id));
    }

    @Override
    public long deleteByCondition(String where, Object... args) {
        return jdbcTemplate.update(super.deleteByConditionSql(clazz, where, args));
    }

    @Override
    public long delete(Delete delete) {
        return jdbcTemplate.update(super.deleteSql(clazz, delete, false));
    }

    @Override
    public long delete(Delete delete, boolean ignore) {
        return jdbcTemplate.update(super.deleteSql(clazz, delete, ignore));
    }

    @Override
    public long logicallyDeleteById(Object id) {
        return jdbcTemplate.update(super.logicallyDeleteByIdSql(clazz, id));
    }

    @Override
    public long logicallyDeleteByCondition(String where, Object... args) {
        return jdbcTemplate.update(super.logicallyDeleteByConditionSql(clazz, where, args));
    }

    @Override
    public long update(Update update) {
        return jdbcTemplate.update(super.updateSql(update, false));
    }

    @Override
    public long update(Update update, boolean ignore) {
        return jdbcTemplate.update(super.updateSql(update, ignore));
    }

    @Override
    public long updateById(T bean, Object id, boolean updateNotNull) {
        return jdbcTemplate.update(super.updateByIdSql(bean, id, updateNotNull, null));
    }

    @Override
    public long updateById(T bean, Object id, boolean updateNotNull, String[] filterFields) {
        return jdbcTemplate.update(super.updateByIdSql(bean, id, updateNotNull, filterFields));
    }

    @Override
    public long updateByBeanId(T bean, boolean updateNotNull) {
        return jdbcTemplate.update(super.updateByBeanIdSql(bean, updateNotNull, null));
    }

    @Override
    public long updateByBeanId(T bean, boolean updateNotNull, String[] filterFields) {
        return jdbcTemplate.update(super.updateByBeanIdSql(bean, updateNotNull, filterFields));
    }

    @Override
    public long updateByCondition(T bean, boolean updateNotNull, String where, Object... args) {
        return jdbcTemplate.update(super.updateByConditionSql(bean, updateNotNull, null, where, args));
    }

    @Override
    public long updateByCondition(T bean, boolean updateNotNull, String[] filterFields, String where, Object... args) {
        return jdbcTemplate.update(super.updateByConditionSql(bean, updateNotNull, filterFields, where, args));
    }

    @Override
    public long updateByBeanCondition(T bean, boolean updateNotNull, String where) {
        return jdbcTemplate.update(super.updateByBeanConditionSql(bean, updateNotNull, null, where));
    }

    @Override
    public long updateByBeanCondition(T bean, boolean updateNotNull, String[] filterFields, String where) {
        return jdbcTemplate.update(super.updateByBeanConditionSql(bean, updateNotNull, filterFields, where));
    }

    @Override
    public long insert(T... bean) {
        return jdbcTemplate.update(super.insertBeanSql(bean));
    }

    @Override
    public long insert(List<T> beanList) {
        return jdbcTemplate.update(super.insertBeanSql(beanList));
    }

    @Override
    public long inset(Insert insert) {
        return jdbcTemplate.update(super.insertBeanSql(insert));
    }
}
