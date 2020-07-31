package cn.vonce.sql.spring.config;

import cn.vonce.sql.spring.mapper.MybatisSqlBeanMapperInterceptor;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * 根据使用Mybatis来配置插件
 *
 * @author Jovi
 * @version 1.0
 * @email 766255988@qq.com
 * @date 2019年6月25日下午12:7:50
 */
public class ConditionalOnUseMybatis implements Condition {

    @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
        try {
            String[] beanName = conditionContext.getBeanFactory().getBeanNamesForType(MybatisSqlBeanMapperInterceptor.class);
            if (beanName == null || beanName.length == 0) {
                Class.forName("org.apache.ibatis.plugin.Interceptor");
                return true;
            }
        } catch (ClassNotFoundException e) {
            return false;
        }
        return false;
    }
}
