package cn.vonce.sql.service;

import cn.vonce.sql.bean.Paging;
import cn.vonce.sql.bean.Select;
import cn.vonce.sql.helper.Wrapper;
import cn.vonce.sql.page.PageHelper;
import cn.vonce.sql.page.PagingService;
import cn.vonce.sql.page.ResultData;

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
    <R> R selectById(Class<R> returnType, ID id);

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
    <R> List<R> selectByIds(Class<R> returnType, ID... ids);

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
    <R> R selectOne(Class<R> returnType, Select select);

    /**
     * 根据自定义条件查询返回Map
     *
     * @param select
     * @return
     */
    Map<String, Object> selectMap(Select select);

    /**
     * 根据条件查询 过时，将在1.6.0版本中移除，请尽快使用新方法替代
     *
     * @param where
     * @param args
     * @return
     */
    @Deprecated
    T selectOneByCondition(String where, Object... args);

    /**
     * 根据条件查询
     *
     * @param where
     * @param args
     * @return
     */
    T selectOneBy(String where, Object... args);

    /**
     * 根据条件查询(可指定返回类型、查询的表) 过时，将在1.6.0版本中移除，请尽快使用新方法替代
     *
     * @param returnType
     * @param where
     * @param args
     * @return
     */
    @Deprecated
    <R> R selectOneByCondition(Class<R> returnType, String where, Object... args);

    /**
     * 根据条件查询(可指定返回类型、查询的表)
     *
     * @param returnType
     * @param where
     * @param args
     * @return
     */
    <R> R selectOneBy(Class<R> returnType, String where, Object... args);

    /**
     * 根据条件查询 过时，将在1.6.0版本中移除，请尽快使用新方法替代
     *
     * @param where
     * @return
     */
    @Deprecated
    T selectOneByCondition(Wrapper where);

    /**
     * 根据条件查询
     *
     * @param where
     * @return
     */
    T selectOneBy(Wrapper where);

    /**
     * 根据条件查询 过时，将在1.6.0版本中移除，请尽快使用新方法替代
     *
     * @param returnType
     * @param where
     * @param <R>
     * @return
     */
    @Deprecated
    <R> R selectOneByCondition(Class<R> returnType, Wrapper where);

    /**
     * 根据条件查询
     *
     * @param returnType
     * @param where
     * @param <R>
     * @return
     */
    <R> R selectOneBy(Class<R> returnType, Wrapper where);

    /**
     * 根据条件查询 过时，将在1.6.0版本中移除，请尽快使用新方法替代
     *
     * @param where
     * @param args
     * @return
     */
    @Deprecated
    List<T> selectByCondition(String where, Object... args);

    /**
     * 根据条件查询
     *
     * @param where
     * @param args
     * @return
     */
    List<T> selectBy(String where, Object... args);

    /**
     * 根据条件查询 过时，将在1.6.0版本中移除，请尽快使用新方法替代
     *
     * @param where
     * @return
     */
    @Deprecated
    List<T> selectByCondition(Wrapper where);

    /**
     * 根据条件查询
     *
     * @param where
     * @return
     */
    List<T> selectBy(Wrapper where);

    /**
     * 根据条件查询 过时，将在1.6.0版本中移除，请尽快使用新方法替代
     *
     * @param paging
     * @param where
     * @param args
     * @return
     */
    @Deprecated
    List<T> selectByCondition(Paging paging, String where, Object... args);

    /**
     * 根据条件查询
     *
     * @param paging
     * @param where
     * @param args
     * @return
     */
    List<T> selectBy(Paging paging, String where, Object... args);

    /**
     * 根据条件查询 过时，将在1.6.0版本中移除，请尽快使用新方法替代
     *
     * @param paging
     * @param where
     * @return
     */
    @Deprecated
    List<T> selectByCondition(Paging paging, Wrapper where);

    /**
     * 根据条件查询
     *
     * @param paging
     * @param where
     * @return
     */
    List<T> selectBy(Paging paging, Wrapper where);

    /**
     * 根据条件查询(可指定返回类型、查询的表)
     *
     * @param returnType
     * @param where
     * @param args
     * @return
     */
    @Deprecated
    <R> List<R> selectByCondition(Class<R> returnType, String where, Object... args);

    /**
     * 根据条件查询(可指定返回类型、查询的表) 过时，将在1.6.0版本中移除，请尽快使用新方法替代
     *
     * @param returnType
     * @param where
     * @param args
     * @return
     */
    <R> List<R> selectBy(Class<R> returnType, String where, Object... args);

    /**
     * 根据条件查询(可指定返回类型、查询的表) 过时，将在1.6.0版本中移除，请尽快使用新方法替代
     *
     * @param returnType
     * @param where
     * @param <R>
     * @return
     */
    @Deprecated
    <R> List<R> selectByCondition(Class<R> returnType, Wrapper where);

    /**
     * 根据条件查询(可指定返回类型、查询的表)
     *
     * @param returnType
     * @param where
     * @param <R>
     * @return
     */
    <R> List<R> selectBy(Class<R> returnType, Wrapper where);

    /**
     * 根据条件查询(可指定返回类型、查询的表) 过时，将在1.6.0版本中移除，请尽快使用新方法替代
     *
     * @param returnType
     * @param paging
     * @param where
     * @param args
     * @return
     */
    @Deprecated
    <R> List<R> selectByCondition(Class<R> returnType, Paging paging, String where, Object... args);

    /**
     * 根据条件查询(可指定返回类型、查询的表)
     *
     * @param returnType
     * @param paging
     * @param where
     * @param args
     * @return
     */
    <R> List<R> selectBy(Class<R> returnType, Paging paging, String where, Object... args);

    /**
     * 根据条件查询(可指定返回类型、查询的表) 过时，将在1.6.0版本中移除，请尽快使用新方法替代
     *
     * @param returnType
     * @param paging
     * @param where
     * @param <R>
     * @return
     */
    @Deprecated
    <R> List<R> selectByCondition(Class<R> returnType, Paging paging, Wrapper where);

    /**
     * 根据条件查询(可指定返回类型、查询的表)
     *
     * @param returnType
     * @param paging
     * @param where
     * @param <R>
     * @return
     */
    <R> List<R> selectBy(Class<R> returnType, Paging paging, Wrapper where);

    /**
     * 根据条件查询统计 过时，将在1.6.0版本中移除，请尽快使用新方法替代
     *
     * @param where
     * @param args
     * @return
     */
    @Deprecated
    int selectCountByCondition(String where, Object... args);

    /**
     * 根据条件查询统计
     *
     * @param where
     * @param args
     * @return
     */
    int countBy(String where, Object... args);

    /**
     * 根据条件查询统计 过时，将在1.6.0版本中移除，请尽快使用新方法替代
     *
     * @param where
     * @return
     */
    @Deprecated
    int selectCountByCondition(Wrapper where);

    /**
     * 根据条件查询统计
     *
     * @param where
     * @return
     */
    int countBy(Wrapper where);

    /**
     * 统计全部 过时，将在1.6.0版本中移除，请尽快使用新方法替代
     *
     * @return
     */
    @Deprecated
    int countAll();

    /**
     * 统计全部
     *
     * @return
     */
    int count();

    /**
     * 查询全部 过时，将在1.6.0版本中移除，请尽快使用新方法替代
     *
     * @return
     */
    @Deprecated
    List<T> selectAll();

    /**
     * 查询全部
     *
     * @return
     */
    List<T> select();

    /**
     * 查询全部(可指定返回类型、查询的表) 过时，将在1.6.0版本中移除，请尽快使用新方法替代
     *
     * @param returnType
     * @return
     */
    @Deprecated
    <R> List<R> selectAll(Class<R> returnType);

    /**
     * 查询全部(可指定返回类型、查询的表)
     *
     * @param returnType
     * @return
     */
    <R> List<R> select(Class<R> returnType);

    /**
     * 查询全部 过时，将在1.6.0版本中移除，请尽快使用新方法替代
     *
     * @param paging
     * @return
     */
    @Deprecated
    List<T> selectAll(Paging paging);

    /**
     * 查询全部
     *
     * @param paging
     * @return
     */
    List<T> select(Paging paging);

    /**
     * 查询全部(可指定返回类型、查询的表) 过时，将在1.6.0版本中移除，请尽快使用新方法替代
     *
     * @param returnType
     * @param paging
     * @return
     */
    <R> List<R> selectAll(Class<R> returnType, Paging paging);

    /**
     * 查询全部(可指定返回类型、查询的表)
     *
     * @param returnType
     * @param paging
     * @return
     */
    <R> List<R> select(Class<R> returnType, Paging paging);

    /**
     * 根据自定义条件查询（可自动分页）返回List<Map>结果集
     *
     * @param select
     * @return
     */
    List<Map<String, Object>> selectMapList(Select select);

    /**
     * 根据自定义条件查询（可自动分页）返回List<T>
     *
     * @param select
     * @return
     */
    List<T> select(Select select);

    /**
     * 根据自定义条件查询（可自动分页）返回List<R> (可指定返回类型、查询的表)
     *
     * @param returnType
     * @param select
     * @return
     */
    <R> List<R> select(Class<R> returnType, Select select);

    /**
     * 根据自定义条件统计
     *
     * @param select
     * @return
     */
    int count(Select select);

    /**
     * 根据自定义条件统计
     *
     * @param clazz
     * @param select
     * @return
     */
    int count(Class<?> clazz, Select select);

    ResultData<T> paging(Select select, PageHelper<T> pageHelper);

    ResultData<T> paging(Select select, int pagenum, int pagesize);

    <R> ResultData<R> paging(Class<R> tClazz, Select select, PageHelper<R> pageHelper);

    <R> ResultData<R> paging(Class<R> tClazz, Select select, int pagenum, int pagesize);

}
