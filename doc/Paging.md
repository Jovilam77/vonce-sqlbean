#### 说明
###### 分页属性，请求时将需要的参数拼上传给后台，new PageHelper<>(request)会自动获取，pagenum从0开始做为第1页
```java
//当前页
private Integer pagenum;
//每页数量
private Integer pagesize;
//排序字段
private String sortdatafield;
//排序方式
private String sortorder;
//时间戳
private String timestamp;
```
#### 一. 返回分页数据包含页数总数信息
```java
@RequestMapping(value = "getList", method = RequestMethod.GET)
@ResponseBody
public ResultData<Order> getList(Integer pageNum, Integer pageSize) {
    Select select = new Select();// 查询对象（具体用法去看Select）
    select.where(Wrapper.where(Cond.gt(Essay::getId, 1)).and(Cond.lt(Essay::getId, 10)).and(Wrapper.where(Cond.eq(Essay::getUserId, 111))));
    PageHelper<Order> pageHelper = new PageHelper<>(pageNum,pageNum);//分页助手，如果你要联表查询请将泛型对象改为你的包装对象（具体请看联表查询注解文档）
    pageHelper.paging(select, essayService);//分页查询
    //如果你要联表查询请使用下面这个（具体请看联表查询注解那里）
    //pageHelper.paging(EssayUnion.class, select, pageHelper);
    return orderService.paging(select,pageHelper);
}
```
#### 二. 仅返回分页数据
```java
@RequestMapping(value = "getList", method = RequestMethod.GET)
@ResponseBody
public List<Order> getList(Integer pageNum, Integer pageSize) {
    PageHelper<Order> pageHelper = new PageHelper<>(pageNum, pageSize);
    return orderService.select(pageHelper.getPaging());
    //return orderService.selectBy(pageHelper.getPaging(), Wrapper.where(Cond.gt(Order::getId, 1)));
}
```
