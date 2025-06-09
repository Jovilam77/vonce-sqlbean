package cn.vonce.sql.java.dao;

import cn.vonce.sql.bean.Select;
import cn.vonce.sql.config.SqlBeanMeta;
import cn.vonce.sql.helper.Wrapper;
import cn.vonce.sql.bean.Delete;
import cn.vonce.sql.bean.Insert;
import cn.vonce.sql.bean.Update;
import cn.vonce.sql.bean.*;
import cn.vonce.sql.java.provider.MybatisSqlBeanProvider;
import org.apache.ibatis.annotations.*;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 通用的数据库操作
 *
 * @param <T>
 * @author Jovi
 * @version 1.0
 * @email imjovi@qq.com
 * @date 2018年5月15日下午3:56:51
 */
@Mapper
public interface MybatisSqlBeanDao<T> {

    /**
     * 根据id条件查询
     *
     * @param sqlBeanMeta
     * @param clazz
     * @param id
     * @return
     */
    @SelectProvider(type = MybatisSqlBeanProvider.class, method = "selectById")
    T selectById(@Param("sqlBeanMeta") SqlBeanMeta sqlBeanMeta, @Param("clazz") Class<?> clazz, @Param("id") Object id);

    /**
     * 根据id条件查询(可指定返回类型、查询的表)
     *
     * @param sqlBeanMeta
     * @param clazz
     * @param returnType
     * @param id
     * @return
     */
    @SelectProvider(type = MybatisSqlBeanProvider.class, method = "selectById")
    <O> O selectByIdO(@Param("sqlBeanMeta") SqlBeanMeta sqlBeanDB, @Param("clazz") Class<?> clazz, @Param("returnType") Class<O> returnType, @Param("id") Object id);

    /**
     * 根据ids条件查询
     *
     * @param sqlBeanDB
     * @param clazz
     * @param ids
     * @return
     */
    @SelectProvider(type = MybatisSqlBeanProvider.class, method = "selectByIds")
    List<T> selectByIds(@Param("sqlBeanDB") SqlBeanMeta sqlBeanDB, @Param("clazz") Class<?> clazz, @Param("ids") Object... ids);

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
    <O> List<O> selectByIdsO(@Param("sqlBeanDB") SqlBeanMeta sqlBeanDB, @Param("clazz") Class<?> clazz, @Param("returnType") Class<O> returnType, @Param("ids") Object... ids);

    /**
     * 根据自定义条件查询 只返回一条记录
     *
     * @param sqlBeanDB
     * @param clazz
     * @param select
     * @return
     */
    @SelectProvider(type = MybatisSqlBeanProvider.class, method = "select")
    T selectOne(@Param("sqlBeanDB") SqlBeanMeta sqlBeanDB, @Param("clazz") Class<?> clazz, @Param("select") Select select);

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
    <O> O selectOneO(@Param("sqlBeanDB") SqlBeanMeta sqlBeanDB, @Param("clazz") Class<?> clazz, @Param("returnType") Class<O> returnType, @Param("select") Select select);

    /**
     * 根据自定义条件查询返回Map
     *
     * @param sqlBeanDB
     * @param clazz
     * @param select
     * @return
     */
    @SelectProvider(type = MybatisSqlBeanProvider.class, method = "select")
    Map<String, Object> selectMap(@Param("sqlBeanDB") SqlBeanMeta sqlBeanDB, @Param("clazz") Class<?> clazz, @Param("select") Select select);

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
    T selectOneBy(@Param("sqlBeanDB") SqlBeanMeta sqlBeanDB, @Param("clazz") Class<?> clazz, @Param("where") String where, @Param("args") Object... args);

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
    <O> O selectOneByO(@Param("sqlBeanDB") SqlBeanMeta sqlBeanDB, @Param("clazz") Class<?> clazz, @Param("returnType") Class<O> returnType, @Param("where") String where, @Param("args") Object... args);

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
    <O> List<O> selectByO(@Param("sqlBeanDB") SqlBeanMeta sqlBeanDB, @Param("clazz") Class<?> clazz, @Param("returnType") Class<O> returnType, @Param("paging") Paging paging, @Param("where") String where, @Param("args") Object... args);

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
    List<T> selectBy(@Param("sqlBeanDB") SqlBeanMeta sqlBeanDB, @Param("clazz") Class<?> clazz, @Param("paging") Paging paging, @Param("where") String where, @Param("args") Object... args);

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
    int countBy(@Param("sqlBeanDB") SqlBeanMeta sqlBeanDB, @Param("clazz") Class<?> clazz, @Param("where") String where, @Param("args") Object... args);

    /**
     * 查询全部
     *
     * @param sqlBeanDB
     * @param clazz
     * @param paging
     * @return
     */
    @SelectProvider(type = MybatisSqlBeanProvider.class, method = "selectAll")
    List<T> selectAll(@Param("sqlBeanDB") SqlBeanMeta sqlBeanDB, @Param("clazz") Class<?> clazz, @Param("paging") Paging paging);

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
    <O> List<O> selectAllO(@Param("sqlBeanDB") SqlBeanMeta sqlBeanDB, @Param("clazz") Class<?> clazz, @Param("returnType") Class<O> returnType, @Param("paging") Paging paging);


    /**
     * 根据自定义条件查询（可自动分页）返回List<Map>结果集
     *
     * @param sqlBeanDB
     * @param clazz
     * @param select
     * @return
     */
    @SelectProvider(type = MybatisSqlBeanProvider.class, method = "select")
    List<Map<String, Object>> selectMapList(@Param("sqlBeanDB") SqlBeanMeta sqlBeanDB, @Param("clazz") Class<?> clazz, @Param("select") Select select);

    /**
     * 根据自定义条件查询（可自动分页）返回List<T>
     *
     * @param sqlBeanDB
     * @param clazz
     * @param select
     * @return
     */
    @SelectProvider(type = MybatisSqlBeanProvider.class, method = "select")
    List<T> select(@Param("sqlBeanDB") SqlBeanMeta sqlBeanDB, @Param("clazz") Class<?> clazz, @Param("select") Select select);

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
    <O> List<O> selectO(@Param("sqlBeanDB") SqlBeanMeta sqlBeanDB, @Param("clazz") Class<?> clazz, @Param("returnType") Class<?> returnType, @Param("select") Select select);

    /**
     * 根据自定义条件统计
     *
     * @param sqlBeanDB
     * @param clazz
     * @param select
     * @return
     */
    @SelectProvider(type = MybatisSqlBeanProvider.class, method = "count")
    int count(@Param("sqlBeanDB") SqlBeanMeta sqlBeanDB, @Param("clazz") Class<?> clazz, @Param("returnType") Class<?> returnType, @Param("select") Select select);

    /**
     * 获取最后插入的自增id
     *
     * @return
     */
    @SelectProvider(type = MybatisSqlBeanProvider.class, method = "lastInsertId")
    long lastInsertId();

    /**
     * 根据id条件删除
     *
     * @param sqlBeanDB
     * @param clazz
     * @param id
     * @return
     */
    @DeleteProvider(type = MybatisSqlBeanProvider.class, method = "deleteById")
    int deleteById(@Param("sqlBeanDB") SqlBeanMeta sqlBeanDB, @Param("clazz") Class<?> clazz, @Param("id") Object id);

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
    int deleteBy(@Param("sqlBeanDB") SqlBeanMeta sqlBeanDB, @Param("clazz") Class<?> clazz, @Param("where") String where, @Param("args") Object... args);

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
    int delete(@Param("sqlBeanDB") SqlBeanMeta sqlBeanDB, @Param("clazz") Class<?> clazz, @Param("delete") Delete delete, @Param("ignore") boolean ignore);

    /**
     * 根据id逻辑删除
     *
     * @param sqlBeanDB
     * @param clazz
     * @param id
     * @return
     */
    @UpdateProvider(type = MybatisSqlBeanProvider.class, method = "logicallyDeleteById")
    int logicallyDeleteById(@Param("sqlBeanDB") SqlBeanMeta sqlBeanDB, @Param("clazz") Class<?> clazz, @Param("id") Object id);

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
    int logicallyDeleteBy(@Param("sqlBeanDB") SqlBeanMeta sqlBeanDB, @Param("clazz") Class<?> clazz, @Param("where") String where, @Param("args") Object... args);

    /**
     * 根据条件逻辑删除
     *
     * @param sqlBeanDB
     * @param clazz
     * @param wrapper
     * @return
     */
    @UpdateProvider(type = MybatisSqlBeanProvider.class, method = "logicallyDeleteByWrapper")
    int logicallyDeleteByWrapper(@Param("sqlBeanDB") SqlBeanMeta sqlBeanDB, @Param("clazz") Class<?> clazz, @Param("wrapper") Wrapper wrapper);

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
    int update(@Param("sqlBeanDB") SqlBeanMeta sqlBeanDB, @Param("clazz") Class<?> clazz, @Param("update") Update update, @Param("ignore") boolean ignore);

    /**
     * 根据id条更新
     *
     * @param sqlBeanDB
     * @param clazz
     * @param bean
     * @param updateNotNull
     * @param optimisticLock
     * @param filterColumns
     * @return
     */
    @UpdateProvider(type = MybatisSqlBeanProvider.class, method = "updateById")
    int updateById(@Param("sqlBeanDB") SqlBeanMeta sqlBeanDB, @Param("clazz") Class<?> clazz, @Param("bean") T bean, @Param("id") Object id, @Param("updateNotNull") boolean updateNotNull, @Param("optimisticLock") boolean optimisticLock, @Param("filterColumns") Column[] filterColumns);

    /**
     * 根据实体类id条件更新
     *
     * @param sqlBeanDB
     * @param clazz
     * @param bean
     * @param updateNotNull
     * @param optimisticLock
     * @param filterColumns
     * @return
     */
    @UpdateProvider(type = MybatisSqlBeanProvider.class, method = "updateByBeanId")
    int updateByBeanId(@Param("sqlBeanDB") SqlBeanMeta sqlBeanDB, @Param("clazz") Class<?> clazz, @Param("bean") T bean, @Param("updateNotNull") boolean updateNotNull, @Param("optimisticLock") boolean optimisticLock, @Param("filterColumns") Column[] filterColumns);

    /**
     * 根据条件更新
     *
     * @param sqlBeanDB
     * @param clazz
     * @param bean
     * @param updateNotNull
     * @param optimisticLock
     * @param filterColumns
     * @param where
     * @param args
     * @return
     */
    @UpdateProvider(type = MybatisSqlBeanProvider.class, method = "updateBy")
    int updateBy(@Param("sqlBeanDB") SqlBeanMeta sqlBeanDB, @Param("clazz") Class<?> clazz, @Param("bean") T bean, @Param("updateNotNull") boolean updateNotNull, @Param("optimisticLock") boolean optimisticLock, @Param("filterColumns") Column[] filterColumns, @Param("where") String where, @Param("args") Object... args);

    /**
     * 根据实体类字段条件更新
     *
     * @param sqlBeanDB
     * @param clazz
     * @param bean
     * @param updateNotNull
     * @param optimisticLock
     * @param filterColumns
     * @param where
     * @return
     */
    @UpdateProvider(type = MybatisSqlBeanProvider.class, method = "updateByBean")
    int updateByBean(@Param("sqlBeanDB") SqlBeanMeta sqlBeanDB, @Param("clazz") Class<?> clazz, @Param("bean") T bean, @Param("updateNotNull") boolean updateNotNull, @Param("optimisticLock") boolean optimisticLock, @Param("where") String where, @Param("filterColumns") Column[] filterColumns);

    /**
     * 插入数据
     *
     * @param sqlBeanDB
     * @param clazz
     * @param beanList
     * @return
     */
    @InsertProvider(type = MybatisSqlBeanProvider.class, method = "insertBean")
    int insertBean(@Param("sqlBeanDB") SqlBeanMeta sqlBeanDB, @Param("clazz") Class<?> clazz, @Param("beanList") Collection<T> beanList);

    /**
     * 插入数据
     *
     * @param sqlBeanDB
     * @param clazz
     * @param insert
     * @return
     */
    @InsertProvider(type = MybatisSqlBeanProvider.class, method = "insert")
    int insert(@Param("sqlBeanDB") SqlBeanMeta sqlBeanDB, @Param("clazz") Class<?> clazz, @Param("insert") Insert insert);

    /**
     * 删除表
     *
     * @param clazz
     * @return
     */
    @UpdateProvider(type = MybatisSqlBeanProvider.class, method = "drop")
    void drop(@Param("sqlBeanDB") SqlBeanMeta sqlBeanDB, @Param("clazz") Class<?> clazz);

    /**
     * 创建表
     *
     * @param sqlBeanDB
     * @param clazz
     * @return
     */
    @UpdateProvider(type = MybatisSqlBeanProvider.class, method = "create")
    void create(@Param("sqlBeanDB") SqlBeanMeta sqlBeanDB, @Param("clazz") Class<?> clazz);

    /**
     * 获取表名列表
     *
     * @param sqlBeanDB
     * @param schema
     * @param name
     * @return
     */
    @SelectProvider(type = MybatisSqlBeanProvider.class, method = "selectTableList")
    List<TableInfo> selectTableList(@Param("sqlBeanDB") SqlBeanMeta sqlBeanDB, @Param("schema") String schema, @Param("name") String name);

    /**
     * 获取表名列表
     *
     * @param sqlBeanDB
     * @param schema
     * @param name
     * @return
     */
    @SelectProvider(type = MybatisSqlBeanProvider.class, method = "selectColumnInfoList")
    List<ColumnInfo> selectColumnInfoList(@Param("sqlBeanDB") SqlBeanMeta sqlBeanDB, @Param("schema") String schema, @Param("name") String name);

    /**
     * 备份表和数据
     *
     * @param sqlBeanDB
     * @param clazz
     * @param wrapper
     * @param targetTableName
     * @param columns
     * @return
     */
    @InsertProvider(type = MybatisSqlBeanProvider.class, method = "backup")
    int backup(@Param("sqlBeanDB") SqlBeanMeta sqlBeanDB, @Param("clazz") Class<?> clazz, @Param("wrapper") Wrapper wrapper, @Param("targetSchema") String targetSchema, @Param("targetTableName") String targetTableName, @Param("columns") Column[] columns);

    /**
     * 复制数据到指定表
     *
     * @param sqlBeanDB
     * @param clazz
     * @param wrapper
     * @param targetTableName
     * @param columns
     * @return
     */
    @InsertProvider(type = MybatisSqlBeanProvider.class, method = "copy")
    int copy(@Param("sqlBeanDB") SqlBeanMeta sqlBeanDB, @Param("clazz") Class<?> clazz, @Param("wrapper") Wrapper wrapper, @Param("targetSchema") String targetSchema, @Param("targetTableName") String targetTableName, @Param("columns") Column[] columns);

    /**
     * 执行Sql
     *
     * @param sql
     * @return
     */
    @UpdateProvider(type = MybatisSqlBeanProvider.class, method = "executeSql")
    int executeSql(String sql);

    /**
     * 模式列表
     *
     * @param sqlBeanDB
     * @param name
     * @return
     */
    @SelectProvider(type = MybatisSqlBeanProvider.class, method = "databases")
    List<String> databases(@Param("sqlBeanDB") SqlBeanMeta sqlBeanDB, @Param("name") String name);

    /**
     * 创建模式
     *
     * @param sqlBeanDB
     * @param name
     * @return
     */
    @UpdateProvider(type = MybatisSqlBeanProvider.class, method = "createSchema")
    int createSchema(@Param("sqlBeanDB") SqlBeanMeta sqlBeanDB, @Param("name") String name);

    /**
     * 删除模式
     *
     * @param sqlBeanDB
     * @param name
     * @return
     */
    @UpdateProvider(type = MybatisSqlBeanProvider.class, method = "dropSchema")
    int dropSchema(@Param("sqlBeanDB") SqlBeanMeta sqlBeanDB, @Param("name") String name);

}
