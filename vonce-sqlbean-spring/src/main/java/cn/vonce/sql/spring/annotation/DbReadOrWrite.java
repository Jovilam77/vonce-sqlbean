package cn.vonce.sql.spring.annotation;

import java.lang.annotation.*;

/**
 * @author Jovi
 * @version 1.0
 * @email imjovi@qq.com
 * @date 2020/11/2 23:11
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
