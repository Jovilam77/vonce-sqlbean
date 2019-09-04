## Select
##### Select对象的方法请看代码中的文档描述
#### 方法和示例
###### 1：根据id条件查询
```java
T selectById(Object id);
```
```java
@RequestMapping(value = "getEssay", method = RequestMethod.GET)
@ResponseBody
public RS getEssay() {
	Essay essay = essayService.selectById(1);
	return super.successHint("获取成功",essay);
}
```
###### 2：根据id条件查询（指定返回类型）
```java
<O> O selectById(Class<O> returnType, Object id);
```
```java
@RequestMapping(value = "getEssay", method = RequestMethod.GET)
@ResponseBody
public RS getEssay() {
	EssayPojo essayPojo = essayService.selectById(EssayPojo.class,1);
	//也可以这样使用
	//返回第一个字段内容，可以用你想要的类型接收
	//String id = essayService.selectById(String.class,1);
	//返回Map
	//Map<String,Object> map = essayService.selectById(Map.class,1);
	return super.successHint("获取成功",essayPojo);
}
```
###### 3：根据ids条件查询
```java
List<T> selectByIds(Object... ids);
```
```java
@RequestMapping(value = "getEssay", method = RequestMethod.GET)
@ResponseBody
public RS getEssay() {
	List<Essay> essayList = essayService.selectByIds(1,2,3);
	return super.successHint("获取成功",essayList);
}
```
###### 4：根据ids条件查询（指定返回类型）
```java
<O> List<O> selectByIds(Class<O> returnType, Object... ids);
```
```java
@RequestMapping(value = "getEssay", method = RequestMethod.GET)
@ResponseBody
public RS getEssay() {
	List<EssayPojo> essayPojoList = essayService.selectByIds(EssayPojo.class, 1,2,3);
	//也可以这样使用
	//返回第一个字段内容，可以用你想要的类型接收
	//String id = essayService.selectByIds(String.class, 1,2,3);
	//返回Map
	//Map<String,Object> map = essayService.selectByIds(Map.class, 1,2,3);
	return super.successHint("获取成功",essayPojoList);
}
```
###### 5：根据自定义条件查询 只返回一条记录
```java
T selectOne(Select select);
```
```java
@RequestMapping(value = "getEssay", method = RequestMethod.GET)
@ResponseBody
public RS getEssay() {
	Select select = new Select();
	select.column("id");
	select.column("title");
	select.where("id",1);
	Essay essay = essayService.selectOne(select);
	return super.successHint("获取成功",essay);
}
```
###### 6：根据自定义条件查询 只返回一条记录（指定返回类型）
```java
<O> O selectOne(Class<O> returnType, Select select);
```
```java
@RequestMapping(value = "getEssay", method = RequestMethod.GET)
@ResponseBody
public RS getEssay() {
	Select select = new Select();
	select.column("id");
	select.column("title");
	select.where("id",1);
	EssayPojo essayPojo = essayService.selectOne(EssayPojo.class, select);	//也可以这样使用
	//返回第一个字段内容,column("你想要的字段")，可以用你想要的类型接收
	//String id = essayService.selectOne(String.class, select);
	//返回Map
	//Map<String,Object> map = essayService.selectOne(Map.class, select);
	return super.successHint("获取成功",essayPojo);
}
```
###### 7：根据自定义条件查询 返回Map
```java
Map<String, Object> selectMap(Select select);
```
```java
@RequestMapping(value = "getEssay", method = RequestMethod.GET)
@ResponseBody
public RS getEssay() {
	Select select = new Select();
	select.column("id");
	select.column("title");
	select.where("id",1);
	Map<String,Object> map = essayService.selectMap(select);
	return super.successHint("获取成功",map);
}
```
###### 8：根据条件查询
```java
T selectOneByCondition(String where, Object... args);
```
```java
@RequestMapping(value = "getEssay", method = RequestMethod.GET)
@ResponseBody
public RS getEssay() {
	Essay essay = essayService.selectOneByCondition("id = ?",1);
	return super.successHint("获取成功",essay);
}
```
###### 9：根据条件查询（指定返回类型）
```java
<O> O selectOneByCondition(Class<O> returnType, String where, Object... args);
```
```java
@RequestMapping(value = "getEssay", method = RequestMethod.GET)
@ResponseBody
public RS getEssay() {
	EssayPojo essayPojo = essayService.selectOneByCondition(EssayPojo.class, "id = ?",1);
	//也可以这样使用
	//返回第一个字段内容，可以用你想要的类型接收
	//String id = essayService.selectOneByCondition(String.class, "id = ?",1);
	//返回Map
	//Map<String,Object> map = essayService.selectOneByCondition(Map.class, "id = ?",1);
	return super.successHint("获取成功",essayPojo);
}
```
###### 10：根据条件查询
```java
List<T> selectByCondition(String where, Object... args);
```
```java
@RequestMapping(value = "getEssay", method = RequestMethod.GET)
@ResponseBody
public RS getEssay() {
	List<Essay> essayList = essayService.selectByCondition("userId = ?",888);
	return super.successHint("获取成功",essayPojo);
}
```
###### 11：根据条件查询 分页
```java
List<T> selectByCondition(Paging paging, String where, Object... args);
```
```java
@RequestMapping(value = "getEssay", method = RequestMethod.GET)
@ResponseBody
public RS getEssay() {
	//查询第1页，一页显示10条，根据创建时间降序
	Paging paging = new Paging(0,10,"creationTime","desc");
	//PageHelper<Essay> pageHelper = new PageHelper<>(request);
	//Paging paging = pageHelper.getPaging()
	List<Essay> essayList = essayService.selectByCondition(paging,"userId = ?",888);
	return super.successHint("获取成功",essayPojo);
}
```
###### 12：根据条件查询（指定返回类型）
```java
<O> List<O> selectByCondition(Class<O> returnType, String where, Object... args);
```
```java
@RequestMapping(value = "getEssay", method = RequestMethod.GET)
@ResponseBody
public RS getEssay() {
	List<EssayPojo> essayPojoList = essayService.selectByCondition(EssayPojo.class,
	"userId = ?",888);
	//也可以这样使用
	//返回第一个字段内容，可以用你想要的类型接收
	//List<String> idList = essayService.selectByCondition(String.class,
	"userId = ?",888);
	//返回Map
	//List<Map<String,Object>> mapList = essayService.selectByCondition(Map.class,
	"userId = ?",888);
	return super.successHint("获取成功",essayPojoList);
}
```
###### 13：根据条件查询 分页（指定返回类型）
```java
<O> List<O> selectByCondition(Class<O> returnType, Paging paging, String where, Object... args);
```
```java
@RequestMapping(value = "getEssay", method = RequestMethod.GET)
@ResponseBody
public RS getEssay() {
	//查询第1页，一页显示10条，根据创建时间降序
	Paging paging = new Paging(0,10,"creationTime","desc");
	//PageHelper<Essay> pageHelper = new PageHelper<>(request);
	//Paging paging = pageHelper.getPaging()
	List<EssayPojo> essayPojoList = essayService.selectByCondition(EssayPojo.class, paging, 
	"userId = ?",888);
	//也可以这样使用
	//返回第一个字段内容，可以用你想要的类型接收
	//List<String> idList = essayService.selectByCondition(String.class, paging, 
	"userId = ?",888);
	//返回Map
	//List<Map<String,Object>> mapList = essayService.selectByCondition(Map.class, paging, 
	"userId = ?",888);
	return super.successHint("获取成功",essayPojoList);
}
```
###### 14：根据条件查询统计
```java
long selectCountByCondition(String where, Object... args);
```
```java
@RequestMapping(value = "getEssay", method = RequestMethod.GET)
@ResponseBody
public RS getEssay() {
	long count = essayService.selectCountByCondition("userId = ?",888);
	return super.successHint("获取成功",count);
}
```
###### 15：统计全部
```java
long countAll();
```
```java
@RequestMapping(value = "getEssay", method = RequestMethod.GET)
@ResponseBody
public RS getEssay() {
	long count = essayService.countAll();
	return super.successHint("获取成功",count);
}
```
###### 15：查询全部
```java
List<T> selectAll();
```
```java
@RequestMapping(value = "getEssay", method = RequestMethod.GET)
@ResponseBody
public RS getEssay() {
	List<Essay> essayList = essayService.selectAll();
	return super.successHint("获取成功",essayList);
}
```
###### 16：查询全部（指定返回类型）
```java
<O> List<O> selectAll(Class<O> returnType);
```
```java
@RequestMapping(value = "getEssay", method = RequestMethod.GET)
@ResponseBody
public RS getEssay() {
	List<EssayPojo> essayPojoList = essayService.selectAll(EssayPojo.class);
	//也可以这样使用
	//返回第一个字段内容，可以用你想要的类型接收
	//List<String> idList = essayService.selectAll(String.class);
	//返回Map
	//List<Map<String,Object>> mapList = essayService.selectAll(Map.class);
	return super.successHint("获取成功",essayPojoList);
}
```
###### 17：查询全部 分页
```java
List<T> selectAll(Paging paging);
```
```java
@RequestMapping(value = "getEssay", method = RequestMethod.GET)
@ResponseBody
public RS getEssay() {
	//查询第1页，一页显示10条，根据创建时间降序
	Paging paging = new Paging(0,10,"creationTime","desc");
	//PageHelper<Essay> pageHelper = new PageHelper<>(request);
	//Paging paging = pageHelper.getPaging()
	List<Essay> essayList = essayService.selectAll(paging);
	return super.successHint("获取成功",essayList);
}
```
###### 18：查询全部 分页（指定返回类型）
```java
<O> List<O> selectAll(Class<O> returnType, Paging paging);
```
```java
@RequestMapping(value = "getEssay", method = RequestMethod.GET)
@ResponseBody
public RS getEssay() {
	//查询第1页，一页显示10条，根据创建时间降序
	Paging paging = new Paging(0,10,"creationTime","desc");
	//PageHelper<Essay> pageHelper = new PageHelper<>(request);
	//Paging paging = pageHelper.getPaging()
	List<EssayPojo> essayPojoList = essayService.selectAll(EssayPojo.class,paging);
	//也可以这样使用
	//返回第一个字段内容，可以用你想要的类型接收
	//List<String> idList = essayService.selectAll(String.class,paging);
	//返回Map
	//List<Map<String,Object>> mapList = essayService.selectAll(Map.class,paging);
	return super.successHint("获取成功",essayPojoList);
}
```
###### 19：根据自定义条件查询 返回Map
```java
List<Map<String, Object>> selectMapList(Select select);
```
```java
@RequestMapping(value = "getEssay", method = RequestMethod.GET)
@ResponseBody
public RS getEssay() {
	Select select = new Select();
	select.column("id");
	select.column("title");
	List<Map<String, Object>> mapList = essayService.selectMapList(select);
	return super.successHint("获取成功",mapList);
}
```
###### 21：根据自定义条件查询
```java
List<T> select(Select select);
```
```java
@RequestMapping(value = "getEssay", method = RequestMethod.GET)
@ResponseBody
public RS getEssay() {
	Select select = new Select();
	select.column("id");
	select.column("title");
	List<Essay> essayList = essayService.select(select);
	return super.successHint("获取成功",essayList);
}
```
###### 22：根据自定义条件查询
```java
<O> List<O> select(Class<O> returnType, Select select);
```
```java
@RequestMapping(value = "getEssay", method = RequestMethod.GET)
@ResponseBody
public RS getEssay() {
	Select select = new Select();
	select.column("id");
	select.column("title");
	List<EssayPojo> essayPojoList = essayService.select(EssayPojo.class, select);
	//也可以这样使用
	//返回第一个字段内容,column("你想要的字段")，可以用你想要的类型接收
	//List<String> idList = essayService.select(String.class,select);
	//返回Map
	//List<Map<String,Object>> mapList = essayService.select(Map.class,select);
	return super.successHint("获取成功",essayPojoList);
}
```
###### 23：根据自定义条件查询
```java
long count(Select select);
```
```java
@RequestMapping(value = "getEssay", method = RequestMethod.GET)
@ResponseBody
public RS getEssay() {
	Select select = new Select();
	select.column("id");
	select.column("title");
	long count = essayService.select(select);
	return super.successHint("获取成功",count);
}
```
###### 24：根据自定义条件查询（插件内部使用）
```java
long count(Class<?> clazz, Select select);
```
```java
@RequestMapping(value = "getEssay", method = RequestMethod.GET)
@ResponseBody
public RS getEssay() {
	Select select = new Select();
	select.column("id");
	select.column("title");
	long count = essayService.select(EssayPojo.class, select);
	return super.successHint("获取成功",count);
}
```