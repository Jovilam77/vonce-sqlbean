package cn.vonce.sql.helper;

import cn.vonce.sql.annotation.SqlBeanTable;
import cn.vonce.sql.bean.*;
import cn.vonce.sql.config.SqlBeanConfig;
import cn.vonce.sql.constant.SqlHelperCons;
import cn.vonce.sql.enumerate.ConditionType;
import cn.vonce.sql.enumerate.DbType;
import cn.vonce.sql.enumerate.SqlOperator;
import cn.vonce.sql.exception.SqlBeanException;
import cn.vonce.sql.uitls.SqlBeanUtil;
import com.google.common.collect.ListMultimap;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

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

    private static SqlBeanConfig sqlBeanConfig;

    public static void init(SqlBeanConfig sqlBeanConfig) {
        if (SqlHelper.sqlBeanConfig == null) {
            isNull(sqlBeanConfig, "参数sqlBeanConfig为null");
            isNull(sqlBeanConfig.getDbType(), "请设置sqlBeanConfig > dbType(数据库类型)");
            if (sqlBeanConfig.getDbType() == DbType.Oracle) {
                isNull(sqlBeanConfig.getOracleToUpperCase(), "请设置sqlBeanConfig > oracleToUpperCase(sql语句是否转大写)");
            }
            SqlHelper.sqlBeanConfig = sqlBeanConfig;
        }
    }

    public static SqlBeanConfig getSqlBeanConfig() {
        return sqlBeanConfig;
    }

    /**
     * 检查SqlHelper初始化状态
     */
    public static void checkInitStatus() {
        isNull(sqlBeanConfig, "请初始化SqlHelper");
    }

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
        checkInitStatus();
        StringBuilder sqlSb = new StringBuilder();
        Integer[] pageParam = null;
        String orderSql = orderBySql(select);
        //SQLServer2008 分页处理
        if (sqlBeanConfig.getDbType() == DbType.SQLServer2008) {
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
        if (sqlBeanConfig.getDbType() == DbType.SQLServer2008) {
            if (SqlBeanUtil.isUsePage(select)) {
                sqlSb.append(SqlHelperCons.TOP);
                sqlSb.append(pageParam[0]);
                sqlSb.append(SqlHelperCons.ROW_NUMBER + SqlHelperCons.OVER + SqlHelperCons.BEGIN_BRACKET + orderSql + SqlHelperCons.END_BRACKET + SqlHelperCons.ROWNUM + SqlHelperCons.COMMA);
            }
        }
        //标准Sql
        sqlSb.append(select(select));
        sqlSb.append(SqlHelperCons.FROM);
        sqlSb.append(from(select));
        sqlSb.append(innerJoinSql(select));
        sqlSb.append(fullJoinSql(select));
        sqlSb.append(leftJoinSql(select));
        sqlSb.append(rightJoinSql(select));
        sqlSb.append(whereSql(select));
        String groupBySql = groupBySql(select);
        sqlSb.append(groupBySql);
        sqlSb.append(havingSql(select));
        if (!SqlBeanUtil.isCount(select)) {
            sqlSb.append(orderSql);
        }
        //SQLServer2008 分页处理
        if (sqlBeanConfig.getDbType() == DbType.SQLServer2008) {
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
        if (SqlBeanUtil.isCount(select) && StringUtils.isNotEmpty(groupBySql)) {
            sqlSb.insert(0, SqlHelperCons.SELECT + SqlHelperCons.COUNT + SqlHelperCons.BEGIN_BRACKET + SqlHelperCons.ALL + SqlHelperCons.END_BRACKET + SqlHelperCons.FROM + SqlHelperCons.BEGIN_BRACKET);
            sqlSb.append(SqlHelperCons.END_BRACKET + SqlHelperCons.AS + SqlHelperCons.T);
        }
        //MySQL,MariaDB 分页处理
        if (sqlBeanConfig.getDbType() == DbType.MySQL || sqlBeanConfig.getDbType() == DbType.MariaDB) {
            if (SqlBeanUtil.isUsePage(select)) {
                sqlSb.append(mysqlPageSql(select));
            }
        }
        //PostgreSQL 分页处理
        else if (sqlBeanConfig.getDbType() == DbType.PostgreSQL) {
            if (SqlBeanUtil.isUsePage(select)) {
                sqlSb.append(postgreSqlPageSql(select));
            }
        }
        //Oracle 分页处理
        else if (sqlBeanConfig.getDbType() == DbType.Oracle) {
            oraclePageDispose(select, sqlSb);
        }
        //DB2 分页处理
        else if (sqlBeanConfig.getDbType() == DbType.DB2) {
            db2PageDispose(select, sqlSb);
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
        checkInitStatus();
        StringBuilder sqlSb = new StringBuilder();
        try {
            sqlSb.append(SqlHelperCons.UPDATE);
            sqlSb.append(updateTableName(update));
            sqlSb.append(SqlHelperCons.SET);
            sqlSb.append(setSql(update));
            sqlSb.append(whereSql(update));
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
        checkInitStatus();
        Object[] objects;
        String tableName;
        if (insert.getInsertBean().getClass().isArray()) {
            objects = (Object[]) insert.getInsertBean();
        } else if (insert.getInsertBean() instanceof List) {
            List<Object> list = (List<Object>) insert.getInsertBean();
            objects = list.toArray();
        } else {
            objects = new Object[]{insert.getInsertBean()};
        }
        SqlBeanTable sqlBeanTable = objects[0].getClass().getAnnotation(SqlBeanTable.class);
        // 优先级 注解第一，getUpdateTable第二，第三为类名
        if (sqlBeanTable != null) {
            tableName = sqlBeanTable.value();
        } else if (insert.getInsertTable() != null && !"".equals(insert.getInsertTable())) {
            tableName = insert.getInsertTable();
        } else {
            tableName = objects[0].getClass().getSimpleName();
        }
        String sql = null;
        try {
            sql = fieldAndValuesSql(tableName, objects);
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
        checkInitStatus();
        StringBuilder sqlSb = new StringBuilder();
        sqlSb.append(SqlHelperCons.DELETE_FROM);
        sqlSb.append(SqlBeanUtil.isOracleToUpperCase() ? delete.getDeleteBable().toUpperCase() : delete.getDeleteBable());
        sqlSb.append(whereSql(delete));
        return sqlSb.toString();
    }

    /**
     * 返回表名
     *
     * @param update
     * @return
     * @author Jovi
     * @date 2017年8月17日下午5:24:25
     */
    private static String updateTableName(Update update) {
        String tableName;
        Object bean = update.getUpdateBean();
        SqlBeanTable sqlBeanTable = bean.getClass().getAnnotation(SqlBeanTable.class);
        // 优先级 注解第一，getUpdateTable第二，第三为类名
        if (sqlBeanTable != null) {
            tableName = sqlBeanTable.value();
        } else if (update.getUpdateTable() != null && !"".equals(update.getUpdateTable())) {
            tableName = update.getUpdateTable();
        } else {
            tableName = bean.getClass().getSimpleName();
        }
        return SqlBeanUtil.isOracleToUpperCase() ? tableName.toUpperCase() : tableName;
    }

    /**
     * 返回select语句
     *
     * @param select
     * @return
     * @author Jovi
     * @date 2017年8月22日上午9:46:05
     */
    private static String select(Select select) {
        StringBuilder selectSql = new StringBuilder();
        if (select.getColumn() != null && select.getColumn().size() != 0) {
            for (int i = 0; i < select.getColumn().size(); i++) {
                selectSql.append(select.getColumn().get(i));
                selectSql.append(SqlHelperCons.COMMA);
            }
            selectSql.deleteCharAt(selectSql.length() - SqlHelperCons.COMMA.length());
        }
        return selectSql.toString();
    }

    /**
     * 返回from的表名
     *
     * @param select
     * @return
     * @author Jovi
     * @date 2017年8月22日上午9:46:05
     */
    private static String from(Select select) {
        StringBuilder fromSql = new StringBuilder();
        if (select.getFrom() != null && select.getFrom().length == 1) {
            String tableName = select.getFrom()[0];
            return SqlBeanUtil.isOracleToUpperCase() ? tableName.toUpperCase() : tableName;
        } else if (select.getFrom() != null && select.getFrom().length > 1) {
            for (int i = 0; i < select.getFrom().length; i++) {
                String tableName = select.getFrom()[i];
                fromSql.append(SqlBeanUtil.isOracleToUpperCase() ? tableName.toUpperCase() : tableName);
                fromSql.append(SqlHelperCons.COMMA);
            }
            fromSql.deleteCharAt(fromSql.length() - SqlHelperCons.COMMA.length());
        }
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
    private static String innerJoinSql(Select select) {
        StringBuilder innerJoinSql = new StringBuilder();
        if (select.getInnerJoin() != null && select.getInnerJoin().size() != 0) {
            for (int i = 0; i < select.getInnerJoin().size(); i++) {
                innerJoinSql.append(select.getInnerJoin().get(i));
                if (i < select.getInnerJoin().size() - 1) {
                    innerJoinSql.append(SqlHelperCons.SPACES);
                }
            }
        }
        return innerJoinSql.toString();
    }

    /**
     * 返回outerJoin语句
     *
     * @param select
     * @return
     * @author Jovi
     * @date 2017年8月18日下午12:45:39
     */
    private static String fullJoinSql(Select select) {
        StringBuilder outerJoinSql = new StringBuilder();
        if (select.getFullJoin() != null && select.getFullJoin().size() != 0) {
            for (int i = 0; i < select.getFullJoin().size(); i++) {
                outerJoinSql.append(select.getFullJoin().get(i));
                if (i < select.getFullJoin().size() - 1) {
                    outerJoinSql.append(SqlHelperCons.SPACES);
                }
            }
        }
        return outerJoinSql.toString();
    }

    /**
     * 返回leftOuterJoin语句
     *
     * @param select
     * @return
     * @author Jovi
     * @date 2017年8月18日下午12:45:39
     */
    private static String leftJoinSql(Select select) {
        StringBuilder leftJoinSql = new StringBuilder();
        if (select.getLeftOuterJoin() != null && select.getLeftOuterJoin().size() != 0) {
            for (int i = 0; i < select.getLeftOuterJoin().size(); i++) {
                leftJoinSql.append(select.getLeftOuterJoin().get(i));
                if (i < select.getLeftOuterJoin().size() - 1) {
                    leftJoinSql.append(SqlHelperCons.SPACES);
                }
            }
        }
        return leftJoinSql.toString();
    }

    /**
     * 返回rightOuterJoin语句
     *
     * @param select
     * @return
     * @author Jovi
     * @date 2017年8月18日下午12:45:39
     */
    private static String rightJoinSql(Select select) {
        StringBuilder rightJoinSql = new StringBuilder();
        if (select.getRightOuterJoin() != null && select.getRightOuterJoin().size() != 0) {
            for (int i = 0; i < select.getRightOuterJoin().size(); i++) {
                rightJoinSql.append(select.getRightOuterJoin().get(i));
                if (i < select.getRightOuterJoin().size() - 1) {
                    rightJoinSql.append(SqlHelperCons.SPACES);
                }
            }
        }
        return rightJoinSql.toString();
    }

    /**
     * 返回field及values语句
     *
     * @param tableName
     * @param objects
     * @return
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @author Jovi
     * @date 2017年8月17日下午6:28:57
     */
    private static String fieldAndValuesSql(String tableName, Object[] objects) throws IllegalArgumentException, IllegalAccessException {
        StringBuilder fieldSql = new StringBuilder();
        StringBuilder valueSql = new StringBuilder();
        StringBuilder fieldAndValuesSql = new StringBuilder();
        List<String> valueSqlList = new ArrayList<>();
        String transferred = SqlBeanUtil.getTransferred();
        if (sqlBeanConfig.getDbType() == DbType.Oracle) {
            if (sqlBeanConfig.getOracleToUpperCase()) {
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
            valueSql.delete(0, valueSql.length());
            Field[] fields = objects[i].getClass().getDeclaredFields();
            //只有在循环第一遍的时候才会处理
            if (i == 0) {
                fieldSql.append(SqlHelperCons.BEGIN_BRACKET);
            }
            valueSql.append(SqlHelperCons.BEGIN_BRACKET);
            for (int j = 0; j < fields.length; j++) {
                if (Modifier.isStatic(fields[j].getModifiers())) {
                    continue;
                }
                String name = SqlBeanUtil.getFieldName(fields[j]);
                if (SqlBeanUtil.isIgnore(fields[j])) {
                    continue;
                }
                //只有在循环第一遍的时候才会处理
                if (i == 0) {
                    fieldSql.append(transferred + (SqlBeanUtil.isOracleToUpperCase() ? name.toUpperCase() : name) + transferred);
                    fieldSql.append(SqlHelperCons.COMMA);
                }
                fields[j].setAccessible(true);
                valueSql.append(SqlBeanUtil.getSqlValue(fields[j].get(objects[i])));
                //valuesSql.append(getSqlValue(ReflectAsmUtil.get(objects[i].getClass(), objects[i], fields[j].getName())));
                valueSql.append(SqlHelperCons.COMMA);
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
        if (sqlBeanConfig.getDbType() == DbType.Oracle) {
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
        StringBuilder setSql = new StringBuilder();
        String transferred = SqlBeanUtil.getTransferred();
        String[] filterFields = update.getFilterFields();
        Object bean = update.getUpdateBean();
        Field[] fields = bean.getClass().getDeclaredFields();
        List<Field> filterAfterList = new ArrayList<>();
        for (int i = 0; i < fields.length; i++) {
            if (Modifier.isStatic(fields[i].getModifiers())) {
                continue;
            }
            fields[i].setAccessible(true);
            String name = SqlBeanUtil.getFieldName(fields[i]);
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
                filterAfterList.add(fields[i]);
            }
        }
        for (int i = 0; i < filterAfterList.size(); i++) {
            String name = SqlBeanUtil.getFieldName(filterAfterList.get(i));
            filterAfterList.get(i).setAccessible(true);
            Object objectValue = filterAfterList.get(i).get(bean);
            String value;
            if (objectValue == null || objectValue.equals(SqlHelperCons.WELL_NUMBER + SqlHelperCons.NULL_VALUE)) {
                value = SqlHelperCons.EQUAL_TO + SqlHelperCons.NULL_VALUE;
            } else {
                value = SqlHelperCons.EQUAL_TO + SqlBeanUtil.getSqlValue(objectValue);
            }
            setSql.append(transferred + (SqlBeanUtil.isOracleToUpperCase() ? name.toUpperCase() : name) + transferred + value);
            setSql.append(SqlHelperCons.COMMA);
        }
        setSql.deleteCharAt(setSql.length() - SqlHelperCons.COMMA.length());
        return setSql.toString();
    }

    /**
     * 返回where语句
     *
     * @param common
     * @return
     * @throws Exception
     * @author Jovi
     * @date 2017年8月17日下午4:29:30
     */
    @SuppressWarnings("unchecked")
    private static String whereSql(Common common) {
        return conditionHandle(ConditionType.WHERE, common.getWhere(), common.getWhereMap());
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
        StringBuilder groupBySql = new StringBuilder();
        String transferred = SqlBeanUtil.getTransferred();
        if (select.getGroupBy() != null && select.getGroupBy().size() != 0) {
            groupBySql.append(SqlHelperCons.GROUP_BY);
            for (int i = 0; i < select.getGroupBy().size(); i++) {
                String groupBy = select.getGroupBy().get(i);
                if (groupBy.indexOf(SqlHelperCons.POINT) == -1) {
                    groupBy = transferred + select.getFrom()[0] + transferred + SqlHelperCons.POINT + groupBy;
                }
                groupBySql.append(groupBy);
                groupBySql.append(SqlHelperCons.COMMA);
            }
            groupBySql.deleteCharAt(groupBySql.length() - SqlHelperCons.COMMA.length());
        }
        return groupBySql.toString();
    }

    /**
     * @param select
     * @return
     * @author Jovi
     * @date 2017年8月18日下午9:38:55
     */
    private static String havingSql(Select select) {
        return conditionHandle(ConditionType.HAVING, select.getHaving(), select.getHavingMap());
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
        StringBuilder orderBySql = new StringBuilder();
        if (select.getOrderBy() != null && select.getOrderBy().size() != 0) {
            orderBySql.append(SqlHelperCons.ORDER_BY);
            for (int i = 0; i < select.getOrderBy().size(); i++) {
                String orderBy = select.getOrderBy().get(i);
                if (orderBy.indexOf(SqlHelperCons.POINT) == -1) {
                    orderBy = select.getFrom()[0] + SqlHelperCons.POINT + orderBy;
                }
                orderBySql.append(orderBy);
                orderBySql.append(SqlHelperCons.COMMA);
            }
            orderBySql.deleteCharAt(orderBySql.length() - SqlHelperCons.COMMA.length());
        } else {
            if (sqlBeanConfig.getDbType() == DbType.SQLServer2008 && !SqlBeanUtil.isCount(select)) {
                orderBySql.append(SqlHelperCons.ORDER_BY);
                orderBySql.append(SqlBeanUtil.getFieldFullName(select.getFrom()[0], select.getPage().getIdName()));
            }
        }
        return orderBySql.toString();
    }

    /**
     * 条件处理
     *
     * @param conditionType
     * @param where
     * @param whereMap
     * @return
     */
    private static String conditionHandle(ConditionType conditionType, String where, ListMultimap<String, SqlCondition> whereMap) {
        StringBuilder conditioneSql = new StringBuilder();
        // 优先使用条件字符串拼接
        if (where != null && !"".equals(where)) {
            conditioneSql.append(ConditionType.WHERE == conditionType ? SqlHelperCons.WHERE : SqlHelperCons.HAVING);
            conditioneSql.append(SqlHelperCons.BEGIN_BRACKET);
            conditioneSql.append(where);
            conditioneSql.append(SqlHelperCons.END_BRACKET);
        } else {
            if (whereMap.size() > 0) {
                conditioneSql.append(SqlHelperCons.BEGIN_BRACKET);
                int i = 0;
                // 遍历所有条件
                for (String key : whereMap.keySet()) {
                    int j = 0;
                    List<SqlCondition> sqlConditionSet = whereMap.get(key);
                    for (SqlCondition sqlCondition : sqlConditionSet) {
                        Object value;
                        // 如果key使用#开头，实际值为传入值，不做任何处理
                        if (key.indexOf(SqlHelperCons.WELL_NUMBER) > -1) {
                            value = sqlCondition.getValue().toString();
                            sqlCondition.setField(sqlCondition.getField().substring(1));
                        } else {
                            value = sqlCondition.getValue();
                            // 如果值不为数组则做处理
                            if (!value.getClass().isArray() && !(value instanceof List)) {
                                value = SqlBeanUtil.getSqlValue(value);
                            }
                        }
                        boolean needEndBracket = isNeedEndBracket(sqlCondition);
                        String operator = getOperator(sqlCondition);
                        // 遍历sql逻辑处理
                        if (((i == 0 && j != 0) || i != 0) && j < sqlConditionSet.size()) {
                            conditioneSql.append(getLogic(sqlCondition));
                        }
                        String fieldName = sqlCondition.getField();
                        if (SqlBeanUtil.isOracleToUpperCase()) {
                            fieldName = fieldName.toUpperCase();
                        }
                        conditioneSql.append(valueOperator(operator, fieldName, value, needEndBracket));
                        j++;
                    }
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
            if (sqlOperator == SqlOperator.IN) {
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
        } else if (sqlCondition.getOperator() != null && !"".equals(sqlCondition.getOperator())) {
            operator = SqlHelperCons.SPACES + sqlCondition.getOperator() + SqlHelperCons.SPACES;
        } else {
            operator = SqlHelperCons.EQUAL_TO;
        }
        return operator;
    }

    /**
     * 获取逻辑
     *
     * @param sqlCondition
     * @return
     */
    private static String getLogic(SqlCondition sqlCondition) {
        String logic = null;
        if (sqlCondition.getSqlLogic() != null && !"".equals(sqlCondition.getSqlLogic())) {
            switch (sqlCondition.getSqlLogic()) {
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
     * @param sqlCondition
     * @return
     */
    private static boolean isNeedEndBracket(SqlCondition sqlCondition) {
        if (sqlCondition.getSqlOperator() == SqlOperator.IN || sqlCondition.getSqlOperator() == SqlOperator.NOT_IN) {
            return true;
        }
        return false;
    }

    /**
     * 值操作
     *
     * @param operator
     * @param fieldName
     * @param value
     * @param needEndBracket
     * @return
     */
    private static StringBuilder valueOperator(String operator, String fieldName, Object value, boolean needEndBracket) {
        StringBuilder sql = new StringBuilder();
        // 如果操作符为BETWEEN ，IN、NOT IN 则需额外处理
        if (operator.indexOf(SqlHelperCons.BETWEEN) > -1) {
            Object[] betweenValues;
            if (value.getClass().isArray()) {
                betweenValues = (Object[]) value;
            } else if (value instanceof List) {
                List<Object> list = ((List<Object>) value);
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
            sql.append(fieldName + operator + SqlBeanUtil.getSqlValue(betweenValues[0]) + SqlHelperCons.AND + SqlBeanUtil.getSqlValue(betweenValues[1]));
        } else if (operator.indexOf(SqlHelperCons.IN) > -1) {
            Object[] in_notInValues = getObjects(value);
            StringBuffer in_notIn = new StringBuffer();
            if (in_notInValues != null && in_notInValues.length > 0) {
                for (int k = 0; k < in_notInValues.length; k++) {
                    in_notIn.append(SqlBeanUtil.getSqlValue(in_notInValues[k]));
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
        } else if (value instanceof List) {
            List<Object> list = ((List<Object>) value);
            objects = list.toArray();
        } else {
            objects = new Object[]{value};
        }
        return objects;
    }

    /**
     * 返回MySQL 分页语句
     *
     * @param select
     * @return
     * @author Jovi
     * @date 2017年8月17日下午5:23:46
     */
    private static String mysqlPageSql(Select select) {
        StringBuilder limit = new StringBuilder();
        Integer[] param = pageParam(select);
        limit.append(SqlHelperCons.LIMIT);
        limit.append(param[0]);
        limit.append(SqlHelperCons.COMMA);
        limit.append(param[1]);
        return limit.toString();
    }

    /**
     * 返回PostgreSql 分页语句
     *
     * @param select
     * @return
     */
    private static String postgreSqlPageSql(Select select) {
        StringBuilder limit = new StringBuilder();
        Integer[] param = pageParam(select);
        limit.append(SqlHelperCons.LIMIT);
        limit.append(param[1]);
        limit.append(SqlHelperCons.OFFSET);
        limit.append(param[0]);
        return limit.toString();
    }

    /**
     * Oracle 分页处理
     *
     * @param select
     * @param sqlSb
     * @author Jovi
     * @date 2019年4月1日下午6:18:20
     */
    private static void oraclePageDispose(Select select, StringBuilder sqlSb) {
        //oracle 分页语句前缀
        if (SqlBeanUtil.isUsePage(select)) {
            Integer[] param = pageParam(select);
            StringBuilder beginSqlSb = new StringBuilder();
            beginSqlSb.append(SqlHelperCons.SELECT + SqlHelperCons.ALL + SqlHelperCons.FROM + SqlHelperCons.BEGIN_BRACKET);
            beginSqlSb.append(SqlHelperCons.SELECT + SqlHelperCons.TB + SqlHelperCons.POINT + SqlHelperCons.ALL + SqlHelperCons.COMMA + SqlHelperCons.ROWNUM + SqlHelperCons.RN + SqlHelperCons.FROM + SqlHelperCons.BEGIN_BRACKET);
            sqlSb.insert(0, beginSqlSb);
            StringBuilder endSb = new StringBuilder();
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
    private static void db2PageDispose(Select select, StringBuilder sqlSb) {
        //db2 分页语句前缀
        if (SqlBeanUtil.isUsePage(select)) {
            Integer[] param = pageParam(select);
            StringBuilder beginSqlSb = new StringBuilder();
            beginSqlSb.append(SqlHelperCons.SELECT + SqlHelperCons.ALL + SqlHelperCons.FROM + SqlHelperCons.BEGIN_BRACKET);
            beginSqlSb.append(SqlHelperCons.SELECT + SqlHelperCons.T + SqlHelperCons.POINT + SqlHelperCons.ALL + SqlHelperCons.COMMA + SqlHelperCons.ROW_NUMBER + SqlHelperCons.BEGIN_BRACKET + SqlHelperCons.SPACES + SqlHelperCons.END_BRACKET);
            beginSqlSb.append(SqlHelperCons.OVER + SqlHelperCons.BEGIN_BRACKET + SqlHelperCons.SPACES + SqlHelperCons.END_BRACKET + SqlHelperCons.AS + SqlHelperCons.RN + SqlHelperCons.FROM + SqlHelperCons.BEGIN_BRACKET);
            sqlSb.insert(0, beginSqlSb);
            StringBuilder endSb = new StringBuilder();
            endSb.append(SqlHelperCons.END_BRACKET + SqlHelperCons.T + SqlHelperCons.SPACES + SqlHelperCons.END_BRACKET + SqlHelperCons.TB + SqlHelperCons.WHERE + SqlHelperCons.TB + SqlHelperCons.POINT + SqlHelperCons.RN + SqlHelperCons.BETWEEN);
            endSb.append(param[1]);
            endSb.append(SqlHelperCons.AND);
            endSb.append(param[0]);
            sqlSb.append(endSb);
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
        if (DbType.SQLServer2008 == sqlBeanConfig.getDbType()) {
            int top = (select.getPage().getPagenum() + 1) * select.getPage().getPagesize();
            int begin = top - select.getPage().getPagesize();
            param = new Integer[]{top, begin};
        }
        //Oracle,DB2
        else if (DbType.Oracle == sqlBeanConfig.getDbType() || DbType.DB2 == sqlBeanConfig.getDbType()) {
            //startIndex = (当前页 * 每页显示的数量)，例如：(0 * 10)
            //endIndex = (当前页 * 每页显示的数量) + 每页显示的数量，例如：10 = (0 * 10) + 10
            //那么如果startIndex=0，endIndex=10，就是查询第0到10条数据
            int startIndex = select.getPage().getPagenum() * select.getPage().getPagesize();
            int endIndex = (select.getPage().getPagenum() * select.getPage().getPagesize()) + select.getPage().getPagesize();
            param = new Integer[]{startIndex, endIndex};
        }
        //Mysql,MariaDB,PostgreSQL
        else {
            int limitOffset = select.getPage().getPagenum() * select.getPage().getPagesize();
            int limitAmount = select.getPage().getPagesize();
            param = new Integer[]{limitOffset, limitAmount};
        }
        return param;
    }


}
