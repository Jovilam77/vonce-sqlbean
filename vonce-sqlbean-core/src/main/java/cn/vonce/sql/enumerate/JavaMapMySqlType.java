package cn.vonce.sql.enumerate;

import java.math.BigDecimal;

/**
 * Java类型对应的Oracle类型枚举类
 *
 * @author Jovi
 * @email imjovi@qq.com
 * @date 2022/8/22 16:28
 */
public enum JavaMapMySqlType {

    INT(new Class[]{int.class, Integer.class}),
    BIGINT(new Class[]{long.class, Long.class}),
    SMALLINT(new Class[]{short.class, Short.class}),
    FLOAT(new Class[]{float.class, Float.class}),
    DOUBLE(new Class[]{double.class, Double.class}),
    DECIMAL(new Class[]{BigDecimal.class}),
    CHAR(new Class[]{char.class, Character.class}),
    VARCHAR(new Class[]{String.class}),
    TINYINT(new Class[]{byte.class, Byte.class}),
    BIT(new Class[]{boolean.class, Boolean.class}),
    DATE(new Class[]{java.sql.Date.class, java.time.LocalDate.class}),
    TIME(new Class[]{java.sql.Time.class, java.time.LocalTime.class}),
    TIMESTAMP(new Class[]{java.sql.Timestamp.class}),
    DATETIME(new Class[]{java.util.Date.class, java.time.LocalDateTime.class}),
    CLOB(new Class[]{java.sql.Clob.class}),
    BLOB(new Class[]{java.sql.Blob.class, Object.class});


    JavaMapMySqlType(Class<?>[] classes) {
        this.classes = classes;
    }

    private Class<?>[] classes;

    public Class<?>[] getClasses() {
        return classes;
    }

}
