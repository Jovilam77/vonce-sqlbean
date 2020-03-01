package cn.vonce.sql.helper;

import cn.vonce.common.utils.StringUtil;
import cn.vonce.sql.annotation.SqlBeanId;
import cn.vonce.sql.annotation.SqlBeanPojo;
import cn.vonce.sql.annotation.SqlBeanTable;
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
     * 生成select sql语句
     *
     * @param select
     * @return
     * @throws SqlBeanException
     * @author jovi
     * @date 2017年6月2日下午5:41:45
     */
    public static String buildSelectSql(Select select) {
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
     * @author Jovi
     * @date 2017年6月21日下午6:44:57
     */
    public static String buildUpdateSql(Update update) {
        StringBuffer sqlSb = new StringBuffer();
        try {
            sqlSb.append(SqlHelperCons.UPDATE);
            sqlSb.append(getTableName(update, update.getUpdateBean()));
            sqlSb.append(SqlHelperCons.SET);
            sqlSb.append(setSql(update));
            sqlSb.append(whereSql(update, update.getUpdateBean()));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return null;
        }
        return sqlSb.toString();
    }

    /**
     * 生成insert sql语句(目前仅限数据库字段与实体类bean字段一致使用)
     *
     * @param insert
     * @return
     * @author Jovi
     * @date 2017年6月28日下午5:01:35
     */
    @SuppressWarnings("unchecked")
    public static String buildInsertSql(Insert insert) {
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
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return null;
        }
        return sql;
    }

    /**
     * 生成delete sql语句
     *
     * @param delete
     * @return
     * @author Jovi
     * @date 2017年6月28日下午4:58:27
     */
    public static String buildDeleteSql(Delete delete) {
        StringBuffer sqlSb = new StringBuffer();
        sqlSb.append(SqlHelperCons.DELETE_FROM);
        sqlSb.append(SqlBeanUtil.isToUpperCase(delete) ? delete.getTable().getName().toUpperCase() : delete.getTable().getName());
        sqlSb.append(whereSql(delete, null));
        return sqlSb.toString();
    }

    /**
     * 返回表名,优先级 tableName第一，注解第二，类名第三
     *
     * @param common
     * @param bean
     * @return
     * @author Jovi
     * @date 2017年8月17日下午5:24:25
     */
    private static String getTableName(Common common, Object bean) {
        SqlBeanTable sqlBeanTable = bean.getClass().getAnnotation(SqlBeanTable.class);
        String tableName;
        if (common.getTable() == null || StringUtil.isEmpty(common.getTable().getName())) {
            tableName = sqlBeanTable.value();
        } else {
            tableName = common.getTable().getName();
        }
        if (StringUtil.isEmpty(tableName)) {
            tableName = bean.getClass().getSimpleName();
        }
        return SqlBeanUtil.isToUpperCase(common) ? tableName.toUpperCase() : tableName;
    }

    /**
     * 返回column语句
     *
     * @param select
     * @return
     * @author Jovi
     * @date 2017年8月22日上午9:46:05
     */
    private static String column(Select select) {
        StringBuffer columnSql = new StringBuffer();
        if (select.getColumnList() != null && select.getColumnList().size() != 0) {
            for (int i = 0; i < select.getColumnList().size(); i++) {
                if (StringUtil.isNotEmpty(select.getColumnList().get(i).getName()) && StringUtil.isNotEmpty(select.getColumnList().get(i).getAlias())) {
                    columnSql.append(SqlHelperCons.BEGIN_BRACKET);
                    columnSql.append(select.getColumnList().get(i).getName());
                    columnSql.append(SqlHelperCons.END_BRACKET);
                    columnSql.append(SqlHelperCons.AS);
                    columnSql.append(SqlBeanUtil.getTransferred(select));
                    columnSql.append(select.getColumnList().get(i).getAlias());
                    columnSql.append(SqlBeanUtil.getTransferred(select));
                } else {
                    columnSql.append(select.getColumnList().get(i).getName());
                }
                columnSql.append(SqlHelperCons.COMMA);
            }
            columnSql.deleteCharAt(columnSql.length() - SqlHelperCons.COMMA.length());
        }
        return columnSql.toString();
    }

    /**
     * 返回from的表名
     *
     * @param select
     * @return
     * @author Jovi
     * @date 2017年8月22日上午9:46:05
     */
    private static String tableName(Select select) {
        return SqlBeanUtil.isToUpperCase(select) ? select.getTable().getName().toUpperCase() : select.getTable().getName();
    }

    /**
     * 返回from的别名
     *
     * @param select
     * @return
     * @author Jovi
     * @date 2017年8月22日上午9:46:05
     */
    private static String tableAlias(Select select) {
        return SqlBeanUtil.isToUpperCase(select) ? select.getTable().getAlias().toUpperCase() : select.getTable().getAlias();
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
        fromSql.append(tableName(select));
        fromSql.append(SqlHelperCons.SPACES);
        fromSql.append(transferred + tableAlias(select) + transferred);
        return fromSql.toString();
    }

    /**
     * 返回innerJoin语句
     *
     * @param select
     * @return
     * @author Jovi
     * @date 2017年8月18日下午12:45:39
     */
    private static String joinSql(Select select) {
        StringBuffer joinSql = new StringBuffer();
        if (select != null && select.getJoin().size() != 0) {
            for (int i = 0; i < select.getJoin().size(); i++) {
                Join join = select.getJoin().get(i);
                joinSql.append(join.getTableName());
                joinSql.append(SqlHelperCons.SPACES);
                joinSql.append(join.getTableAlias());
                joinSql.append(SqlHelperCons.ON);
                joinSql.append(SqlBeanUtil.getTableFieldFullName(select, join.getTableAlias(), join.getTableKeyword()));
                joinSql.append(SqlHelperCons.EQUAL_TO);
                joinSql.append(SqlBeanUtil.getTableFieldFullName(select, select.getTable().getAlias(), join.getMainKeyword()));
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
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @author Jovi
     * @date 2017年8月17日下午6:28:57
     */
    private static String fieldAndValuesSql(Common common, Object[] objects) throws IllegalArgumentException, IllegalAccessException {
        String tableName = getTableName(common, objects[0]);
        StringBuffer fieldSql = new StringBuffer();
        StringBuffer valueSql = new StringBuffer();
        StringBuffer fieldAndValuesSql = new StringBuffer();
        List<String> valueSqlList = new ArrayList<>();
        String transferred = SqlBeanUtil.getTransferred(common);
        //获取sqlbean的全部字段
        Field[] fields;
        if (objects[0].getClass().getAnnotation(SqlBeanPojo.class) != null) {
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
                SqlBeanId sqlBeanId = field.getAnnotation(SqlBeanId.class);
                if (sqlBeanId != null) {
                    existId++;
                }
                if (existId > 1) {
                    throw new SqlBeanException("请正确的标识id字段，id字段只能标识一个，但我们在'" + field.getDeclaringClass().getName() + "'此实体类或其父类找到了不止一处");
                }
                //只有在循环第一遍的时候才会处理
                if (i == 0) {
                    String tableFieldName = SqlBeanUtil.getTableFieldName(field);
                    //如果此字段非id字段 或者 此字段为id字段但是不是自增的id则生成该字段的insert语句
                    if (sqlBeanId == null || (sqlBeanId != null && sqlBeanId.generateType() != GenerateType.AUTO)) {
                        fieldSql.append(transferred + (SqlBeanUtil.isToUpperCase(common) ? tableFieldName.toUpperCase() : tableFieldName) + transferred);
                        fieldSql.append(SqlHelperCons.COMMA);
                    }
                }
                //如果此字段为id且需要生成唯一id
                if (sqlBeanId != null && sqlBeanId.generateType() != GenerateType.AUTO && sqlBeanId.generateType() != GenerateType.NORMAL) {
                    Object value = field.get(objects[i]);
                    if (StringUtil.isEmpty(value)) {
                        value = common.getSqlBeanConfig().getUniqueIdProcessor().uniqueId(sqlBeanId.generateType());
                    }
                    valueSql.append(SqlBeanUtil.getSqlValue(common, value));
                    valueSql.append(SqlHelperCons.COMMA);
                } else if (sqlBeanId == null) {
                    field.setAccessible(true);
                    valueSql.append(SqlBeanUtil.getSqlValue(common, field.get(objects[i])));
                    //valuesSql.append(getSqlValue(ReflectAsmUtil.get(objects[i].getClass(), objects[i], fields[j].getName())));
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
     * @author Jovi
     * @date 2017年8月17日下午5:28:26
     */
    private static String setSql(Update update) throws IllegalAccessException {
        StringBuffer setSql = new StringBuffer();
        String transferred = SqlBeanUtil.getTransferred(update);
        String[] filterFields = update.getFilterFields();
        Object bean = update.getUpdateBean();
        Field[] fields;
        if (bean.getClass().getAnnotation(SqlBeanPojo.class) != null) {
            fields = bean.getClass().getSuperclass().getDeclaredFields();
        } else {
            fields = bean.getClass().getDeclaredFields();
        }
        List<Field> filterAfterList = new ArrayList<>();
        for (int i = 0; i < fields.length; i++) {
            if (Modifier.isStatic(fields[i].getModifiers())) {
                continue;
            }
            fields[i].setAccessible(true);
            String name = SqlBeanUtil.getTableFieldName(fields[i]);
            Object objectValue = fields[i].get(bean);
            //Object objectValue = ReflectAsmUtil.get(bean.getClass(), bean, fields[i].getName());
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
            filterAfterList.get(i).setAccessible(true);
            Object objectValue = filterAfterList.get(i).get(bean);
            String value;
            if (objectValue == null || objectValue.equals(SqlHelperCons.WELL_NUMBER + SqlHelperCons.NULL_VALUE)) {
                value = SqlHelperCons.EQUAL_TO + SqlHelperCons.NULL_VALUE;
            } else {
                value = SqlHelperCons.EQUAL_TO + SqlBeanUtil.getSqlValue(update, objectValue);
            }
            setSql.append(transferred + (SqlBeanUtil.isToUpperCase(update) ? name.toUpperCase() : name) + transferred + value);
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
     * @throws Exception
     * @author Jovi
     * @date 2017年8月17日下午4:29:30
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
     * @author Jovi
     * @date 2017年8月17日下午5:24:04
     */
    private static String groupBySql(Select select) {
        return groupByAndOrderBySql(SqlHelperCons.GROUP_BY, select);
    }

    /**
     * @param select
     * @return
     * @author Jovi
     * @date 2017年8月18日下午9:38:55
     */
    private static String havingSql(Select select) {
        return conditionHandle(ConditionType.HAVING, select, select.getHaving(), select.getHavingArgs(), null, select.getHavingMap());
    }

    /**
     * 返回orderBy语句
     *
     * @param select
     * @return
     * @author Jovi
     * @date 2017年8月17日下午5:24:04
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
     * @author Jovi
     * @date 2019年9月30日下午11:21:20
     */
    private static String groupByAndOrderBySql(String type, Select select) {
        StringBuffer groupByAndOrderBySql = new StringBuffer();
        List<String> by = SqlHelperCons.ORDER_BY.equals(type) ? select.getOrderBy() : select.getGroupBy();
        String transferred = SqlBeanUtil.getTransferred(select);
        if (by != null && by.size() != 0) {
            groupByAndOrderBySql.append(type);
            for (int i = 0; i < by.size(); i++) {
                String orderBy = by.get(i);
                if (orderBy.indexOf(SqlHelperCons.WELL_NUMBER) > -1) {
                    orderBy = orderBy.substring(1);
                } else if (orderBy.indexOf(SqlHelperCons.POINT) == -1) {
                    orderBy = transferred + tableAlias(select) + transferred + SqlHelperCons.POINT + orderBy;
                }
                groupByAndOrderBySql.append(orderBy);
                groupByAndOrderBySql.append(SqlHelperCons.COMMA);
            }
            groupByAndOrderBySql.deleteCharAt(groupByAndOrderBySql.length() - SqlHelperCons.COMMA.length());
        } else {
            if (SqlHelperCons.ORDER_BY.equals(type) && select.getSqlBeanConfig().getDbType() == DbType.SQLServer2008 && SqlBeanUtil.isUsePage(select) && !SqlBeanUtil.isCount(select)) {
                groupByAndOrderBySql.append(type);
                groupByAndOrderBySql.append(SqlBeanUtil.getTableFieldFullName(select, tableAlias(select), select.getPage().getIdName()));
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
        StringBuffer conditioneSql = new StringBuffer();
        // 优先使用条件字符串拼接
        if (where != null && !"".equals(where)) {
            conditioneSql.append(ConditionType.WHERE == conditionType ? SqlHelperCons.WHERE : SqlHelperCons.HAVING);
            conditioneSql.append(SqlHelperCons.BEGIN_BRACKET);
            if (args != null && args.length > 0) {
                conditioneSql.append(SqlBeanUtil.getCondition(common, where, args));
            } else if (where.indexOf("${") > -1 && bean != null) {
                try {
                    conditioneSql.append(SqlBeanUtil.getCondition(common, where, bean));
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                    logger.error(e.getMessage(), e);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    logger.error(e.getMessage(), e);
                }
            } else {
                conditioneSql.append(where);
            }
            conditioneSql.append(SqlHelperCons.END_BRACKET);
        } else {
            if (whereMap.size() > 0) {
                conditioneSql.append(SqlHelperCons.BEGIN_BRACKET);
                int i = 0;
                // 遍历所有条件
                Collection<Map.Entry<String, SqlCondition>> sqlConditionEntryCollection = whereMap.entries();
                for (Map.Entry<String, SqlCondition> sqlConditionEntry : sqlConditionEntryCollection) {
                    String key = sqlConditionEntry.getKey();
                    SqlCondition sqlCondition = sqlConditionEntry.getValue();
                    Object value;
                    String fieldName = sqlCondition.getField();
                    if (StringUtil.isNotEmpty(sqlCondition.getTableAlias())) {
                        fieldName = sqlCondition.getTableAlias() + SqlHelperCons.POINT + fieldName;
                    }
                    // 如果key使用#开头，实际值为传入值，不做任何处理
                    if (key.indexOf(SqlHelperCons.WELL_NUMBER) > -1) {
                        value = sqlCondition.getValue().toString();
                        fieldName = sqlCondition.getTableAlias() + SqlHelperCons.POINT + sqlCondition.getField().substring(1);
                    } else {
                        value = sqlCondition.getValue();
                        // 如果值不为数组则做处理
                        if (!value.getClass().isArray() && !(value instanceof Collection)) {
                            value = SqlBeanUtil.getSqlValue(common, value);
                        }
                    }
                    boolean needEndBracket = isNeedEndBracket(sqlCondition.getSqlOperator());
                    String operator = getOperator(sqlCondition);
                    // 遍历sql逻辑处理
                    if (i != 0 && i < sqlConditionEntryCollection.size()) {
                        conditioneSql.append(getLogic(sqlCondition.getSqlLogic()));
                    }
                    if (SqlBeanUtil.isToUpperCase(common)) {
                        fieldName = fieldName.toUpperCase();
                    }
                    conditioneSql.append(valueOperator(common, operator, fieldName, value, needEndBracket));
                    i++;
                }
                conditioneSql.append(SqlHelperCons.END_BRACKET);
            }
            if (conditioneSql.length() != 0) {
                conditioneSql.insert(0, ConditionType.WHERE == conditionType ? SqlHelperCons.WHERE : SqlHelperCons.HAVING);
            }
        }
        return conditioneSql.toString();
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
            } /*else if (sqlOperator == MySqlOperator.EXISTS) {
                                operator = EXISTS;
                                needEndBracket = true;
                            } else if (sqlOperator == MySqlOperator.NOT_EXISTS) {
                                operator = NOT_EXISTS;
                                needEndBracket = true;
                            }*/ else if (sqlOperator == SqlOperator.LIKE) {
                operator = SqlHelperCons.LIKE;
            } else if (sqlOperator == SqlOperator.NOT_LIKE) {
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
     * 需要额外包裹括号
     *
     * @param sqlOperator
     * @return
     */
    private static boolean isNeedEndBracket(SqlOperator sqlOperator) {
        if (sqlOperator == SqlOperator.IN || sqlOperator == SqlOperator.NOT_IN) {
            return true;
        }
        return false;
    }

    /**
     * 值操作
     *
     * @param common
     * @param operator
     * @param fieldName
     * @param value
     * @param needEndBracket
     * @return
     */
    private static StringBuffer valueOperator(Common common, String operator, String fieldName, Object value, boolean needEndBracket) {
        StringBuffer sql = new StringBuffer();
        // 如果操作符为BETWEEN ，IN、NOT IN 则需额外处理
        if (operator.indexOf(SqlHelperCons.BETWEEN) > -1) {
            Object[] betweenValues;
            if (value.getClass().isArray()) {
                betweenValues = (Object[]) value;
            } else if (value instanceof Collection) {
                Collection<Object> list = ((Collection<Object>) value);
                betweenValues = list.toArray();
            } else {
                try {
                    throw new SqlBeanException("between 条件的值必须为Array或ArrayList");
                } catch (SqlBeanException e) {
                    e.printStackTrace();
                    logger.error(e.getMessage(), e);
                    return null;
                }
            }
            sql.append(fieldName + operator + SqlBeanUtil.getSqlValue(common, betweenValues[0]) + SqlHelperCons.AND + SqlBeanUtil.getSqlValue(common, betweenValues[1]));
        } else if (operator.indexOf(SqlHelperCons.IN) > -1) {
            Object[] in_notInValues = getObjects(value);
            StringBuffer in_notIn = new StringBuffer();
            if (in_notInValues != null && in_notInValues.length > 0) {
                for (int k = 0; k < in_notInValues.length; k++) {
                    in_notIn.append(SqlBeanUtil.getSqlValue(common, in_notInValues[k]));
                    in_notIn.append(SqlHelperCons.COMMA);
                }
                in_notIn.deleteCharAt(in_notIn.length() - SqlHelperCons.COMMA.length());
                sql.append(fieldName + operator + in_notIn.toString());
            }
        } else {
            sql.append(fieldName + operator + value);
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
        Object[] objects;
        if (value.getClass().isArray()) {
            objects = (Object[]) value;
        } else if (value instanceof Collection) {
            Collection<Object> list = ((Collection<Object>) value);
            objects = list.toArray();
        } else {
            objects = new Object[]{value};
        }
        return objects;
    }

    /**
     * 返回MySQL,MariaDB,H2 分页语句
     *
     * @param select
     * @return
     * @author Jovi
     * @date 2017年8月17日下午5:23:46
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
     * @author Jovi
     * @date 2019年4月1日下午6:18:20
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
     * @author Jovi
     * @date 2019年8月22日下午20:14:10
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
     * @author Jovi
     * @date 2020年2月21日下午5:40:20
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
