package cn.vonce.sql.service;

import cn.vonce.sql.bean.Delete;
import cn.vonce.sql.define.ConditionHandle;
import cn.vonce.sql.helper.Wrapper;

/**
 * Delete 通用业务接口
 *
 * @param
 * @author Jovi
 * @version 1.0
 * @email imjovi@qq.com
 * @date 2019年6月27日下午3:57:33
 */
public interface DeleteService<T, ID> {

    /**
     * 根据id条件删除
     *
     * @param id 单个id或数组
     * @return
     */
    int deleteById(ID... id);

    /**
     * 根据条件删除
     *
     * @param where 条件表达式
     * @param args  条件参数
     * @return
     */
    int deleteBy(String where, Object... args);

    /**
     * 根据条件删除
     *
     * @param where 条件包装器
     * @return
     */
    int deleteBy(Wrapper where);

    /**
     * 根据条件删除
     *
     * @param cond Lambda条件
     * @return
     */
    int deleteBy(ConditionHandle<T> cond);

    /**
     * 删除(where条件为空会抛异常，因为删除全部非常危险)
     *
     * @param delete 删除对象
     * @return
     */
    int delete(Delete delete);

    /**
     * 删除
     *
     * @param delete 删除对象
     * @param ignore 如果为true则不指定where条件也能执行，false则抛异常
     * @return
     */
    int delete(Delete delete, boolean ignore);

    /**
     * 逻辑删除根据id条件
     *
     * @param id 单个id或数组
     * @return
     */
    int logicallyDeleteById(ID... id);

    /**
     * 根据条件逻辑删除
     *
     * @param where 条件表达式
     * @param args  条件参数
     * @return
     */
    int logicallyDeleteBy(String where, Object... args);

    /**
     * 根据条件逻辑删除
     *
     * @param where 条件包装器
     * @return
     */
    int logicallyDeleteBy(Wrapper where);

    /**
     * 根据条件逻辑删除
     *
     * @param cond Lambda条件
     * @return
     */
    int logicallyDeleteBy(ConditionHandle<T> cond);

}
