package cn.vonce.sql.spring.dao;

import cn.vonce.sql.config.SqlBeanDB;
import cn.vonce.sql.helper.Wrapper;
import cn.vonce.sql.spring.provider.MybatisSqlBeanProvider;
import cn.vonce.sql.bean.Delete;
import cn.vonce.sql.bean.Insert;
import cn.vonce.sql.bean.Select;
import cn.vonce.sql.bean.Update;
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
    @SelectProvider(type = MybatisSqlBeanProvider.class, method = "selectBy")
    T selectOneBy(@Param("sqlBeanDB") SqlBeanDB sqlBeanDB, @Param("clazz") Class<?> clazz, @Param("where") String where, @Param("args") Object... args);

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
    @SelectProvider(type = MybatisSqlBeanProvider.class, method = "selectBy")
    <O> O selectOneByO(@Param("sqlBeanDB") SqlBeanDB sqlBeanDB, @Param("clazz") Class<?> clazz, @Param("returnType") Class<O> returnType, @Param("where") String where, @Param("args") Object... args);

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
    @SelectProvider(type = MybatisSqlBeanProvider.class, method = "selectBy")
    <O> List<O> selectByO(@Param("sqlBeanDB") SqlBeanDB sqlBeanDB, @Param("clazz") Class<?> clazz, @Param("returnType") Class<O> returnType, @Param("paging") Paging paging, @Param("where") String where, @Param("args") Object... args);

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
    @SelectProvider(type = MybatisSqlBeanProvider.class, method = "selectBy")
    List<T> selectBy(@Param("sqlBeanDB") SqlBeanDB sqlBeanDB, @Param("clazz") Class<?> clazz, @Param("paging") Paging paging, @Param("where") String where, @Param("args") Object... args);

    /**
     * 根据条件查询统计
     *
     * @param sqlBeanDB
     * @param clazz
     * @param where
     * @param args
     * @return
     */
    @SelectProvider(type = MybatisSqlBeanProvider.class, method = "countBy")
    int countBy(@Param("sqlBeanDB") SqlBeanDB sqlBeanDB, @Param("clazz") Class<?> clazz, @Param("where") String where, @Param("args") Object... args);

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
    <O> List<O> selectO(@Param("sqlBeanDB") SqlBeanDB sqlBeanDB, @Param("clazz") Class<?> clazz, @Param("returnType") Class<?> returnType, @Param("select") Select select);

    /**
     * 根据自定义条件统计
     *
     * @param sqlBeanDB
     * @param clazz
     * @param select
     * @return
     */
    @SelectProvider(type = MybatisSqlBeanProvider.class, method = "count")
    int count(@Param("sqlBeanDB") SqlBeanDB sqlBeanDB, @Param("clazz") Class<?> clazz, @Param("returnType") Class<?> returnType, @Param("select") Select select);

    /**
     * 根据id条件删除
     *
     * @param sqlBeanDB
     * @param clazz
     * @param id
     * @return
     */
    @DeleteProvider(type = MybatisSqlBeanProvider.class, method = "deleteById")
    int deleteById(@Param("sqlBeanDB") SqlBeanDB sqlBeanDB, @Param("clazz") Class<?> clazz, @Param("id") Object id);

    /**
     * 根据条件删除
     *
     * @param sqlBeanDB
     * @param clazz
     * @param where
     * @param args
     * @return
     */
    @DeleteProvider(type = MybatisSqlBeanProvider.class, method = "deleteBy")
    int deleteBy(@Param("sqlBeanDB") SqlBeanDB sqlBeanDB, @Param("clazz") Class<?> clazz, @Param("where") String where, @Param("args") Object... args);

    /**
     * 删除
     *
     * @param sqlBeanDB
     * @param clazz
     * @param delete
     * @param ignore
     * @return
     */
    @DeleteProvider(type = MybatisSqlBeanProvider.class, method = "delete")
    int delete(@Param("sqlBeanDB") SqlBeanDB sqlBeanDB, @Param("clazz") Class<?> clazz, @Param("delete") Delete delete, @Param("ignore") boolean ignore);

    /**
     * 根据id逻辑删除
     *
     * @param sqlBeanDB
     * @param clazz
     * @param id
     * @return
     */
    @UpdateProvider(type = MybatisSqlBeanProvider.class, method = "logicallyDeleteById")
    int logicallyDeleteById(@Param("sqlBeanDB") SqlBeanDB sqlBeanDB, @Param("clazz") Class<?> clazz, @Param("id") Object id);

    /**
     * 根据条件逻辑删除
     *
     * @param sqlBeanDB
     * @param clazz
     * @param where
     * @param args
     * @return
     */
    @UpdateProvider(type = MybatisSqlBeanProvider.class, method = "logicallyDeleteBy")
    int logicallyDeleteBy(@Param("sqlBeanDB") SqlBeanDB sqlBeanDB, @Param("clazz") Class<?> clazz, @Param("where") String where, @Param("args") Object... args);

    /**
     * 根据条件逻辑删除
     *
     * @param sqlBeanDB
     * @param clazz
     * @param wrapper
     * @return
     */
    @UpdateProvider(type = MybatisSqlBeanProvider.class, method = "logicallyDeleteByWrapper")
    int logicallyDeleteByWrapper(@Param("sqlBeanDB") SqlBeanDB sqlBeanDB, @Param("clazz") Class<?> clazz, @Param("wrapper") Wrapper wrapper);

    /**
     * 更新
     *
     * @param sqlBeanDB
     * @param clazz
     * @param update
     * @param ignore
     * @return
     */
    @UpdateProvider(type = MybatisSqlBeanProvider.class, method = "update")
    int update(@Param("sqlBeanDB") SqlBeanDB sqlBeanDB, @Param("clazz") Class<?> clazz, @Param("update") Update update, @Param("ignore") boolean ignore);

    /**
     * 根据id条更新
     *
     * @param sqlBeanDB
     * @param clazz
     * @param bean
     * @param updateNotNull
     * @param optimisticLock
     * @param filterFields
     * @return
     */
    @UpdateProvider(type = MybatisSqlBeanProvider.class, method = "updateById")
    int updateById(@Param("sqlBeanDB") SqlBeanDB sqlBeanDB, @Param("clazz") Class<?> clazz, @Param("bean") T bean, @Param("id") Object id, @Param("updateNotNull") boolean updateNotNull, @Param("optimisticLock") boolean optimisticLock, @Param("filterFields") String[] filterFields);

    /**
     * 根据实体类id条件更新
     *
     * @param sqlBeanDB
     * @param clazz
     * @param bean
     * @param updateNotNull
     * @param optimisticLock
     * @param filterFields
     * @return
     */
    @UpdateProvider(type = MybatisSqlBeanProvider.class, method = "updateByBeanId")
    int updateByBeanId(@Param("sqlBeanDB") SqlBeanDB sqlBeanDB, @Param("clazz") Class<?> clazz, @Param("bean") T bean, @Param("updateNotNull") boolean updateNotNull, @Param("optimisticLock") boolean optimisticLock, @Param("filterFields") String[] filterFields);

    /**
     * 根据条件更新
     *
     * @param sqlBeanDB
     * @param clazz
     * @param bean
     * @param updateNotNull
     * @param optimisticLock
     * @param filterFields
     * @param where
     * @param args
     * @return
     */
    @UpdateProvider(type = MybatisSqlBeanProvider.class, method = "updateBy")
    int updateBy(@Param("sqlBeanDB") SqlBeanDB sqlBeanDB, @Param("clazz") Class<?> clazz, @Param("bean") T bean, @Param("updateNotNull") boolean updateNotNull, @Param("optimisticLock") boolean optimisticLock, @Param("filterFields") String[] filterFields, @Param("where") String where, @Param("args") Object... args);

    /**
     * 根据实体类字段条件更新
     *
     * @param sqlBeanDB
     * @param clazz
     * @param bean
     * @param updateNotNull
     * @param optimisticLock
     * @param filterFields
     * @param where
     * @return
     */
    @UpdateProvider(type = MybatisSqlBeanProvider.class, method = "updateByBean")
    int updateByBean(@Param("sqlBeanDB") SqlBeanDB sqlBeanDB, @Param("clazz") Class<?> clazz, @Param("bean") T bean, @Param("updateNotNull") boolean updateNotNull, @Param("optimisticLock") boolean optimisticLock, @Param("filterFields") String[] filterFields, @Param("where") String where);

    /**
     * 插入数据
     *
     * @param sqlBeanDB
     * @param clazz
     * @param beanList
     * @return
     */
    @InsertProvider(type = MybatisSqlBeanProvider.class, method = "insertBean")
    int insertBean(@Param("sqlBeanDB") SqlBeanDB sqlBeanDB, @Param("clazz") Class<?> clazz, @Param("beanList") List<T> beanList);

    /**
     * 插入数据
     *
     * @param sqlBeanDB
     * @param clazz
     * @param insert
     * @return
     */
    @InsertProvider(type = MybatisSqlBeanProvider.class, method = "insert")
    int insert(@Param("sqlBeanDB") SqlBeanDB sqlBeanDB, @Param("clazz") Class<?> clazz, @Param("insert") Insert insert);

    /**
     * 删除表
     *
     * @param clazz
     * @return
     */
    @UpdateProvider(type = MybatisSqlBeanProvider.class, method = "drop")
    void drop(@Param("sqlBeanDB") SqlBeanDB sqlBeanDB, @Param("clazz") Class<?> clazz);

    /**
     * 创建表
     *
     * @param sqlBeanDB
     * @param clazz
     * @return
     */
    @UpdateProvider(type = MybatisSqlBeanProvider.class, method = "create")
    void create(@Param("sqlBeanDB") SqlBeanDB sqlBeanDB, @Param("clazz") Class<?> clazz);

    /**
     * 获取表名列表
     *
     * @param sqlBeanDB
     * @return
     */
    @SelectProvider(type = MybatisSqlBeanProvider.class, method = "selectTableList")
    List<TableInfo> selectTableList(@Param("sqlBeanDB") SqlBeanDB sqlBeanDB, @Param("name") String name);

    /**
     * 获取表名列表
     *
     * @param sqlBeanDB
     * @return
     */
    @SelectProvider(type = MybatisSqlBeanProvider.class, method = "selectColumnInfoList")
    List<ColumnInfo> selectColumnInfoList(@Param("sqlBeanDB") SqlBeanDB sqlBeanDB, @Param("name") String name);

    /**
     * 备份表和数据
     *
     * @param sqlBeanDB
     * @param clazz
     * @param targetTableName
     * @param columns
     * @param wrapper
     * @return
     */
    @InsertProvider(type = MybatisSqlBeanProvider.class, method = "backup")
    int backup(@Param("sqlBeanDB") SqlBeanDB sqlBeanDB, @Param("clazz") Class<?> clazz, @Param("targetSchema") String targetSchema, @Param("targetTableName") String targetTableName, @Param("columns") Column[] columns, @Param("wrapper") Wrapper wrapper);

    /**
     * 复制数据到指定表
     *
     * @param sqlBeanDB
     * @param clazz
     * @param targetTableName
     * @param columns
     * @param wrapper
     * @return
     */
    @InsertProvider(type = MybatisSqlBeanProvider.class, method = "copy")
    int copy(@Param("sqlBeanDB") SqlBeanDB sqlBeanDB, @Param("clazz") Class<?> clazz, @Param("targetSchema") String targetSchema, @Param("targetTableName") String targetTableName, @Param("columns") Column[] columns, @Param("wrapper") Wrapper wrapper);

    /**
     * 执行Sql
     *
     * @param sql
     * @return
     */
    @UpdateProvider(type = MybatisSqlBeanProvider.class, method = "executeSql")
    int executeSql(String sql);


}
