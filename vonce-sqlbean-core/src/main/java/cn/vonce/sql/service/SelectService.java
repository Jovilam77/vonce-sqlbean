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
     * @param id 唯一id
     * @return
     */
    T selectById(ID id);

    /**
     * 根据id条件查询(可指定返回类型、查询的表)
     *
     * @param returnType 指定返回到类型
     * @param id         唯一id
     * @return
     */
    <R> R selectById(Class<R> returnType, ID id);

    /**
     * 根据ids条件查询
     *
     * @param ids 唯一id数组
     * @return
     */
    List<T> selectByIds(ID... ids);

    /**
     * 根据id条件查询(可指定返回类型、查询的表)
     *
     * @param returnType 指定返回到类型
     * @param ids        唯一id数组
     * @return
     */
    <R> List<R> selectByIds(Class<R> returnType, ID... ids);

    /**
     * 根据自定义条件查询 只返回一条记录
     *
     * @param select 查询对象
     * @return
     */
    T selectOne(Select select);

    /**
     * 根据自定义条件查询 只返回一条记录(可指定返回类型)
     *
     * @param returnType 指定返回到类型
     * @param select     查询对象
     * @return
     */
    <R> R selectOne(Class<R> returnType, Select select);

    /**
     * 根据自定义条件查询返回Map
     *
     * @param select 查询对象
     * @return
     */
    Map<String, Object> selectMap(Select select);

    /**
     * 根据条件查询
     *
     * @param where 查询条件
     * @param args  条件参数
     * @return
     */
    T selectOneBy(String where, Object... args);

    /**
     * 根据条件查询(可指定返回类型、查询的表)
     *
     * @param returnType 指定返回到类型
     * @param where      查询条件
     * @param args       条件参数
     * @return
     */
    <R> R selectOneBy(Class<R> returnType, String where, Object... args);

    /**
     * 根据条件查询
     *
     * @param where 条件包装器
     * @return
     */
    T selectOneBy(Wrapper where);

    /**
     * 根据条件查询
     *
     * @param returnType 指定返回到类型
     * @param where      条件包装器
     * @param <R>
     * @return
     */
    <R> R selectOneBy(Class<R> returnType, Wrapper where);

    /**
     * 根据条件查询
     *
     * @param where 查询条件
     * @param args  条件参数
     * @return
     */
    List<T> selectBy(String where, Object... args);

    /**
     * 根据条件查询
     *
     * @param where 条件包装器
     * @return
     */
    List<T> selectBy(Wrapper where);

    /**
     * 根据条件查询
     *
     * @param paging 分页对象
     * @param where  查询条件
     * @param args   条件参数
     * @return
     */
    List<T> selectBy(Paging paging, String where, Object... args);

    /**
     * 根据条件查询
     *
     * @param paging 分页对象
     * @param where  条件包装器
     * @return
     */
    List<T> selectBy(Paging paging, Wrapper where);

    /**
     * 根据条件查询(可指定返回类型、查询的表)
     *
     * @param returnType 指定返回到类型
     * @param where      查询条件
     * @param args       条件参数
     * @return
     */
    <R> List<R> selectBy(Class<R> returnType, String where, Object... args);

    /**
     * 根据条件查询(可指定返回类型、查询的表)
     *
     * @param returnType 指定返回到类型
     * @param where      条件包装器
     * @param <R>
     * @return
     */
    <R> List<R> selectBy(Class<R> returnType, Wrapper where);

    /**
     * 根据条件查询(可指定返回类型、查询的表)
     *
     * @param returnType 指定返回到类型
     * @param paging     分页对象
     * @param where      查询条件
     * @param args       条件参数
     * @return
     */
    <R> List<R> selectBy(Class<R> returnType, Paging paging, String where, Object... args);

    /**
     * 根据条件查询(可指定返回类型、查询的表)
     *
     * @param returnType 指定返回到类型
     * @param paging     分页对象
     * @param where      条件包装器
     * @param <R>
     * @return
     */
    <R> List<R> selectBy(Class<R> returnType, Paging paging, Wrapper where);

    /**
     * 根据条件查询统计
     *
     * @param where 查询条件
     * @param args  条件参数
     * @return
     */
    int countBy(String where, Object... args);

    /**
     * 根据条件查询统计
     *
     * @param where 条件包装器
     * @return
     */
    int countBy(Wrapper where);

    /**
     * 统计全部
     *
     * @return
     */
    int count();

    /**
     * 查询全部
     *
     * @return
     */
    List<T> select();

    /**
     * 查询全部(可指定返回类型、查询的表)
     *
     * @param returnType 指定返回到类型
     * @return
     */
    <R> List<R> select(Class<R> returnType);

    /**
     * 查询全部
     *
     * @param paging 分页对象
     * @return
     */
    List<T> select(Paging paging);

    /**
     * 查询全部(可指定返回类型、查询的表)
     *
     * @param returnType 指定返回到类型
     * @param paging     分页对象
     * @return
     */
    <R> List<R> select(Class<R> returnType, Paging paging);

    /**
     * 根据自定义条件查询（可自动分页）返回List<Map>结果集
     *
     * @param select 查询对象
     * @return
     */
    List<Map<String, Object>> selectMapList(Select select);

    /**
     * 根据自定义条件查询（可自动分页）返回List<T>
     *
     * @param select 查询对象
     * @return
     */
    List<T> select(Select select);

    /**
     * 根据自定义条件查询（可自动分页）返回List<R> (可指定返回类型、查询的表)
     *
     * @param returnType 指定返回到类型
     * @param select     查询对象
     * @return
     */
    <R> List<R> select(Class<R> returnType, Select select);

    /**
     * 根据自定义条件统计
     *
     * @param select 查询对象
     * @return
     */
    int count(Select select);

    /**
     * 根据自定义条件统计
     *
     * @param returnType 指定返回到类型
     * @param select     查询对象
     * @return
     */
    int count(Class<?> returnType, Select select);

    /**
     * 分页
     *
     * @param select     查询对象
     * @param pageHelper 分页助手
     * @return
     */
    ResultData<T> paging(Select select, PageHelper<T> pageHelper);

    /**
     * 分页
     *
     * @param select  查询对象
     * @param pagenum 当前页
     * @param pagenum 每页数量
     * @return
     */
    ResultData<T> paging(Select select, int pagenum, int pagesize);

    /**
     * 分页
     *
     * @param returnType 指定返回到类型
     * @param select     查询对象
     * @param pageHelper 分页助手
     * @param <R>
     * @return
     */
    <R> ResultData<R> paging(Class<R> returnType, Select select, PageHelper<R> pageHelper);

    /**
     * 分页
     *
     * @param returnType 指定返回到类型
     * @param select     查询对象
     * @param pagenum    当前页
     * @param pagenum    每页数量
     * @param <R>
     * @return
     */
    <R> ResultData<R> paging(Class<R> returnType, Select select, int pagenum, int pagesize);

}
