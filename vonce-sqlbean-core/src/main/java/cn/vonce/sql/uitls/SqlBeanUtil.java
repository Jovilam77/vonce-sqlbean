package cn.vonce.sql.uitls;

import cn.vonce.sql.annotation.*;
import cn.vonce.sql.bean.*;
import cn.vonce.sql.config.SqlBeanDB;
import cn.vonce.sql.constant.SqlConstant;
import cn.vonce.sql.enumerate.AlterType;
import cn.vonce.sql.enumerate.DbType;
import cn.vonce.sql.enumerate.JdbcType;
import cn.vonce.sql.enumerate.WhatType;
import cn.vonce.sql.exception.SqlBeanException;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * SqlBean 工具类 Created by Jovi on 2018/6/17.
 */
public class SqlBeanUtil {

    /**
     * 保存SqlTable缓存
     */
    private static final WeakHashMap<Class<?>, SqlTable> sqlTableMap = new WeakHashMap<>();

    /**
     * 获取SqlTable注解
     *
     * @param clazz
     * @return
     */
    public static SqlTable getSqlTable(Class<?> clazz) {
        if (sqlTableMap.containsKey(clazz)) {
            return sqlTableMap.get(clazz);
        }
        SqlTable sqlTable = clazz.getAnnotation(SqlTable.class);
        if (sqlTable == null) {
            Class<?> superClass = clazz.getSuperclass();
            if (superClass == null) {
                return null;
            }
            do {
                if (!superClass.getName().equals("java.lang.Object")) {
                    sqlTable = superClass.getAnnotation(SqlTable.class);
                    if (sqlTable != null) {
                        break;
                    }
                    superClass = superClass.getSuperclass();
                }
                if (superClass.getName().equals("java.lang.Object")) {
                    break;
                }
            } while (!superClass.getName().equals("java.lang.Object"));
        }
        sqlTableMap.put(clazz, sqlTable);
        return sqlTable;
    }

    /**
     * 校验两个SqlTable是否一致
     *
     * @param clazz1
     * @param clazz2
     * @return
     */
    public static boolean sqlTableIsConsistent(Class<?> clazz1, Class<?> clazz2) {
        SqlTable sqlTable = getSqlTable(clazz1);
        SqlTable sqlTable2 = getSqlTable(clazz2);
        if (sqlTable != null && sqlTable2 != null && sqlTable.schema().equals(sqlTable2.schema()) && sqlTable.value().equals(sqlTable2.value())) {
            return true;
        }
        return false;
    }

    /**
     * 根据类名获取表名信息
     *
     * @param clazz
     * @return
     */
    public static Table getTable(Class<?> clazz) {
        SqlTable sqlTable = getSqlTable(clazz);
        if (sqlTable != null) {
            return new Table(sqlTable.schema(), sqlTable.value(), StringUtil.isEmpty(sqlTable.alias()) ? sqlTable.value() : sqlTable.alias());
        }
        String tableName = clazz.getSimpleName();
        String tableAlias = tableName;
        if (StringUtil.isEmpty(tableAlias)) {
            tableAlias = tableName;
        }
        return new Table("", tableName, tableAlias);
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
     * 返回带转义表名,优先级 tableName第一，注解第二，类名第三
     *
     * @param table
     * @param common
     * @return
     */
    public static String getTableName(Table table, Common common) {
        String schema = table.getSchema();
        String tableName = table.getName();
        if (StringUtil.isNotEmpty(schema)) {
            tableName = schema + SqlConstant.POINT + tableName;
        }
        return SqlBeanUtil.isToUpperCase(common) ? tableName.toUpperCase() : tableName;
    }


    /**
     * 获取Bean字段中实际对于的表字段
     *
     * @param common
     * @param field
     * @param sqlTable
     * @return
     */
    public static String getTableFieldName(Common common, Field field, SqlTable sqlTable) {
        return getTableFieldName(common, getTableFieldName(field, sqlTable));
    }

    /**
     * 获取Bean字段中实际对于的表字段
     *
     * @param common
     * @param name
     * @return
     */
    public static String getTableFieldName(Common common, String name) {
        if (SqlBeanUtil.isToUpperCase(common)) {
            name = name.toUpperCase();
        }
        String transferred = SqlBeanUtil.getTransferred(common);
        return transferred + name + transferred;
    }

    /**
     * 获取Bean字段中实际对于的表字段
     *
     * @param field
     * @param sqlTable
     * @return
     */
    public static String getTableFieldName(Field field, SqlTable sqlTable) {
        SqlColumn sqlColumn = field.getAnnotation(SqlColumn.class);
        String name = field.getName();
        if (sqlColumn != null && StringUtil.isNotBlank(sqlColumn.value())) {
            name = sqlColumn.value();
        } else {
            if (sqlTable == null || sqlTable.mapUsToCc()) {
                name = StringUtil.humpToUnderline(name);
            }
        }
        return name;
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
        String transferred = getTransferred(common);
        StringBuffer fullName = new StringBuffer();
        fullName.append(transferred);
        fullName.append(tableAlias);
        fullName.append(transferred);
        fullName.append(SqlConstant.POINT);
        fullName.append(transferred);
        fullName.append(SqlBeanUtil.isToUpperCase(common) ? tableFieldName.toUpperCase() : tableFieldName);
        fullName.append(transferred);
        return fullName.toString();
    }

    /**
     * 获得新的表字段名
     *
     * @param common
     * @param tableAlias
     * @param field
     * @param sqlTable
     * @return
     */
    public static String getTableFieldFullName(Common common, String tableAlias, Field field, SqlTable sqlTable) {
        return getTableFieldFullName(common, tableAlias, getTableFieldName(field, sqlTable));
    }

    /**
     * 返回from的表名包括别名
     *
     * @param schema
     * @param tableName
     * @param tableAlias
     * @param common
     * @return
     */
    public static String fromFullName(String schema, String tableName, String tableAlias, Common common) {
        String transferred = SqlBeanUtil.getTransferred(common);
        StringBuffer fromSql = new StringBuffer();
        if (SqlBeanUtil.isToUpperCase(common)) {
            tableName = tableName.toUpperCase();
            schema = schema.toUpperCase();
        }
        if (StringUtil.isNotEmpty(schema)) {
            fromSql.append(schema);
            fromSql.append(SqlConstant.POINT);
        }
        fromSql.append(tableName);
        fromSql.append(SqlConstant.SPACES);
        fromSql.append(transferred);
        fromSql.append(tableAlias);
        fromSql.append(transferred);
        return fromSql.toString();
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
     * 获取列信息
     *
     * @param common
     * @param field
     * @return
     */
    public static ColumnInfo getColumnInfo(Common common, Field field) {
        return getColumnInfo(common, field, field.getDeclaringClass().getAnnotation(SqlTable.class), field.getAnnotation(SqlColumn.class));
    }

    /**
     * 获取列信息
     *
     * @param common
     * @param field
     * @param sqlTable
     * @param sqlColumn
     * @return
     */
    public static ColumnInfo getColumnInfo(Common common, Field field, SqlTable sqlTable, SqlColumn sqlColumn) {
        String columnName = SqlBeanUtil.getTableFieldName(field, sqlTable);
        ColumnInfo columnInfo = new ColumnInfo();
        columnInfo.setName(SqlBeanUtil.isToUpperCase(common) ? columnName.toUpperCase() : columnName);
        columnInfo.setPk(field.isAnnotationPresent(SqlId.class));
        JdbcType jdbcType;
        if (sqlColumn != null && sqlColumn.type() != JdbcType.NOTHING) {
            jdbcType = sqlColumn.type();
        } else {
            jdbcType = JdbcType.getType(common.getSqlBeanDB().getDbType(), field);
        }
        columnInfo.setType(jdbcType.name());
        if (sqlColumn != null) {
            if (columnInfo.getPk()) {
                columnInfo.setNotnull(true);
            } else {
                columnInfo.setNotnull(sqlColumn.notNull());
            }
        }
        if (sqlColumn != null && sqlColumn.length() != 0) {
            columnInfo.setLength(sqlColumn.length());
            columnInfo.setScale(sqlColumn.scale());
        } else {
            columnInfo.setLength(jdbcType.getLength() > 0 ? jdbcType.getLength() : null);
        }
        if (sqlColumn != null && sqlColumn.scale() != 0) {
            columnInfo.setScale(sqlColumn.scale());
        } else {
            columnInfo.setScale(jdbcType.getScale() > 0 ? jdbcType.getScale() : null);
        }
        if (sqlColumn != null && StringUtil.isNotEmpty(sqlColumn.def())) {
            columnInfo.setDfltValue(sqlColumn.def());
        }
        if (sqlColumn != null && StringUtil.isNotEmpty(sqlColumn.remarks())) {
            columnInfo.setRemarks(sqlColumn.remarks());
        } else {
            columnInfo.setRemarks("");
        }
        return columnInfo;
    }

    /**
     * 比较两个字段信息是否一致
     *
     * @param sqlBeanDB
     * @param columnInfo
     * @param toColumnInfo
     * @return
     */
    public static boolean columnInfoCompare(SqlBeanDB sqlBeanDB, ColumnInfo columnInfo, ColumnInfo toColumnInfo) {
        if (!columnInfo.getPk().equals(toColumnInfo.getPk())) {
            return false;
        }
        if (!columnInfo.getName().equals(toColumnInfo.getName())) {
            return false;
        }
        if (!columnInfo.getType().equalsIgnoreCase(toColumnInfo.getType())) {
            return false;
        }
        if (columnInfo.getNotnull() != null && !columnInfo.getNotnull().equals(toColumnInfo.getNotnull())) {
            return false;
        }
        if ((columnInfo.getDfltValue() == null && columnInfo.getDfltValue() != toColumnInfo.getDfltValue()) || (columnInfo.getDfltValue() != null && toColumnInfo.getDfltValue() != null && !columnInfo.getDfltValue().equals(toColumnInfo.getDfltValue()))) {
            return false;
        }
        if (columnInfo.getLength() != null && !columnInfo.getLength().equals(toColumnInfo.getLength())) {
            return false;
        }
        if (columnInfo.getScale() != null && !columnInfo.getScale().equals(toColumnInfo.getScale())) {
            return false;
        }
        if (sqlBeanDB.getDbType() != DbType.SQLite && sqlBeanDB.getDbType() != DbType.Derby) {
            if (StringUtil.isNotBlank(columnInfo.getRemarks()) && !columnInfo.getRemarks().equals(toColumnInfo.getRemarks())) {
                return false;
            }
        }
        return true;
    }

    /**
     * 是否使用逻辑删除
     *
     * @param clazz
     * @return
     */
    public static boolean checkLogically(Class<?> clazz) {
        List<Field> fieldList = getBeanAllField(clazz);
        for (Field field : fieldList) {
            SqlLogically sqlLogically = field.getAnnotation(SqlLogically.class);
            if (sqlLogically != null) {
                return true;
            }
        }
        return false;
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
        if (Modifier.isStatic(field.getModifiers())) {
            return true;
        }
        SqlJoin sqlJoin = field.getAnnotation(SqlJoin.class);
        if (sqlJoin != null) {
            return true;
        }
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
     * 获取该bean所有字段（包括父类）
     *
     * @param clazz
     * @return
     */
    public static List<Field> getBeanAllField(Class<?> clazz) {
        List<Field> fieldList = new ArrayList<>();
        Class<?> superClass = clazz.getSuperclass();
        do {
            if (!superClass.getName().equals("java.lang.Object")) {
                fieldList.addAll(Arrays.asList(superClass.getDeclaredFields()));
                superClass = superClass.getSuperclass();
            }
            if (superClass.getName().equals("java.lang.Object")) {
                break;
            }
        } while (!superClass.getName().equals("java.lang.Object"));
        fieldList.addAll(Arrays.asList(clazz.getDeclaredFields()));
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
            if (sqlColumn != null && sqlColumn.value().equals(tableFieldName) || StringUtil.humpToUnderline(field.getName()).equals(tableFieldName) || field.getName().equals(tableFieldName)) {
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
        SqlTable sqlTable = getSqlTable(clazz);
        Table table = getTable(clazz);
        String tableAlias = table.getAlias();
        List<Field> fieldList = getBeanAllField(clazz);
        for (Field field : fieldList) {
            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }
            SqlColumn sqlColumn = field.getAnnotation(SqlColumn.class);
            if (sqlColumn != null && sqlColumn.ignore()) {
                continue;
            }
            if (isFilter(filterTableFields, getTableFieldName(field, sqlTable))) {
                continue;
            }
            SqlJoin sqlJoin = field.getAnnotation(SqlJoin.class);
            if (sqlJoin != null && sqlJoin.isBean()) {
                Class<?> subBeanClazz = field.getType();
                if (sqlJoin.from() != null) {
                    subBeanClazz = sqlJoin.from();
                }
                SqlTable subSqlTable = getSqlTable(subBeanClazz);
                //如果有指定查询的字段
                List<Field> subBeanFieldList = getBeanAllField(subBeanClazz);
                if (sqlJoin.value().length > 0 && !sqlJoin.value()[0].equals("")) {
                    for (String fieldName : sqlJoin.value()) {
                        Field javaField = getFieldByTableFieldName(subBeanFieldList, fieldName);
                        if (javaField == null) {
                            throw new SqlBeanException("该表连接查询的字段未与java字段关联：" + subBeanClazz.getName() + "类中找不到" + fieldName);
                        }
                        //表名、别名优先从@SqlBeanJoin注解中取，如果不存在则从类注解中取，再其次是类名
                        Table subTable = getTable(subBeanClazz, sqlJoin);
                        columnSet.add(new Column(subSqlTable == null ? "" : subTable.getAlias(), fieldName, getColumnAlias(subSqlTable == null ? "" : subTable.getAlias(), javaField.getName())));
                    }
                }
                //没有指定查询的字段则查询所有字段
                else {
                    //表名、别名优先从@SqlBeanJoin注解中取，如果不存在则从类注解中取，再其次是类名
                    Table subTable = getTable(subBeanClazz, sqlJoin);
                    for (Field subBeanField : subBeanFieldList) {
                        if (Modifier.isStatic(subBeanField.getModifiers())) {
                            continue;
                        }
                        SqlColumn subSqlColumn = subBeanField.getAnnotation(SqlColumn.class);
                        if (subSqlColumn != null && subSqlColumn.ignore()) {
                            continue;
                        }
                        columnSet.add(new Column(subTable.getAlias(), getTableFieldName(subBeanField, subSqlTable), getColumnAlias(subTable.getAlias(), subBeanField.getName())));
                    }
                }
            } else if (sqlJoin != null) {
                //获取SqlBeanJoin 注解中的查询字段
                String tableFieldName = sqlJoin.value()[0];
                if (StringUtil.isEmpty(tableFieldName)) {
                    throw new SqlBeanException("需指定连接查询的字段：@SqlJoin > value = {“xxx”}");
                }
                //可能会连同一个表，但连接条件不一样（这时表需要区分别名），所以查询的字段可能是同一个，但属于不同表别名下，所以用java字段名当sql字段别名不会出错
                String subTableAlias = StringUtil.isEmpty(sqlJoin.tableAlias()) ? sqlJoin.table() : sqlJoin.tableAlias();
                columnSet.add(new Column(subTableAlias, tableFieldName, getColumnAlias(subTableAlias, field.getName())));
            } else {
                columnSet.add(new Column(tableAlias, getTableFieldName(field, sqlTable), field.getName()));
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
    public static Map<String, Join> getJoin(Select select, Class<?> clazz) throws SqlBeanException, InstantiationException, IllegalAccessException {
        SqlTable sqlTable = getSqlTable(clazz);
        List<Field> fieldList = getBeanAllField(clazz);
        Map<String, Join> joinFieldMap = new HashMap<>();
        for (Field field : fieldList) {
            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }
            SqlJoin sqlJoin = field.getAnnotation(SqlJoin.class);
            Join join = new Join();
            if (sqlJoin != null && !sqlJoin.isBean()) {
                join.setJoinType(sqlJoin.type());
                join.setSchema(sqlJoin.schema());
                join.setTableName(sqlJoin.table());
                join.setTableAlias(StringUtil.isEmpty(sqlJoin.tableAlias()) ? sqlJoin.table() : sqlJoin.tableAlias());
                if (sqlJoin.on() != null && sqlJoin.on() != void.class) {
                    JoinOn joinOn = (JoinOn) sqlJoin.on().newInstance();
                    joinOn.on(join);
                } else {
                    join.on(SqlBeanUtil.getTableFieldFullName(select, join.getTableAlias(), sqlJoin.tableKeyword()), new Original(SqlBeanUtil.getTableFieldFullName(select, select.getTable().getAlias(), sqlJoin.mainKeyword())));
                }
                //key是唯一的，作用是为了去重复，因为可能连接相同的表取不同的字段，但连接相同的表，连接条件不同是可以允许的
                joinFieldMap.put(sqlJoin.table().toLowerCase() + sqlJoin.tableKeyword().toLowerCase() + sqlJoin.mainKeyword().toLowerCase(), join);
            } else if (sqlJoin != null && sqlJoin.isBean()) {
                Class<?> subClazz = field.getType();
                if (sqlJoin.from() != null) {
                    subClazz = sqlJoin.from();
                }
                //表名、别名优先从@SqlBeanJoin注解中取，如果不存在则从类注解中取，再其次是类名
                Table table = getTable(subClazz, sqlJoin);
                String tableKeyword = getTableFieldName(getIdField(subClazz), sqlTable);
                join.setJoinType(sqlJoin.type());
                join.setSchema(table.getSchema());
                join.setTableName(table.getName());
                join.setTableAlias(table.getAlias());
                if (sqlJoin.on() != null && sqlJoin.on() != void.class) {
                    JoinOn joinOn = (JoinOn) sqlJoin.on().newInstance();
                    joinOn.on(join);
                } else {
                    join.on(SqlBeanUtil.getTableFieldFullName(select, join.getTableAlias(), tableKeyword), new Original(SqlBeanUtil.getTableFieldFullName(select, select.getTable().getAlias(), sqlJoin.mainKeyword())));
                }
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
    public static void setJoin(Select select, Class<?> clazz) throws SqlBeanException, InstantiationException, IllegalAccessException {
        Map<String, Join> joinFieldMap = getJoin(select, clazz);
        for (Join join : joinFieldMap.values()) {
            String schema = join.getSchema();
            String tableName = join.getTableName();
            String tableAlias = join.getTableAlias();
            //过时暂时兼容
            String on = join.getOn();
            if (StringUtil.isNotEmpty(on)) {
                select.join(join.getJoinType(), schema, tableName, tableAlias, on);
            } else {
                select.getJoin().add(join);
//                String tableKeyword = join.getTableKeyword();
//                String mainKeyword = join.getMainKeyword();
//                select.join(join.getJoinType(), schema, tableName, tableAlias, tableKeyword, mainKeyword);
            }
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
        return tableAlias + SqlConstant.UNDERLINE + fieldName;
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
                        value.append(getOriginal(common, objects[i]));
                        value.append(SqlConstant.COMMA);
                    }
                    value.delete(value.length() - SqlConstant.COMMA.length(), value.length());
                }
                conditionSql.append(value);
                index++;
            } else if ('&' == c) {
                conditionSql.append(getColumn(common, args[index]));
                index++;
            } else {
                conditionSql.append(c);
            }
        }
        return conditionSql.toString();
    }

    /**
     * 获取列
     *
     * @param common
     * @param columnObject
     * @return
     */
    public static String getColumn(Common common, Object columnObject) {
        StringBuffer value = new StringBuffer();
        Column column = null;
        if (columnObject instanceof Column) {
            column = (Column) columnObject;
        }
        if (column != null) {
            String transferred = getTransferred(common);
            if (StringUtil.isNotEmpty(column.getTableAlias())) {
                value.append(transferred);
                value.append(column.getTableAlias());
                value.append(transferred);
                value.append(SqlConstant.POINT);
            }
            value.append(column.getName());
        } else {
            value.append(columnObject);
        }
        return value.toString();
    }

    /**
     * 获取实际类型值
     *
     * @param common
     * @param value
     * @return
     */
    public static Object getOriginal(Common common, Object value) {
        if (value instanceof Column) {
            Column column = (Column) value;
            return SqlBeanUtil.getTableFieldFullName(common, column.getTableAlias(), column.getName());
        } else if (value instanceof Original) {
            Original original = (Original) value;
            return original.getValue();
        }
        return getSqlValue(common, value);
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
     * 获取字段类型
     *
     * @param jdbcType
     * @return
     */
    public static WhatType whatType(JdbcType jdbcType) {
        WhatType whatType;
        switch (jdbcType) {
            case CHAR:
            case NCHAR:
            case VARCHAR:
            case VARCHAR2:
            case NVARCHAR:
            case TINYTEXT:
            case TEXT:
            case NTEXT:
            case LONGTEXT:
            case CLOB:
            case NCLOB:
                whatType = WhatType.STRING_TYPE;
                break;
            case BIT:
                whatType = WhatType.BOOL_TYPE;
                break;
            case TINYINT:
            case SMALLINT:
            case INTEGER:
            case BIGINT:
            case LONG:
            case FLOAT:
            case DOUBLE:
            case DECIMAL:
            case NUMERIC:
            case MONEY:
            case SMALLMONEY:
                whatType = WhatType.VALUE_TYPE;
                break;
            case DATE:
            case TIME:
            case DATETIME:
            case DATETIME2:
            case TIMESTAMP:
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
            case "char":
            case "java.lang.Character":
            case "boolean":
            case "java.lang.Boolean":
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
        return getSqlValue(common, value, null);
    }

    /**
     * 获取sql实际值(过滤sql注入)
     *
     * @param value
     * @return
     */
    public static String getSqlValue(Common common, Object value, JdbcType jdbcType) {
        if (value == null) {
            return SqlConstant.NULL_VALUE;
        }
        String single_quotation_mark = SqlConstant.SINGLE_QUOTATION_MARK;
        String sqlValue;
        WhatType whatType;
        if (jdbcType == null) {
            whatType = whatType(value.getClass().getName());
        } else {
            whatType = whatType(jdbcType);
        }
        switch (whatType) {
            case VALUE_TYPE:
                sqlValue = value.toString();
                break;
            case BOOL_TYPE:
                if (jdbcType != null) {
                    sqlValue = (String) value;
                } else {
                    DbType dbType = common.getSqlBeanDB().getDbType();
                    if (dbType == DbType.Postgresql) {
                        sqlValue = Boolean.parseBoolean(value.toString()) == true ? "'1'" : "'0'";
                    } else if (dbType == DbType.H2 || dbType == DbType.Hsql) {
                        sqlValue = Boolean.parseBoolean(value.toString()) == true ? "true" : "false";
                    } else {
                        sqlValue = Boolean.parseBoolean(value.toString()) == true ? "1" : "0";
                    }
                }
                break;
            case DATE_TYPE:
                String dateString;
                if (jdbcType != null) {
                    dateString = (String) value;
                } else {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                    dateString = sdf.format(value);
                }
                switch (common.getSqlBeanDB().getDbType()) {
                    case Oracle:
                        sqlValue = "to_timestamp(" + single_quotation_mark + dateString + single_quotation_mark + ", 'syyyy-mm-dd hh24:mi:ss.ff')";
                        break;
                    default:
                        sqlValue = single_quotation_mark + dateString + single_quotation_mark;
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
     * 是否使用分页
     *
     * @param select
     * @return
     */
    public static boolean isUsePage(Select select) {
        if (select.getPage() != null && select.getPage().getPagenum() != null && select.getPage().getPagesize() != null) {
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
        String transferred = SqlConstant.DOUBLE_ESCAPE_CHARACTER;
        DbType dbType = common.getSqlBeanDB().getDbType();
        if (dbType == DbType.MySQL || dbType == DbType.MariaDB) {
            transferred = SqlConstant.SINGLE_ESCAPE_CHARACTER;
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

    /**
     * 获取object数组
     *
     * @param bean
     * @return
     */
    public static Object[] getObjectArray(Object bean) {
        Object[] objects;
        if (bean.getClass().isArray()) {
            objects = (Object[]) bean;
        } else if (bean instanceof Collection) {
            Collection<Object> list = (Collection<Object>) bean;
            objects = list.toArray();
        } else {
            objects = new Object[]{bean};
        }
        return objects;
    }

    /**
     * 获取类型的默认值（仅支持基本类型、String、Date、Timestamp、BigDecimal）
     *
     * @param typeName
     * @return
     */
    public static Object getDefaultValue(String typeName) {
        switch (typeName) {
            case "java.lang.Byte":
            case "java.lang.Short":
            case "java.lang.Integer":
                return 0;
            case "java.lang.Long":
                return 0l;
            case "java.lang.Float":
                return 0f;
            case "java.lang.Double":
                return 0d;
            case "java.lang.Character":
                return '\u0000';
            case "java.lang.Boolean":
                return false;
            case "java.lang.String":
                return "";
            case "java.util.Date":
                return new Date();
            case "java.sql.Timestamp":
                return new Timestamp(System.currentTimeMillis());
            case "java.math.BigDecimal":
                return new BigDecimal(0);
        }
        return null;
    }

    /**
     * 增加一列
     *
     * @param common
     * @param columnInfo
     * @return
     */
    public static String addColumn(Common common, ColumnInfo columnInfo, String afterColumnName) {
        StringBuffer sql = new StringBuffer();
        JdbcType jdbcType = JdbcType.getType(columnInfo.getType());
        sql.append(getTableFieldName(common, columnInfo.getName()));
        sql.append(SqlConstant.SPACES);
        sql.append(jdbcType.name());
        if (columnInfo.getLength() != null && columnInfo.getLength() > 0) {
            sql.append(SqlConstant.BEGIN_BRACKET);
            //字段长度
            sql.append(columnInfo.getLength());
            if (jdbcType.isFloat()) {
                sql.append(SqlConstant.COMMA);
                sql.append(columnInfo.getScale() == null ? 0 : columnInfo.getScale());
            }
            sql.append(SqlConstant.END_BRACKET);
        }
        //是否为null
        if ((columnInfo.getNotnull() != null && columnInfo.getNotnull()) || columnInfo.getPk()) {
            sql.append(SqlConstant.SPACES);
            sql.append(SqlConstant.NOT_NULL);
        } else if (common instanceof Alter) {
            Alter alter = (Alter) common;
            if (alter.getType() == AlterType.MODIFY && columnInfo.getNotnull() != null && !columnInfo.getNotnull()) {
                sql.append(SqlConstant.SPACES);
                sql.append(SqlConstant.NULL);
            }
        }
        //默认值
        if (StringUtil.isNotEmpty(columnInfo.getDfltValue())) {
            sql.append(SqlConstant.SPACES);
            sql.append(SqlConstant.DEFAULT);
            sql.append(SqlConstant.SPACES);
            sql.append(SqlBeanUtil.getSqlValue(common, columnInfo.getDfltValue(), jdbcType));
        }
        //如果是Mysql或MariaDB
        if (common.getSqlBeanDB().getDbType() == DbType.MySQL || common.getSqlBeanDB().getDbType() == DbType.MariaDB) {
            //存在备注
            if (StringUtil.isNotBlank(columnInfo.getRemarks())) {
                sql.append(SqlConstant.SPACES);
                sql.append(SqlConstant.COMMENT);
                sql.append(SqlConstant.SPACES);
                sql.append(SqlConstant.SINGLE_QUOTATION_MARK);
                sql.append(columnInfo.getRemarks());
                sql.append(SqlConstant.SINGLE_QUOTATION_MARK);
            }
            //存在排序
            if (StringUtil.isNotBlank(afterColumnName)) {
                String transferred = SqlBeanUtil.getTransferred(common);
                sql.append(SqlConstant.SPACES);
                sql.append(SqlConstant.AFTER);
                sql.append(SqlConstant.SPACES);
                sql.append(transferred);
                sql.append(afterColumnName);
                sql.append(transferred);
            }
        }
        return sql.toString();
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
     * 检查
     */
    public static void check(Common common) {
        isNull(common.getSqlBeanDB(), "请设置sqlBeanConfig");
        isNull(common.getSqlBeanDB().getDbType(), "请设置sqlBeanConfig -> dbType");
    }

}
