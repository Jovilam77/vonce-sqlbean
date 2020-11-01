package cn.vonce.sql.spring.service;

import cn.vonce.sql.bean.*;
import cn.vonce.sql.enumerate.DbType;
import cn.vonce.sql.spring.annotation.DbReadOrWrite;
import cn.vonce.sql.spring.config.UseMybatis;
import cn.vonce.sql.spring.dao.MybatisSqlBeanDao;
import cn.vonce.sql.service.SqlBeanService;
import cn.vonce.sql.service.TableService;
import cn.vonce.sql.uitls.DateUtil;
import cn.vonce.sql.uitls.SqlBeanUtil;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.Arrays;
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
    private SqlSession sqlSession;

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
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public String getProductName() {
        try {
            return sqlSession.getConfiguration().getEnvironment().getDataSource().getConnection().getMetaData().getDatabaseProductName();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    @Override
    public Class<?> getBeanClass() {
        return clazz;
    }

    @DbReadOrWrite(DbReadOrWrite.Type.READ)
    @Override
    public T selectById(ID id) {
        if (id == null) {
            return null;
        }
        return mybatisSqlBeanDao.selectById(DbType.getSqlBeanConfig(getProductName()), clazz, id);
    }

    @DbReadOrWrite(DbReadOrWrite.Type.READ)
    @Override
    public <O> O selectById(Class<O> returnType, ID id) {
        if (id == null) {
            return null;
        }
        if (SqlBeanUtil.isBaseType(returnType.getName()) || SqlBeanUtil.isMap(returnType.getName())) {
            return mybatisSqlBeanDao.selectByIdO(DbType.getSqlBeanConfig(getProductName()), clazz, returnType, id);
        }
        return mybatisSqlBeanDao.selectByIdO(DbType.getSqlBeanConfig(getProductName()), returnType, returnType, id);
    }

    @DbReadOrWrite(DbReadOrWrite.Type.READ)
    @Override
    public List<T> selectByIds(ID... ids) {
        if (ids == null || ids.length == 0) {
            return null;
        }
        return mybatisSqlBeanDao.selectByIds(DbType.getSqlBeanConfig(getProductName()), clazz, ids);
    }

    @DbReadOrWrite(DbReadOrWrite.Type.READ)
    @Override
    public <O> List<O> selectByIds(Class<O> returnType, ID... ids) {
        if (ids == null || ids.length == 0) {
            return null;
        }
        if (SqlBeanUtil.isBaseType(returnType.getName()) || SqlBeanUtil.isMap(returnType.getName())) {
            return mybatisSqlBeanDao.selectByIdsO(DbType.getSqlBeanConfig(getProductName()), clazz, returnType, ids);
        }
        return mybatisSqlBeanDao.selectByIdsO(DbType.getSqlBeanConfig(getProductName()), returnType, returnType, ids);
    }

    @DbReadOrWrite(DbReadOrWrite.Type.READ)
    @Override
    public T selectOne(Select select) {
        return mybatisSqlBeanDao.selectOne(DbType.getSqlBeanConfig(getProductName()), clazz, select);
    }

    @DbReadOrWrite(DbReadOrWrite.Type.READ)
    @Override
    public <O> O selectOne(Class<O> returnType, Select select) {
        if (SqlBeanUtil.isBaseType(returnType.getName()) || SqlBeanUtil.isMap(returnType.getName())) {
            return mybatisSqlBeanDao.selectOneO(DbType.getSqlBeanConfig(getProductName()), clazz, returnType, select);
        }
        return mybatisSqlBeanDao.selectOneO(DbType.getSqlBeanConfig(getProductName()), returnType, returnType, select);
    }

    @DbReadOrWrite(DbReadOrWrite.Type.READ)
    @Override
    public Map<String, Object> selectMap(Select select) {
        return mybatisSqlBeanDao.selectMap(DbType.getSqlBeanConfig(getProductName()), clazz, select);
    }

    @DbReadOrWrite(DbReadOrWrite.Type.READ)
    @Override
    public T selectOneByCondition(String where, Object... args) {
        return mybatisSqlBeanDao.selectOneByCondition(DbType.getSqlBeanConfig(getProductName()), clazz, where, args);
    }

    @DbReadOrWrite(DbReadOrWrite.Type.READ)
    @Override
    public <O> O selectOneByCondition(Class<O> returnType, String where, Object... args) {
        if (SqlBeanUtil.isBaseType(returnType.getName()) || SqlBeanUtil.isMap(returnType.getName())) {
            return mybatisSqlBeanDao.selectOneByConditionO(DbType.getSqlBeanConfig(getProductName()), clazz, returnType, where, args);
        }
        return mybatisSqlBeanDao.selectOneByConditionO(DbType.getSqlBeanConfig(getProductName()), returnType, returnType, where, args);
    }

    @DbReadOrWrite(DbReadOrWrite.Type.READ)
    @Override
    public <O> List<O> selectByCondition(Class<O> returnType, String where, Object... args) {
        if (SqlBeanUtil.isBaseType(returnType.getName()) || SqlBeanUtil.isMap(returnType.getName())) {
            return mybatisSqlBeanDao.selectByConditionO(DbType.getSqlBeanConfig(getProductName()), clazz, returnType, null, where, args);
        }
        return mybatisSqlBeanDao.selectByConditionO(DbType.getSqlBeanConfig(getProductName()), returnType, returnType, null, where, args);
    }

    @DbReadOrWrite(DbReadOrWrite.Type.READ)
    @Override
    public <O> List<O> selectByCondition(Class<O> returnType, Paging paging, String where, Object... args) {
        if (SqlBeanUtil.isBaseType(returnType.getName()) || SqlBeanUtil.isMap(returnType.getName())) {
            return mybatisSqlBeanDao.selectByConditionO(DbType.getSqlBeanConfig(getProductName()), clazz, returnType, paging, where, args);
        }
        return mybatisSqlBeanDao.selectByConditionO(DbType.getSqlBeanConfig(getProductName()), returnType, returnType, paging, where, args);
    }

    @DbReadOrWrite(DbReadOrWrite.Type.READ)
    @Override
    public List<T> selectByCondition(String where, Object... args) {
        return mybatisSqlBeanDao.selectByCondition(DbType.getSqlBeanConfig(getProductName()), clazz, null, where, args);
    }

    @DbReadOrWrite(DbReadOrWrite.Type.READ)
    @Override
    public List<T> selectByCondition(Paging paging, String where, Object... args) {
        return mybatisSqlBeanDao.selectByCondition(DbType.getSqlBeanConfig(getProductName()), clazz, paging, where, args);
    }

    @DbReadOrWrite(DbReadOrWrite.Type.READ)
    @Override
    public long selectCountByCondition(String where, Object... args) {
        return mybatisSqlBeanDao.selectCountByCondition(DbType.getSqlBeanConfig(getProductName()), clazz, where, args);
    }

    @DbReadOrWrite(DbReadOrWrite.Type.READ)
    @Override
    public long countAll() {
        return mybatisSqlBeanDao.selectCountByCondition(DbType.getSqlBeanConfig(getProductName()), clazz, null, null);
    }

    @DbReadOrWrite(DbReadOrWrite.Type.READ)
    @Override
    public List<T> selectAll() {
        return mybatisSqlBeanDao.selectAll(DbType.getSqlBeanConfig(getProductName()), clazz, null);
    }

    @DbReadOrWrite(DbReadOrWrite.Type.READ)
    @Override
    public List<T> selectAll(Paging paging) {
        return mybatisSqlBeanDao.selectAll(DbType.getSqlBeanConfig(getProductName()), clazz, paging);
    }

    @DbReadOrWrite(DbReadOrWrite.Type.READ)
    @Override
    public <O> List<O> selectAll(Class<O> returnType) {
        if (SqlBeanUtil.isBaseType(returnType.getName()) || SqlBeanUtil.isMap(returnType.getName())) {
            return mybatisSqlBeanDao.selectAllO(DbType.getSqlBeanConfig(getProductName()), clazz, returnType, null);
        }
        return mybatisSqlBeanDao.selectAllO(DbType.getSqlBeanConfig(getProductName()), returnType, returnType, null);
    }

    @DbReadOrWrite(DbReadOrWrite.Type.READ)
    @Override
    public <O> List<O> selectAll(Class<O> returnType, Paging paging) {
        if (SqlBeanUtil.isBaseType(returnType.getName()) || SqlBeanUtil.isMap(returnType.getName())) {
            return mybatisSqlBeanDao.selectAllO(DbType.getSqlBeanConfig(getProductName()), clazz, returnType, paging);
        }
        return mybatisSqlBeanDao.selectAllO(DbType.getSqlBeanConfig(getProductName()), returnType, returnType, paging);
    }

    @DbReadOrWrite(DbReadOrWrite.Type.READ)
    @Override
    public List<Map<String, Object>> selectMapList(Select select) {
        return mybatisSqlBeanDao.selectMapList(DbType.getSqlBeanConfig(getProductName()), clazz, select);
    }

    @DbReadOrWrite(DbReadOrWrite.Type.READ)
    @Override
    public <O> List<O> select(Class<O> returnType, Select select) {
        if (SqlBeanUtil.isBaseType(returnType.getName()) || SqlBeanUtil.isMap(returnType.getName())) {
            return mybatisSqlBeanDao.selectO(DbType.getSqlBeanConfig(getProductName()), clazz, returnType, select);
        }
        return mybatisSqlBeanDao.selectO(DbType.getSqlBeanConfig(getProductName()), returnType, returnType, select);
    }

    @DbReadOrWrite(DbReadOrWrite.Type.READ)
    @Override
    public List<T> select(Select select) {
        return mybatisSqlBeanDao.select(DbType.getSqlBeanConfig(getProductName()), clazz, select);
    }

    @DbReadOrWrite(DbReadOrWrite.Type.READ)
    @Override
    public long count(Select select) {
        return mybatisSqlBeanDao.count(DbType.getSqlBeanConfig(getProductName()), clazz, select);
    }

    @DbReadOrWrite(DbReadOrWrite.Type.READ)
    @Override
    public long count(Class<?> clazz, Select select) {
        return mybatisSqlBeanDao.count(DbType.getSqlBeanConfig(getProductName()), clazz, select);
    }

    @DbReadOrWrite(DbReadOrWrite.Type.WRITE)
    @Override
    public long deleteById(ID... id) {
        return mybatisSqlBeanDao.deleteById(DbType.getSqlBeanConfig(getProductName()), clazz, id);
    }

    @DbReadOrWrite(DbReadOrWrite.Type.WRITE)
    @Override
    public long deleteByCondition(String where, Object... args) {
        return mybatisSqlBeanDao.deleteByCondition(DbType.getSqlBeanConfig(getProductName()), clazz, where, args);
    }

    @DbReadOrWrite(DbReadOrWrite.Type.WRITE)
    @Override
    public long delete(Delete delete) {
        return mybatisSqlBeanDao.delete(DbType.getSqlBeanConfig(getProductName()), clazz, delete, false);
    }

    @DbReadOrWrite(DbReadOrWrite.Type.WRITE)
    @Override
    public long delete(Delete delete, boolean ignore) {
        return mybatisSqlBeanDao.delete(DbType.getSqlBeanConfig(getProductName()), clazz, delete, ignore);
    }

    @DbReadOrWrite(DbReadOrWrite.Type.WRITE)
    @Override
    public long logicallyDeleteById(ID id) {
        return mybatisSqlBeanDao.logicallyDeleteById(DbType.getSqlBeanConfig(getProductName()), clazz, id);
    }

    @DbReadOrWrite(DbReadOrWrite.Type.WRITE)
    @Override
    public long logicallyDeleteByCondition(String where, Object... args) {
        return mybatisSqlBeanDao.logicallyDeleteByCondition(DbType.getSqlBeanConfig(getProductName()), clazz, where, args);
    }

    @DbReadOrWrite(DbReadOrWrite.Type.WRITE)
    @Override
    public long update(Update update) {
        return mybatisSqlBeanDao.update(DbType.getSqlBeanConfig(getProductName()), update, false);
    }

    @DbReadOrWrite(DbReadOrWrite.Type.WRITE)
    @Override
    public long update(Update update, boolean ignore) {
        return mybatisSqlBeanDao.update(DbType.getSqlBeanConfig(getProductName()), update, ignore);
    }

    @DbReadOrWrite(DbReadOrWrite.Type.WRITE)
    @Override
    public long updateById(T bean, ID id, boolean updateNotNull) {
        return mybatisSqlBeanDao.updateById(DbType.getSqlBeanConfig(getProductName()), bean, id, updateNotNull, null);
    }

    @DbReadOrWrite(DbReadOrWrite.Type.WRITE)
    @Override
    public long updateById(T bean, ID id, boolean updateNotNull, String[] filterFields) {
        return mybatisSqlBeanDao.updateById(DbType.getSqlBeanConfig(getProductName()), bean, id, updateNotNull, filterFields);
    }

    @DbReadOrWrite(DbReadOrWrite.Type.WRITE)
    @Override
    public long updateByBeanId(T bean, boolean updateNotNull) {
        return mybatisSqlBeanDao.updateByBeanId(DbType.getSqlBeanConfig(getProductName()), bean, updateNotNull, null);
    }

    @DbReadOrWrite(DbReadOrWrite.Type.WRITE)
    @Override
    public long updateByBeanId(T bean, boolean updateNotNull, String[] filterFields) {
        return mybatisSqlBeanDao.updateByBeanId(DbType.getSqlBeanConfig(getProductName()), bean, updateNotNull, filterFields);
    }

    @DbReadOrWrite(DbReadOrWrite.Type.WRITE)
    @Override
    public long updateByCondition(T bean, boolean updateNotNull, String where, Object... args) {
        return mybatisSqlBeanDao.updateByCondition(DbType.getSqlBeanConfig(getProductName()), bean, updateNotNull, null, where, args);
    }

    @DbReadOrWrite(DbReadOrWrite.Type.WRITE)
    @Override
    public long updateByCondition(T bean, boolean updateNotNull, String[] filterFields, String where, Object... args) {
        return mybatisSqlBeanDao.updateByCondition(DbType.getSqlBeanConfig(getProductName()), bean, updateNotNull, filterFields, where, args);
    }

    @DbReadOrWrite(DbReadOrWrite.Type.WRITE)
    @Override
    public long updateByBeanCondition(T bean, boolean updateNotNull, String where) {
        return mybatisSqlBeanDao.updateByBeanCondition(DbType.getSqlBeanConfig(getProductName()), bean, updateNotNull, null, where);
    }

    @DbReadOrWrite(DbReadOrWrite.Type.WRITE)
    @Override
    public long updateByBeanCondition(T bean, boolean updateNotNull, String[] filterFields, String where) {
        return mybatisSqlBeanDao.updateByBeanCondition(DbType.getSqlBeanConfig(getProductName()), bean, updateNotNull, filterFields, where);
    }

    @SuppressWarnings("unchecked")
    @DbReadOrWrite(DbReadOrWrite.Type.WRITE)
    @Override
    public long insert(T... bean) {
        return mybatisSqlBeanDao.insertBean(DbType.getSqlBeanConfig(getProductName()), Arrays.asList(bean));
    }

    @DbReadOrWrite(DbReadOrWrite.Type.WRITE)
    @Override
    public long insert(List<T> beanList) {
        return mybatisSqlBeanDao.insertBean(DbType.getSqlBeanConfig(getProductName()), beanList);
    }

    @DbReadOrWrite(DbReadOrWrite.Type.WRITE)
    @Override
    public long inset(Insert insert) {
        return mybatisSqlBeanDao.insert(DbType.getSqlBeanConfig(getProductName()), insert);
    }

    @DbReadOrWrite(DbReadOrWrite.Type.WRITE)
    @Override
    public String backup() {
        String targetTableName = SqlBeanUtil.getTable(clazz).getName() + "_" + DateUtil.dateToString(new Date(), "yyyyMMddHHmmss");
        try {
            mybatisSqlBeanDao.backup(DbType.getSqlBeanConfig(getProductName()), clazz, targetTableName, null, null);
        } catch (Exception e) {
            return null;
        }
        return targetTableName;
    }

    @DbReadOrWrite(DbReadOrWrite.Type.WRITE)
    @Override
    public void backup(String targetTableName) {
        mybatisSqlBeanDao.backup(DbType.getSqlBeanConfig(getProductName()), clazz, targetTableName, null, null);
    }

    @DbReadOrWrite(DbReadOrWrite.Type.WRITE)
    @Override
    public void backup(String targetTableName, Column[] columns, Condition condition) {
        mybatisSqlBeanDao.backup(DbType.getSqlBeanConfig(getProductName()), clazz, targetTableName, columns, condition);
    }

    @DbReadOrWrite(DbReadOrWrite.Type.WRITE)
    @Override
    public long copy(String targetTableName, Condition condition) {
        return mybatisSqlBeanDao.copy(DbType.getSqlBeanConfig(getProductName()), clazz, targetTableName, null, condition);
    }

    @DbReadOrWrite(DbReadOrWrite.Type.WRITE)
    @Override
    public long copy(String targetTableName, Column[] columns, Condition condition) {
        return mybatisSqlBeanDao.copy(DbType.getSqlBeanConfig(getProductName()), clazz, targetTableName, columns, condition);
    }

    @Override
    public TableService getTableService() {
        if (tableService == null) {
            tableService = new TableService() {

                @DbReadOrWrite(DbReadOrWrite.Type.WRITE)
                @Override
                public void dropTable() {
                    mybatisSqlBeanDao.drop(clazz);
                }

                @DbReadOrWrite(DbReadOrWrite.Type.WRITE)
                @Override
                public void createTable() {
                    mybatisSqlBeanDao.create(DbType.getSqlBeanConfig(getProductName()), clazz);
                }

                @DbReadOrWrite(DbReadOrWrite.Type.WRITE)
                @Override
                public void dropAndCreateTable() {
                    dropTable();
                    createTable();
                }

                @DbReadOrWrite(DbReadOrWrite.Type.READ)
                @Override
                public List<String> getTableList() {
                    return mybatisSqlBeanDao.selectTableList(DbType.getSqlBeanConfig(getProductName()));
                }

            };
        }
        return tableService;
    }
}
