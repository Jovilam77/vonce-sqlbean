package cn.vonce.sql.spring.annotation;

import java.lang.annotation.*;

/**
 * @author Jovi
 * @version 1.0
 * @email imjovi@qq.com
 * @date 2020/10/29 23:33
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface DbSource {

    String master();

    String[] slave() default {};

}
