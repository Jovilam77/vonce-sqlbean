package cn.vonce.sql.orm.mapper;


import cn.vonce.sql.annotation.SqlBeanField;
import cn.vonce.sql.orm.mapper.SpringJbdcSqlBeanMapper;
import cn.vonce.sql.uitls.SqlBeanUtil;
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

    private Logger logger = LoggerFactory.getLogger(SpringJbdcSqlBeanMapper.class);

    /**
     * 基础对象映射
     *
     * @param resultSet
     * @return
     */
    public Object baseHandleResultSet(ResultSet resultSet) {
        Object object = null;
        try {
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            Object value = getValue(resultSetMetaData.getColumnTypeName(1), 1, resultSet);
            if (value == null || value.equals("null")) {
                value = getDefaultValueByColumnType(resultSetMetaData.getColumnTypeName(1));
            }
            object = value;
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("基础对象映射异常SQLException，{}", e.getMessage());
        }
        return object;
    }

    /**
     * map对象映射
     *
     * @param resultSet
     * @return
     */
    public Object mapHandleResultSet(ResultSet resultSet) {
        Map<String, Object> object = null;
        try {
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            int columns = resultSetMetaData.getColumnCount();
            Map<String, Object> map = new HashMap<>();
            for (int i = 1; i <= columns; i++) {
                Object value = getValue(resultSetMetaData.getColumnTypeName(i), i, resultSet);
                if (value == null || value.equals("null")) {
                    value = getDefaultValueByColumnType(resultSetMetaData.getColumnTypeName(i));
                }
                map.put(resultSetMetaData.getColumnLabel(i), value);
            }
            object = map;
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("map对象映射异常SQLException，{}", e.getMessage());
        }
        return object;
    }

    /**
     * bean对象映射处理
     *
     * @param resultSet
     * @param clazz
     * @return
     */
    public Object beanHandleResultSet(Class<?> clazz, ResultSet resultSet, List<String> columnNameList) {
        Object bean = null;
        try {
            bean = clazz.newInstance();
            //Object bean = ReflectAsmUtil.getInstance(clazz);
            String tableAlias = SqlBeanUtil.getTableAlias(null, clazz);
            List<Field> fieldList = SqlBeanUtil.getBeanAllField(clazz);
            for (Field field : fieldList) {
                if (Modifier.isStatic(field.getModifiers())) {
                    continue;
                }
                SqlBeanField sqlBeanField = field.getAnnotation(SqlBeanField.class);
                String fieldName = field.getName();
                if (sqlBeanField != null) {
                    fieldName = sqlBeanField.value();
                    if (sqlBeanField.isBean()) {
                        Class<?> subClazz = field.getType();
                        Object subBean = subClazz.newInstance();
                        String subTableAlias = SqlBeanUtil.getTableAlias(null, subClazz);
                        Field[] subFields = subClazz.getDeclaredFields();
                        for (Field subField : subFields) {
                            if (Modifier.isStatic(subField.getModifiers())) {
                                continue;
                            }
                            SqlBeanField subSqlBeanField = subField.getAnnotation(SqlBeanField.class);
                            String subFieldName = subField.getName();
                            if (subSqlBeanField != null) {
                                subFieldName = subSqlBeanField.value();
                            }
                            subFieldName = subTableAlias + "." + subFieldName;
                            setFieldValue(subBean, subField, subFieldName, resultSet);
                        }
                        field.setAccessible(true);
                        field.set(bean, subBean);
                        continue;
                    }
                }
                if (!columnNameList.contains(fieldName)) {
                    fieldName = tableAlias + "." + fieldName;
                }
                setFieldValue(bean, field, fieldName, resultSet);
            }

        } catch (IllegalAccessException e) {
            e.printStackTrace();
            logger.error("bean对象映射IllegalAccessException，反射操作私有字段异常：{}", e.getMessage());
        } catch (InstantiationException e) {
            e.printStackTrace();
            logger.error("bean对象映射InstantiationException，实例化对象异常：{}", e.getMessage());
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
     * @throws IllegalAccessException
     */
    public void setFieldValue(Object obj, Field field, String fieldName, ResultSet resultSet) throws IllegalAccessException {
        field.setAccessible(true);
        Object value = getValue(field.getType().getSimpleName(), fieldName, resultSet);
        if (value == null || value.equals("null")) {
            value = getDefaultValue(field.getType().getSimpleName());
        }
        field.set(obj, value);
    }

    /**
     * 获取该字段对应的值
     *
     * @param fieldType
     * @param fieldName
     * @param resultSet
     * @return
     * @author Jovi
     * @date 2018年5月15日上午9:21:04
     */
    public Object getValue(String fieldType, String fieldName, ResultSet resultSet) {
        Object value = null;
        try {
            switch (fieldType) {
                case "byte":
                case "Byte":
                case "java.lang.Byte":
                    value = resultSet.getByte(fieldName);
                    break;
                case "short":
                case "Short":
                case "java.lang.Short":
                    value = resultSet.getShort(fieldName);
                    break;
                case "int":
                case "Integer":
                case "java.lang.Integer":
                    value = resultSet.getInt(fieldName);
                    break;
                case "float":
                case "Float":
                case "java.lang.Float":
                    value = resultSet.getFloat(fieldName);
                    break;
                case "double":
                case "Double":
                case "java.lang.Double":
                    value = resultSet.getDouble(fieldName);
                    break;
                case "long":
                case "Long":
                case "java.lang.Long":
                    value = resultSet.getLong(fieldName);
                    break;
                case "boolean":
                case "Boolean":
                case "java.lang.Boolean":
                    value = resultSet.getBoolean(fieldName);
                    break;
                case "char":
                case "Character":
                case "java.lang.Character":
                case "String":
                case "java.lang.String":
                    value = resultSet.getString(fieldName) + "";
                    break;
                case "Date":
                case "java.util.Date":
                    value = resultSet.getTimestamp(fieldName);
                    break;
                case "BigDecimal":
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
     * @author Jovi
     * @date 2018年7月30日上午20:55:35
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
            case "short":
            case "int":
            case "long":
            case "float":
            case "double":
                value = 0;
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

}
