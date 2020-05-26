## 条件操作符
```java
SqlOperator.IN = "IN"
SqlOperator.NOT_IN = "NOT IN"
SqlOperator.LIKE = "LIKE"
SqlOperator.NOT_LIKE = "NOT LIKE"
SqlOperator.SqlOperator.BETWEEN = "BETWEEN"
SqlOperator.GREATER_THAN = ">"
SqlOperator.GREAT_THAN_OR_EQUAL_TO = ">="
SqlOperator.LESS_THAN = "<"
SqlOperator.LESS_THAN_OR_EQUAL_TO = "<="
SqlOperator.EQUAL_TO = "="
SqlOperator.NOT_EQUAL_TO = "!="
```

## 条件逻辑
```java
SqlLogic.OR = select.wOR("字段","值");//或者
SqlLogic.AND = select.wAND("字段","值");//并且
SqlLogic.ORBracket = select.wORBracket("字段","值");//或者的前面加个括号（如果需要优先运算）
SqlLogic.ANDBracket = select.wANDBracket("字段","值");//并且的前面加个括号（如果需要优先运算）
```
#### 示例
###### 正常运算：WHERE id = 1 or id = 2
```java
Select select = new Select();
select.where("id", 1); //不写操作符参数，则默认为SqlOperator.EQUAL_TO
select.where(SqlLogic.OR, "id", 2); //不写操作符参数，则默认为SqlOperator.EQUAL_TO
//等同于
//Select select = new Select();
//select.where("id", 1); //不写操作符参数，则默认为SqlOperator.EQUAL_TO
//select.wOR("id", 2); //不写操作符参数，则默认为SqlOperator.EQUAL_TO
```
###### 需要优先运算：WHERE (id > 1 AND id < 10) OR type = '军事'
```java
Select select = new Select();
select.where("id", 1, SqlOperator.GREATER_THAN);
select.where(SqlLogic.AND, "id", 10, SqlOperator.LESS_THAN);
select.where(SqlLogic.ORBracket, "type", "军事"); //不写操作符参数，则默认为SqlOperator.EQUAL_TO
//等同于
//Select select = new Select();
//select.where("id", 1, SqlOperator.GREATER_THAN);
//select.wAND("id", 10, SqlOperator.LESS_THAN);
//select.wORBracket("type", "军事"); //不写操作符参数，则默认为SqlOperator.EQUAL_TO
```

## Original
```
在where条件中，有些条件的值可能等于某个字段或者等于某个函数，这个时候传入的值就不能对它进行处理。
比如说Java中select.where("time", "DATE_FORMAT(NOW(),'%Y-%m-%d')");假设time字段是varchar
那么该对应的sql条件就是：WHERE time = 'DATE_FORMAT(NOW(),'%Y-%m-%d')'
这种情况Java把条件值当作字符串来处理了，所以不是我们想要的，这时就需要用new Original("");
正确写法：select.where("time", new Original("DATE_FORMAT(NOW(),'%Y-%m-%d')"));
```
