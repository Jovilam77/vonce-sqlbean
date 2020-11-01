package cn.vonce.sql.spring.annotation;

import java.lang.annotation.*;

/**
 * @author Jovi
 * @version 1.0
 * @email imjovi@qq.com
 * @date 2020/10/30 21:06
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
@Inherited
public @interface DbReadOrWrite {

    enum Type {
        READ, WRITE
    }

    Type value();

}
