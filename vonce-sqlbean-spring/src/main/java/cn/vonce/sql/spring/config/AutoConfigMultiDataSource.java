package cn.vonce.sql.spring.config;

import cn.vonce.sql.spring.annotation.EnableAutoConfigMultiDataSource;
import cn.vonce.sql.spring.datasource.DynamicDataSource;
import cn.vonce.sql.spring.datasource.TransactionalInterceptor;
import cn.vonce.sql.uitls.SqlBeanUtil;
import cn.vonce.sql.uitls.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.type.AnnotationMetadata;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * 自动配置多数据源
 *
 * @author Jovi
 * @version 1.0
 * @email imjovi@qq.com
 * @date 2021/7/7 17:00
 */
public class AutoConfigMultiDataSource implements ImportBeanDefinitionRegistrar, EnvironmentAware {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private final static List<String> fieldList = new ArrayList<>();
    private final static String MULTI_DATA_SOURCE_TYPE = "spring.datasource.type";
    private final static String MULTI_DATA_SOURCE_PREFIX = "spring.datasource.sqlbean";
    private final static String DRUID_DATA_SOURCE_CLASS = "com.alibaba.druid.pool.DruidDataSource";
    private final static Map<Class<?>, Map<String, Method>> classMethodMap = new WeakHashMap<>(8);
    private Environment environment;

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

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry beanDefinitionRegistry) {
        if (annotationMetadata.isAnnotated(EnableAutoConfigMultiDataSource.class.getName())) {
            if (!(environment instanceof StandardEnvironment)) {
                logger.warn("Expected StandardEnvironment, but found: {}", environment.getClass().getName());
                return;
            }
            Map<String, Object> annotationAttributeMap = annotationMetadata.getAnnotationAttributes(EnableAutoConfigMultiDataSource.class.getName());
            //取得注解中的属性
            Class<?> multiDataSourceClass = (Class<?>) annotationAttributeMap.get("multiDataSource");
            String defaultDataSource = (String) annotationAttributeMap.get("defaultDataSource");
            List<String> dataSourceNameList = getDataSourceNameList(multiDataSourceClass);
            if (dataSourceNameList == null || dataSourceNameList.isEmpty()) {
                return;
            }
            //如果未设置默认数据源将默认设置第一个
            if (StringUtil.isEmpty(defaultDataSource)) {
                defaultDataSource = dataSourceNameList.get(0);
            }
            Map<Object, Object> dataSourceMap = new HashMap<>(8);
            BeanDefinitionBuilder definitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(DynamicDataSource.class);
            StandardEnvironment env = (StandardEnvironment) environment;
            Class<?> typeClass = getTypeClass(env.getProperty(MULTI_DATA_SOURCE_TYPE));
            for (String dataSourceName : dataSourceNameList) {
                Map<String, Method> methodMap = getMethodMap(typeClass);
                Object dataSource = null;
                try {
                    dataSource = typeClass.newInstance();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                }
                for (String fieldName : fieldList) {
                    String propertyValue = env.getProperty(MULTI_DATA_SOURCE_PREFIX + "." + dataSourceName + "." + fieldName);
                    if (StringUtil.isBlank(propertyValue)) {
                        propertyValue = env.getProperty(MULTI_DATA_SOURCE_PREFIX + "." + dataSourceName + "." + StringUtil.humpToHyphen(fieldName));
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
                    definitionBuilder.addPropertyValue("defaultTargetDataSource", dataSource);
                }
                dataSourceMap.put(dataSourceName, dataSource);
            }
            definitionBuilder.addPropertyValue("targetDataSources", dataSourceMap);
            beanDefinitionRegistry.registerBeanDefinition("dynamicDataSource", definitionBuilder.getBeanDefinition());
            beanDefinitionRegistry.registerBeanDefinition("sqlBeanTransactional", transactionalDefinition());
            classMethodMap.values().forEach(item -> item.clear());
            classMethodMap.clear();
        }
    }

    /**
     * 获取多数据源名称
     *
     * @param multiDataSourceClass
     * @return
     */
    private List<String> getDataSourceNameList(Class<?> multiDataSourceClass) {
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
    private Class<?> getTypeClass(String type) {
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
    private Map<String, Method> getMethodMap(Class<?> typeClass) {
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

    private BeanDefinition transactionalDefinition() {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression("@annotation(cn.vonce.sql.spring.annotation.DbTransactional)");
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(DefaultPointcutAdvisor.class);
        beanDefinitionBuilder.addPropertyValue("pointcut", pointcut);
        beanDefinitionBuilder.addPropertyValue("advice", new TransactionalInterceptor());
        return beanDefinitionBuilder.getBeanDefinition();
    }

}
