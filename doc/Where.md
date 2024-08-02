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
Select select = new Select();
select.where("status = ? AND type = ?", 7, 1); //这种方式sql的列字段仍自己手写
select.where("& > ? AND & = ?", User$.status, 10, User$.type, 1);//建议采用此方式

//此例子同样适用SqlBeanService接口中内置的xxxBy()方法
//User$类 maven编译后自动生成, 如不理解请查看注解文档

```

#### 三. Where条件包装器（推荐使用）
```java
//SQL: where id = 1 and content = '222'
Select select = new Select();
select.where(
       Wrapper.where(Cond.gt(Essay::getId, 1)).
	           and(Cond.eq(Essay::getContent, "222"))
   );

//SQL（优先运算）: where content = '222' and (type = 1 or type = 2)
select.where(
       Wrapper.where(Cond.gt(Essay::getContent, "222")).
	           and(
			       Wrapper.where(Cond.gt(Essay::getType, 1)).
	                       or(Cond.eq(Essay::getType, 2))
				)
   );

```

#### 四. Sql函数
```java
//Sql函数可使用在查询字段中,也可使用在Where条件上.
//所有的Sql函数都在SqlFun类中，如：SqlFun.count(User::getId)
select = new Select();
select.column(SqlFun.count(User::getId), "count").column(SqlFun.avg(User::getAge));
select.where().gt(SqlFun.date_format(User::getCreateTime, "%Y-%m-%d"), "2024-06-24");
select.groupBy(User::getGender);
select.orderByDesc("count");
List<Map<String, Object>> mapList = userService.selectMapList(select);
```