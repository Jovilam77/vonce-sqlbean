package cn.vonce.sql.java.datasource;

/**
 * 事务持有者
 *
 * @author Jovi
 * @email imjovi@qq.com
 * @date 2022/12/14 13:57
 */
public class TransactionalContextHolder {

    private static final ThreadLocal<String> contextHolder = new ThreadLocal<>();

    /**
     * 设置当前xid
     *
     * @param xid
     */
    public static void setXid(String xid) {
        contextHolder.set(xid);
    }

    /**
     * 根据获取源
     *
     * @return
     */
    public static String getXid() {
        return contextHolder.get();
    }

    /**
     * 清除xid
     */
    public static void clearXid() {
        contextHolder.remove();
    }

}
