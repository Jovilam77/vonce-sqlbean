package cn.vonce.sql.orm.service.impl;

import cn.vonce.sql.bean.*;
import cn.vonce.sql.config.UseMybatis;
import cn.vonce.sql.orm.dao.MybatisSqlBeanDao;
import cn.vonce.sql.orm.service.SqlBeanService;
import cn.vonce.sql.uitls.SqlBeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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
public class MybatisSqlBeanServiceImpl<T> implements SqlBeanService<T> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Autowired
    public MybatisSqlBeanDao<T> mybatisSqlBeanDao;

    public Class<?> clazz;

    public MybatisSqlBeanServiceImpl() {
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
        return mybatisSqlBeanDao.selectById(clazz, id);
    }

    @Override
    public <O> O selectById(Class<O> returnType, Object id) {
        if (SqlBeanUtil.isBaseType(returnType.getName()) || SqlBeanUtil.isMap(returnType.getName())) {
            return mybatisSqlBeanDao.selectByIdO(clazz, returnType, id);
        }
        return mybatisSqlBeanDao.selectByIdO(returnType, returnType, id);
    }

    @Override
    public List<T> selectByIds(Object[] ids) {
        return mybatisSqlBeanDao.selectByIds(clazz, ids);
    }

    @Override
    public <O> List<O> selectByIds(Class<O> returnType, Object[] ids) {
        if (SqlBeanUtil.isBaseType(returnType.getName()) || SqlBeanUtil.isMap(returnType.getName())) {
            return mybatisSqlBeanDao.selectByIdsO(clazz, returnType, ids);
        }
        return mybatisSqlBeanDao.selectByIdsO(returnType, returnType, ids);
    }

    @Override
    public T selectOne(Select select) {
        return mybatisSqlBeanDao.selectOne(clazz, select);
    }

    @Override
    public <O> O selectOne(Class<O> returnType, Select select) {
        if (SqlBeanUtil.isBaseType(returnType.getName()) || SqlBeanUtil.isMap(returnType.getName())) {
            return mybatisSqlBeanDao.selectOneO(clazz, returnType, select);
        }
        return mybatisSqlBeanDao.selectOneO(returnType, returnType, select);
    }

    @Override
    public Map<String, Object> selectMap(Select select) {
        return mybatisSqlBeanDao.selectMap(clazz, select);
    }

    @Override
    public T selectOneByCondition(String where, Object... args) {
        return mybatisSqlBeanDao.selectOneByCondition(clazz, where, args);
    }

    @Override
    public <O> O selectOneByCondition(Class<O> returnType, String where, Object... args) {
        if (SqlBeanUtil.isBaseType(returnType.getName()) || SqlBeanUtil.isMap(returnType.getName())) {
            return mybatisSqlBeanDao.selectOneByConditionO(clazz, returnType, where, args);
        }
        return mybatisSqlBeanDao.selectOneByConditionO(returnType, returnType, where, args);
    }

    @Override
    public <O> List<O> selectByCondition(Class<O> returnType, String where, Object... args) {
        if (SqlBeanUtil.isBaseType(returnType.getName()) || SqlBeanUtil.isMap(returnType.getName())) {
            return mybatisSqlBeanDao.selectByConditionO(clazz, returnType, null, where, args);
        }
        return mybatisSqlBeanDao.selectByConditionO(returnType, returnType, null, where, args);
    }

    @Override
    public <O> List<O> selectByCondition(Class<O> returnType, Paging paging, String where, Object... args) {
        if (SqlBeanUtil.isBaseType(returnType.getName()) || SqlBeanUtil.isMap(returnType.getName())) {
            return mybatisSqlBeanDao.selectByConditionO(clazz, returnType, paging, where, args);
        }
        return mybatisSqlBeanDao.selectByConditionO(returnType, returnType, paging, where, args);
    }

    @Override
    public List<T> selectByCondition(String where, Object... args) {
        return mybatisSqlBeanDao.selectByCondition(clazz, null, where, args);
    }

    @Override
    public List<T> selectByCondition(Paging paging, String where, Object... args) {
        return mybatisSqlBeanDao.selectByCondition(clazz, paging, where, args);
    }

    public long selectCountByCondition(String where, Object... args) {
        return mybatisSqlBeanDao.selectCountByCondition(clazz, where, args);
    }

    @Override
    public long countAll() {
        return mybatisSqlBeanDao.selectCountByCondition(clazz, null, null);
    }

    @Override
    public List<T> selectAll() {
        return mybatisSqlBeanDao.selectAll(clazz, null);
    }

    @Override
    public List<T> selectAll(Paging paging) {
        return mybatisSqlBeanDao.selectAll(clazz, paging);
    }

    @Override
    public <O> List<O> selectAll(Class<O> returnType) {
        if (SqlBeanUtil.isBaseType(returnType.getName()) || SqlBeanUtil.isMap(returnType.getName())) {
            return mybatisSqlBeanDao.selectAllO(clazz, returnType, null);
        }
        return mybatisSqlBeanDao.selectAllO(returnType, returnType, null);
    }

    @Override
    public <O> List<O> selectAll(Class<O> returnType, Paging paging) {
        if (SqlBeanUtil.isBaseType(returnType.getName()) || SqlBeanUtil.isMap(returnType.getName())) {
            return mybatisSqlBeanDao.selectAllO(clazz, returnType, paging);
        }
        return mybatisSqlBeanDao.selectAllO(returnType, returnType, paging);
    }

    @Override
    public List<Map<String, Object>> selectMapList(Select select) {
        return mybatisSqlBeanDao.selectMapList(clazz, select);
    }

    @Override
    public <O> List<O> select(Class<O> returnType, Select select) {
        if (SqlBeanUtil.isBaseType(returnType.getName()) || SqlBeanUtil.isMap(returnType.getName())) {
            return mybatisSqlBeanDao.selectO(clazz, returnType, select);
        }
        return mybatisSqlBeanDao.selectO(returnType, returnType, select);
    }

    @Override
    public List<T> select(Select select) {
        return mybatisSqlBeanDao.select(clazz, select);
    }

    @Override
    public long count(Select select) {
        return mybatisSqlBeanDao.count(clazz, select);
    }

    @Override
    public long count(Class<?> clazz, Select select) {
        return mybatisSqlBeanDao.count(clazz, select);
    }

    @Override
    public long deleteById(Object... id) {
        return mybatisSqlBeanDao.deleteById(clazz, id);
    }

    @Override
    public long deleteByCondition(String where, Object... args) {
        return mybatisSqlBeanDao.deleteByCondition(clazz, where, args);
    }

    @Override
    public long delete(Delete delete) {
        return mybatisSqlBeanDao.delete(clazz, delete, false);
    }

    @Override
    public long delete(Delete delete, boolean ignore) {
        return mybatisSqlBeanDao.delete(clazz, delete, ignore);
    }

    @Override
    public long logicallyDeleteById(Object id) {
        return mybatisSqlBeanDao.logicallyDeleteById(clazz, id);
    }

    @Override
    public long logicallyDeleteByCondition(String where, Object... args) {
        return mybatisSqlBeanDao.logicallyDeleteByCondition(clazz, where, args);
    }

    @Override
    public long update(Update update) {
        return mybatisSqlBeanDao.update(update, false);
    }

    @Override
    public long update(Update update, boolean ignore) {
        return mybatisSqlBeanDao.update(update, ignore);
    }

    @Override
    public long updateById(T bean, Object id, boolean updateNotNull) {
        return mybatisSqlBeanDao.updateById(bean, id, updateNotNull, null);
    }

    @Override
    public long updateById(T bean, Object id, boolean updateNotNull, String[] filterFields) {
        return mybatisSqlBeanDao.updateById(bean, id, updateNotNull, filterFields);
    }

    @Override
    public long updateByBeanId(T bean, boolean updateNotNull) {
        return mybatisSqlBeanDao.updateByBeanId(bean, updateNotNull, null);
    }

    @Override
    public long updateByBeanId(T bean, boolean updateNotNull, String[] filterFields) {
        return mybatisSqlBeanDao.updateByBeanId(bean, updateNotNull, filterFields);
    }

    @Override
    public long updateByCondition(T bean, boolean updateNotNull, String where, Object... args) {
        return mybatisSqlBeanDao.updateByCondition(bean, updateNotNull, null, where, args);
    }

    @Override
    public long updateByCondition(T bean, boolean updateNotNull, String[] filterFields, String where, Object... args) {
        return mybatisSqlBeanDao.updateByCondition(bean, updateNotNull, filterFields, where, args);
    }

    @Override
    public long updateByBeanCondition(T bean, boolean updateNotNull, String where) {
        return mybatisSqlBeanDao.updateByBeanCondition(bean, updateNotNull, null, where);
    }

    @Override
    public long updateByBeanCondition(T bean, boolean updateNotNull, String[] filterFields, String where) {
        return mybatisSqlBeanDao.updateByBeanCondition(bean, updateNotNull, filterFields, where);
    }

    @SuppressWarnings("unchecked")
    @Override
    public long insert(T... bean) {
        return mybatisSqlBeanDao.insertBean(Arrays.asList(bean));
    }

    @Override
    public long insert(List<T> beanList) {
        return mybatisSqlBeanDao.insertBean(beanList);
    }

    @Override
    public long inset(Insert insert) {
        return mybatisSqlBeanDao.insert(insert);
    }

}
