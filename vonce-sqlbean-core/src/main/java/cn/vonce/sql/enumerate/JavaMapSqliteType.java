package cn.vonce.sql.enumerate;

import java.math.BigDecimal;

/**
 * Java类型对应的SQLite类型枚举类
 *
 * @author Jovi
 * @version 1.0
 * @email imjovi@qq.com
 */
public enum JavaMapSqliteType {

    NULL(new Class[]{}),
    INTEGER(new Class[]{boolean.class, Boolean.class, byte.class, Byte.class, short.class, Short.class, int.class, Integer.class, long.class, Long.class}),
    REAL(new Class[]{float.class, Float.class, double.class, Double.class, BigDecimal.class}),
    TEXT(new Class[]{java.sql.Clob.class, char.class, Character.class, String.class, java.sql.Date.class, java.sql.Time.class, java.sql.Timestamp.class, java.util.Date.class/*, java.time.LocalDateTime.class, java.time.LocalDate.class, java.time.LocalTime.class*/}),
    BLOB(new Class[]{java.sql.Blob.class, Object.class});

    JavaMapSqliteType(Class<?>[] classes) {
        this.classes = classes;
    }

    private Class<?>[] classes;

    public Class<?>[] getClasses() {
        return classes;
    }

}
