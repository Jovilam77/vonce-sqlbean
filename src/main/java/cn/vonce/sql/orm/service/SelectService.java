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
     * @author Jovi
     * @date 2018年5月15日下午4:03:28
     */
    T selectById(ID id);

    /**
     * 根据id条件查询(可指定返回类型、查询的表)
     *
     * @param returnType
     * @param id
     * @return
     * @author Jovi
     * @date 2018年6月15日下午6:36:38
     */
    <O> O selectById(Class<O> returnType, ID id);

    /**
     * 根据ids条件查询
     *
     * @param ids
     * @return
     * @author Jovi
     * @date 2018年5月15日下午3:58:28
     */
    List<T> selectByIds(ID... ids);

    /**
     * 根据id条件查询(可指定返回类型、查询的表)
     *
     * @param returnType
     * @param ids
     * @return
     * @author Jovi
     * @date 2018年6月15日下午6:36:38
     */
    <O> List<O> selectByIds(Class<O> returnType, ID... ids);

    /**
     * 根据自定义条件查询 只返回一条记录
     *
     * @param select
     * @return
     * @author Jovi
     * @date 2018年5月15日下午4:03:42
     */
    T selectOne(Select select);

    /**
     * 根据自定义条件查询 只返回一条记录(可指定返回类型)
     *
     * @param returnType
     * @param select
     * @return
     * @author Jovi
     * @date 2018年5月15日下午3:58:46
     */
    <O> O selectOne(Class<O> returnType, Select select);

    /**
     * 根据自定义条件查询返回Map
     *
     * @param select
     * @return
     * @author Jovi
     * @date 2019年5月17日下午5:11:22
     */
    Map<String, Object> selectMap(Select select);

    /**
     * 根据条件查询
     *
     * @param where
     * @param args
     * @return
     * @author Jovi
     * @date 2018年5月15日下午4:03:56
     */
    T selectOneByCondition(String where, Object... args);

    /**
     * 根据条件查询(可指定返回类型、查询的表)
     *
     * @param returnType
     * @param where
     * @param args
     * @return
     * @author Jovi
     * @date 2018年5月15日下午4:03:56
     */
    <O> O selectOneByCondition(Class<O> returnType, String where, Object... args);

    /**
     * 根据条件查询
     *
     * @param where
     * @param args
     * @return
     * @author Jovi
     * @date 2018年5月15日下午4:03:56
     */
    List<T> selectByCondition(String where, Object... args);

    /**
     * 根据条件查询
     *
     * @param paging
     * @param where
     * @param args
     * @return
     * @author Jovi
     * @date 2018年5月15日下午4:03:56
     */
    List<T> selectByCondition(Paging paging, String where, Object... args);

    /**
     * 根据条件查询(可指定返回类型、查询的表)
     *
     * @param returnType
     * @param where
     * @param args
     * @return
     * @author Jovi
     * @date 2018年5月15日下午4:03:56
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
     * @author Jovi
     * @date 2018年5月15日下午4:03:56
     */
    <O> List<O> selectByCondition(Class<O> returnType, Paging paging, String where, Object... args);

    /**
     * 根据条件查询统计
     *
     * @param where
     * @param args
     * @return
     * @author Jovi
     * @date 2018年7月5日下午4:09:45
     */
    long selectCountByCondition(String where, Object... args);

    /**
     * 统计全部
     *
     * @return
     * @author Jovi
     * @date 2019年6月24日下午3:20:21
     */
    long countAll();

    /**
     * 查询全部
     *
     * @return
     * @author Jovi
     * @date 2018年5月15日下午4:03:59
     */
    List<T> selectAll();

    /**
     * 查询全部(可指定返回类型、查询的表)
     *
     * @param returnType
     * @return
     * @author Jovi
     * @date 2018年5月15日下午4:03:59
     */
    <O> List<O> selectAll(Class<O> returnType);

    /**
     * 查询全部
     *
     * @param paging
     * @return
     * @author Jovi
     * @date 2019年6月23日下午11:59:49
     */
    List<T> selectAll(Paging paging);

    /**
     * 查询全部(可指定返回类型、查询的表)
     *
     * @param returnType
     * @param paging
     * @return
     * @author Jovi
     * @date 2019年6月23日下午11:59:49
     */
    <O> List<O> selectAll(Class<O> returnType, Paging paging);

    /**
     * 根据自定义条件查询（可自动分页）返回List<Map>结果集
     *
     * @param select
     * @return
     * @author Jovi
     * @date 2018年5月15日下午4:04:15
     */
    List<Map<String, Object>> selectMapList(Select select);

    /**
     * 根据自定义条件查询（可自动分页）返回List<T>
     *
     * @param select
     * @return
     * @author Jovi
     * @date 2018年5月15日下午4:04:15
     */
    List<T> select(Select select);

    /**
     * 根据自定义条件查询（可自动分页）返回List<O> (可指定返回类型、查询的表)
     *
     * @param returnType
     * @param select
     * @return
     * @author Jovi
     * @date 2018年6月13日下午2:39:08
     */
    <O> List<O> select(Class<O> returnType, Select select);

    /**
     * 根据自定义条件统计
     *
     * @param select
     * @return
     * @author Jovi
     * @date 2018年5月15日下午4:04:34
     */
    long count(Select select);

    /**
     * 根据自定义条件统计
     *
     * @param clazz
     * @param select
     * @return
     * @author Jovi
     * @date 2018年5月15日下午4:04:34
     */
    long count(Class<?> clazz, Select select);

}
