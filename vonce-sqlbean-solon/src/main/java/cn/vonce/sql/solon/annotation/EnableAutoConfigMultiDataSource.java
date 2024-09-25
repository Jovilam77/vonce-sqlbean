package cn.vonce.sql.solon.annotation;

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
public @interface EnableAutoConfigMultiDataSource {

    /**
     * 默认数据源名称
     *
     * @return
     */
    String defaultDataSource() default "";

}
