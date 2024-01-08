#### 一. Update对象使用示例（通常情况下不使用该方式，查看下方文档使用更简便方式）
```java
    Essay essay = essayService.selectById(1L);

    Update<Essay> update = new Update<>();
    //delete.setTable("t_essay");
    update.setTable(Essay.class);
    //作为更新的数据模板对象
    update.setUpdateBean(essay);
    //是否仅更新不为null的字段
    update.setUpdateNotNull(true);
    //是否使用乐观锁
    update.setOptimisticLock(false);
    //需要过滤不更新的表字段
    //update.setFilterFields(new String[]{"user_id"});
    update.setFilterFields(new String[]{SqlEssay.user_id.name()});

    //根据id更新
    update.where(SqlEssay.id, essay.getId());
    //其他写法
    //update.setWhere(Wrapper.where(Cond.eq(SqlEssay.id, essay.getId())));
    //update.setWhere("& = ?", SqlEssay.id, essay.getId());
    
    essayService.update(update);
```
#### 二. UpdateService接口文档
###### 1：根据id条件更新
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
###### 2：根据实体类id条件更新
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
###### 3：根据实体类id条件更新
```java
  /**
    * 根据实体类id条件更新
    *
    * @param bean           更新的bean实体
    * @param updateNotNull  是否仅更新不为null的字段
    * @param optimisticLock 是否使用乐观锁
    * @param filterFields   过滤不需更新的字段
    * @return
    */
    int updateByBeanId(T bean, boolean updateNotNull, boolean optimisticLock, String[] filterFields);
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
    * @param filterFields   过滤不需更新的字段
    * @return
    */
    int updateById(T bean, ID id, boolean updateNotNull, boolean optimisticLock, String[] filterFields);
```
###### 5：根据条件更新
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
###### 6：根据条件更新
```java
  /**
    * 根据条件更新
    *
    * @param bean           更新的bean实体
    * @param updateNotNull  是否仅更新不为null的字段
    * @param optimisticLock 是否使用乐观锁
    * @param where          条件包装器
    * @return
    */
    int updateBy(T bean, boolean updateNotNull, boolean optimisticLock, Wrapper where);
```
###### 7：根据条件更新
```java
  /**
    * 根据条件更新
    *
    * @param bean           更新的bean实体
    * @param updateNotNull  是否仅更新不为null的字段
    * @param optimisticLock 是否使用乐观锁
    * @param filterFields   过滤不需更新的字段
    * @param where          条件字符串表达式
    * @param args           条件参数
    * @return
    */
    int updateBy(T bean, boolean updateNotNull, boolean optimisticLock, String[] filterFields, String where, Object... args);
```
###### 8：根据条件更新
```java
  /**
    * 根据条件更新
    *
    * @param bean           更新的bean实体
    * @param updateNotNull  是否仅更新不为null的字段
    * @param optimisticLock 是否使用乐观锁
    * @param filterFields   过滤不需更新的字段
    * @param where          条件包装器
    * @return
    */
    int updateBy(T bean, boolean updateNotNull, boolean optimisticLock, String[] filterFields, Wrapper where);
```
###### 9：根据实体类字段条件更新
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
###### 10：根据实体类字段条件更新
```java
  /**
    * 根据实体类字段条件更新
    *
    * @param bean           更新的bean实体
    * @param updateNotNull  是否仅更新不为null的字段
    * @param optimisticLock 是否使用乐观锁
    * @param filterFields   过滤不需更新的字段
    * @param where          条件字符串表达式
    * @return
    */
    int updateByBean(T bean, boolean updateNotNull, boolean optimisticLock, String[] filterFields, String where);
```
###### 11：更新
```java
  /**
    * 更新(where条件为空会抛异常，因为更新全部非常危险)
    *
    * @param update 更新对象
    * @return
    */
    int update(Update<T> update);
```
###### 12：更新
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
