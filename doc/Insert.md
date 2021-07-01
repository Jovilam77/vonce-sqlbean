#### Insert使用示例
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
     int inset(Insert<T> insert);
```
