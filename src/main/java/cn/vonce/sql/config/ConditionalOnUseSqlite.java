package cn.vonce.sql.config;

import org.springframework.context.annotation.Conditional;

import java.lang.annotation.*;

/**
 * 根据使用Sqlite数据库来配置SqlBeanConfig
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
public @interface ConditionalOnUseSqlite {


}
