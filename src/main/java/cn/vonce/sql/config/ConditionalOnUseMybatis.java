package cn.vonce.sql.config;

import cn.vonce.sql.orm.mapper.MybatisSqlBeanMapper;
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
        String[] beanName = conditionContext.getBeanFactory().getBeanNamesForType(MybatisSqlBeanMapper.class);
        System.out.println("未配置mybatis插件");
        if (beanName == null || beanName.length == 0) {
            try {
                Class.forName("org.apache.ibatis.plugin.Interceptor");
                System.out.println("启用自动配置mybatis插件");
                return true;
            } catch (ClassNotFoundException e) {
                return false;
            }
        } else {
            return false;
        }
    }
}
