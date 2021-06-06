package cn.vonce.sql.spring.dao;

import cn.vonce.sql.config.SqlBeanDB;
import cn.vonce.sql.spring.provider.MybatisSqlBeanProvider;
import cn.vonce.sql.bean.Select;
import cn.vonce.sql.bean.*;
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
     * @param sqlBeanDB
     * @param clazz
     * @param id
     * @return
     */
    @SelectProvider(type = MybatisSqlBeanProvider.class, method = "selectById")
    T selectById(@Param("sqlBeanDB") SqlBeanDB sqlBeanDB, @Param("clazz") Class<?> clazz, @Param("id") Object id);

    /**
     * 根据id条件查询(可指定返回类型、查询的表)
     *
     * @param sqlBeanDB
     * @param clazz
     * @param returnType
     * @param id
     * @return
     */
    @SelectProvider(type = MybatisSqlBeanProvider.class, method = "selectById")
    <O> O selectByIdO(@Param("sqlBeanDB") SqlBeanDB sqlBeanDB, @Param("clazz") Class<?> clazz, @Param("returnType") Class<O> returnType, @Param("id") Object id);

    /**
     * 根据ids条件查询
     *
     * @param sqlBeanDB
     * @param clazz
     * @param ids
     * @return
     */
    @SelectProvider(type = MybatisSqlBeanProvider.class, method = "selectByIds")
    List<T> selectByIds(@Param("sqlBeanDB") SqlBeanDB sqlBeanDB, @Param("clazz") Class<?> clazz, @Param("ids") Object... ids);

    /**
     * 根据id条件查询(可指定返回类型、查询的表)
     *
     * @param sqlBeanDB
     * @param clazz
     * @param returnType
     * @param ids
     * @return
     */
    @SelectProvider(type = MybatisSqlBeanProvider.class, method = "selectByIds")
    <O> List<O> selectByIdsO(@Param("sqlBeanDB") SqlBeanDB sqlBeanDB, @Param("clazz") Class<?> clazz, @Param("returnType") Class<O> returnType, @Param("ids") Object... ids);

    /**
     * 根据自定义条件查询 只返回一条记录
     *
     * @param sqlBeanDB
     * @param clazz
     * @param select
     * @return
     */
    @SelectProvider(type = MybatisSqlBeanProvider.class, method = "select")
    T selectOne(@Param("sqlBeanDB") SqlBeanDB sqlBeanDB, @Param("clazz") Class<?> clazz, @Param("select") Select select);

    /**
     * 根据自定义条件查询 只返回一条记录(可指定返回类型)
     *
     * @param sqlBeanDB
     * @param clazz
     * @param returnType
     * @param select
     * @return
     */
    @SelectProvider(type = MybatisSqlBeanProvider.class, method = "select")
    <O> O selectOneO(@Param("sqlBeanDB") SqlBeanDB sqlBeanDB, @Param("clazz") Class<?> clazz, @Param("returnType") Class<O> returnType, @Param("select") Select select);

    /**
     * 根据自定义条件查询返回Map
     *
     * @param sqlBeanDB
     * @param clazz
     * @param select
     * @return
     */
    @SelectProvider(type = MybatisSqlBeanProvider.class, method = "select")
    Map<String, Object> selectMap(@Param("sqlBeanDB") SqlBeanDB sqlBeanDB, @Param("clazz") Class<?> clazz, @Param("select") Select select);

    /**
     * 根据条件查询
     *
     * @param sqlBeanDB
     * @param clazz
     * @param where
     * @param args
     * @return
     */
    @SelectProvider(type = MybatisSqlBeanProvider.class, method = "selectByCondition")
    T selectOneByCondition(@Param("sqlBeanDB") SqlBeanDB sqlBeanDB, @Param("clazz") Class<?> clazz, @Param("where") String where, @Param("args") Object... args);

    /**
     * 根据条件查询(可指定返回类型、查询的表)
     *
     * @param sqlBeanDB
     * @param clazz
     * @param returnType
     * @param where
     * @param args
     * @return
     */
    @SelectProvider(type = MybatisSqlBeanProvider.class, method = "selectByCondition")
    <O> O selectOneByConditionO(@Param("sqlBeanDB") SqlBeanDB sqlBeanDB, @Param("clazz") Class<?> clazz, @Param("returnType") Class<O> returnType, @Param("where") String where, @Param("args") Object... args);

    /**
     * 根据条件查询(可指定返回类型、查询的表)
     *
     * @param sqlBeanDB
     * @param clazz
     * @param paging
     * @param where
     * @param args
     * @return
     */
    @SelectProvider(type = MybatisSqlBeanProvider.class, method = "selectByCondition")
    <O> List<O> selectByConditionO(@Param("sqlBeanDB") SqlBeanDB sqlBeanDB, @Param("clazz") Class<?> clazz, @Param("returnType") Class<O> returnType, @Param("paging") Paging paging, @Param("where") String where, @Param("args") Object... args);

    /**
     * 根据条件查询
     *
     * @param sqlBeanDB
     * @param clazz
     * @param paging
     * @param where
     * @param args
     * @return
     */
    @SelectProvider(type = MybatisSqlBeanProvider.class, method = "selectByCondition")
    List<T> selectByCondition(@Param("sqlBeanDB") SqlBeanDB sqlBeanDB, @Param("clazz") Class<?> clazz, @Param("paging") Paging paging, @Param("where") String where, @Param("args") Object... args);

    /**
     * 查询全部
     *
     * @param sqlBeanDB
     * @param clazz
     * @param paging
     * @return
     */
    @SelectProvider(type = MybatisSqlBeanProvider.class, method = "selectAll")
    List<T> selectAll(@Param("sqlBeanDB") SqlBeanDB sqlBeanDB, @Param("clazz") Class<?> clazz, @Param("paging") Paging paging);

    /**
     * 查询全部(可指定返回类型、查询的表)
     *
     * @param sqlBeanDB
     * @param clazz
     * @param returnType
     * @param paging
     * @return
     */
    @SelectProvider(type = MybatisSqlBeanProvider.class, method = "selectAll")
    <O> List<O> selectAllO(@Param("sqlBeanDB") SqlBeanDB sqlBeanDB, @Param("clazz") Class<?> clazz, @Param("returnType") Class<O> returnType, @Param("paging") Paging paging);


    /**
     * 根据自定义条件查询（可自动分页）返回List<Map>结果集
     *
     * @param sqlBeanDB
     * @param clazz
     * @param select
     * @return
     */
    @SelectProvider(type = MybatisSqlBeanProvider.class, method = "select")
    List<Map<String, Object>> selectMapList(@Param("sqlBeanDB") SqlBeanDB sqlBeanDB, @Param("clazz") Class<?> clazz, @Param("select") Select select);

    /**
     * 根据自定义条件查询（可自动分页）返回List<T>
     *
     * @param sqlBeanDB
     * @param clazz
     * @param select
     * @return
     */
    @SelectProvider(type = MybatisSqlBeanProvider.class, method = "select")
    List<T> select(@Param("sqlBeanDB") SqlBeanDB sqlBeanDB, @Param("clazz") Class<?> clazz, @Param("select") Select select);

    /**
     * 根据自定义条件查询（可自动分页）返回List<O> (可指定返回类型)
     *
     * @param sqlBeanDB
     * @param clazz
     * @param returnType
     * @param select
     * @return
     */
    @SelectProvider(type = MybatisSqlBeanProvider.class, method = "select")
    <O> List<O> selectO(@Param("sqlBeanDB") SqlBeanDB sqlBeanDB, @Param("clazz") Class<?> clazz, @Param("returnType") Class<O> returnType, @Param("select") Select select);

}
