package cn.vonce.sql.uitls;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * 反射工具类 JDK
 *
 * @author Jovi
 * @version 1.0
 * @email imjovi@qq.com
 * @date 2020/7/24 18:30
 */
public class ReflectJdkUtil extends ReflectUtil {

    private final Map<String, Method> methodMap = new WeakHashMap<>();
    private final Map<Class<?>, Constructor> constructorMap = new WeakHashMap<>();

    private static ReflectJdkUtil reflectJdkUtil;

    private ReflectJdkUtil() {

    }

    public static ReflectJdkUtil instance() {
        if (reflectJdkUtil == null) {
            synchronized (ReflectJdkUtil.class) {
                if (reflectJdkUtil == null) {
                    reflectJdkUtil = new ReflectJdkUtil();
                }
            }
        }
        return reflectJdkUtil;
    }

    @Override
    public Object newObject(Class<?> clazz) {
        Constructor constructor = constructorMap.get(clazz);
        Object object = null;
        if (constructor == null) {
            try {
                constructor = clazz.getDeclaredConstructor();
                object = constructor.newInstance();
                constructorMap.put(clazz, constructor);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return object;
    }

    @Override
    public Object get(Class<?> clazz, Object instance, String name) {
        if (clazz == null || name == null || name.trim().length() == 0) {
            return null;
        }
        Object object = null;
        try {
            name = "get" + name.substring(0, 1).toUpperCase() + name.substring(1);
            String methodFullName = clazz.getName() + "." + name;
            Method method = methodMap.get(methodFullName);
            if (method == null) {
                method = clazz.getMethod(name);
                methodMap.put(methodFullName, method);
            }
            object = method.invoke(instance);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return object;
    }

    @Override
    public void set(Class<?> clazz, Object instance, String name, Object value) {
        if (clazz == null || name == null || name.trim().length() == 0) {
            return;
        }
        try {
            name = "set" + name.substring(0, 1).toUpperCase() + name.substring(1);
            String methodFullName = clazz.getName() + "." + name;
            Method method = methodMap.get(methodFullName);
            if (method == null) {
                Object.class.isArray();
                method = getMethod(clazz.getMethods(), name, 1);
                methodMap.put(methodFullName, method);
            }
            method.invoke(instance, value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private Method getMethod(Method[] methods, String name, int paramCount) {
        for (Method method : methods) {
            if (method.getName().equals(name) && method.getParameterTypes().length == paramCount) {
                return method;
            }
        }
        return null;
    }

}
