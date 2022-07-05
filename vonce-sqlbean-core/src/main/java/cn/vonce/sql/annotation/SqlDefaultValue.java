package cn.vonce.sql.annotation;

import cn.vonce.sql.enumerate.FillWith;

import java.lang.annotation.*;

/**
 * 标识该注解的字段如果为null自动注入默认值（仅支持基本类型、String、Date、Timestamp、BigDecimal）
 *
 * @author Jovi
 * @email imjovi@qq.com
 * @date 2022/6/24 20:22
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
@Inherited
public @interface SqlDefaultValue {

    /**
     * 填充类型（insert=新增、update=更新，together=新增更新同时）
     *
     * @return
     */
    FillWith with();

}
