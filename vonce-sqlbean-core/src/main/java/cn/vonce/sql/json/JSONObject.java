package cn.vonce.sql.json;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;

import cn.vonce.sql.uitls.BeanUtil;

/**
 * JSONObject
 *
 * @author Jovi
 * @email imjovi@qq.com
 * @date 2024/11/22 19:21
 */
public class JSONObject extends JSONParser implements Map<String, Object>, Cloneable, Serializable {

    private static final int DEFAULT_INITIAL_CAPACITY = 16;

    private final Map<String, Object> map;

    public JSONObject() {
        this(DEFAULT_INITIAL_CAPACITY, false);
    }

    public JSONObject(Map<String, Object> map) {
        if (map == null) {
            throw new IllegalArgumentException("map is null.");
        }
        this.map = map;
    }

    public JSONObject(boolean ordered) {
        this(DEFAULT_INITIAL_CAPACITY, ordered);
    }

    public JSONObject(int initialCapacity) {
        this(initialCapacity, false);
    }

    public JSONObject(int initialCapacity, boolean ordered) {
        if (ordered) {
            this.map = new LinkedHashMap<String, Object>(initialCapacity);
        } else {
            this.map = new HashMap<String, Object>(initialCapacity);
        }
    }

    @Override
    public int size() {
        return this.map.size();
    }

    @Override
    public boolean isEmpty() {
        return this.map.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        boolean result = this.map.containsKey(key);
        if (!result) {
            if (key instanceof Number
                    || key instanceof Character
                    || key instanceof Boolean
                    || key instanceof UUID
            ) {
                result = this.map.containsKey(key.toString());
            }
        }
        return result;
    }

    @Override
    public boolean containsValue(Object value) {
        return this.map.containsValue(value);
    }

    @Override
    public Object get(Object key) {
        Object val = this.map.get(key);
        if (val == null) {
            if (key instanceof Number
                    || key instanceof Character
                    || key instanceof Boolean
                    || key instanceof UUID
            ) {
                val = this.map.get(key.toString());
            }
        }
        return val;
    }

    @Override
    public Object put(String key, Object value) {
        return this.map.put(key, value);
    }

    @Override
    public Object remove(Object key) {
        return this.remove(key.toString());
    }

    @Override
    public void putAll(Map<? extends String, ?> m) {
        this.map.putAll(m);
    }

    @Override
    public void clear() {
        this.map.clear();
    }

    @Override
    public Set<String> keySet() {
        return this.map.keySet();
    }

    @Override
    public Collection<Object> values() {
        return this.map.values();
    }

    @Override
    public Set<Entry<String, Object>> entrySet() {
        return this.map.entrySet();
    }

    @Override
    public String toString() {
        return this.map.toString();
    }

    public String getString(String key) {
        Object value = this.map.get(key);
        if (value == null) {
            return null;
        }
        if (value instanceof String) {
            String str = (String) value;
            if (str.length() == 0 || str.equalsIgnoreCase("null")) {
                return null;
            }
        }
        return value.toString();
    }

    public Integer getInteger(String key) {
        Object value = this.map.get(key);
        if (value == null) {
            return null;
        }
        if (value instanceof Integer) {
            return (Integer) value;
        } else if (value instanceof Number) {
            return ((Number) value).intValue();
        } else if (value instanceof String) {
            String str = (String) value;
            if (str.length() == 0 || str.equalsIgnoreCase("null")) {
                return null;
            }
            if (str.indexOf(',') != -1) {
                str = str.replaceAll(",", "");
            }
            return Integer.valueOf(str);
        } else if (value instanceof Boolean) {
            return (Boolean) value ? 1 : 0;
        }
        return Integer.valueOf(value.toString());
    }

    public Long getLong(String key) {
        Object value = this.map.get(key);
        if (value == null) {
            return null;
        }
        if (value instanceof Long) {
            return (Long) value;
        } else if (value instanceof Number) {
            return ((Number) value).longValue();
        } else if (value instanceof String) {
            String str = (String) value;
            if (str.length() == 0 || str.equalsIgnoreCase("null")) {
                return null;
            }
            if (str.indexOf(',') != -1) {
                str = str.replaceAll(",", "");
            }
            return Long.valueOf(str);
        } else if (value instanceof Boolean) {
            return (Boolean) value ? 1L : 0L;
        }
        return Long.valueOf(value.toString());
    }

    public Double getDouble(String key) {
        Object value = this.map.get(key);
        if (value == null) {
            return null;
        }
        if (value instanceof Double) {
            return (Double) value;
        } else if (value instanceof Number) {
            return ((Number) value).doubleValue();
        } else if (value instanceof String) {
            String str = (String) value;
            if (str.length() == 0 || str.equalsIgnoreCase("null")) {
                return null;
            }
            if (str.indexOf(',') != -1) {
                str = str.replaceAll(",", "");
            }
            return Double.valueOf(str);
        } else if (value instanceof Boolean) {
            return (Boolean) value ? 1D : 0D;
        }
        return Double.valueOf(value.toString());
    }

    public Float getFloat(String key) {
        Object value = this.map.get(key);
        if (value == null) {
            return null;
        }
        if (value instanceof Float) {
            return (Float) value;
        } else if (value instanceof Number) {
            return ((Number) value).floatValue();
        } else if (value instanceof String) {
            String str = (String) value;
            if (str.length() == 0 || str.equalsIgnoreCase("null")) {
                return null;
            }
            if (str.indexOf(',') != -1) {
                str = str.replaceAll(",", "");
            }
            return Float.valueOf(str);
        } else if (value instanceof Boolean) {
            return (Boolean) value ? 1F : 0F;
        }
        return Float.valueOf(value.toString());
    }

    public Short getShort(String key) {
        Object value = this.map.get(key);
        if (value == null) {
            return null;
        }
        if (value instanceof Short) {
            return (Short) value;
        } else if (value instanceof Number) {
            return ((Number) value).shortValue();
        } else if (value instanceof String) {
            String str = (String) value;
            if (str.length() == 0 || str.equalsIgnoreCase("null")) {
                return null;
            }
            if (str.indexOf(',') != -1) {
                str = str.replaceAll(",", "");
            }
            return Short.valueOf(str);
        } else if (value instanceof Boolean) {
            return (Boolean) value ? (short) 1 : (short) 0;
        }
        return Short.valueOf(value.toString());
    }

    public Byte getByte(String key) {
        Object value = this.map.get(key);
        if (value == null) {
            return null;
        }
        if (value instanceof Byte) {
            return (Byte) value;
        } else if (value instanceof Number) {
            return ((Number) value).byteValue();
        } else if (value instanceof String) {
            String str = (String) value;
            if (str.length() == 0 || str.equalsIgnoreCase("null")) {
                return null;
            }
            if (str.indexOf(',') != -1) {
                str = str.replaceAll(",", "");
            }
            return Byte.valueOf(str);
        } else if (value instanceof Boolean) {
            return (Boolean) value ? (byte) 1 : (byte) 0;
        }
        return Byte.valueOf(value.toString());
    }

    public BigDecimal getBigDecimal(String key) {
        Object value = this.map.get(key);
        if (value == null) {
            return null;
        }
        if (value instanceof BigDecimal) {
            return ((BigDecimal) value);
        }
        return new BigDecimal(value.toString());
    }

    public Boolean getBoolean(String key) {
        Object value = this.map.get(key);
        if (value == null) {
            return null;
        }
        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        if (value instanceof BigDecimal) {
            return ((BigDecimal) value).intValue() == 1;
        }
        if (value instanceof Number) {
            return ((Number) value).intValue() == 1;
        }
        if (value instanceof String) {
            String str = (String) value;
            if (str.length() == 0 || str.equalsIgnoreCase("null")) {
                return null;
            }
            if (str.indexOf(',') != -1) {
                str = str.replaceAll(",", "");
            }
            if ("true".equalsIgnoreCase(str) || "1".equals(str)) {
                return Boolean.TRUE;
            }
            if ("false".equalsIgnoreCase(str) || "0".equals(str)) {
                return Boolean.FALSE;
            }
            if ("Y".equalsIgnoreCase(str) || "T".equals(str)) {
                return Boolean.TRUE;
            }
            if ("F".equalsIgnoreCase(str) || "N".equals(str)) {
                return Boolean.FALSE;
            }
        }
        return Boolean.valueOf(value.toString());
    }

    public JSONObject getJSONObject(String key) {
        Object value = this.map.get(key);
        if (value == null) {
            return null;
        }
        return value instanceof JSONObject ? (JSONObject) value : null;
    }

    public JSONArray getJSONArray(String key) {
        Object value = this.map.get(key);
        if (value == null) {
            return null;
        }
        return value instanceof JSONArray ? (JSONArray) value : null;
    }

    public <T> T toJavaBean(Class<T> clazz) {
        return BeanUtil.toBean(clazz, this);
    }

    public static JSONObject parseObject(Object obj) {
        return JSONObject.parseObject(JSONParser.toJSONString(obj));
    }

}
