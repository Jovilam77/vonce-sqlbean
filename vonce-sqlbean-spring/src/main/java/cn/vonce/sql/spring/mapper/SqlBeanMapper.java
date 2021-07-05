package cn.vonce.sql.spring.mapper;


import cn.vonce.sql.annotation.SqlJoin;
import cn.vonce.sql.constant.SqlConstant;
import cn.vonce.sql.uitls.ReflectUtil;
import cn.vonce.sql.uitls.SqlBeanUtil;
import cn.vonce.sql.uitls.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * SqlBean 结果映射
 */
public class SqlBeanMapper {

    private Logger logger = LoggerFactory.getLogger(SqlBeanMapper.class);

    /**
     * 基础对象映射
     *
     * @param resultSet
     * @return
     */
    public Object baseHandleResultSet(ResultSet resultSet) {
        Object value = null;
        try {
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            value = getValue(resultSetMetaData.getColumnTypeName(1), 1, resultSet);
            if (value == null || value.equals("null")) {
                value = getDefaultValueByColumnType(resultSetMetaData.getColumnTypeName(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("基础对象映射异常SQLException，{}", e.getMessage());
        }
        return value;
    }

    /**
     * map对象映射
     *
     * @param resultSet
     * @return
     */
    public Object mapHandleResultSet(ResultSet resultSet) {
        Map<String, Object> map = null;
        try {
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            int columns = resultSetMetaData.getColumnCount();
            map = new HashMap<>();
            for (int i = 1; i <= columns; i++) {
                Object value = getValue(resultSetMetaData.getColumnTypeName(i), i, resultSet);
                if (value == null || value.equals("null")) {
                    value = getDefaultValueByColumnType(resultSetMetaData.getColumnTypeName(i));
                }
                map.put(resultSetMetaData.getColumnLabel(i), value);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("map对象映射异常SQLException，{}", e.getMessage());
        }
        return map;
    }

    /**
     * bean对象映射处理
     *
     * @param resultSet
     * @param clazz
     * @return
     */
    public Object beanHandleResultSet(Class<?> clazz, ResultSet resultSet, List<String> columnNameList) {
        Object bean = ReflectUtil.instance().newObject(clazz);
        String tableAlias = SqlBeanUtil.getTable(clazz).getAlias();
        List<Field> fieldList = SqlBeanUtil.getBeanAllField(clazz);
        for (Field field : fieldList) {
            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }
            SqlJoin sqlJoin = field.getAnnotation(SqlJoin.class);
            String fieldName = field.getName();
            if (sqlJoin != null) {
                if (sqlJoin.isBean()) {
                    Class<?> subClazz = field.getType();
                    Object subBean = ReflectUtil.instance().newObject(subClazz);
                    //获取表的别名，先是获取别名，获取不到就会获取表名
                    String subTableAlias = SqlBeanUtil.getTable(subClazz).getAlias();
                    //如果在SqlBeanJoin中设置了表名，那么优先使用该表名，如果有多个联表查询的对象需要连接同一张表的，那么需要保证表名一致
                    if (StringUtil.isNotEmpty(sqlJoin.table())) {
                        subTableAlias = sqlJoin.table();
                    }
                    //如果在SqlBeanJoin中设置了别名，那么优先使用该别名，如果有多个联表查询的对象需要连接同一张表的，那么需要保证别名一致
                    if (StringUtil.isNotEmpty(sqlJoin.tableAlias())) {
                        subTableAlias = sqlJoin.tableAlias();
                    }
                    Field[] subFields = subClazz.getDeclaredFields();
                    for (Field subField : subFields) {
                        if (Modifier.isStatic(subField.getModifiers())) {
                            continue;
                        }
                        String subFieldName = subField.getName();
                        subFieldName = subTableAlias + SqlConstant.UNDERLINE + subFieldName;
                        setFieldValue(subBean, subField, subFieldName, resultSet);
                    }
                    ReflectUtil.instance().set(bean.getClass(), bean, fieldName, subBean);
                    continue;
                } else {
                    if (!columnNameList.contains(fieldName)) {
                        String subTableAlias = sqlJoin.table();
                        if (StringUtil.isNotEmpty(sqlJoin.tableAlias())) {
                            subTableAlias = sqlJoin.tableAlias();
                        }
                        fieldName = subTableAlias + SqlConstant.UNDERLINE + fieldName;
                    }
                    setFieldValue(bean, field, fieldName, resultSet);
                }
            } else {
                //优先使用 表别名+字段名才方式匹配
                String newFieldName = tableAlias + SqlConstant.UNDERLINE + fieldName;
                if (!columnNameList.contains(newFieldName)) {
                    //其次通过驼峰转下划线方式匹配
                    newFieldName = StringUtil.humpToUnderline(fieldName);
                    if (!columnNameList.contains(newFieldName)) {
                        //再其次通过字段名匹配
                        newFieldName = fieldName;
                    }
                }
                setFieldValue(bean, field, newFieldName, resultSet);
            }
        }
        return bean;
    }

    /**
     * 获取所有列名
     *
     * @param resultSet
     * @return
     * @throws SQLException
     */
    public List<String> getColumnNameList(ResultSet resultSet) {
        List<String> columnNameList = new ArrayList<>();
        try {
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            int columns = resultSetMetaData.getColumnCount();
            if (columnNameList.size() == 0) {
                for (int i = 1; i <= columns; i++) {
                    columnNameList.add(resultSetMetaData.getColumnLabel(i));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("bean对象映射SQLException，Sql语句执行异常：{}", e.getMessage());
        }
        return columnNameList;
    }

    /**
     * 字段赋值
     *
     * @param obj
     * @param field
     * @param fieldName
     * @param resultSet
     */
    public void setFieldValue(Object obj, Field field, String fieldName, ResultSet resultSet) {
        Object value = getValue(field.getType().getName(), fieldName, resultSet);
        if (value == null || value.equals("null")) {
            value = getDefaultValue(field.getType().getName());
        }
        ReflectUtil.instance().set(obj.getClass(), obj, field.getName(), value);
    }

    /**
     * 获取该字段对应的值
     *
     * @param fieldType
     * @param fieldName
     * @param resultSet
     * @return
     */
    public Object getValue(String fieldType, String fieldName, ResultSet resultSet) {
        Object value = null;
        try {
            switch (fieldType) {
                case "byte":
                    value = resultSet.getByte(fieldName);
                    break;
                case "java.lang.Byte":
                    value = resultSet.getObject(fieldName);
                    if (value != null) {
                        value = resultSet.getByte(fieldName);
                    }
                    break;
                case "short":
                    value = resultSet.getShort(fieldName);
                    break;
                case "java.lang.Short":
                    value = resultSet.getObject(fieldName);
                    if (value != null) {
                        value = resultSet.getShort(fieldName);
                    }
                    break;
                case "int":
                    value = resultSet.getInt(fieldName);
                    break;
                case "java.lang.Integer":
                    value = resultSet.getObject(fieldName);
                    if (value != null) {
                        value = resultSet.getInt(fieldName);
                    }
                    break;
                case "float":
                    value = resultSet.getFloat(fieldName);
                    break;
                case "java.lang.Float":
                    value = resultSet.getObject(fieldName);
                    if (value != null) {
                        value = resultSet.getFloat(fieldName);
                    }
                    break;
                case "double":
                    value = resultSet.getDouble(fieldName);
                    break;
                case "long":
                    value = resultSet.getLong(fieldName);
                    break;
                case "boolean":
                    value = resultSet.getBoolean(fieldName);
                    break;
                case "char":
                case "java.lang.Character":
                    value = resultSet.getString(fieldName);
                    if (StringUtil.isNotEmpty(value)) {
                        value = value.toString().charAt(0);
                    }
                    break;
                case "java.lang.String":
                    value = resultSet.getString(fieldName);
                    break;
                case "java.sql.Date":
                    value = resultSet.getDate(fieldName);
                    break;
                case "java.sql.Time":
                    value = resultSet.getTime(fieldName);
                    break;
                case "java.util.Date":
                case "java.sql.Timestamp":
                    value = resultSet.getTimestamp(fieldName);
                    break;
                case "java.math.BigDecimal":
                    value = resultSet.getBigDecimal(fieldName);
                    break;
                default:
                    value = resultSet.getObject(fieldName);
                    break;
            }
        } catch (SQLException e) {
            //logger.warn(e.getMessage());
        }
        return value;

    }

    /**
     * 获取该字段对应的值
     *
     * @param jdbcType
     * @param index
     * @param resultSet
     * @return
     */
    public Object getValue(String jdbcType, int index, ResultSet resultSet) {
        Object value = null;
        try {
            switch (jdbcType) {
                case "TINYINT":
                    value = resultSet.getByte(index);
                    break;
                case "SMALLINT":
                    value = resultSet.getShort(index);
                    break;
                case "INTEGER":
                    value = resultSet.getInt(index);
                    break;
                case "BIGINT":
                    value = resultSet.getLong(index);
                    break;
                case "REAL":
                    value = resultSet.getFloat(index);
                    break;
                case "FLOAT":
                case "DOUBLE":
                    value = resultSet.getDouble(index);
                    break;
                case "BIT":
                case "BOOLEAN":
                    value = resultSet.getBoolean(index);
                    break;
                case "CHAR":
                case "VARCHAR":
                case "LONGVARCHAR":
                    value = resultSet.getString(index);
                    break;
                case "NUMERIC":
                case "DECIMAL":
                    value = resultSet.getBigDecimal(index);
                    break;
                default:
                    value = resultSet.getObject(index);
                    break;
            }
        } catch (SQLException e) {
            //logger.warn(e.getMessage());
        }
        return value;

    }

    /**
     * 获取基本类型默认值
     *
     * @param typeName
     * @return
     */
    public static Object getDefaultValue(String typeName) {
        Object value = null;
        switch (typeName) {
            case "byte":
                value = new Byte("0");
                break;
            case "short":
                value = new Short("0");
                break;
            case "int":
                value = 0;
                break;
            case "long":
                value = 0L;
                break;
            case "float":
                value = 0F;
                break;
            case "double":
                value = 0D;
                break;
            case "char":
                value = '\u0000';
                break;
            case "boolean":
                value = false;
                break;
        }
        return value;
    }

    /**
     * 获取基本类型默认值
     *
     * @param typeName
     * @return
     */
    public static Object getDefaultValueByColumnType(String typeName) {
        Object value = null;
        switch (typeName) {
            case "BIGINT":
            case "INTEGER":
            case "TINYINT":
            case "SMALLINT":
                value = 0;
                break;
            case "FLOAT":
                value = 0f;
                break;
            case "DOUBLE":
            case "NUMERIC":
                value = 0d;
                break;
            case "BIT":
                value = false;
                break;
            case "CHAR":
            case "VARCHAR":
            case "LONGVARCHAR":
                value = null;
                break;
            default:
                value = null;
                break;
        }
        return value;
    }

    /**
     * 获取转换后的值
     *
     * @param typeName
     * @param value
     * @return
     */
    public static Object getValueConvert(String typeName, Object value) {
        Object newValue = value;
        switch (typeName) {
            case "byte":
            case "java.lang.Byte":
                newValue = new Byte(value.toString());
                break;
            case "short":
            case "java.lang.Short":
                newValue = new Short(value.toString());
                break;
            case "int":
            case "java.lang.Integer":
                newValue = new Integer(value.toString());
                break;
            case "long":
            case "java.lang.Long":
                newValue = new Long(value.toString());
                break;
            case "float":
            case "java.lang.Float":
                newValue = new Float(value.toString());
                break;
            case "double":
            case "java.lang.Double":
                newValue = new Double(value.toString());
                break;
        }
        return newValue;
    }


}
