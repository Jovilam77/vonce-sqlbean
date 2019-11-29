package cn.vonce.sql.orm.dao;

import cn.vonce.sql.bean.Delete;
import cn.vonce.sql.bean.Insert;
import cn.vonce.sql.bean.Paging;
import cn.vonce.sql.bean.Select;
import cn.vonce.sql.bean.Update;
import cn.vonce.sql.orm.provider.MybatisSqlBeanProvider;
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
     * @param clazz
     * @param id
     * @return
     * @Param selectConfig
     * @author Jovi
     * @date 2018年5月15日下午3:58:28
     */
    @SelectProvider(type = MybatisSqlBeanProvider.class, method = "selectById")
    T selectById(@Param("clazz") Class<?> clazz, @Param("id") Object id);

    /**
     * 根据id条件查询(可指定返回类型、查询的表)
     *
     * @param clazz
     * @param returnType
     * @param id
     * @return
     * @Param selectConfig
     * @author Jovi
     * @date 2018年6月15日下午6:36:38
     */
    @SelectProvider(type = MybatisSqlBeanProvider.class, method = "selectById")
    <O> O selectByIdO(@Param("clazz") Class<?> clazz, @Param("returnType") Class<O> returnType, @Param("id") Object id);

    /**
     * 根据ids条件查询
     *
     * @param clazz
     * @param ids
     * @return
     * @Param selectConfig
     * @author Jovi
     * @date 2018年5月15日下午3:58:28
     */
    @SelectProvider(type = MybatisSqlBeanProvider.class, method = "selectByIds")
    List<T> selectByIds(@Param("clazz") Class<?> clazz, @Param("ids") Object... ids);

    /**
     * 根据id条件查询(可指定返回类型、查询的表)
     *
     * @param clazz
     * @param returnType
     * @param ids
     * @return
     * @Param selectConfig
     * @author Jovi
     * @date 2018年6月15日下午6:36:38
     */
    @SelectProvider(type = MybatisSqlBeanProvider.class, method = "selectByIds")
    <O> List<O> selectByIdsO(@Param("clazz") Class<?> clazz, @Param("returnType") Class<O> returnType, @Param("ids") Object... ids);

    /**
     * 根据自定义条件查询 只返回一条记录
     *
     * @param clazz
     * @param select
     * @return
     * @Param selectConfig
     * @author Jovi
     * @date 2018年5月15日下午3:58:46
     */
    @SelectProvider(type = MybatisSqlBeanProvider.class, method = "select")
    T selectOne(@Param("clazz") Class<?> clazz, @Param("select") Select select);

    /**
     * 根据自定义条件查询 只返回一条记录(可指定返回类型)
     *
     * @param clazz
     * @return
     * @Param selectConfig
     * @Param select
     * @author Jovi
     * @date 2018年5月15日下午3:58:46
     */
    @SelectProvider(type = MybatisSqlBeanProvider.class, method = "select")
    <O> O selectOneO(@Param("clazz") Class<?> clazz, @Param("returnType") Class<O> returnType, @Param("select") Select select);

    /**
     * 根据自定义条件查询返回Map
     *
     * @param clazz
     * @return
     * @Param selectConfig
     * @Param select
     * @author Jovi
     * @date 2019年5月17日下午5:11:22
     */
    @SelectProvider(type = MybatisSqlBeanProvider.class, method = "select")
    Map<String, Object> selectMap(@Param("clazz") Class<?> clazz, @Param("select") Select select);

    /**
     * 根据条件查询
     *
     * @param clazz
     * @param where
     * @param args
     * @return
     * @Param selectConfig
     * @author Jovi
     * @date 2018年5月15日下午4:03:56
     */
    @SelectProvider(type = MybatisSqlBeanProvider.class, method = "selectByCondition")
    T selectOneByCondition(@Param("clazz") Class<?> clazz, @Param("where") String where, @Param("args") Object... args);

    /**
     * 根据条件查询(可指定返回类型、查询的表)
     *
     * @param clazz
     * @param where
     * @param args
     * @return
     * @Param selectConfig
     * @author Jovi
     * @date 2018年5月15日下午4:03:56
     */
    @SelectProvider(type = MybatisSqlBeanProvider.class, method = "selectByCondition")
    <O> O selectOneByConditionO(@Param("clazz") Class<?> clazz, @Param("returnType") Class<O> returnType, @Param("where") String where, @Param("args") Object... args);

    /**
     * 根据条件查询(可指定返回类型、查询的表)
     *
     * @param clazz
     * @param paging
     * @param where
     * @param args
     * @return
     * @Param selectConfig
     * @author Jovi
     * @date 2018年5月15日下午4:03:56
     */
    @SelectProvider(type = MybatisSqlBeanProvider.class, method = "selectByCondition")
    <O> List<O> selectByConditionO(@Param("clazz") Class<?> clazz, @Param("returnType") Class<O> returnType, @Param("paging") Paging paging, @Param("where") String where, @Param("args") Object... args);

    /**
     * 根据条件查询
     *
     * @param clazz
     * @param paging
     * @param where
     * @param args
     * @return
     * @Param selectConfig
     * @author Jovi
     * @date 2018年5月15日下午3:58:50
     */
    @SelectProvider(type = MybatisSqlBeanProvider.class, method = "selectByCondition")
    List<T> selectByCondition(@Param("clazz") Class<?> clazz, @Param("paging") Paging paging, @Param("where") String where, @Param("args") Object... args);

    /**
     * 根据条件查询统计
     *
     * @param clazz
     * @param where
     * @param args
     * @return
     * @Param selectConfig
     * @author Jovi
     * @date 2018年7月5日下午4:09:45
     */
    @SelectProvider(type = MybatisSqlBeanProvider.class, method = "selectCountByCondition")
    long selectCountByCondition(@Param("clazz") Class<?> clazz, @Param("where") String where, @Param("args") Object... args);

    /**
     * 查询全部
     *
     * @param clazz
     * @param paging
     * @return
     * @Param selectConfig
     * @ @param paging
     * @author Jovi
     * @date 2018年5月15日下午3:59:27
     */
    @SelectProvider(type = MybatisSqlBeanProvider.class, method = "selectAll")
    List<T> selectAll(@Param("clazz") Class<?> clazz, @Param("paging") Paging paging);

    /**
     * 查询全部(可指定返回类型、查询的表)
     *
     * @param clazz
     * @param returnType
     * @param paging
     * @return
     * @Param selectConfig
     * @author Jovi
     * @date 2018年5月15日下午3:59:27
     */
    @SelectProvider(type = MybatisSqlBeanProvider.class, method = "selectAll")
    <O> List<O> selectAllO(@Param("clazz") Class<?> clazz, @Param("returnType") Class<O> returnType, @Param("paging") Paging paging);


    /**
     * 根据自定义条件查询（可自动分页）返回List<Map>结果集
     *
     * @param clazz
     * @return
     * @Param selectConfig
     * @Param select
     * @author Jovi
     * @date 2018年6月8日下午2:34:06
     */
    @SelectProvider(type = MybatisSqlBeanProvider.class, method = "select")
    List<Map<String, Object>> selectMapList(@Param("clazz") Class<?> clazz, @Param("select") Select select);

    /**
     * 根据自定义条件查询（可自动分页）返回List<T>
     *
     * @param clazz
     * @return
     * @Param selectConfig
     * @Param select
     * @author Jovi
     * @date 2018年5月15日下午3:59:43
     */
    @SelectProvider(type = MybatisSqlBeanProvider.class, method = "select")
    List<T> select(@Param("clazz") Class<?> clazz, @Param("select") Select select);

    /**
     * 根据自定义条件查询（可自动分页）返回List<O> (可指定返回类型)
     *
     * @param clazz
     * @return
     * @Param selectConfig
     * @Param select
     * @author Jovi
     * @date 2018年6月13日下午2:39:08
     */
    @SelectProvider(type = MybatisSqlBeanProvider.class, method = "select")
    <O> List<O> selectO(@Param("clazz") Class<?> clazz, @Param("returnType") Class<O> returnType, @Param("select") Select select);

    /**
     * 根据自定义条件统计
     *
     * @param clazz
     * @return
     * @Param selectConfig
     * @Param select
     * @author Jovi
     * @date 2018年5月15日下午3:59:47
     */
    @SelectProvider(type = MybatisSqlBeanProvider.class, method = "count")
    long count(@Param("clazz") Class<?> clazz, @Param("select") Select select);

    /**
     * 根据id条件删除
     *
     * @param clazz
     * @param id
     * @return
     * @Param selectConfig
     * @author Jovi
     * @date 2018年5月15日下午4:00:43
     */
    @DeleteProvider(type = MybatisSqlBeanProvider.class, method = "deleteById")
    long deleteById(@Param("clazz") Class<?> clazz, @Param("id") Object id);

    /**
     * 根据条件删除
     *
     * @param clazz
     * @param where
     * @param args
     * @return
     * @Param selectConfig
     * @author Jovi
     * @date 2018年5月15日下午4:00:57
     */
    @DeleteProvider(type = MybatisSqlBeanProvider.class, method = "deleteByCondition")
    long deleteByCondition(@Param("clazz") Class<?> clazz, @Param("where") String where, @Param("args") Object... args);

    /**
     * 删除
     *
     * @param clazz
     * @param delete
     * @param ignore
     * @return
     * @Param selectConfig
     */
    @DeleteProvider(type = MybatisSqlBeanProvider.class, method = "delete")
    long delete(@Param("clazz") Class<?> clazz, @Param("delete") Delete delete, @Param("ignore") boolean ignore);

    /**
     * 根据id逻辑删除
     *
     * @param clazz
     * @param id
     * @return
     * @Param selectConfig
     */
    @UpdateProvider(type = MybatisSqlBeanProvider.class, method = "logicallyDeleteById")
    long logicallyDeleteById(@Param("clazz") Class<?> clazz, @Param("id") Object id);

    /**
     * 根据条件逻辑删除
     *
     * @param clazz
     * @param where
     * @param args
     * @return
     * @Param selectConfig
     */
    @UpdateProvider(type = MybatisSqlBeanProvider.class, method = "logicallyDeleteByCondition")
    long logicallyDeleteByCondition(@Param("clazz") Class<?> clazz, @Param("where") String where, @Param("args") Object... args);

    /**
     * 更新
     *
     * @param update
     * @param ignore
     * @return
     * @Param selectConfig
     */
    @UpdateProvider(type = MybatisSqlBeanProvider.class, method = "update")
    long update(@Param("update") Update update, @Param("ignore") boolean ignore);

    /**
     * 根据id条更新
     *
     * @param bean
     * @param updateNotNull
     * @return
     * @Param selectConfig
     * @author Jovi
     * @date 2018年5月15日下午4:01:00
     */
    @UpdateProvider(type = MybatisSqlBeanProvider.class, method = "updateById")
    long updateById(@Param("bean") T bean, @Param("id") Object id, @Param("updateNotNull") boolean updateNotNull, @Param("filterFields") String[] filterFields);

    /**
     * 根据实体类id条件更新
     *
     * @param bean
     * @param updateNotNull
     * @return
     * @Param selectConfig
     */
    @UpdateProvider(type = MybatisSqlBeanProvider.class, method = "updateByBeanId")
    long updateByBeanId(@Param("bean") T bean, @Param("updateNotNull") boolean updateNotNull, @Param("filterFields") String[] filterFields);

    /**
     * 根据条件更新
     *
     * @param bean
     * @param where
     * @param updateNotNull
     * @param args
     * @return
     * @Param selectConfig
     * @author Jovi
     * @date 2018年5月15日下午4:01:03
     */
    @UpdateProvider(type = MybatisSqlBeanProvider.class, method = "updateByCondition")
    long updateByCondition(@Param("bean") T bean, @Param("updateNotNull") boolean updateNotNull, @Param("filterFields") String[] filterFields, @Param("where") String where, @Param("args") Object... args);

    /**
     * 根据实体类字段条件更新
     *
     * @param bean
     * @param where
     * @param updateNotNull
     * @return
     * @Param selectConfig
     * @author Jovi
     * @date 2018年5月15日下午4:01:06
     */
    @UpdateProvider(type = MybatisSqlBeanProvider.class, method = "updateByBeanCondition")
    long updateByBeanCondition(@Param("bean") T bean, @Param("updateNotNull") boolean updateNotNull, @Param("filterFields") String[] filterFields, @Param("where") String where);

    /**
     * 插入数据
     *
     * @param beanList
     * @return
     * @Param selectConfig
     * @author Jovi
     * @date 2018年5月15日下午4:01:09
     */
    @InsertProvider(type = MybatisSqlBeanProvider.class, method = "insertBean")
    long insertBean(@Param("beanList") List<T> beanList);

    /**
     * 插入数据
     *
     * @param insert
     * @return
     * @Param selectConfig
     * @author Jovi
     * @date 2018年5月15日下午4:01:09
     */
    @InsertProvider(type = MybatisSqlBeanProvider.class, method = "insert")
    long insert(@Param("insert") Insert insert);

}
