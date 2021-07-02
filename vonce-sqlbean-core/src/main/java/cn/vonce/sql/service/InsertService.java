package cn.vonce.sql.service;

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
     * @param bean 单个实体或数组
     * @return
     */
    @SuppressWarnings("unchecked")
    int insert(T... bean);

    /**
     * 插入数据
     *
     * @param beanList 实体列表
     * @return
     */
    @SuppressWarnings("unchecked")
    int insert(List<T> beanList);

    /**
     * 插入数据
     *
     * @param insert 插入对象
     * @return
     */
    int insert(Insert<T> insert);

}
