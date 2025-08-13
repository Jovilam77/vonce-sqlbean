#### 更多实例FlexSQL使用实例以及代码生成点击这里👉 [https://gitee.com/iJovi/flexsql-example](https://gitee.com/iJovi/flexsql-example "FlexSQL-Example")
#### 一. Update对象使用示例（通常情况下不使用该方式，查看下方文档使用更简便方式）
######1.包装Update对象进行更新
```java
    Essay essay = essayService.selectById(1L);

    Update<Essay> update = new Update<>();
    //delete.setTable("t_essay");//不需要设置会默认填充
    //update.setTable(Essay.class);//不需要设置会默认填充
    //作为更新的数据模板对象
    update.setUpdateBean(essay);
    //是否仅更新不为null的字段
    update.setUpdateNotNull(true);
    //是否使用乐观锁
    update.setOptimisticLock(false);
    //需要过滤不更新的表字段
    update.filterFields(Essay::getUserId);

    //根据id更新
    update.where(Essay::getId, essay.getId());
    //其他写法
    //update.where(Wrapper.where(Cond.eq(Essay::getId, essay.getId())));
    //update.where("& = ?", Essay$.id, essay.getId());
    
    essayService.update(update);
```
######2.拟SQL语句方式更新
```java
userService.update(new Update<User>().set(User::getNickName, "Vicky").set(User::getAge, 19).where().eq(User::getId, UserStatus.NORMAL).back();
```
#### 二. UpdateService接口文档
###### 1：根据id条件更新
```java
    /**
     * 根据id条件更新
     *
     * @param bean 更新的bean实体
     * @param id   id条件
     * @return
     */
    int updateById(T bean, ID id);
```
###### 2：根据id条件更新
```java
    /**
     * 根据id条件更新
     *
     * @param bean           更新的bean实体
     * @param id             id条件
     * @param updateNotNull  是否仅更新不为null的字段
     * @param optimisticLock 是否使用乐观锁
     * @return
     */
    int updateById(T bean, ID id, boolean updateNotNull, boolean optimisticLock);
```
###### 3：根据实体类id条件更新
```java
    /**
     * 根据实体类id条件更新
     *
     * @param bean           更新的bean实体
     * @param id             id条件
     * @param updateNotNull  是否仅更新不为null的字段
     * @param optimisticLock 是否使用乐观锁
     * @param filterColumns  过滤不需更新的字段
     * @return
     */
    int updateById(T bean, ID id, boolean updateNotNull, boolean optimisticLock, Column... filterColumns);
```
###### 4：根据实体类id条件更新
```java
    /**
     * 根据实体类id条件更新
     *
     * @param bean           更新的bean实体
     * @param id             id条件
     * @param updateNotNull  是否仅更新不为null的字段
     * @param optimisticLock 是否使用乐观锁
     * @param filterColumns  过滤不需更新的字段
     * @return
     */
<R> int updateById(T bean, ID id, boolean updateNotNull, boolean optimisticLock, ColumnFun<T, R>... filterColumns);
```
###### 5：根据实体类id条件更新
```java
    /**
     * 根据实体类id条件更新
     *
     * @param bean 更新的bean实体
     * @return
     */
    int updateByBeanId(T bean);
```
###### 6：根据实体类id条件更新
```java
    /**
     * 根据实体类id条件更新
     *
     * @param bean           更新的bean实体
     * @param updateNotNull  是否仅更新不为null的字段
     * @param optimisticLock 是否使用乐观锁
     * @return
     */
    int updateByBeanId(T bean, boolean updateNotNull, boolean optimisticLock);
```
###### 7：根据实体类id条件更新
```java
    /**
     * 根据实体类id条件更新
     *
     * @param bean           更新的bean实体
     * @param updateNotNull  是否仅更新不为null的字段
     * @param optimisticLock 是否使用乐观锁
     * @param filterColumns  过滤不需更新的字段
     * @return
     */
    int updateByBeanId(T bean, boolean updateNotNull, boolean optimisticLock, Column... filterColumns);
```
###### 8：根据实体类id条件更新
```java
    /**
     * 根据实体类id条件更新
     *
     * @param bean           更新的bean实体
     * @param updateNotNull  是否仅更新不为null的字段
     * @param optimisticLock 是否使用乐观锁
     * @param filterColumns  过滤不需更新的字段
     * @return
     */
<R> int updateByBeanId(T bean, boolean updateNotNull, boolean optimisticLock, ColumnFun<T, R>... filterColumns);
```
###### 9：根据条件更新
```java
    /**
     * 根据条件更新
     *
     * @param bean  更新的bean实体
     * @param where 条件字符串表达式
     * @param args  条件参数
     * @return
     */
    int updateBy(T bean, String where, Object... args);
```
###### 10：根据条件更新
```java
     /**
     * 根据条件更新
     *
     * @param bean           更新的bean实体
     * @param updateNotNull  是否仅更新不为null的字段
     * @param optimisticLock 是否使用乐观锁
     * @param where          条件字符串表达式
     * @param args           条件参数
     * @return
     */
    int updateBy(T bean, boolean updateNotNull, boolean optimisticLock, String where, Object... args);
```
###### 11：更新根据条件更新
```java
    /**
     * 根据条件更新
     *
     * @param where 条件包装器
     * @return
     */
    int updateBy(T bean, Wrapper where);
```
###### 12：根据条件更新
```java
    /**
     * 根据条件更新
     *
     * @param bean           更新的bean实体
     * @param updateNotNull  是否仅更新不为null的字段
     * @param optimisticLock 是否使用乐观锁
     * @param wrapper        条件包装器
     * @return
     */
    int updateBy(T bean, boolean updateNotNull, boolean optimisticLock, Wrapper wrapper);
```
###### 13：根据条件更新
```java
    /**
     * 根据条件更新
     *
     * @param bean           更新的bean实体
     * @param updateNotNull  是否仅更新不为null的字段
     * @param optimisticLock 是否使用乐观锁
     * @param wrapper        条件包装器
     * @return
     */
    int updateBy(T bean, boolean updateNotNull, boolean optimisticLock, Wrapper wrapper, Column... filterColumns);
```
###### 14：根据条件更新
```java
    /**
     * 根据条件更新
     *
     * @param bean           更新的bean实体
     * @param updateNotNull  是否仅更新不为null的字段
     * @param optimisticLock 是否使用乐观锁
     * @param wrapper        条件包装器
     * @return
     */
    <R> int updateBy(T bean, boolean updateNotNull, boolean optimisticLock, Wrapper wrapper, ColumnFun<T, R>... filterColumns);
```
###### 15：根据条件更新
```java
    /**
     * 根据条件更新
     *
     * @param bean           更新的bean实体
     * @param updateNotNull  是否仅更新不为null的字段
     * @param optimisticLock 是否使用乐观锁
     * @param filterColumns  过滤不需更新的字段
     * @param where          条件字符串表达式
     * @param args           条件参数
     * @return
     */
    int updateBy(T bean, boolean updateNotNull, boolean optimisticLock, Column[] filterColumns, String where, Object... args);
```
###### 16：根据实体类字段条件更新
```java
    /**
     * 根据实体类字段条件更新
     *
     * @param bean  更新的bean实体
     * @param where 条件字符串表达式
     * @return
     */
    int updateByBean(T bean, String where);
```
###### 17：根据实体类字段条件更新
```java
    /**
     * 根据实体类字段条件更新
     *
     * @param bean           更新的bean实体
     * @param updateNotNull  是否仅更新不为null的字段
     * @param optimisticLock 是否使用乐观锁
     * @param where          条件字符串表达式
     * @return
     */
    int updateByBean(T bean, boolean updateNotNull, boolean optimisticLock, String where);
```
###### 18：根据实体类字段条件更新
```java
    /**
     * 根据实体类字段条件更新
     *
     * @param bean           更新的bean实体
     * @param updateNotNull  是否仅更新不为null的字段
     * @param optimisticLock 是否使用乐观锁
     * @param where          条件字符串表达式
     * @param filterColumns  过滤不需更新的字段
     * @return
     */
    int updateByBean(T bean, boolean updateNotNull, boolean optimisticLock, String where, Column... filterColumns);
```
###### 19：根据实体类字段条件更新
```java
    /**
     * 根据实体类字段条件更新
     *
     * @param bean           更新的bean实体
     * @param updateNotNull  是否仅更新不为null的字段
     * @param optimisticLock 是否使用乐观锁
     * @param where          条件字符串表达式
     * @param filterColumns  过滤不需更新的字段
     * @return
     */
    <R> int updateByBean(T bean, boolean updateNotNull, boolean optimisticLock, String where, ColumnFun<T, R>[] filterColumns);
```
###### 20：自定义更新
```java
    /**
     * 更新(where条件为空会抛异常，因为更新全部非常危险)
     *
     * @param update 更新对象
     * @return
     */
    int update(Update<T> update);
```
###### 20：自定义更新
```java
    /**
     * 更新
     *
     * @param update 更新对象
     * @param ignore 如果为true则不指定where条件也能执行，false则抛异常
     * @return
     */
    int update(Update<T> update, boolean ignore);
```
