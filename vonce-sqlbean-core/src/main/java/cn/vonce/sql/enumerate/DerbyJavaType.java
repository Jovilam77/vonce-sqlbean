package cn.vonce.sql.enumerate;

import java.math.BigDecimal;

/**
 * Java类型对应的Derby类型枚举类
 *
 * @author Jovi
 * @email imjovi@qq.com
 * @date 2022/8/12 14:42
 */
public enum DerbyJavaType {

    INTEGER(new Class[]{int.class, Integer.class}),
    BIGINT(new Class[]{long.class, Long.class}),
    SMALLINT(new Class[]{byte.class, Byte.class, short.class, Short.class, boolean.class, Boolean.class}),
    FLOAT(new Class[]{float.class, Float.class}),
    DOUBLE(new Class[]{double.class, Double.class}),
    NUMERIC(new Class[]{BigDecimal.class}),
    CHAR(new Class[]{char.class, Character.class}),
    VARCHAR(new Class[]{String.class}),
    DATE(new Class[]{java.sql.Date.class}),
    TIME(new Class[]{java.sql.Time.class}),
    TIMESTAMP(new Class[]{java.sql.Timestamp.class, java.util.Date.class}),
    CLOB(new Class[]{java.sql.Clob.class}),
    BLOB(new Class[]{java.sql.Blob.class, Object.class});

    DerbyJavaType(Class<?>[] classes) {
        this.classes = classes;
    }

    private Class<?>[] classes;

    public static DerbyJavaType getType(Class<?> clazz) {
        for (DerbyJavaType javaType : values()) {
            for (Class<?> thisClazz : javaType.classes) {
                if (thisClazz == clazz) {
                    return javaType;
                }
            }
        }
        return null;
    }

    public static String getTypeName(Class<?> clazz) {
        return getType(clazz).name();
    }

}
