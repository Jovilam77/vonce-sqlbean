## Insert
##### Insert对象的方法请看代码中的文档描述
#### 方法和示例
###### 1：插入单条或多条数组形式的数据
```java
long insert(T... bean);
```
```java
    @RequestMapping(value = "insert", method = RequestMethod.POST)
    @ResponseBody
    public RS insert() {
        Essay essay = new Essay();
        essay.setId(1);
		essay.setUserId(1111);
		essay.setTitle("测试")
        essay.setContent("测试123");
        essay.setCreationTime(new Date());
        long i = essayService.insert(essay);
        if (i > 0) {
            return super.successHint("插入成功");
        }
        return super.successHint("插入失败");
    }
```
###### 2：插入多条List形式的数据
```java
long insert(List<T> beanList);
```
```java
    @RequestMapping(value = "insert", method = RequestMethod.POST)
    @ResponseBody
    public RS insert() {
        List<Essay> list = new ArrayList<>();
        Date date = new Date();
        for (int i = 0; i < 100; i++) {
            Essay essay = new Essay();
            essay.setId(i);
			essay.setUserId(1111);
            essay.setContent("测试" + i);
            essay.setCreationTime(date);
            list.add(essay);
        }
        long i = essayService.insert(list);
        if (i > 0) {
            return super.successHint("插入成功");
        }
        return super.successHint("插入失败");
    }
```
###### 3：插入数据
```java
long inset(Insert insert);
```
```java
    @RequestMapping(value = "insert", method = RequestMethod.POST)
    @ResponseBody
    public RS insert() {
        Essay essay = new Essay();
        essay.setId(1);
		essay.setUserId(1111);
		essay.setTitle("测试")
        essay.setContent("测试123");
        essay.setCreationTime(new Date());
		
		Insert insert = new Insert();
		//这里可以是单个对象也可以是数组或者List
        insert.setInsertBean(essay);
        long i = essayService.inset(insert);
        if (i > 0) {
            return super.successHint("插入成功");
        }
        return super.successHint("插入失败");
    }
```
