package cn.vonce.sql.spring.provider;

import cn.vonce.sql.bean.*;
import cn.vonce.sql.config.SqlBeanDB;
import cn.vonce.sql.provider.SqlBeanProvider;

import java.util.Map;

/**
 * 通用的数据库操作sql语句生成
 *
 * @author Jovi
 * @version 1.0
 * @email 766255988@qq.com
 * @date 2018年5月15日下午2:23:47
 */
public class MybatisSqlBeanProvider extends SqlBeanProvider {

    /**
     * 根据id条件查询
     *
     * @param map
     * @return
     */
    public String selectById(Map<String, Object> map) {
        return super.selectByIdSql((SqlBeanDB) map.get("sqlBeanDB"), (Class<?>) map.get("clazz"), map.get("id"));
    }

    /**
     * 根据id条件查询
     *
     * @param map
     * @return
     */
    public String selectByIds(Map<String, Object> map) {
        return super.selectByIdsSql((SqlBeanDB) map.get("sqlBeanDB"), (Class<?>) map.get("clazz"), (Object[]) map.get("ids"));
    }

    /**
     * 根据条件查询
     *
     * @param map
     * @return
     */
    public String selectByCondition(Map<String, Object> map) {
        Paging paging = null;
        if (map.containsKey("paging")) {
            paging = (Paging) map.get("paging");
        }
        Object[] args = null;
        if (map.containsKey("args")) {
            args = (Object[]) map.get("args");
        }
        return super.selectByConditionSql((SqlBeanDB) map.get("sqlBeanDB"), (Class<?>) map.get("clazz"), paging, (String) map.get("where"), args);
    }

    /**
     * 根据条件查询统计
     *
     * @param map
     * @return
     */
    public String selectCountByCondition(Map<String, Object> map) {
        Object[] args = null;
        if (map.containsKey("args")) {
            args = (Object[]) map.get("args");
        }
        return super.selectCountByConditionSql((SqlBeanDB) map.get("sqlBeanDB"), (Class<?>) map.get("clazz"), (String) map.get("where"), args);
    }

    /**
     * 查询全部
     *
     * @param map
     * @return
     */
    public String selectAll(Map<String, Object> map) {
        Paging paging = null;
        if (map.containsKey("paging")) {
            paging = (Paging) map.get("paging");
        }
        return super.selectAllSql((SqlBeanDB) map.get("sqlBeanDB"), (Class<?>) map.get("clazz"), paging);
    }

    /**
     * 根据自定义条件查询（可自动分页）
     *
     * @param map
     * @return
     */
    public String select(Map<String, Object> map) {
        return super.selectSql((SqlBeanDB) map.get("sqlBeanDB"), (Class<?>) map.get("clazz"), (Select) map.get("select"));
    }

}
