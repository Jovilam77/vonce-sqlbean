package cn.vonce.sql.orm.service;

import cn.vonce.sql.bean.Update;

/**
 * Update 通用业务接口
 *
 * @param <T>
 * @author Jovi
 * @version 1.0
 * @email 766255988@qq.com
 * @date 2019年6月27日下午3:57:33
 */
public interface UpdateService<T,ID> {

    /**
     * 根据id条件更新
     *
     * @param bean
     * @param id
     * @param updateNotNull
     * @return
     */
    long updateById(T bean, ID id, boolean updateNotNull);

    /**
     * 根据实体类id条件更新
     *
     * @param bean
     * @param updateNotNull
     * @return
     */
    long updateByBeanId(T bean, boolean updateNotNull);

    /**
     * 根据实体类id条件更新
     *
     * @param bean
     * @param updateNotNull
     * @param filterFields
     * @return
     */
    long updateByBeanId(T bean, boolean updateNotNull, String[] filterFields);

    /**
     * 根据实体类id条件更新
     *
     * @param bean
     * @param id
     * @param updateNotNull
     * @param filterFields
     * @return
     */
    long updateById(T bean, ID id, boolean updateNotNull, String[] filterFields);

    /**
     * 根据条件更新
     *
     * @param bean
     * @param updateNotNull
     * @param where
     * @param args
     * @return
     */
    long updateByCondition(T bean, boolean updateNotNull, String where, Object... args);

    /**
     * 根据条件更新
     *
     * @param bean
     * @param updateNotNull
     * @param filterFields
     * @param where
     * @param args
     * @return
     */
    long updateByCondition(T bean, boolean updateNotNull, String[] filterFields, String where, Object... args);

    /**
     * 根据实体类字段条件更新
     *
     * @param bean
     * @param updateNotNull
     * @param where
     * @return
     */
    long updateByBeanCondition(T bean, boolean updateNotNull, String where);

    /**
     * 根据实体类字段条件更新
     *
     * @param bean
     * @param updateNotNull
     * @param filterFields
     * @param where
     * @return
     */
    long updateByBeanCondition(T bean, boolean updateNotNull, String[] filterFields, String where);

    /**
     * 更新(where条件为空会抛异常，因为更新全部非常危险)
     *
     * @param update
     * @return
     */
    long update(Update update);

    /**
     * 更新
     *
     * @param update
     * @param ignore 如果为true则不指定where条件也能执行，false则抛异常
     * @return
     */
    long update(Update update, boolean ignore);

}
