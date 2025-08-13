package cn.vonce.sql.spring.mapper;

import cn.vonce.sql.mapper.ResultSetDelegate;
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
public class SpringJdbcSqlBeanMapper<T> extends SqlBeanMapper implements RowMapper<T> {

    public Class<?> clazz;
    public Class<?> returnType;

    public SpringJdbcSqlBeanMapper(Class<?> clazz, Class<?> returnType) {
        this.clazz = clazz;
        this.returnType = returnType;
    }

    @Override
    public T mapRow(ResultSet resultSet, int index) {
        ResultSetDelegate resultSetDelegate = new ResultSetDelegate<>(resultSet);
        Object value;
        if (SqlBeanUtil.isMap(returnType)) {
            value = super.mapHandleResultSet(resultSetDelegate);
        } else if (!SqlBeanUtil.isBaseType(returnType)) {
            value = super.beanHandleResultSet(returnType, resultSetDelegate, super.getColumnNameList(resultSetDelegate));
        } else {
            value = super.baseHandleResultSet(resultSetDelegate);
            if (value != null && value.getClass() != returnType) {
                value = SqlBeanUtil.getValueConvert(returnType, value);
            }
        }
        return (T) value;
    }

}
