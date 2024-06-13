package cn.vonce.sql.service;

import cn.vonce.sql.bean.*;
import cn.vonce.sql.define.ColumnFun;
import cn.vonce.sql.helper.Wrapper;

import java.util.List;

/**
 * 数据库操作业务接口
 *
 * @author Jovi
 * @version 1.0
 * @email imjovi@qq.com
 * @date 2020/6/30 15:40
 */
public interface DbManageService<T> {

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
     * @return
     */
    List<TableInfo> getTableList();

    /**
     * 获取表名列表
     *
     * @param tableName 可以为null
     * @return
     */
    List<TableInfo> getTableList(String tableName);

    /**
     * 获取表名列表
     *
     * @param schema
     * @param tableName
     * @return
     */
    List<TableInfo> getTableList(String schema, String tableName);

    /**
     * 获取列信息列表
     *
     * @return
     */
    List<ColumnInfo> getColumnInfoList();

    /**
     * 获取列信息列表
     *
     * @param tableName
     * @return
     */
    List<ColumnInfo> getColumnInfoList(String tableName);

    /**
     * 获取列信息列表
     *
     * @param schema
     * @param tableName
     * @return
     */
    List<ColumnInfo> getColumnInfoList(String schema, String tableName);

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
     * 备份表和数据到一张指定名称的新表
     *
     * @param wrapper         条件包装器
     * @param targetSchema    目标schema
     * @param targetTableName 目标表名
     * @return
     */
    void backup(Wrapper wrapper, String targetSchema, String targetTableName);

    /**
     * 根据条件备份表和数据到一张指定名称的新表
     *
     * @param wrapper         条件包装器
     * @param targetTableName 目标表名
     * @param columns         指定的列
     * @return
     */
    void backup(Wrapper wrapper, String targetTableName, Column... columns);

    /**
     * 根据条件备份表和数据到一张指定名称的新表
     *
     * @param wrapper         条件包装器
     * @param targetTableName 目标表名
     * @param columns         指定的列
     * @return
     */
    <R> void backup(Wrapper wrapper, String targetTableName, ColumnFun<T, R>... columns);

    /**
     * 根据条件备份表和数据到一张指定名称的新表
     *
     * @param wrapper         条件包装器
     * @param targetSchema    目标schema
     * @param targetTableName 目标表名
     * @param columns         指定的列
     * @return
     */
    void backup(Wrapper wrapper, String targetSchema, String targetTableName, Column... columns);

    /**
     * 根据条件备份表和数据到一张指定名称的新表
     *
     * @param wrapper         条件包装器
     * @param targetSchema    目标schema
     * @param targetTableName 目标表名
     * @param columns         指定的列
     * @return
     */
    <R> void backup(Wrapper wrapper, String targetSchema, String targetTableName, ColumnFun<T, R>... columns);

    /**
     * 根据条件将数据复制插入到同样结构的表中
     *
     * @param wrapper         条件包装器
     * @param targetTableName 目标表名
     * @return
     */
    int copy(Wrapper wrapper, String targetTableName);

    /**
     * 根据条件将数据复制插入到同样结构的表中
     *
     * @param wrapper         条件包装器
     * @param targetSchema    目标schema
     * @param targetTableName 目标表名
     * @return
     */
    int copy(Wrapper wrapper, String targetSchema, String targetTableName);

    /**
     * 根据条件将数据复制插入到指定结构的表中
     *
     * @param wrapper         条件包装器
     * @param targetTableName 目标表名
     * @param columns         指定的列
     * @return
     */
    int copy(Wrapper wrapper, String targetTableName, Column... columns);

    /**
     * 根据条件将数据复制插入到指定结构的表中
     *
     * @param wrapper         条件包装器
     * @param targetTableName 目标表名
     * @param columns         指定的列
     * @return
     */
    <R> int copy(Wrapper wrapper, String targetTableName, ColumnFun<T, R>... columns);

    /**
     * 根据条件将数据复制插入到指定结构的表中
     *
     * @param wrapper         条件包装器
     * @param columns         指定的列
     * @param targetSchema    目标schema
     * @param targetTableName 目标表名
     * @return
     */
    int copy(Wrapper wrapper, String targetSchema, String targetTableName, Column... columns);

    /**
     * 根据条件将数据复制插入到指定结构的表中
     *
     * @param wrapper         条件包装器
     * @param columns         指定的列
     * @param targetSchema    目标schema
     * @param targetTableName 目标表名
     * @return
     */
    <R> int copy(Wrapper wrapper, String targetSchema, String targetTableName, ColumnFun<T, R>... columns);

    /**
     * 更改表结构
     *
     * @param table          表对象
     * @param columnInfoList 列表信息列表
     * @return
     */
    int alter(Table table, List<ColumnInfo> columnInfoList);

    /**
     * 更改表结构
     *
     * @param alter 改变表结构对象
     * @return
     */
    int alter(Alter alter);

    /**
     * 更改表结构
     *
     * @param alterList 改变表结构对象列表
     * @return
     */
    int alter(List<Alter> alterList);

}
