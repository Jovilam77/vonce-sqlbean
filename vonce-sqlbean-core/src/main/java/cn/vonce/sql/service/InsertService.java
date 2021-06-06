package cn.vonce.sql.service;

import cn.vonce.sql.bean.Column;
import cn.vonce.sql.bean.Condition;
import cn.vonce.sql.bean.Insert;
import cn.vonce.sql.helper.Wrapper;

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
     */
    @SuppressWarnings("unchecked")
    int insert(T... bean);

    /**
     * 插入数据
     *
     * @param beanList
     * @return
     */
    @SuppressWarnings("unchecked")
    int insert(List<T> beanList);

    /**
     * 插入数据
     *
     * @param insert
     * @return
     */
    int inset(Insert insert);

    /**
     * 备份表和数据到一张新表(表名_+时间)
     *
     * @return 默认创建的表名
     */
    String backup();

    /**
     * 备份表和数据到一张指定名称的新表
     *
     * @param targetTableName
     * @return
     */
    void backup(String targetTableName);

    /**
     * 根据条件备份表和数据到一张指定名称的新表
     *
     * @param targetTableName
     * @param columns
     * @param wrapper
     * @return
     */
    void backup(String targetTableName, Column[] columns, Wrapper wrapper);

    /**
     * 根据条件将数据复制插入到同样结构的表中
     *
     * @param targetTableName
     * @param wrapper
     * @return
     */
    int copy(String targetTableName, Wrapper wrapper);

    /**
     * 根据条件将数据复制插入到指定结构的表中
     *
     * @param targetTableName
     * @param columns
     * @param wrapper
     * @return
     */
    int copy(String targetTableName, Column[] columns, Wrapper wrapper);

}
