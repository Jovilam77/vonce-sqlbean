package cn.vonce.sql.orm.provider;

import cn.vonce.sql.bean.Delete;
import cn.vonce.sql.bean.Paging;
import cn.vonce.sql.bean.Select;
import cn.vonce.sql.bean.Update;
import cn.vonce.sql.config.SqlBeanConfig;

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
        return super.selectByIdSql((SqlBeanConfig) map.get("sqlBeanConfig"), (Class<?>) map.get("clazz"), map.get("id"));
    }

    /**
     * 根据id条件查询
     *
     * @param map
     * @return
     */
    public String selectByIds(Map<String, Object> map) {
        return super.selectByIdsSql((SqlBeanConfig) map.get("sqlBeanConfig"), (Class<?>) map.get("clazz"), (Object[]) map.get("ids"));
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
        return super.selectByConditionSql((SqlBeanConfig) map.get("sqlBeanConfig"), (Class<?>) map.get("clazz"), paging, (String) map.get("where"), args);
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
        return super.selectCountByConditionSql((SqlBeanConfig) map.get("sqlBeanConfig"), (Class<?>) map.get("clazz"), (String) map.get("where"), args);
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
        return super.selectAllSql((SqlBeanConfig) map.get("sqlBeanConfig"), (Class<?>) map.get("clazz"), paging);
    }

    /**
     * 根据自定义条件查询（可自动分页）
     *
     * @param map
     * @return
     */
    public String select(Map<String, Object> map) {
        return super.selectSql((SqlBeanConfig) map.get("sqlBeanConfig"), (Class<?>) map.get("clazz"), (Select) map.get("select"));
    }

    /**
     * 根据自定义条件统计
     *
     * @param map
     * @return
     */
    public String count(Map<String, Object> map) {
        return super.countSql((SqlBeanConfig) map.get("sqlBeanConfig"), (Class<?>) map.get("clazz"), (Select) map.get("select"));
    }

    /**
     * 根据id条件删除
     *
     * @param map
     * @return
     */
    public String deleteById(Map<String, Object> map) {
        return super.deleteByIdSql((SqlBeanConfig) map.get("sqlBeanConfig"), (Class<?>) map.get("clazz"), map.get("id"));
    }

    /**
     * 根据条件删除
     *
     * @param map
     * @return
     */
    public String deleteByCondition(Map<String, Object> map) {
        Object[] args = null;
        if (map.containsKey("args")) {
            args = (Object[]) map.get("args");
        }
        return super.deleteByConditionSql((SqlBeanConfig) map.get("sqlBeanConfig"), (Class<?>) map.get("clazz"), (String) map.get("where"), args);
    }

    /**
     * 删除
     *
     * @param map
     * @return
     */
    public String delete(Map<String, Object> map) {
        return super.deleteSql((SqlBeanConfig) map.get("sqlBeanConfig"), (Class<?>) map.get("clazz"), (Delete) map.get("delete"), (boolean) map.get("ignore"));
    }

    /**
     * 逻辑删除
     *
     * @param map
     * @return
     */
    public String logicallyDeleteById(Map<String, Object> map) {
        return super.logicallyDeleteByIdSql((SqlBeanConfig) map.get("sqlBeanConfig"), (Class<?>) map.get("clazz"), map.get("id"));
    }

    /**
     * 根据条件逻辑删除
     *
     * @param map
     * @return
     */
    public String logicallyDeleteByCondition(Map<String, Object> map) {
        Object[] args = null;
        if (map.containsKey("args")) {
            args = (Object[]) map.get("args");
        }
        return super.logicallyDeleteByConditionSql((SqlBeanConfig) map.get("sqlBeanConfig"), (Class<?>) map.get("clazz"), (String) map.get("where"), args);
    }

    /**
     * 更新
     *
     * @param map
     * @return
     */
    public String update(Map<String, Object> map) {
        return super.updateSql((SqlBeanConfig) map.get("sqlBeanConfig"), (Update) map.get("update"), (boolean) map.get("ignore"));
    }

    /**
     * 根据id条件更新
     *
     * @param map
     * @return
     */
    public String updateById(Map<String, Object> map) {
        return super.updateByIdSql((SqlBeanConfig) map.get("sqlBeanConfig"), map.get("bean"), map.get("id"), (boolean) map.get("updateNotNull"), (String[]) map.get("filterFields"));
    }

    /**
     * 根据实体类id条件更新
     *
     * @param map
     * @return
     */
    public String updateByBeanId(Map<String, Object> map) {
        return super.updateByBeanIdSql((SqlBeanConfig) map.get("sqlBeanConfig"), map.get("bean"), (boolean) map.get("updateNotNull"), (String[]) map.get("filterFields"));
    }

    /**
     * 根据条件更新
     *
     * @param map
     * @return
     */
    public String updateByCondition(Map<String, Object> map) {
        Object[] args = null;
        if (map.containsKey("args")) {
            args = (Object[]) map.get("args");
        }
        return super.updateByConditionSql((SqlBeanConfig) map.get("sqlBeanConfig"), map.get("bean"), (boolean) map.get("updateNotNull"), (String[]) map.get("filterFields"), (String) map.get("where"), args);
    }

    /**
     * 根据实体类字段条件更新
     *
     * @param map
     * @return
     */
    public String updateByBeanCondition(Map<String, Object> map) {
        return super.updateByBeanConditionSql((SqlBeanConfig) map.get("sqlBeanConfig"), map.get("bean"), (boolean) map.get("updateNotNull"), (String[]) map.get("filterFields"), (String) map.get("where"));
    }

    /**
     * 插入数据
     *
     * @param map
     * @return
     */
    public String insertBean(Map<String, Object> map) {
        return super.insertBeanSql((SqlBeanConfig) map.get("sqlBeanConfig"), map.get("beanList"));
    }

    /**
     * 插入数据
     *
     * @param map
     * @return
     */
    public String insert(Map<String, Object> map) {
        return super.insertBeanSql((SqlBeanConfig) map.get("sqlBeanConfig"), map.get("insert"));
    }

    /**
     * 删除表
     *
     * @param map
     * @return
     */
    public String drop(Map<String, Object> map) {
        return super.dropTableSql((SqlBeanConfig) map.get("sqlBeanConfig"), (Class<?>) map.get("clazz"));
    }

    /**
     * 创建表
     *
     * @param map
     * @return
     */
    public String create(Map<String, Object> map) {
        return super.createTableSql((SqlBeanConfig) map.get("sqlBeanConfig"), (Class<?>) map.get("clazz"));
    }

}
