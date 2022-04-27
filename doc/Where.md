#### 一. Where条件
```java
//SQL: where id = 1 or id = 2
Select select = new Select();
select.where().eq("id", 1).or().eq("id", 2);
```

#### 二. Where条件表达式
```java
//该方式设置where条件以手写sql表达式进行, 不支持where以外的语法, 如group、order、limit等, 占位符“&”代表字段名称, 占位符“?”代表字段值。

//SQL: where status = 7 and type = 1
//那么使用占位符的方式即为
setWhere("status = ? AND type = ?", 7, 1); //这种方式sql的列字段仍自己手写
setWhere("& > ? AND & = ?", User$.status, 10, User$.type, 1);//建议采用此方式
	 
//此例子同样适用SqlBeanService接口中内置的xxxByCondition()方法
//SqlUser类 maven编译后自动生成, 如不理解请查看注解文档

```

#### 三. Where条件包装器（推荐使用）
```java
//SQL: where id = 1 and content = '222'
setWhere(
       Wrapper.where(Cond.gt(Essay$.id, 1)).
	           and(Cond.eq(Essay$.content, "222"))
   );

//SQL（优先运算）: where content = '222' and (type = 1 or type = 2)
setWhere(
       Wrapper.where(Cond.gt(Essay$.content, "222")).
	           and(
			       Wrapper.where(Cond.gt(Essay$.type, 1)).
	                       or(Cond.eq(Essay$.type, 2))
				)
   );

```

#### 四. 原始值Original
```
在where条件中, 有些条件的值可能等于某个字段或者等于某个函数, 这个时候传入的值就不能对它进行处理。
比如说Java中: select.where("time", "DATE_FORMAT(NOW(),'%Y-%m-%d')");
假设time字段是varchar, 那么该对应的sql条件就是: WHERE time = 'DATE_FORMAT(NOW(),'%Y-%m-%d')'
这种情况Java把条件值当作字符串来处理了, 所以不是我们想要的, 这时就需要用new Original("");
正确写法: select.where("time", new Original("DATE_FORMAT(NOW(),'%Y-%m-%d')"));
```
