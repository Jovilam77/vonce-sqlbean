package cn.vonce.sql.spring.config;

import cn.vonce.sql.java.config.BaseAutoConfigMultiDataSource;
import cn.vonce.sql.spring.annotation.EnableAutoConfigMultiDataSource;
import cn.vonce.sql.spring.datasource.DynamicDataSource;
import cn.vonce.sql.spring.datasource.TransactionalInterceptor;
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
import java.util.*;

/**
 * 自动配置多数据源
 *
 * @author Jovi
 * @version 1.0
 * @email imjovi@qq.com
 * @date 2021/7/7 17:00
 */
public class AutoConfigMultiDataSource extends BaseAutoConfigMultiDataSource implements ImportBeanDefinitionRegistrar, EnvironmentAware {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private StandardEnvironment environment;

    @Override
    public void setEnvironment(Environment environment) {
        if (environment instanceof StandardEnvironment) {
            this.environment = (StandardEnvironment) environment;
        }
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry beanDefinitionRegistry) {
        if (!(environment instanceof StandardEnvironment)) {
            logger.warn("Expected StandardEnvironment, but found: {}", environment.getClass().getName());
            return;
        }
        Map<String, Object> annotationAttributeMap = annotationMetadata.getAnnotationAttributes(EnableAutoConfigMultiDataSource.class.getName());
        //取得注解中的属性
        Class<?> multiDataSourceClass = (Class<?>) annotationAttributeMap.get("multiDataSource");
        String defaultDataSource = (String) annotationAttributeMap.get("defaultDataSource");
        super.config(multiDataSourceClass, defaultDataSource, (defaultTargetDataSource, dataSourceMap) -> {
            BeanDefinitionBuilder definitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(DynamicDataSource.class);
            definitionBuilder.addPropertyValue("defaultTargetDataSource", defaultTargetDataSource);
            definitionBuilder.addPropertyValue("targetDataSources", dataSourceMap);
            beanDefinitionRegistry.registerBeanDefinition("dynamicDataSource", definitionBuilder.getBeanDefinition());
            beanDefinitionRegistry.registerBeanDefinition("sqlBeanTransactional", transactionalDefinition());
        });
    }

    @Override
    public String getProperty(String key) {
        return environment.getProperty(key);
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
