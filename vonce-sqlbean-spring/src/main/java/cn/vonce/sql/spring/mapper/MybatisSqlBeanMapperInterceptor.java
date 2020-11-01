package cn.vonce.sql.spring.mapper;

import cn.vonce.common.utils.ReflectUtil;
import cn.vonce.sql.uitls.SqlBeanUtil;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.resultset.DefaultResultSetHandler;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.plugin.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

/**
 * Mybatis 结果映射拦截器
 *
 * @author Jovi
 * @version 1.0
 * @email 766255988@qq.com
 * @date 2018年5月15日上午9:21:48
 */
@Intercepts(@Signature(method = "handleResultSets", type = ResultSetHandler.class, args = {Statement.class}))
public class MybatisSqlBeanMapperInterceptor extends SqlBeanMapper implements Interceptor {

    private Logger logger = LoggerFactory.getLogger(MybatisSqlBeanMapperInterceptor.class);

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        // 获取代理目标对象
        Object target = invocation.getTarget();
        if (target instanceof DefaultResultSetHandler) {
            DefaultResultSetHandler resultSetHandler = (DefaultResultSetHandler) target;
            // 利用反射获取参数对象
            ParameterHandler parameterHandler = ReflectUtil.getFieldValue(resultSetHandler, "parameterHandler");
            MappedStatement mappedStatement = ReflectUtil.getFieldValue(resultSetHandler, "mappedStatement");
            Object parameterObj = parameterHandler.getParameterObject();
            // 获取节点属性的集合
            List<ResultMap> resultMaps = mappedStatement.getResultMaps();
            if (parameterObj instanceof HashMap) {
                @SuppressWarnings("unchecked")
                Map<String, Object> mapParam = (HashMap<String, Object>) parameterObj;
                if (mapParam.containsKey("clazz") && (resultMaps.get(0).getResultMappings() == null || resultMaps.get(0).getResultMappings().isEmpty())) {
                    // 获取当前resultType的类型
                    Class<?> resultType = resultMaps.get(0).getType();
                    // 获取当前statement
                    Statement stmt = (Statement) invocation.getArgs()[0];
                    if (Object.class.getName().equals(resultType.getName())) {
                        return handleResultSet(stmt.getResultSet(), mapParam, resultType);
                    } else if (SqlBeanUtil.isMap(resultType.getName())) {
                        return mapHandleResultSet(stmt.getResultSet());
                    }
                    return baseHandleResultSet(stmt.getResultSet(), resultType);
                }
            }
        }
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        // TODO Auto-generated method stub

    }

    /**
     * 对象映射处理
     *
     * @param resultSet
     * @param mapParam
     * @return
     */
    private Object handleResultSet(ResultSet resultSet, Map<String, Object> mapParam, Class<?> resultType) {
        if (null != resultSet) {
            // 获取实际需要返回的类型
            Class<?> clazz = (Class<?>) mapParam.get("clazz");
            Class<?> returnType;
            if (mapParam.containsKey("returnType")) {
                returnType = (Class<?>) mapParam.get("returnType");
            } else {
                returnType = clazz;
            }
            List<Object> list;
            if (!SqlBeanUtil.isBaseType(returnType.getName())) {
                list = beanHandleResultSet(returnType, resultSet, super.getColumnNameList(resultSet));
            } else {
                list = baseHandleResultSet(resultSet, resultType);
            }
            return list;
        }
        return null;
    }

    /**
     * bean对象映射处理
     *
     * @param resultSet
     * @param clazz
     * @return
     */
    public List<Object> beanHandleResultSet(Class<?> clazz, ResultSet resultSet, List<String> columnNameList) {
        List<Object> list = new ArrayList<>();
        try {
            while (resultSet.next()) {
                list.add(super.beanHandleResultSet(clazz, resultSet, columnNameList));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResultSet(resultSet);
        }
        return list;
    }

    /**
     * map对象映射
     *
     * @param resultSet
     * @return
     */
    public Object mapHandleResultSet(ResultSet resultSet) {
        List<Object> resultList = new ArrayList<>();
        if (null != resultSet) {
            try {
                while (resultSet.next()) {
                    resultList.add(super.mapHandleResultSet(resultSet));
                }
            } catch (SQLException e) {
                logger.error("map对象映射异常SQLException，{}", e.getMessage());
            } finally {
                // 关闭result set
                closeResultSet(resultSet);
            }
        }
        return resultList;
    }

    /**
     * 基础对象映射
     *
     * @param resultSet
     * @return
     */
    public List<Object> baseHandleResultSet(ResultSet resultSet, Class<?> resultType) {
        List<Object> resultList = new ArrayList<>();
        if (null != resultSet) {
            try {
                while (resultSet.next()) {
                    Object value = super.baseHandleResultSet(resultSet);
                    if (value != null && !value.getClass().getName().equals(resultType.getName())) {
                        value = super.getValueConvert(resultType.getName(), value);
                    }
                    resultList.add(value);
                }
            } catch (SQLException e) {
                logger.error("基础对象映射异常SQLException，{}", e.getMessage());
            } finally {
                // 关闭result set
                closeResultSet(resultSet);
            }
        }
        if (resultList.isEmpty()) {
            resultList.add(getDefaultValue(resultType.getName()));
        }
        return resultList;
    }

    /**
     * 关闭ResultSet
     *
     * @param resultSet
     */
    private void closeResultSet(ResultSet resultSet) {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
        } catch (SQLException e) {
            logger.error("关闭 result set异常,{}", e.getMessage());
        }
    }

}
