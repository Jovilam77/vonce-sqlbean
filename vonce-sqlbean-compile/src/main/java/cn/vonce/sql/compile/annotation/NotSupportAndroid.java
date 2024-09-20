package cn.vonce.sql.compile.annotation;

import java.lang.annotation.*;

/**
 * @author Jovi
 * @email imjovi@qq.com
 * @date 2024/9/20 14:14
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.CONSTRUCTOR})
@Documented
@Inherited
public @interface NotSupportAndroid {
}
