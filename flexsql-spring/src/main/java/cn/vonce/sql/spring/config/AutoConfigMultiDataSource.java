package cn.vonce.sql.spring.config;

import cn.vonce.sql.java.config.BaseAutoConfigMultiDataSource;
import cn.vonce.sql.spring.annotation.EnableAutoConfigMultiDataSource;
import cn.vonce.sql.spring.datasource.DynamicDataSource;
import cn.vonce.sql.spring.datasource.TransactionalInterceptor;
import cn.vonce.sql.uitls.StringUtil;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.type.AnnotationMetadata;

import java.util.*;
import java.util.logging.Logger;

/**
 * 自动配置多数据源
 *
 * @author Jovi
 * @version 1.0
 * @email imjovi@qq.com
 * @date 2021/7/7 17:00
 */
public class AutoConfigMultiDataSource extends BaseAutoConfigMultiDataSource implements ImportBeanDefinitionRegistrar, EnvironmentAware {

    private final Logger logger = Logger.getLogger(this.getClass().getName());
    private StandardEnvironment environment;
    private Map<String, Object> propertyMap;

    @Override
    public void setEnvironment(Environment environment) {
        if (environment instanceof StandardEnvironment) {
            this.environment = (StandardEnvironment) environment;
        }
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry beanDefinitionRegistry) {
        if (!(environment instanceof StandardEnvironment)) {
            logger.warning(String.format("Expected StandardEnvironment, but found: %s", environment.getClass().getName()));
            return;
        }
        MutablePropertySources propertySources = environment.getPropertySources();
        String defaultDataSource = null;
        Set<String> dataSourceNameSet = new LinkedHashSet();
        String dataSourcePrefix = this.getDataSourcePrefix();
        this.propertyMap = new HashMap<>();
        for (PropertySource propertySource : propertySources) {
            String name = propertySource.getName();
            if (propertySource.getName().indexOf("[") >= 0 && propertySource.getName().indexOf("]") >= 0) {
                name = propertySource.getName().substring(propertySource.getName().indexOf("[") + 1, propertySource.getName().indexOf("]"));
            }
            if (name.indexOf("classpath:/") >= 0) {
                name = name.substring(name.indexOf("/") + 1);
            }
            if (name.startsWith("application")) {
                Object map = propertySource.getSource();
                if (map instanceof Map) {
                    for (Map.Entry<String, Object> entry : ((Map<String, Object>) map).entrySet()) {
                        String key = entry.getKey();
                        if (key.startsWith(dataSourcePrefix)) {
                            key = key.substring(key.indexOf(dataSourcePrefix) + dataSourcePrefix.length() + 1);
                            key = key.substring(0, key.indexOf("."));
                            dataSourceNameSet.add(key);
                            if (defaultDataSource == null) {
                                defaultDataSource = key;
                            }
                            this.propertyMap.put(entry.getKey(), entry.getValue());
                        }
                    }
                }
            }
        }
        Map<String, Object> annotationAttributeMap = annotationMetadata.getAnnotationAttributes(EnableAutoConfigMultiDataSource.class.getName());
        String anonDefaultDataSource = (String) annotationAttributeMap.get("defaultDataSource");
        if (anonDefaultDataSource != null && StringUtil.isNotBlank(anonDefaultDataSource)) {
            defaultDataSource = anonDefaultDataSource;
        }
        super.config(dataSourceNameSet, defaultDataSource, (defaultTargetDataSource, dataSourceMap) -> {
            BeanDefinitionBuilder definitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(DynamicDataSource.class);
            definitionBuilder.addPropertyValue("defaultTargetDataSource", defaultTargetDataSource);
            definitionBuilder.addPropertyValue("targetDataSources", dataSourceMap);
            beanDefinitionRegistry.registerBeanDefinition("dynamicDataSource", definitionBuilder.getBeanDefinition());
            beanDefinitionRegistry.registerBeanDefinition("sqlBeanTransactional", transactionalDefinition());
        });
    }

    @Override
    public Map<String, Object> getPropertyMap() {
        return this.propertyMap;
    }

    @Override
    public String getProperty(String key) {
        return environment.getProperty(key);
    }

    @Override
    public String getDataSourceType() {
        return "spring.datasource.type";
    }

    @Override
    public String getDataSourcePrefix() {
        return "spring.datasource.sqlbean";
    }

    private BeanDefinition transactionalDefinition() {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression("@annotation(" + cn.vonce.sql.java.annotation.DbTransactional.class.getName() + ")");
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(DefaultPointcutAdvisor.class);
        beanDefinitionBuilder.addPropertyValue("pointcut", pointcut);
        beanDefinitionBuilder.addPropertyValue("advice", new TransactionalInterceptor());
        return beanDefinitionBuilder.getBeanDefinition();
    }

}
