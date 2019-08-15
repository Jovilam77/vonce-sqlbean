package cn.vonce.sql.bean;

import cn.vonce.common.uitls.StringUtil;
import cn.vonce.sql.config.SqlBeanConfig;
import cn.vonce.sql.constant.SqlHelperCons;
import cn.vonce.sql.enumerate.DbType;
import cn.vonce.sql.enumerate.JoinType;
import cn.vonce.sql.enumerate.SqlLogic;
import cn.vonce.sql.enumerate.SqlSort;
import cn.vonce.sql.uitls.SqlBeanUtil;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.ListMultimap;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * column
 *
 * @author Jovi
 * @version 1.0
 * @email 766255988@qq.com
 * @date 2017年8月18日上午9:00:19
 */
public class Select extends Common implements Serializable {

    private static final long serialVersionUID = 1L;

    private boolean useDistinct = false;//默认不去重复
    private boolean customMode = true;//默认为自定义模式
    private String[] from = null;//来自哪张表
    private List<String> columnList = new ArrayList<>();//查询的字段数组
    private List<String> innerJoinList = new ArrayList<>();//内连接的表数组
    private List<String> fullJoinList = new ArrayList<>();//全连接的表数组
    private List<String> leftJoinList = new ArrayList<>();//左连接的表数组
    private List<String> rightJoinList = new ArrayList<>();//右连接的表数组
    private List<String> groupByList = new ArrayList<>();//分组
    private List<String> orderByList = new ArrayList<>();//排序
    private Page page = null;
    private String having = null;//条件
    private ListMultimap<String, SqlCondition> havingMap = LinkedListMultimap.create();//having条件包含的逻辑
    private String[] filterFields = null;//需要过滤的字段

    /**
     * 获取useDistinct是否过滤重复
     *
     * @return
     * @author Jovi
     * @date 2017年8月18日下午4:21:19
     */
    public boolean isUseDistinct() {
        return useDistinct;
    }

    /**
     * 设置useDistinct是否过滤重复
     *
     * @param useDistinct
     * @author Jovi
     * @date 2017年8月18日下午4:21:05
     */
    public void setUseDistinct(boolean useDistinct) {
        this.useDistinct = useDistinct;
    }

    /**
     * 获取是否为自定义模式
     *
     * @return
     */
    public boolean isCustomMode() {
        return customMode;
    }

    /**
     * 设置是否为自定义模式
     *
     * @param customMode
     */
    public void setCustomMode(boolean customMode) {
        this.customMode = customMode;
    }

    /**
     * 获取from sql 内容
     *
     * @return
     * @author Jovi
     * @date 2017年8月18日上午8:59:49
     */
    public String[] getFrom() {
        return from;
    }

    /**
     * 设置from sql 内容
     *
     * @param from
     * @author Jovi
     * @date 2017年8月18日上午8:59:38
     */
    public void setFrom(String... from) {
        this.from = from;
    }

    /**
     * 设置from sql 内容
     *
     * @param clazz
     * @author Jovi
     * @date 2018年5月14日下午11:54:45
     */
    public void setFrom(Class<?> clazz) {
        this.from = new String[1];
        this.from[0] = SqlBeanUtil.getTableName(clazz);
    }

    /**
     * 获取column sql 内容
     *
     * @return
     * @author Jovi
     * @date 2017年8月18日上午9:00:05
     */
    public List<String> getColumn() {
        return columnList;
    }

    /**
     * 设置column sql 内容
     *
     * @param column
     * @author Jovi
     * @date 2017年8月18日上午8:59:56
     */
    public void setColumn(String... column) {
        for (String string : column) {
            this.columnList.add(string);
        }
    }

    /**
     * 添加column字段
     *
     * @param column
     * @return
     * @author Jovi
     * @date 2017年8月18日下午4:18:18
     */
    public Select column(String column) {
        if (StringUtil.isNotEmpty(column)) {
            columnList.add(column);
        }
        return this;
    }

    /**
     * 添加column字段
     *
     * @param subSql
     * @param alias
     * @return
     * @author Jovi
     * @date 2017年8月18日下午4:18:18
     */
    public Select column(String subSql, String alias) {
        if (StringUtil.isNotEmpty(subSql) && StringUtil.isNotEmpty(alias)) {
            columnList.add(SqlHelperCons.BEGIN_BRACKET + subSql + SqlHelperCons.END_BRACKET + SqlHelperCons.AS + SqlBeanUtil.getTransferred() + alias + SqlBeanUtil.getTransferred());
        }
        return this;
    }

    /**
     * 获取内连接的表
     *
     * @return
     * @author Jovi
     * @date 2017年8月18日下午4:20:49
     */
    public List<String> getInnerJoin() {
        return innerJoinList;
    }

    /**
     * 添加表内连接
     *
     * @param table
     * @param tableKeyword
     * @param mainKeyword
     * @author Jovi
     * @date 2017年8月18日上午10:54:26
     */
    public Select innerJoin(String table, String tableKeyword, String mainKeyword) {
        return innerJoin(table, table, tableKeyword, mainKeyword);
    }

    /**
     * 添加表内连接
     *
     * @param table
     * @param tableAlias
     * @param tableKeyword
     * @param mainKeyword
     * @author Jovi
     * @date 2017年8月18日上午10:54:26
     */
    public Select innerJoin(String table, String tableAlias, String tableKeyword, String mainKeyword) {
        if (SqlBeanUtil.joinIsNotEmpty(table, tableKeyword, mainKeyword)) {
            innerJoinList.add(SqlHelperCons.INNER_JOIN + joinSplitJoint(table, tableAlias, tableKeyword, mainKeyword));
        }
        return this;
    }


    /**
     * 获取外连接的表
     *
     * @return
     * @author Jovi
     * @date 2017年8月18日下午4:20:41
     */
    public List<String> getFullJoin() {
        return fullJoinList;
    }

    /**
     * 添加表外连接
     *
     * @param table
     * @param tableKeyword
     * @param mainKeyword
     * @author Jovi
     * @date 2017年8月18日上午10:55:52
     */
    public Select fullJoin(String table, String tableKeyword, String mainKeyword) {
        return fullJoin(table, table, tableKeyword, mainKeyword);
    }

    /**
     * 添加表外连接
     *
     * @param table
     * @param tableAlias
     * @param tableKeyword
     * @param mainKeyword
     * @author Jovi
     * @date 2017年8月18日上午10:55:52
     */
    public Select fullJoin(String table, String tableAlias, String tableKeyword, String mainKeyword) {
        if (SqlBeanUtil.joinIsNotEmpty(table, tableKeyword, mainKeyword)) {
            fullJoinList.add(SqlHelperCons.FULL_JOIN + joinSplitJoint(table, tableAlias, tableKeyword, mainKeyword));
        }
        return this;
    }

    /**
     * 获取左外连接的表
     *
     * @return
     * @author Jovi
     * @date 2017年8月18日下午4:20:33
     */
    public List<String> getLeftOuterJoin() {
        return leftJoinList;
    }

    /**
     * 添加表左外连接
     *
     * @param table
     * @param tableKeyword
     * @param mainKeyword
     * @author Jovi
     * @date 2017年8月18日上午10:55:04
     */
    public Select leftJoin(String table, String tableKeyword, String mainKeyword) {
        return leftJoin(table, table, tableKeyword, mainKeyword);
    }

    /**
     * 添加表左外连接
     *
     * @param table
     * @param tableAlias
     * @param tableKeyword
     * @param mainKeyword
     * @author Jovi
     * @date 2017年8月18日上午10:55:04
     */
    public Select leftJoin(String table, String tableAlias, String tableKeyword, String mainKeyword) {
        if (SqlBeanUtil.joinIsNotEmpty(table, tableKeyword, mainKeyword)) {
            leftJoinList.add(SqlHelperCons.LEFT_JOIN + joinSplitJoint(table, tableAlias, tableKeyword, mainKeyword));
        }
        return this;
    }

    /**
     * 获取右外连接的表
     *
     * @return
     * @author Jovi
     * @date 2017年8月18日下午4:20:01
     */
    public List<String> getRightOuterJoin() {
        return rightJoinList;
    }

    /**
     * 添加表右外连接
     *
     * @param table
     * @param tableKeyword
     * @param mainKeyword
     * @author Jovi
     * @date 2017年8月18日上午10:55:23
     */
    public Select rightJoin(String table, String tableKeyword, String mainKeyword) {
        return rightJoin(table, table, tableKeyword, mainKeyword);
    }

    /**
     * 添加表右外连接
     *
     * @param table
     * @param tableAlias
     * @param tableKeyword
     * @param mainKeyword
     * @author Jovi
     * @date 2017年8月18日上午10:55:23
     */
    public Select rightJoin(String table, String tableAlias, String tableKeyword, String mainKeyword) {
        if (SqlBeanUtil.joinIsNotEmpty(table, tableKeyword, mainKeyword)) {
            rightJoinList.add(SqlHelperCons.RIGHT_JOIN + joinSplitJoint(table, tableAlias, tableKeyword, mainKeyword));
        }
        return this;
    }

    /**
     * join 字符串拼接
     *
     * @param table
     * @param tableAlias
     * @param tableKeyword
     * @param mainKeyword
     * @return
     */
    private String joinSplitJoint(String table, String tableAlias, String tableKeyword, String mainKeyword) {
        return table + SqlHelperCons.SPACES + tableAlias + SqlHelperCons.ON + tableKeyword + SqlHelperCons.EQUAL_TO + mainKeyword;
    }

    /**
     * 添加表连接
     *
     * @param table
     * @param tableKeyword
     * @param mainKeyword
     * @author Jovi
     * @date 2019年6月21日上午10:27:50
     */
    public Select join(JoinType joinType, String table, String tableKeyword, String mainKeyword) {
        return join(joinType, table, table, tableKeyword, mainKeyword);
    }

    /**
     * 添加表连接
     *
     * @param table
     * @param tableKeyword
     * @param mainKeyword
     * @author Jovi
     * @date 2019年6月21日上午10:27:50
     */
    public Select join(JoinType joinType, String table, String tableAlias, String tableKeyword, String mainKeyword) {
        switch (joinType) {
            case INNER_JOIN:
                innerJoin(table, tableAlias, tableKeyword, mainKeyword);
                break;
            case FULL_JOIN:
                fullJoin(table, tableAlias, tableKeyword, mainKeyword);
                break;
            case LEFT_JOIN:
                leftJoin(table, tableAlias, tableKeyword, mainKeyword);
                break;
            case RIGHT_JOIN:
                rightJoin(table, tableAlias, tableKeyword, mainKeyword);
                break;
        }
        return this;
    }

    /**
     * 获取groupBy分组字段
     *
     * @return
     * @author Jovi
     * @date 2017年8月18日下午4:19:36
     */
    public List<String> getGroupBy() {
        return groupByList;
    }

    /**
     * 添加groupBy分组
     *
     * @param field
     * @return
     * @author Jovi
     * @date 2017年8月18日下午3:32:56
     */
    public Select groupBy(String field) {
        if (StringUtil.isNotEmpty(field)) {
            groupByList.add(field);
        }
        return this;
    }

    /**
     * 获取where sql 内容
     *
     * @return
     * @author Jovi
     * @date 2017年8月18日上午8:58:33
     */
    public String getHaving() {
        return having;
    }

    /**
     * 设置having sql 内容
     *
     * @param having
     * @author Jovi
     * @date 2017年8月18日上午8:58:11
     */
    public void setHaving(String having) {
        this.having = having;
    }

    /**
     * 设置having sql 内容
     *
     * @param having
     * @param args
     * @author Jovi
     * @date 2018年9月13日下午15:34:45
     */
    public void setHaving(String having, Object... args) {
        this.having = SqlBeanUtil.getWhere(having, args);
    }

    /**
     * 获取having条件值映射
     *
     * @return
     * @author Jovi
     * @date 2017年8月18日下午4:19:25
     */
    public ListMultimap<String, SqlCondition> getHavingMap() {
        return havingMap;
    }

    /**
     * 获取orderBy排序字段
     *
     * @return
     * @author Jovi
     * @date 2017年8月18日下午4:18:45
     */
    public List<String> getOrderBy() {
        return orderByList;
    }

    /**
     * 添加字段排序
     *
     * @param field
     * @param sort
     * @author Jovi
     * @date 2017年8月18日上午11:10:11
     */
    public Select orderBy(String field, String sort) {
        if (StringUtil.isNotEmpty(field) && StringUtil.isNotEmpty(sort)) {
            orderByList.add(field + " " + sort);
        }
        return this;
    }

    /**
     * 添加字段排序
     *
     * @param field
     * @param sqlSort
     * @return
     * @author Jovi
     * @date 2018年4月16日下午6:31:18
     */
    public Select orderBy(String field, SqlSort sqlSort) {
        if (StringUtil.isNotEmpty(field) && sqlSort != null) {
            orderByList.add(field + " " + sqlSort.name().toUpperCase());
        }
        return this;
    }

    /**
     * 设置分页参数
     *
     * @param pagenum  当前页(第一页从0开始)
     * @param pagesize 每页显示数量
     */
    public void setPage(Integer pagenum, Integer pagesize) {
        this.page = new Page(pagenum, pagesize);
    }

    /**
     * 设置分页参数(SqlServer专用)
     *
     * @param idName   主键的名称
     * @param pagenum  当前页(第一页从0开始)
     * @param pagesize 每页显示数量
     */
    public void setPage(String idName, Integer pagenum, Integer pagesize) {
        this.page = new Page(idName, pagenum, pagesize);
    }

    /**
     * 获取分页参数
     *
     * @return
     */
    public Page getPage() {
        return this.page;
    }

    /**
     * 添加having条件
     *
     * @param field 字段
     * @param value 字段值
     * @return
     * @author Jovi
     * @date 2017年8月18日下午3:41:07
     */
    public Select having(String field, Object value) {
        if (StringUtil.isNotEmpty(field) && value != null) {
            having(field, value, SqlLogic.AND);
        }
        return this;
    }

    /**
     * @param field    字段
     * @param value    字段值
     * @param sqlLogic 该条件与下一条件之间的逻辑关系
     * @return
     * @author Jovi
     * @date 2017年8月18日下午4:23:17
     */
    public Select having(String field, Object value, SqlLogic sqlLogic) {
        if (StringUtil.isNotEmpty(field) && value != null) {
            havingMap.put(field.replaceAll("\\.", "*"), new SqlCondition(sqlLogic, field, value, ""));
        }
        return this;
    }

    /**
     * 添加having条件
     *
     * @param field    字段
     * @param value    字段值
     * @param operator 操作符
     * @return
     * @author Jovi
     * @date 2017年8月18日下午3:40:52
     */
    public Select having(String field, Object value, String operator) {
        if (StringUtil.isNotEmpty(field) && value != null) {
            having(field, value, operator, SqlLogic.AND);
        }
        return this;
    }

    /**
     * @param field    字段
     * @param value    字段值
     * @param operator 操作符
     * @param sqlLogic 该条件与下一条件之间的逻辑关系
     * @return
     * @author Jovi
     * @date 2017年8月18日下午4:25:05
     */
    public Select having(String field, Object value, String operator, SqlLogic sqlLogic) {
        if (StringUtil.isNotEmpty(field) && value != null) {
            havingMap.put(field.replaceAll("\\.", "*"), new SqlCondition(sqlLogic, field, value, operator));
        }
        return this;
    }

    /**
     * 获取过滤的字段
     *
     * @return
     * @author Jovi
     * @date 2017年8月18日上午8:59:26
     */
    public String[] getFilterFields() {
        return filterFields;
    }

    /**
     * 设置过滤的字段
     *
     * @param filterField
     * @author Jovi
     * @date 2017年8月18日上午8:59:14
     */
    public void setFilterFields(String... filterField) {
        this.filterFields = filterField;
    }

    /**
     * 复制对象
     *
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     * @author Jovi
     * @date 2017年8月18日上午8:53:55
     */
    public Object copy() throws IOException, ClassNotFoundException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(this);
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bos.toByteArray()));
        return ois.readObject();
    }

}