#### 更多实例FlexSQL使用实例以及代码生成点击这里👉 [https://gitee.com/iJovi/flexsql-example](https://gitee.com/iJovi/flexsql-example "FlexSQL-Example")
#### 一. Insert对象使用示例（通常情况下不使用该方式，查看下方文档使用更简便方式）
######1.使用实体类方式插入
```java
    Date date = new Date();
    //单条插入
    Essay essay = new Essay();
    essay.setId(1L);
    essay.setUserId(222L);
    essay.setContent("内容");
    essay.setCreationTime(date);

    Insert<Essay> insert1 = new Insert();
	//insert1.setTable("t_essay");
    insert1.setTable(Essay.class);
    insert1.setInsertBean(essay);
    essayService.insert(insert1);

    //批量插入
    List<Essay> list = new ArrayList<>();
    for (int i = 0; i < 100; i++) {
        essay = new Essay();
        essay.setId((long) i);
        essay.setUserId((long) (10000 + i));
        essay.setContent("内容" + i);
        essay.setCreationTime(date);
        list.add(essay);
    }
	
    Insert<Essay> insert2 = new Insert();
	//insert2.setTable("t_essay");
    insert2.setTable(Essay.class);
    insert2.setInsertBean(essay);
    essayService.insert(insert2);
```
######2.拟SQL语句方式插入
```java
Insert userInsert = new Insert<User>().column(
                        User::getId,
                        User::getAge,
                        User::getUserName,
                        User::getNickName,
                        User::getEmail,
                        User::getGender,
                        User::getMobilePhone,
                        User::getStatus,
                        User::getPassword)
                .values(IdBuilder.snowflake16(),
                        18,
                        "Jovi",
                        "Jovi Lam",
                        "imjovi@qq.com",
                        1,
                        "18888888888",
                        UserStatus.NORMAL,
                        "123456");
userService.insert(userInsert);
```
#### 二. InsertService接口文档
###### 1：插入单条或多条数组形式的数据
```java
  /**
    * 插入数据
    *
    * @param bean 单个实体或数组
    * @return
    */
    @SuppressWarnings("unchecked")
    int insert(T... bean);
```
###### 2：插入多条List形式的数据
```java
   /**
     * 插入数据
     *
     * @param beanList 实体列表
     * @return
     */
     @SuppressWarnings("unchecked")
     int insert(Collection<T> beanList);
```
###### 3：插入数据
```java
   /**
     * 插入数据
     *
     * @param insert 插入对象
     * @return
     */
     int insert(Insert<T> insert);
```
