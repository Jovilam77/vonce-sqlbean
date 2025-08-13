package cn.vonce.sql.uitls;

/**
 * @author Jovi《imjovi@qq.com》
 * @version 1.0《2025/2/28 16:46》
 */
public interface Reflect {

    Object newObject(Class<?> clazz);

    Object get(Class<?> clazz, Object instance, String name);

    void set(Class<?> clazz, Object instance, String name, Object value);

    Object invoke(Class<?> clazz, Object instance, String name);

    Object invoke(Class<?> clazz, Object instance, String name, Object value);

    Object invoke(Class<?> clazz, Object instance, String name, Class<?>[] parameterTypes, Object[] values);

}
