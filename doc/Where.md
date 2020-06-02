## 条件操作符 枚举解释
```java
SqlOperator.IN = "IN"                       //包含
SqlOperator.NOT_IN = "NOT IN"               //不包含
SqlOperator.LIKE = "LIKE"                   //相似
SqlOperator.NOT_LIKE = "NOT LIKE"           //不相似
SqlOperator.SqlOperator.BETWEEN = "BETWEEN" //介于
SqlOperator.GREATER_THAN = ">"              //大于
SqlOperator.GREAT_THAN_OR_EQUAL_TO = ">="   //大于等于
SqlOperator.LESS_THAN = "<"                 //小于
SqlOperator.LESS_THAN_OR_EQUAL_TO = "<="    //小于等于
SqlOperator.EQUAL_TO = "="                  //等于
SqlOperator.NOT_EQUAL_TO = "!="             //不等于
```

## 条件逻辑 枚举解释
```java
SqlLogic.OR = select.wOR("字段","值");                  //或者
SqlLogic.AND = select.wAND("字段","值");                //并且
SqlLogic.ORBracket = select.wORBracket("字段","值");    //或者的前面加个括号（如果需要优先运算）
SqlLogic.ANDBracket = select.wANDBracket("字段","值");  //并且的前面加个括号（如果需要优先运算）
```
## Where条件
###### 正常运算：WHERE id = 1 or id = 2
```java
Select select = new Select();
select.where("id", 1);              //不写操作符参数，则默认为SqlOperator.EQUAL_TO
select.where(SqlLogic.OR, "id", 2); //不写操作符参数，则默认为SqlOperator.EQUAL_TO
//等同于
//Select select = new Select();
//select.where("id", 1);            //不写操作符参数，则默认为SqlOperator.EQUAL_TO
//select.wOR("id", 2);              //不写操作符参数，则默认为SqlOperator.EQUAL_TO
```
###### 需要优先运算：WHERE (id > 1 AND id < 10) OR type = '军事'
```java
Select select = new Select();
select.where("id", 1, SqlOperator.GREATER_THAN);
select.where(SqlLogic.AND, "id", 10, SqlOperator.LESS_THAN);
select.where(SqlLogic.ORBracket, "type", "军事");            //不写操作符参数，则默认为SqlOperator.EQUAL_TO
//等同于
//Select select = new Select();
//select.where("id", 1, SqlOperator.GREATER_THAN);
//select.wAND("id", 10, SqlOperator.LESS_THAN);
//select.wORBracket("type", "军事");                        //不写操作符参数，则默认为SqlOperator.EQUAL_TO
```

## 原始值Original
```
在where条件中，有些条件的值可能等于某个字段或者等于某个函数，这个时候传入的值就不能对它进行处理。
比如说Java中：select.where("time", "DATE_FORMAT(NOW(),'%Y-%m-%d')");
假设time字段是varchar，那么该对应的sql条件就是：WHERE time = 'DATE_FORMAT(NOW(),'%Y-%m-%d')'
这种情况Java把条件值当作字符串来处理了，所以不是我们想要的，这时就需要用new Original("");
正确写法：select.where("time", new Original("DATE_FORMAT(NOW(),'%Y-%m-%d')"));
```

## setWhere()、xxxByCondition()与占位符
```
除了以xxx.where()方式设置where条件以外还支持xxx.setWhere()方法设置where条件，
该方式设置where条件以手写sql的方式进行，不支持where以外的语法，如group、order、limit等，
占位符“&”代表字段名称，占位符“?”代表字段值。

例如：setWhere("status = 7 AND type = 1");
     //那么使用占位符的方式即为
	 setWhere("status = ? AND type = ?", 7, 1); //这种方式sql的列字段仍自己手写
	 setWhere("& > ? AND & = ?", SqlUser.status, 10, SqlUser.type, 1);//建议采用此方式
	 
	 //此例子同样适用SqlBeanService接口中内置的xxxByCondition()方法
	 //SqlUser类 maven编译后自动生成，如不理解请查看注解文档

```