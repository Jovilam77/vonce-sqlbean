#### 说明
```
本插件所有内置方法内部均通过SqlBean + SqlHelper来生成的Sql语句交给Mybatis或Spring JDBC执行，
所以以下例子使用方式同样适用于内置的方法，传递给内置方法的SqlBean可以不设置Sql语句的table，
内部处理的时候发现table不存在的话会自动配置，Select语句连表查询的话仅支持自动补充主表的table。

```
#### 一. SqlHelper 生成Sql语句
```java
   String selectSql =  SqlHelper.buildSelectSql(select);
   String insertSql =  SqlHelper.buildInsertSql(insert);
   String updateSql =  SqlHelper.buildUpdateSql(update);
   String deleteSql =  SqlHelper.buildDeleteSql(delete);
```

#### 二. SqlBeanDB
```java
    SqlBeanDB sqlBeanDB = new SqlBeanDB();
    sqlBeanDB.setDbType(DbType.MySQL);
    sqlBeanDB.setSqlBeanConfig(new SqlBeanConfig());
```

#### 三. Select查询对象
```java
    private static void select1(SqlBeanDB sqlBeanDB) {
        Select select = new Select();
        select.setSqlBeanDB(sqlBeanDB);
        select.setColumn($Essay._all);
        select.column($User.headPortrait);
        select.column($User.nickname);
        select.setTable($Essay._tableName);
        select.join(JoinType.INNER_JOIN, $User._tableAlias, $User.id, $Essay.userId);
        select.where($Essay.userId, "1111");
        //value 直接输入字符串 会当作字符串处理，sql中会带''，如果希望不被做处理则使用Original
        select.where("DATE_FORMAT( " + $Essay.creationTime + ", '%Y-%m-%d' )", new Original("DATE_FORMAT( '2018-01-19 20:24:19', '%Y-%m-%d' ) "), SqlOperator.EQUAL_TO);
        select.orderBy($Essay.id, SqlSort.DESC);
        System.out.println(SqlHelper.buildSelectSql(select));
    }

    private static void select2(SqlBeanDB sqlBeanDB) {
        Select select2 = new Select();
        select2.setSqlBeanDB(sqlBeanDB);
        select2.column($Essay.id)
                .column($Essay.content)
                .column($Essay.creationTime)
                .column($User.nickname);
        select2.setTable($Essay._tableName);
        select2.join(JoinType.INNER_JOIN, $User._tableAlias, $User.id, $Essay.userId);
        select2.where("date_format(" + $Essay.creationTime + ",'%y%m%m ')", "2020-01-01 00:00:00", SqlOperator.GREATER_THAN);
        select2.wAND($User.nickname, "vicky", SqlOperator.EQUAL_TO);
        System.out.println(SqlHelper.buildSelectSql(select2));
    }

    private static void select3(SqlBeanDB sqlBeanDB) {
        Select select3 = new Select();
        select3.setSqlBeanDB(sqlBeanDB);
        select3.column($Essay._all, "count")
                .column($Essay.categoryId);
        select3.setTable($Essay._tableName);
        select3.groupBy($Essay.categoryId);
        select3.having("count", 5, SqlOperator.GREATER_THAN);
        System.out.println(SqlHelper.buildSelectSql(select3));
    }

    private static void select4(SqlBeanDB sqlBeanDB) {
        Select select4 = new Select();
        select4.setSqlBeanDB(sqlBeanDB);
        select4.setColumn($User._all);
        select4.setTable($User._tableName);
        Integer[] between = {2, 6};
//        List<Integer> between = new ArrayList<>();
//        between.add(2);
//        between.add(6);
        Integer[] gender = {0, 1};
        select4.where($User.id, between, SqlOperator.BETWEEN)
                .wANDBracket($User.nickname, "vicky", SqlOperator.EQUAL_TO)
                .wOR($User.gender, gender, SqlOperator.IN);
        System.out.println(SqlHelper.buildSelectSql(select4));
    }
```

#### 四. Insert插入对象
```java
    private static void insert1(SqlBeanDB sqlBeanDB) {
        Insert insert = new Insert();
        insert.setSqlBeanDB(sqlBeanDB);
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

    private static void insert2(SqlBeanDB sqlBeanDB) {
        Insert insert = new Insert();
        insert.setSqlBeanDB(sqlBeanDB);
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

#### 五. Update更新对象
```java
    private static void update(SqlBeanDB sqlBeanDB) {
        User user = new User();
        user.setHeadPortrait("logo.png");
        user.setUsername("123");
        user.setGender(1);
        Update update = new Update();
        update.setSqlBeanDB(sqlBeanDB);
        update.setTable(User.class);
        update.setFilterFields("username");//java字段名
        update.setUpdateBean(user);
        update.setUpdateNotNull(true);
        update.where($User.id, 0, SqlOperator.GREATER_THAN);
        update.wAND($User.id, 10, SqlOperator.LESS_THAN);
        System.out.println(SqlHelper.buildUpdateSql(update));
    }
```

#### 六. Delete删除对象
```java
    private static void delete(SqlBeanDB sqlBeanDB) {
        Delete delete = new Delete();
        delete.setSqlBeanDB(sqlBeanDB);
        delete.where($User.id, 1, SqlOperator.GREATER_THAN);
        delete.wOR($User.nickname, "jovi");
        delete.setTable(User.class);
        System.out.println(SqlHelper.buildDeleteSql(delete));
    }
```
