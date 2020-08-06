## 说明
```
本插件所有内置方法内部均通过SqlBean + SqlHelper来生成的Sql语句交给Mybatis或Spring JDBC执行，
所以以下例子使用方式同样适用于内置的方法，传递给内置方法的SqlBean可以不设置Sql语句的table，
因为在内部处理的时候发现table不存在的话会自动查找设置，Select语句连表查询的话仅支持自动补充主表的table。

```
## SqlHelper 生成Sql语句
```java
   String selectSql =  SqlHelper.buildSelectSql(select);
   String insertSql =  SqlHelper.buildInsertSql(insert);
   String updateSql =  SqlHelper.buildUpdateSql(update);
   String deleteSql =  SqlHelper.buildDeleteSql(delete);
```

## SqlBeanConfig
```java
    //在项目启动时会自动配置为你配置好SqlBeanConfig，
	//所以在项目中如需生成Sql语句可通过自动注入即可。
	
	@Autowired
    private SqlBeanConfig sqlBeanConfig;
```

## SqlBean - Select
```java
    private static void select1(SqlBeanConfig sqlBeanConfig) {
        Select select = new Select();
        select.setSqlBeanConfig(sqlBeanConfig);
        select.setColumn(SqlEssay._all);
        select.column(SqlUser.headPortrait, "头像");
        select.column(SqlUser.nickname, "昵称");
        select.setTable(SqlEssay._tableName);
        select.join(JoinType.INNER_JOIN, SqlUser._tableAlias, SqlUser.id.name(), SqlEssay.userId.name());
        select.where(SqlEssay.userId, "1111");
        //value 直接输入字符串 会当作字符串处理，sql中会带''，如果希望不被做处理则使用Original
        select.where("DATE_FORMAT( " + SqlEssay.creationTime + ", '%Y-%m-%d' )", new Original("DATE_FORMAT( '2018-01-19 20:24:19', '%Y-%m-%d' ) "), SqlOperator.EQUAL_TO);
        select.orderBy(SqlEssay.id, SqlSort.DESC);
        System.out.println(SqlHelper.buildSelectSql(select));
    }

    private static void select2(SqlBeanConfig sqlBeanConfig) {
        Select select2 = new Select();
        select2.setSqlBeanConfig(sqlBeanConfig);
        select2.column(SqlEssay.id, "序号")
                .column(SqlEssay.content, "文章内容")
                .column(SqlEssay.creationTime, "创建时间")
                .column(SqlUser.nickname, "用户昵称");
        select2.setTable(SqlEssay._tableName);
        select2.join(JoinType.INNER_JOIN, SqlUser._tableAlias, SqlUser.id.name(), SqlEssay.userId.name());
        select2.where("date_format(" + SqlEssay.creationTime + ",'%y%m%m ')", "2020-01-01 00:00:00", SqlOperator.GREATER_THAN);
        select2.wAND(SqlUser.nickname, "vicky", SqlOperator.EQUAL_TO);
        System.out.println(SqlHelper.buildSelectSql(select2));
    }

    private static void select3(SqlBeanConfig sqlBeanConfig) {
        Select select3 = new Select();
        select3.setSqlBeanConfig(sqlBeanConfig);
        select3.column(SqlEssay._all, "count")
                .column(SqlEssay.categoryId);
        select3.setTable(SqlEssay._tableName);
        select3.groupBy(SqlEssay.categoryId);
        select3.having("count", 5, SqlOperator.GREATER_THAN);
        System.out.println(SqlHelper.buildSelectSql(select3));
    }

    private static void select4(SqlBeanConfig sqlBeanConfig) {
        Select select4 = new Select();
        select4.setSqlBeanConfig(sqlBeanConfig);
        select4.setColumn(SqlUser._all);
        select4.setTable(SqlUser._tableName);
        Integer[] between = {2, 6};
//        List<Integer> between = new ArrayList<>();
//        between.add(2);
//        between.add(6);
        Integer[] gender = {0, 1};
        select4.where(SqlUser.id, between, SqlOperator.BETWEEN)
                .wANDBracket(SqlUser.nickname, "vicky", SqlOperator.EQUAL_TO)
                .wOR(SqlUser.gender, gender, SqlOperator.IN);
        System.out.println(SqlHelper.buildSelectSql(select4));
    }
```

## SqlBean - Insert
```java
    private static void insert1(SqlBeanConfig sqlBeanConfig) {
        Insert insert = new Insert();
        insert.setSqlBeanConfig(sqlBeanConfig);
        User user = new User();
        user.setId("10000");
        user.setUsername("10000");
        user.setNickname("麻花疼");
        user.setHeadPortrait("logo.png");
        user.setGender(0);
        insert.setInsertBean(user);
        System.out.println("---insert1---");
        System.out.println(SqlHelper.buildInsertSql(insert));
    }

    private static void insert2(SqlBeanConfig sqlBeanConfig) {
        Insert insert = new Insert();
        insert.setSqlBeanConfig(sqlBeanConfig);
        List<User> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            User user = new User();
            user.setId("id" + i);
            user.setUsername("username" + i);
            user.setNickname("nickname" + i);
            user.setHeadPortrait("logo.png");
            user.setGender(i % 2);
            list.add(user);
        }
        insert.setInsertBean(list);
        System.out.println(SqlHelper.buildInsertSql(insert));
    }
```

## SqlBean - Update
```java
    private static void update(SqlBeanConfig sqlBeanConfig) {
        User user = new User();
        user.setHeadPortrait("logo.png");
        user.setUsername("123");
        user.setGender(1);
        Update update = new Update();
        update.setSqlBeanConfig(sqlBeanConfig);
        update.setTable(User.class);
        update.setFilterFields("username");//java字段名
        update.setUpdateBean(user);
        update.setUpdateNotNull(true);
        update.where(SqlUser.id, 0, SqlOperator.GREATER_THAN);
        update.wAND(SqlUser.id, 10, SqlOperator.LESS_THAN);
        System.out.println(SqlHelper.buildUpdateSql(update));
    }
```

## SqlBean - Delete
```java
    private static void delete(SqlBeanConfig sqlBeanConfig) {
        Delete delete = new Delete();
        delete.setSqlBeanConfig(sqlBeanConfig);
        delete.where(SqlUser.id, 1, SqlOperator.GREATER_THAN);
        delete.wOR(SqlUser.nickname, "jovi");
        delete.setTable(User.class);
        System.out.println(SqlHelper.buildDeleteSql(delete));
    }
```