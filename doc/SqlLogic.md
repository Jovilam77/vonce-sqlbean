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
select.where("id", 1);
select.where(SqlLogic.OR, "id", 2);
//等同于
//Select select = new Select();
//select.where("id", 1);
//select.wOR("id", 2);
```
###### 需要优先运算：WHERE (id > 1 AND id < 10) OR type = '军事'
```java
Select select = new Select();
select.where("id", 1, SqlOperator.GREATER_THAN);
select.where(SqlLogic.AND, "id", 10, SqlOperator.LESS_THAN);
select.where(SqlLogic.ORBracket, "type", "军事");
//等同于
//Select select = new Select();
//select.where("id", 1, SqlOperator.GREATER_THAN);
//select.wAND("id", 10, SqlOperator.LESS_THAN);
//select.wORBracket("type", "军事");
```