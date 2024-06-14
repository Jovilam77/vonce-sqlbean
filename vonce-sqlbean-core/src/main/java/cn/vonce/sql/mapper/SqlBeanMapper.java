package cn.vonce.sql.mapper;


import cn.vonce.sql.uitls.DateUtil;
import cn.vonce.sql.uitls.StringUtil;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;

/**
 * SqlBean 结果映射
 */
public class SqlBeanMapper extends BaseMapper<ResultSet> {

    /**
     * 获取所有列名
     *
     * @param resultSetDelegate
     * @return
     * @throws SQLException
     */
    @Override
    public List<String> getColumnNameList(ResultSetDelegate<ResultSet> resultSetDelegate) {
        ResultSet resultSet = resultSetDelegate.getDelegate();
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
        }
        return columnNameList;
    }

    @Override
    public Object baseHandleResultSet(ResultSetDelegate<ResultSet> resultSetDelegate) {
        ResultSet resultSet = resultSetDelegate.getDelegate();
        Object value = null;
        try {
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            value = getValue(resultSetMetaData.getColumnTypeName(1), 1, resultSetDelegate);
            if (value == null || value.equals("null")) {
                value = getDefaultValueByColumnType(resultSetMetaData.getColumnTypeName(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return value;
    }

    @Override
    public Object mapHandleResultSet(ResultSetDelegate<ResultSet> resultSetDelegate) {
        ResultSet resultSet = resultSetDelegate.getDelegate();
        Map<String, Object> map = null;
        try {
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            int columns = resultSetMetaData.getColumnCount();
            map = new HashMap<>();
            for (int i = 1; i <= columns; i++) {
                Object value = getValue(resultSetMetaData.getColumnTypeName(i), i, resultSetDelegate);
                if (value == null || value.equals("null")) {
                    value = getDefaultValueByColumnType(resultSetMetaData.getColumnTypeName(i));
                }
                map.put(resultSetMetaData.getColumnLabel(i), value);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return map;
    }

    @Override
    public Object getValue(String fieldType, String fieldName, ResultSetDelegate<ResultSet> resultSetDelegate) {
        ResultSet resultSet = resultSetDelegate.getDelegate();
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
                case "java.lang.Double":
                    value = resultSet.getObject(fieldName);
                    if (value != null) {
                        value = resultSet.getDouble(fieldName);
                    }
                    break;
                case "long":
                    value = resultSet.getLong(fieldName);
                    break;
                case "java.lang.Long":
                    value = resultSet.getObject(fieldName);
                    if (value != null) {
                        value = resultSet.getLong(fieldName);
                    }
                    break;
                case "boolean":
                    value = resultSet.getBoolean(fieldName);
                    break;
                case "java.lang.Boolean":
                    value = resultSet.getObject(fieldName);
                    if (value != null) {
                        value = resultSet.getBoolean(fieldName);
                    }
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
                case "java.time.LocalDate":
                    value = DateUtil.dateToLocalDate(resultSet.getTimestamp(fieldName));
                    break;
                case "java.time.LocalTime":
                    value = DateUtil.dateToLocalTime(resultSet.getTimestamp(fieldName));
                    break;
                case "java.time.LocalDateTime":
                    value = DateUtil.dateToLocalDateTime(resultSet.getTimestamp(fieldName));
                    break;
                case "java.math.BigDecimal":
                    value = resultSet.getBigDecimal(fieldName);
                    break;
                default:
                    value = resultSet.getObject(fieldName);
                    break;
            }
        } catch (SQLException e) {
        }
        return value;
    }

    @Override
    public Object getValue(String jdbcType, int index, ResultSetDelegate<ResultSet> resultSetDelegate) {
        ResultSet resultSet = resultSetDelegate.getDelegate();
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
        }
        return value;
    }

}
