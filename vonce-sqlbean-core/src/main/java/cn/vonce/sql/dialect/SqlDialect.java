package cn.vonce.sql.dialect;

import cn.vonce.sql.bean.Alter;
import cn.vonce.sql.config.SqlBeanDB;
import cn.vonce.sql.enumerate.JdbcType;
import cn.vonce.sql.uitls.SqlBeanUtil;

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
     * @param escape
     * @return
     */
    String addRemarks(boolean isTable, Alter item, String escape);

    /**
     * 获取模式列表sql
     *
     * @param sqlBeanDB
     * @param schemaName
     * @return
     */
    String getSchemaSql(SqlBeanDB sqlBeanDB, String schemaName);

    /**
     * 创建模式sql
     *
     * @param sqlBeanDB
     * @param schemaName
     * @return
     */
    String getCreateSchemaSql(SqlBeanDB sqlBeanDB, String schemaName);

    /**
     * 删除模式sql
     *
     * @param sqlBeanDB
     * @param schemaName
     * @return
     */
    String getDropSchemaSql(SqlBeanDB sqlBeanDB, String schemaName);

    /**
     * 获取schema名称
     *
     * @param sqlBeanDB
     * @param schemaName
     * @return
     */
    default String getSchemaName(SqlBeanDB sqlBeanDB, String schemaName) {
        return (SqlBeanUtil.isToUpperCase(sqlBeanDB) ? schemaName.toUpperCase() : schemaName);
    }

}
