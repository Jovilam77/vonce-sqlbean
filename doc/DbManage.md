#### 一. TableService接口文档
###### 1：删除表结构
```java
    /**
     * 删除表结构
     *
     * @return
     */
    void dropTable();
```
###### 2：创建表结构
```java
    /**
     * 创建表结构
     *
     * @return
     */
    void createTable();
```
###### 3：删除并创建表结构
```java
    /**
     * 删除并创建表结构
     */
    void dropAndCreateTable();
```
###### 4：获取表名列表
```java
    /**
     * 获取表名列表
     *
     * @param tableName 可以为null
     * @return
     */
    List<TableInfo> getTableList(String tableName);
```
###### 5：获取表名列表
```java
    /**
     * 获取表名列表
     *
     * @param schema
     * @param tableName
     * @return
     */
    List<TableInfo> getTableList(String schema, String tableName);
```
###### 6：获取列信息列表
```java
    /**
     * 获取列信息列表
     *
     * @param tableName
     * @return
     */
    List<ColumnInfo> getColumnInfoList(String tableName);
```
###### 7：获取列信息列表
```java
    /**
     * 获取列信息列表
     *
     * @param schema
     * @param tableName
     * @return
     */
    List<ColumnInfo> getColumnInfoList(String schema, String tableName);
```
###### 8：备份表和数据到一张新表(表名_+时间)
```java
    /**
     * 备份表和数据到一张新表(表名_+时间)
     *
     * @return 默认创建的表名
     */
    String backup();
```
###### 9：备份表和数据到一张指定名称的新表
```java
    /**
     * 备份表和数据到一张指定名称的新表
     *
     * @param targetTableName 目标表名
     * @return
     */
    void backup(String targetTableName);
```
###### 10：备份表和数据到一张指定名称的新表
```java
    /**
     * 备份表和数据到一张指定名称的新表
     *
     * @param targetSchema    目标schema
     * @param targetTableName 目标表名
     * @return
     */
    void backup(String targetSchema, String targetTableName);
```
###### 11：根据条件备份表和数据到一张指定名称的新表
```java
    /**
     * 根据条件备份表和数据到一张指定名称的新表
     *
     * @param wrapper         条件包装器
     * @param targetTableName 目标表名
     * @param columns         指定的列
     * @return
     */
    void backup(Wrapper wrapper, String targetTableName, Column... columns);
```
###### 12：根据条件备份表和数据到一张指定名称的新表
```java
    /**
     * 根据条件备份表和数据到一张指定名称的新表
     *
     * @param wrapper         条件包装器
     * @param targetTableName 目标表名
     * @param columns         指定的列
     * @return
     */
    <R> void backup(Wrapper wrapper, String targetTableName, ColumnFun<T, R>... columns);
```
###### 13：根据条件备份表和数据到一张指定名称的新表
```java
    /**
     * 根据条件备份表和数据到一张指定名称的新表
     *
     * @param wrapper         条件包装器
     * @param targetSchema    目标schema
     * @param targetTableName 目标表名
     * @param columns         指定的列
     * @return
     */
    void backup(Wrapper wrapper, String targetSchema, String targetTableName, Column... columns);
```
###### 14：根据条件备份表和数据到一张指定名称的新表
```java
    /**
     * 根据条件备份表和数据到一张指定名称的新表
     *
     * @param wrapper         条件包装器
     * @param targetSchema    目标schema
     * @param targetTableName 目标表名
     * @param columns         指定的列
     * @return
     */
    <R> void backup(Wrapper wrapper, String targetSchema, String targetTableName, ColumnFun<T, R>... columns);
```
###### 15：根据条件将数据复制插入到同样结构的表中
```java
    /**
     * 根据条件将数据复制插入到同样结构的表中
     *
     * @param wrapper         条件包装器
     * @param targetTableName 目标表名
     * @return
     */
    int copy(Wrapper wrapper, String targetTableName);
```
###### 16：根据条件将数据复制插入到同样结构的表中
```java
    /**
     * 根据条件将数据复制插入到同样结构的表中
     *
     * @param wrapper         条件包装器
     * @param targetSchema    目标schema
     * @param targetTableName 目标表名
     * @return
     */
    int copy(Wrapper wrapper, String targetSchema, String targetTableName);
```
###### 17：根据条件将数据复制插入到指定结构的表中
```java
    /**
     * 根据条件将数据复制插入到指定结构的表中
     *
     * @param wrapper         条件包装器
     * @param targetTableName 目标表名
     * @param columns         指定的列
     * @return
     */
    int copy(Wrapper wrapper, String targetTableName, Column... columns);
```
###### 18：根据条件将数据复制插入到指定结构的表中
```java
    /**
     * 根据条件将数据复制插入到指定结构的表中
     *
     * @param wrapper         条件包装器
     * @param targetTableName 目标表名
     * @param columns         指定的列
     * @return
     */
    <R> int copy(Wrapper wrapper, String targetTableName, ColumnFun<T, R>... columns);
```
###### 19：根据条件将数据复制插入到指定结构的表中
```java
    /**
     * 根据条件将数据复制插入到指定结构的表中
     *
     * @param wrapper         条件包装器
     * @param columns         指定的列
     * @param targetSchema    目标schema
     * @param targetTableName 目标表名
     * @return
     */
    int copy(Wrapper wrapper, String targetSchema, String targetTableName, Column... columns);
```
###### 20：根据条件将数据复制插入到指定结构的表中
```java
    /**
     * 根据条件将数据复制插入到指定结构的表中
     *
     * @param wrapper         条件包装器
     * @param columns         指定的列
     * @param targetSchema    目标schema
     * @param targetTableName 目标表名
     * @return
     */
    <R> int copy(Wrapper wrapper, String targetSchema, String targetTableName, ColumnFun<T, R>... columns);
```
###### 21：更改表结构
```java
    /**
     * 更改表结构
     *
     * @param table          表对象
     * @param columnInfoList 列表信息列表
     * @return
     */
    int alter(Table table, List<ColumnInfo> columnInfoList);
```
###### 22：更改表结构
```java
    /**
     * 更改表结构
     *
     * @param alter 改变表结构对象
     * @return
     */
    int alter(Alter alter);
```
###### 23：更改表结构
```java
    /**
     * 更改表结构
     *
     * @param alterList 改变表结构对象列表
     * @return
     */
    int alter(List<Alter> alterList);
```
###### 24：更改表注释
```java
    /**
     * 更改表注释
     *
     * @param remarks 注释
     * @return
     */
    int alterRemarks(String remarks);
```
###### 25：模式列表
```java
    /**
     * 模式列表
     *
     * @param name
     * @return
     */
    List<String> getSchemas(String name);
```
###### 26：创建模式
```java
    /**
     * 创建模式
     *
     * @param name
     * @return
     */
    int createSchema(String name);
```
###### 27：删除模式
```java
    /**
     * 删除模式
     *
     * @param name
     * @return
     */
    int dropSchema(String name);
```