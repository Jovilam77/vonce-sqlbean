package cn.vonce.sql.spring.service;

import cn.vonce.sql.bean.*;
import cn.vonce.sql.config.SqlBeanConfig;
import cn.vonce.sql.helper.Wrapper;
import cn.vonce.sql.service.TableService;
import cn.vonce.sql.spring.annotation.DbSwitch;
import cn.vonce.sql.spring.config.UseMybatis;
import cn.vonce.sql.spring.dao.MybatisSqlBeanDao;
import cn.vonce.sql.service.SqlBeanService;
import cn.vonce.sql.spring.enumerate.DbRole;
import cn.vonce.sql.uitls.DateUtil;
import cn.vonce.sql.uitls.SqlBeanUtil;
import cn.vonce.sql.uitls.StringUtil;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class MybatisSqlBeanServiceImpl<T, ID> extends BaseSqlBeanServiceImpl implements SqlBeanService<T, ID>, TableService {

    private Logger logger = LoggerFactory.getLogger(MybatisSqlBeanServiceImpl.class);

    @Autowired
    private MybatisSqlBeanDao<T> mybatisSqlBeanDao;

    @Autowired(required = false)
    private SqlBeanConfig sqlBeanConfig;

    @Autowired
    private SqlSession sqlSession;

    private String productName;

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
                productName = sqlSession.getConfiguration().getEnvironment().getDataSource().getConnection().getMetaData().getDatabaseProductName();
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
        return mybatisSqlBeanDao.selectById(getSqlBeanDB(), clazz, id);
    }

    @DbSwitch(DbRole.SLAVE)
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

    @DbSwitch(DbRole.SLAVE)
    @Override
    public List<T> selectByIds(ID... ids) {
        if (ids == null || ids.length == 0) {
            return null;
        }
        return mybatisSqlBeanDao.selectByIds(getSqlBeanDB(), clazz, ids);
    }

    @DbSwitch(DbRole.SLAVE)
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

    @DbSwitch(DbRole.SLAVE)
    @Override
    public T selectOne(Select select) {
        return mybatisSqlBeanDao.selectOne(getSqlBeanDB(), clazz, select);
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public <O> O selectOne(Class<O> returnType, Select select) {
        if (SqlBeanUtil.isBaseType(returnType.getName()) || SqlBeanUtil.isMap(returnType.getName())) {
            return mybatisSqlBeanDao.selectOneO(getSqlBeanDB(), clazz, returnType, select);
        }
        return mybatisSqlBeanDao.selectOneO(getSqlBeanDB(), returnType, returnType, select);
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public Map<String, Object> selectMap(Select select) {
        return mybatisSqlBeanDao.selectMap(getSqlBeanDB(), clazz, select);
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public T selectOneByCondition(String where, Object... args) {
        return mybatisSqlBeanDao.selectOneByCondition(getSqlBeanDB(), clazz, where, args);
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public <O> O selectOneByCondition(Class<O> returnType, String where, Object... args) {
        if (SqlBeanUtil.isBaseType(returnType.getName()) || SqlBeanUtil.isMap(returnType.getName())) {
            return mybatisSqlBeanDao.selectOneByConditionO(getSqlBeanDB(), clazz, returnType, where, args);
        }
        return mybatisSqlBeanDao.selectOneByConditionO(getSqlBeanDB(), returnType, returnType, where, args);
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public T selectOneByCondition(Wrapper where) {
        Select select = new Select();
        select.setWhere(where);
        return mybatisSqlBeanDao.selectOne(getSqlBeanDB(), clazz, select);
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public <O> O selectOneByCondition(Class<O> returnType, Wrapper where) {
        Select select = new Select();
        select.setWhere(where);
        if (SqlBeanUtil.isBaseType(returnType.getName()) || SqlBeanUtil.isMap(returnType.getName())) {
            return mybatisSqlBeanDao.selectOneO(getSqlBeanDB(), clazz, returnType, select);
        }
        return mybatisSqlBeanDao.selectOneO(getSqlBeanDB(), returnType, returnType, select);
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public <O> List<O> selectByCondition(Class<O> returnType, String where, Object... args) {
        if (SqlBeanUtil.isBaseType(returnType.getName()) || SqlBeanUtil.isMap(returnType.getName())) {
            return mybatisSqlBeanDao.selectByConditionO(getSqlBeanDB(), clazz, returnType, null, where, args);
        }
        return mybatisSqlBeanDao.selectByConditionO(getSqlBeanDB(), returnType, returnType, null, where, args);
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public <O> List<O> selectByCondition(Class<O> returnType, Wrapper where) {
        Select select = new Select();
        select.setWhere(where);
        if (SqlBeanUtil.isBaseType(returnType.getName()) || SqlBeanUtil.isMap(returnType.getName())) {
            return mybatisSqlBeanDao.selectO(getSqlBeanDB(), clazz, returnType, select);
        }
        return mybatisSqlBeanDao.selectO(getSqlBeanDB(), returnType, returnType, select);
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public <O> List<O> selectByCondition(Class<O> returnType, Paging paging, String where, Object... args) {
        if (SqlBeanUtil.isBaseType(returnType.getName()) || SqlBeanUtil.isMap(returnType.getName())) {
            return mybatisSqlBeanDao.selectByConditionO(getSqlBeanDB(), clazz, returnType, paging, where, args);
        }
        return mybatisSqlBeanDao.selectByConditionO(getSqlBeanDB(), returnType, returnType, paging, where, args);
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public <O> List<O> selectByCondition(Class<O> returnType, Paging paging, Wrapper where) {
        Select select = new Select();
        select.setWhere(where);
        select.setPage(paging.getPagenum(), paging.getPagesize());
        select.orderBy(paging.getOrders());
        if (SqlBeanUtil.isBaseType(returnType.getName()) || SqlBeanUtil.isMap(returnType.getName())) {
            return mybatisSqlBeanDao.selectO(getSqlBeanDB(), clazz, returnType, select);
        }
        return mybatisSqlBeanDao.selectO(getSqlBeanDB(), returnType, returnType, select);
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public List<T> selectByCondition(String where, Object... args) {
        return mybatisSqlBeanDao.selectByCondition(getSqlBeanDB(), clazz, null, where, args);
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public List<T> selectByCondition(Wrapper where) {
        Select select = new Select();
        select.setWhere(where);
        return mybatisSqlBeanDao.select(getSqlBeanDB(), clazz, select);
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public List<T> selectByCondition(Paging paging, String where, Object... args) {
        return mybatisSqlBeanDao.selectByCondition(getSqlBeanDB(), clazz, paging, where, args);
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public List<T> selectByCondition(Paging paging, Wrapper where) {
        Select select = new Select();
        select.setWhere(where);
        select.setPage(paging.getPagenum(), paging.getPagesize());
        select.orderBy(paging.getOrders());
        return mybatisSqlBeanDao.select(getSqlBeanDB(), clazz, select);
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public int selectCountByCondition(String where, Object... args) {
        return mybatisSqlBeanDao.selectCountByCondition(getSqlBeanDB(), clazz, where, args);
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public int selectCountByCondition(Wrapper where) {
        Select select = new Select();
        select.setWhere(where);
        return mybatisSqlBeanDao.count(getSqlBeanDB(), clazz, select);
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public int countAll() {
        return mybatisSqlBeanDao.selectCountByCondition(getSqlBeanDB(), clazz, null, null);
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public List<T> selectAll() {
        return mybatisSqlBeanDao.selectAll(getSqlBeanDB(), clazz, null);
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public List<T> selectAll(Paging paging) {
        return mybatisSqlBeanDao.selectAll(getSqlBeanDB(), clazz, paging);
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public <O> List<O> selectAll(Class<O> returnType) {
        if (SqlBeanUtil.isBaseType(returnType.getName()) || SqlBeanUtil.isMap(returnType.getName())) {
            return mybatisSqlBeanDao.selectAllO(getSqlBeanDB(), clazz, returnType, null);
        }
        return mybatisSqlBeanDao.selectAllO(getSqlBeanDB(), returnType, returnType, null);
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public <O> List<O> selectAll(Class<O> returnType, Paging paging) {
        if (SqlBeanUtil.isBaseType(returnType.getName()) || SqlBeanUtil.isMap(returnType.getName())) {
            return mybatisSqlBeanDao.selectAllO(getSqlBeanDB(), clazz, returnType, paging);
        }
        return mybatisSqlBeanDao.selectAllO(getSqlBeanDB(), returnType, returnType, paging);
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public List<Map<String, Object>> selectMapList(Select select) {
        return mybatisSqlBeanDao.selectMapList(getSqlBeanDB(), clazz, select);
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public <O> List<O> select(Class<O> returnType, Select select) {
        if (SqlBeanUtil.isBaseType(returnType.getName()) || SqlBeanUtil.isMap(returnType.getName())) {
            return mybatisSqlBeanDao.selectO(getSqlBeanDB(), clazz, returnType, select);
        }
        return mybatisSqlBeanDao.selectO(getSqlBeanDB(), returnType, returnType, select);
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public List<T> select(Select select) {
        return mybatisSqlBeanDao.select(getSqlBeanDB(), clazz, select);
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public int count(Select select) {
        return mybatisSqlBeanDao.count(getSqlBeanDB(), clazz, select);
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public int count(Class<?> clazz, Select select) {
        return mybatisSqlBeanDao.count(getSqlBeanDB(), clazz, select);
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int deleteById(ID... id) {
        return mybatisSqlBeanDao.deleteById(getSqlBeanDB(), clazz, id);
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int deleteByCondition(String where, Object... args) {
        return mybatisSqlBeanDao.deleteByCondition(getSqlBeanDB(), clazz, where, args);
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int deleteByCondition(Wrapper where) {
        Delete delete = new Delete();
        delete.setWhere(where);
        return mybatisSqlBeanDao.delete(getSqlBeanDB(), clazz, delete, false);
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int delete(Delete delete) {
        return mybatisSqlBeanDao.delete(getSqlBeanDB(), clazz, delete, false);
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int delete(Delete delete, boolean ignore) {
        return mybatisSqlBeanDao.delete(getSqlBeanDB(), clazz, delete, ignore);
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int logicallyDeleteById(ID id) {
        return mybatisSqlBeanDao.logicallyDeleteById(getSqlBeanDB(), clazz, id);
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int logicallyDeleteByCondition(String where, Object... args) {
        return mybatisSqlBeanDao.logicallyDeleteByCondition(getSqlBeanDB(), clazz, where, args);
    }

    @Override
    public int logicallyDeleteByCondition(Wrapper where) {
        return mybatisSqlBeanDao.logicallyDeleteByWrapper(getSqlBeanDB(), clazz, where);
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int update(Update update) {
        return mybatisSqlBeanDao.update(getSqlBeanDB(), clazz, update, false);
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int update(Update update, boolean ignore) {
        return mybatisSqlBeanDao.update(getSqlBeanDB(), clazz, update, ignore);
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int updateById(T bean, ID id, boolean updateNotNull) {
        return mybatisSqlBeanDao.updateById(getSqlBeanDB(), clazz, bean, id, updateNotNull, null);
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int updateById(T bean, ID id, boolean updateNotNull, String[] filterFields) {
        return mybatisSqlBeanDao.updateById(getSqlBeanDB(), clazz, bean, id, updateNotNull, filterFields);
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int updateByBeanId(T bean, boolean updateNotNull) {
        return mybatisSqlBeanDao.updateByBeanId(getSqlBeanDB(), clazz, bean, updateNotNull, null);
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int updateByBeanId(T bean, boolean updateNotNull, String[] filterFields) {
        return mybatisSqlBeanDao.updateByBeanId(getSqlBeanDB(), clazz, bean, updateNotNull, filterFields);
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int updateByCondition(T bean, boolean updateNotNull, String where, Object... args) {
        return mybatisSqlBeanDao.updateByCondition(getSqlBeanDB(), clazz, bean, updateNotNull, null, where, args);
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int updateByCondition(T bean, boolean updateNotNull, Wrapper where) {
        Update update = new Update();
        update.setUpdateBean(bean);
        update.setWhere(where);
        return mybatisSqlBeanDao.update(getSqlBeanDB(), clazz, update, false);
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int updateByCondition(T bean, boolean updateNotNull, String[] filterFields, String where, Object... args) {
        return mybatisSqlBeanDao.updateByCondition(getSqlBeanDB(), clazz, bean, updateNotNull, filterFields, where, args);
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int updateByCondition(T bean, boolean updateNotNull, String[] filterFields, Wrapper where) {
        Update update = new Update();
        update.setUpdateBean(bean);
        update.setUpdateNotNull(updateNotNull);
        update.setFilterFields(filterFields);
        update.setWhere(where);
        return mybatisSqlBeanDao.update(getSqlBeanDB(), clazz, update, false);
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int updateByBeanCondition(T bean, boolean updateNotNull, String where) {
        return mybatisSqlBeanDao.updateByBeanCondition(getSqlBeanDB(), clazz, bean, updateNotNull, null, where);
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int updateByBeanCondition(T bean, boolean updateNotNull, String[] filterFields, String where) {
        return mybatisSqlBeanDao.updateByBeanCondition(getSqlBeanDB(), clazz, bean, updateNotNull, filterFields, where);
    }

    @SuppressWarnings("unchecked")
    @DbSwitch(DbRole.MASTER)
    @Override
    public int insert(T... bean) {
        return mybatisSqlBeanDao.insertBean(getSqlBeanDB(), clazz, Arrays.asList(bean));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int insert(List<T> beanList) {
        return mybatisSqlBeanDao.insertBean(getSqlBeanDB(), clazz, beanList);
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int inset(Insert insert) {
        return mybatisSqlBeanDao.insert(getSqlBeanDB(), clazz, insert);
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public String backup() {
        String targetTableName = SqlBeanUtil.getTable(clazz).getName() + "_" + DateUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS");
        try {
            mybatisSqlBeanDao.backup(getSqlBeanDB(), clazz, targetTableName, null, null);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }
        return targetTableName;
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public void backup(String targetTableName) {
        mybatisSqlBeanDao.backup(getSqlBeanDB(), clazz, targetTableName, null, null);
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public void backup(String targetTableName, Column[] columns, Wrapper wrapper) {
        mybatisSqlBeanDao.backup(getSqlBeanDB(), clazz, targetTableName, columns, wrapper);
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int copy(String targetTableName, Wrapper wrapper) {
        return mybatisSqlBeanDao.copy(getSqlBeanDB(), clazz, targetTableName, null, wrapper);
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int copy(String targetTableName, Column[] columns, Wrapper wrapper) {
        return mybatisSqlBeanDao.copy(getSqlBeanDB(), clazz, targetTableName, columns, wrapper);
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public void dropTable() {
        mybatisSqlBeanDao.drop(getSqlBeanDB(), clazz);
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public void createTable() {
        mybatisSqlBeanDao.create(getSqlBeanDB(), clazz);
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
        return mybatisSqlBeanDao.selectTableList(getSqlBeanDB());
    }
}
