#### 更多实例Sqlbean使用实例以及代码生成点击这里👉 [https://gitee.com/iJovi/sqlbean-example](https://gitee.com/iJovi/sqlbean-example "sqlbean-example")
#### 一. Select对象使用示例（复杂查询或灵活性较高时使用，查看下方文档使用更简便方式）
```java
    Select select = new Select();
    select.column(SqlEssay._all);
    select.column(SqlUser.headPortrait, "头像");
    select.column(SqlUser.nickname, "昵称");
    select.join(JoinType.INNER_JOIN, SqlUser._tableAlias, SqlUser.id, SqlEssay.userId);
    select.where().eq(SqlEssay.userId, "1111");
    //value 直接输入字符串 会当作字符串处理，sql中会带''，如果希望不被做处理则使用Original
    select.where().eq(SqlFun.date_format(SqlEssay.creationTime$, "%Y-%m-%d"), SqlFun.date_format(SqlFun.now(), "%Y-%m-%d"));
    select.orderByDesc(SqlEssay.id);
    essayService.select(select);
```
#### 二. SelectService接口文档
###### 1.根据id条件查询
```java
    /**
     * 根据id条件查询
     *
     * @param id 唯一id
     * @return
     */
    T selectById(ID id);
```
###### 2.根据id条件查询(可指定返回类型、查询的表)
```java
    /**
     * 根据id条件查询(可指定返回类型、查询的表)
     *
     * @param returnType 指定返回到类型
     * @param id         唯一id
     * @return
     */
    <R> R selectById(Class<R> returnType, ID id);
```
###### 3.根据ids条件查询
```java
    /**
     * 根据ids条件查询
     *
     * @param ids 唯一id数组
     * @return
     */
    List<T> selectByIds(ID... ids);
```
###### 4.根据id条件查询(可指定返回类型、查询的表)
```java
    /**
     * 根据id条件查询(可指定返回类型、查询的表)
     *
     * @param returnType 指定返回到类型
     * @param ids        唯一id数组
     * @return
     */
    <R> List<R> selectByIds(Class<R> returnType, ID... ids);
```
###### 5.根据自定义条件查询 只返回一条记录
```java
    /**
     * 根据自定义条件查询 只返回一条记录
     *
     * @param select 查询对象
     * @return
     */
    T selectOne(Select select);
```
###### 6.根据自定义条件查询 只返回一条记录(可指定返回类型)
```java
    /**
     * 根据自定义条件查询 只返回一条记录(可指定返回类型)
     *
     * @param returnType 指定返回到类型
     * @param select     查询对象
     * @return
     */
    <R> R selectOne(Class<R> returnType, Select select);
```
###### 7.根据自定义条件查询返回Map
```java
    /**
     * 根据自定义条件查询返回Map
     *
     * @param select 查询对象
     * @return
     */
    Map<String, Object> selectMap(Select select);
```
###### 8.根据条件查询
```java
    /**
     * 根据条件查询
     *
     * @param where 查询条件
     * @param args  条件参数
     * @return
     */
    T selectOneBy(String where, Object... args);
```
###### 9.根据条件查询(可指定返回类型、查询的表)
```java
    /**
     * 根据条件查询(可指定返回类型、查询的表)
     *
     * @param returnType 指定返回到类型
     * @param where      查询条件
     * @param args       条件参数
     * @return
     */
    <R> R selectOneBy(Class<R> returnType, String where, Object... args);
```
###### 10.根据条件查询
```java
    /**
     * 根据条件查询
     *
     * @param where 条件包装器
     * @return
     */
    T selectOneBy(Wrapper where);
```
###### 11.根据条件查询
```java
    /**
     * 根据条件查询
     *
     * @param returnType 指定返回到类型
     * @param where      条件包装器
     * @param <R>
     * @return
     */
    <R> R selectOneBy(Class<R> returnType, Wrapper where);
```
###### 12.根据条件查询
```java
    /**
     * 根据条件查询
     *
     * @param where 查询条件
     * @param args  条件参数
     * @return
     */
    List<T> selectBy(String where, Object... args);
```
###### 13.根据条件查询
```java
    /**
     * 根据条件查询
     *
     * @param where 条件包装器
     * @return
     */
    List<T> selectBy(Wrapper where);
```
###### 14.根据条件查询
```java
    /**
     * 根据条件查询
     *
     * @param paging 分页对象
     * @param where  查询条件
     * @param args   条件参数
     * @return
     */
    List<T> selectBy(Paging paging, String where, Object... args);
```
###### 15.根据条件查询
```java
    /**
     * 根据条件查询
     *
     * @param paging 分页对象
     * @param where  条件包装器
     * @return
     */
    List<T> selectBy(Paging paging, Wrapper where);
```
###### 16.根据条件查询(可指定返回类型、查询的表)
```java
    /**
     * 根据条件查询(可指定返回类型、查询的表)
     *
     * @param returnType 指定返回到类型
     * @param where      查询条件
     * @param args       条件参数
     * @return
     */
    <R> List<R> selectBy(Class<R> returnType, String where, Object... args);
```
###### 17.根据条件查询(可指定返回类型、查询的表)
```java
    /**
     * 根据条件查询(可指定返回类型、查询的表)
     *
     * @param returnType 指定返回到类型
     * @param where      条件包装器
     * @param <R>
     * @return
     */
    <R> List<R> selectBy(Class<R> returnType, Wrapper where);
```
###### 18.根据条件查询(可指定返回类型、查询的表)
```java
    /**
     * 根据条件查询(可指定返回类型、查询的表)
     *
     * @param returnType 指定返回到类型
     * @param paging     分页对象
     * @param where      查询条件
     * @param args       条件参数
     * @return
     */
    <R> List<R> selectBy(Class<R> returnType, Paging paging, String where, Object... args);
```
###### 19.根据条件查询(可指定返回类型、查询的表)
```java
    /**
     * 根据条件查询(可指定返回类型、查询的表)
     *
     * @param returnType 指定返回到类型
     * @param paging     分页对象
     * @param where      条件包装器
     * @param <R>
     * @return
     */
    <R> List<R> selectBy(Class<R> returnType, Paging paging, Wrapper where);
```
###### 20.根据条件查询统计
```java
    /**
     * 根据条件查询统计
     *
     * @param where 查询条件
     * @param args  条件参数
     * @return
     */
    int countBy(String where, Object... args);
```
###### 21.根据条件查询统计
```java
    /**
     * 根据条件查询统计
     *
     * @param where 条件包装器
     * @return
     */
    int countBy(Wrapper where);
```
###### 22.统计全部
```java
    /**
     * 统计全部
     *
     * @return
     */
    int count();
```
###### 23.查询全部
```java
    /**
     * 查询全部
     *
     * @return
     */
    List<T> select();
```
###### 24.查询全部
```java
    /**
     * 查询全部(可指定返回类型、查询的表)
     *
     * @param returnType 指定返回到类型
     * @return
     */
    <R> List<R> select(Class<R> returnType);
```
###### 25.查询全部
```java
    /**
     * 查询全部
     *
     * @param paging 分页对象
     * @return
     */
    List<T> select(Paging paging);
```
###### 26.查询全部(可指定返回类型、查询的表)
```java
    /**
     * 查询全部(可指定返回类型、查询的表)
     *
     * @param returnType 指定返回到类型
     * @param paging     分页对象
     * @return
     */
    <R> List<R> select(Class<R> returnType, Paging paging);
```
###### 27.根据自定义条件查询（可自动分页）返回List<Map>结果集
```java
    /**
     * 根据自定义条件查询（可自动分页）返回List<Map>结果集
     *
     * @param select 查询对象
     * @return
     */
    List<Map<String, Object>> selectMapList(Select select);
```
###### 28.根据自定义条件查询（可自动分页）返回List<T>
```java
    /**
     * 根据自定义条件查询（可自动分页）返回List<T>
     *
     * @param select 查询对象
     * @return
     */
    List<T> select(Select select);
```
###### 29.根据自定义条件查询（可自动分页）返回List<R> (可指定返回类型、查询的表)
```java
    /**
     * 根据自定义条件查询（可自动分页）返回List<R> (可指定返回类型、查询的表)
     *
     * @param returnType 指定返回到类型
     * @param select     查询对象
     * @return
     */
    <R> List<R> select(Class<R> returnType, Select select);
```
###### 30.根据自定义条件统计
```java
    /**
     * 根据自定义条件统计
     *
     * @param select 查询对象
     * @return
     */
    int count(Select select);
```
###### 31.根据自定义条件统计
```java
    /**
     * 根据自定义条件统计
     *
     * @param returnType 指定返回到类型
     * @param select     查询对象
     * @return
     */
    int count(Class<?> returnType, Select select);
```
###### 32.分页
```java
    /**
     * 分页
     *
     * @param select     查询对象
     * @param pageHelper 分页助手
     * @return
     */
    ResultData<T> paging(Select select, PageHelper<T> pageHelper);
```
###### 33.分页
```java
    /**
     * 分页
     *
     * @param select  查询对象
     * @param pagenum 当前页
     * @param pagenum 每页数量
     * @return
     */
    ResultData<T> paging(Select select, int pagenum, int pagesize);
```
###### 34.分页
```java
    /**
     * 分页
     *
     * @param returnType 指定返回到类型
     * @param select     查询对象
     * @param pageHelper 分页助手
     * @param <R>
     * @return
     */
    <R> ResultData<R> paging(Class<R> returnType, Select select, PageHelper<R> pageHelper);
```
###### 35.分页
```java
    /**
     * 分页
     *
     * @param returnType 指定返回到类型
     * @param select     查询对象
     * @param pagenum    当前页
     * @param pagenum    每页数量
     * @param <R>
     * @return
     */
    <R> ResultData<R> paging(Class<R> returnType, Select select, int pagenum, int pagesize);
```