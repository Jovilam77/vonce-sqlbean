package cn.vonce.sql.uitls;

import java.lang.reflect.*;
import java.math.BigDecimal;
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

            // 获取所有字段（包括私有字段）
            List<Field> fields = getAllFields(clazz);

            for (Field field : fields) {
                String fieldName = field.getName();
                if (map.containsKey(fieldName)) {
                    Object value = map.get(fieldName);
                    if (value != null) {
                        Class<?> fieldType = field.getType();

                        // 处理嵌套的 Bean
                        if (value instanceof Map && isBean(fieldType)) {
                            value = toBean(fieldType, (Map<String, Object>) value);
                        }
                        // 处理 List<Bean>
                        else if (List.class.isAssignableFrom(fieldType) && value instanceof List) {
                            Type genericType = field.getGenericType();
                            if (genericType instanceof ParameterizedType) {
                                ParameterizedType parameterizedType = (ParameterizedType) genericType;
                                Type actualType = parameterizedType.getActualTypeArguments()[0];

                                if (actualType instanceof Class && isBean((Class<?>) actualType)) {
                                    value = toBeanList((List<Map<String, Object>>) value, (Class<?>) actualType);
                                }
                            }
                        }
                    }

                    // 设置字段值
                    try {
                        // 处理枚举类型
                        if (field.getType().isEnum() && value != null) {
                            Object[] constants = field.getType().getEnumConstants();
                            for (Object constant : constants) {
                                if (constant.toString().equals(value.toString())) {
                                    value = constant;
                                    break;
                                }
                            }
                        }

                        // 尝试直接设置字段值
                        field.setAccessible(true);
                        field.set(obj, convertValue(field.getType(), value));
                    } catch (Exception e) {
                        try {
                            // 尝试通过setter方法设置值
                            String setterName = "set" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
                            Method setter = clazz.getMethod(setterName, field.getType());
                            setter.invoke(obj, convertValue(field.getType(), value));
                        } catch (Exception ex) {
                            System.err.println("Failed to set property: " + fieldName + " - " + ex.getMessage());
                        }
                    }
                }
            }
            return obj;
        } catch (Exception e) {
            throw new RuntimeException("Failed to instantiate or populate JavaBean: " + e.getMessage(), e);
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
        Class<?> clazz = bean.getClass();

        // 获取所有字段（包括私有字段）
        List<Field> fields = getAllFields(clazz);

        for (Field field : fields) {
            String fieldName = field.getName();
            if ("class".equals(fieldName)) {
                continue;
            }

            try {
                // 获取字段值
                field.setAccessible(true);
                Object value = field.get(bean);

                if (value != null) {
                    // 处理嵌套的 Bean
                    if (isBean(value.getClass())) {
                        value = toMap(value);
                    }
                    // 处理 List<Bean>
                    else if (value instanceof List<?>) {
                        value = toMapList((List<?>) value);
                    }
                }

                resultMap.put(fieldName, value);
            } catch (Exception e) {
                try {
                    // 尝试通过getter方法获取值
                    String getterName = getGetterName(field);
                    Method getter = clazz.getMethod(getterName);
                    Object value = getter.invoke(bean);

                    if (value != null) {
                        if (isBean(value.getClass())) {
                            value = toMap(value);
                        } else if (value instanceof List<?>) {
                            value = toMapList((List<?>) value);
                        }
                    }

                    resultMap.put(fieldName, value);
                } catch (Exception ex) {
                    System.err.println("Failed to read property: " + fieldName + " - " + ex.getMessage());
                }
            }
        }

        return resultMap;
    }

    /**
     * 获取类的所有字段（包括父类的字段）
     */
    private static List<Field> getAllFields(Class<?> clazz) {
        List<Field> fields = new ArrayList<>();
        Class<?> currentClass = clazz;

        while (currentClass != null && currentClass != Object.class) {
            fields.addAll(Arrays.asList(currentClass.getDeclaredFields()));
            currentClass = currentClass.getSuperclass();
        }

        return fields;
    }

    /**
     * 获取getter方法名
     */
    private static String getGetterName(Field field) {
        String fieldName = field.getName();
        if (field.getType() == boolean.class) {
            return "is" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
        } else {
            return "get" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
        }
    }

    /**
     * 判断是否是 Bean 类型（非基础类型或集合类型）
     */
    private static boolean isBean(Class<?> clazz) {
        return !(clazz.isPrimitive() || clazz.equals(String.class) || Number.class.isAssignableFrom(clazz)
                || Boolean.class.equals(clazz) || Character.class.equals(clazz) || Enum.class.isAssignableFrom(clazz)
                || Calendar.class.isAssignableFrom(clazz) || Collection.class.isAssignableFrom(clazz)
                || Map.class.isAssignableFrom(clazz) || BigDecimal.class.isAssignableFrom(clazz)
                || isDate(clazz));
    }

    private static boolean isDate(Class<?> clazz) {
        if (!SqlBeanUtil.isAndroidEnv()) {
            return (Date.class.isAssignableFrom(clazz) || java.time.LocalDate.class.isAssignableFrom(clazz)
                    || java.time.LocalDateTime.class.isAssignableFrom(clazz)
                    || java.time.LocalTime.class.isAssignableFrom(clazz));
        }
        return (Date.class.isAssignableFrom(clazz));
    }

    /**
     * 将 Map List 转化为 Bean List
     */
    @SuppressWarnings("unchecked")
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

    /**
     * 转换值为目标类型
     */
    private static Object convertValue(Class<?> targetType, Object value) {
        if (value == null) {
            return null;
        }

        // 如果类型已经匹配，直接返回
        if (targetType.isInstance(value)) {
            return value;
        }

        // 使用SqlBeanUtil进行类型转换
        try {
            return SqlBeanUtil.getValueConvert(targetType, value);
        } catch (Exception e) {
            System.err.println("Failed to convert value " + value + " to type " + targetType.getName() + ": " + e.getMessage());
            return value;
        }
    }
}
