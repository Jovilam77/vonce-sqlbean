package cn.vonce.sql.bean;

import cn.vonce.sql.enumerate.JoinType;
import cn.vonce.sql.enumerate.SqlLogic;
import cn.vonce.sql.enumerate.SqlOperator;
import cn.vonce.sql.enumerate.SqlSort;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.ListMultimap;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 查询
 *
 * @author Jovi
 * @version 1.0
 * @email 766255988@qq.com
 * @date 2017年8月18日上午9:00:19
 */
public class Select extends Condition implements Serializable {

    private static final long serialVersionUID = 1L;

    private boolean useDistinct = false;//默认不去重复
    private List<Column> columnList = new ArrayList<>();//查询的列字段数组
    private List<Join> joinList = new ArrayList<>();//表连接的表数组
    private List<Group> groupByList = new ArrayList<>();//分组
    private List<Order> orderByList = new ArrayList<>();//排序
    private Page page = null;
    private String having = null;
    private Object[] havingArgs = null;
    private ListMultimap<String, SqlCondition> havingMap = LinkedListMultimap.create();//having条件包含的逻辑
    private String[] filterFields = null;//需要过滤的列字段

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
     * @param useDistinct 是否过滤
     * @author Jovi
     * @date 2017年8月18日下午4:21:05
     */
    public void setUseDistinct(boolean useDistinct) {
        this.useDistinct = useDistinct;
    }

    /**
     * 获取column sql 内容
     *
     * @return
     * @author Jovi
     * @date 2017年8月18日上午9:00:05
     */
    public List<Column> getColumnList() {
        return columnList;
    }

    /**
     * 设置column list
     *
     * @param columnList
     */
    public void setColumnList(List<Column> columnList) {
        this.columnList.addAll(columnList);
    }

    /**
     * 设置column（追加）
     *
     * @param columnNames
     * @author Jovi
     * @date 2017年8月18日上午8:59:56
     */
    public void setColumn(String... columnNames) {
        for (String columnName : columnNames) {
            this.columnList.add(new Column(columnName));
        }
    }

    /**
     * 添加column列字段
     *
     * @param columnName
     * @return
     * @author Jovi
     * @date 2017年8月18日下午4:18:18
     */
    public Select column(String columnName) {
        columnList.add(new Column(columnName));
        return this;
    }

    /**
     * 添加column列字段
     *
     * @param columnName  列列字段名
     * @param columnAlias 别名
     * @return
     * @author Jovi
     * @date 2017年8月18日下午4:18:18
     */
    public Select column(String columnName, String columnAlias) {
        columnList.add(new Column(columnName, columnAlias));
        return this;
    }

    /**
     * 添加column列字段
     *
     * @param tableAlias  表别名
     * @param columnName  列列字段名
     * @param columnAlias 别名
     * @return
     * @author Jovi
     * @date 2017年8月18日下午4:18:18
     */
    public Select column(String tableAlias, String columnName, String columnAlias) {
        columnList.add(new Column(tableAlias, columnName, columnAlias));
        return this;
    }

    /**
     * 获取表连接
     */
    public List<Join> getJoin() {
        return joinList;
    }

    /**
     * 添加表连接
     *
     * @param table        关联的表名
     * @param tableKeyword 关联的表关键列字段
     * @param mainKeyword  主表关键列字段
     * @author Jovi
     * @date 2019年6月21日上午10:27:50
     */
    public Select join(String table, String tableKeyword, String mainKeyword) {
        return join(JoinType.INNER_JOIN, table, table, tableKeyword, mainKeyword);
    }

    /**
     * 添加表连接
     *
     * @param joinType     连接类型
     * @param table        关联的表名
     * @param tableKeyword 关联的表关键列字段
     * @param mainKeyword  主表关键列字段
     * @author Jovi
     * @date 2019年6月21日上午10:27:50
     */
    public Select join(JoinType joinType, String table, String tableKeyword, String mainKeyword) {
        return join(joinType, table, table, tableKeyword, mainKeyword);
    }

    /**
     * 添加表连接
     *
     * @param table        关联的表名
     * @param tableKeyword 关联的表关键列字段
     * @param mainKeyword  主表关键列字段
     * @author Jovi
     * @date 2019年6月21日上午10:27:50
     */
    public Select join(String table, String tableAlias, String tableKeyword, String mainKeyword) {
        return join(JoinType.INNER_JOIN, table, tableAlias, tableKeyword, mainKeyword);
    }

    /**
     * 添加表连接
     *
     * @param joinType     连接类型
     * @param table        关联的表名
     * @param tableKeyword 关联的表关键列字段
     * @param mainKeyword  主表关键列字段
     * @author Jovi
     * @date 2019年6月21日上午10:27:50
     */
    public Select join(JoinType joinType, String table, String tableAlias, String tableKeyword, String mainKeyword) {
        joinList.add(new Join(joinType, table, tableAlias, tableKeyword, mainKeyword));
        return this;
    }

    /**
     * 获取groupBy分组列字段
     *
     * @return
     * @author Jovi
     * @date 2017年8月18日下午4:19:36
     */
    public List<Group> getGroupBy() {
        return groupByList;
    }

    /**
     * 添加groupBy分组
     *
     * @param columNname 列字段名
     * @return
     * @author Jovi
     * @date 2017年8月18日下午3:32:56
     */
    public Select groupBy(String columNname) {
        return groupBy("", columNname);
    }

    /**
     * 添加groupBy分组
     *
     * @param tableAlias 表别名
     * @param columNname 列字段名
     * @return
     * @author Jovi
     * @date 2017年8月18日下午3:32:56
     */
    public Select groupBy(String tableAlias, String columNname) {
        groupByList.add(new Group(tableAlias, columNname));
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
        this.having = having;
        this.havingArgs = args;
    }

    /**
     * 获取Having
     *
     * @return
     */
    public Object[] getHavingArgs() {
        return havingArgs;
    }

    /**
     * 设置Having
     *
     * @param havingArgs
     */
    public void setHavingArgs(Object[] havingArgs) {
        this.havingArgs = havingArgs;
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
     * 获取orderBy排序列字段
     *
     * @return
     * @author Jovi
     * @date 2017年8月18日下午4:18:45
     */
    public List<Order> getOrderBy() {
        return orderByList;
    }

    /**
     * 添加列字段排序
     *
     * @param columNname 列字段名
     * @param sqlSort    排序方式
     * @author Jovi
     * @date 2017年8月18日上午11:10:11
     */
    public Select orderBy(String columNname, SqlSort sqlSort) {
        return orderBy("", columNname, sqlSort);
    }

    /**
     * 添加列字段排序
     *
     * @param tableAlias 表别名
     * @param columNname 列字段名
     * @param sqlSort    排序方式
     * @return
     * @author Jovi
     * @date 2018年4月16日下午6:31:18
     */
    public Select orderBy(String tableAlias, String columNname, SqlSort sqlSort) {
        orderByList.add(new Order(tableAlias, columNname, sqlSort));
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
     * @param field 列字段
     * @param value 列字段值
     * @author Jovi
     * @date 2017年8月18日上午8:53:11
     */
    public Select having(String field, Object value) {
        return having(field, value, SqlOperator.EQUAL_TO);
    }


    /**
     * 添加having条件
     *
     * @param field       列字段
     * @param value       列字段值
     * @param sqlOperator 操作符
     * @return
     * @author Jovi
     * @date 2017年8月30日上午11:37:56
     */
    public Select having(String field, Object value, SqlOperator sqlOperator) {
        return having(SqlLogic.AND, "", field, value, sqlOperator);
    }

    /**
     * @param sqlLogic   该条件与下一条件之间的逻辑关系
     * @param tableAlias 表别名
     * @param field      列字段
     * @param value      列字段值
     * @return
     * @author Jovi
     * @date 2017年8月18日下午4:08:28
     * @author Jovi
     * @date 2017年8月18日上午8:53:13
     */
    public Select having(SqlLogic sqlLogic, String tableAlias, String field, Object value) {
        return having(sqlLogic, tableAlias, field, value, SqlOperator.EQUAL_TO);
    }

    /**
     * 添加having条件
     *
     * @param tableAlias  表别名
     * @param field       列字段
     * @param value       列字段值
     * @param sqlOperator 操作符
     * @return
     * @author Jovi
     * @date 2017年8月30日上午11:37:56
     */
    public Select having(String tableAlias, String field, Object value, SqlOperator sqlOperator) {
        return having(SqlLogic.AND, tableAlias, field, value, sqlOperator);
    }

    /**
     * 添加having条件
     *
     * @param sqlLogic    该条件与下一条件之间的逻辑关系
     * @param tableAlias  表别名
     * @param field       列字段
     * @param value       列字段值
     * @param sqlOperator 操作符
     * @return
     * @author Jovi
     * @date 2017年8月30日上午11:43:15
     */
    public Select having(SqlLogic sqlLogic, String tableAlias, String field, Object value, SqlOperator sqlOperator) {
        havingMap.put(tableAlias + field, new SqlCondition(sqlLogic, tableAlias, field, value, sqlOperator));
        return this;
    }

    /**
     * 获取过滤的列字段
     *
     * @return
     * @author Jovi
     * @date 2017年8月18日上午8:59:26
     */
    public String[] getFilterFields() {
        return filterFields;
    }

    /**
     * 设置过滤的列字段
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