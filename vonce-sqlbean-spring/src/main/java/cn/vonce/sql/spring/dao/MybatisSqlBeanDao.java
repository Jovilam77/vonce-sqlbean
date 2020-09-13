package cn.vonce.sql.spring.dao;

import cn.vonce.sql.spring.provider.MybatisSqlBeanProvider;
import cn.vonce.sql.bean.Delete;
import cn.vonce.sql.bean.Insert;
import cn.vonce.sql.bean.Select;
import cn.vonce.sql.bean.Update;
import cn.vonce.sql.bean.*;
import cn.vonce.sql.config.SqlBeanConfig;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 通用的数据库操作
 *
 * @param <T>
 * @author Jovi
 * @version 1.0
 * @email 766255988@qq.com
 * @date 2018年5月15日下午3:56:51
 */
@Repository
public interface MybatisSqlBeanDao<T> {

    /**
     * 根据id条件查询
     *
     * @param sqlBeanConfig
     * @param clazz
     * @param id
     * @return
     */
    @SelectProvider(type = MybatisSqlBeanProvider.class, method = "selectById")
    T selectById(@Param("sqlBeanConfig") SqlBeanConfig sqlBeanConfig, @Param("clazz") Class<?> clazz, @Param("id") Object id);

    /**
     * 根据id条件查询(可指定返回类型、查询的表)
     *
     * @param sqlBeanConfig
     * @param clazz
     * @param returnType
     * @param id
     * @return
     */
    @SelectProvider(type = MybatisSqlBeanProvider.class, method = "selectById")
    <O> O selectByIdO(@Param("sqlBeanConfig") SqlBeanConfig sqlBeanConfig, @Param("clazz") Class<?> clazz, @Param("returnType") Class<O> returnType, @Param("id") Object id);

    /**
     * 根据ids条件查询
     *
     * @param sqlBeanConfig
     * @param clazz
     * @param ids
     * @return
     */
    @SelectProvider(type = MybatisSqlBeanProvider.class, method = "selectByIds")
    List<T> selectByIds(@Param("sqlBeanConfig") SqlBeanConfig sqlBeanConfig, @Param("clazz") Class<?> clazz, @Param("ids") Object... ids);

    /**
     * 根据id条件查询(可指定返回类型、查询的表)
     *
     * @param sqlBeanConfig
     * @param clazz
     * @param returnType
     * @param ids
     * @return
     */
    @SelectProvider(type = MybatisSqlBeanProvider.class, method = "selectByIds")
    <O> List<O> selectByIdsO(@Param("sqlBeanConfig") SqlBeanConfig sqlBeanConfig, @Param("clazz") Class<?> clazz, @Param("returnType") Class<O> returnType, @Param("ids") Object... ids);

    /**
     * 根据自定义条件查询 只返回一条记录
     *
     * @param sqlBeanConfig
     * @param clazz
     * @param select
     * @return
     */
    @SelectProvider(type = MybatisSqlBeanProvider.class, method = "select")
    T selectOne(@Param("sqlBeanConfig") SqlBeanConfig sqlBeanConfig, @Param("clazz") Class<?> clazz, @Param("select") Select select);

    /**
     * 根据自定义条件查询 只返回一条记录(可指定返回类型)
     *
     * @param sqlBeanConfig
     * @param clazz
     * @param returnType
     * @param select
     * @return
     */
    @SelectProvider(type = MybatisSqlBeanProvider.class, method = "select")
    <O> O selectOneO(@Param("sqlBeanConfig") SqlBeanConfig sqlBeanConfig, @Param("clazz") Class<?> clazz, @Param("returnType") Class<O> returnType, @Param("select") Select select);

    /**
     * 根据自定义条件查询返回Map
     *
     * @param sqlBeanConfig
     * @param clazz
     * @param select
     * @return
     */
    @SelectProvider(type = MybatisSqlBeanProvider.class, method = "select")
    Map<String, Object> selectMap(@Param("sqlBeanConfig") SqlBeanConfig sqlBeanConfig, @Param("clazz") Class<?> clazz, @Param("select") Select select);

    /**
     * 根据条件查询
     *
     * @param sqlBeanConfig
     * @param clazz
     * @param where
     * @param args
     * @return
     */
    @SelectProvider(type = MybatisSqlBeanProvider.class, method = "selectByCondition")
    T selectOneByCondition(@Param("sqlBeanConfig") SqlBeanConfig sqlBeanConfig, @Param("clazz") Class<?> clazz, @Param("where") String where, @Param("args") Object... args);

    /**
     * 根据条件查询(可指定返回类型、查询的表)
     *
     * @param sqlBeanConfig
     * @param clazz
     * @param returnType
     * @param where
     * @param args
     * @return
     */
    @SelectProvider(type = MybatisSqlBeanProvider.class, method = "selectByCondition")
    <O> O selectOneByConditionO(@Param("sqlBeanConfig") SqlBeanConfig sqlBeanConfig, @Param("clazz") Class<?> clazz, @Param("returnType") Class<O> returnType, @Param("where") String where, @Param("args") Object... args);

    /**
     * 根据条件查询(可指定返回类型、查询的表)
     *
     * @param sqlBeanConfig
     * @param clazz
     * @param paging
     * @param where
     * @param args
     * @return
     */
    @SelectProvider(type = MybatisSqlBeanProvider.class, method = "selectByCondition")
    <O> List<O> selectByConditionO(@Param("sqlBeanConfig") SqlBeanConfig sqlBeanConfig, @Param("clazz") Class<?> clazz, @Param("returnType") Class<O> returnType, @Param("paging") Paging paging, @Param("where") String where, @Param("args") Object... args);

    /**
     * 根据条件查询
     *
     * @param sqlBeanConfig
     * @param clazz
     * @param paging
     * @param where
     * @param args
     * @return
     */
    @SelectProvider(type = MybatisSqlBeanProvider.class, method = "selectByCondition")
    List<T> selectByCondition(@Param("sqlBeanConfig") SqlBeanConfig sqlBeanConfig, @Param("clazz") Class<?> clazz, @Param("paging") Paging paging, @Param("where") String where, @Param("args") Object... args);

    /**
     * 根据条件查询统计
     *
     * @param sqlBeanConfig
     * @param clazz
     * @param where
     * @param args
     * @return
     */
    @SelectProvider(type = MybatisSqlBeanProvider.class, method = "selectCountByCondition")
    long selectCountByCondition(@Param("sqlBeanConfig") SqlBeanConfig sqlBeanConfig, @Param("clazz") Class<?> clazz, @Param("where") String where, @Param("args") Object... args);

    /**
     * 查询全部
     *
     * @param sqlBeanConfig
     * @param clazz
     * @param paging
     * @return
     */
    @SelectProvider(type = MybatisSqlBeanProvider.class, method = "selectAll")
    List<T> selectAll(@Param("sqlBeanConfig") SqlBeanConfig sqlBeanConfig, @Param("clazz") Class<?> clazz, @Param("paging") Paging paging);

    /**
     * 查询全部(可指定返回类型、查询的表)
     *
     * @param sqlBeanConfig
     * @param clazz
     * @param returnType
     * @param paging
     * @return
     */
    @SelectProvider(type = MybatisSqlBeanProvider.class, method = "selectAll")
    <O> List<O> selectAllO(@Param("sqlBeanConfig") SqlBeanConfig sqlBeanConfig, @Param("clazz") Class<?> clazz, @Param("returnType") Class<O> returnType, @Param("paging") Paging paging);


    /**
     * 根据自定义条件查询（可自动分页）返回List<Map>结果集
     *
     * @param sqlBeanConfig
     * @param clazz
     * @param select
     * @return
     */
    @SelectProvider(type = MybatisSqlBeanProvider.class, method = "select")
    List<Map<String, Object>> selectMapList(@Param("sqlBeanConfig") SqlBeanConfig sqlBeanConfig, @Param("clazz") Class<?> clazz, @Param("select") Select select);

    /**
     * 根据自定义条件查询（可自动分页）返回List<T>
     *
     * @param sqlBeanConfig
     * @param clazz
     * @param select
     * @return
     */
    @SelectProvider(type = MybatisSqlBeanProvider.class, method = "select")
    List<T> select(@Param("sqlBeanConfig") SqlBeanConfig sqlBeanConfig, @Param("clazz") Class<?> clazz, @Param("select") Select select);

    /**
     * 根据自定义条件查询（可自动分页）返回List<O> (可指定返回类型)
     *
     * @param sqlBeanConfig
     * @param clazz
     * @param returnType
     * @param select
     * @return
     */
    @SelectProvider(type = MybatisSqlBeanProvider.class, method = "select")
    <O> List<O> selectO(@Param("sqlBeanConfig") SqlBeanConfig sqlBeanConfig, @Param("clazz") Class<?> clazz, @Param("returnType") Class<O> returnType, @Param("select") Select select);

    /**
     * 根据自定义条件统计
     *
     * @param sqlBeanConfig
     * @param clazz
     * @param select
     * @return
     */
    @SelectProvider(type = MybatisSqlBeanProvider.class, method = "count")
    long count(@Param("sqlBeanConfig") SqlBeanConfig sqlBeanConfig, @Param("clazz") Class<?> clazz, @Param("select") Select select);

    /**
     * 根据id条件删除
     *
     * @param sqlBeanConfig
     * @param clazz
     * @param id
     * @return
     */
    @DeleteProvider(type = MybatisSqlBeanProvider.class, method = "deleteById")
    long deleteById(@Param("sqlBeanConfig") SqlBeanConfig sqlBeanConfig, @Param("clazz") Class<?> clazz, @Param("id") Object id);

    /**
     * 根据条件删除
     *
     * @param sqlBeanConfig
     * @param clazz
     * @param where
     * @param args
     * @return
     */
    @DeleteProvider(type = MybatisSqlBeanProvider.class, method = "deleteByCondition")
    long deleteByCondition(@Param("sqlBeanConfig") SqlBeanConfig sqlBeanConfig, @Param("clazz") Class<?> clazz, @Param("where") String where, @Param("args") Object... args);

    /**
     * 删除
     *
     * @param sqlBeanConfig
     * @param clazz
     * @param delete
     * @param ignore
     * @return
     */
    @DeleteProvider(type = MybatisSqlBeanProvider.class, method = "delete")
    long delete(@Param("sqlBeanConfig") SqlBeanConfig sqlBeanConfig, @Param("clazz") Class<?> clazz, @Param("delete") Delete delete, @Param("ignore") boolean ignore);

    /**
     * 根据id逻辑删除
     *
     * @param sqlBeanConfig
     * @param clazz
     * @param id
     * @return
     */
    @UpdateProvider(type = MybatisSqlBeanProvider.class, method = "logicallyDeleteById")
    long logicallyDeleteById(@Param("sqlBeanConfig") SqlBeanConfig sqlBeanConfig, @Param("clazz") Class<?> clazz, @Param("id") Object id);

    /**
     * 根据条件逻辑删除
     *
     * @param sqlBeanConfig
     * @param clazz
     * @param where
     * @param args
     * @return
     */
    @UpdateProvider(type = MybatisSqlBeanProvider.class, method = "logicallyDeleteByCondition")
    long logicallyDeleteByCondition(@Param("sqlBeanConfig") SqlBeanConfig sqlBeanConfig, @Param("clazz") Class<?> clazz, @Param("where") String where, @Param("args") Object... args);

    /**
     * 更新
     *
     * @param sqlBeanConfig
     * @param update
     * @param ignore
     * @return
     */
    @UpdateProvider(type = MybatisSqlBeanProvider.class, method = "update")
    long update(@Param("sqlBeanConfig") SqlBeanConfig sqlBeanConfig, @Param("update") Update update, @Param("ignore") boolean ignore);

    /**
     * 根据id条更新
     *
     * @param sqlBeanConfig
     * @param bean
     * @param updateNotNull
     * @param filterFields
     * @return
     */
    @UpdateProvider(type = MybatisSqlBeanProvider.class, method = "updateById")
    long updateById(@Param("sqlBeanConfig") SqlBeanConfig sqlBeanConfig, @Param("bean") T bean, @Param("id") Object id, @Param("updateNotNull") boolean updateNotNull, @Param("filterFields") String[] filterFields);

    /**
     * 根据实体类id条件更新
     *
     * @param sqlBeanConfig
     * @param bean
     * @param updateNotNull
     * @param filterFields
     * @return
     */
    @UpdateProvider(type = MybatisSqlBeanProvider.class, method = "updateByBeanId")
    long updateByBeanId(@Param("sqlBeanConfig") SqlBeanConfig sqlBeanConfig, @Param("bean") T bean, @Param("updateNotNull") boolean updateNotNull, @Param("filterFields") String[] filterFields);

    /**
     * 根据条件更新
     *
     * @param sqlBeanConfig
     * @param bean
     * @param updateNotNull
     * @param filterFields
     * @param where
     * @param args
     * @return
     */
    @UpdateProvider(type = MybatisSqlBeanProvider.class, method = "updateByCondition")
    long updateByCondition(@Param("sqlBeanConfig") SqlBeanConfig sqlBeanConfig, @Param("bean") T bean, @Param("updateNotNull") boolean updateNotNull, @Param("filterFields") String[] filterFields, @Param("where") String where, @Param("args") Object... args);

    /**
     * 根据实体类字段条件更新
     *
     * @param sqlBeanConfig
     * @param bean
     * @param updateNotNull
     * @param filterFields
     * @param where
     * @return
     */
    @UpdateProvider(type = MybatisSqlBeanProvider.class, method = "updateByBeanCondition")
    long updateByBeanCondition(@Param("sqlBeanConfig") SqlBeanConfig sqlBeanConfig, @Param("bean") T bean, @Param("updateNotNull") boolean updateNotNull, @Param("filterFields") String[] filterFields, @Param("where") String where);

    /**
     * 插入数据
     *
     * @param sqlBeanConfig
     * @param beanList
     * @return
     */
    @InsertProvider(type = MybatisSqlBeanProvider.class, method = "insertBean")
    long insertBean(@Param("sqlBeanConfig") SqlBeanConfig sqlBeanConfig, @Param("beanList") List<T> beanList);

    /**
     * 插入数据
     *
     * @param sqlBeanConfig
     * @param insert
     * @return
     */
    @InsertProvider(type = MybatisSqlBeanProvider.class, method = "insert")
    long insert(@Param("sqlBeanConfig") SqlBeanConfig sqlBeanConfig, @Param("insert") Insert insert);

    /**
     * 删除表
     *
     * @param clazz
     * @return
     */
    @UpdateProvider(type = MybatisSqlBeanProvider.class, method = "drop")
    void drop(@Param("clazz") Class<?> clazz);

    /**
     * 创建表
     *
     * @param sqlBeanConfig
     * @param clazz
     * @return
     */
    @UpdateProvider(type = MybatisSqlBeanProvider.class, method = "create")
    void create(@Param("sqlBeanConfig") SqlBeanConfig sqlBeanConfig, @Param("clazz") Class<?> clazz);

    /**
     * 获取表名列表
     *
     * @param sqlBeanConfig
     * @return
     */
    @SelectProvider(type = MybatisSqlBeanProvider.class, method = "selectTableList")
    List<String> selectTableList(@Param("sqlBeanConfig") SqlBeanConfig sqlBeanConfig);

}