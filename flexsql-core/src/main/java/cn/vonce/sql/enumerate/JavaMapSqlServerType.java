package cn.vonce.sql.enumerate;

import java.math.BigDecimal;

/**
 * Java类型对应的Oracle类型枚举类
 *
 * @author Jovi
 * @email imjovi@qq.com
 * @date 2022/8/22 16:28
 */
public enum JavaMapSqlServerType {

    INT(new Class[]{int.class, Integer.class}),
    BIGINT(new Class[]{long.class, Long.class}),
    SMALLINT(new Class[]{short.class, Short.class}),
    REAL(new Class[]{float.class, Float.class}),
    FLOAT(new Class[]{double.class, Double.class}),
    NUMERIC(new Class[]{BigDecimal.class}),
    NCHAR(new Class[]{char.class, Character.class}), NVARCHAR(new Class[]{String.class}),
    TINYINT(new Class[]{byte.class, Byte.class}),
    BIT(new Class[]{boolean.class, Boolean.class}),
    DATE(new Class[]{java.sql.Date.class, java.time.LocalDate.class}),
    TIME(new Class[]{java.sql.Time.class, java.time.LocalTime.class}),
    DATETIME2(new Class[]{java.sql.Timestamp.class}),
    DATETIME(new Class[]{java.util.Date.class, java.time.LocalDateTime.class}),
    NTEXT(new Class[]{java.sql.Clob.class}),
    IMAGE(new Class[]{java.sql.Blob.class, Object.class});


    JavaMapSqlServerType(Class<?>[] classes) {
        this.classes = classes;
    }

    private Class<?>[] classes;

    public Class<?>[] getClasses() {
        return classes;
    }

}
