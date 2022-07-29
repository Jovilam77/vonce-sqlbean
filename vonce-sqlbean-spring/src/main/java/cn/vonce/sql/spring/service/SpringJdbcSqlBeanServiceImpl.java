package cn.vonce.sql.spring.service;

import cn.vonce.sql.bean.*;
import cn.vonce.sql.config.SqlBeanConfig;
import cn.vonce.sql.config.SqlBeanDB;
import cn.vonce.sql.enumerate.DbType;
import cn.vonce.sql.exception.SqlBeanException;
import cn.vonce.sql.helper.Wrapper;
import cn.vonce.sql.page.PageHelper;
import cn.vonce.sql.page.ResultData;
import cn.vonce.sql.service.TableService;
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
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
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
public class SpringJdbcSqlBeanServiceImpl<T, ID> extends BaseSqlBeanServiceImpl implements SqlBeanService<T, ID>, TableService {

    private Logger logger = LoggerFactory.getLogger(SpringJdbcSqlBeanServiceImpl.class);

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
                    logger.error(e.getMessage());
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
                Connection connection = jdbcTemplate.getDataSource().getConnection();
                productName = connection.getMetaData().getDatabaseProductName();
                connection.close();
            } catch (SQLException e) {
                logger.error(e.getMessage());
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
        return singleResult(jdbcTemplate.query(SqlBeanProvider.selectByIdSql(getSqlBeanDB(), clazz, id), new SpringJbdcSqlBeanMapper<T>(clazz, clazz)));
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public <R> R selectById(Class<R> returnType, ID id) {
        if (id == null) {
            return null;
        }
//        if (!SqlBeanUtil.isBaseType(returnType.getName()) && !SqlBeanUtil.isMap(returnType.getName())) {
//            clazz = returnType;
//        }
        return singleResult(jdbcTemplate.query(SqlBeanProvider.selectByIdSql(getSqlBeanDB(), clazz, id), new SpringJbdcSqlBeanMapper<R>(clazz, returnType)));
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public List<T> selectByIds(ID... ids) {
        if (ids == null || ids.length == 0) {
            throw new SqlBeanException("selectByIds方法ids参数必须拥有一个值");
        }
        return jdbcTemplate.query(SqlBeanProvider.selectByIdsSql(getSqlBeanDB(), clazz, ids), new SpringJbdcSqlBeanMapper<T>(clazz, clazz));
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public <R> List<R> selectByIds(Class<R> returnType, ID... ids) {
        if (ids == null || ids.length == 0) {
            throw new SqlBeanException("selectByIds方法ids参数必须拥有一个值");
        }
//        if (!SqlBeanUtil.isBaseType(returnType.getName()) && !SqlBeanUtil.isMap(returnType.getName())) {
//            clazz = returnType;
//        }
        return jdbcTemplate.query(SqlBeanProvider.selectByIdsSql(getSqlBeanDB(), clazz, ids), new SpringJbdcSqlBeanMapper<R>(clazz, returnType));
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public T selectOne(Select select) {
        return singleResult(jdbcTemplate.query(SqlBeanProvider.selectSql(getSqlBeanDB(), clazz, select), new SpringJbdcSqlBeanMapper<T>(clazz, clazz)));
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public <R> R selectOne(Class<R> returnType, Select select) {
//        if (!SqlBeanUtil.isBaseType(returnType.getName()) && !SqlBeanUtil.isMap(returnType.getName())) {
//            clazz = returnType;
//        }
        return singleResult(jdbcTemplate.query(SqlBeanProvider.selectSql(getSqlBeanDB(), clazz, select), new SpringJbdcSqlBeanMapper<R>(clazz, returnType)));
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public Map<String, Object> selectMap(Select select) {
        return singleResult(jdbcTemplate.query(SqlBeanProvider.selectSql(getSqlBeanDB(), clazz, select), new SpringJbdcSqlBeanMapper<Map<String, Object>>(clazz, Map.class)));
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public T selectOneBy(String where, Object... args) {
        return singleResult(jdbcTemplate.query(SqlBeanProvider.selectByConditionSql(getSqlBeanDB(), clazz, null, where, args), new SpringJbdcSqlBeanMapper<T>(clazz, clazz)));
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public <R> R selectOneBy(Class<R> returnType, String where, Object... args) {
//        if (!SqlBeanUtil.isBaseType(returnType.getName()) && !SqlBeanUtil.isMap(returnType.getName())) {
//            clazz = returnType;
//        }
        return singleResult(jdbcTemplate.query(SqlBeanProvider.selectByConditionSql(getSqlBeanDB(), clazz, null, where, args), new SpringJbdcSqlBeanMapper<>(clazz, returnType)));
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public T selectOneBy(Wrapper wrapper) {
        Select select = new Select();
        select.where(wrapper);
        return singleResult(jdbcTemplate.query(SqlBeanProvider.selectSql(getSqlBeanDB(), clazz, select), new SpringJbdcSqlBeanMapper<>(clazz, clazz)));
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public <R> R selectOneBy(Class<R> returnType, Wrapper wrapper) {
        Select select = new Select();
        select.where(wrapper);
//        if (!SqlBeanUtil.isBaseType(returnType.getName()) && !SqlBeanUtil.isMap(returnType.getName())) {
//            clazz = returnType;
//        }
        return singleResult(jdbcTemplate.query(SqlBeanProvider.selectSql(getSqlBeanDB(), clazz, select), new SpringJbdcSqlBeanMapper<>(clazz, returnType)));
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public <R> List<R> selectBy(Class<R> returnType, String where, Object... args) {
//        if (!SqlBeanUtil.isBaseType(returnType.getName()) && !SqlBeanUtil.isMap(returnType.getName())) {
//            clazz = returnType;
//        }
        return jdbcTemplate.query(SqlBeanProvider.selectByConditionSql(getSqlBeanDB(), clazz, null, where, args), new SpringJbdcSqlBeanMapper<>(clazz, returnType));
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public <R> List<R> selectBy(Class<R> returnType, Wrapper wrapper) {
        Select select = new Select();
        select.where(wrapper);
//        if (!SqlBeanUtil.isBaseType(returnType.getName()) && !SqlBeanUtil.isMap(returnType.getName())) {
//            clazz = returnType;
//        }
        return jdbcTemplate.query(SqlBeanProvider.selectSql(getSqlBeanDB(), clazz, select), new SpringJbdcSqlBeanMapper<>(clazz, returnType));
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public <R> List<R> selectBy(Class<R> returnType, Paging paging, String where, Object... args) {
//        if (!SqlBeanUtil.isBaseType(returnType.getName()) && !SqlBeanUtil.isMap(returnType.getName())) {
//            clazz = returnType;
//        }
        return jdbcTemplate.query(SqlBeanProvider.selectByConditionSql(getSqlBeanDB(), clazz, paging, where, args), new SpringJbdcSqlBeanMapper<>(clazz, returnType));
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public <R> List<R> selectBy(Class<R> returnType, Paging paging, Wrapper wrapper) {
        Select select = new Select();
        select.where(wrapper);
        select.page(paging.getPagenum(), paging.getPagesize(), paging.getStartByZero());
        select.orderBy(paging.getOrders());
//        if (!SqlBeanUtil.isBaseType(returnType.getName()) && !SqlBeanUtil.isMap(returnType.getName())) {
//            clazz = returnType;
//        }
        return jdbcTemplate.query(SqlBeanProvider.selectSql(getSqlBeanDB(), clazz, select), new SpringJbdcSqlBeanMapper<>(clazz, returnType));
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public List<T> selectBy(String where, Object... args) {
        return jdbcTemplate.query(SqlBeanProvider.selectByConditionSql(getSqlBeanDB(), clazz, null, where, args), new SpringJbdcSqlBeanMapper<>(clazz, clazz));
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public List<T> selectBy(Wrapper wrapper) {
        Select select = new Select();
        select.where(wrapper);
        return jdbcTemplate.query(SqlBeanProvider.selectSql(getSqlBeanDB(), clazz, select), new SpringJbdcSqlBeanMapper<>(clazz, clazz));
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public List<T> selectBy(Paging paging, String where, Object... args) {
        return jdbcTemplate.query(SqlBeanProvider.selectByConditionSql(getSqlBeanDB(), clazz, paging, where, args), new SpringJbdcSqlBeanMapper<>(clazz, clazz));
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public List<T> selectBy(Paging paging, Wrapper wrapper) {
        Select select = new Select();
        select.where(wrapper);
        select.page(paging.getPagenum(), paging.getPagesize(), paging.getStartByZero());
        select.orderBy(paging.getOrders());
        return jdbcTemplate.query(SqlBeanProvider.selectSql(getSqlBeanDB(), clazz, select), new SpringJbdcSqlBeanMapper<>(clazz, clazz));
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public int countBy(String where, Object... args) {
        return jdbcTemplate.queryForObject(SqlBeanProvider.countByConditionSql(getSqlBeanDB(), clazz, where, args), new SpringJbdcSqlBeanMapper<>(clazz, Integer.class));
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public int countBy(Wrapper wrapper) {
        Select select = new Select();
        select.where(wrapper);
        return jdbcTemplate.queryForObject(SqlBeanProvider.countSql(getSqlBeanDB(), clazz, select), new SpringJbdcSqlBeanMapper<>(clazz, Integer.class));
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public int count() {
        return jdbcTemplate.queryForObject(SqlBeanProvider.countByConditionSql(getSqlBeanDB(), clazz, null, null), new SpringJbdcSqlBeanMapper<>(clazz, Integer.class));
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public List<T> select() {
        return jdbcTemplate.query(SqlBeanProvider.selectAllSql(getSqlBeanDB(), clazz, null), new SpringJbdcSqlBeanMapper<T>(clazz, clazz));
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public List<T> select(Paging paging) {
        return jdbcTemplate.query(SqlBeanProvider.selectAllSql(getSqlBeanDB(), clazz, paging), new SpringJbdcSqlBeanMapper<T>(clazz, clazz));
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public <R> List<R> select(Class<R> returnType) {
//        if (!SqlBeanUtil.isBaseType(returnType.getName()) && !SqlBeanUtil.isMap(returnType.getName())) {
//            clazz = returnType;
//        }
        return jdbcTemplate.query(SqlBeanProvider.selectAllSql(getSqlBeanDB(), clazz, null), new SpringJbdcSqlBeanMapper<R>(clazz, returnType));
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public <R> List<R> select(Class<R> returnType, Paging paging) {
//        if (!SqlBeanUtil.isBaseType(returnType.getName()) && !SqlBeanUtil.isMap(returnType.getName())) {
//            clazz = returnType;
//        }
        return jdbcTemplate.query(SqlBeanProvider.selectAllSql(getSqlBeanDB(), clazz, paging), new SpringJbdcSqlBeanMapper<R>(clazz, returnType));
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public List<Map<String, Object>> selectMapList(Select select) {
        return jdbcTemplate.query(SqlBeanProvider.selectSql(getSqlBeanDB(), clazz, select), new SpringJbdcSqlBeanMapper<Map<String, Object>>(clazz, Map.class));
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public <R> List<R> select(Class<R> returnType, Select select) {
//        if (!SqlBeanUtil.isBaseType(returnType.getName()) && !SqlBeanUtil.isMap(returnType.getName())) {
//            clazz = returnType;
//        }
        return jdbcTemplate.query(SqlBeanProvider.selectSql(getSqlBeanDB(), clazz, select), new SpringJbdcSqlBeanMapper<R>(clazz, returnType));
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public List<T> select(Select select) {
        return jdbcTemplate.query(SqlBeanProvider.selectSql(getSqlBeanDB(), clazz, select), new SpringJbdcSqlBeanMapper<T>(clazz, clazz));
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public int count(Select select) {
        return jdbcTemplate.queryForObject(SqlBeanProvider.countSql(getSqlBeanDB(), clazz, select), new SpringJbdcSqlBeanMapper<Integer>(clazz, Integer.class));
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public int count(Class<?> returnType, Select select) {
//        if (SqlBeanUtil.isBaseType(returnType.getName()) || SqlBeanUtil.isMap(returnType.getName())) {
//            return jdbcTemplate.queryForObject(SqlBeanProvider.countSql(getSqlBeanDB(), clazz, select), Integer.class);
//        }
        return jdbcTemplate.queryForObject(SqlBeanProvider.countSql(getSqlBeanDB(), clazz, select), Integer.class);
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public ResultData<T> paging(Select select, PageHelper<T> pageHelper) {
        pageHelper.paging(select, this);
        return pageHelper.getResultData();
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public ResultData<T> paging(Select select, int pagenum, int pagesize) {
        PageHelper<T> pageHelper = new PageHelper<>(pagenum, pagesize);
        pageHelper.paging(select, this);
        return pageHelper.getResultData();
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public <R> ResultData<R> paging(Class<R> tClazz, Select select, PageHelper<R> pageHelper) {
        pageHelper.paging(tClazz, select, this);
        return pageHelper.getResultData();
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public <R> ResultData<R> paging(Class<R> tClazz, Select select, int pagenum, int pagesize) {
        PageHelper<R> pageHelper = new PageHelper<>(pagenum, pagesize);
        pageHelper.paging(tClazz, select, this);
        return pageHelper.getResultData();
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int deleteById(ID... id) {
        if (id == null || id.length == 0) {
            throw new SqlBeanException("deleteById方法id参数必须拥有一个值");
        }
        return jdbcTemplate.update(SqlBeanProvider.deleteByIdSql(getSqlBeanDB(), clazz, id));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int deleteBy(String where, Object... args) {
        return jdbcTemplate.update(SqlBeanProvider.deleteByConditionSql(getSqlBeanDB(), clazz, where, args));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int deleteBy(Wrapper wrapper) {
        Delete delete = new Delete();
        delete.where(wrapper);
        return jdbcTemplate.update(SqlBeanProvider.deleteSql(getSqlBeanDB(), clazz, delete, false));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int delete(Delete delete) {
        return jdbcTemplate.update(SqlBeanProvider.deleteSql(getSqlBeanDB(), clazz, delete, false));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int delete(Delete delete, boolean ignore) {
        return jdbcTemplate.update(SqlBeanProvider.deleteSql(getSqlBeanDB(), clazz, delete, ignore));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int logicallyDeleteById(ID... id) {
        if (id == null || id.length == 0) {
            throw new SqlBeanException("logicallyDeleteById方法id参数必须拥有一个值");
        }
        return jdbcTemplate.update(SqlBeanProvider.logicallyDeleteByIdSql(getSqlBeanDB(), clazz, id));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int logicallyDeleteBy(String where, Object... args) {
        return jdbcTemplate.update(SqlBeanProvider.logicallyDeleteByConditionSql(getSqlBeanDB(), clazz, where, args));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int logicallyDeleteBy(Wrapper wrapper) {
        return jdbcTemplate.update(SqlBeanProvider.logicallyDeleteByConditionSql(getSqlBeanDB(), clazz, wrapper));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int update(Update update) {
        return jdbcTemplate.update(SqlBeanProvider.updateSql(getSqlBeanDB(), clazz, update, false));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int update(Update update, boolean ignore) {
        return jdbcTemplate.update(SqlBeanProvider.updateSql(getSqlBeanDB(), clazz, update, ignore));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int updateById(T bean, ID id) {
        return jdbcTemplate.update(SqlBeanProvider.updateByIdSql(getSqlBeanDB(), clazz, bean, id, true, false, null));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int updateById(T bean, ID id, boolean updateNotNull, boolean optimisticLock) {
        return jdbcTemplate.update(SqlBeanProvider.updateByIdSql(getSqlBeanDB(), clazz, bean, id, updateNotNull, optimisticLock, null));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int updateByBeanId(T bean) {
        return jdbcTemplate.update(SqlBeanProvider.updateByBeanIdSql(getSqlBeanDB(), clazz, bean, true, false, null));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int updateById(T bean, ID id, boolean updateNotNull, boolean optimisticLock, String[] filterFields) {
        return jdbcTemplate.update(SqlBeanProvider.updateByIdSql(getSqlBeanDB(), clazz, bean, id, updateNotNull, optimisticLock, filterFields));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int updateBy(T bean, String where, Object... args) {
        return jdbcTemplate.update(SqlBeanProvider.updateByConditionSql(getSqlBeanDB(), clazz, bean, true, false, null, where, args));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int updateByBeanId(T bean, boolean updateNotNull, boolean optimisticLock) {
        return jdbcTemplate.update(SqlBeanProvider.updateByBeanIdSql(getSqlBeanDB(), clazz, bean, updateNotNull, optimisticLock, null));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int updateByBeanId(T bean, boolean updateNotNull, boolean optimisticLock, String[] filterFields) {
        return jdbcTemplate.update(SqlBeanProvider.updateByBeanIdSql(getSqlBeanDB(), clazz, bean, updateNotNull, optimisticLock, filterFields));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int updateBy(T bean, boolean updateNotNull, boolean optimisticLock, String where, Object... args) {
        return jdbcTemplate.update(SqlBeanProvider.updateByConditionSql(getSqlBeanDB(), clazz, bean, updateNotNull, optimisticLock, null, where, args));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int updateBy(T bean, Wrapper wrapper) {
        Update update = new Update();
        update.setUpdateBean(bean);
        update.setUpdateNotNull(true);
        update.setOptimisticLock(false);
        update.where(wrapper);
        return jdbcTemplate.update(SqlBeanProvider.updateSql(getSqlBeanDB(), clazz, update, false));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int updateBy(T bean, boolean updateNotNull, boolean optimisticLock, Wrapper wrapper) {
        Update update = new Update();
        update.setUpdateBean(bean);
        update.setUpdateNotNull(updateNotNull);
        update.setOptimisticLock(optimisticLock);
        update.where(wrapper);
        return jdbcTemplate.update(SqlBeanProvider.updateSql(getSqlBeanDB(), clazz, update, false));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int updateBy(T bean, boolean updateNotNull, boolean optimisticLock, String[] filterFields, String where, Object... args) {
        return jdbcTemplate.update(SqlBeanProvider.updateByConditionSql(getSqlBeanDB(), clazz, bean, updateNotNull, optimisticLock, filterFields, where, args));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int updateBy(T bean, boolean updateNotNull, boolean optimisticLock, String[] filterFields, Wrapper wrapper) {
        Update update = new Update();
        update.setUpdateBean(bean);
        update.setUpdateNotNull(updateNotNull);
        update.setOptimisticLock(optimisticLock);
        update.setFilterFields(filterFields);
        update.where(wrapper);
        return jdbcTemplate.update(SqlBeanProvider.updateSql(getSqlBeanDB(), clazz, update, false));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int updateByBean(T bean, String where) {
        return jdbcTemplate.update(SqlBeanProvider.updateByBeanConditionSql(getSqlBeanDB(), clazz, bean, true, false, null, where));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int updateByBean(T bean, boolean updateNotNull, boolean optimisticLock, String where) {
        return jdbcTemplate.update(SqlBeanProvider.updateByBeanConditionSql(getSqlBeanDB(), clazz, bean, updateNotNull, optimisticLock, null, where));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int updateByBean(T bean, boolean updateNotNull, boolean optimisticLock, String[] filterFields, String where) {
        return jdbcTemplate.update(SqlBeanProvider.updateByBeanConditionSql(getSqlBeanDB(), clazz, bean, updateNotNull, optimisticLock, filterFields, where));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int insert(T... bean) {
        if (bean == null || bean.length == 0) {
            throw new SqlBeanException("insert方法bean参数必须拥有一个值");
        }
        return jdbcTemplate.update(SqlBeanProvider.insertBeanSql(getSqlBeanDB(), clazz, bean));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int insert(List<T> beanList) {
        if (beanList == null || beanList.size() == 0) {
            throw new SqlBeanException("insert方法beanList参数至少拥有一个值");
        }
        return jdbcTemplate.update(SqlBeanProvider.insertBeanSql(getSqlBeanDB(), clazz, beanList));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int insert(Insert insert) {
        return jdbcTemplate.update(SqlBeanProvider.insertBeanSql(getSqlBeanDB(), clazz, insert));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public String backup() {
        String targetTableName = SqlBeanUtil.getTable(clazz).getName() + "_" + DateUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS");
        jdbcTemplate.update(SqlBeanProvider.backupSql(getSqlBeanDB(), clazz, null, targetTableName, null, null));
        return targetTableName;
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public void backup(String targetTableName) {
        jdbcTemplate.update(SqlBeanProvider.backupSql(getSqlBeanDB(), clazz, null, targetTableName, null, null));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public void backup(String targetSchema, String targetTableName) {
        jdbcTemplate.update(SqlBeanProvider.backupSql(getSqlBeanDB(), clazz, targetSchema, targetTableName, null, null));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public void backup(String targetTableName, Column[] columns, Wrapper wrapper) {
        jdbcTemplate.update(SqlBeanProvider.backupSql(getSqlBeanDB(), clazz, null, targetTableName, columns, wrapper));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public void backup(String targetSchema, String targetTableName, Column[] columns, Wrapper wrapper) {
        jdbcTemplate.update(SqlBeanProvider.backupSql(getSqlBeanDB(), clazz, targetSchema, targetTableName, columns, wrapper));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int copy(String targetTableName, Wrapper wrapper) {
        return jdbcTemplate.update(SqlBeanProvider.copySql(getSqlBeanDB(), clazz, null, targetTableName, null, wrapper));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int copy(String targetSchema, String targetTableName, Wrapper wrapper) {
        return jdbcTemplate.update(SqlBeanProvider.copySql(getSqlBeanDB(), clazz, targetSchema, targetTableName, null, wrapper));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int copy(String targetTableName, Column[] columns, Wrapper wrapper) {
        return jdbcTemplate.update(SqlBeanProvider.copySql(getSqlBeanDB(), clazz, null, targetTableName, columns, wrapper));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public int copy(String targetSchema, String targetTableName, Column[] columns, Wrapper wrapper) {
        return jdbcTemplate.update(SqlBeanProvider.copySql(getSqlBeanDB(), clazz, targetSchema, targetTableName, columns, wrapper));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public void dropTable() {
        SqlBeanDB sqlBeanDB = getSqlBeanDB();
        if (sqlBeanDB.getDbType() != DbType.MySQL && sqlBeanDB.getDbType() != DbType.MariaDB && sqlBeanDB.getDbType() != DbType.PostgreSQL && sqlBeanDB.getDbType() != DbType.SQLServer && sqlBeanDB.getDbType() != DbType.H2) {
            List<TableInfo> nameList = jdbcTemplate.queryForList(SqlBeanProvider.selectTableListSql(sqlBeanDB, SqlBeanUtil.getTable(clazz).getName()), TableInfo.class);
            if (nameList == null || nameList.isEmpty()) {
                return;
            }
        }
        jdbcTemplate.update(SqlBeanProvider.dropTableSql(sqlBeanDB, clazz));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public void createTable() {
        jdbcTemplate.update(SqlBeanProvider.createTableSql(getSqlBeanDB(), clazz));
    }

    @DbSwitch(DbRole.MASTER)
    @Override
    public void dropAndCreateTable() {
        dropTable();
        createTable();
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public List<TableInfo> getTableList(String tableName) {
        return jdbcTemplate.query(SqlBeanProvider.selectTableListSql(getSqlBeanDB(), tableName), new SpringJbdcSqlBeanMapper<TableInfo>(TableInfo.class, TableInfo.class));
    }

    @DbSwitch(DbRole.SLAVE)
    @Override
    public List<ColumnInfo> getColumnInfoList(String tableName) {
        return jdbcTemplate.query(SqlBeanProvider.selectColumnListSql(getSqlBeanDB(), tableName), new SpringJbdcSqlBeanMapper<ColumnInfo>(ColumnInfo.class, ColumnInfo.class));
    }

    private static <T> T singleResult(Collection<T> results) {
        int size = results != null ? results.size() : 0;
        if (size == 0) {
            return null;
        } else if (results.size() > 1) {
            throw new SqlBeanException("执行返回的结果不止一条, size: " + results.size());
        } else {
            return results.iterator().next();
        }
    }


}
