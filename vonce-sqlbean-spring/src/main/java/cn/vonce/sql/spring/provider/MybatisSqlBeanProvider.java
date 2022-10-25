package cn.vonce.sql.spring.provider;

import cn.vonce.sql.bean.*;
import cn.vonce.sql.config.SqlBeanDB;
import cn.vonce.sql.helper.Wrapper;
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
public class MybatisSqlBeanProvider {

    /**
     * 根据id条件查询
     *
     * @param map
     * @return
     */
    public String selectById(Map<String, Object> map) {
        Class<?> returnType = null;
        if (map.containsKey("returnType")) {
            returnType = (Class<?>) map.get("returnType");
        }
        return SqlBeanProvider.selectByIdSql((SqlBeanDB) map.get("sqlBeanDB"), (Class<?>) map.get("clazz"), returnType, map.get("id"));
    }

    /**
     * 根据id条件查询
     *
     * @param map
     * @return
     */
    public String selectByIds(Map<String, Object> map) {
        Class<?> returnType = null;
        if (map.containsKey("returnType")) {
            returnType = (Class<?>) map.get("returnType");
        }
        return SqlBeanProvider.selectByIdsSql((SqlBeanDB) map.get("sqlBeanDB"), (Class<?>) map.get("clazz"), returnType, (Object[]) map.get("ids"));
    }

    /**
     * 根据条件查询
     *
     * @param map
     * @return
     */
    public String selectBy(Map<String, Object> map) {
        Paging paging = null;
        Class<?> returnType = null;
        if (map.containsKey("paging")) {
            paging = (Paging) map.get("paging");
        }
        if (map.containsKey("returnType")) {
            returnType = (Class<?>) map.get("returnType");
        }
        Object[] args = null;
        if (map.containsKey("args")) {
            args = (Object[]) map.get("args");
        }
        return SqlBeanProvider.selectBySql((SqlBeanDB) map.get("sqlBeanDB"), (Class<?>) map.get("clazz"), returnType, paging, (String) map.get("where"), args);
    }

    /**
     * 根据条件查询统计
     *
     * @param map
     * @return
     */
    public String countBy(Map<String, Object> map) {
        Object[] args = null;
        if (map.containsKey("args")) {
            args = (Object[]) map.get("args");
        }
        return SqlBeanProvider.countBySql((SqlBeanDB) map.get("sqlBeanDB"), (Class<?>) map.get("clazz"), (String) map.get("where"), args);
    }

    /**
     * 查询全部
     *
     * @param map
     * @return
     */
    public String selectAll(Map<String, Object> map) {
        Paging paging = null;
        Class<?> returnType = null;
        if (map.containsKey("paging")) {
            paging = (Paging) map.get("paging");
        }
        if (map.containsKey("returnType")) {
            returnType = (Class<?>) map.get("returnType");
        }
        return SqlBeanProvider.selectAllSql((SqlBeanDB) map.get("sqlBeanDB"), (Class<?>) map.get("clazz"), returnType, paging);
    }

    /**
     * 根据自定义条件查询（可自动分页）
     *
     * @param map
     * @return
     */
    public String select(Map<String, Object> map) {
        Class<?> returnType = null;
        if (map.containsKey("returnType")) {
            returnType = (Class<?>) map.get("returnType");
        }
        return SqlBeanProvider.selectSql((SqlBeanDB) map.get("sqlBeanDB"), (Class<?>) map.get("clazz"), returnType, (Select) map.get("select"));
    }

    /**
     * 根据自定义条件统计
     *
     * @param map
     * @return
     */
    public String count(Map<String, Object> map) {
        Class<?> returnType = null;
        if (map.containsKey("returnType")) {
            returnType = (Class<?>) map.get("returnType");
        }
        return SqlBeanProvider.countSql((SqlBeanDB) map.get("sqlBeanDB"), (Class<?>) map.get("clazz"), returnType, (Select) map.get("select"));
    }

    /**
     * 根据id条件删除
     *
     * @param map
     * @return
     */
    public String deleteById(Map<String, Object> map) {
        return SqlBeanProvider.deleteByIdSql((SqlBeanDB) map.get("sqlBeanDB"), (Class<?>) map.get("clazz"), map.get("id"));
    }

    /**
     * 根据条件删除
     *
     * @param map
     * @return
     */
    public String deleteBy(Map<String, Object> map) {
        Object[] args = null;
        if (map.containsKey("args")) {
            args = (Object[]) map.get("args");
        }
        return SqlBeanProvider.deleteBySql((SqlBeanDB) map.get("sqlBeanDB"), (Class<?>) map.get("clazz"), (String) map.get("where"), args);
    }

    /**
     * 删除
     *
     * @param map
     * @return
     */
    public String delete(Map<String, Object> map) {
        return SqlBeanProvider.deleteSql((SqlBeanDB) map.get("sqlBeanDB"), (Class<?>) map.get("clazz"), (Delete) map.get("delete"), (boolean) map.get("ignore"));
    }

    /**
     * 逻辑删除
     *
     * @param map
     * @return
     */
    public String logicallyDeleteById(Map<String, Object> map) {
        return SqlBeanProvider.logicallyDeleteByIdSql((SqlBeanDB) map.get("sqlBeanDB"), (Class<?>) map.get("clazz"), map.get("id"));
    }

    /**
     * 根据条件逻辑删除
     *
     * @param map
     * @return
     */
    public String logicallyDeleteBy(Map<String, Object> map) {
        Object[] args = null;
        if (map.containsKey("args")) {
            args = (Object[]) map.get("args");
        }
        return SqlBeanProvider.logicallyDeleteBySql((SqlBeanDB) map.get("sqlBeanDB"), (Class<?>) map.get("clazz"), (String) map.get("where"), args);
    }

    /**
     * 根据条件逻辑删除
     *
     * @param map
     * @return
     */
    public String logicallyDeleteByWrapper(Map<String, Object> map) {
        return SqlBeanProvider.logicallyDeleteBySql((SqlBeanDB) map.get("sqlBeanDB"), (Class<?>) map.get("clazz"), (Wrapper) map.get("wrapper"));
    }

    /**
     * 更新
     *
     * @param map
     * @return
     */
    public String update(Map<String, Object> map) {
        return SqlBeanProvider.updateSql((SqlBeanDB) map.get("sqlBeanDB"), (Class<?>) map.get("clazz"), (Update) map.get("update"), (boolean) map.get("ignore"));
    }

    /**
     * 根据id条件更新
     *
     * @param map
     * @return
     */
    public String updateById(Map<String, Object> map) {
        return SqlBeanProvider.updateByIdSql((SqlBeanDB) map.get("sqlBeanDB"), (Class<?>) map.get("clazz"), map.get("bean"), map.get("id"), (boolean) map.get("updateNotNull"), (boolean) map.get("optimisticLock"), (String[]) map.get("filterFields"));
    }

    /**
     * 根据实体类id条件更新
     *
     * @param map
     * @return
     */
    public String updateByBeanId(Map<String, Object> map) {
        return SqlBeanProvider.updateByBeanIdSql((SqlBeanDB) map.get("sqlBeanDB"), (Class<?>) map.get("clazz"), map.get("bean"), (boolean) map.get("updateNotNull"), (boolean) map.get("optimisticLock"), (String[]) map.get("filterFields"));
    }

    /**
     * 根据条件更新
     *
     * @param map
     * @return
     */
    public String updateBy(Map<String, Object> map) {
        Object[] args = null;
        if (map.containsKey("args")) {
            args = (Object[]) map.get("args");
        }
        return SqlBeanProvider.updateBySql((SqlBeanDB) map.get("sqlBeanDB"), (Class<?>) map.get("clazz"), map.get("bean"), (boolean) map.get("updateNotNull"), (boolean) map.get("optimisticLock"), (String[]) map.get("filterFields"), (String) map.get("where"), args);
    }

    /**
     * 根据实体类字段条件更新
     *
     * @param map
     * @return
     */
    public String updateByBean(Map<String, Object> map) {
        return SqlBeanProvider.updateByBeanSql((SqlBeanDB) map.get("sqlBeanDB"), (Class<?>) map.get("clazz"), map.get("bean"), (boolean) map.get("updateNotNull"), (boolean) map.get("optimisticLock"), (String[]) map.get("filterFields"), (String) map.get("where"));
    }

    /**
     * 插入数据
     *
     * @param map
     * @return
     */
    public String insertBean(Map<String, Object> map) {
        return SqlBeanProvider.insertBeanSql((SqlBeanDB) map.get("sqlBeanDB"), (Class<?>) map.get("clazz"), map.get("beanList"));
    }

    /**
     * 插入数据
     *
     * @param map
     * @return
     */
    public String insert(Map<String, Object> map) {
        return SqlBeanProvider.insertBeanSql((SqlBeanDB) map.get("sqlBeanDB"), (Class<?>) map.get("clazz"), map.get("insert"));
    }

    /**
     * 删除表
     *
     * @param map
     * @return
     */
    public String drop(Map<String, Object> map) {
        return SqlBeanProvider.dropTableSql((SqlBeanDB) map.get("sqlBeanDB"), (Class<?>) map.get("clazz"));
    }

    /**
     * 创建表
     *
     * @param map
     * @return
     */
    public String create(Map<String, Object> map) {
        return SqlBeanProvider.createTableSql((SqlBeanDB) map.get("sqlBeanDB"), (Class<?>) map.get("clazz"));
    }

    /**
     * 获取表名列表
     *
     * @param map
     * @return
     */
    public String selectTableList(Map<String, Object> map) {
        return SqlBeanProvider.selectTableListSql((SqlBeanDB) map.get("sqlBeanDB"), null, (String) map.get("name"));
    }

    /**
     * 获取列信息列表
     *
     * @param map
     * @return
     */
    public String selectColumnInfoList(Map<String, Object> map) {
        return SqlBeanProvider.selectColumnListSql((SqlBeanDB) map.get("sqlBeanDB"), (String) map.get("name"));
    }

    /**
     * 备份表和数据
     *
     * @param map
     * @return
     */
    public String backup(Map<String, Object> map) {
        return SqlBeanProvider.backupSql((SqlBeanDB) map.get("sqlBeanDB"), (Class<?>) map.get("clazz"), (String) map.get("targetSchema"), (String) map.get("targetTableName"), (Column[]) map.get("columns"), (Wrapper) map.get("wrapper"));
    }

    /**
     * 复制数据到指定表
     *
     * @param map
     * @return
     */
    public String copy(Map<String, Object> map) {
        return SqlBeanProvider.copySql((SqlBeanDB) map.get("sqlBeanDB"), (Class<?>) map.get("clazz"), (String) map.get("targetSchema"), (String) map.get("targetTableName"), (Column[]) map.get("columns"), (Wrapper) map.get("wrapper"));
    }

    /**
     * 执行Sql
     *
     * @param sql
     * @return
     */
    public String executeSql(String sql) {
        return sql;
    }

}
