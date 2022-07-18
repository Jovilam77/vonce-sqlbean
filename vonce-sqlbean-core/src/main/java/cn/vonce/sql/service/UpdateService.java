package cn.vonce.sql.service;

import cn.vonce.sql.bean.Update;
import cn.vonce.sql.helper.Wrapper;

/**
 * Update 通用业务接口
 *
 * @param <T>
 * @author Jovi
 * @version 1.0
 * @email 766255988@qq.com
 * @date 2019年6月27日下午3:57:33
 */
public interface UpdateService<T, ID> {

    /**
     * 根据id条件更新
     *
     * @param bean 更新的bean实体
     * @param id   id条件
     * @return
     */
    int updateById(T bean, ID id);

    /**
     * 根据id条件更新
     *
     * @param bean           更新的bean实体
     * @param id             id条件
     * @param updateNotNull  是否仅更新不为null的字段
     * @param optimisticLock 是否使用乐观锁
     * @return
     */
    int updateById(T bean, ID id, boolean updateNotNull, boolean optimisticLock);

    /**
     * 根据实体类id条件更新
     *
     * @param bean 更新的bean实体
     * @return
     */
    int updateByBeanId(T bean);

    /**
     * 根据实体类id条件更新
     *
     * @param bean           更新的bean实体
     * @param updateNotNull  是否仅更新不为null的字段
     * @param optimisticLock 是否使用乐观锁
     * @return
     */
    int updateByBeanId(T bean, boolean updateNotNull, boolean optimisticLock);

    /**
     * 根据实体类id条件更新
     *
     * @param bean           更新的bean实体
     * @param updateNotNull  是否仅更新不为null的字段
     * @param optimisticLock 是否使用乐观锁
     * @param filterFields   过滤不需更新的字段
     * @return
     */
    int updateByBeanId(T bean, boolean updateNotNull, boolean optimisticLock, String[] filterFields);

    /**
     * 根据实体类id条件更新
     *
     * @param bean           更新的bean实体
     * @param id             id条件
     * @param updateNotNull  是否仅更新不为null的字段
     * @param optimisticLock 是否使用乐观锁
     * @param filterFields   过滤不需更新的字段
     * @return
     */
    int updateById(T bean, ID id, boolean updateNotNull, boolean optimisticLock, String[] filterFields);

    /**
     * 根据条件更新
     *
     * @param bean  更新的bean实体
     * @param where 条件字符串表达式
     * @param args  条件参数
     * @return
     */
    int updateBy(T bean, String where, Object... args);

    /**
     * 根据条件更新
     *
     * @param bean           更新的bean实体
     * @param updateNotNull  是否仅更新不为null的字段
     * @param optimisticLock 是否使用乐观锁
     * @param where          条件字符串表达式
     * @param args           条件参数
     * @return
     */
    int updateBy(T bean, boolean updateNotNull, boolean optimisticLock, String where, Object... args);

    /**
     * 根据条件更新
     *
     * @param bean  更新的bean实体
     * @param where 条件包装器
     * @return
     */
    int updateBy(T bean, Wrapper where);

    /**
     * 根据条件更新
     *
     * @param bean           更新的bean实体
     * @param updateNotNull  是否仅更新不为null的字段
     * @param optimisticLock 是否使用乐观锁
     * @param where          条件包装器
     * @return
     */
    int updateBy(T bean, boolean updateNotNull, boolean optimisticLock, Wrapper where);

    /**
     * 根据条件更新
     *
     * @param bean           更新的bean实体
     * @param updateNotNull  是否仅更新不为null的字段
     * @param optimisticLock 是否使用乐观锁
     * @param filterFields   过滤不需更新的字段
     * @param where          条件字符串表达式
     * @param args           条件参数
     * @return
     */
    int updateBy(T bean, boolean updateNotNull, boolean optimisticLock, String[] filterFields, String where, Object... args);

    /**
     * 根据条件更新
     *
     * @param bean           更新的bean实体
     * @param updateNotNull  是否仅更新不为null的字段
     * @param optimisticLock 是否使用乐观锁
     * @param filterFields   过滤不需更新的字段
     * @param where          条件包装器
     * @return
     */
    int updateBy(T bean, boolean updateNotNull, boolean optimisticLock, String[] filterFields, Wrapper where);

    /**
     * 根据实体类字段条件更新
     *
     * @param bean  更新的bean实体
     * @param where 条件字符串表达式
     * @return
     */
    int updateByBean(T bean, String where);

    /**
     * 根据实体类字段条件更新
     *
     * @param bean           更新的bean实体
     * @param updateNotNull  是否仅更新不为null的字段
     * @param optimisticLock 是否使用乐观锁
     * @param where          条件字符串表达式
     * @return
     */
    int updateByBean(T bean, boolean updateNotNull, boolean optimisticLock, String where);

    /**
     * 根据实体类字段条件更新
     *
     * @param bean           更新的bean实体
     * @param updateNotNull  是否仅更新不为null的字段
     * @param optimisticLock 是否使用乐观锁
     * @param filterFields   过滤不需更新的字段
     * @param where          条件字符串表达式
     * @return
     */
    int updateByBean(T bean, boolean updateNotNull, boolean optimisticLock, String[] filterFields, String where);

    /**
     * 更新(where条件为空会抛异常，因为更新全部非常危险)
     *
     * @param update 更新对象
     * @return
     */
    int update(Update<T> update);

    /**
     * 更新
     *
     * @param update 更新对象
     * @param ignore 如果为true则不指定where条件也能执行，false则抛异常
     * @return
     */
    int update(Update<T> update, boolean ignore);

}
