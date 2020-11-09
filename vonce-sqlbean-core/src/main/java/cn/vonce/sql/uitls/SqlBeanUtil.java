package cn.vonce.sql.uitls;

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
        SqlUnion sqlUnion = clazz.getAnnotation(SqlUnion.class);
        SqlTable sqlTable;
        String className = "";
        String schema = "";
        String tableName = "";
        String tableAlias = "";
        if (sqlUnion != null) {
            sqlTable = clazz.getSuperclass().getAnnotation(SqlTable.class);
        } else {
            sqlTable = clazz.getAnnotation(SqlTable.class);
        }
        if (sqlTable != null) {
            schema = sqlTable.schema();
            tableName = sqlTable.value();
            tableAlias = sqlTable.alias();
        } else {
            tableName = className;
            tableAlias = tableName;
        }
        if (StringUtil.isEmpty(tableAlias)) {
            tableAlias = tableName;
        }
        return new Table(schema, tableName, tableAlias);
    }

    /**
     * 优先根据@SqlBeanJoin注解获取，获取不到则从类获取
     *
     * @param clazz
     * @return
     */
    public static Table getTable(Class<?> clazz, SqlJoin sqlJoin) {
        Table table = new Table();
        if (sqlJoin != null) {
            table.setSchema(sqlJoin.schema());
            table.setName(sqlJoin.table());
            table.setAlias(sqlJoin.tableAlias());
        }
        Table classTable = getTable(clazz);
        if (StringUtil.isEmpty(table.getSchema())) {
            table.setSchema(classTable.getSchema());
        }
        if (StringUtil.isEmpty(table.getName())) {
            table.setName(classTable.getName());
        }
        if (StringUtil.isEmpty(table.getAlias())) {
            table.setAlias(classTable.getAlias());
        }
        if (StringUtil.isEmpty(table.getAlias())) {
            table.setAlias(classTable.getName());
        }
        return table;
    }

    /**
     * 获取Bean字段中实际对于的表字段
     *
     * @param field
     * @return
     */
    public static String getTableFieldName(Field field) {
        SqlColumn sqlColumn = field.getAnnotation(SqlColumn.class);
        String name = field.getName();
        if (sqlColumn != null) {
            name = sqlColumn.value();
        }
        return name;
    }

    /**
     * 获取id标识字段
     *
     * @param clazz
     * @return
     */
    public static Field getIdField(Class<?> clazz) throws SqlBeanException {
        List<Field> fieldList = getBeanAllField(clazz);
        Field idField = null;
        int existId = 0;
        for (Field field : fieldList) {
            SqlId sqlBeanField = field.getAnnotation(SqlId.class);
            if (sqlBeanField != null) {
                idField = field;
                existId++;
            }
            if (existId > 1) {
                throw new SqlBeanException("请正确标识@SqlId注解，id字段只能标识一个，但我们在'" + field.getDeclaringClass().getName() + "'此实体类或其父类找到了不止一处");
            }
        }
        if (existId == 0) {
            throw new SqlBeanException("请检查实体类的id字段是否正确标识@SqlId注解");
        }
        return idField;
    }

    /**
     * 获取逻辑删除标识字段
     *
     * @param clazz
     * @return
     */
    public static Field getLogicallyField(Class<?> clazz) throws SqlBeanException {
        List<Field> fieldList = getBeanAllField(clazz);
        Field logicallyField = null;
        int existLogicallyField = 0;
        for (Field field : fieldList) {
            SqlLogically sqlLogically = field.getAnnotation(SqlLogically.class);
            if (sqlLogically != null) {
                logicallyField = field;
                existLogicallyField++;
            }
            if (existLogicallyField > 1) {
                throw new SqlBeanException("请正确标识@SqlLogically注解，逻辑删除字段只能标识一个，但我们在'" + field.getDeclaringClass().getName() + "'此实体类或其父类找到了不止一处");
            }
        }
        if (existLogicallyField == 0) {
            throw new SqlBeanException("请检查实体类申明逻辑删除的字段是否正确标识@SqlLogically注解");
        }
        return logicallyField;
    }

    /**
     * 获取乐观锁标识字段
     *
     * @param clazz
     * @return
     */
    public static Field getVersionField(Class<?> clazz) throws SqlBeanException {
        List<Field> fieldList = getBeanAllField(clazz);
        Field versionField = null;
        int existVersionField = 0;
        for (Field field : fieldList) {
            SqlVersion sqlVersion = field.getAnnotation(SqlVersion.class);
            if (sqlVersion != null) {
                versionField = field;
                existVersionField++;
            }
            if (existVersionField > 1) {
                throw new SqlBeanException("请正确标识SqlVersion注解，version字段只能标识一个，但我们在'" + field.getDeclaringClass().getName() + "'此实体类或其父类找到了不止一处");
            }
        }
        return versionField;
    }

    /**
     * 是否忽略该字段
     *
     * @param field
     * @return
     */
    public static boolean isIgnore(Field field) {
        SqlColumn sqlColumn = field.getAnnotation(SqlColumn.class);
        if (sqlColumn != null) {
            return sqlColumn.ignore();
        }
        return false;
    }

    /**
     * 是否过滤该字段
     *
     * @param filterTableFields
     * @param tableFieldName
     * @return
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
     * @param sqlJoin
     * @return
     */
    public static boolean sqlBeanJoinIsNotEmpty(SqlJoin sqlJoin) {
        return joinIsNotEmpty(sqlJoin.table(), sqlJoin.tableKeyword(), sqlJoin.mainKeyword());
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
        SqlUnion sqlUnion = clazz.getAnnotation(SqlUnion.class);
        if (sqlUnion != null) {
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
            SqlColumn sqlColumn = field.getAnnotation(SqlColumn.class);
            if (sqlColumn != null && sqlColumn.value().equals(tableFieldName)) {
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
     * @param filterTableFields
     * @return
     */
    public static List<Column> getSelectColumns(Class<?> clazz, String[] filterTableFields) throws SqlBeanException {
        if (clazz == null) {
            return null;
        }
        Set<Column> columnSet = new LinkedHashSet<>();
        Table table = getTable(clazz);
        String tableAlias = table.getAlias();
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
            SqlJoin sqlJoin = field.getAnnotation(SqlJoin.class);
            if (sqlJoin != null && sqlJoin.isBean()) {
                Class<?> subBeanClazz = field.getType();
                //如果有指定查询的字段
                if (sqlBeanJoinIsNotEmpty(sqlJoin) && sqlJoin.value().length > 0 && !sqlJoin.value()[0].equals("")) {
                    List<Field> subBeanFieldList = getBeanAllField(subBeanClazz);
                    for (String fieldName : sqlJoin.value()) {
                        Field javaField = getFieldByTableFieldName(subBeanFieldList, fieldName);
                        if (javaField == null) {
                            throw new SqlBeanException("该类的表连接查询字段未与java字段关联：" + clazz.getName() + ">" + field.getName() + ">" + fieldName);
                        }
                        //表名、别名优先从@SqlBeanJoin注解中取，如果不存在则从类注解中取，再其次是类名
                        Table subTable = getTable(subBeanClazz, sqlJoin);
                        columnSet.add(new Column(/*sqlJoin.schema(), */sqlJoin.table(), fieldName, getColumnAlias(subTable.getAlias(), javaField.getName())));
                    }
                }
                //没有指定查询的字段则查询所有字段
                else {
                    Field[] subBeanFields = subBeanClazz.getDeclaredFields();
                    //表名、别名优先从@SqlBeanJoin注解中取，如果不存在则从类注解中取，再其次是类名
                    Table subTable = getTable(subBeanClazz, sqlJoin);
                    for (Field subBeanField : subBeanFields) {
                        if (Modifier.isStatic(subBeanField.getModifiers())) {
                            continue;
                        }
                        if (isIgnore(field)) {
                            continue;
                        }
                        columnSet.add(new Column(/*subTable.getSchema(), */subTable.getAlias(), getTableFieldName(subBeanField), getColumnAlias(subTable.getAlias(), subBeanField.getName())));
                    }
                }
            } else if (sqlJoin != null) {
                if (sqlBeanJoinIsNotEmpty(sqlJoin)) {
                    //获取SqlBeanJoin 注解中的查询字段
                    String tableFieldName = sqlJoin.value()[0];
                    if (StringUtil.isEmpty(tableFieldName)) {
                        throw new SqlBeanException("该类的表连接查询字段未与java字段关联：");
                    }
                    //可能会连同一个表，但连接条件不一样（这时表需要区分别名），所以查询的字段可能是同一个，但属于不同表别名下，所以用java字段名当sql字段别名不会出错
                    String subTableAlias = StringUtil.isEmpty(sqlJoin.tableAlias()) ? sqlJoin.table() : sqlJoin.tableAlias();
                    columnSet.add(new Column(/*sqlJoin.schema(), */subTableAlias, tableFieldName, getColumnAlias(subTableAlias, field.getName())));
                }
            } else {
                columnSet.add(new Column(/*table.getSchema(), */tableAlias, getTableFieldName(field), getColumnAlias(tableAlias, field.getName())));
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
    public static Map<String, Join> getJoin(Class<?> clazz) throws SqlBeanException {
        List<Field> fieldList = getBeanAllField(clazz);
        Map<String, Join> joinFieldMap = new HashMap<>();
        for (Field field : fieldList) {
            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }
            SqlJoin sqlJoin = field.getAnnotation(SqlJoin.class);
            Join join = new Join();
            if (sqlJoin != null && sqlBeanJoinIsNotEmpty(sqlJoin)) {
                join.setJoinType(sqlJoin.type());
                join.setSchema(sqlJoin.schema());
                join.setTableName(sqlJoin.table());
                join.setTableAlias(StringUtil.isEmpty(sqlJoin.tableAlias()) ? sqlJoin.table() : sqlJoin.tableAlias());
                join.setTableKeyword(sqlJoin.tableKeyword());
                join.setMainKeyword(sqlJoin.mainKeyword());
                //key是唯一的，作用是为了去重复，因为可能连接相同的表取不同的字段，但连接相同的表，连接条件不同是可以允许的
                joinFieldMap.put(sqlJoin.table().toLowerCase() + sqlJoin.tableKeyword().toLowerCase() + sqlJoin.mainKeyword().toLowerCase(), join);
            } else if (sqlJoin != null && sqlJoin.isBean()) {
                Class<?> subClazz = field.getType();
                //表名、别名优先从@SqlBeanJoin注解中取，如果不存在则从类注解中取，再其次是类名
                Table table = getTable(subClazz, sqlJoin);
                String tableKeyword = getTableFieldName(getIdField(subClazz));
                join.setJoinType(sqlJoin.type());
                join.setSchema(table.getSchema());
                join.setTableName(table.getName());
                join.setTableAlias(table.getAlias());
                join.setTableKeyword(tableKeyword);
                join.setMainKeyword(sqlJoin.mainKeyword());
                //key是唯一的，作用是为了去重复，因为可能连接相同的表取不同的字段，但连接相同的表，连接条件不同是可以允许的
                joinFieldMap.put(join.getTableName().toLowerCase() + tableKeyword.toLowerCase() + sqlJoin.mainKeyword().toLowerCase(), join);
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
        Map<String, Join> joinFieldMap = getJoin(clazz);
        for (Join join : joinFieldMap.values()) {
            String schema = join.getSchema();
            String tableName = join.getTableName();
            String tableAlias = join.getTableAlias();
            String tableKeyword = join.getTableKeyword();
            String mainKeyword = join.getMainKeyword();
            select.join(join.getJoinType(), schema, tableName, tableAlias, tableKeyword, mainKeyword);
        }
    }

    /**
     * 获取字段别名
     *
     * @param tableAlias
     * @param fieldName
     * @return
     */
    public static String getColumnAlias(String tableAlias, String fieldName) {
        return tableAlias + SqlHelperCons.UNDERLINE + fieldName;
    }

    /**
     * 获得新的表字段名
     *
     * @param common
     * @param schema
     * @param tableAlias
     * @param tableFieldName
     * @return
     */
    public static String getTableFieldFullName(Common common, String schema, String tableAlias, String tableFieldName) {
        String transferred = getTransferred(common);
        StringBuffer fullName = new StringBuffer();
        if (StringUtil.isNotEmpty(schema)) {
            fullName.append(schema);
            fullName.append(SqlHelperCons.POINT);
        }
        fullName.append(transferred);
        fullName.append(tableAlias);
        fullName.append(transferred);
        fullName.append(SqlHelperCons.POINT);
        fullName.append(tableFieldName);
        return fullName.toString();
    }

    /**
     * 获取处理后的where语句
     *
     * @param common
     * @param where
     * @param args
     * @return
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
            } else if ('&' == c) {
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
     */
    public static String getCondition(Common common, String where, Object bean) {
        if (where == null || bean == null) {
            return "";
        }
        String prefix = "${";
        String suffix = "}";
        StringBuffer conditionSql = new StringBuffer(where);
        int startIndex = conditionSql.indexOf(prefix);
        while (startIndex != -1) {
            int endIndex = conditionSql.indexOf(suffix, startIndex + prefix.length());
            if (endIndex != -1) {
                String name = conditionSql.substring(startIndex + prefix.length(), endIndex);
                int nextIndex = endIndex + suffix.length();
                String value = getSqlValue(common, ReflectJdkUtil.instance().get(bean.getClass(), bean, name));
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
     * 获取字段类型
     *
     * @param typeName
     * @return
     */
    public static WhatType whatType(String typeName) {
        WhatType whatType;
        switch (typeName) {
            case "java.lang.String":
            case "char":
            case "java.lang.Character":
                whatType = WhatType.STRING_TYPE;
                break;
            case "boolean":
            case "java.lang.Boolean":
                whatType = WhatType.BOOL_TYPE;
                break;
            case "byte":
            case "java.lang.Byte":
            case "short":
            case "java.lang.Short":
            case "int":
            case "java.lang.Integer":
            case "long":
            case "java.lang.Long":
            case "float":
            case "java.lang.Float":
            case "double":
            case "java.lang.Double":
                whatType = WhatType.VALUE_TYPE;
                break;
            case "java.util.Date":
            case "java.sql.Date":
            case "java.sql.Timestamp":
            case "java.sql.Time":
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
            case "java.lang.String":
            case "java.lang.Character":
            case "java.lang.Boolean":
            case "java.lang.Byte":
            case "java.lang.Short":
            case "java.lang.Integer":
            case "java.lang.Long":
            case "java.lang.Float":
            case "java.lang.Double":
            case "java.util.Date":
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
            case "java.util.Map":
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
     */
    public static String getSqlValue(Common common, Object value) {
        if (value == null) {
            return SqlHelperCons.NULL_VALUE;
        }
        String single_quotation_mark = SqlHelperCons.SINGLE_QUOTATION_MARK;
        String sqlValue = "";
        switch (whatType(value.getClass().getName())) {
            case VALUE_TYPE:
                sqlValue = value.toString();
                break;
            case BOOL_TYPE:
                sqlValue = Boolean.parseBoolean(value.toString()) == true ? "1" : "0";
                break;
            case DATE_TYPE:
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                switch (common.getSqlBeanDB().getDbType()) {
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
        DbType dbType = common.getSqlBeanDB().getDbType();
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
        if (common.getSqlBeanDB().getSqlBeanConfig().getToUpperCase() != null && common.getSqlBeanDB().getSqlBeanConfig().getToUpperCase()) {
            return true;
        }
        return false;
    }

    /**
     * 更新乐观锁版本
     *
     * @param typeName
     * @param value
     * @return
     */
    public static Object updateVersion(String typeName, Object value) {
        switch (typeName) {
            case "int":
            case "java.lang.Integer":
            case "long":
            case "java.lang.Long":
                long val = 0;
                if (value != null) {
                    val = Long.parseLong(value.toString());
                }
                value = val + 1;
                break;
            case "java.util.Date":
            case "java.sql.Timestamp":
                value = new Date();
                break;
        }
        return value;
    }

    /**
     * 乐观锁字段是否有效
     *
     * @param typeName
     * @return
     */
    public static boolean versionEffectiveness(String typeName) {
        switch (typeName) {
            case "int":
            case "java.lang.Integer":
            case "long":
            case "java.lang.Long":
            case "java.util.Date":
            case "java.sql.Timestamp":
                return true;
        }
        return false;
    }

}
