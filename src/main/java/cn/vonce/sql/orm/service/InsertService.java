package cn.vonce.sql.orm.service;

import cn.vonce.sql.bean.Insert;

import java.util.List;

/**
 * Insert 通用业务接口
 *
 * @param <T>
 * @author Jovi
 * @version 1.0
 * @email 766255988@qq.com
 * @date 2019年6月27日下午3:57:33
 */
public interface InsertService<T> {

    /**
     * 插入数据
     *
     * @param bean
     * @return
     * @author Jovi
     * @date 2018年5月15日下午4:05:03
     */
    @SuppressWarnings("unchecked")
    long insert(T... bean);

    /**
     * 插入数据
     *
     * @param beanList
     * @return
     * @author Jovi
     * @date 2018年5月15日下午4:05:03
     */
    @SuppressWarnings("unchecked")
    long insert(List<T> beanList);

    /**
     * 插入数据
     *
     * @param insert
     * @return
     * @author Jovi
     * @date 2018年5月15日下午4:05:03
     */
    long inset(Insert insert);

}
