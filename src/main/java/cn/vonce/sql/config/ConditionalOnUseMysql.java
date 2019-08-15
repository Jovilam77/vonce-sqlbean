package cn.vonce.sql.config;

import org.springframework.context.annotation.Conditional;

import java.lang.annotation.*;

/**
 * 根据使用Mysql数据库来配置SqlBeanConfig
 *
 * @author Jovi
 * @version 1.0
 * @email 766255988@qq.com
 * @date 2019年6月25日下午12:7:50
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Conditional({ConditionOnDbType.class})
public @interface ConditionalOnUseMysql {



}
