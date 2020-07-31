package cn.vonce.sql.spring.service;

import cn.vonce.sql.bean.*;
import cn.vonce.sql.config.SqlBeanConfig;
import cn.vonce.sql.spring.config.UseMybatis;
import cn.vonce.sql.spring.dao.MybatisSqlBeanDao;
import cn.vonce.sql.service.SqlBeanService;
import cn.vonce.sql.service.TableService;
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
public class MybatisSqlBeanServiceImpl<T, ID> implements SqlBeanService<T, ID> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private TableService tableService;

    @Autowired
    private MybatisSqlBeanDao<T> mybatisSqlBeanDao;

    @Autowired
    private SqlBeanConfig sqlBeanConfig;

    public Class<?> clazz;

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
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public T selectById(ID id) {
        if (id == null) {
            return null;
        }
        return mybatisSqlBeanDao.selectById(sqlBeanConfig, clazz, id);
    }

    @Override
    public <O> O selectById(Class<O> returnType, ID id) {
        if (id == null) {
            return null;
        }
        if (SqlBeanUtil.isBaseType(returnType.getName()) || SqlBeanUtil.isMap(returnType.getName())) {
            return mybatisSqlBeanDao.selectByIdO(sqlBeanConfig, clazz, returnType, id);
        }
        return mybatisSqlBeanDao.selectByIdO(sqlBeanConfig, returnType, returnType, id);
    }

    @Override
    public List<T> selectByIds(ID... ids) {
        if (ids == null || ids.length == 0) {
            return null;
        }
        return mybatisSqlBeanDao.selectByIds(sqlBeanConfig, clazz, ids);
    }

    @Override
    public <O> List<O> selectByIds(Class<O> returnType, ID... ids) {
        if (ids == null || ids.length == 0) {
            return null;
        }
        if (SqlBeanUtil.isBaseType(returnType.getName()) || SqlBeanUtil.isMap(returnType.getName())) {
            return mybatisSqlBeanDao.selectByIdsO(sqlBeanConfig, clazz, returnType, ids);
        }
        return mybatisSqlBeanDao.selectByIdsO(sqlBeanConfig, returnType, returnType, ids);
    }

    @Override
    public T selectOne(Select select) {
        return mybatisSqlBeanDao.selectOne(sqlBeanConfig, clazz, select);
    }

    @Override
    public <O> O selectOne(Class<O> returnType, Select select) {
        if (SqlBeanUtil.isBaseType(returnType.getName()) || SqlBeanUtil.isMap(returnType.getName())) {
            return mybatisSqlBeanDao.selectOneO(sqlBeanConfig, clazz, returnType, select);
        }
        return mybatisSqlBeanDao.selectOneO(sqlBeanConfig, returnType, returnType, select);
    }

    @Override
    public Map<String, Object> selectMap(Select select) {
        return mybatisSqlBeanDao.selectMap(sqlBeanConfig, clazz, select);
    }

    @Override
    public T selectOneByCondition(String where, Object... args) {
        return mybatisSqlBeanDao.selectOneByCondition(sqlBeanConfig, clazz, where, args);
    }

    @Override
    public <O> O selectOneByCondition(Class<O> returnType, String where, Object... args) {
        if (SqlBeanUtil.isBaseType(returnType.getName()) || SqlBeanUtil.isMap(returnType.getName())) {
            return mybatisSqlBeanDao.selectOneByConditionO(sqlBeanConfig, clazz, returnType, where, args);
        }
        return mybatisSqlBeanDao.selectOneByConditionO(sqlBeanConfig, returnType, returnType, where, args);
    }

    @Override
    public <O> List<O> selectByCondition(Class<O> returnType, String where, Object... args) {
        if (SqlBeanUtil.isBaseType(returnType.getName()) || SqlBeanUtil.isMap(returnType.getName())) {
            return mybatisSqlBeanDao.selectByConditionO(sqlBeanConfig, clazz, returnType, null, where, args);
        }
        return mybatisSqlBeanDao.selectByConditionO(sqlBeanConfig, returnType, returnType, null, where, args);
    }

    @Override
    public <O> List<O> selectByCondition(Class<O> returnType, Paging paging, String where, Object... args) {
        if (SqlBeanUtil.isBaseType(returnType.getName()) || SqlBeanUtil.isMap(returnType.getName())) {
            return mybatisSqlBeanDao.selectByConditionO(sqlBeanConfig, clazz, returnType, paging, where, args);
        }
        return mybatisSqlBeanDao.selectByConditionO(sqlBeanConfig, returnType, returnType, paging, where, args);
    }

    @Override
    public List<T> selectByCondition(String where, Object... args) {
        return mybatisSqlBeanDao.selectByCondition(sqlBeanConfig, clazz, null, where, args);
    }

    @Override
    public List<T> selectByCondition(Paging paging, String where, Object... args) {
        return mybatisSqlBeanDao.selectByCondition(sqlBeanConfig, clazz, paging, where, args);
    }

    public long selectCountByCondition(String where, Object... args) {
        return mybatisSqlBeanDao.selectCountByCondition(sqlBeanConfig, clazz, where, args);
    }

    @Override
    public long countAll() {
        return mybatisSqlBeanDao.selectCountByCondition(sqlBeanConfig, clazz, null, null);
    }

    @Override
    public List<T> selectAll() {
        return mybatisSqlBeanDao.selectAll(sqlBeanConfig, clazz, null);
    }

    @Override
    public List<T> selectAll(Paging paging) {
        return mybatisSqlBeanDao.selectAll(sqlBeanConfig, clazz, paging);
    }

    @Override
    public <O> List<O> selectAll(Class<O> returnType) {
        if (SqlBeanUtil.isBaseType(returnType.getName()) || SqlBeanUtil.isMap(returnType.getName())) {
            return mybatisSqlBeanDao.selectAllO(sqlBeanConfig, clazz, returnType, null);
        }
        return mybatisSqlBeanDao.selectAllO(sqlBeanConfig, returnType, returnType, null);
    }

    @Override
    public <O> List<O> selectAll(Class<O> returnType, Paging paging) {
        if (SqlBeanUtil.isBaseType(returnType.getName()) || SqlBeanUtil.isMap(returnType.getName())) {
            return mybatisSqlBeanDao.selectAllO(sqlBeanConfig, clazz, returnType, paging);
        }
        return mybatisSqlBeanDao.selectAllO(sqlBeanConfig, returnType, returnType, paging);
    }

    @Override
    public List<Map<String, Object>> selectMapList(Select select) {
        return mybatisSqlBeanDao.selectMapList(sqlBeanConfig, clazz, select);
    }

    @Override
    public <O> List<O> select(Class<O> returnType, Select select) {
        if (SqlBeanUtil.isBaseType(returnType.getName()) || SqlBeanUtil.isMap(returnType.getName())) {
            return mybatisSqlBeanDao.selectO(sqlBeanConfig, clazz, returnType, select);
        }
        return mybatisSqlBeanDao.selectO(sqlBeanConfig, returnType, returnType, select);
    }

    @Override
    public List<T> select(Select select) {
        return mybatisSqlBeanDao.select(sqlBeanConfig, clazz, select);
    }

    @Override
    public long count(Select select) {
        return mybatisSqlBeanDao.count(sqlBeanConfig, clazz, select);
    }

    @Override
    public long count(Class<?> clazz, Select select) {
        return mybatisSqlBeanDao.count(sqlBeanConfig, clazz, select);
    }

    @Override
    public long deleteById(ID... id) {
        return mybatisSqlBeanDao.deleteById(sqlBeanConfig, clazz, id);
    }

    @Override
    public long deleteByCondition(String where, Object... args) {
        return mybatisSqlBeanDao.deleteByCondition(sqlBeanConfig, clazz, where, args);
    }

    @Override
    public long delete(Delete delete) {
        return mybatisSqlBeanDao.delete(sqlBeanConfig, clazz, delete, false);
    }

    @Override
    public long delete(Delete delete, boolean ignore) {
        return mybatisSqlBeanDao.delete(sqlBeanConfig, clazz, delete, ignore);
    }

    @Override
    public long logicallyDeleteById(ID id) {
        return mybatisSqlBeanDao.logicallyDeleteById(sqlBeanConfig, clazz, id);
    }

    @Override
    public long logicallyDeleteByCondition(String where, Object... args) {
        return mybatisSqlBeanDao.logicallyDeleteByCondition(sqlBeanConfig, clazz, where, args);
    }

    @Override
    public long update(Update update) {
        return mybatisSqlBeanDao.update(sqlBeanConfig, update, false);
    }

    @Override
    public long update(Update update, boolean ignore) {
        return mybatisSqlBeanDao.update(sqlBeanConfig, update, ignore);
    }

    @Override
    public long updateById(T bean, ID id, boolean updateNotNull) {
        return mybatisSqlBeanDao.updateById(sqlBeanConfig, bean, id, updateNotNull, null);
    }

    @Override
    public long updateById(T bean, ID id, boolean updateNotNull, String[] filterFields) {
        return mybatisSqlBeanDao.updateById(sqlBeanConfig, bean, id, updateNotNull, filterFields);
    }

    @Override
    public long updateByBeanId(T bean, boolean updateNotNull) {
        return mybatisSqlBeanDao.updateByBeanId(sqlBeanConfig, bean, updateNotNull, null);
    }

    @Override
    public long updateByBeanId(T bean, boolean updateNotNull, String[] filterFields) {
        return mybatisSqlBeanDao.updateByBeanId(sqlBeanConfig, bean, updateNotNull, filterFields);
    }

    @Override
    public long updateByCondition(T bean, boolean updateNotNull, String where, Object... args) {
        return mybatisSqlBeanDao.updateByCondition(sqlBeanConfig, bean, updateNotNull, null, where, args);
    }

    @Override
    public long updateByCondition(T bean, boolean updateNotNull, String[] filterFields, String where, Object... args) {
        return mybatisSqlBeanDao.updateByCondition(sqlBeanConfig, bean, updateNotNull, filterFields, where, args);
    }

    @Override
    public long updateByBeanCondition(T bean, boolean updateNotNull, String where) {
        return mybatisSqlBeanDao.updateByBeanCondition(sqlBeanConfig, bean, updateNotNull, null, where);
    }

    @Override
    public long updateByBeanCondition(T bean, boolean updateNotNull, String[] filterFields, String where) {
        return mybatisSqlBeanDao.updateByBeanCondition(sqlBeanConfig, bean, updateNotNull, filterFields, where);
    }

    @SuppressWarnings("unchecked")
    @Override
    public long insert(T... bean) {
        return mybatisSqlBeanDao.insertBean(sqlBeanConfig, Arrays.asList(bean));
    }

    @Override
    public long insert(List<T> beanList) {
        return mybatisSqlBeanDao.insertBean(sqlBeanConfig, beanList);
    }

    @Override
    public long inset(Insert insert) {
        return mybatisSqlBeanDao.insert(sqlBeanConfig, insert);
    }

    @Override
    public TableService getTableService() {
        if (tableService == null) {
            tableService = new TableService() {
                @Override
                public long dropTable() {
                    return mybatisSqlBeanDao.drop(sqlBeanConfig, clazz);
                }

                @Override
                public long createTable() {
                    dropTable();
                    return mybatisSqlBeanDao.create(sqlBeanConfig, clazz);
                }
            };
        }
        return tableService;
    }
}
