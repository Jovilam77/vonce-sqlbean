package cn.vonce.sql.uitls;

import com.esotericsoftware.reflectasm.ConstructorAccess;
import com.esotericsoftware.reflectasm.MethodAccess;

import java.util.Map;
import java.util.WeakHashMap;

/**
 * 反射工具栏 ASM
 * Created by Jovi on 2018/6/24.
 */
public class ReflectAsmUtil extends ReflectUtil {

    private final Map<Class<?>, MethodAccess> methodAccessMap = new WeakHashMap<>();
    private final Map<Class<?>, ConstructorAccess> constructorAccessMap = new WeakHashMap<>();
    private static ReflectAsmUtil reflectAsmUtil;

    public static ReflectAsmUtil instance() {
        if (reflectAsmUtil == null) {
            synchronized (ReflectAsmUtil.class) {
                if (reflectAsmUtil == null) {
                    reflectAsmUtil = new ReflectAsmUtil();
                }
            }
        }
        return reflectAsmUtil;
    }

    @Override
    public Object newObject(Class<?> clazz) {
        ConstructorAccess<?> constructorAccess = constructorAccessMap.get(clazz);
        if (constructorAccess == null) {
            constructorAccess = ConstructorAccess.get(clazz);
            constructorAccessMap.put(clazz, constructorAccess);
        }
        return constructorAccess.newInstance();
    }

    @Override
    public Object get(Class<?> clazz, Object instance, String name) {
        if (clazz == null || name == null || name.trim().length() == 0) {
            return null;
        }
        name = "get" + name.substring(0, 1).toUpperCase() + name.substring(1);
        MethodAccess methodAccess = methodAccessMap.get(clazz);
        if (methodAccess == null) {
            methodAccess = MethodAccess.get(clazz);
            methodAccessMap.put(clazz, methodAccess);
        }
        return methodAccess.invoke(instance, name);
    }

    @Override
    public void set(Class<?> clazz, Object instance, String name, Object value) {
        if (clazz == null || name == null || name.trim().length() == 0) {
            return;
        }
        name = "set" + name.substring(0, 1).toUpperCase() + name.substring(1);
        MethodAccess methodAccess = methodAccessMap.get(clazz);
        if (methodAccess == null) {
            methodAccess = MethodAccess.get(clazz);
            methodAccessMap.put(clazz, methodAccess);
        }
        methodAccess.invoke(instance, name, value);
    }

}
