## Delete
##### Delete对象的方法请看代码中的文档描述
#### 方法和示例
###### 1：根据id条件删除
```java
long deleteById(Object... id);
```
```java
@RequestMapping(value = "delete", method = RequestMethod.DELETE)
@ResponseBody
public RS delete() {
	long i = essayService.deleteById(1);
	if(i > 0){
		super.successHint("删除成功");
	}
	return super.othersHint("删除失败");
}
```
###### 2：根据条件删除
```java
long deleteByCondition(String where, Object... args);
```
```java
@RequestMapping(value = "delete", method = RequestMethod.DELETE)
@ResponseBody
public RS delete() {
	long i = essayService.deleteByCondition("id > ? and id < ?" ,1 ,10);
	if(i > 0){
		super.successHint("删除成功");
	}
	return super.othersHint("删除失败");
}
```
###### 3：删除（where条件为空会抛异常，因为删除全部非常危险）
```java
long delete(Delete delete);
```
```java
@RequestMapping(value = "delete", method = RequestMethod.DELETE)
@ResponseBody
public RS delete() {
	Delete delete = new Delete();
	delete.setDeleteBable(Essay.class);
	delete.where("id", 1);
	long i = essayService.delete(delete);
	if(i > 0){
		super.successHint("删除成功");
	}
	return super.othersHint("删除失败");
}
```
###### 3：删除（如果要删除全部可以用这个）
```java
long delete(Delete delete, boolean ignore);
```
```java
@RequestMapping(value = "delete", method = RequestMethod.Delete)
@ResponseBody
public RS delete() {
	Delete delete = new Delete();
	delete.setDeleteBable(Essay.class);
	//如果这里为false则抛异常
	long i = essayService.delete(delete,true);
	if(i > 0){
		super.successHint("删除成功");
	}
	return super.othersHint("删除失败");
}
```
###### 4：逻辑删除根据id条件
```java
long logicallyDeleteById(Object id);
```
```java
@RequestMapping(value = "delete", method = RequestMethod.DELETE)
@ResponseBody
public RS delete() {
	long i = essayService.logicallyDeleteById(1);
	if(i > 0){
		super.successHint("删除成功");
	}
	return super.othersHint("删除失败");
}
```
###### 5：根据id条件逻辑删除（需在实体类标记某个字段为逻辑删除字段，[请看注解文档](https://github.com/Jovilam77/vonce-sqlbean/blob/develop/doc/Annotation.md "请看注解文档")）
```java
long logicallyDeleteById(Object id);
```
```java
@RequestMapping(value = "delete", method = RequestMethod.DELETE)
@ResponseBody
public RS delete() {
	long i = essayService.logicallyDeleteById(1);
	if(i > 0){
		super.successHint("删除成功");
	}
	return super.othersHint("删除失败");
}
```
###### 6：根据条件逻辑删除（需在实体类标记某个字段为逻辑删除字段，[请看注解文档](https://github.com/Jovilam77/vonce-sqlbean/blob/develop/doc/Annotation.md "请看注解文档")）
```java
long logicallyDeleteByCondition(String where, Object... args);
```
```java
@RequestMapping(value = "delete", method = RequestMethod.DELETE)
@ResponseBody
public RS delete() {
	long i = essayService.logicallyDeleteByCondition("id > ? and id < ?" ,1 ,10);
	if(i > 0){
		super.successHint("删除成功");
	}
	return super.othersHint("删除失败");
}
```