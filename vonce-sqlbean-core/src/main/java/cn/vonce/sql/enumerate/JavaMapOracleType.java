package cn.vonce.sql.enumerate;

import cn.vonce.sql.bean.Alter;
import cn.vonce.sql.bean.Common;
import cn.vonce.sql.bean.Table;
import cn.vonce.sql.config.SqlBeanDB;
import cn.vonce.sql.constant.SqlConstant;
import cn.vonce.sql.uitls.SqlBeanUtil;
import cn.vonce.sql.uitls.StringUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Java类型对应的Oracle类型枚举类
 *
 * @author Jovi
 * @email imjovi@qq.com
 * @date 2022/8/22 16:28
 */
public enum JavaMapOracleType {

    FLOAT(new Class[]{float.class, Float.class, double.class, Double.class, BigDecimal.class}), NUMBER(new Class[]{boolean.class, Boolean.class, byte.class, Byte.class, short.class, Short.class, int.class, Integer.class, long.class, Long.class}), CHAR(new Class[]{char.class, Character.class}), VARCHAR2(new Class[]{String.class}), DATE(new Class[]{java.sql.Date.class, java.sql.Time.class, java.sql.Timestamp.class, java.util.Date.class}), CLOB(new Class[]{java.sql.Clob.class}), BLOB(new Class[]{java.sql.Blob.class, Object.class});


    JavaMapOracleType(Class<?>[] classes) {
        this.classes = classes;
    }

    private Class<?>[] classes;

    public static JavaMapOracleType getType(Class<?> clazz) {
        for (JavaMapOracleType javaType : values()) {
            for (Class<?> thisClazz : javaType.classes) {
                if (thisClazz == clazz) {
                    return javaType;
                }
            }
        }
        return null;
    }

    public static String getTypeName(Class<?> clazz) {
        return getType(clazz).name();
    }

    /**
     * 获取表数据列表的SQL
     *
     * @param sqlBeanDB
     * @param schema
     * @param tableName
     * @return
     */
    public static String getTableListSql(SqlBeanDB sqlBeanDB, String schema, String tableName) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT t.table_name AS \"name\", c.comments AS \"remarks\" ");
        sql.append("FROM user_tables t ");
        sql.append("LEFT JOIN user_tab_comments c ");
        sql.append("ON c.table_name = t.table_name");
        if (StringUtil.isNotEmpty(tableName)) {
            sql.append(" AND t.table_name = '");
            sql.append((sqlBeanDB.getSqlBeanConfig().getToUpperCase() != null && sqlBeanDB.getSqlBeanConfig().getToUpperCase()) ? tableName.toUpperCase() : tableName);
            sql.append("'");
        }
        return sql.toString();
    }

    /**
     * 获取列数据列表的SQL
     *
     * @param sqlBeanDB
     * @param tableName
     * @return
     */
    public static String getColumnListSql(SqlBeanDB sqlBeanDB, String schema, String tableName) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT col.column_id AS cid, col.column_name AS name, col.data_type AS type, ");
        sql.append("(CASE col.nullable WHEN 'N' THEN '1' ELSE '0' END) AS notnull, col.data_default AS dflt_value, ");
        sql.append("(CASE uc1.constraint_type WHEN 'P' THEN '1' ELSE '0' END) AS pk, ");
        sql.append("(CASE uc2.constraint_type WHEN 'R' THEN '1' ELSE '0' END) AS fk, ");
        sql.append("(CASE WHEN col.data_type = 'FLOAT' OR col.data_type = 'DOUBLE' OR col.data_type = 'DECIMAL' OR col.data_type = 'NUMBER' THEN col.data_precision ELSE col.char_length END) AS length, ");
        sql.append("col.data_scale AS scale, ");
        sql.append("user_col_comments.comments AS remarks ");
        sql.append("FROM user_tab_columns col ");
        sql.append("LEFT JOIN user_cons_columns ucc ON ucc.table_name = col.table_name AND ucc.column_name = col.column_name AND ucc.position IS NOT NULL ");
        sql.append("LEFT JOIN user_constraints uc1 ON uc1.constraint_name = ucc.constraint_name AND uc1.constraint_type = 'P' ");
        sql.append("LEFT JOIN user_constraints uc2 ON uc2.constraint_name = ucc.constraint_name AND uc2.constraint_type = 'R' ");
        sql.append("INNER JOIN user_col_comments ON user_col_comments.table_name = col.table_name AND user_col_comments.column_name = col.column_name ");
        sql.append("WHERE col.table_name = '");
        sql.append(tableName);
        sql.append("'");
        return sql.toString();
    }

    /**
     * 更改表结构
     *
     * @param alterList
     * @return
     */
    public static List<String> alterTable(List<Alter> alterList) {
        List<String> sqlList = new ArrayList<>();
        String transferred = SqlBeanUtil.getTransferred(alterList.get(0));
        Table table = alterList.get(0).getTable();
        StringBuffer addOrModifySql = new StringBuffer();
        addOrModifySql.append(SqlConstant.ALTER_TABLE);
        addOrModifySql.append(getFullName(alterList.get(0), table));
        for (int i = 0; i < alterList.size(); i++) {
            Alter alter = alterList.get(i);
            if (alter.getType() == AlterType.ADD) {
                addOrModifySql.append(SqlConstant.ADD);
                addOrModifySql.append(SqlConstant.BEGIN_BRACKET);
                addOrModifySql.append(SqlBeanUtil.addColumn(alter, alter.getColumnInfo(), null));
                addOrModifySql.append(SqlConstant.END_BRACKET);
                addOrModifySql.append(SqlConstant.SPACES);
                sqlList.add(addRemarks(alter, transferred));
            } else if (alter.getType() == AlterType.MODIFY) {
                addOrModifySql.append(modifyColumn(alter));
                sqlList.add(addRemarks(alter, transferred));
            } else if (alter.getType() == AlterType.DROP) {
                StringBuffer dropSql = new StringBuffer();
                dropSql.append(SqlConstant.ALTER_TABLE);
                dropSql.append(getFullName(alter, table));
                dropSql.append(SqlConstant.DROP);
                dropSql.append(SqlConstant.BEGIN_BRACKET);
                dropSql.append(transferred);
                dropSql.append(SqlBeanUtil.isToUpperCase(alter) ? alter.getColumnInfo().getName().toUpperCase() : alter.getColumnInfo().getName());
                dropSql.append(transferred);
                dropSql.append(SqlConstant.END_BRACKET);
                sqlList.add(dropSql.toString());
            } else if (alter.getType() == AlterType.CHANGE) {
                sqlList.add(changeColumn(alter));
                sqlList.add(addRemarks(alter, transferred));
                //更改名称的同时可能也更改其他信息
                alter.getColumnInfo().setName(alter.getOldColumnName());
                addOrModifySql.append(modifyColumn(alter));
            }
        }
        //新增更改类型信息的语句需要先执行
        sqlList.add(0, addOrModifySql.toString());
        return sqlList;
    }

    /**
     * 获取全名
     *
     * @param common
     * @param table
     * @return
     */
    private static String getFullName(Common common, Table table) {
        String transferred = SqlBeanUtil.getTransferred(common);
        boolean toUpperCase = SqlBeanUtil.isToUpperCase(common);
        StringBuffer sql = new StringBuffer();
        if (StringUtil.isNotBlank(table.getSchema())) {
            sql.append(transferred);
            sql.append(toUpperCase ? table.getSchema().toUpperCase() : table.getSchema());
            sql.append(transferred);
            sql.append(SqlConstant.POINT);
        }
        sql.append(transferred);
        sql.append(toUpperCase ? table.getName().toUpperCase() : table.getName());
        sql.append(transferred);
        sql.append(SqlConstant.SPACES);
        return sql.toString();
    }

    /**
     * 更改列信息
     *
     * @param alter
     * @return
     */
    private static String modifyColumn(Alter alter) {
        StringBuffer modifySql = new StringBuffer();
        modifySql.append(SqlConstant.MODIFY);
        modifySql.append(SqlConstant.BEGIN_BRACKET);
        modifySql.append(SqlBeanUtil.addColumn(alter, alter.getColumnInfo(), null));
        modifySql.append(SqlConstant.END_BRACKET);
        modifySql.append(SqlConstant.SPACES);
        return modifySql.toString();
    }

    /**
     * 更改字段名
     *
     * @param alter
     * @return
     */
    private static String changeColumn(Alter alter) {
        StringBuffer changeSql = new StringBuffer();
        changeSql.append(SqlConstant.ALTER_TABLE);
        changeSql.append(getFullName(alter, alter.getTable()));
        changeSql.append(SqlConstant.RENAME);
        changeSql.append(SqlConstant.COLUMN);
        changeSql.append(SqlBeanUtil.isToUpperCase(alter) ? alter.getOldColumnName().toUpperCase() : alter.getOldColumnName());
        changeSql.append(SqlConstant.TO);
        changeSql.append(SqlBeanUtil.isToUpperCase(alter) ? alter.getColumnInfo().getName().toUpperCase() : alter.getColumnInfo().getName());
        return changeSql.toString();
    }

    /**
     * 增加列备注
     *
     * @param item
     * @param transferred
     * @return
     */
    private static String addRemarks(Alter item, String transferred) {
        StringBuffer remarksSql = new StringBuffer();
        if (StringUtil.isNotBlank(item.getColumnInfo().getRemarks())) {
            remarksSql.append(SqlConstant.COMMENT);
            remarksSql.append(SqlConstant.ON);
            remarksSql.append(SqlConstant.COLUMN);
            remarksSql.append(getFullName(item, item.getTable()));
            remarksSql.append(SqlConstant.POINT);
            remarksSql.append(transferred);
            remarksSql.append(item.getColumnInfo().getName());
            remarksSql.append(transferred);
            remarksSql.append(SqlConstant.IS);
            remarksSql.append(SqlConstant.SINGLE_QUOTATION_MARK);
            remarksSql.append(item.getColumnInfo().getRemarks());
            remarksSql.append(SqlConstant.SINGLE_QUOTATION_MARK);
        }
        return remarksSql.toString();
    }

}
