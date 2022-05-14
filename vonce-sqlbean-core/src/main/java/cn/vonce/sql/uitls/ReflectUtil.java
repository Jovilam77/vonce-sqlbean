package cn.vonce.sql.uitls;

import java.lang.reflect.Field;

/**
 * 反射工具抽象类
 *
 * @author Jovi
 * @version 1.0
 * @email imjovi@qq.com
 * @date 2020/7/24 18:30
 */
public abstract class ReflectUtil {

    public abstract Object newObject(Class<?> clazz);

    public abstract Object get(Class<?> clazz, Object instance, String name);

    public abstract void set(Class<?> clazz, Object instance, String name, Object value);

    public abstract Object invoke(Class<?> clazz, Object instance, String name);

    public abstract Object invoke(Class<?> clazz, Object instance, String name, Object value);

    public abstract Object invoke(Class<?> clazz, Object instance, String name, Class<?>[] parameterTypes, Object[] values);

    private static ReflectUtil reflectUtil;

    public static ReflectUtil instance() {
        return reflectUtil;
    }

    static {
        try {
            Class.forName("android.content.Context");
            reflectUtil = ReflectJdkUtil.instance();
        } catch (ClassNotFoundException e) {
            reflectUtil = ReflectAsmUtil.instance();
        }
    }

    /**
     * 利用反射获取指定对象的指定属性
     *
     * @param obj       目标对象
     * @param fieldName 目标属性
     * @return 目标属性的值
     */
    @SuppressWarnings("unchecked")
    public static <T> T getFieldValue(Object obj, String fieldName) {
        Object result = null;
        Field field = ReflectUtil.getField(obj, fieldName);
        if (field != null) {
            field.setAccessible(true);
            try {
                result = field.get(obj);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return (T) result;
    }

    /**
     * 利用反射获取指定对象里面的指定属性
     *
     * @param obj       目标对象
     * @param fieldName 目标属性
     * @return 目标字段
     */
    private static Field getField(Object obj, String fieldName) {
        Field field = null;
        for (Class<?> clazz = obj.getClass(); clazz != Object.class; clazz = clazz.getSuperclass()) {
            try {
                field = clazz.getDeclaredField(fieldName);
                break;
            } catch (NoSuchFieldException e) {
                // 这里不用做处理，子类没有该字段可能对应的父类有，都没有就返回null。
            }
        }
        return field;
    }

    /**
     * 利用反射设置指定对象的指定属性为指定的值
     *
     * @param obj        目标对象
     * @param fieldName  目标属性
     * @param fieldValue 目标值
     */
    public static void setFieldValue(Object obj, String fieldName, String fieldValue) {
        Field field = ReflectUtil.getField(obj, fieldName);
        if (field != null) {
            try {
                field.setAccessible(true);
                field.set(obj, fieldValue);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

}
