package cn.vonce.sql.spring.annotation;

import cn.vonce.sql.spring.config.AutoConfigMultiDataSource;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 启用自动配置多数据源
 *
 * @author Jovi
 * @version 1.0
 * @email imjovi@qq.com
 * @date 2021/7/7 17:00
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(AutoConfigMultiDataSource.class)
public @interface EnableAutoConfigMultiDataSource {

    /**
     * 多数据源名称常量类
     *
     * @return
     */
    Class<?> multiDataSource();

    /**
     * 默认数据源名称
     *
     * @return
     */
    String defaultDataSource() default "";

}
