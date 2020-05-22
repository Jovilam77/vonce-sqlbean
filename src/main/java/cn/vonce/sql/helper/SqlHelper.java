package cn.vonce.sql.helper;

import cn.vonce.common.utils.ReflectAsmUtil;
import cn.vonce.common.utils.StringUtil;
import cn.vonce.sql.annotation.SqlId;
import cn.vonce.sql.annotation.SqlUnion;
import cn.vonce.sql.annotation.SqlTable;
import cn.vonce.sql.annotation.SqlVersion;
import cn.vonce.sql.bean.*;
import cn.vonce.sql.constant.SqlHelperCons;
import cn.vonce.sql.enumerate.*;
import cn.vonce.sql.exception.SqlBeanException;
import cn.vonce.sql.uitls.SqlBeanUtil;
import com.google.common.collect.ListMultimap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * SQL 语句助手
 *
 * @author jovi
 * @version 1.0
 * @email 766255988@qq.com
 * @date 2017年6月2日下午5:41:59
 */
public class SqlHelper {

    private static Logger logger = LoggerFactory.getLogger(SqlHelper.class);

    /**
     * 参数为空抛出异常
     *
     * @param object
     * @param message
     */
    public static void isNull(Object object, String message) {
        if (object == null) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * 检查
     */
    public static void check(Common common) {
        isNull(common.getSqlBeanConfig(), "请设置sqlBeanConfig");
        isNull(common.getSqlBeanConfig().getDbType(), "请设置sqlBeanConfig -> dbType");
    }

    /**
     * 生成select sql语句
     *
     * @param select
     * @return
     */
    public static String buildSelectSql(Select select) {
        check(select);
        StringBuffer sqlSb = new StringBuffer();
        Integer[] pageParam = null;
        String orderSql = orderBySql(select);
        //SQLServer2008 分页处理
        if (select.getSqlBeanConfig().getDbType() == DbType.SQLServer2008) {
            if (SqlBeanUtil.isUsePage(select)) {
                pageParam = pageParam(select);
                sqlSb.append(SqlHelperCons.SELECT);
                sqlSb.append(SqlHelperCons.ALL);
                sqlSb.append(SqlHelperCons.FROM);
                sqlSb.append(SqlHelperCons.BEGIN_BRACKET);
            }
        }
        //标准Sql
        sqlSb.append(select.isUseDistinct() ? SqlHelperCons.SELECT_DISTINCT : SqlHelperCons.SELECT);
        //SqlServer 分页处理
        if (select.getSqlBeanConfig().getDbType() == DbType.SQLServer2008) {
            if (SqlBeanUtil.isUsePage(select)) {
                sqlSb.append(SqlHelperCons.TOP);
                sqlSb.append(pageParam[0]);
                sqlSb.append(SqlHelperCons.ROW_NUMBER + SqlHelperCons.OVER + SqlHelperCons.BEGIN_BRACKET + orderSql + SqlHelperCons.END_BRACKET + SqlHelperCons.ROWNUM + SqlHelperCons.COMMA);
            }
        }
        //标准Sql
        sqlSb.append(column(select));
        sqlSb.append(SqlHelperCons.FROM);
        sqlSb.append(fromFullName(select));
        sqlSb.append(joinSql(select));
        sqlSb.append(whereSql(select, null));
        String groupBySql = groupBySql(select);
        sqlSb.append(groupBySql);
        sqlSb.append(havingSql(select));
        if (!SqlBeanUtil.isCount(select)) {
            sqlSb.append(orderSql);
        }
        //SQLServer2008 分页处理
        if (select.getSqlBeanConfig().getDbType() == DbType.SQLServer2008) {
            // 主要逻辑 结束
            if (SqlBeanUtil.isUsePage(select)) {
                sqlSb.append(SqlHelperCons.END_BRACKET);
                sqlSb.append(SqlHelperCons.T);
                sqlSb.append(SqlHelperCons.WHERE);
                sqlSb.append(SqlHelperCons.T + SqlHelperCons.POINT + SqlHelperCons.ROWNUM);
                sqlSb.append(SqlHelperCons.GREATER_THAN);
                sqlSb.append(pageParam[1]);
            }
        }
        //标准Sql
        if (SqlBeanUtil.isCount(select) && StringUtil.isNotEmpty(groupBySql)) {
            sqlSb.insert(0, SqlHelperCons.SELECT + SqlHelperCons.COUNT + SqlHelperCons.BEGIN_BRACKET + SqlHelperCons.ALL + SqlHelperCons.END_BRACKET + SqlHelperCons.FROM + SqlHelperCons.BEGIN_BRACKET);
            sqlSb.append(SqlHelperCons.END_BRACKET + SqlHelperCons.AS + SqlHelperCons.T);
        }
        //MySQL,MariaDB,H2 分页处理
        if (select.getSqlBeanConfig().getDbType() == DbType.MySQL || select.getSqlBeanConfig().getDbType() == DbType.MariaDB || select.getSqlBeanConfig().getDbType() == DbType.H2) {
            mysqlPageDispose(select, sqlSb);
        }
        //PostgreSQL,SQLite,Hsql 分页处理
        else if (select.getSqlBeanConfig().getDbType() == DbType.PostgreSQL || select.getSqlBeanConfig().getDbType() == DbType.SQLite || select.getSqlBeanConfig().getDbType() == DbType.Hsql) {
            postgreSqlPageDispose(select, sqlSb);
        }
        //Oracle 分页处理
        else if (select.getSqlBeanConfig().getDbType() == DbType.Oracle) {
            oraclePageDispose(select, sqlSb);
        }
        //DB2 分页处理
        else if (select.getSqlBeanConfig().getDbType() == DbType.DB2) {
            db2PageDispose(select, sqlSb);
        }
        //Derby 分页处理
        else if (select.getSqlBeanConfig().getDbType() == DbType.Derby) {
            derbyPageDispose(select, sqlSb);
        }
        return sqlSb.toString();
    }

    /**
     * 生成update sql语句(目前仅限数据库字段与实体类bean字段一致使用)
     *
     * @param update
     * @return
     * @throws SqlBeanException
     */
    public static String buildUpdateSql(Update update) {
        check(update);
        StringBuffer sqlSb = new StringBuffer();
        sqlSb.append(SqlHelperCons.UPDATE);
        sqlSb.append(getTableName(update, update.getUpdateBean()));
        sqlSb.append(SqlHelperCons.SET);
        sqlSb.append(setSql(update));
        sqlSb.append(whereSql(update, update.getUpdateBean()));
        return sqlSb.toString();
    }

    /**
     * 生成insert sql语句(目前仅限数据库字段与实体类bean字段一致使用)
     *
     * @param insert
     * @return
     */
    @SuppressWarnings("unchecked")
    public static String buildInsertSql(Insert insert) {
        check(insert);
        Object[] objects;
        if (insert.getInsertBean().getClass().isArray()) {
            objects = (Object[]) insert.getInsertBean();
        } else if (insert.getInsertBean() instanceof Collection) {
            Collection<Object> list = (Collection<Object>) insert.getInsertBean();
            objects = list.toArray();
        } else {
            objects = new Object[]{insert.getInsertBean()};
        }
        String sql = null;
        try {
            sql = fieldAndValuesSql(insert, objects);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        }
        return sql;
    }

    /**
     * 生成delete sql语句
     *
     * @param delete
     * @return
     */
    public static String buildDeleteSql(Delete delete) {
        check(delete);
        StringBuffer sqlSb = new StringBuffer();
        sqlSb.append(SqlHelperCons.DELETE_FROM);
        sqlSb.append(getTableName(delete, null));
        sqlSb.append(whereSql(delete, null));
        return sqlSb.toString();
    }

    /**
     * 返回带转义表名,优先级 tableName第一，注解第二，类名第三
     *
     * @param common
     * @param bean
     * @return
     */
    private static String getTableName(Common common, Object bean) {
        String schema = common.getTable().getSchema();
        String tableName = common.getTable().getName();
        if ((common.getTable() == null || StringUtil.isEmpty(tableName)) && bean != null) {
            SqlTable sqlTable = bean.getClass().getAnnotation(SqlTable.class);
            if (sqlTable != null) {
                schema = sqlTable.schema();
                tableName = sqlTable.value();
            }
        }
        if (StringUtil.isEmpty(tableName) && bean != null) {
            tableName = bean.getClass().getSimpleName();
        }
        if (StringUtil.isNotEmpty(schema)) {
            tableName = schema + SqlHelperCons.POINT + tableName;
        }
        return SqlBeanUtil.isToUpperCase(common) ? tableName.toUpperCase() : tableName;
    }

    /**
     * 返回column语句
     *
     * @param select
     * @return
     */
    private static String column(Select select) {
        StringBuffer columnSql = new StringBuffer();
        if (select.getColumnList() != null && select.getColumnList().size() != 0) {
            for (int i = 0; i < select.getColumnList().size(); i++) {
                String schema = select.getColumnList().get(i).getSchema();
                String tableAlias = select.getColumnList().get(i).getTableAlias();
                String columnName = select.getColumnList().get(i).getName();
                String transferred = SqlBeanUtil.getTransferred(select);
                boolean existAlias = StringUtil.isNotEmpty(select.getColumnList().get(i).getAlias());
                if (existAlias) {
                    columnSql.append(SqlHelperCons.BEGIN_BRACKET);
                }
                if (StringUtil.isNotEmpty(schema)) {
                    columnSql.append(SqlBeanUtil.isToUpperCase(select) ? schema.toUpperCase() : schema);
                    columnSql.append(SqlHelperCons.POINT);
                }
                if (StringUtil.isNotEmpty(tableAlias)) {
                    columnSql.append(transferred);
                    columnSql.append(SqlBeanUtil.isToUpperCase(select) ? tableAlias.toUpperCase() : tableAlias);
                    columnSql.append(transferred);
                    columnSql.append(SqlHelperCons.POINT);
                }
                columnSql.append(columnName);
                if (existAlias) {
                    columnSql.append(SqlHelperCons.END_BRACKET);
                    columnSql.append(SqlHelperCons.AS);
                    columnSql.append(transferred);
                    columnSql.append(select.getColumnList().get(i).getAlias());
                    columnSql.append(transferred);
                }
                columnSql.append(SqlHelperCons.COMMA);
            }
            columnSql.deleteCharAt(columnSql.length() - SqlHelperCons.COMMA.length());
        }
        return columnSql.toString();
    }

    /**
     * 返回from的表名包括别名
     *
     * @param select
     * @return
     */
    private static String fromFullName(Select select) {
        String transferred = SqlBeanUtil.getTransferred(select);
        StringBuffer fromSql = new StringBuffer();
        if (StringUtil.isNotEmpty(select.getTable().getSchema())) {
            fromSql.append(select.getTable().getSchema());
            fromSql.append(SqlHelperCons.POINT);
        }
        fromSql.append(select.getTable().getName());
        fromSql.append(SqlHelperCons.SPACES);
        fromSql.append(transferred);
        fromSql.append(select.getTable().getAlias());
        fromSql.append(transferred);
        return fromSql.toString();
    }

    /**
     * 返回innerJoin语句
     *
     * @param select
     * @return
     */
    private static String joinSql(Select select) {
        StringBuffer joinSql = new StringBuffer();
        if (select != null && select.getJoin().size() != 0) {
            for (int i = 0; i < select.getJoin().size(); i++) {
                Join join = select.getJoin().get(i);
                switch (join.getJoinType()) {
                    case INNER_JOIN:
                        joinSql.append(SqlHelperCons.INNER_JOIN);
                        break;
                    case LEFT_JOIN:
                        joinSql.append(SqlHelperCons.LEFT_JOIN);
                        break;
                    case RIGHT_JOIN:
                        joinSql.append(SqlHelperCons.RIGHT_JOIN);
                        break;
                    case FULL_JOIN:
                        joinSql.append(SqlHelperCons.FULL_JOIN);
                        break;
                }
                String schema = join.getSchema();
                String tableName = join.getTableName();
                String tableAlias = join.getTableAlias();
                String tableKeyword = SqlBeanUtil.getTableFieldFullName(select, schema, tableAlias, join.getTableKeyword());
                String mainKeyword = SqlBeanUtil.getTableFieldFullName(select, select.getTable().getSchema(), select.getTable().getAlias(), join.getMainKeyword());
                if (SqlBeanUtil.isToUpperCase(select)) {
                    schema = schema.toUpperCase();
                    tableName = tableName.toUpperCase();
                    tableAlias = tableAlias.toUpperCase();
                    tableKeyword = tableKeyword.toUpperCase();
                    mainKeyword = mainKeyword.toUpperCase();
                }
                if (StringUtil.isNotEmpty(schema)) {
                    joinSql.append(schema);
                    joinSql.append(SqlHelperCons.POINT);
                }
                joinSql.append(tableName);
                joinSql.append(SqlHelperCons.SPACES);
                String transferred = SqlBeanUtil.getTransferred(select);
                joinSql.append(transferred);
                joinSql.append(tableAlias);
                joinSql.append(transferred);
                joinSql.append(SqlHelperCons.ON);
                joinSql.append(tableKeyword);
                joinSql.append(SqlHelperCons.EQUAL_TO);
                joinSql.append(mainKeyword);
                if (i < select.getJoin().size() - 1) {
                    joinSql.append(SqlHelperCons.SPACES);
                }
            }
        }
        return joinSql.toString();
    }

    /**
     * 返回field及values语句
     *
     * @param common
     * @param objects
     * @return
     * @throws IllegalArgumentException
     */
    private static String fieldAndValuesSql(Common common, Object[] objects) throws IllegalArgumentException {
        String tableName = getTableName(common, objects[0]);
        StringBuffer fieldSql = new StringBuffer();
        StringBuffer valueSql = new StringBuffer();
        StringBuffer fieldAndValuesSql = new StringBuffer();
        List<String> valueSqlList = new ArrayList<>();
        String transferred = SqlBeanUtil.getTransferred(common);
        //获取sqlbean的全部字段
        Field[] fields;
        if (objects[0].getClass().getAnnotation(SqlUnion.class) != null) {
            fields = objects[0].getClass().getSuperclass().getDeclaredFields();
        } else {
            fields = objects[0].getClass().getDeclaredFields();
        }
        if (common.getSqlBeanConfig().getDbType() == DbType.Oracle) {
            if (common.getSqlBeanConfig().getToUpperCase()) {
                tableName = tableName.toUpperCase();
            }
            if (objects != null && objects.length > 1) {
                fieldAndValuesSql.append(SqlHelperCons.INSERT_ALL_INTO);
            } else {
                fieldAndValuesSql.append(SqlHelperCons.INSERT_INTO);
            }
        } else {
            fieldAndValuesSql.append(SqlHelperCons.INSERT_INTO);
        }
        for (int i = 0; i < objects.length; i++) {
            //每次必须清空
            valueSql.delete(0, valueSql.length());
            //只有在循环第一遍的时候才会处理
            if (i == 0) {
                fieldSql.append(SqlHelperCons.BEGIN_BRACKET);
            }
            valueSql.append(SqlHelperCons.BEGIN_BRACKET);
            int existId = 0;
            for (Field field : fields) {
                if (Modifier.isStatic(field.getModifiers())) {
                    continue;
                }
                if (SqlBeanUtil.isIgnore(field)) {
                    continue;
                }
                SqlId sqlId = field.getAnnotation(SqlId.class);
                if (sqlId != null) {
                    existId++;
                }
                if (existId > 1) {
                    throw new SqlBeanException("请正确的标识id字段，id字段只能标识一个，但我们在'" + field.getDeclaringClass().getName() + "'此实体类或其父类找到了不止一处");
                }
                //只有在循环第一遍的时候才会处理
                if (i == 0) {
                    String tableFieldName = SqlBeanUtil.getTableFieldName(field);
                    //如果此字段非id字段 或者 此字段为id字段但是不是自增的id则生成该字段的insert语句
                    if (sqlId == null || (sqlId != null && sqlId.generateType() != GenerateType.AUTO)) {
                        fieldSql.append(transferred + (SqlBeanUtil.isToUpperCase(common) ? tableFieldName.toUpperCase() : tableFieldName) + transferred);
                        fieldSql.append(SqlHelperCons.COMMA);
                    }
                }
                //如果此字段为id且需要生成唯一id
                if (sqlId != null && sqlId.generateType() != GenerateType.AUTO && sqlId.generateType() != GenerateType.NORMAL) {
                    Object value = ReflectAsmUtil.get(objects[i].getClass(), objects[i], field.getName());
                    if (StringUtil.isEmpty(value)) {
                        value = common.getSqlBeanConfig().getUniqueIdProcessor().uniqueId(sqlId.generateType());
                    }
                    valueSql.append(SqlBeanUtil.getSqlValue(common, value));
                    valueSql.append(SqlHelperCons.COMMA);
                } else {
                    valueSql.append(SqlBeanUtil.getSqlValue(common, ReflectAsmUtil.get(objects[i].getClass(), objects[i], field.getName())));
                    valueSql.append(SqlHelperCons.COMMA);
                }
            }
            valueSql.deleteCharAt(valueSql.length() - SqlHelperCons.COMMA.length());
            valueSql.append(SqlHelperCons.END_BRACKET);
            valueSqlList.add(valueSql.toString());
            //只有在循环第一遍的时候才会处理
            if (i == 0) {
                fieldSql.deleteCharAt(fieldSql.length() - SqlHelperCons.COMMA.length());
                fieldSql.append(SqlHelperCons.END_BRACKET);
            }
        }
        if (common.getSqlBeanConfig().getDbType() == DbType.Oracle) {
            for (int k = 0; k < valueSqlList.size(); k++) {
                if (k > 0) {
                    fieldAndValuesSql.append(SqlHelperCons.INTO);
                }
                fieldAndValuesSql.append(tableName);
                fieldAndValuesSql.append(fieldSql.toString());
                fieldAndValuesSql.append(SqlHelperCons.VALUES);
                fieldAndValuesSql.append(valueSqlList.get(k));
            }
            if (objects != null && objects.length > 1) {
                fieldAndValuesSql.append(SqlHelperCons.SELECT_DUAL);
            }
        } else {
            for (int k = 0; k < valueSqlList.size(); k++) {
                if (k == 0) {
                    fieldAndValuesSql.append(tableName);
                    fieldAndValuesSql.append(fieldSql.toString());
                    fieldAndValuesSql.append(SqlHelperCons.VALUES);
                }
                fieldAndValuesSql.append(valueSqlList.get(k));
                fieldAndValuesSql.append(SqlHelperCons.COMMA);
            }
            fieldAndValuesSql.deleteCharAt(fieldAndValuesSql.length() - SqlHelperCons.COMMA.length());
        }
        return fieldAndValuesSql.toString();
    }

    /**
     * 返回set语句
     *
     * @param update
     * @return
     */
    private static String setSql(Update update) {
        StringBuffer setSql = new StringBuffer();
        String transferred = SqlBeanUtil.getTransferred(update);
        String[] filterFields = update.getFilterFields();
        Object bean = update.getUpdateBean();
        Field[] fields;
        if (bean.getClass().getAnnotation(SqlUnion.class) != null) {
            fields = bean.getClass().getSuperclass().getDeclaredFields();
        } else {
            fields = bean.getClass().getDeclaredFields();
        }
        List<Field> filterAfterList = new ArrayList<>();
        for (int i = 0; i < fields.length; i++) {
            if (Modifier.isStatic(fields[i].getModifiers())) {
                continue;
            }
            String name = SqlBeanUtil.getTableFieldName(fields[i]);
            Object objectValue = ReflectAsmUtil.get(bean.getClass(), bean, fields[i].getName());
            if (update.isUpdateNotNull()) {
                if (objectValue == null) {
                    continue;
                }
            }
            if (SqlBeanUtil.isIgnore(fields[i])) {
                continue;
            }
            if (SqlBeanUtil.isFilter(filterFields, name)) {
                continue;
            }
            filterAfterList.add(fields[i]);
        }
        for (int i = 0; i < filterAfterList.size(); i++) {
            String name = SqlBeanUtil.getTableFieldName(filterAfterList.get(i));
            Object objectValue = ReflectAsmUtil.get(bean.getClass(), bean, filterAfterList.get(i).getName());
            setSql.append(transferred);
            setSql.append(SqlBeanUtil.isToUpperCase(update) ? name.toUpperCase() : name);
            setSql.append(transferred);
            setSql.append(SqlHelperCons.EQUAL_TO);
            if (filterAfterList.get(i).getAnnotation(SqlVersion.class) != null) {
                Object o = SqlBeanUtil.updateVersion(filterAfterList.get(i).getType().getName(), objectValue);
                setSql.append(SqlBeanUtil.getSqlValue(update, o));
            } else {
                setSql.append(SqlBeanUtil.getSqlValue(update, objectValue));
            }
            setSql.append(SqlHelperCons.COMMA);
        }
        setSql.deleteCharAt(setSql.length() - SqlHelperCons.COMMA.length());
        return setSql.toString();
    }

    /**
     * 返回where语句
     *
     * @param condition
     * @param bean
     * @return
     */
    @SuppressWarnings("unchecked")
    private static String whereSql(Condition condition, Object bean) {
        return conditionHandle(ConditionType.WHERE, condition, condition.getWhere(), condition.getAgrs(), bean, condition.getWhereMap());
    }

    /**
     * 返回groupBy语句
     *
     * @param select
     * @return
     */
    private static String groupBySql(Select select) {
        return groupByAndOrderBySql(SqlHelperCons.GROUP_BY, select);
    }

    /**
     * @param select
     * @return
     */
    private static String havingSql(Select select) {
        return conditionHandle(ConditionType.HAVING, select, select.getHaving(), select.getHavingArgs(), null, select.getHavingMap());
    }

    /**
     * 返回orderBy语句
     *
     * @param select
     * @return
     */
    private static String orderBySql(Select select) {
        return groupByAndOrderBySql(SqlHelperCons.ORDER_BY, select);
    }

    /**
     * 返回orderBy和groupBy语句
     *
     * @param type   SqlHelperCons.ORDER_BY or SqlHelperCons.GROUP_BY
     * @param select
     * @return
     */
    private static String groupByAndOrderBySql(String type, Select select) {
        StringBuffer groupByAndOrderBySql = new StringBuffer();
        SqlColumn[] sqlColumn;
        if (SqlHelperCons.ORDER_BY.equals(type)) {
            sqlColumn = select.getOrderBy().toArray(new SqlColumn[]{});
        } else {
            sqlColumn = select.getGroupBy().toArray(new SqlColumn[]{});
        }
        String transferred = SqlBeanUtil.getTransferred(select);
        if (sqlColumn != null && sqlColumn.length != 0) {
            groupByAndOrderBySql.append(type);
            for (int i = 0; i < sqlColumn.length; i++) {
                SqlColumn orderBy = sqlColumn[i];
                groupByAndOrderBySql.append(transferred);
                String tableAlias;
                if (StringUtil.isNotEmpty(orderBy.getTableAlias())) {
                    tableAlias = orderBy.getTableAlias();
                } else {
                    tableAlias = select.getTable().getAlias();
                }
                String name = orderBy.getName();
                if (SqlBeanUtil.isToUpperCase(select)) {
                    tableAlias = tableAlias.toUpperCase();
                    name = name.toUpperCase();
                }
                groupByAndOrderBySql.append(tableAlias);
                groupByAndOrderBySql.append(transferred);
                groupByAndOrderBySql.append(SqlHelperCons.POINT);
                groupByAndOrderBySql.append(name);
                if (SqlHelperCons.ORDER_BY.equals(type)) {
                    groupByAndOrderBySql.append(SqlHelperCons.SPACES);
                    groupByAndOrderBySql.append(select.getOrderBy().get(i).getSqlSort().name());
                    groupByAndOrderBySql.append(SqlHelperCons.SPACES);
                }
                groupByAndOrderBySql.append(SqlHelperCons.COMMA);
            }
            groupByAndOrderBySql.deleteCharAt(groupByAndOrderBySql.length() - SqlHelperCons.COMMA.length());
        } else {
            if (SqlHelperCons.ORDER_BY.equals(type) && select.getSqlBeanConfig().getDbType() == DbType.SQLServer2008 && SqlBeanUtil.isUsePage(select) && !SqlBeanUtil.isCount(select)) {
                groupByAndOrderBySql.append(type);
                String tableFieldFullName = SqlBeanUtil.getTableFieldFullName(select, select.getTable().getSchema(), select.getTable().getAlias(), select.getPage().getIdName());
                groupByAndOrderBySql.append(SqlBeanUtil.isToUpperCase(select) ? tableFieldFullName.toUpperCase() : tableFieldFullName);
            }
        }
        return groupByAndOrderBySql.toString();
    }

    /**
     * 条件处理
     *
     * @param conditionType
     * @param common
     * @param where
     * @param args
     * @param whereMap
     * @return
     */
    private static String conditionHandle(ConditionType conditionType, Common common, String where, Object[] args, Object bean, ListMultimap<String, SqlCondition> whereMap) {
        StringBuffer conditionSql = new StringBuffer();
        StringBuffer versionConditionSql = null;
        Field versionField = null;
        boolean versionEffectiveness = false;
        //更新时乐观锁处理
        if (common instanceof Update) {
            versionConditionSql = new StringBuffer();
            if (bean != null) {
                versionField = SqlBeanUtil.getVersionField(bean.getClass());
            }
            if (versionField != null) {
                versionEffectiveness = SqlBeanUtil.versionEffectiveness(versionField.getType().getName());
                if (versionEffectiveness) {
                    versionConditionSql.append(SqlHelperCons.BEGIN_BRACKET);
                    versionConditionSql.append(SqlBeanUtil.getTableFieldName(versionField));
                    Object versionValue = ReflectAsmUtil.get(bean.getClass(), bean, versionField.getName());
                    versionConditionSql.append(versionValue == null ? SqlHelperCons.IS : SqlHelperCons.EQUAL_TO);
                    versionConditionSql.append(SqlBeanUtil.getSqlValue(common, versionValue));
                    versionConditionSql.append(SqlHelperCons.END_BRACKET);
                    versionConditionSql.append(SqlHelperCons.AND);
                }
            }
        }
        // 优先使用条件字符串拼接
        if (where != null && !"".equals(where)) {
            conditionSql.append(ConditionType.WHERE == conditionType ? SqlHelperCons.WHERE : SqlHelperCons.HAVING);
            if (versionField != null && versionEffectiveness) {
                conditionSql.append(versionConditionSql);
            }
            conditionSql.append(SqlHelperCons.BEGIN_BRACKET);
            if (args != null && args.length > 0) {
                conditionSql.append(SqlBeanUtil.getCondition(common, where, args));
            } else if (where.indexOf("${") > -1 && bean != null) {
                conditionSql.append(SqlBeanUtil.getCondition(common, where, bean));
            } else {
                conditionSql.append(where);
            }
            conditionSql.append(SqlHelperCons.END_BRACKET);
        } else {
            if (whereMap.size() > 0) {
                if (versionField != null && versionEffectiveness) {
                    conditionSql.append(versionConditionSql);
                }
                conditionSql.append(SqlHelperCons.BEGIN_BRACKET);
                int i = 0;
                // 遍历所有条件
                Collection<Map.Entry<String, SqlCondition>> sqlConditionEntryCollection = whereMap.entries();
                for (Map.Entry<String, SqlCondition> sqlConditionEntry : sqlConditionEntryCollection) {
                    SqlCondition sqlCondition = sqlConditionEntry.getValue();
                    // 遍历sql逻辑处理
                    if (i != 0 && i < sqlConditionEntryCollection.size()) {
                        conditionSql.append(getLogic(sqlCondition.getSqlLogic()));
                    }
                    if (SqlBeanUtil.isToUpperCase(common)) {
                        sqlCondition.setName(sqlCondition.getName().toUpperCase());
                    }
                    conditionSql.append(valueOperator(common, sqlCondition));
                    i++;
                }
                conditionSql.append(SqlHelperCons.END_BRACKET);
            }
            if (conditionSql.length() != 0) {
                conditionSql.insert(0, ConditionType.WHERE == conditionType ? SqlHelperCons.WHERE : SqlHelperCons.HAVING);
            }
        }
        return conditionSql.toString();
    }

    /**
     * 获取操作符
     *
     * @param sqlCondition
     * @return
     */
    private static String getOperator(SqlCondition sqlCondition) {
        String operator = "";
        // 优先使用枚举类型的操作符
        if (sqlCondition.getSqlOperator() != null) {
            SqlOperator sqlOperator = sqlCondition.getSqlOperator();
            if (sqlOperator == SqlOperator.IS) {
                operator = SqlHelperCons.IS;
            } else if (sqlOperator == SqlOperator.IS_NOT) {
                operator = SqlHelperCons.IS_NOT;
            } else if (sqlOperator == SqlOperator.IN) {
                operator = SqlHelperCons.IN;
            } else if (sqlOperator == SqlOperator.NOT_IN) {
                operator = SqlHelperCons.NOT_IN;
            } else if (sqlOperator == SqlOperator.LIKE || sqlOperator == SqlOperator.LIKE_L || sqlOperator == SqlOperator.LIKE_R) {
                operator = SqlHelperCons.LIKE;
            } else if (sqlOperator == SqlOperator.NOT_LIKE || sqlOperator == SqlOperator.NOT_LIKE_L || sqlOperator == SqlOperator.NOT_LIKE_R) {
                operator = SqlHelperCons.NOT_LIKE;
            } else if (sqlOperator == SqlOperator.BETWEEN) {
                operator = SqlHelperCons.BETWEEN;
            } else if (sqlOperator == SqlOperator.GREATER_THAN) {
                operator = SqlHelperCons.GREATER_THAN;
            } else if (sqlOperator == SqlOperator.GREAT_THAN_OR_EQUAL_TO) {
                operator = SqlHelperCons.GREAT_THAN_OR_EQUAL_TO;
            } else if (sqlOperator == SqlOperator.LESS_THAN) {
                operator = SqlHelperCons.LESS_THAN;
            } else if (sqlOperator == SqlOperator.LESS_THAN_OR_EQUAL_TO) {
                operator = SqlHelperCons.LESS_THAN_OR_EQUAL_TO;
            } else if (sqlOperator == SqlOperator.EQUAL_TO) {
                operator = SqlHelperCons.EQUAL_TO;
            } else if (sqlOperator == SqlOperator.NOT_EQUAL_TO) {
                operator = SqlHelperCons.NOT_EQUAL_TO;
            }
        } else {
            operator = SqlHelperCons.EQUAL_TO;
        }
        return operator;
    }

    /**
     * 获取逻辑
     *
     * @param sqlLogic
     * @return
     */
    private static String getLogic(SqlLogic sqlLogic) {
        String logic = null;
        if (sqlLogic != null && !"".equals(sqlLogic)) {
            switch (sqlLogic) {
                case AND:
                    logic = SqlHelperCons.AND;
                    break;
                case OR:
                    logic = SqlHelperCons.OR;
                    break;
                case ORBracket:
                    logic = SqlHelperCons.OR_BRACKET;
                    break;
                case ANDBracket:
                    logic = SqlHelperCons.AND_BRACKET;
                    break;
            }
        } else {
            logic = SqlHelperCons.AND;
        }
        return logic;
    }

    /**
     * 值操作
     *
     * @param common
     * @param sqlCondition
     * @return
     */
    private static StringBuffer valueOperator(Common common, SqlCondition sqlCondition) {
        StringBuffer sql = new StringBuffer();
        String operator = getOperator(sqlCondition);
        String transferred = SqlBeanUtil.getTransferred(common);
        boolean needEndBracket = false;
        Object[] betweenValues = null;
        Object value = sqlCondition.getValue();
        // 如果操作符为BETWEEN ，IN、NOT IN 则需额外处理
        if (sqlCondition.getSqlOperator() == SqlOperator.BETWEEN) {
            betweenValues = getObjects(value);
            if (betweenValues == null) {
                try {
                    throw new SqlBeanException("between 条件的值必须为Array或ArrayList");
                } catch (SqlBeanException e) {
                    e.printStackTrace();
                    logger.error(e.getMessage(), e);
                    return null;
                }
            }
        } else if (sqlCondition.getSqlOperator() == SqlOperator.IN || sqlCondition.getSqlOperator() == SqlOperator.NOT_IN) {
            needEndBracket = true;
            Object[] in_notInValues = getObjects(value);
            if (in_notInValues == null) {
                in_notInValues = new Object[]{value};
            }
            StringBuffer in_notIn = new StringBuffer();
            if (in_notInValues != null && in_notInValues.length > 0) {
                for (int k = 0; k < in_notInValues.length; k++) {
                    if (in_notInValues[k] instanceof Original) {
                        Original original = (Original) in_notInValues[k];
                        in_notIn.append(original.getValue());
                    } else {
                        in_notIn.append(SqlBeanUtil.getSqlValue(common, in_notInValues[k]));
                    }
                    in_notIn.append(SqlHelperCons.COMMA);
                }
                in_notIn.deleteCharAt(in_notIn.length() - SqlHelperCons.COMMA.length());
                value = in_notIn.toString();
            }
        } else {
            value = sqlCondition.getValue();
            //对like操作符处理
            if (operator.indexOf(SqlHelperCons.LIKE) > -1) {
                if (sqlCondition.getSqlOperator() == SqlOperator.LIKE || sqlCondition.getSqlOperator() == SqlOperator.LIKE_L || sqlCondition.getSqlOperator() == SqlOperator.NOT_LIKE || sqlCondition.getSqlOperator() == SqlOperator.NOT_LIKE_L) {
                    value = SqlHelperCons.PERCENT + value;
                }
                if (sqlCondition.getSqlOperator() == SqlOperator.LIKE || sqlCondition.getSqlOperator() == SqlOperator.LIKE_R || sqlCondition.getSqlOperator() == SqlOperator.NOT_LIKE || sqlCondition.getSqlOperator() == SqlOperator.NOT_LIKE_R) {
                    value = value + SqlHelperCons.PERCENT;
                }
                value = SqlHelperCons.SINGLE_QUOTATION_MARK + value + SqlHelperCons.SINGLE_QUOTATION_MARK;
            } else if (value instanceof Original) {
                Original original = (Original) sqlCondition.getValue();
                value = original.getValue();
            } else {
                value = SqlBeanUtil.getSqlValue(common, value);
            }
        }
        //schema
        if (StringUtil.isNotEmpty(sqlCondition.getSchema())) {
            sql.append(sqlCondition.getSchema());
            sql.append(SqlHelperCons.POINT);
        }
        //表别名
        if (StringUtil.isNotEmpty(sqlCondition.getTableAlias())) {
            sql.append(transferred);
            sql.append(sqlCondition.getTableAlias());
            sql.append(transferred);
            sql.append(SqlHelperCons.POINT);
        }
        //字段名
        sql.append(sqlCondition.getName());
        //操作符
        sql.append(operator);
        //值
        if (sqlCondition.getSqlOperator() == SqlOperator.BETWEEN) {
            sql.append(SqlBeanUtil.getSqlValue(common, betweenValues[0]));
            sql.append(SqlHelperCons.AND);
            sql.append(SqlBeanUtil.getSqlValue(common, betweenValues[1]));
        } else {
            sql.append(value);
        }
        // in与not in 额外加结束括号
        if (needEndBracket) {
            sql.append(SqlHelperCons.END_BRACKET);
        }
        return sql;
    }

    /**
     * 根据一个Object类型获得一个Object类型的数据
     *
     * @param value
     * @return
     */
    private static Object[] getObjects(Object value) {
        if (value == null) {
            return null;
        }
        Object[] objects = null;
        if (value.getClass().isArray()) {
            objects = (Object[]) value;
        } else if (value instanceof Collection) {
            Collection<Object> list = ((Collection<Object>) value);
            objects = list.toArray();
        }
        return objects;
    }

    /**
     * 返回MySQL,MariaDB,H2 分页语句
     *
     * @param select
     * @return
     */
    private static void mysqlPageDispose(Select select, StringBuffer sqlSb) {
        if (SqlBeanUtil.isUsePage(select)) {
            Integer[] param = pageParam(select);
            sqlSb.append(SqlHelperCons.LIMIT);
            sqlSb.append(param[0]);
            sqlSb.append(SqlHelperCons.COMMA);
            sqlSb.append(param[1]);
        }
    }

    /**
     * 返回PostgreSql,Sqlite,Hsql 分页语句
     *
     * @param select
     * @return
     */
    private static void postgreSqlPageDispose(Select select, StringBuffer sqlSb) {
        if (SqlBeanUtil.isUsePage(select)) {
            Integer[] param = pageParam(select);
            sqlSb.append(SqlHelperCons.LIMIT);
            sqlSb.append(param[1]);
            sqlSb.append(SqlHelperCons.OFFSET);
            sqlSb.append(param[0]);
        }
    }

    /**
     * Oracle 分页处理
     *
     * @param select
     * @param sqlSb
     */
    private static void oraclePageDispose(Select select, StringBuffer sqlSb) {
        //oracle 分页语句前缀
        if (SqlBeanUtil.isUsePage(select)) {
            Integer[] param = pageParam(select);
            StringBuffer beginSqlSb = new StringBuffer();
            beginSqlSb.append(SqlHelperCons.SELECT + SqlHelperCons.ALL + SqlHelperCons.FROM + SqlHelperCons.BEGIN_BRACKET);
            beginSqlSb.append(SqlHelperCons.SELECT + SqlHelperCons.TB + SqlHelperCons.POINT + SqlHelperCons.ALL + SqlHelperCons.COMMA + SqlHelperCons.ROWNUM + SqlHelperCons.RN + SqlHelperCons.FROM + SqlHelperCons.BEGIN_BRACKET);
            sqlSb.insert(0, beginSqlSb);
            StringBuffer endSb = new StringBuffer();
            endSb.append(SqlHelperCons.END_BRACKET + SqlHelperCons.TB + SqlHelperCons.WHERE + SqlHelperCons.ROWNUM + SqlHelperCons.LESS_THAN_OR_EQUAL_TO);
            endSb.append(param[1]);
            endSb.append(SqlHelperCons.END_BRACKET + SqlHelperCons.WHERE + SqlHelperCons.RN + SqlHelperCons.GREATER_THAN);
            endSb.append(param[0]);
            sqlSb.append(endSb);
        }
    }

    /**
     * DB2 分页处理
     *
     * @param select
     * @param sqlSb
     */
    private static void db2PageDispose(Select select, StringBuffer sqlSb) {
        //db2 分页语句前缀
        if (SqlBeanUtil.isUsePage(select)) {
            Integer[] param = pageParam(select);
            StringBuffer beginSqlSb = new StringBuffer();
            beginSqlSb.append(SqlHelperCons.SELECT + SqlHelperCons.ALL + SqlHelperCons.FROM + SqlHelperCons.BEGIN_BRACKET);
            beginSqlSb.append(SqlHelperCons.SELECT + SqlHelperCons.T + SqlHelperCons.POINT + SqlHelperCons.ALL + SqlHelperCons.COMMA + SqlHelperCons.ROWNUMBER);
            beginSqlSb.append(SqlHelperCons.OVER + SqlHelperCons.BEGIN_BRACKET + SqlHelperCons.SPACES + SqlHelperCons.END_BRACKET + SqlHelperCons.AS + SqlHelperCons.RN + SqlHelperCons.FROM + SqlHelperCons.BEGIN_BRACKET);
            sqlSb.insert(0, beginSqlSb);
            StringBuffer endSb = new StringBuffer();
            endSb.append(SqlHelperCons.END_BRACKET + SqlHelperCons.T + SqlHelperCons.SPACES + SqlHelperCons.END_BRACKET + SqlHelperCons.TB + SqlHelperCons.WHERE + SqlHelperCons.TB + SqlHelperCons.POINT + SqlHelperCons.RN + SqlHelperCons.BETWEEN);
            endSb.append(param[0]);
            endSb.append(SqlHelperCons.AND);
            endSb.append(param[1]);
            sqlSb.append(endSb);
        }
    }

    /**
     * 返回Derby 分页语句
     *
     * @param select
     * @return
     */
    private static void derbyPageDispose(Select select, StringBuffer sqlSb) {
        if (SqlBeanUtil.isUsePage(select)) {
            Integer[] param = pageParam(select);
            sqlSb.append(SqlHelperCons.OFFSET);
            sqlSb.append(param[0]);
            sqlSb.append(" ROWS FETCH NEXT ");
            sqlSb.append(param[1]);
            sqlSb.append(" ROWS ONLY");
        }
    }

    /**
     * 各个数据库的分页参数
     *
     * @param select
     * @return
     */
    public static Integer[] pageParam(Select select) {
        Integer[] param;
        //SQLServer2008
        if (DbType.SQLServer2008 == select.getSqlBeanConfig().getDbType()) {
            int top = (select.getPage().getPagenum() + 1) * select.getPage().getPagesize();
            int begin = top - select.getPage().getPagesize();
            param = new Integer[]{top, begin};
        }
        //Oracle,DB2
        else if (DbType.Oracle == select.getSqlBeanConfig().getDbType() || DbType.DB2 == select.getSqlBeanConfig().getDbType()) {
            //startIndex = (当前页 * 每页显示的数量)，例如：(0 * 10)
            //endIndex = (当前页 * 每页显示的数量) + 每页显示的数量，例如：10 = (0 * 10) + 10
            //那么如果startIndex=0，endIndex=10，就是查询第0到10条数据
            int startIndex = select.getPage().getPagenum() * select.getPage().getPagesize();
            int endIndex = (select.getPage().getPagenum() * select.getPage().getPagesize()) + select.getPage().getPagesize();
            param = new Integer[]{startIndex, endIndex};
        }
        //Mysql,MariaDB,PostgreSQL,Sqlite,Hsql
        else {
            int limitOffset = select.getPage().getPagenum() * select.getPage().getPagesize();
            int limitAmount = select.getPage().getPagesize();
            param = new Integer[]{limitOffset, limitAmount};
        }
        return param;
    }


}
