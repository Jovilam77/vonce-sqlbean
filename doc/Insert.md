#### Insert对象使用示例（常规情况下不使用该方式，查看下方文档使用更简便方式）
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
#### InsertService接口文档
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
     int insert(List<T> beanList);
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
