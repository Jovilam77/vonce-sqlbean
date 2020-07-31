package cn.vonce.sql.uitls;

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

}
