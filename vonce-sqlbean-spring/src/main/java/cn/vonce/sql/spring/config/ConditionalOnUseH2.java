package cn.vonce.sql.spring.config;

import org.springframework.context.annotation.Conditional;

import java.lang.annotation.*;

/**
 * 根据使用H2数据库来配置SqlBeanConfig
 *
 * @author Jovi
 * @version 1.0
 * @email imjovi@qq.com
 * @date 2020年2月16日下午17:54:20
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Conditional({ConditionOnDbType.class})
public @interface ConditionalOnUseH2 {


}
