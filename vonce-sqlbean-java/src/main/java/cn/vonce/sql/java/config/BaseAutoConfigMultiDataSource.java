package cn.vonce.sql.java.config;

import cn.vonce.sql.uitls.SqlBeanUtil;
import cn.vonce.sql.uitls.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * 自动配置多数据源 抽象类
 *
 * @author Jovi
 * @email imjovi@qq.com
 * @date 2024/9/24 16:53
 */
public abstract class BaseAutoConfigMultiDataSource {


    private Logger logger = LoggerFactory.getLogger(this.getClass());

    protected final static List<String> fieldList = new ArrayList<>();
    protected final static String MULTI_DATA_SOURCE_TYPE = "spring.datasource.type";
    protected final static String MULTI_DATA_SOURCE_PREFIX = "spring.datasource.sqlbean";
    protected final static String DRUID_DATA_SOURCE_CLASS = "com.alibaba.druid.pool.DruidDataSource";
    protected final static Map<Class<?>, Map<String, Method>> classMethodMap = new WeakHashMap<>(8);

    static {
        fieldList.add("driverClassName");
        fieldList.add("url");
        fieldList.add("username");
        fieldList.add("password");
        fieldList.add("initialSize");
        fieldList.add("minIdle");
        fieldList.add("maxIdle");
        fieldList.add("maxActive");
        fieldList.add("maxWait");
        fieldList.add("timeBetweenEvictionRunsMillis");
        fieldList.add("minEvictableIdleTimeMillis");
        fieldList.add("validationQuery");
        fieldList.add("testWhileIdle");
        fieldList.add("testOnBorrow");
        fieldList.add("testOnReturn");
        fieldList.add("validationQueryTimeout");
        fieldList.add("keepAlive");
        fieldList.add("removeAbandoned");
        fieldList.add("removeAbandonedTimeout");
        fieldList.add("removeAbandonedTimeoutMillis");
        fieldList.add("logAbandoned");
        fieldList.add("connectionProperties");
        fieldList.add("poolPreparedStatements");
        fieldList.add("maxPoolPreparedStatementPerConnectionSize");
        fieldList.add("filters");
    }

    /**
     * 获取多数据源名称
     *
     * @param multiDataSourceClass
     * @return
     */
    public List<String> getDataSourceNameList(Class<?> multiDataSourceClass) {
        Field[] fields = multiDataSourceClass.getFields();
        if (fields == null || fields.length == 0) {
            return null;
        }
        List<String> dataSourceNameList = new ArrayList<>();
        for (int i = 0; i < fields.length; i++) {
            if (!Modifier.isStatic(fields[i].getModifiers())) {
                continue;
            }
            if (!Modifier.isFinal(fields[i].getModifiers())) {
                continue;
            }
            try {
                Object value = fields[i].get(null);
                dataSourceNameList.add(value.toString());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return dataSourceNameList;
    }

    /**
     * 获取数据库连接类型
     *
     * @param type
     * @return
     */
    public Class<?> getTypeClass(String type) {
        Class<?> typeClass = null;
        try {
            if (StringUtil.isEmpty(type)) {
                typeClass = Class.forName(DRUID_DATA_SOURCE_CLASS);
            } else {
                typeClass = Class.forName(type);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return typeClass;
    }

    /**
     * 获取连接类型中的set方法Map
     *
     * @param typeClass
     * @return
     */
    public Map<String, Method> getMethodMap(Class<?> typeClass) {
        Map<String, Method> methodMap = classMethodMap.get(typeClass);
        if (methodMap == null) {
            methodMap = new HashMap<>(16);
            Method[] methods = typeClass.getMethods();
            for (Method method : methods) {
                if (method.getName().indexOf("set") == 0) {
                    methodMap.put(method.getName(), method);
                }
            }
            classMethodMap.put(typeClass, methodMap);
        }
        return methodMap;
    }

    public abstract String getProperty(String key);

    public void config(Class<?> multiDataSourceClass, String defaultDataSource, RegisterAutoConfigMultiDataSourceBean register) {
        List<String> dataSourceNameList = getDataSourceNameList(multiDataSourceClass);
        if (dataSourceNameList == null || dataSourceNameList.isEmpty()) {
            return;
        }
        //如果未设置默认数据源将默认设置第一个
        if (StringUtil.isEmpty(defaultDataSource)) {
            defaultDataSource = dataSourceNameList.get(0);
        }
        DataSource defaultTargetDataSource = null;
        Map<String, DataSource> dataSourceMap = new HashMap<>(8);
        Class<?> typeClass = getTypeClass(this.getProperty(MULTI_DATA_SOURCE_TYPE));
        for (String dataSourceName : dataSourceNameList) {
            Map<String, Method> methodMap = getMethodMap(typeClass);
            DataSource dataSource = null;
            try {
                dataSource = (DataSource) typeClass.newInstance();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
            for (String fieldName : fieldList) {
                String propertyValue = this.getProperty(MULTI_DATA_SOURCE_PREFIX + "." + dataSourceName + "." + fieldName);
                if (StringUtil.isBlank(propertyValue)) {
                    propertyValue = this.getProperty(MULTI_DATA_SOURCE_PREFIX + "." + dataSourceName + "." + StringUtil.humpToHyphen(fieldName));
                }
                if (StringUtil.isNotBlank(propertyValue)) {
                    try {
                        Method method = methodMap.get("set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1));
                        if (method != null) {
                            method.invoke(dataSource, SqlBeanUtil.getValueConvert(method.getParameterTypes()[0], propertyValue));
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
            if (dataSourceName.equals(defaultDataSource)) {
                defaultTargetDataSource = dataSource;
            }
            dataSourceMap.put(dataSourceName, dataSource);
        }
        register.registerBean(defaultTargetDataSource, dataSourceMap);
        classMethodMap.values().forEach(item -> item.clear());
        classMethodMap.clear();
    }

}
