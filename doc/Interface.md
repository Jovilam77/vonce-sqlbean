#### Service接口和实现类
##### 一. 增删查改接口 + 表操作接口
```java
public interface EssayService extends SqlBeanService<Essay, Long>, TableService {

}
```
###### 在Service层除了可以实现SqlBeanService接口以外，如果你的业务只需要单独查询 或 插入 或 删除 或 更新，那么你还可以使用以下接口

##### 二. 仅查询接口[SelectService 查看文档](Select.md "SelectService")
```java
public interface EssayService extends SelectService<Essay, Long> {

}
```
##### 三. 仅插入接口[InsertService 查看文档](Insert.md "InsertService")
```java
public interface EssayService extends InsertService<Essay, Long> {

}
```
##### 四. 仅删除接口[DeleteService 查看文档](Delete.md "DeleteService")
```java
public interface EssayService extends DeleteService {

}
```
##### 五. 仅更新接口[UpdateService 查看文档](Update.md "UpdateService")
```java
public interface EssayService extends UpdateService<Essay, Long> {

}
```
##### 六. 仅表操作接口[TableService 查看文档](DbManage.md "TableService")
```java
public interface EssayService extends TableService {

}
```

##### 七. Mybatis实现类
```java
@Service
public class EssayServiceImpl extends MybatisSqlBeanServiceImpl<Essay, Long> implements EssayService {

}
```
##### 八. Spring JDBC实现类
```java
@Service
public class EssayServiceImpl extends SpringJdbcSqlBeanServiceImpl<Essay, Long> implements EssayService {

}
```
