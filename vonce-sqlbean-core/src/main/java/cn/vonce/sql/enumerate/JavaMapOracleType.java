package cn.vonce.sql.enumerate;

import java.math.BigDecimal;

/**
 * Java类型对应的Oracle类型枚举类
 *
 * @author Jovi
 * @email imjovi@qq.com
 * @date 2022/8/22 16:28
 */
public enum JavaMapOracleType {

    FLOAT(new Class[]{float.class, Float.class, double.class, Double.class, BigDecimal.class}),
    NUMBER(new Class[]{boolean.class, Boolean.class, byte.class, Byte.class, short.class, Short.class, int.class, Integer.class, long.class, Long.class}),
    CHAR(new Class[]{char.class, Character.class}),
    VARCHAR2(new Class[]{String.class}),
    DATE(new Class[]{java.sql.Date.class, java.sql.Time.class, java.sql.Timestamp.class, java.util.Date.class, java.time.LocalDateTime.class, java.time.LocalDate.class, java.time.LocalTime.class}),
    CLOB(new Class[]{java.sql.Clob.class}),
    BLOB(new Class[]{java.sql.Blob.class, Object.class}),
    JSON(new Class[]{});


    JavaMapOracleType(Class<?>[] classes) {
        this.classes = classes;
    }

    private Class<?>[] classes;

    public Class<?>[] getClasses() {
        return classes;
    }

}
