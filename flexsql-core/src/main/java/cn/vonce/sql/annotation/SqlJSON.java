package cn.vonce.sql.annotation;

import cn.vonce.sql.json.JSONConvert;
import cn.vonce.sql.json.JSONConvertImpl;

import java.lang.annotation.*;

/**
 * Json注解
 *
 * @author Jovi
 * @email imjovi@qq.com
 * @date 2024/11/21 14:53
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
@Inherited
public @interface SqlJSON {

    /**
     * 自定义转换器
     *
     * @return
     */
    Class<? extends JSONConvert> convert() default JSONConvertImpl.class;

}
