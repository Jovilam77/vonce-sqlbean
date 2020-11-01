package cn.vonce.sql.spring.annotation;

import java.lang.annotation.*;

/**
 * @author Jovi
 * @version 1.0
 * @email imjovi@qq.com
 * @date 2020/10/29 23:45
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
@Inherited
public @interface DbSwitch {

    String value();

}
