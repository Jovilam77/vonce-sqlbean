## 表结构操作
#### 方法和示例
###### 1：删除表结构
```java
void dropTable();
```
```java
public void dropTable() {
	sqlBeanService.getTableService().dropTable();
}
```
###### 2：创建表结构
```java
void createTable();
```
```java
public void createTable() {
	sqlBeanService.getTableService().createTable();
}
```
###### 3：删除并创建表结构
```java
void dropAndCreateTable();
```
```java
public void dropAndCreateTable() {
	sqlBeanService.getTableService().dropAndCreateTable();
}
```
###### 4：获取表名列表
```java
List<String> getTableList();
```
```java
public void getTableList() {
	List<String> list = sqlBeanService.getTableService().createTable();
}
```
