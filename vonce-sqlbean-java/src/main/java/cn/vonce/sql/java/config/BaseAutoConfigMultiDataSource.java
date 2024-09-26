package cn.vonce.sql.java.config;

import cn.vonce.sql.uitls.SqlBeanUtil;
import cn.vonce.sql.uitls.StringUtil;

import javax.sql.DataSource;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * 自动配置多数据源 抽象类
 *
 * @author Jovi
 * @email imjovi@qq.com
 * @date 2024/9/24 16:53
 */
public abstract class BaseAutoConfigMultiDataSource {

    protected final static List<String> fieldList = new ArrayList<>();
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

    public abstract String getDataSourceType();

    public abstract String getDataSourcePrefix();

    public void config(Set<String> dataSourceNameSet, String defaultDataSource, RegisterAutoConfigMultiDataSourceBean register) {
        if (dataSourceNameSet == null || dataSourceNameSet.isEmpty()) {
            return;
        }
        DataSource defaultTargetDataSource = null;
        Map<String, DataSource> dataSourceMap = new HashMap<>(8);
        Class<?> typeClass = getTypeClass(this.getProperty(this.getDataSourceType()));
        String dataSourcePrefix = this.getDataSourcePrefix();
        for (String dataSourceName : dataSourceNameSet) {
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
                String propertyValue = this.getProperty(dataSourcePrefix + "." + dataSourceName + "." + fieldName);
                if (StringUtil.isBlank(propertyValue)) {
                    propertyValue = this.getProperty(dataSourcePrefix + "." + dataSourceName + "." + StringUtil.humpToHyphen(fieldName));
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
