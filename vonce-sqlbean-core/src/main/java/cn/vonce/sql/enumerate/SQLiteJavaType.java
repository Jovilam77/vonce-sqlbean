package cn.vonce.sql.enumerate;

import java.math.BigDecimal;

/**
 * Java类型对应的SQLite类型枚举类
 *
 * @author Jovi
 * @version 1.0
 * @email 766255988@qq.com
 */
public enum SQLiteJavaType {

    NULL(new Class[]{}),
    INTEGER(new Class[]{boolean.class, Boolean.class, byte.class, Byte.class, short.class, Short.class, int.class, Integer.class, long.class, Long.class}),
    REAL(new Class[]{float.class, Float.class, double.class, Double.class, BigDecimal.class}),
    TEXT(new Class[]{java.sql.Clob.class, char.class, Character.class, String.class, java.sql.Date.class, java.sql.Time.class, java.sql.Timestamp.class, java.util.Date.class}),
    BLOB(new Class[]{java.sql.Blob.class, Object.class});

    SQLiteJavaType(Class<?>[] classes) {
        this.classes = classes;
    }

    private Class<?>[] classes;

    public static SQLiteJavaType getType(Class<?> clazz) {
        for (SQLiteJavaType javaType : values()) {
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
