package cn.vonce.sql.orm.service;

import cn.vonce.sql.bean.Delete;

/**
 * Delete 通用业务接口
 *
 * @param
 * @author Jovi
 * @version 1.0
 * @email 766255988@qq.com
 * @date 2019年6月27日下午3:57:33
 */
public interface DeleteService<ID> {

    /**
     * 根据id条件删除
     *
     * @param id
     * @return
     * @author Jovi
     * @date 2018年5月15日下午4:04:39
     */
    long deleteById(ID... id);

    /**
     * 根据条件删除
     *
     * @param where
     * @param args
     * @return
     * @author Jovi
     * @date 2018年5月15日下午4:04:43
     */
    long deleteByCondition(String where, Object... args);

    /**
     * 删除(where条件为空会抛异常，因为删除全部非常危险)
     *
     * @param delete
     * @return
     * @author Jovi
     * @date 2018年5月15日下午4:04:43
     */
    long delete(Delete delete);

    /**
     * 删除
     *
     * @param delete
     * @param ignore  如果为true则不指定where条件也能执行，false则抛异常
     * @return
     * @author Jovi
     * @date 2018年1月12日下午4:19:43
     */
    long delete(Delete delete, boolean ignore);

    /**
     * 逻辑删除根据id条件
     *
     * @param id
     * @return
     * @author Jovi
     * @date 2019年6月6日下午16:41:20
     */
    long logicallyDeleteById(ID id);

    /**
     * 根据条件逻辑删除
     *
     * @param where
     * @param args
     * @return
     * @author Jovi
     * @date 2019年6月6日下午16:41:20
     */
    long logicallyDeleteByCondition(String where, Object... args);

}
