#### Select对象使用示例（复杂查询或灵活性较高时使用，查看下方文档使用更简便方式）
```java
    Select select = new Select();
    //指定查询的列
    select.column(SqlEssay._all).column(SqlUser.headPortrait).column(SqlUser.nickname);
    //指定查询的表
    //select.setTable(SqlEssay._tableName);
    select.setTable(Essay.class);
    //指定连表
    select.join(JoinType.INNER_JOIN, SqlUser._tableAlias, SqlUser.id.name(), SqlEssay.user_id.name());
    //查询条件
    //select.where(SqlEssay.user_id, "1111");
    //value 直接输入字符串 会当作字符串处理，sql中会带''，如果希望不被做处理则使用Original
    //select.wAND("DATE_FORMAT( " + SqlEssay.creation_time + ", '%Y-%m-%d' )", new Original("DATE_FORMAT( '2021-01-01 00:00:00', '%Y-%m-%d' ) "), SqlOperator.EQUAL_TO);
    select.setWhere(Wrapper.where(Cond.eq(SqlEssay.user_id, "1111")).and(Cond.eq("DATE_FORMAT( " + SqlEssay.creation_time + ", '%Y-%m-%d' )", new Original("DATE_FORMAT( '2021-01-01 00:00:00', '%Y-%m-%d' ) "))));
    select.orderBy(SqlEssay.id, SqlSort.DESC);
    
    essayService.select(select);
```
#### SelectService接口文档
###### 1：根据id条件查询
```java
  /**
    * 根据id条件查询
    *
    * @param id id主键
    * @return
    */
    T selectById(ID id);
```
###### 2：根据id条件查询
```java
  /**
    * 根据id条件查询
    *
    * @param returnType 指定返回类型，如简化的实体类、或者基本类型、Map
    * @param id         id主键
    * @return
    */
    <O> O selectById(Class<O> returnType, ID id);
```
###### 3：根据ids条件查询
```java
  /**
    * 根据ids条件查询
    *
    * @param ids 单个id主键或数组
    * @return
    */
    List<T> selectByIds(ID... ids);
```
###### 4：根据ids条件查询
```java
  /**
    * 根据id条件查询
    *
    * @param returnType 指定返回类型，如简化的实体类、或者基本类型、Map
    * @param ids        单个id主键或数组
    * @return
    */
    <O> List<O> selectByIds(Class<O> returnType, ID... ids);
```
###### 5：根据自定义条件查询
```java
  /**
    * 根据自定义条件查询
    *
    * @param select 查询对象
    * @return
    */
    T selectOne(Select select);
```
###### 6：根据自定义条件查询
```java
  /**
    * 根据自定义条件查询
    *
    * @param returnType 指定返回类型，如简化的实体类、或者基本类型、Map
    * @param select     查询对象
    * @return
    */
    <O> O selectOne(Class<O> returnType, Select select);
```
###### 7：根据自定义条件查询返回Map
```java
  /**
    * 根据自定义条件查询返回Map
    *
    * @param select 查询对象
    * @return
    */
    Map<String, Object> selectMap(Select select);
```
###### 8：根据条件查询
```java
  /**
    * 根据条件查询
    *
    * @param where 条件字符串表达式
    * @param args  条件参数
    * @return
    */
    T selectOneByCondition(String where, Object... args);
```
###### 9：根据条件查询
```java
  /**
    * 根据条件查询
    *
    * @param returnType 指定返回类型，如简化的实体类、或者基本类型、Map
    * @param where      条件字符串表达式
    * @param args       条件参数
    * @return
    */
    <O> O selectOneByCondition(Class<O> returnType, String where, Object... args);
```
###### 10：根据条件查询
```java
  /**
    * 根据条件查询
    *
    * @param where 条件包装器
    * @return
    */
    T selectOneByCondition(Wrapper where);
```
###### 11：根据条件查询
```java
  /**
    * 根据条件查询
    *
    * @param returnType 指定返回类型，如简化的实体类、或者基本类型、Map
    * @param where      条件包装器
    * @return
    */
    <O> O selectOneByCondition(Class<O> returnType, Wrapper where);
```
###### 12：根据条件查询
```java
  /**
    * 根据条件查询
    *
    * @param where 条件字符串表达式
    * @param args  条件参数
    * @return
    */
    List<T> selectByCondition(String where, Object... args);
```
###### 13：根据条件查询
```java
  /**
    * 根据条件查询
    *
    * @param where 条件包装器
    * @return
    */
    List<T> selectByCondition(Wrapper where);
```
###### 14：根据条件查询
```java
  /**
    * 根据条件查询
    *
    * @param paging 分页对象
    * @param where  条件字符串表达式
    * @param args   条件参数
    * @return
    */
    List<T> selectByCondition(Paging paging, String where, Object... args);
```
###### 15：根据条件查询
```java
  /**
    * 根据条件查询
    *
    * @param paging 分页对象
    * @param where  条件包装器
    * @return
    */
    List<T> selectByCondition(Paging paging, Wrapper where);
```
###### 16：根据条件查询
```java
  /**
    * 根据条件查询
    *
    * @param returnType 指定返回类型，如简化的实体类、或者基本类型、Map
    * @param where      条件字符串表达式
    * @param args       条件参数
    * @return
    */
    <O> List<O> selectByCondition(Class<O> returnType, String where, Object... args);
```
###### 17：根据条件查询
```java
  /**
    * 根据条件查询
    *
    * @param returnType 指定返回类型，如简化的实体类、或者基本类型、Map
    * @param where      条件包装器
    * @return
    */
    <O> List<O> selectByCondition(Class<O> returnType, Wrapper where);
```
###### 18：根据条件查询
```java
  /**
    * 根据条件查询
    *
    * @param returnType 指定返回类型，如简化的实体类、或者基本类型、Map
    * @param paging     分页对象
    * @param where      条件字符串表达式
    * @param args       条件参数
    * @return
    */
    <O> List<O> selectByCondition(Class<O> returnType, Paging paging, String where, Object... args);
```
###### 19：根据条件查询
```java
  /**
    * 根据条件查询
    *
    * @param returnType 指定返回类型，如简化的实体类、或者基本类型、Map
    * @param paging     分页对象
    * @param where      条件包装器
    * @return
    */
    <O> List<O> selectByCondition(Class<O> returnType, Paging paging, Wrapper where);
```
###### 20：根据条件查询统计
```java
  /**
    * 根据条件查询统计
    *
    * @param where 条件字符串表达式
    * @param args  条件参数
    * @return
    */
    int selectCountByCondition(String where, Object... args);
```
###### 21：根据条件查询统计
```java
  /**
    * 根据条件查询统计
    *
    * @param where 条件包装器
    * @return
    */
    int selectCountByCondition(Wrapper where);
```
###### 22：统计全部
```java
  /**
    * 统计全部
    *
    * @return
    */
    int countAll();
```
###### 23：查询全部
```java
  /**
    * 查询全部
    *
    * @return
    */
    List<T> selectAll();
```
###### 24：查询全部
```java
  /**
    * 查询全部
    *
    * @param returnType 指定返回类型，如简化的实体类、或者基本类型、Map
    * @return
    */
    <O> List<O> selectAll(Class<O> returnType);
```
###### 25：查询全部
```java
  /**
    * 查询全部
    *
    * @param paging 分页对象
    * @return
    */
    List<T> selectAll(Paging paging);
```
###### 26：查询全部
```java
  /**
    * 查询全部
    *
    * @param returnType 指定返回类型，如简化的实体类、或者基本类型、Map
    * @param paging     分页对象
    * @return
    */
    <O> List<O> selectAll(Class<O> returnType, Paging paging);
```
###### 27：根据自定义条件查询返回Map List
```java
  /**
    * 根据自定义条件查询返回Map List
    *
    * @param select 查询对象
    * @return
    */
    List<Map<String, Object>> selectMapList(Select select);
```
###### 28：根据自定义条件查询
```java
  /**
    * 根据自定义条件查询
    *
    * @param select 查询对象
    * @return
    */
    List<T> select(Select select);
```
###### 28：根据自定义条件查询
```java
  /**
    * 根据自定义条件查询
    *
    * @param returnType 指定返回类型，如简化的实体类、或者基本类型、Map
    * @param select     查询对象
    * @return
    */
    <O> List<O> select(Class<O> returnType, Select select);
```
###### 29：根据自定义条件统计
```java
  /**
    * 根据自定义条件统计
    *
    * @param select 查询对象
    * @return
    */
    int count(Select select);
```
###### 30：根据自定义条件统计
```java
  /**
    * 根据自定义条件统计(内置方法使用)
    *
    * @param clazz
    * @param select 查询对象
    * @return
    */
    int count(Class<?> clazz, Select select);
```
