package cn.vonce.sql.orm.service.impl;

import cn.vonce.sql.bean.*;
import cn.vonce.sql.config.EnableAutoConfig;
import cn.vonce.sql.orm.mapper.SpringJbdcSqlBeanMapper;
import cn.vonce.sql.orm.service.SqlBeanService;
import cn.vonce.sql.provider.SqlBeanProvider;
import cn.vonce.sql.uitls.SqlBeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
@EnableAutoConfig
@Transactional(readOnly = true)
@Service
public class SpringJdbcSqlBeanServiceImpl<T> extends SqlBeanProvider implements SqlBeanService<T> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

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
        return jdbcTemplate.queryForObject(super.selectByIdSql(clazz, id),
                new SpringJbdcSqlBeanMapper<T>(clazz, clazz));
    }

    @Override
    public <O> O selectById(Class<O> returnType, Object id) {
        if (!SqlBeanUtil.isBaseType(returnType.getName()) && !SqlBeanUtil.isMap(returnType.getName())) {
            clazz = returnType;
        }
        return jdbcTemplate.queryForObject(super.selectByIdSql(clazz, id),
                new SpringJbdcSqlBeanMapper<O>(clazz, returnType));
    }

    @Override
    public List<T> selectByIds(Object[] ids) {
        return jdbcTemplate.queryForObject(super.selectByIdsSql(clazz, ids),
                new SpringJbdcSqlBeanMapper<List<T>>(clazz, clazz));
    }

    @Override
    public <O> List<O> selectByIds(Class<O> returnType, Object[] ids) {
        if (!SqlBeanUtil.isBaseType(returnType.getName()) && !SqlBeanUtil.isMap(returnType.getName())) {
            clazz = returnType;
        }
        return jdbcTemplate.queryForObject(super.selectByIdsSql(clazz, ids),
                new SpringJbdcSqlBeanMapper<List<O>>(clazz, returnType));
    }


    @Override
    public T selectOne(Select select) {
        return jdbcTemplate.queryForObject(super.selectSql(clazz, select),
                new SpringJbdcSqlBeanMapper<T>(clazz, clazz));
    }


    @Override
    public <O> O selectOne(Class<O> returnType, Select select) {
        if (!SqlBeanUtil.isBaseType(returnType.getName()) && !SqlBeanUtil.isMap(returnType.getName())) {
            clazz = returnType;
        }
        return jdbcTemplate.queryForObject(super.selectSql(clazz, select),
                new SpringJbdcSqlBeanMapper<O>(clazz, returnType));
    }

    @Override
    public Map<String, Object> selectMap(Select select) {
        return jdbcTemplate.queryForObject(super.selectSql(clazz, select),
                new SpringJbdcSqlBeanMapper<Map<String, Object>>(clazz, Map.class));
    }


    @Override
    public T selectOneByCondition(String where, Object... args) {
        return jdbcTemplate.queryForObject(super.selectByConditionSql(clazz, null, where, args),
                new SpringJbdcSqlBeanMapper<T>(clazz, clazz));
    }

    @Override
    public <O> O selectOneByCondition(Class<O> returnType, String where, Object... args) {
        if (!SqlBeanUtil.isBaseType(returnType.getName()) && !SqlBeanUtil.isMap(returnType.getName())) {
            clazz = returnType;
        }
        return jdbcTemplate.queryForObject(super.selectByConditionSql(clazz, null, where, args),
                new SpringJbdcSqlBeanMapper<O>(clazz, returnType));
    }

    @Override
    public <O> List<O> selectByCondition(Class<O> returnType, String where, Object... args) {
        if (!SqlBeanUtil.isBaseType(returnType.getName()) && !SqlBeanUtil.isMap(returnType.getName())) {
            clazz = returnType;
        }
        return jdbcTemplate.query(super.selectByConditionSql(clazz, null, where, args),
                new SpringJbdcSqlBeanMapper<O>(clazz, returnType));
    }

    @Override
    public <O> List<O> selectByCondition(Class<O> returnType, Paging paging, String where, Object... args) {
        if (!SqlBeanUtil.isBaseType(returnType.getName()) && !SqlBeanUtil.isMap(returnType.getName())) {
            clazz = returnType;
        }
        return jdbcTemplate.query(super.selectByConditionSql(clazz, paging, where, args),
                new SpringJbdcSqlBeanMapper<O>(clazz, returnType));
    }

    @Override
    public List<T> selectByCondition(String where, Object... args) {
        return jdbcTemplate.query(super.selectByConditionSql(clazz, null, where, args),
                new SpringJbdcSqlBeanMapper<T>(clazz, clazz));
    }

    @Override
    public List<T> selectByCondition(Paging paging, String where, Object... args) {
        return jdbcTemplate.query(super.selectByConditionSql(clazz, paging, where, args),
                new SpringJbdcSqlBeanMapper<T>(clazz, clazz));
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
        return jdbcTemplate.query(super.selectAllSql(clazz, null),
                new SpringJbdcSqlBeanMapper<T>(clazz, clazz));
    }

    @Override
    public List<T> selectAll(Paging paging) {
        return jdbcTemplate.query(super.selectAllSql(clazz, paging),
                new SpringJbdcSqlBeanMapper<T>(clazz, clazz));
    }

    @Override
    public <O> List<O> selectAll(Class<O> returnType) {
        if (!SqlBeanUtil.isBaseType(returnType.getName()) && !SqlBeanUtil.isMap(returnType.getName())) {
            clazz = returnType;
        }
        return jdbcTemplate.query(super.selectAllSql(clazz, null),
                new SpringJbdcSqlBeanMapper<O>(clazz, returnType));
    }

    @Override
    public <O> List<O> selectAll(Class<O> returnType, Paging paging) {
        if (!SqlBeanUtil.isBaseType(returnType.getName()) && !SqlBeanUtil.isMap(returnType.getName())) {
            clazz = returnType;
        }
        return jdbcTemplate.query(super.selectAllSql(clazz, paging),
                new SpringJbdcSqlBeanMapper<O>(clazz, returnType));
    }

    @Override
    public List<Map<String, Object>> selectMapList(Select select) {
        return jdbcTemplate.query(super.selectSql(clazz, select),
                new SpringJbdcSqlBeanMapper<Map<String, Object>>(clazz, Map.class));
    }

    @Override
    public <O> List<O> select(Class<O> returnType, Select select) {
        if (!SqlBeanUtil.isBaseType(returnType.getName()) && !SqlBeanUtil.isMap(returnType.getName())) {
            clazz = returnType;
        }
        return jdbcTemplate.query(super.selectSql(clazz, select),
                new SpringJbdcSqlBeanMapper<O>(clazz, returnType));
    }

    @Override
    public List<T> select(Select select) {
        return jdbcTemplate.query(super.selectSql(clazz, select),
                new SpringJbdcSqlBeanMapper<T>(clazz, clazz));
    }

    @Override
    public long count(Select select) {
        return jdbcTemplate.queryForObject(super.selectSql(clazz, select), new SpringJbdcSqlBeanMapper<Long>(clazz, Long.class));
    }

    @Override
    public long count(Class<?> clazz, Select select) {
        return jdbcTemplate.queryForObject(super.selectSql(clazz, select), Long.class);
    }

    @Transactional(readOnly = false)
    @Override
    public long deleteById(Object... id) {
        return jdbcTemplate.update(super.deleteByIdSql(clazz, id));
    }

    @Transactional(readOnly = false)
    @Override
    public long deleteByCondition(String where, Object... args) {
        return jdbcTemplate.update(super.deleteByConditionSql(clazz, where, args));
    }

    @Transactional(readOnly = false)
    @Override
    public long delete(Delete delete) {
        return jdbcTemplate.update(super.deleteSql(clazz, delete, false));
    }

    @Transactional(readOnly = false)
    @Override
    public long delete(Delete delete, boolean ignore) {
        return jdbcTemplate.update(super.deleteSql(clazz, delete, ignore));
    }

    @Transactional(readOnly = false)
    @Override
    public long logicallyDeleteById(Object id) {
        return jdbcTemplate.update(super.logicallyDeleteByIdSql(clazz, id));
    }

    @Transactional(readOnly = false)
    @Override
    public long logicallyDeleteByCondition(String where, Object... args) {
        return jdbcTemplate.update(super.logicallyDeleteByConditionSql(clazz, where, args));
    }

    @Transactional(readOnly = false)
    @Override
    public long update(Update update) {
        return jdbcTemplate.update(super.updateSql(update, false));
    }

    @Transactional(readOnly = false)
    @Override
    public long update(Update update, boolean ignore) {
        return jdbcTemplate.update(super.updateSql(update, ignore));
    }

    @Transactional(readOnly = false)
    @Override
    public long updateById(T bean, Object id, boolean updateNotNull) {
        return jdbcTemplate.update(super.updateByIdSql(bean, id, updateNotNull, null));
    }

    @Transactional(readOnly = false)
    @Override
    public long updateById(T bean, Object id, boolean updateNotNull, String[] filterFields) {
        return jdbcTemplate.update(super.updateByIdSql(bean, id, updateNotNull, filterFields));
    }

    @Transactional(readOnly = false)
    @Override
    public long updateByBeanId(T bean, boolean updateNotNull) {
        return jdbcTemplate.update(super.updateByBeanIdSql(bean, updateNotNull, null));
    }

    @Transactional(readOnly = false)
    @Override
    public long updateByBeanId(T bean, boolean updateNotNull, String[] filterFields) {
        return jdbcTemplate.update(super.updateByBeanIdSql(bean, updateNotNull, filterFields));
    }

    @Transactional(readOnly = false)
    @Override
    public long updateByCondition(T bean, boolean updateNotNull, String where, Object... args) {
        return jdbcTemplate.update(super.updateByConditionSql(bean, updateNotNull, null, where, args));
    }

    @Transactional(readOnly = false)
    @Override
    public long updateByCondition(T bean, boolean updateNotNull, String[] filterFields, String where, Object... args) {
        return jdbcTemplate.update(super.updateByConditionSql(bean, updateNotNull, filterFields, where, args));
    }

    @Transactional(readOnly = false)
    @Override
    public long updateByBeanCondition(T bean, boolean updateNotNull, String where) {
        return jdbcTemplate.update(super.updateByBeanConditionSql(bean, updateNotNull, null, where));
    }

    @Transactional(readOnly = false)
    @Override
    public long updateByBeanCondition(T bean, boolean updateNotNull, String[] filterFields, String where) {
        return jdbcTemplate.update(super.updateByBeanConditionSql(bean, updateNotNull, filterFields, where));
    }

    @Transactional(readOnly = false)
    @Override
    public long insert(T... bean) {
        return jdbcTemplate.update(super.insertBeanSql(bean));
    }

    @Transactional(readOnly = false)
    @Override
    public long insert(List<T> beanList) {
        return jdbcTemplate.update(super.insertBeanSql(beanList));
    }

    @Transactional(readOnly = false)
    @Override
    public long inset(Insert insert) {
        return jdbcTemplate.update(super.insertBeanSql(insert));
    }
}
