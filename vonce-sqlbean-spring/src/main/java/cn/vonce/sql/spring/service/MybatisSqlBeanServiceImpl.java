package cn.vonce.sql.spring.service;

import cn.vonce.sql.bean.*;
import cn.vonce.sql.config.SqlBeanConfig;
import cn.vonce.sql.config.SqlBeanDBConfig;
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

    private SqlBeanDBConfig sqlBeanDBConfig;

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
    public SqlBeanDBConfig getSqlBeanDBConfig() {
        if (sqlBeanDBConfig == null) {
            sqlBeanDBConfig = new SqlBeanDBConfig();
            //如果用户未进行配置
            boolean isUserConfig = true;
            if (sqlBeanConfig == null) {
                isUserConfig = false;
                sqlBeanConfig = new SqlBeanConfig();
            }
            //获取当前连接的数据库类型
            String productName = null;
            try {
                productName = sqlSession.getConfiguration().getEnvironment().getDataSource().getConnection().getMetaData().getDatabaseProductName();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            DbType dbType = DbType.getDbType(productName);
            sqlBeanDBConfig.setDbType(dbType);
            //如果用户未进行配置则对某些数据库进行设置
            if (!isUserConfig) {
                switch (Objects.requireNonNull(dbType)) {
                    case Oracle:
                    case DB2:
                    case Derby:
                    case Hsql:
                    case H2:
                        sqlBeanConfig.setToUpperCase(true);
                        break;
                }
            }
            sqlBeanDBConfig.setSqlBeanConfig(sqlBeanConfig);
        }
        return sqlBeanDBConfig;
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
        return mybatisSqlBeanDao.selectById(getSqlBeanDBConfig(), clazz, id);
    }

    @DbReadOrWrite(DbReadOrWrite.Type.READ)
    @Override
    public <O> O selectById(Class<O> returnType, ID id) {
        if (id == null) {
            return null;
        }
        if (SqlBeanUtil.isBaseType(returnType.getName()) || SqlBeanUtil.isMap(returnType.getName())) {
            return mybatisSqlBeanDao.selectByIdO(getSqlBeanDBConfig(), clazz, returnType, id);
        }
        return mybatisSqlBeanDao.selectByIdO(getSqlBeanDBConfig(), returnType, returnType, id);
    }

    @DbReadOrWrite(DbReadOrWrite.Type.READ)
    @Override
    public List<T> selectByIds(ID... ids) {
        if (ids == null || ids.length == 0) {
            return null;
        }
        return mybatisSqlBeanDao.selectByIds(getSqlBeanDBConfig(), clazz, ids);
    }

    @DbReadOrWrite(DbReadOrWrite.Type.READ)
    @Override
    public <O> List<O> selectByIds(Class<O> returnType, ID... ids) {
        if (ids == null || ids.length == 0) {
            return null;
        }
        if (SqlBeanUtil.isBaseType(returnType.getName()) || SqlBeanUtil.isMap(returnType.getName())) {
            return mybatisSqlBeanDao.selectByIdsO(getSqlBeanDBConfig(), clazz, returnType, ids);
        }
        return mybatisSqlBeanDao.selectByIdsO(getSqlBeanDBConfig(), returnType, returnType, ids);
    }

    @DbReadOrWrite(DbReadOrWrite.Type.READ)
    @Override
    public T selectOne(Select select) {
        return mybatisSqlBeanDao.selectOne(getSqlBeanDBConfig(), clazz, select);
    }

    @DbReadOrWrite(DbReadOrWrite.Type.READ)
    @Override
    public <O> O selectOne(Class<O> returnType, Select select) {
        if (SqlBeanUtil.isBaseType(returnType.getName()) || SqlBeanUtil.isMap(returnType.getName())) {
            return mybatisSqlBeanDao.selectOneO(getSqlBeanDBConfig(), clazz, returnType, select);
        }
        return mybatisSqlBeanDao.selectOneO(getSqlBeanDBConfig(), returnType, returnType, select);
    }

    @DbReadOrWrite(DbReadOrWrite.Type.READ)
    @Override
    public Map<String, Object> selectMap(Select select) {
        return mybatisSqlBeanDao.selectMap(getSqlBeanDBConfig(), clazz, select);
    }

    @DbReadOrWrite(DbReadOrWrite.Type.READ)
    @Override
    public T selectOneByCondition(String where, Object... args) {
        return mybatisSqlBeanDao.selectOneByCondition(getSqlBeanDBConfig(), clazz, where, args);
    }

    @DbReadOrWrite(DbReadOrWrite.Type.READ)
    @Override
    public <O> O selectOneByCondition(Class<O> returnType, String where, Object... args) {
        if (SqlBeanUtil.isBaseType(returnType.getName()) || SqlBeanUtil.isMap(returnType.getName())) {
            return mybatisSqlBeanDao.selectOneByConditionO(getSqlBeanDBConfig(), clazz, returnType, where, args);
        }
        return mybatisSqlBeanDao.selectOneByConditionO(getSqlBeanDBConfig(), returnType, returnType, where, args);
    }

    @DbReadOrWrite(DbReadOrWrite.Type.READ)
    @Override
    public <O> List<O> selectByCondition(Class<O> returnType, String where, Object... args) {
        if (SqlBeanUtil.isBaseType(returnType.getName()) || SqlBeanUtil.isMap(returnType.getName())) {
            return mybatisSqlBeanDao.selectByConditionO(getSqlBeanDBConfig(), clazz, returnType, null, where, args);
        }
        return mybatisSqlBeanDao.selectByConditionO(getSqlBeanDBConfig(), returnType, returnType, null, where, args);
    }

    @DbReadOrWrite(DbReadOrWrite.Type.READ)
    @Override
    public <O> List<O> selectByCondition(Class<O> returnType, Paging paging, String where, Object... args) {
        if (SqlBeanUtil.isBaseType(returnType.getName()) || SqlBeanUtil.isMap(returnType.getName())) {
            return mybatisSqlBeanDao.selectByConditionO(getSqlBeanDBConfig(), clazz, returnType, paging, where, args);
        }
        return mybatisSqlBeanDao.selectByConditionO(getSqlBeanDBConfig(), returnType, returnType, paging, where, args);
    }

    @DbReadOrWrite(DbReadOrWrite.Type.READ)
    @Override
    public List<T> selectByCondition(String where, Object... args) {
        return mybatisSqlBeanDao.selectByCondition(getSqlBeanDBConfig(), clazz, null, where, args);
    }

    @DbReadOrWrite(DbReadOrWrite.Type.READ)
    @Override
    public List<T> selectByCondition(Paging paging, String where, Object... args) {
        return mybatisSqlBeanDao.selectByCondition(getSqlBeanDBConfig(), clazz, paging, where, args);
    }

    @DbReadOrWrite(DbReadOrWrite.Type.READ)
    @Override
    public long selectCountByCondition(String where, Object... args) {
        return mybatisSqlBeanDao.selectCountByCondition(getSqlBeanDBConfig(), clazz, where, args);
    }

    @DbReadOrWrite(DbReadOrWrite.Type.READ)
    @Override
    public long countAll() {
        return mybatisSqlBeanDao.selectCountByCondition(getSqlBeanDBConfig(), clazz, null, null);
    }

    @DbReadOrWrite(DbReadOrWrite.Type.READ)
    @Override
    public List<T> selectAll() {
        return mybatisSqlBeanDao.selectAll(getSqlBeanDBConfig(), clazz, null);
    }

    @DbReadOrWrite(DbReadOrWrite.Type.READ)
    @Override
    public List<T> selectAll(Paging paging) {
        return mybatisSqlBeanDao.selectAll(getSqlBeanDBConfig(), clazz, paging);
    }

    @DbReadOrWrite(DbReadOrWrite.Type.READ)
    @Override
    public <O> List<O> selectAll(Class<O> returnType) {
        if (SqlBeanUtil.isBaseType(returnType.getName()) || SqlBeanUtil.isMap(returnType.getName())) {
            return mybatisSqlBeanDao.selectAllO(getSqlBeanDBConfig(), clazz, returnType, null);
        }
        return mybatisSqlBeanDao.selectAllO(getSqlBeanDBConfig(), returnType, returnType, null);
    }

    @DbReadOrWrite(DbReadOrWrite.Type.READ)
    @Override
    public <O> List<O> selectAll(Class<O> returnType, Paging paging) {
        if (SqlBeanUtil.isBaseType(returnType.getName()) || SqlBeanUtil.isMap(returnType.getName())) {
            return mybatisSqlBeanDao.selectAllO(getSqlBeanDBConfig(), clazz, returnType, paging);
        }
        return mybatisSqlBeanDao.selectAllO(getSqlBeanDBConfig(), returnType, returnType, paging);
    }

    @DbReadOrWrite(DbReadOrWrite.Type.READ)
    @Override
    public List<Map<String, Object>> selectMapList(Select select) {
        return mybatisSqlBeanDao.selectMapList(getSqlBeanDBConfig(), clazz, select);
    }

    @DbReadOrWrite(DbReadOrWrite.Type.READ)
    @Override
    public <O> List<O> select(Class<O> returnType, Select select) {
        if (SqlBeanUtil.isBaseType(returnType.getName()) || SqlBeanUtil.isMap(returnType.getName())) {
            return mybatisSqlBeanDao.selectO(getSqlBeanDBConfig(), clazz, returnType, select);
        }
        return mybatisSqlBeanDao.selectO(getSqlBeanDBConfig(), returnType, returnType, select);
    }

    @DbReadOrWrite(DbReadOrWrite.Type.READ)
    @Override
    public List<T> select(Select select) {
        return mybatisSqlBeanDao.select(getSqlBeanDBConfig(), clazz, select);
    }

    @DbReadOrWrite(DbReadOrWrite.Type.READ)
    @Override
    public long count(Select select) {
        return mybatisSqlBeanDao.count(getSqlBeanDBConfig(), clazz, select);
    }

    @DbReadOrWrite(DbReadOrWrite.Type.READ)
    @Override
    public long count(Class<?> clazz, Select select) {
        return mybatisSqlBeanDao.count(getSqlBeanDBConfig(), clazz, select);
    }

    @DbReadOrWrite(DbReadOrWrite.Type.WRITE)
    @Override
    public long deleteById(ID... id) {
        return mybatisSqlBeanDao.deleteById(getSqlBeanDBConfig(), clazz, id);
    }

    @DbReadOrWrite(DbReadOrWrite.Type.WRITE)
    @Override
    public long deleteByCondition(String where, Object... args) {
        return mybatisSqlBeanDao.deleteByCondition(getSqlBeanDBConfig(), clazz, where, args);
    }

    @DbReadOrWrite(DbReadOrWrite.Type.WRITE)
    @Override
    public long delete(Delete delete) {
        return mybatisSqlBeanDao.delete(getSqlBeanDBConfig(), clazz, delete, false);
    }

    @DbReadOrWrite(DbReadOrWrite.Type.WRITE)
    @Override
    public long delete(Delete delete, boolean ignore) {
        return mybatisSqlBeanDao.delete(getSqlBeanDBConfig(), clazz, delete, ignore);
    }

    @DbReadOrWrite(DbReadOrWrite.Type.WRITE)
    @Override
    public long logicallyDeleteById(ID id) {
        return mybatisSqlBeanDao.logicallyDeleteById(getSqlBeanDBConfig(), clazz, id);
    }

    @DbReadOrWrite(DbReadOrWrite.Type.WRITE)
    @Override
    public long logicallyDeleteByCondition(String where, Object... args) {
        return mybatisSqlBeanDao.logicallyDeleteByCondition(getSqlBeanDBConfig(), clazz, where, args);
    }

    @DbReadOrWrite(DbReadOrWrite.Type.WRITE)
    @Override
    public long update(Update update) {
        return mybatisSqlBeanDao.update(getSqlBeanDBConfig(), update, false);
    }

    @DbReadOrWrite(DbReadOrWrite.Type.WRITE)
    @Override
    public long update(Update update, boolean ignore) {
        return mybatisSqlBeanDao.update(getSqlBeanDBConfig(), update, ignore);
    }

    @DbReadOrWrite(DbReadOrWrite.Type.WRITE)
    @Override
    public long updateById(T bean, ID id, boolean updateNotNull) {
        return mybatisSqlBeanDao.updateById(getSqlBeanDBConfig(), bean, id, updateNotNull, null);
    }

    @DbReadOrWrite(DbReadOrWrite.Type.WRITE)
    @Override
    public long updateById(T bean, ID id, boolean updateNotNull, String[] filterFields) {
        return mybatisSqlBeanDao.updateById(getSqlBeanDBConfig(), bean, id, updateNotNull, filterFields);
    }

    @DbReadOrWrite(DbReadOrWrite.Type.WRITE)
    @Override
    public long updateByBeanId(T bean, boolean updateNotNull) {
        return mybatisSqlBeanDao.updateByBeanId(getSqlBeanDBConfig(), bean, updateNotNull, null);
    }

    @DbReadOrWrite(DbReadOrWrite.Type.WRITE)
    @Override
    public long updateByBeanId(T bean, boolean updateNotNull, String[] filterFields) {
        return mybatisSqlBeanDao.updateByBeanId(getSqlBeanDBConfig(), bean, updateNotNull, filterFields);
    }

    @DbReadOrWrite(DbReadOrWrite.Type.WRITE)
    @Override
    public long updateByCondition(T bean, boolean updateNotNull, String where, Object... args) {
        return mybatisSqlBeanDao.updateByCondition(getSqlBeanDBConfig(), bean, updateNotNull, null, where, args);
    }

    @DbReadOrWrite(DbReadOrWrite.Type.WRITE)
    @Override
    public long updateByCondition(T bean, boolean updateNotNull, String[] filterFields, String where, Object... args) {
        return mybatisSqlBeanDao.updateByCondition(getSqlBeanDBConfig(), bean, updateNotNull, filterFields, where, args);
    }

    @DbReadOrWrite(DbReadOrWrite.Type.WRITE)
    @Override
    public long updateByBeanCondition(T bean, boolean updateNotNull, String where) {
        return mybatisSqlBeanDao.updateByBeanCondition(getSqlBeanDBConfig(), bean, updateNotNull, null, where);
    }

    @DbReadOrWrite(DbReadOrWrite.Type.WRITE)
    @Override
    public long updateByBeanCondition(T bean, boolean updateNotNull, String[] filterFields, String where) {
        return mybatisSqlBeanDao.updateByBeanCondition(getSqlBeanDBConfig(), bean, updateNotNull, filterFields, where);
    }

    @SuppressWarnings("unchecked")
    @DbReadOrWrite(DbReadOrWrite.Type.WRITE)
    @Override
    public long insert(T... bean) {
        return mybatisSqlBeanDao.insertBean(getSqlBeanDBConfig(), Arrays.asList(bean));
    }

    @DbReadOrWrite(DbReadOrWrite.Type.WRITE)
    @Override
    public long insert(List<T> beanList) {
        return mybatisSqlBeanDao.insertBean(getSqlBeanDBConfig(), beanList);
    }

    @DbReadOrWrite(DbReadOrWrite.Type.WRITE)
    @Override
    public long inset(Insert insert) {
        return mybatisSqlBeanDao.insert(getSqlBeanDBConfig(), insert);
    }

    @DbReadOrWrite(DbReadOrWrite.Type.WRITE)
    @Override
    public String backup() {
        String targetTableName = SqlBeanUtil.getTable(clazz).getName() + "_" + DateUtil.dateToString(new Date(), "yyyyMMddHHmmss");
        try {
            mybatisSqlBeanDao.backup(getSqlBeanDBConfig(), clazz, targetTableName, null, null);
        } catch (Exception e) {
            return null;
        }
        return targetTableName;
    }

    @DbReadOrWrite(DbReadOrWrite.Type.WRITE)
    @Override
    public void backup(String targetTableName) {
        mybatisSqlBeanDao.backup(getSqlBeanDBConfig(), clazz, targetTableName, null, null);
    }

    @DbReadOrWrite(DbReadOrWrite.Type.WRITE)
    @Override
    public void backup(String targetTableName, Column[] columns, Condition condition) {
        mybatisSqlBeanDao.backup(getSqlBeanDBConfig(), clazz, targetTableName, columns, condition);
    }

    @DbReadOrWrite(DbReadOrWrite.Type.WRITE)
    @Override
    public long copy(String targetTableName, Condition condition) {
        return mybatisSqlBeanDao.copy(getSqlBeanDBConfig(), clazz, targetTableName, null, condition);
    }

    @DbReadOrWrite(DbReadOrWrite.Type.WRITE)
    @Override
    public long copy(String targetTableName, Column[] columns, Condition condition) {
        return mybatisSqlBeanDao.copy(getSqlBeanDBConfig(), clazz, targetTableName, columns, condition);
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
                    mybatisSqlBeanDao.create(getSqlBeanDBConfig(), clazz);
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
                    return mybatisSqlBeanDao.selectTableList(getSqlBeanDBConfig());
                }

            };
        }
        return tableService;
    }
}
