package cn.vonce.sql.uitls;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

public class BeanUtil {

    /**
     * 将一个 Map 对象转化为一个 JavaBean
     *
     * @param clazz 要转化的类型
     * @param map   包含属性值的 Map
     * @param <T>   JavaBean 类型
     * @return 转化出来的 JavaBean 对象
     */
    public static <T> T toBean(Class<T> clazz, Map<String, Object> map) {
        if (clazz == null || map == null) {
            throw new IllegalArgumentException("Class and Map cannot be null");
        }

        try {
            // 创建 JavaBean 对象
            T obj = clazz.getDeclaredConstructor().newInstance();
            BeanInfo beanInfo = Introspector.getBeanInfo(clazz);

            for (PropertyDescriptor descriptor : beanInfo.getPropertyDescriptors()) {
                String propertyName = descriptor.getName();
                if (map.containsKey(propertyName)) {
                    Object value = map.get(propertyName);
                    if (value != null) {
                        Class<?> propertyType = descriptor.getPropertyType();
                        if (value instanceof Map) {
                            // 处理嵌套的 Bean
                            value = toBean(propertyType, (Map<String, Object>) value);
                        } else if (isListOfBean(propertyType, descriptor)) {
                            // 处理 List<Bean>
                            value = toBeanList((List<Map<String, Object>>) value, getListGenericType(descriptor));
                        }
                    }
                    try {
                        if (descriptor.getPropertyType().isEnum()) {
                            Object[] constants = descriptor.getPropertyType().getEnumConstants();
                            for (Object constant : constants) {
                                if (constant.toString().equals(value)) {
                                    value = constant;
                                    break;
                                }
                            }
                            descriptor.getWriteMethod().invoke(obj, value);
                        } else {
                            descriptor.getWriteMethod().invoke(obj, value);
                        }
                    } catch (Exception e) {
                        try {
                            value = SqlBeanUtil.getValueConvert(descriptor.getPropertyType(), value);
                            descriptor.getWriteMethod().invoke(obj, value);
                        } catch (Exception ex) {
                            System.err.println("Failed to set property: " + propertyName + " - " + ex.getMessage());
                        }
                    }
                }
            }
            return obj;
        } catch (IntrospectionException e) {
            throw new RuntimeException("Failed to analyze bean properties: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Failed to instantiate JavaBean: " + e.getMessage(), e);
        }
    }

    /**
     * 将一个 JavaBean 对象转化为一个 Map
     *
     * @param bean 要转化的 JavaBean 对象
     * @return 转化出来的 Map 对象
     */
    public static Map<String, Object> toMap(Object bean) {
        if (bean == null) {
            throw new IllegalArgumentException("Bean cannot be null");
        }

        Map<String, Object> resultMap = new HashMap<>();
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass());

            for (PropertyDescriptor descriptor : beanInfo.getPropertyDescriptors()) {
                String propertyName = descriptor.getName();
                if (!"class".equals(propertyName)) {
                    try {
                        Object value = descriptor.getReadMethod().invoke(bean);
                        if (value != null) {
                            if (isBean(value.getClass())) {
                                // 处理嵌套的 Bean
                                value = toMap(value);
                            } else if (value instanceof List<?>) {
                                // 处理 List<Bean>
                                value = toMapList((List<?>) value);
                            }
                        }
                        resultMap.put(propertyName, value);
                    } catch (Exception e) {
                        System.err.println("Failed to read property: " + propertyName + " - " + e.getMessage());
                    }
                }
            }
        } catch (IntrospectionException e) {
            throw new RuntimeException("Failed to analyze bean properties: " + e.getMessage(), e);
        }

        return resultMap;
    }

    /**
     * 判断是否是 Bean 类型（非基础类型或集合类型）
     */
    private static boolean isBean(Class<?> clazz) {
        return !(clazz.isPrimitive() || clazz.equals(String.class) || Number.class.isAssignableFrom(clazz)
                || Boolean.class.equals(clazz) || Character.class.equals(clazz) || Enum.class.isAssignableFrom(clazz) || Date.class.isAssignableFrom(clazz)
                || Calendar.class.isAssignableFrom(clazz) || Collection.class.isAssignableFrom(clazz) || Map.class.isAssignableFrom(clazz)
                || BigDecimal.class.isAssignableFrom(clazz) || LocalDate.class.isAssignableFrom(clazz) || LocalDateTime.class.isAssignableFrom(clazz)
                || LocalTime.class.isAssignableFrom(clazz));
    }

    /**
     * 判断是否是 List<Bean>
     */
    private static boolean isListOfBean(Class<?> clazz, PropertyDescriptor descriptor) {
        if (List.class.isAssignableFrom(clazz)) {
            ParameterizedType type = (ParameterizedType) descriptor.getReadMethod().getGenericReturnType();
            Class<?> genericType = (Class<?>) type.getActualTypeArguments()[0];
            return isBean(genericType);
        }
        return false;
    }

    /**
     * 获取 List 的泛型类型
     */
    private static Class<?> getListGenericType(PropertyDescriptor descriptor) {
        ParameterizedType type = (ParameterizedType) descriptor.getReadMethod().getGenericReturnType();
        return (Class<?>) type.getActualTypeArguments()[0];
    }

    /**
     * 将 Map List 转化为 Bean List
     */
    private static <T> List<T> toBeanList(List<Map<String, Object>> mapList, Class<T> clazz) {
        List<T> beanList = new ArrayList<>();
        if (mapList != null) {
            for (Map<String, Object> map : mapList) {
                beanList.add(toBean(clazz, map));
            }
        }
        return beanList;
    }

    /**
     * 将 Bean List 转化为 Map List
     */
    private static List<Map<String, Object>> toMapList(List<?> beanList) {
        List<Map<String, Object>> mapList = new ArrayList<>();
        if (beanList != null) {
            for (Object bean : beanList) {
                mapList.add(toMap(bean));
            }
        }
        return mapList;
    }
}
