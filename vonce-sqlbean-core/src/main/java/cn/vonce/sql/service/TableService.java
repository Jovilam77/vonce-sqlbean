package cn.vonce.sql.service;

import cn.vonce.sql.bean.Column;
import cn.vonce.sql.bean.ColumnInfo;
import cn.vonce.sql.bean.TableInfo;
import cn.vonce.sql.helper.Wrapper;

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
     * 获取Bean类型
     *
     * @return
     */
    Class<?> getBeanClass();

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
     * 获取表名列表
     *
     * @param tableName 可以为null
     * @return
     */
    List<TableInfo> getTableList(String tableName);

    /**
     * 获取列信息列表
     *
     * @param tableName
     * @return
     */
    List<ColumnInfo> getColumnInfoList(String tableName);

    /**
     * 备份表和数据到一张新表(表名_+时间)
     *
     * @return 默认创建的表名
     */
    String backup();

    /**
     * 备份表和数据到一张指定名称的新表
     *
     * @param targetTableName 目标表名
     * @return
     */
    void backup(String targetTableName);

    /**
     * 备份表和数据到一张指定名称的新表
     *
     * @param targetSchema    目标schema
     * @param targetTableName 目标表名
     * @return
     */
    void backup(String targetSchema, String targetTableName);

    /**
     * 根据条件备份表和数据到一张指定名称的新表
     *
     * @param targetTableName 目标表名
     * @param columns         指定的列
     * @param wrapper         条件包装器
     * @return
     */
    void backup(String targetTableName, Column[] columns, Wrapper wrapper);

    /**
     * 根据条件备份表和数据到一张指定名称的新表
     *
     * @param targetSchema    目标schema
     * @param targetTableName 目标表名
     * @param columns         指定的列
     * @param wrapper         条件包装器
     * @return
     */
    void backup(String targetSchema, String targetTableName, Column[] columns, Wrapper wrapper);

    /**
     * 根据条件将数据复制插入到同样结构的表中
     *
     * @param targetTableName 目标表名
     * @param wrapper         条件包装器
     * @return
     */
    int copy(String targetTableName, Wrapper wrapper);

    /**
     * 根据条件将数据复制插入到同样结构的表中
     *
     * @param targetSchema    目标schema
     * @param targetTableName 目标表名
     * @param wrapper         条件包装器
     * @return
     */
    int copy(String targetSchema, String targetTableName, Wrapper wrapper);

    /**
     * 根据条件将数据复制插入到指定结构的表中
     *
     * @param targetTableName 目标表名
     * @param columns         指定的列
     * @param wrapper         条件包装器
     * @return
     */
    int copy(String targetTableName, Column[] columns, Wrapper wrapper);

    /**
     * 根据条件将数据复制插入到指定结构的表中
     *
     * @param targetSchema    目标schema
     * @param targetTableName 目标表名
     * @param columns         指定的列
     * @param wrapper         条件包装器
     * @return
     */
    int copy(String targetSchema, String targetTableName, Column[] columns, Wrapper wrapper);

}
