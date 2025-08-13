package cn.vonce.sql.enumerate;

import java.math.BigDecimal;

/**
 * Java类型对应的Oracle类型枚举类
 *
 * @author Jovi
 * @email imjovi@qq.com
 * @date 2022/8/22 16:28
 */
public enum JavaMapDB2Type {

    INTEGER(new Class[]{int.class, Integer.class}),
    BIGINT(new Class[]{long.class, Long.class}),
    SMALLINT(new Class[]{boolean.class, Boolean.class, byte.class, Byte.class, short.class, Short.class}),
    REAL(new Class[]{float.class, Float.class}),
    DOUBLE(new Class[]{double.class, Double.class}),
    DECIMAL(new Class[]{BigDecimal.class}),
    CHAR(new Class[]{char.class, Character.class}),
    VARCHAR(new Class[]{String.class}),
    DATE(new Class[]{java.sql.Date.class, java.time.LocalDate.class}),
    TIME(new Class[]{java.sql.Time.class, java.time.LocalTime.class}),
    TIMESTAMP(new Class[]{java.sql.Timestamp.class, java.util.Date.class, java.time.LocalDateTime.class}),
    CLOB(new Class[]{java.sql.Clob.class}),
    BLOB(new Class[]{java.sql.Blob.class, Object.class});


    JavaMapDB2Type(Class<?>[] classes) {
        this.classes = classes;
    }

    private Class<?>[] classes;

    public Class<?>[] getClasses() {
        return classes;
    }

}
