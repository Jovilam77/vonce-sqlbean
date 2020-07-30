package cn.vonce.sql.orm.service;

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
    long dropTable();

    /**
     * 创建表结构
     *
     * @return
     */
    long createTable();

}
