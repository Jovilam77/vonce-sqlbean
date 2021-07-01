#### TableService接口文档
###### 1：获取Bean类型
```java
  /**
    * 获取Bean类型
    *
    * @return
    */
    Class<?> getBeanClass();
```
###### 2：删除表结构
```java
  /**
    * 删除表结构
    *
    * @return
    */
    void dropTable();
```
###### 3：创建表结构
```java
  /**
    * 创建表结构
    *
    * @return
    */
    void createTable();
```
###### 4：删除并创建表结构
```java
  /**
    * 删除并创建表结构
    */
    void dropAndCreateTable();
```
###### 5：获取表名列表
```java
  /**
    * 获取表名列表
    *
    * @return
    */
    List<String> getTableList();
```
###### 6：备份表和数据到一张新表(表名_+时间)
```java
  /**
    * 备份表和数据到一张新表(表名_+时间)
    *
    * @return 默认创建的表名
    */
    String backup();
```
###### 7：备份表和数据到一张指定名称的新表
```java
  /**
    * 备份表和数据到一张指定名称的新表
    *
    * @param targetTableName 目标表名
    * @return
    */
    void backup(String targetTableName);
```
###### 8：备份表和数据到一张指定名称的新表
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
###### 9：根据条件备份表和数据到一张指定名称的新表
```java
  /**
    * 根据条件备份表和数据到一张指定名称的新表
    *
    * @param targetTableName 目标表名
    * @param columns         指定的列
    * @param wrapper         条件包装器
    * @return
    */
    void backup(String targetTableName, Column[] columns, Wrapper wrapper);
```
###### 10：根据条件备份表和数据到一张指定名称的新表
```java
   /**
    * 根据条件备份表和数据到一张指定名称的新表
    *
    * @param targetSchema    目标schema
    * @param targetTableName 目标表名
    * @param columns         指定的列
    * @param wrapper         条件包装器
    * @return
    */
    void backup(String targetSchema, String targetTableName, Column[] columns, Wrapper wrapper);
```
###### 11：根据条件将数据复制插入到同样结构的表中
```java
  /**
    * 根据条件将数据复制插入到同样结构的表中
    *
    * @param targetTableName 目标表名
    * @param wrapper         条件包装器
    * @return
    */
    int copy(String targetTableName, Wrapper wrapper);
```
###### 12：根据条件将数据复制插入到同样结构的表中
```java
  /**
    * 根据条件将数据复制插入到同样结构的表中
    *
    * @param targetSchema    目标schema
    * @param targetTableName 目标表名
    * @param wrapper         条件包装器
    * @return
    */
    int copy(String targetSchema, String targetTableName, Wrapper wrapper);
```
###### 13：根据条件将数据复制插入到指定结构的表中
```java
  /**
    * 根据条件将数据复制插入到指定结构的表中
    *
    * @param targetTableName 目标表名
    * @param columns         指定的列
    * @param wrapper         条件包装器
    * @return
    */
    int copy(String targetTableName, Column[] columns, Wrapper wrapper);
```
###### 14：根据条件将数据复制插入到指定结构的表中
```java
  /**
    * 根据条件将数据复制插入到指定结构的表中
    *
    * @param targetSchema    目标schema
    * @param targetTableName 目标表名
    * @param columns         指定的列
    * @param wrapper         条件包装器
    * @return
    */
    int copy(String targetSchema, String targetTableName, Column[] columns, Wrapper wrapper);
```
