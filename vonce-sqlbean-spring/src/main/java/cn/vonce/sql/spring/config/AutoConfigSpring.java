package cn.vonce.sql.spring.config;

import cn.vonce.sql.spring.datasource.DataSourceAspect;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Solon Mybatis 配置
 *
 * @author Jovi
 * @email imjovi@qq.com
 * @date 2024/8/12 15:27
 */
public class AutoConfigSpring implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext context) {
        ((BeanDefinitionRegistry) context).registerBeanDefinition("autoCreateTableListener", new RootBeanDefinition(AutoCreateTableListener.class));
        ((BeanDefinitionRegistry) context).registerBeanDefinition("dataSourceAspect", new RootBeanDefinition(DataSourceAspect.class));
    }

}