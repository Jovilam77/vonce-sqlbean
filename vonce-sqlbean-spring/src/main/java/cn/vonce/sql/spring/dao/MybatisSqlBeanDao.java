package cn.vonce.sql.spring.dao;

import cn.vonce.sql.config.SqlBeanDBConfig;
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
     * @param sqlBeanDBConfig
     * @param clazz
     * @param id
     * @return
     */
    @SelectProvider(type = MybatisSqlBeanProvider.class, method = "selectById")
    T selectById(@Param("sqlBeanDBConfig") SqlBeanDBConfig sqlBeanDBConfig, @Param("clazz") Class<?> clazz, @Param("id") Object id);

    /**
     * 根据id条件查询(可指定返回类型、查询的表)
     *
     * @param sqlBeanDBConfig
     * @param clazz
     * @param returnType
     * @param id
     * @return
     */
    @SelectProvider(type = MybatisSqlBeanProvider.class, method = "selectById")
    <O> O selectByIdO(@Param("sqlBeanDBConfig") SqlBeanDBConfig sqlBeanDBConfig, @Param("clazz") Class<?> clazz, @Param("returnType") Class<O> returnType, @Param("id") Object id);

    /**
     * 根据ids条件查询
     *
     * @param sqlBeanDBConfig
     * @param clazz
     * @param ids
     * @return
     */
    @SelectProvider(type = MybatisSqlBeanProvider.class, method = "selectByIds")
    List<T> selectByIds(@Param("sqlBeanDBConfig") SqlBeanDBConfig sqlBeanDBConfig, @Param("clazz") Class<?> clazz, @Param("ids") Object... ids);

    /**
     * 根据id条件查询(可指定返回类型、查询的表)
     *
     * @param sqlBeanDBConfig
     * @param clazz
     * @param returnType
     * @param ids
     * @return
     */
    @SelectProvider(type = MybatisSqlBeanProvider.class, method = "selectByIds")
    <O> List<O> selectByIdsO(@Param("sqlBeanDBConfig") SqlBeanDBConfig sqlBeanDBConfig, @Param("clazz") Class<?> clazz, @Param("returnType") Class<O> returnType, @Param("ids") Object... ids);

    /**
     * 根据自定义条件查询 只返回一条记录
     *
     * @param sqlBeanDBConfig
     * @param clazz
     * @param select
     * @return
     */
    @SelectProvider(type = MybatisSqlBeanProvider.class, method = "select")
    T selectOne(@Param("sqlBeanDBConfig") SqlBeanDBConfig sqlBeanDBConfig, @Param("clazz") Class<?> clazz, @Param("select") Select select);

    /**
     * 根据自定义条件查询 只返回一条记录(可指定返回类型)
     *
     * @param sqlBeanDBConfig
     * @param clazz
     * @param returnType
     * @param select
     * @return
     */
    @SelectProvider(type = MybatisSqlBeanProvider.class, method = "select")
    <O> O selectOneO(@Param("sqlBeanDBConfig") SqlBeanDBConfig sqlBeanDBConfig, @Param("clazz") Class<?> clazz, @Param("returnType") Class<O> returnType, @Param("select") Select select);

    /**
     * 根据自定义条件查询返回Map
     *
     * @param sqlBeanDBConfig
     * @param clazz
     * @param select
     * @return
     */
    @SelectProvider(type = MybatisSqlBeanProvider.class, method = "select")
    Map<String, Object> selectMap(@Param("sqlBeanDBConfig") SqlBeanDBConfig sqlBeanDBConfig, @Param("clazz") Class<?> clazz, @Param("select") Select select);

    /**
     * 根据条件查询
     *
     * @param sqlBeanDBConfig
     * @param clazz
     * @param where
     * @param args
     * @return
     */
    @SelectProvider(type = MybatisSqlBeanProvider.class, method = "selectByCondition")
    T selectOneByCondition(@Param("sqlBeanDBConfig") SqlBeanDBConfig sqlBeanDBConfig, @Param("clazz") Class<?> clazz, @Param("where") String where, @Param("args") Object... args);

    /**
     * 根据条件查询(可指定返回类型、查询的表)
     *
     * @param sqlBeanDBConfig
     * @param clazz
     * @param returnType
     * @param where
     * @param args
     * @return
     */
    @SelectProvider(type = MybatisSqlBeanProvider.class, method = "selectByCondition")
    <O> O selectOneByConditionO(@Param("sqlBeanDBConfig") SqlBeanDBConfig sqlBeanDBConfig, @Param("clazz") Class<?> clazz, @Param("returnType") Class<O> returnType, @Param("where") String where, @Param("args") Object... args);

    /**
     * 根据条件查询(可指定返回类型、查询的表)
     *
     * @param sqlBeanDBConfig
     * @param clazz
     * @param paging
     * @param where
     * @param args
     * @return
     */
    @SelectProvider(type = MybatisSqlBeanProvider.class, method = "selectByCondition")
    <O> List<O> selectByConditionO(@Param("sqlBeanDBConfig") SqlBeanDBConfig sqlBeanDBConfig, @Param("clazz") Class<?> clazz, @Param("returnType") Class<O> returnType, @Param("paging") Paging paging, @Param("where") String where, @Param("args") Object... args);

    /**
     * 根据条件查询
     *
     * @param sqlBeanDBConfig
     * @param clazz
     * @param paging
     * @param where
     * @param args
     * @return
     */
    @SelectProvider(type = MybatisSqlBeanProvider.class, method = "selectByCondition")
    List<T> selectByCondition(@Param("sqlBeanDBConfig") SqlBeanDBConfig sqlBeanDBConfig, @Param("clazz") Class<?> clazz, @Param("paging") Paging paging, @Param("where") String where, @Param("args") Object... args);

    /**
     * 根据条件查询统计
     *
     * @param sqlBeanDBConfig
     * @param clazz
     * @param where
     * @param args
     * @return
     */
    @SelectProvider(type = MybatisSqlBeanProvider.class, method = "selectCountByCondition")
    long selectCountByCondition(@Param("sqlBeanDBConfig") SqlBeanDBConfig sqlBeanDBConfig, @Param("clazz") Class<?> clazz, @Param("where") String where, @Param("args") Object... args);

    /**
     * 查询全部
     *
     * @param sqlBeanDBConfig
     * @param clazz
     * @param paging
     * @return
     */
    @SelectProvider(type = MybatisSqlBeanProvider.class, method = "selectAll")
    List<T> selectAll(@Param("sqlBeanDBConfig") SqlBeanDBConfig sqlBeanDBConfig, @Param("clazz") Class<?> clazz, @Param("paging") Paging paging);

    /**
     * 查询全部(可指定返回类型、查询的表)
     *
     * @param sqlBeanDBConfig
     * @param clazz
     * @param returnType
     * @param paging
     * @return
     */
    @SelectProvider(type = MybatisSqlBeanProvider.class, method = "selectAll")
    <O> List<O> selectAllO(@Param("sqlBeanDBConfig") SqlBeanDBConfig sqlBeanDBConfig, @Param("clazz") Class<?> clazz, @Param("returnType") Class<O> returnType, @Param("paging") Paging paging);


    /**
     * 根据自定义条件查询（可自动分页）返回List<Map>结果集
     *
     * @param sqlBeanDBConfig
     * @param clazz
     * @param select
     * @return
     */
    @SelectProvider(type = MybatisSqlBeanProvider.class, method = "select")
    List<Map<String, Object>> selectMapList(@Param("sqlBeanDBConfig") SqlBeanDBConfig sqlBeanDBConfig, @Param("clazz") Class<?> clazz, @Param("select") Select select);

    /**
     * 根据自定义条件查询（可自动分页）返回List<T>
     *
     * @param sqlBeanDBConfig
     * @param clazz
     * @param select
     * @return
     */
    @SelectProvider(type = MybatisSqlBeanProvider.class, method = "select")
    List<T> select(@Param("sqlBeanDBConfig") SqlBeanDBConfig sqlBeanDBConfig, @Param("clazz") Class<?> clazz, @Param("select") Select select);

    /**
     * 根据自定义条件查询（可自动分页）返回List<O> (可指定返回类型)
     *
     * @param sqlBeanDBConfig
     * @param clazz
     * @param returnType
     * @param select
     * @return
     */
    @SelectProvider(type = MybatisSqlBeanProvider.class, method = "select")
    <O> List<O> selectO(@Param("sqlBeanDBConfig") SqlBeanDBConfig sqlBeanDBConfig, @Param("clazz") Class<?> clazz, @Param("returnType") Class<O> returnType, @Param("select") Select select);

    /**
     * 根据自定义条件统计
     *
     * @param sqlBeanDBConfig
     * @param clazz
     * @param select
     * @return
     */
    @SelectProvider(type = MybatisSqlBeanProvider.class, method = "count")
    long count(@Param("sqlBeanDBConfig") SqlBeanDBConfig sqlBeanDBConfig, @Param("clazz") Class<?> clazz, @Param("select") Select select);

    /**
     * 根据id条件删除
     *
     * @param sqlBeanDBConfig
     * @param clazz
     * @param id
     * @return
     */
    @DeleteProvider(type = MybatisSqlBeanProvider.class, method = "deleteById")
    long deleteById(@Param("sqlBeanDBConfig") SqlBeanDBConfig sqlBeanDBConfig, @Param("clazz") Class<?> clazz, @Param("id") Object id);

    /**
     * 根据条件删除
     *
     * @param sqlBeanDBConfig
     * @param clazz
     * @param where
     * @param args
     * @return
     */
    @DeleteProvider(type = MybatisSqlBeanProvider.class, method = "deleteByCondition")
    long deleteByCondition(@Param("sqlBeanDBConfig") SqlBeanDBConfig sqlBeanDBConfig, @Param("clazz") Class<?> clazz, @Param("where") String where, @Param("args") Object... args);

    /**
     * 删除
     *
     * @param sqlBeanDBConfig
     * @param clazz
     * @param delete
     * @param ignore
     * @return
     */
    @DeleteProvider(type = MybatisSqlBeanProvider.class, method = "delete")
    long delete(@Param("sqlBeanDBConfig") SqlBeanDBConfig sqlBeanDBConfig, @Param("clazz") Class<?> clazz, @Param("delete") Delete delete, @Param("ignore") boolean ignore);

    /**
     * 根据id逻辑删除
     *
     * @param sqlBeanDBConfig
     * @param clazz
     * @param id
     * @return
     */
    @UpdateProvider(type = MybatisSqlBeanProvider.class, method = "logicallyDeleteById")
    long logicallyDeleteById(@Param("sqlBeanDBConfig") SqlBeanDBConfig sqlBeanDBConfig, @Param("clazz") Class<?> clazz, @Param("id") Object id);

    /**
     * 根据条件逻辑删除
     *
     * @param sqlBeanDBConfig
     * @param clazz
     * @param where
     * @param args
     * @return
     */
    @UpdateProvider(type = MybatisSqlBeanProvider.class, method = "logicallyDeleteByCondition")
    long logicallyDeleteByCondition(@Param("sqlBeanDBConfig") SqlBeanDBConfig sqlBeanDBConfig, @Param("clazz") Class<?> clazz, @Param("where") String where, @Param("args") Object... args);

    /**
     * 更新
     *
     * @param sqlBeanDBConfig
     * @param update
     * @param ignore
     * @return
     */
    @UpdateProvider(type = MybatisSqlBeanProvider.class, method = "update")
    long update(@Param("sqlBeanDBConfig") SqlBeanDBConfig sqlBeanDBConfig, @Param("update") Update update, @Param("ignore") boolean ignore);

    /**
     * 根据id条更新
     *
     * @param sqlBeanDBConfig
     * @param bean
     * @param updateNotNull
     * @param filterFields
     * @return
     */
    @UpdateProvider(type = MybatisSqlBeanProvider.class, method = "updateById")
    long updateById(@Param("sqlBeanDBConfig") SqlBeanDBConfig sqlBeanDBConfig, @Param("bean") T bean, @Param("id") Object id, @Param("updateNotNull") boolean updateNotNull, @Param("filterFields") String[] filterFields);

    /**
     * 根据实体类id条件更新
     *
     * @param sqlBeanDBConfig
     * @param bean
     * @param updateNotNull
     * @param filterFields
     * @return
     */
    @UpdateProvider(type = MybatisSqlBeanProvider.class, method = "updateByBeanId")
    long updateByBeanId(@Param("sqlBeanDBConfig") SqlBeanDBConfig sqlBeanDBConfig, @Param("bean") T bean, @Param("updateNotNull") boolean updateNotNull, @Param("filterFields") String[] filterFields);

    /**
     * 根据条件更新
     *
     * @param sqlBeanDBConfig
     * @param bean
     * @param updateNotNull
     * @param filterFields
     * @param where
     * @param args
     * @return
     */
    @UpdateProvider(type = MybatisSqlBeanProvider.class, method = "updateByCondition")
    long updateByCondition(@Param("sqlBeanDBConfig") SqlBeanDBConfig sqlBeanDBConfig, @Param("bean") T bean, @Param("updateNotNull") boolean updateNotNull, @Param("filterFields") String[] filterFields, @Param("where") String where, @Param("args") Object... args);

    /**
     * 根据实体类字段条件更新
     *
     * @param sqlBeanDBConfig
     * @param bean
     * @param updateNotNull
     * @param filterFields
     * @param where
     * @return
     */
    @UpdateProvider(type = MybatisSqlBeanProvider.class, method = "updateByBeanCondition")
    long updateByBeanCondition(@Param("sqlBeanDBConfig") SqlBeanDBConfig sqlBeanDBConfig, @Param("bean") T bean, @Param("updateNotNull") boolean updateNotNull, @Param("filterFields") String[] filterFields, @Param("where") String where);

    /**
     * 插入数据
     *
     * @param sqlBeanDBConfig
     * @param beanList
     * @return
     */
    @InsertProvider(type = MybatisSqlBeanProvider.class, method = "insertBean")
    long insertBean(@Param("sqlBeanDBConfig") SqlBeanDBConfig sqlBeanDBConfig, @Param("beanList") List<T> beanList);

    /**
     * 插入数据
     *
     * @param sqlBeanDBConfig
     * @param insert
     * @return
     */
    @InsertProvider(type = MybatisSqlBeanProvider.class, method = "insert")
    long insert(@Param("sqlBeanDBConfig") SqlBeanDBConfig sqlBeanDBConfig, @Param("insert") Insert insert);

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
     * @param sqlBeanDBConfig
     * @param clazz
     * @return
     */
    @UpdateProvider(type = MybatisSqlBeanProvider.class, method = "create")
    void create(@Param("sqlBeanDBConfig") SqlBeanDBConfig sqlBeanDBConfig, @Param("clazz") Class<?> clazz);

    /**
     * 获取表名列表
     *
     * @param sqlBeanDBConfig
     * @return
     */
    @SelectProvider(type = MybatisSqlBeanProvider.class, method = "selectTableList")
    List<String> selectTableList(@Param("sqlBeanDBConfig") SqlBeanDBConfig sqlBeanDBConfig);

    /**
     * 备份表和数据
     *
     * @param sqlBeanDBConfig
     * @param clazz
     * @param targetTableName
     * @param columns
     * @param condition
     * @return
     */
    @InsertProvider(type = MybatisSqlBeanProvider.class, method = "backup")
    long backup(@Param("sqlBeanDBConfig") SqlBeanDBConfig sqlBeanDBConfig, @Param("clazz") Class<?> clazz, @Param("targetTableName") String targetTableName, @Param("columns") Column[] columns, @Param("condition") Condition condition);

    /**
     * 复制数据到指定表
     *
     * @param sqlBeanDBConfig
     * @param clazz
     * @param targetTableName
     * @param columns
     * @param condition
     * @return
     */
    @InsertProvider(type = MybatisSqlBeanProvider.class, method = "copy")
    long copy(@Param("sqlBeanDBConfig") SqlBeanDBConfig sqlBeanDBConfig, @Param("clazz") Class<?> clazz, @Param("targetTableName") String targetTableName, @Param("columns") Column[] columns, @Param("condition") Condition condition);

}
