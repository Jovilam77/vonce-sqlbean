package cn.vonce.sql.bean;

import cn.vonce.sql.enumerate.JoinType;
import cn.vonce.sql.enumerate.SqlSort;
import cn.vonce.sql.helper.SqlHelper;
import cn.vonce.sql.helper.Wrapper;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 查询
 *
 * @author Jovi
 * @version 1.0
 * @email 766255988@qq.com
 * @date 2017年8月18日上午9:00:19
 */
public class Select extends SelectCondition implements Serializable {

    private static final long serialVersionUID = 1L;

    private boolean useDistinct = false;//默认不去重复
    private List<Column> columnList = new ArrayList<>();//查询的列字段数组
    private List<Join> joinList = new ArrayList<>();//表连接的表数组
    private List<Group> groupByList = new ArrayList<>();//分组
    private List<Order> orderByList = new ArrayList<>();//排序
    private Page page = null;
    private String[] filterFields = null;
    private SimpleCondition<Select> whereSimpleCondition = new SimpleCondition<>(this);
    private SimpleCondition<Select> havingSimpleCondition = new SimpleCondition<>(this);


    /**
     * 获取useDistinct是否过滤重复
     *
     * @return
     */
    public boolean isUseDistinct() {
        return useDistinct;
    }

    /**
     * 设置useDistinct是否过滤重复
     *
     * @param useDistinct 是否过滤
     */
    public void setUseDistinct(boolean useDistinct) {
        this.useDistinct = useDistinct;
    }

    /**
     * 获取column sql 内容
     *
     * @return
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
     * 设置column
     *
     * @param columnNames
     */
    public void setColumn(String... columnNames) {
        for (String columnName : columnNames) {
            this.columnList.add(new Column(columnName));
        }
    }

    /**
     * 设置column
     *
     * @param columns
     */
    public void setColumn(Column... columns) {
        if (columns == null || columns.length == 0) {
            return;
        }
        this.columnList.addAll(Arrays.asList(columns));
    }

    /**
     * 添加column列字段
     *
     * @param column
     * @return
     */
    public Select column(Column column) {
        this.columnList.add(column);
        return this;
    }

    /**
     * 添加column列字段
     *
     * @param columnName
     * @return
     */
    public Select column(String columnName) {
        return column("", columnName, "");
    }

    /**
     * 添加column列字段
     *
     * @param columnName  列列字段名
     * @param columnAlias 别名
     * @return
     */
    public Select column(String columnName, String columnAlias) {
        return column("", columnName, columnAlias);
    }

    /**
     * 添加column列字段
     *
     * @param select      子Sql
     * @param columnAlias 别名
     * @return
     */
    public Select column(Select select, String columnAlias) {
        return column(SqlHelper.buildSelectSql(select), columnAlias);
    }

    /**
     * 添加column列字段
     *
     * @param column
     * @return
     */
    public Select column(Column column, String columnAlias) {
        return column(column.getTableAlias(), column.getName(), columnAlias);
    }

    /**
     * 添加column列字段
     *
     * @param tableAlias  表别名
     * @param columnName  列列字段名
     * @param columnAlias 别名
     * @return
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
     */
    public Select join(String table, String tableKeyword, String mainKeyword) {
        return join(JoinType.INNER_JOIN, "", table, table, tableKeyword, mainKeyword);
    }

    /**
     * 添加表连接
     *
     * @param joinType     连接类型
     * @param table        关联的表名
     * @param tableKeyword 关联的表关键列字段
     * @param mainKeyword  主表关键列字段
     */
    public Select join(JoinType joinType, String table, String tableKeyword, String mainKeyword) {
        return join(joinType, "", table, table, tableKeyword, mainKeyword);
    }

    /**
     * 添加表连接
     *
     * @param table        关联的表名
     * @param tableKeyword 关联的表关键列字段
     * @param mainKeyword  主表关键列字段
     */
    public Select join(String schema, String table, String tableAlias, String tableKeyword, String mainKeyword) {
        return join(JoinType.INNER_JOIN, schema, table, tableAlias, tableKeyword, mainKeyword);
    }

    /**
     * 添加表连接
     *
     * @param joinType     连接类型
     * @param schema       schema
     * @param table        关联的表名
     * @param tableKeyword 关联的表关键列字段
     * @param mainKeyword  主表关键列字段
     */
    public Select join(JoinType joinType, String schema, String table, String tableAlias, String tableKeyword, String mainKeyword) {
        joinList.add(new Join(joinType, schema, table, tableAlias, tableKeyword, mainKeyword, ""));
        return this;
    }

    /**
     * 添加表连接
     *
     * @param table 关联的表名
     * @param on    连接条件
     */
    public Select join(String table, String on) {
        return join(JoinType.INNER_JOIN, "", table, table, on);
    }

    /**
     * 添加表连接
     *
     * @param joinType 连接类型
     * @param table    关联的表名
     * @param on       连接条件
     */
    public Select join(JoinType joinType, String table, String on) {
        return join(joinType, "", table, table, on);
    }

    /**
     * 添加表连接
     *
     * @param joinType 连接类型
     * @param schema   schema
     * @param table    关联的表名
     * @param on       连接条件
     */
    public Select join(JoinType joinType, String schema, String table, String tableAlias, String on) {
        joinList.add(new Join(joinType, schema, table, tableAlias, "", "", on));
        return this;
    }

    /**
     * 获取groupBy分组列字段
     *
     * @return
     */
    public List<Group> getGroupBy() {
        return groupByList;
    }

    /**
     * 添加groupBy分组
     *
     * @param columNname 列字段名
     * @return
     */
    public Select groupBy(String columNname) {
        return groupBy("", columNname);
    }

    /**
     * 添加groupBy分组
     *
     * @param column 列字段信息
     * @return
     */
    public Select groupBy(Column column) {
        return groupBy(column.getTableAlias(), column.getName());
    }

    /**
     * 添加groupBy分组
     *
     * @param tableAlias 表别名
     * @param columNname 列字段名
     * @return
     */
    public Select groupBy(String tableAlias, String columNname) {
        groupByList.add(new Group(tableAlias, columNname));
        return this;
    }


    /**
     * 获取orderBy排序列字段
     *
     * @return
     */
    public List<Order> getOrderBy() {
        return orderByList;
    }

    /**
     * 添加列字段排序
     *
     * @param columNname 列字段名
     * @param sqlSort    排序方式
     */
    public Select orderBy(String columNname, SqlSort sqlSort) {
        return orderBy("", columNname, sqlSort);
    }

    /**
     * 添加列字段排序
     *
     * @param column  列字段信息
     * @param sqlSort 排序方式
     * @return
     */
    public Select orderBy(Column column, SqlSort sqlSort) {
        return orderBy(column.getTableAlias(), column.getName(), sqlSort);
    }

    /**
     * 添加列字段排序
     *
     * @param tableAlias 表别名
     * @param columNname 列字段名
     * @param sqlSort    排序方式
     * @return
     */
    public Select orderBy(String tableAlias, String columNname, SqlSort sqlSort) {
        orderByList.add(new Order(tableAlias, columNname, sqlSort));
        return this;
    }

    /**
     * 添加列字段排序
     *
     * @param orders 排序
     * @return
     */
    public Select orderBy(Order[] orders) {
        if (orders != null && orders.length > 0) {
            orderByList.addAll(Arrays.asList(orders));
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
     * 设置分页参数
     *
     * @param pagenum     当前页(默认第一页从0开始)
     * @param pagesize    每页显示数量
     * @param startByZero 第一页是否从0开始
     */
    public void setPage(Integer pagenum, Integer pagesize, boolean startByZero) {
        this.page = new Page(pagenum, pagesize, startByZero);
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
     * 设置分页参数(SqlServer专用)
     *
     * @param idName      主键的名称
     * @param pagenum     当前页(第一页从0开始)
     * @param pagesize    每页显示数量
     * @param startByZero startByZero 第一页是否从0开始
     */
    public void setPage(String idName, Integer pagenum, Integer pagesize, boolean startByZero) {
        this.page = new Page(idName, pagenum, pagesize, startByZero);
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
     * 获取过滤的列字段
     *
     * @return
     */
    public String[] getFilterFields() {
        return filterFields;
    }

    /**
     * 设置过滤的列字段
     *
     * @param filterField
     */
    public void setFilterFields(String... filterField) {
        this.filterFields = filterField;
    }

    public SimpleCondition<Select> where() {
        return whereSimpleCondition;
    }

    public SimpleCondition<Select> having() {
        return havingSimpleCondition;
    }

    /**
     * 复制对象
     *
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public Select copy() throws IOException, ClassNotFoundException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(this);
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bos.toByteArray()));
        return (Select) ois.readObject();
    }

}
