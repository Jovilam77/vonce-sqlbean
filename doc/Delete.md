#### Delete使用示例
```java
    Delete delete = new Delete();
	
    //delete.setTable("t_essay"); 表名
    delete.setTable(Essay.class);
	
    //id 大于 1  这里的id建议用SqlEssay.id 常量替代 这里演示多种写法特意不写
    delete.where("id", 1, SqlOperator.GREATER_THAN);
	
    //并且 内容等于222 这里的content建议用SqlEssay.content 常量替代 这里演示多种写法特意不写
    delete.wAND("content", "222");
	
    //条件也可用包装器 复杂条件推荐使用
    //delete.setWhere(Wrapper.where(Cond.gt(SqlEssay.id, 1)).and(Cond.eq(SqlEssay.content, "222")));
    //也可使用表达式 如果这三种条件同时出现 那么此方式优先级最高 上面包装器次之
    //delete.setWhere("& = ? AND & = ?", SqlEssay.id, 1, SqlEssay.content, "222");
	
    essayService.delete(delete);
```
#### DeleteService接口文档
###### 1：根据id条件删除
```java
  /**
   * 根据id条件删除
   *
   * @param id 单个id或数组
   * @return
   */
   int deleteById(ID... id);
```
###### 2：根据条件删除
```java
  /**
   * 根据条件删除
   *
   * @param where 条件表达式
   * @param args  条件参数
   * @return
   */
   int deleteByCondition(String where, Object... args);
```
###### 3：根据条件删除
```java
  /**
   * 根据条件删除
   *
   * @param where 条件包装器
   * @return
   */
   int deleteByCondition(Wrapper where);
```
###### 4：删除
```java
  /**
    * 删除(where条件为空会抛异常，因为删除全部非常危险)
    *
    * @param delete 删除对象
    * @return
    */
    int delete(Delete delete);
```
###### 5：删除
```java
  /**
    * 删除
    *
    * @param delete 删除对象
    * @param ignore 如果为true则不指定where条件也能执行，false则抛异常
    * @return
    */
    int delete(Delete delete, boolean ignore);
```
###### 6：逻辑删除根据id条件（需在实体类标记逻辑删除字段@SqlLogically）
```java
  /**
    * 逻辑删除根据id条件
    *
    * @param id 单个id或数组
    * @return
    */
    int logicallyDeleteById(ID... id);
```
###### 7：根据id条件逻辑删除（需在实体类标记逻辑删除字段@SqlLogically）
```java
  /**
    * 根据条件逻辑删除
    *
    * @param where 条件表达式
    * @param args  条件参数
    * @return
    */
    int logicallyDeleteByCondition(String where, Object... args);
```
###### 8：根据条件逻辑删除（需在实体类标记逻辑删除字段@SqlLogically）
```java
  /**
    * 根据条件逻辑删除
    *
    * @param where 条件包装器
    * @return
    */
    int logicallyDeleteByCondition(Wrapper where);
```
