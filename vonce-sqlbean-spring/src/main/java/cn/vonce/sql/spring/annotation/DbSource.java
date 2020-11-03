package cn.vonce.sql.spring.annotation;

import java.lang.annotation.*;

/**
 * 多数据源
 *
 * @author Jovi
 * @version 1.0
 * @email imjovi@qq.com
 * @date 2020/11/2 23:11
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface DbSource {

    String master();

    String[] slave() default {};

}
