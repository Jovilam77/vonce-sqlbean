package cn.vonce.sql.service;

import java.util.List;

/**
 * 表结构业务接口
 *
 * @author Jovi
 * @version 1.0
 * @email imjovi@qq.com
 * @date 2020/6/30 15:40
 */
public interface TableService {

    /**
     * 删除表结构
     *
     * @return
     */
    void dropTable();

    /**
     * 创建表结构
     *
     * @return
     */
    void createTable();

    /**
     * 删除并创建表结构
     */
    void dropAndCreateTable();

    /**
     * 表是否存在
     *
     * @return
     */
    List<String> getTableList();

}
