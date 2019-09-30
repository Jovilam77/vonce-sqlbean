package cn.vonce.sql.uitls;

import cn.vonce.common.utils.StringUtil;
import cn.vonce.sql.annotation.SqlBeanField;
import cn.vonce.sql.annotation.SqlBeanJoin;
import cn.vonce.sql.annotation.SqlBeanPojo;
import cn.vonce.sql.annotation.SqlBeanTable;
import cn.vonce.sql.bean.From;
import cn.vonce.sql.bean.Join;
import cn.vonce.sql.bean.Select;
import cn.vonce.sql.constant.SqlHelperCons;
import cn.vonce.sql.enumerate.DbType;
import cn.vonce.sql.enumerate.JoinType;
import cn.vonce.sql.enumerate.WhatType;
import cn.vonce.sql.exception.SqlBeanException;
import cn.vonce.sql.helper.SqlHelper;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * SqlBean 工具类 Created by Jovi on 2018/6/17.
 */
public class SqlBeanUtil {

    /**
     * 获取表名
     *
     * @param clazz
     * @return
     */
    public static From getFrom(Class<?> clazz) {
        SqlBeanTable sqlBeanTable = clazz.getAnnotation(SqlBeanTable.class);
        SqlBeanPojo sqlBeanPojo = clazz.getAnnotation(SqlBeanPojo.class);
        String tableName = clazz.getSimpleName();
        String tableAlias = null;
        if (sqlBeanTable != null) {
            tableName = sqlBeanTable.value();
            tableAlias = sqlBeanTable.alias();
        } else if (sqlBeanPojo != null) {
            tableName = clazz.getSuperclass().getSimpleName();
        }
        if (StringUtil.isEmpty(tableAlias)) {
            tableAlias = tableName;
        }
        return new From(tableName, tableAlias);
    }

    /**
     * 获取表别名
     *
     * @param from
     * @param clazz
     * @return
     */
    public static String getTableAlias(From from, Class<?> clazz) {
        if (from == null || StringUtil.isEmpty(from.getName())) {
            from = getFrom(clazz);
        }
        String tableAlias = from.getAlias();
        if (StringUtil.isEmpty(tableAlias)) {
            SqlBeanTable sqlBeanTable = clazz.getAnnotation(SqlBeanTable.class);
            if (sqlBeanTable != null) {
                if (StringUtil.isEmpty(sqlBeanTable.alias())) {
                    tableAlias = from.getName();
                } else {
                    tableAlias = sqlBeanTable.alias();
                }
            }
        }
        return tableAlias;
    }

    /**
     * 获取Bean字段中实际对于的表字段
     *
     * @param field
     * @return
     * @author Jovi
     * @date 2018年6月14日下午6:12:50
     */
    public static String getFieldName(Field field) {
        SqlBeanField sqlBeanField = field.getAnnotation(SqlBeanField.class);
        String name = field.getName();
        if (sqlBeanField != null) {
            name = sqlBeanField.value();
        }
        return name;
    }

    /**
     * 获取id的字段
     *
     * @param clazz
     * @return
     * @author Jovi
     * @date 2018年5月15日下午2:15:58
     */
    public static Field getIdField(Class<?> clazz) throws SqlBeanException {
        List<Field> fieldList = getBeanAllField(clazz);
        for (Field field : fieldList) {
            SqlBeanField sqlBeanField = field.getAnnotation(SqlBeanField.class);
            if (sqlBeanField != null && sqlBeanField.id()) {
                return field;
            }
        }
        throw new SqlBeanException("请检查SqlBean是否有设置id");
    }

    /**
     * 获取逻辑删除的字段
     *
     * @param clazz
     * @return
     * @author Jovi
     * @date 2019年5月29日下午4:40:20
     */
    public static Field getLogicallyField(Class<?> clazz) throws SqlBeanException {
        List<Field> fieldList = getBeanAllField(clazz);
        for (Field field : fieldList) {
            SqlBeanField sqlBeanField = field.getAnnotation(SqlBeanField.class);
            if (sqlBeanField != null && sqlBeanField.logically()) {
                return field;
            }
        }
        throw new SqlBeanException("请检查SqlBean是否有设置logically");
    }

    /**
     * 是否忽略该字段
     *
     * @param field
     * @return
     * @author Jovi
     * @date 2018年6月14日下午6:21:13
     */
    public static boolean isIgnore(Field field) {
        SqlBeanField sqlBeanField = field.getAnnotation(SqlBeanField.class);
        boolean ignore = false;
        if (sqlBeanField != null) {
            ignore = sqlBeanField.ignore();
        }
        return ignore;
    }

    /**
     * 是否过滤该字段
     *
     * @param filterField
     * @param fieldName
     * @return
     * @author Jovi
     * @date 2018年6月14日下午6:21:13
     */
    public static boolean isFilter(String[] filterField, String fieldName) {
        boolean isFilter = true;
        if (filterField != null) {
            for (String filter : filterField) {
                if (fieldName.equals(filter)) {
                    isFilter = false;
                    break;
                }
            }
        }
        return isFilter;
    }

    /**
     * 判断SqlBeanJoin 是否为空
     *
     * @param sqlBeanJoin
     * @return
     */
    public static boolean sqlBeanJoinIsNotEmpty(SqlBeanJoin sqlBeanJoin) {
        return joinIsNotEmpty(sqlBeanJoin.table(), sqlBeanJoin.tableKeyword(), sqlBeanJoin.mainKeyword());
    }

    /**
     * 判断Join 是否为空
     *
     * @param table
     * @param tableKeyword
     * @param mainKeyword
     * @return
     */
    public static boolean joinIsNotEmpty(String table, String tableKeyword, String mainKeyword) {
        if (table != null && !table.equals("") && tableKeyword != null && !tableKeyword.equals("") && mainKeyword != null && !mainKeyword.equals("")) {
            return true;
        }
        return false;
    }

    /**
     * 获取该bean所有字段（包括父类）
     *
     * @param clazz
     * @return
     */
    public static List<Field> getBeanAllField(Class<?> clazz) {
        List<Field> fieldList = new ArrayList<>();
        fieldList.addAll(Arrays.asList(clazz.getDeclaredFields()));
        SqlBeanPojo sqlBeanPojo = clazz.getAnnotation(SqlBeanPojo.class);
        if (sqlBeanPojo != null && sqlBeanPojo.value()) {
            fieldList.addAll(Arrays.asList(clazz.getSuperclass().getDeclaredFields()));
        }
        return fieldList;
    }

    /**
     * 返回查询的字段
     *
     * @param clazz
     * @return
     * @author Jovi
     * @date 2018年6月15日下午3:29:15
     */
    public static String[] getSelectFields(Class<?> clazz, From from, String[] filterFields) {
        if (clazz == null) {
            return null;
        }
        Set<String> list = new LinkedHashSet<>();
        String tableAlias = getTableAlias(from, clazz);
        List<Field> fieldList = getBeanAllField(clazz);
        for (Field field : fieldList) {
            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }
            if (isIgnore(field)) {
                continue;
            }
            SqlBeanField sqlBeanField = field.getAnnotation(SqlBeanField.class);
            if (isFilter(filterFields, field.getName()) && sqlBeanField != null && sqlBeanField.isBean()) {
                Class<?> subBeanClazz = field.getType();
                SqlBeanJoin sqlBeanJoin = sqlBeanField.join();
                if (sqlBeanJoinIsNotEmpty(sqlBeanJoin) && sqlBeanJoin.field().length > 0 && !sqlBeanJoin.field()[0].equals("")) {
                    for (String joinFieldName : sqlBeanJoin.field()) {
                        list.add(getFieldNameAndAlias(sqlBeanJoin.table(), joinFieldName));
                    }
                } else {
                    Field[] subBeanFields = subBeanClazz.getDeclaredFields();
                    String subTableAliasName = getFrom(subBeanClazz).getAlias();
                    if (sqlBeanJoinIsNotEmpty(sqlBeanJoin)) {
                        subTableAliasName = StringUtil.isNotEmpty(sqlBeanJoin.tableAlias()) ? sqlBeanJoin.tableAlias() : sqlBeanJoin.table();
                    }
                    for (Field subBeanField : subBeanFields) {
                        if (Modifier.isStatic(subBeanField.getModifiers())) {
                            continue;
                        }
                        if (isIgnore(field)) {
                            continue;
                        }
                        list.add(getFieldNameAndAlias(subTableAliasName, getFieldName(subBeanField)));
                    }
                }
            } else if (sqlBeanField != null) {
                SqlBeanJoin sqlBeanJoin = sqlBeanField.join();
                if (sqlBeanJoinIsNotEmpty(sqlBeanJoin)) {
                    //获取SqlBeanJoin 注解中的查询字段
                    String fieldName = sqlBeanJoin.field()[0];
                    //可能会连同一个表，但连接条件不一样（这时表需要区分别名），所以查询的字段可能是同一个，但属于不同表别名下，所以用java字段名当sql字段别名不会出错
                    String alias = field.getName();
                    //如果join中字段未配置查询字段，那将取SqlBeanField注解上的字段名，再取不到就取java字段名
                    if (StringUtil.isEmpty(fieldName)) {
                        fieldName = getFieldName(field);
                    }
                    list.add(getFieldFullName(StringUtil.isEmpty(sqlBeanJoin.tableAlias()) ? sqlBeanJoin.table() : sqlBeanJoin.tableAlias(), fieldName, alias));
                } else {
                    list.add(getFieldNameAndAlias(tableAlias, sqlBeanField.value()));
                }
            } else {
                list.add(getFieldNameAndAlias(tableAlias, field.getName()));
            }
        }
        return list.toArray(new String[]{});
    }

    /**
     * 获取连表的数据
     *
     * @param clazz
     * @return
     */
    public static Map<String, Join> getJoin(Select select, Class<?> clazz) throws SqlBeanException {
        List<Field> fieldList = getBeanAllField(clazz);
        Map<String, Join> joinFieldMap = new HashMap<>();
        for (Field field : fieldList) {
            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }
            SqlBeanField sqlBeanField = field.getAnnotation(SqlBeanField.class);
            if (sqlBeanField != null && sqlBeanField.ignore()) {
                continue;
            }
            Join join = new Join();
            if (sqlBeanField != null && sqlBeanJoinIsNotEmpty(sqlBeanField.join())) {
                join.setJoinType(sqlBeanField.join().type());
                join.setTableName(sqlBeanField.join().table());
                join.setTableAlias(StringUtil.isEmpty(sqlBeanField.join().tableAlias()) ? sqlBeanField.join().table() : sqlBeanField.join().tableAlias());
                join.setTableKeyword(getFieldFullName(join.getTableAlias(), sqlBeanField.join().tableKeyword()));
                join.setMainKeyword(getFieldFullName(select.getFrom().getAlias(), sqlBeanField.join().mainKeyword()));
                //key是唯一的，作用是为了去重复，因为可能连接相同的表取不同的字段，但连接相同的表，连接条件不同是可以允许的
                joinFieldMap.put(sqlBeanField.join().table().toLowerCase() + sqlBeanField.join().tableKeyword().toLowerCase() + sqlBeanField.join().mainKeyword().toLowerCase(), join);
            } else if (sqlBeanField != null && sqlBeanField.isBean()) {
                Class<?> subClazz = field.getType();
                From from = getFrom(subClazz);
                String tableKeyword = getFieldName(getIdField(subClazz));
                join.setJoinType(JoinType.INNER_JOIN);
                join.setTableName(from.getName());
                join.setTableAlias(from.getAlias());
                join.setTableKeyword(getFieldFullName(join.getTableAlias(), tableKeyword));
                join.setMainKeyword(getFieldFullName(select.getFrom().getAlias(), sqlBeanField.value()));
                //key是唯一的，作用是为了去重复，因为可能连接相同的表取不同的字段，但连接相同的表，连接条件不同是可以允许的
                joinFieldMap.put(join.getTableName().toLowerCase() + tableKeyword.toLowerCase() + sqlBeanField.value().toLowerCase(), join);
            }
        }
        return joinFieldMap;
    }

    /**
     * 设置表连接
     *
     * @param select
     * @param clazz
     * @throws SqlBeanException
     */
    public static void setJoin(Select select, Class<?> clazz) throws SqlBeanException {
        Map<String, Join> joinFieldMap = getJoin(select, clazz);
        for (Join join : joinFieldMap.values()) {
            String tableName = join.getTableName();
            String tableAlias = join.getTableAlias();
            String tableKeyword = join.getTableKeyword();
            String mainKeyword = join.getMainKeyword();
            if (isToUpperCase()) {
                tableName = tableName.toUpperCase();
                tableAlias = tableAlias.toUpperCase();
                tableKeyword = tableKeyword.toUpperCase();
                mainKeyword = mainKeyword.toUpperCase();
            }
            select.join(join.getJoinType(), tableName, tableAlias, tableKeyword, mainKeyword);
        }
    }

    /**
     * 获得新的字段名 包含别名
     *
     * @param tableAlias
     * @param fieldName
     * @return
     */
    public static String getFieldNameAndAlias(String tableAlias, String fieldName) {
        return getFieldFullName(tableAlias, fieldName, tableAlias + SqlHelperCons.POINT + fieldName);
    }

    /**
     * 获得新的字段名
     *
     * @param tableAlias
     * @param fieldName
     * @return
     */
    public static String getFieldFullName(String tableAlias, String fieldName) {
        return getFieldFullName(tableAlias, fieldName, "");
    }

    /**
     * 获取获取字段全名（表名+字段名）
     *
     * @param clazz
     * @param field
     * @return
     */
    public static String getFieldFullName(Class<?> clazz, Field field) {
        return getFieldFullName(clazz, getFieldName(field));
    }

    /**
     * 获取获取字段全名（表名+字段名）
     *
     * @param clazz
     * @param fieldName
     * @return
     */
    public static String getFieldFullName(Class<?> clazz, String fieldName) {
        return getFieldFullName(getTableAlias(null, clazz), fieldName, "");
    }

    /**
     * 获得新的字段名
     *
     * @param tableAlias
     * @param fieldName
     * @return
     */
    public static String getFieldFullName(String tableAlias, String fieldName, String alias) {
        String transferred = getTransferred();
        if (isToUpperCase()) {
            tableAlias = tableAlias.toUpperCase();
            fieldName = fieldName.toUpperCase();
        }
        if (StringUtil.isNotEmpty(alias)) {
            alias = SqlHelperCons.AS + transferred + alias + transferred;
        }
        return transferred + tableAlias + transferred + SqlHelperCons.POINT + transferred + fieldName + transferred + alias;
    }

    /**
     * 获取处理后的where语句
     *
     * @param where
     * @param args
     * @return
     * @author Jovi
     * @date 2018年5月15日下午2:15:37
     */
    public static String getCondition(String where, Object[] args) {
        if (where == null || where.equals("")) {
            return "";
        }
        StringBuffer conditionSql = new StringBuffer();
        int index = 0;
        for (char c : where.toCharArray()) {
            if ('?' == c) {
                conditionSql.append(getSqlValue(args[index] == null ? "" : args[index]));
                index++;
            } else {
                conditionSql.append(c);
            }
        }
        return conditionSql.toString();
    }

    /**
     * 获取处理后的where语句
     *
     * @param where
     * @param bean
     * @return
     * @author Jovi
     * @date 2018年5月15日下午3:25:57
     */
    public static String getCondition(String where, Object bean) throws NoSuchFieldException, IllegalAccessException {
        String prefix = "${";
        String suffix = "}";
        if (where == null || bean == null) {
            return where;
        }
        StringBuffer conditionSql = new StringBuffer(where);
        int startIndex = conditionSql.indexOf(prefix);
        while (startIndex != -1) {
            int endIndex = conditionSql.indexOf(suffix, startIndex + prefix.length());
            if (endIndex != -1) {
                String name = conditionSql.substring(startIndex + prefix.length(), endIndex);
                int nextIndex = endIndex + suffix.length();
                Field field = bean.getClass().getDeclaredField(name);
                field.setAccessible(true);
                String value = getSqlValue(field.get(bean));
                //String value = SqlHelper.getSqlValue(ReflectAsmUtil.get(bean.getClass(), bean, name));
                conditionSql.replace(startIndex, endIndex + suffix.length(), value);
                nextIndex = startIndex + value.length();
                startIndex = conditionSql.indexOf(prefix, nextIndex);
            } else {
                startIndex = -1;
            }
        }
        return conditionSql.toString();
    }


    /**
     * 获取模糊查询的值
     *
     * @param value    值
     * @param likeType 模糊类型
     * @return
     */
    public static Object getLikeValue(Object value, String likeType) {
        if (likeType != null && likeType.trim().equals("left")) {
            value = "%" + value;
        } else if (likeType != null && likeType.trim().equals("right")) {
            value = value + "%";
        } else {
            value = "%" + value + "%";
        }
        return value;
    }

    /**
     * 获取字段类型
     *
     * @param typeName
     * @return
     * @author Jovi
     * @date 2017年6月22日下午7:08:13
     */
    public static WhatType whatType(String typeName) {
        WhatType whatType;
        switch (typeName) {
            case "String":
            case "java.lang.String":
            case "char":
            case "Character":
            case "java.lang.Character":
                whatType = WhatType.STRING_TYPE;
                break;
            case "boolean":
            case "Boolean":
            case "java.lang.Boolean":
                whatType = WhatType.BOOL_TYPE;
                break;
            case "byte":
            case "Byte":
            case "java.lang.Byte":
            case "short":
            case "Short":
            case "java.lang.Short":
            case "int":
            case "Integer":
            case "java.lang.Integer":
            case "long":
            case "Long":
            case "java.lang.Long":
            case "float":
            case "Float":
            case "java.lang.Float":
            case "double":
            case "Double":
            case "java.lang.Double":
                whatType = WhatType.VALUE_TYPE;
                break;
            case "Date":
            case "java.util.Date":
                whatType = WhatType.DATE_TYPE;
                break;
            default:
                whatType = WhatType.OBJECT_TYPE;
                break;
        }
        return whatType;
    }

    /**
     * 该类型是否为基本类型
     *
     * @param typeName
     * @return
     */
    public static boolean isBaseType(String typeName) {
        boolean isTrue;
        switch (typeName) {
            case "String":
            case "java.lang.String":
            case "Character":
            case "java.lang.Character":
            case "Boolean":
            case "java.lang.Boolean":
            case "Byte":
            case "java.lang.Byte":
            case "Short":
            case "java.lang.Short":
            case "Integer":
            case "java.lang.Integer":
            case "Long":
            case "java.lang.Long":
            case "Float":
            case "java.lang.Float":
            case "Double":
            case "java.lang.Double":
            case "Date":
            case "java.util.Date":
            case "BigDecimal":
            case "java.math.BigDecimal":
                isTrue = true;
                break;
            default:
                isTrue = false;
                break;
        }
        return isTrue;
    }

    /**
     * 该类型是否为
     *
     * @param typeName
     * @return
     */
    public static boolean isMap(String typeName) {
        boolean isTrue;
        switch (typeName) {
            case "Map":
            case "java.util.Map":
            case "HashMap":
            case "java.util.HashMap":
                isTrue = true;
                break;
            default:
                isTrue = false;
                break;
        }
        return isTrue;
    }

    /**
     * 获取sql实际值(过滤sql注入)
     *
     * @param value
     * @return
     * @author Jovi
     * @date 2018年3月1日下午12:01:05
     */
    public static String getSqlValue(Object value) {
        SqlHelper.checkInitStatus();
        if (value == null) {
            return null;
        }
        String single_quotation_mark = SqlHelperCons.SINGLE_QUOTATION_MARK;
        String sqlValue = "";
        switch (whatType(value.getClass().getSimpleName())) {
            case VALUE_TYPE:
                sqlValue = value.toString();
                break;
            case BOOL_TYPE:
                sqlValue = value.toString();
                break;
            case DATE_TYPE:
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                switch (SqlHelper.getSqlBeanConfig().getDbType()) {
                    case Oracle:
                        sqlValue = "to_timestamp(" + single_quotation_mark + sdf.format(value) + single_quotation_mark + ", 'syyyy-mm-dd hh24:mi:ss.ff')";
                        break;
                    default:
                        sqlValue = single_quotation_mark + sdf.format(value) + single_quotation_mark;
                        break;
                }
                break;
            default:
                sqlValue = single_quotation_mark + filterSQLInject(value.toString()) + single_quotation_mark;
                break;
        }
        return sqlValue;
    }

    /**
     * 该查询语句是否为count
     *
     * @param select
     * @return
     */
    public static boolean isCount(Select select) {
        boolean isTrue = true;
        if (select.getColumn() != null && !select.getColumn().contains(SqlHelperCons.COUNT + SqlHelperCons.BEGIN_BRACKET + SqlHelperCons.ALL + SqlHelperCons.END_BRACKET)) {
            isTrue = false;
        }
        return isTrue;
    }

    /**
     * 是否使用分页
     *
     * @param select
     * @return
     */
    public static boolean isUsePage(Select select) {
        if (select.getPage() != null) {
            return true;
        }
        return false;
    }

    /**
     * 过滤sql注入
     *
     * @param str
     * @return
     * @author Jovi
     * @date 2018年3月1日上午11:30:41
     */
    public static String filterSQLInject(String str) {
        return str.replaceAll("([';])+|(--)+", "");
    }

    /**
     * 获取不同数据库的转义符号
     *
     * @return
     */
    public static String getTransferred() {
        SqlHelper.checkInitStatus();
        String transferred = SqlHelperCons.DOUBLE_ESCAPE_CHARACTER;
        DbType dbType = SqlHelper.getSqlBeanConfig().getDbType();
        if (dbType == DbType.MySQL || dbType == DbType.MariaDB) {
            transferred = SqlHelperCons.SINGLE_ESCAPE_CHARACTER;
        }
        return transferred;
    }

    /**
     * 是否需要转大写
     *
     * @return
     */
    public static boolean isToUpperCase() {
        SqlHelper.checkInitStatus();
        if (SqlHelper.getSqlBeanConfig().getToUpperCase() != null && SqlHelper.getSqlBeanConfig().getToUpperCase()) {
            return true;
        }
        return false;
    }

}
