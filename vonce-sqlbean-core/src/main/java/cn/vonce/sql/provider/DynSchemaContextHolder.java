package cn.vonce.sql.provider;

/**
 * 动态Schema持有者
 *
 * @author Jovi
 * @version 1.0
 * @email imjovi@qq.com
 * @date 2021/6/19 00:23
 */
public class DynSchemaContextHolder {

    private static final ThreadLocal<String> contextHolder = new ThreadLocal<>();

    /**
     * 设置当前Schema
     *
     * @param schema
     */
    public static void setSchema(String schema) {
        contextHolder.set(schema);
    }

    /**
     * 获取Schema
     *
     * @return
     */
    public static String getSchema() {
        return contextHolder.get();
    }

    /**
     * 清除Schema
     */
    public static void clearSchema() {
        contextHolder.remove();
    }

}
