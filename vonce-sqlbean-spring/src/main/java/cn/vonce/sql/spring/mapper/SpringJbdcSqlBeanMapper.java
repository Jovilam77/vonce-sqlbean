package cn.vonce.sql.spring.mapper;

import cn.vonce.sql.mapper.SqlBeanMapper;
import cn.vonce.sql.uitls.SqlBeanUtil;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;

/**
 * Spring jdbc 结果映射
 *
 * @author Jovi
 * @version 1.0
 * @email imjovi@qq.com
 */
public class SpringJbdcSqlBeanMapper<T> extends SqlBeanMapper implements RowMapper<T> {

    public Class<?> clazz;
    public Class<?> returnType;

    public SpringJbdcSqlBeanMapper(Class<?> clazz, Class<?> returnType) {
        this.clazz = clazz;
        this.returnType = returnType;
    }

    @Override
    public T mapRow(ResultSet resultSet, int index) {
        Object value;
        if (SqlBeanUtil.isMap(returnType)) {
            value = super.mapHandleResultSet(resultSet);
        } else if (!SqlBeanUtil.isBaseType(returnType)) {
            value = super.beanHandleResultSet(returnType, resultSet, super.getColumnNameList(resultSet));
        } else {
            value = super.baseHandleResultSet(resultSet);
            if (value != null && value.getClass() != returnType) {
                value = SqlBeanUtil.getValueConvert(returnType, value);
            }
        }
        return (T) value;
    }

}
