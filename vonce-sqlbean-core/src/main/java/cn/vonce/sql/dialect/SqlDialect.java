package cn.vonce.sql.dialect;

import cn.vonce.sql.bean.Alter;
import cn.vonce.sql.config.SqlBeanDB;
import cn.vonce.sql.enumerate.JdbcType;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Sql方言
 *
 * @author Jovi
 * @email imjovi@qq.com
 * @date 2024/4/16 9:32
 */
public interface SqlDialect<T> {

    /**
     * 获取字段对应的Jdbc类型
     *
     * @param field
     * @return
     */
    T getType(Field field);

    /**
     * 获取字段对应的Jdbc类型
     *
     * @param field
     * @return
     */
    JdbcType getJdbcType(Field field);

    /**
     * 查询表信息sql
     *
     * @param sqlBeanDB
     * @param schema
     * @param tableName
     * @return
     */
    String getTableListSql(SqlBeanDB sqlBeanDB, String schema, String tableName);

    /**
     * 查询列信息sql
     *
     * @param sqlBeanDB
     * @param schema
     * @param tableName
     * @return
     */
    String getColumnListSql(SqlBeanDB sqlBeanDB, String schema, String tableName);

    /**
     * 更改表结构sql
     *
     * @param alterList
     * @return
     */
    List<String> alterTable(List<Alter> alterList);

    /**
     * 更改注释sql
     *
     * @param isTable
     * @param item
     * @param transferred
     * @return
     */
    String addRemarks(boolean isTable, Alter item, String transferred);

    /**
     * 获取数据库列表sql
     *
     * @param name
     * @return
     */
    String getDatabaseSql(String name);

    /**
     * 创建数据库sql
     *
     * @param sqlBeanDB
     * @param name
     * @return
     */
    String getCreateDatabaseSql(SqlBeanDB sqlBeanDB, String name);

    /**
     * 删除数据库sql
     *
     * @param name
     * @return
     */
    String getDropDatabaseSql(String name);

}
