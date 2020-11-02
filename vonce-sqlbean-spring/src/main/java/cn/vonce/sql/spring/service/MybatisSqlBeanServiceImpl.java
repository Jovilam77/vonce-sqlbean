package cn.vonce.sql.spring.service;

import cn.vonce.sql.bean.*;
import cn.vonce.sql.config.SqlBeanConfig;
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
import java.util.*;

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
public class MybatisSqlBeanServiceImpl<T, ID> extends BaseSqlBeanServiceImpl implements SqlBeanService<T, ID> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private TableService tableService;

    @Autowired
    private MybatisSqlBeanDao<T> mybatisSqlBeanDao;

    @Autowired(required = false)
    private SqlBeanConfig sqlBeanConfig;

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
    public SqlBeanConfig getSqlBeanConfig() {
        return sqlBeanConfig;
    }

    @Override
    public String getProductName(){
        //获取当前连接的数据库类型
        String productName = null;
        try {
            productName = sqlSession.getConfiguration().getEnvironment().getDataSource().getConnection().getMetaData().getDatabaseProductName();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return productName;
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
        return mybatisSqlBeanDao.selectById(getSqlBeanDB(), clazz, id);
    }

    @DbReadOrWrite(DbReadOrWrite.Type.READ)
    @Override
    public <O> O selectById(Class<O> returnType, ID id) {
        if (id == null) {
            return null;
        }
        if (SqlBeanUtil.isBaseType(returnType.getName()) || SqlBeanUtil.isMap(returnType.getName())) {
            return mybatisSqlBeanDao.selectByIdO(getSqlBeanDB(), clazz, returnType, id);
        }
        return mybatisSqlBeanDao.selectByIdO(getSqlBeanDB(), returnType, returnType, id);
    }

    @DbReadOrWrite(DbReadOrWrite.Type.READ)
    @Override
    public List<T> selectByIds(ID... ids) {
        if (ids == null || ids.length == 0) {
            return null;
        }
        return mybatisSqlBeanDao.selectByIds(getSqlBeanDB(), clazz, ids);
    }

    @DbReadOrWrite(DbReadOrWrite.Type.READ)
    @Override
    public <O> List<O> selectByIds(Class<O> returnType, ID... ids) {
        if (ids == null || ids.length == 0) {
            return null;
        }
        if (SqlBeanUtil.isBaseType(returnType.getName()) || SqlBeanUtil.isMap(returnType.getName())) {
            return mybatisSqlBeanDao.selectByIdsO(getSqlBeanDB(), clazz, returnType, ids);
        }
        return mybatisSqlBeanDao.selectByIdsO(getSqlBeanDB(), returnType, returnType, ids);
    }

    @DbReadOrWrite(DbReadOrWrite.Type.READ)
    @Override
    public T selectOne(Select select) {
        return mybatisSqlBeanDao.selectOne(getSqlBeanDB(), clazz, select);
    }

    @DbReadOrWrite(DbReadOrWrite.Type.READ)
    @Override
    public <O> O selectOne(Class<O> returnType, Select select) {
        if (SqlBeanUtil.isBaseType(returnType.getName()) || SqlBeanUtil.isMap(returnType.getName())) {
            return mybatisSqlBeanDao.selectOneO(getSqlBeanDB(), clazz, returnType, select);
        }
        return mybatisSqlBeanDao.selectOneO(getSqlBeanDB(), returnType, returnType, select);
    }

    @DbReadOrWrite(DbReadOrWrite.Type.READ)
    @Override
    public Map<String, Object> selectMap(Select select) {
        return mybatisSqlBeanDao.selectMap(getSqlBeanDB(), clazz, select);
    }

    @DbReadOrWrite(DbReadOrWrite.Type.READ)
    @Override
    public T selectOneByCondition(String where, Object... args) {
        return mybatisSqlBeanDao.selectOneByCondition(getSqlBeanDB(), clazz, where, args);
    }

    @DbReadOrWrite(DbReadOrWrite.Type.READ)
    @Override
    public <O> O selectOneByCondition(Class<O> returnType, String where, Object... args) {
        if (SqlBeanUtil.isBaseType(returnType.getName()) || SqlBeanUtil.isMap(returnType.getName())) {
            return mybatisSqlBeanDao.selectOneByConditionO(getSqlBeanDB(), clazz, returnType, where, args);
        }
        return mybatisSqlBeanDao.selectOneByConditionO(getSqlBeanDB(), returnType, returnType, where, args);
    }

    @DbReadOrWrite(DbReadOrWrite.Type.READ)
    @Override
    public <O> List<O> selectByCondition(Class<O> returnType, String where, Object... args) {
        if (SqlBeanUtil.isBaseType(returnType.getName()) || SqlBeanUtil.isMap(returnType.getName())) {
            return mybatisSqlBeanDao.selectByConditionO(getSqlBeanDB(), clazz, returnType, null, where, args);
        }
        return mybatisSqlBeanDao.selectByConditionO(getSqlBeanDB(), returnType, returnType, null, where, args);
    }

    @DbReadOrWrite(DbReadOrWrite.Type.READ)
    @Override
    public <O> List<O> selectByCondition(Class<O> returnType, Paging paging, String where, Object... args) {
        if (SqlBeanUtil.isBaseType(returnType.getName()) || SqlBeanUtil.isMap(returnType.getName())) {
            return mybatisSqlBeanDao.selectByConditionO(getSqlBeanDB(), clazz, returnType, paging, where, args);
        }
        return mybatisSqlBeanDao.selectByConditionO(getSqlBeanDB(), returnType, returnType, paging, where, args);
    }

    @DbReadOrWrite(DbReadOrWrite.Type.READ)
    @Override
    public List<T> selectByCondition(String where, Object... args) {
        return mybatisSqlBeanDao.selectByCondition(getSqlBeanDB(), clazz, null, where, args);
    }

    @DbReadOrWrite(DbReadOrWrite.Type.READ)
    @Override
    public List<T> selectByCondition(Paging paging, String where, Object... args) {
        return mybatisSqlBeanDao.selectByCondition(getSqlBeanDB(), clazz, paging, where, args);
    }

    @DbReadOrWrite(DbReadOrWrite.Type.READ)
    @Override
    public long selectCountByCondition(String where, Object... args) {
        return mybatisSqlBeanDao.selectCountByCondition(getSqlBeanDB(), clazz, where, args);
    }

    @DbReadOrWrite(DbReadOrWrite.Type.READ)
    @Override
    public long countAll() {
        return mybatisSqlBeanDao.selectCountByCondition(getSqlBeanDB(), clazz, null, null);
    }

    @DbReadOrWrite(DbReadOrWrite.Type.READ)
    @Override
    public List<T> selectAll() {
        return mybatisSqlBeanDao.selectAll(getSqlBeanDB(), clazz, null);
    }

    @DbReadOrWrite(DbReadOrWrite.Type.READ)
    @Override
    public List<T> selectAll(Paging paging) {
        return mybatisSqlBeanDao.selectAll(getSqlBeanDB(), clazz, paging);
    }

    @DbReadOrWrite(DbReadOrWrite.Type.READ)
    @Override
    public <O> List<O> selectAll(Class<O> returnType) {
        if (SqlBeanUtil.isBaseType(returnType.getName()) || SqlBeanUtil.isMap(returnType.getName())) {
            return mybatisSqlBeanDao.selectAllO(getSqlBeanDB(), clazz, returnType, null);
        }
        return mybatisSqlBeanDao.selectAllO(getSqlBeanDB(), returnType, returnType, null);
    }

    @DbReadOrWrite(DbReadOrWrite.Type.READ)
    @Override
    public <O> List<O> selectAll(Class<O> returnType, Paging paging) {
        if (SqlBeanUtil.isBaseType(returnType.getName()) || SqlBeanUtil.isMap(returnType.getName())) {
            return mybatisSqlBeanDao.selectAllO(getSqlBeanDB(), clazz, returnType, paging);
        }
        return mybatisSqlBeanDao.selectAllO(getSqlBeanDB(), returnType, returnType, paging);
    }

    @DbReadOrWrite(DbReadOrWrite.Type.READ)
    @Override
    public List<Map<String, Object>> selectMapList(Select select) {
        return mybatisSqlBeanDao.selectMapList(getSqlBeanDB(), clazz, select);
    }

    @DbReadOrWrite(DbReadOrWrite.Type.READ)
    @Override
    public <O> List<O> select(Class<O> returnType, Select select) {
        if (SqlBeanUtil.isBaseType(returnType.getName()) || SqlBeanUtil.isMap(returnType.getName())) {
            return mybatisSqlBeanDao.selectO(getSqlBeanDB(), clazz, returnType, select);
        }
        return mybatisSqlBeanDao.selectO(getSqlBeanDB(), returnType, returnType, select);
    }

    @DbReadOrWrite(DbReadOrWrite.Type.READ)
    @Override
    public List<T> select(Select select) {
        return mybatisSqlBeanDao.select(getSqlBeanDB(), clazz, select);
    }

    @DbReadOrWrite(DbReadOrWrite.Type.READ)
    @Override
    public long count(Select select) {
        return mybatisSqlBeanDao.count(getSqlBeanDB(), clazz, select);
    }

    @DbReadOrWrite(DbReadOrWrite.Type.READ)
    @Override
    public long count(Class<?> clazz, Select select) {
        return mybatisSqlBeanDao.count(getSqlBeanDB(), clazz, select);
    }

    @DbReadOrWrite(DbReadOrWrite.Type.WRITE)
    @Override
    public long deleteById(ID... id) {
        return mybatisSqlBeanDao.deleteById(getSqlBeanDB(), clazz, id);
    }

    @DbReadOrWrite(DbReadOrWrite.Type.WRITE)
    @Override
    public long deleteByCondition(String where, Object... args) {
        return mybatisSqlBeanDao.deleteByCondition(getSqlBeanDB(), clazz, where, args);
    }

    @DbReadOrWrite(DbReadOrWrite.Type.WRITE)
    @Override
    public long delete(Delete delete) {
        return mybatisSqlBeanDao.delete(getSqlBeanDB(), clazz, delete, false);
    }

    @DbReadOrWrite(DbReadOrWrite.Type.WRITE)
    @Override
    public long delete(Delete delete, boolean ignore) {
        return mybatisSqlBeanDao.delete(getSqlBeanDB(), clazz, delete, ignore);
    }

    @DbReadOrWrite(DbReadOrWrite.Type.WRITE)
    @Override
    public long logicallyDeleteById(ID id) {
        return mybatisSqlBeanDao.logicallyDeleteById(getSqlBeanDB(), clazz, id);
    }

    @DbReadOrWrite(DbReadOrWrite.Type.WRITE)
    @Override
    public long logicallyDeleteByCondition(String where, Object... args) {
        return mybatisSqlBeanDao.logicallyDeleteByCondition(getSqlBeanDB(), clazz, where, args);
    }

    @DbReadOrWrite(DbReadOrWrite.Type.WRITE)
    @Override
    public long update(Update update) {
        return mybatisSqlBeanDao.update(getSqlBeanDB(), update, false);
    }

    @DbReadOrWrite(DbReadOrWrite.Type.WRITE)
    @Override
    public long update(Update update, boolean ignore) {
        return mybatisSqlBeanDao.update(getSqlBeanDB(), update, ignore);
    }

    @DbReadOrWrite(DbReadOrWrite.Type.WRITE)
    @Override
    public long updateById(T bean, ID id, boolean updateNotNull) {
        return mybatisSqlBeanDao.updateById(getSqlBeanDB(), bean, id, updateNotNull, null);
    }

    @DbReadOrWrite(DbReadOrWrite.Type.WRITE)
    @Override
    public long updateById(T bean, ID id, boolean updateNotNull, String[] filterFields) {
        return mybatisSqlBeanDao.updateById(getSqlBeanDB(), bean, id, updateNotNull, filterFields);
    }

    @DbReadOrWrite(DbReadOrWrite.Type.WRITE)
    @Override
    public long updateByBeanId(T bean, boolean updateNotNull) {
        return mybatisSqlBeanDao.updateByBeanId(getSqlBeanDB(), bean, updateNotNull, null);
    }

    @DbReadOrWrite(DbReadOrWrite.Type.WRITE)
    @Override
    public long updateByBeanId(T bean, boolean updateNotNull, String[] filterFields) {
        return mybatisSqlBeanDao.updateByBeanId(getSqlBeanDB(), bean, updateNotNull, filterFields);
    }

    @DbReadOrWrite(DbReadOrWrite.Type.WRITE)
    @Override
    public long updateByCondition(T bean, boolean updateNotNull, String where, Object... args) {
        return mybatisSqlBeanDao.updateByCondition(getSqlBeanDB(), bean, updateNotNull, null, where, args);
    }

    @DbReadOrWrite(DbReadOrWrite.Type.WRITE)
    @Override
    public long updateByCondition(T bean, boolean updateNotNull, String[] filterFields, String where, Object... args) {
        return mybatisSqlBeanDao.updateByCondition(getSqlBeanDB(), bean, updateNotNull, filterFields, where, args);
    }

    @DbReadOrWrite(DbReadOrWrite.Type.WRITE)
    @Override
    public long updateByBeanCondition(T bean, boolean updateNotNull, String where) {
        return mybatisSqlBeanDao.updateByBeanCondition(getSqlBeanDB(), bean, updateNotNull, null, where);
    }

    @DbReadOrWrite(DbReadOrWrite.Type.WRITE)
    @Override
    public long updateByBeanCondition(T bean, boolean updateNotNull, String[] filterFields, String where) {
        return mybatisSqlBeanDao.updateByBeanCondition(getSqlBeanDB(), bean, updateNotNull, filterFields, where);
    }

    @SuppressWarnings("unchecked")
    @DbReadOrWrite(DbReadOrWrite.Type.WRITE)
    @Override
    public long insert(T... bean) {
        return mybatisSqlBeanDao.insertBean(getSqlBeanDB(), Arrays.asList(bean));
    }

    @DbReadOrWrite(DbReadOrWrite.Type.WRITE)
    @Override
    public long insert(List<T> beanList) {
        return mybatisSqlBeanDao.insertBean(getSqlBeanDB(), beanList);
    }

    @DbReadOrWrite(DbReadOrWrite.Type.WRITE)
    @Override
    public long inset(Insert insert) {
        return mybatisSqlBeanDao.insert(getSqlBeanDB(), insert);
    }

    @DbReadOrWrite(DbReadOrWrite.Type.WRITE)
    @Override
    public String backup() {
        String targetTableName = SqlBeanUtil.getTable(clazz).getName() + "_" + DateUtil.dateToString(new Date(), "yyyyMMddHHmmss");
        try {
            mybatisSqlBeanDao.backup(getSqlBeanDB(), clazz, targetTableName, null, null);
        } catch (Exception e) {
            return null;
        }
        return targetTableName;
    }

    @DbReadOrWrite(DbReadOrWrite.Type.WRITE)
    @Override
    public void backup(String targetTableName) {
        mybatisSqlBeanDao.backup(getSqlBeanDB(), clazz, targetTableName, null, null);
    }

    @DbReadOrWrite(DbReadOrWrite.Type.WRITE)
    @Override
    public void backup(String targetTableName, Column[] columns, Condition condition) {
        mybatisSqlBeanDao.backup(getSqlBeanDB(), clazz, targetTableName, columns, condition);
    }

    @DbReadOrWrite(DbReadOrWrite.Type.WRITE)
    @Override
    public long copy(String targetTableName, Condition condition) {
        return mybatisSqlBeanDao.copy(getSqlBeanDB(), clazz, targetTableName, null, condition);
    }

    @DbReadOrWrite(DbReadOrWrite.Type.WRITE)
    @Override
    public long copy(String targetTableName, Column[] columns, Condition condition) {
        return mybatisSqlBeanDao.copy(getSqlBeanDB(), clazz, targetTableName, columns, condition);
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
                    mybatisSqlBeanDao.create(getSqlBeanDB(), clazz);
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
                    return mybatisSqlBeanDao.selectTableList(getSqlBeanDB());
                }

            };
        }
        return tableService;
    }
}
