package cn.vonce.sql.dialect;

import cn.vonce.sql.bean.Alter;
import cn.vonce.sql.config.SqlBeanMeta;
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
     * @param sqlBeanMeta
     * @param schema
     * @param tableName
     * @return
     */
    String getTableListSql(SqlBeanMeta sqlBeanMeta, String schema, String tableName);

    /**
     * 查询列信息sql
     *
     * @param sqlBeanMeta
     * @param schema
     * @param tableName
     * @return
     */
    String getColumnListSql(SqlBeanMeta sqlBeanMeta, String schema, String tableName);

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
     * @param sqlBeanMeta
     * @param schemaName
     * @return
     */
    String getSchemaSql(SqlBeanMeta sqlBeanMeta, String schemaName);

    /**
     * 创建模式sql
     *
     * @param sqlBeanMeta
     * @param schemaName
     * @return
     */
    String getCreateSchemaSql(SqlBeanMeta sqlBeanMeta, String schemaName);

    /**
     * 删除模式sql
     *
     * @param sqlBeanMeta
     * @param schemaName
     * @return
     */
    String getDropSchemaSql(SqlBeanMeta sqlBeanMeta, String schemaName);

    /**
     * 获取schema名称
     *
     * @param sqlBeanMeta
     * @param schemaName
     * @return
     */
    default String getSchemaName(SqlBeanMeta sqlBeanMeta, String schemaName) {
        return (SqlBeanUtil.isToUpperCase(sqlBeanMeta) ? schemaName.toUpperCase() : schemaName);
    }

}
