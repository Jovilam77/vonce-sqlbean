## Update
##### Update对象的方法请看代码中的文档描述
#### 方法和示例
###### 1：根据id条件更新
```java
long updateById(T bean, Object id, boolean updateNotNull);
```
```java
@RequestMapping(value = "update", method = RequestMethod.PUT)
@ResponseBody
public RS update() {
	long i = essayService.updateById(1);
	if(i > 0){
		super.successHint("更新成功");
	}
	return super.othersHint("更新失败");
}
```
###### 2：根据实体类id条件更新
```java
long updateByBeanId(T bean, boolean updateNotNull);
```
```java
@RequestMapping(value = "update", method = RequestMethod.PUT)
@ResponseBody
public RS update() {
	Essay essay = new Essay();
	essay.setId(1);
	essay.setTitle("测试");
	essay.setContent("测试123");
	//true仅更新不为null的字段
	long i = essayService.updateByBeanId(essay, ture);
	if(i > 0){
		super.successHint("更新成功");
	}
	return super.othersHint("更新失败");
}
```
###### 3：根据实体类id条件更新（可过滤某些字段）
```java
long updateByBeanId(T bean, boolean updateNotNull, String[] filterFields);
```
```java
@RequestMapping(value = "update", method = RequestMethod.PUT)
@ResponseBody
public RS update() {
	Essay essay = new Essay();
	essay.setId(1);
	essay.setTitle("测试");
	essay.setContent("测试123");
	//true仅更新不为null的字段，filterFields过滤某些字段
	long i = essayService.updateByBeanId(essay, ture, new String[]{"title"});
	if(i > 0){
		super.successHint("更新成功");
	}
	return super.othersHint("更新失败");
}
```
###### 4：根据外部id条件更新（可过滤某些字段）
```java
long updateById(T bean, Object id, boolean updateNotNull, String[] filterFields);
```
```java
@RequestMapping(value = "update", method = RequestMethod.PUT)
@ResponseBody
public RS update() {
	Essay essay = new Essay();
	essay.setTitle("测试");
	essay.setContent("测试123");
	//true仅更新不为null的字段，filterFields过滤某些字段
	long i = essayService.updateById(essay, 20, ture, new String[]{"title"});
	if(i > 0){
		super.successHint("更新成功");
	}
	return super.othersHint("更新失败");
}
```
###### 5：根据条件更新
```java
long updateByCondition(T bean, boolean updateNotNull, String where, Object... args);
```
```java
@RequestMapping(value = "update", method = RequestMethod.PUT)
@ResponseBody
public RS update() {
	Essay essay = new Essay();
	essay.setTitle("测试");
	essay.setContent("测试123");
	//true仅更新不为null的字段
	long i = essayService.updateByCondition(essay, ture, "id > ? and id < ?", 1 ,10);
	if(i > 0){
		super.successHint("更新成功");
	}
	return super.othersHint("更新失败");
}
```
###### 6：根据条件更新（可过滤某些字段）
```java
long updateByCondition(T bean, boolean updateNotNull, String[] filterFields, String where, Object... args);
```
```java
@RequestMapping(value = "update", method = RequestMethod.PUT)
@ResponseBody
public RS update() {
	Essay essay = new Essay();
	essay.setId(1);
	essay.setTitle("测试");
	essay.setContent("测试123");
	//true仅更新不为null的字段，filterFields过滤某些字段
	long i = essayService.updateByCondition(essay, ture, new String[]{"id"},
	"id > ? and id < ?", 1 ,10);
	if(i > 0){
		super.successHint("更新成功");
	}
	return super.othersHint("更新失败");
}
```
###### 7：根据实体类字段条件更新
```java
long updateByBeanCondition(T bean, boolean updateNotNull, String where);
```
```java
@RequestMapping(value = "update", method = RequestMethod.PUT)
@ResponseBody
public RS update() {
	Essay essay = new Essay();
	essay.setId(1);
	essay.setTitle("测试");
	essay.setContent("测试123");
	//true仅更新不为null的字段，${为你实体类中的字段名}
	long i = essayService.updateByBeanCondition(essay, ture, "id = ${id}");
	if(i > 0){
		super.successHint("更新成功");
	}
	return super.othersHint("更新失败");
}
```
###### 8：根据实体类字段条件更新（可过滤某些字段）
```java
long updateByBeanCondition(T bean, boolean updateNotNull, String[] filterFields, String where);
```
```java
@RequestMapping(value = "update", method = RequestMethod.PUT)
@ResponseBody
public RS update() {
	Essay essay = new Essay();
	essay.setId(1);
	essay.setTitle("测试");
	essay.setContent("测试123");
	//true仅更新不为null的字段，filterFields过滤某些字段，${为你实体类中的字段名}
	long i = essayService.updateByBeanCondition(essay, ture, new String[]{"title"},
	"id = ${id}");
	if(i > 0){
		super.successHint("更新成功");
	}
	return super.othersHint("更新失败");
}
```
###### 9：更新（where条件为空会抛异常，因为更新全部非常危险）
```java
long updateByBeanCondition(T bean, boolean updateNotNull, String[] filterFields, String where);
```
```java
@RequestMapping(value = "update", method = RequestMethod.PUT)
@ResponseBody
public RS update() {
	Essay essay = new Essay();
	essay.setTitle("测试");
	essay.setContent("测试123");
	Update update = new Update();
	update.setUpdateBean(essay);
	update.where("id", 1);
	essayService.update(update);
	if(i > 0){
		super.successHint("更新成功");
	}
	return super.othersHint("更新失败");
}
```
###### 10：更新（如果要更新全部可以用这个）
```java
long updateByBeanCondition(T bean, boolean updateNotNull, String[] filterFields, String where);
```
```java
@RequestMapping(value = "update", method = RequestMethod.PUT)
@ResponseBody
public RS update() {
	Essay essay = new Essay();
	essay.setTitle("测试");
	essay.setContent("测试123");
	Update update = new Update();
	update.setUpdateBean(essay);
	//如果这里为false则抛异常
	essayService.update(update, true);
	if(i > 0){
		super.successHint("更新成功");
	}
	return super.othersHint("更新失败");
}
```