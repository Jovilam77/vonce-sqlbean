package cn.vonce.sql.service;

import java.util.List;

/**
 * 数据库操作业务接口
 *
 * @author Jovi
 * @version 1.0
 * @email imjovi@qq.com
 * @date 2024/6/11 16:53
 */
public interface AdvancedDbManageService<T> extends DbManageService<T> {

    /**
     * 更改表注释
     *
     * @param remarks 注释
     * @return
     */
    int alterRemarks(String remarks);

    /**
     * 模式列表
     *
     * @param name
     * @return
     */
    List<String> getSchemas(String name);

    /**
     * 创建模式
     *
     * @param name
     * @return
     */
    int createSchema(String name);

    /**
     * 删除模式
     *
     * @param name
     * @return
     */
    int dropSchema(String name);

}
