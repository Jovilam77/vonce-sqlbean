package cn.vonce.sql.orm.mapper;

import cn.vonce.sql.uitls.SqlBeanUtil;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;

/**
 * Spring jdbc 结果映射
 *
 * @author Jovi
 * @version 1.0
 * @email 766255988@qq.com
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
        Object object;
        if (SqlBeanUtil.isMap(returnType.getName())) {
            object = super.mapHandleResultSet(resultSet);
        } else if (!SqlBeanUtil.isBaseType(returnType.getName())) {
            object = super.beanHandleResultSet(clazz, resultSet, super.getColumnNameList(resultSet));
        } else {
            object = super.baseHandleResultSet(resultSet);
        }
        return (T) object;
    }

}
