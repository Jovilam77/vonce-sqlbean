package cn.vonce.sql.uitls;

import cn.vonce.common.utils.ReflectAsmUtil;
import cn.vonce.common.utils.StringUtil;
import cn.vonce.sql.annotation.*;
import cn.vonce.sql.bean.*;
import cn.vonce.sql.constant.SqlHelperCons;
import cn.vonce.sql.enumerate.DbType;
import cn.vonce.sql.enumerate.WhatType;
import cn.vonce.sql.exception.SqlBeanException;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * SqlBean 工具类 Created by Jovi on 2018/6/17.
 */
public class SqlBeanUtil {

    /**
     * 根据类名获取表名信息
     *
     * @param clazz
     * @return
     */
    public static Table getTable(Class<?> clazz) {
        SqlBeanTable sqlBeanTable = clazz.getAnnotation(SqlBeanTable.class);
        SqlBeanPojo sqlBeanPojo = clazz.getAnnotation(SqlBeanPojo.class);
        String tableName = clazz.getSimpleName();
        String tableAlias = null;
        if (sqlBeanTable != null) {
            tableName = sqlBeanTable.value();
            tableAlias = sqlBeanTable.alias();
        } else if (sqlBeanPojo != null) {
            SqlBeanTable subSqlBeanTable = clazz.getAnnotation(SqlBeanTable.class);
            if (subSqlBeanTable != null) {
                tableName = subSqlBeanTable.value();
                tableAlias = subSqlBeanTable.alias();
            } else {
                tableName = clazz.getSuperclass().getName();
            }
        }
        if (StringUtil.isEmpty(tableAlias)) {
            tableAlias = tableName;
        }
        return new Table(tableName, tableAlias);
    }

    /**
     * 根据表名信息或类获取表别名
     *
     * @param table
     * @param clazz
     * @return
     */
    public static String getTableAlias(Table table, Class<?> clazz) {
        if (table == null || StringUtil.isEmpty(table.getName())) {
            table = getTable(clazz);
        }
        String tableAlias = table.getAlias();
        if (StringUtil.isEmpty(tableAlias)) {
            SqlBeanTable sqlBeanTable = clazz.getAnnotation(SqlBeanTable.class);
            if (sqlBeanTable != null) {
                if (StringUtil.isEmpty(sqlBeanTable.alias())) {
                    tableAlias = table.getName();
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
    public static String getTableFieldName(Field field) {
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
        return getIdField(getBeanAllField(clazz));
    }

    /**
     * 获取id的字段
     *
     * @param fieldList
     * @return
     */
    public static Field getIdField(List<Field> fieldList) throws SqlBeanException {
        Field idField = null;
        int existId = 0;
        for (Field field : fieldList) {
            SqlBeanId sqlBeanField = field.getAnnotation(SqlBeanId.class);
            if (sqlBeanField != null) {
                idField = field;
                existId++;
            }
            if (existId > 1) {
                throw new SqlBeanException("请正确的标识id字段，id字段只能标识一个，但我们在'" + field.getDeclaringClass().getName() + "'此实体类或其父类找到了不止一处");
            }
        }
        if (existId == 0) {
            throw new SqlBeanException("请检查实体类是否有标识id字段");
        }
        return idField;
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
        if (sqlBeanField != null) {
            return sqlBeanField.ignore();
        }
        return false;
    }

    /**
     * 是否过滤该字段
     *
     * @param filterTableFields
     * @param tableFieldName
     * @return
     * @author Jovi
     * @date 2018年6月14日下午6:21:13
     */
    public static boolean isFilter(String[] filterTableFields, String tableFieldName) {
        if (filterTableFields != null) {
            for (String filter : filterTableFields) {
                if (tableFieldName.equals(filter)) {
                    return true;
                }
            }
        }
        return false;
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
     * 根据表字段获取java中对应的java字段
     *
     * @param fieldList
     * @param tableFieldName
     * @return
     */
    public static Field getFieldByTableFieldName(List<Field> fieldList, String tableFieldName) {
        Field thisField = null;
        for (Field field : fieldList) {
            SqlBeanField sqlBeanField = field.getAnnotation(SqlBeanField.class);
            if (sqlBeanField != null && sqlBeanField.value().equals(tableFieldName)) {
                thisField = field;
            } else if (field.getName().equals(tableFieldName)) {
                thisField = field;
            }
        }
        return thisField;
    }

    /**
     * 返回查询的字段
     *
     * @param clazz
     * @return
     * @author Jovi
     * @date 2018年6月15日下午3:29:15
     */
    public static List<Column> getSelectColumns(Common common, Class<?> clazz, Table table, String[] filterTableFields) throws SqlBeanException {
        if (clazz == null) {
            return null;
        }
        Set<Column> columnSet = new LinkedHashSet<>();
        String tableAlias = getTableAlias(table, clazz);
        List<Field> fieldList = getBeanAllField(clazz);
        for (Field field : fieldList) {
            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }
            if (isIgnore(field)) {
                continue;
            }
            if (isFilter(filterTableFields, getTableFieldName(field))) {
                continue;
            }
            SqlBeanJoin sqlBeanJoin = field.getAnnotation(SqlBeanJoin.class);
            if (sqlBeanJoin != null && sqlBeanJoin.isBean()) {
                Class<?> subBeanClazz = field.getType();
                //如果有指定查询的字段
                if (sqlBeanJoinIsNotEmpty(sqlBeanJoin) && sqlBeanJoin.value().length > 0 && !sqlBeanJoin.value()[0].equals("")) {
                    List<Field> subBeanFieldList = getBeanAllField(subBeanClazz);
                    for (String fieldName : sqlBeanJoin.value()) {
                        Field javaField = getFieldByTableFieldName(subBeanFieldList, fieldName);
                        if (javaField == null) {
                            throw new SqlBeanException("该类的表连接查询字段未与java字段关联：" + clazz.getName() + ">" + field.getName() + ">" + fieldName);
                        }
                        columnSet.add(new Column(sqlBeanJoin.table(), fieldName, javaField.getName()));
                    }
                }
                //没有指定查询的字段则查询所有字段
                else {
                    Field[] subBeanFields = subBeanClazz.getDeclaredFields();
                    String subTableAliasName = getTable(subBeanClazz).getAlias();
                    if (StringUtil.isNotEmpty(sqlBeanJoin.table()) || StringUtil.isNotEmpty(sqlBeanJoin.tableAlias())) {
                        subTableAliasName = StringUtil.isNotEmpty(sqlBeanJoin.tableAlias()) ? sqlBeanJoin.tableAlias() : sqlBeanJoin.table();
                    }
                    for (Field subBeanField : subBeanFields) {
                        if (Modifier.isStatic(subBeanField.getModifiers())) {
                            continue;
                        }
                        if (isIgnore(field)) {
                            continue;
                        }
                        columnSet.add(new Column(subTableAliasName, getTableFieldName(subBeanField), subBeanField.getName()));
                    }
                }
            } else if (sqlBeanJoin != null) {
                if (sqlBeanJoinIsNotEmpty(sqlBeanJoin)) {
                    //获取SqlBeanJoin 注解中的查询字段
                    String tableFieldName = sqlBeanJoin.value()[0];
                    if (StringUtil.isEmpty(tableFieldName)) {
                        throw new SqlBeanException("该类的表连接查询字段未与java字段关联：");
                    }
                    //可能会连同一个表，但连接条件不一样（这时表需要区分别名），所以查询的字段可能是同一个，但属于不同表别名下，所以用java字段名当sql字段别名不会出错
                    String alias = field.getName();
                    columnSet.add(new Column(StringUtil.isEmpty(sqlBeanJoin.tableAlias()) ? sqlBeanJoin.table() : sqlBeanJoin.tableAlias(), tableFieldName, alias));
                }
            } else {
                columnSet.add(new Column(tableAlias, getTableFieldName(field), field.getName()));
            }
        }
        return new ArrayList<>(columnSet);
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
            SqlBeanJoin sqlBeanJoin = field.getAnnotation(SqlBeanJoin.class);
            Join join = new Join();
            if (sqlBeanJoin != null && sqlBeanJoinIsNotEmpty(sqlBeanJoin)) {
                join.setJoinType(sqlBeanJoin.type());
                join.setTableName(sqlBeanJoin.table());
                join.setTableAlias(StringUtil.isEmpty(sqlBeanJoin.tableAlias()) ? sqlBeanJoin.table() : sqlBeanJoin.tableAlias());
                join.setTableKeyword(sqlBeanJoin.tableKeyword());
                join.setMainKeyword(sqlBeanJoin.mainKeyword());
                //key是唯一的，作用是为了去重复，因为可能连接相同的表取不同的字段，但连接相同的表，连接条件不同是可以允许的
                joinFieldMap.put(sqlBeanJoin.table().toLowerCase() + sqlBeanJoin.tableKeyword().toLowerCase() + sqlBeanJoin.mainKeyword().toLowerCase(), join);
            } else if (sqlBeanJoin != null && sqlBeanJoin.isBean()) {
                Class<?> subClazz = field.getType();
                Table table = getTable(subClazz);
                String tableKeyword = getTableFieldName(getIdField(subClazz));
                join.setJoinType(sqlBeanJoin.type());
                join.setTableName(table.getName());
                join.setTableAlias(table.getAlias());
                join.setTableKeyword(tableKeyword);
                join.setMainKeyword(sqlBeanJoin.mainKeyword());
                //key是唯一的，作用是为了去重复，因为可能连接相同的表取不同的字段，但连接相同的表，连接条件不同是可以允许的
                joinFieldMap.put(join.getTableName().toLowerCase() + tableKeyword.toLowerCase() + sqlBeanJoin.mainKeyword().toLowerCase(), join);
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
            if (isToUpperCase(select)) {
                tableName = tableName.toUpperCase();
                tableAlias = tableAlias.toUpperCase();
                tableKeyword = tableKeyword.toUpperCase();
                mainKeyword = mainKeyword.toUpperCase();
            }
            select.join(join.getJoinType(), tableName, tableAlias, tableKeyword, mainKeyword);
        }
    }

    /**
     * 获得新的表字段名
     *
     * @param common
     * @param tableAlias
     * @param tableFieldName
     * @return
     */
    public static String getTableFieldFullName(Common common, String tableAlias, String tableFieldName) {
        return getTableFieldFullName(common, tableAlias, tableFieldName, "");
    }

    /**
     * 获取获取表字段全名（表名+字段名）
     *
     * @param common
     * @param clazz
     * @param field
     * @return
     */
    public static String getTableFieldFullName(Common common, Class<?> clazz, Field field) {
        return getTableFieldFullName(common, clazz, getTableFieldName(field));
    }

    /**
     * 获取获取表字段全名（表名+字段名）
     *
     * @param common
     * @param clazz
     * @param tableFieldName
     * @return
     */
    public static String getTableFieldFullName(Common common, Class<?> clazz, String tableFieldName) {
        return getTableFieldFullName(common, getTableAlias(null, clazz), tableFieldName, "");
    }

    /**
     * 获得新的表字段名
     *
     * @param common
     * @param tableAlias
     * @param tableFieldName
     * @return
     */
    public static String getTableFieldFullName(Common common, String tableAlias, String tableFieldName, String tableFieldAlias) {
        String transferred = getTransferred(common);
        if (isToUpperCase(common)) {
            tableAlias = tableAlias.toUpperCase();
            tableFieldName = tableFieldName.toUpperCase();
        }
        if (StringUtil.isNotEmpty(tableFieldAlias)) {
            tableFieldAlias = SqlHelperCons.AS + transferred + tableFieldAlias + transferred;
        }
        return transferred + tableAlias + transferred + SqlHelperCons.POINT + transferred + tableFieldName + transferred + tableFieldAlias;
    }

    /**
     * 获取处理后的where语句
     *
     * @param common
     * @param where
     * @param args
     * @return
     * @author Jovi
     * @date 2018年5月15日下午2:15:37
     */
    public static String getCondition(Common common, String where, Object[] args) {
        if (where == null || where.equals("")) {
            return "";
        }
        StringBuffer conditionSql = new StringBuffer();
        int index = 0;
        for (char c : where.toCharArray()) {
            if ('?' == c) {
                StringBuffer value = new StringBuffer();
                Object[] objects = null;
                if (args[index] == null) {
                    objects = null;
                } else if (args[index].getClass().isArray()) {
                    objects = (Object[]) args[index];
                } else if (args[index] instanceof Collection) {
                    objects = ((Collection) args[index]).toArray();
                } else {
                    objects = new Object[]{args[index]};
                }
                if (objects != null) {
                    for (int i = 0; i < objects.length; i++) {
                        value.append(getSqlValue(common, objects[i]));
                        value.append(SqlHelperCons.COMMA);
                    }
                    value.deleteCharAt(value.length() - SqlHelperCons.COMMA.length());
                }
                conditionSql.append(value);
                index++;
            } else if ('!' == c) {
                conditionSql.append(args[index]);
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
     * @param common
     * @param where
     * @param bean
     * @return
     * @author Jovi
     * @date 2018年5月15日下午3:25:57
     */
    public static String getCondition(Common common, String where, Object bean) throws NoSuchFieldException, IllegalAccessException {
        String prefix = "${";
        String suffix = "}";
        if (where == null || bean == null) {
            return "";
        }
        StringBuffer conditionSql = new StringBuffer(where);
        int startIndex = conditionSql.indexOf(prefix);
        while (startIndex != -1) {
            int endIndex = conditionSql.indexOf(suffix, startIndex + prefix.length());
            if (endIndex != -1) {
                String name = conditionSql.substring(startIndex + prefix.length(), endIndex);
                int nextIndex = endIndex + suffix.length();
                String value = getSqlValue(common, ReflectAsmUtil.get(bean.getClass(), bean, name));
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
    public static String getSqlValue(Common common, Object value) {
        if (value == null) {
            return null;
        }
        String single_quotation_mark = SqlHelperCons.SINGLE_QUOTATION_MARK;
        String sqlValue = "";
        switch (whatType(value.getClass().getSimpleName())) {
            case VALUE_TYPE:
            case BOOL_TYPE:
                sqlValue = value.toString();
                break;
            case DATE_TYPE:
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                switch (common.getSqlBeanConfig().getDbType()) {
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
        if (select.getColumnList() != null && !select.getColumnList().contains(SqlHelperCons.COUNT + SqlHelperCons.BEGIN_BRACKET + SqlHelperCons.ALL + SqlHelperCons.END_BRACKET)) {
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
     * @param common
     * @return
     */
    public static String getTransferred(Common common) {
        String transferred = SqlHelperCons.DOUBLE_ESCAPE_CHARACTER;
        DbType dbType = common.getSqlBeanConfig().getDbType();
        if (dbType == DbType.MySQL || dbType == DbType.MariaDB) {
            transferred = SqlHelperCons.SINGLE_ESCAPE_CHARACTER;
        }
        return transferred;
    }

    /**
     * 是否需要转大写
     *
     * @param common
     * @return
     */
    public static boolean isToUpperCase(Common common) {
        if (common.getSqlBeanConfig().getToUpperCase() != null && common.getSqlBeanConfig().getToUpperCase()) {
            return true;
        }
        return false;
    }

}
