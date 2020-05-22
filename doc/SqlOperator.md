## 条件操作
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
###### 示例
```java
Select select = new Select();
select.where("id", 1, SqlOperator.GREATER_THAN);
select.wAND("id", 10, SqlOperator.LESS_THAN);
select.wORBracket("type", "军事");
```
