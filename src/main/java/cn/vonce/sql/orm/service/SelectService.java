package cn.vonce.sql.orm.service;

import cn.vonce.sql.bean.Paging;
import cn.vonce.sql.bean.Select;
import cn.vonce.sql.page.PagingService;

import java.util.List;
import java.util.Map;

/**
 * Select 通用业务接口
 *
 * @param <T>
 * @author Jovi
 * @version 1.0
 * @email 766255988@qq.com
 * @date 2019年6月27日下午3:57:33
 */
public interface SelectService<T, ID> extends PagingService {

    /**
     * 根据id条件查询
     *
     * @param id
     * @return
     */
    T selectById(ID id);

    /**
     * 根据id条件查询(可指定返回类型、查询的表)
     *
     * @param returnType
     * @param id
     * @return
     */
    <O> O selectById(Class<O> returnType, ID id);

    /**
     * 根据ids条件查询
     *
     * @param ids
     * @return
     */
    List<T> selectByIds(ID... ids);

    /**
     * 根据id条件查询(可指定返回类型、查询的表)
     *
     * @param returnType
     * @param ids
     * @return
     */
    <O> List<O> selectByIds(Class<O> returnType, ID... ids);

    /**
     * 根据自定义条件查询 只返回一条记录
     *
     * @param select
     * @return
     */
    T selectOne(Select select);

    /**
     * 根据自定义条件查询 只返回一条记录(可指定返回类型)
     *
     * @param returnType
     * @param select
     * @return
     */
    <O> O selectOne(Class<O> returnType, Select select);

    /**
     * 根据自定义条件查询返回Map
     *
     * @param select
     * @return
     */
    Map<String, Object> selectMap(Select select);

    /**
     * 根据自定义条件查询返回Map
     *
     * @param valueType
     * @param select
     * @return
     */
    <O> Map<String, O> selectMap(Class<O> valueType, Select select);

    /**
     * 根据条件查询
     *
     * @param where
     * @param args
     * @return
     */
    T selectOneByCondition(String where, Object... args);

    /**
     * 根据条件查询(可指定返回类型、查询的表)
     *
     * @param returnType
     * @param where
     * @param args
     * @return
     */
    <O> O selectOneByCondition(Class<O> returnType, String where, Object... args);

    /**
     * 根据条件查询
     *
     * @param where
     * @param args
     * @return
     */
    List<T> selectByCondition(String where, Object... args);

    /**
     * 根据条件查询
     *
     * @param paging
     * @param where
     * @param args
     * @return
     */
    List<T> selectByCondition(Paging paging, String where, Object... args);

    /**
     * 根据条件查询(可指定返回类型、查询的表)
     *
     * @param returnType
     * @param where
     * @param args
     * @return
     */
    <O> List<O> selectByCondition(Class<O> returnType, String where, Object... args);

    /**
     * 根据条件查询(可指定返回类型、查询的表)
     *
     * @param returnType
     * @param paging
     * @param where
     * @param args
     * @return
     */
    <O> List<O> selectByCondition(Class<O> returnType, Paging paging, String where, Object... args);

    /**
     * 根据条件查询统计
     *
     * @param where
     * @param args
     * @return
     */
    long selectCountByCondition(String where, Object... args);

    /**
     * 统计全部
     *
     * @return
     */
    long countAll();

    /**
     * 查询全部
     *
     * @return
     */
    List<T> selectAll();

    /**
     * 查询全部(可指定返回类型、查询的表)
     *
     * @param returnType
     * @return
     */
    <O> List<O> selectAll(Class<O> returnType);

    /**
     * 查询全部
     *
     * @param paging
     * @return
     */
    List<T> selectAll(Paging paging);

    /**
     * 查询全部(可指定返回类型、查询的表)
     *
     * @param returnType
     * @param paging
     * @return
     */
    <O> List<O> selectAll(Class<O> returnType, Paging paging);

    /**
     * 根据自定义条件查询（可自动分页）返回List<Map>结果集
     *
     * @param select
     * @return
     */
    List<Map<String, Object>> selectMapList(Select select);

    /**
     * 根据自定义条件查询（可自动分页）返回List<Map>结果集
     *
     * @param valueType
     * @param select
     * @return
     */
    <O> List<Map<String, O>> selectMapList(Class<O> valueType, Select select);

    /**
     * 根据自定义条件查询（可自动分页）返回List<T>
     *
     * @param select
     * @return
     */
    List<T> select(Select select);

    /**
     * 根据自定义条件查询（可自动分页）返回List<O> (可指定返回类型、查询的表)
     *
     * @param returnType
     * @param select
     * @return
     */
    <O> List<O> select(Class<O> returnType, Select select);

    /**
     * 根据自定义条件统计
     *
     * @param select
     * @return
     */
    long count(Select select);

    /**
     * 根据自定义条件统计
     *
     * @param clazz
     * @param select
     * @return
     */
    long count(Class<?> clazz, Select select);

}
