package cn.vonce.sql.test;

import cn.vonce.sql.bean.*;
import cn.vonce.sql.config.SqlBeanConfig;
import cn.vonce.sql.enumerate.*;
import cn.vonce.sql.helper.SqlHelper;
import cn.vonce.sql.model.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * sql语句生成测试
 * 测试中写的sql，可能某些字段的条件写的不符合现实逻辑，不要在意，只是为了测试语法支持。
 * 实例仅供参考，可自由发挥写出符合自己业务需要的sql语句
 */
public class SqlHelperTest {

    public static void main(String[] args) {
        SqlBeanConfig sqlBeanConfig = new SqlBeanConfig();
        sqlBeanConfig.setDbType(DbType.MySQL);
        sqlBeanConfig.setToUpperCase(false);

        long startTime = System.currentTimeMillis();

        // select1
        select1(sqlBeanConfig);

        // select2
        select2(sqlBeanConfig);

        // select3
        select3(sqlBeanConfig);

        // select4
        select4(sqlBeanConfig);

        // insert1
        insert1(sqlBeanConfig);

        // insert2
        insert2(sqlBeanConfig);

        // update
        update(sqlBeanConfig);

        // delete
        delete(sqlBeanConfig);

        float excTime = (float) (System.currentTimeMillis() - startTime) / 1000;
        System.out.println("耗时：" + excTime + "秒");
    }

    /**
     * 查询1
     *
     * @param sqlBeanConfig
     */
    private static void select1(SqlBeanConfig sqlBeanConfig) {
        Select select = new Select();
        select.setSqlBeanConfig(sqlBeanConfig);
        select.setColumn("d_essay.*");
        select.column("", "user", "headPortrait", "头像");
        select.column("", "user", "nickname", "昵称");
        select.setTable("d_essay");
        select.join(JoinType.INNER_JOIN, "", "d_user", "user", "id", "userId");
        select.where(SqlLogic.AND, "", "userId", "1111");
        //value 直接输入字符串 会当作字符串处理，sql中会带''，如果希望不被做处理则使用Original
        select.where("DATE_FORMAT( d_essay.creationTime, '%Y-%m-%d' )", new Original("DATE_FORMAT( '2018-01-19 20:24:19', '%Y-%m-%d' ) "), SqlOperator.EQUAL_TO);
        select.orderBy("", "d_essay", "id", SqlSort.DESC);
        System.out.println("---select---");
        System.out.println(SqlHelper.buildSelectSql(select));
    }

    /**
     * 查询2
     *
     * @param sqlBeanConfig
     */
    private static void select2(SqlBeanConfig sqlBeanConfig) {
        Select select2 = new Select();
        select2.setSqlBeanConfig(sqlBeanConfig);
        select2.column("", "d_essay", "id", "序号")
                .column("content", "文章内容")
                .column("", "d_essay", "creationTime", "创建时间")
                .column("", "user", "nickname", "用户昵称");
        select2.setTable("d_essay");
        select2.join(JoinType.INNER_JOIN, "", "d_user", "user", "id", "userId");
        select2.where("date_format(d_essay.creationTime,'%y%m%m ')", "2020-01-01 00:00:00", SqlOperator.GREATER_THAN);
        select2.wAND("", "user", "nickname", "vicky", SqlOperator.EQUAL_TO);
        System.out.println("---select2---");
        System.out.println(SqlHelper.buildSelectSql(select2));
    }

    /**
     * 查询3
     *
     * @param sqlBeanConfig
     */
    private static void select3(SqlBeanConfig sqlBeanConfig) {
        Select select3 = new Select();
        select3.setSqlBeanConfig(sqlBeanConfig);
        select3.column("count(*)", "count")
                .column("categoryId");
        select3.setTable("d_essay");
        select3.groupBy("categoryId");
        select3.having("count", 5, SqlOperator.GREATER_THAN);
        System.out.println("---select3---");
        System.out.println(SqlHelper.buildSelectSql(select3));
        ;
    }

    /**
     * 查询4
     *
     * @param sqlBeanConfig
     */
    private static void select4(SqlBeanConfig sqlBeanConfig) {
        Select select4 = new Select();
        select4.setSqlBeanConfig(sqlBeanConfig);
        select4.setColumn("*");
        select4.setTable("d_user");
        Integer[] between = {2, 6};
//        List<Integer> between = new ArrayList<>();
//        between.add(2);
//        between.add(6);
        Integer[] gender = {0, 1};
        select4.where("", "id", between, SqlOperator.BETWEEN)
                .wANDBracket("", "", "nickname", "vicky", SqlOperator.EQUAL_TO)
                .wOR("gender", gender, SqlOperator.IN);
        System.out.println("---select4---");// where id between 1 and 2
        System.out.println(SqlHelper.buildSelectSql(select4));
    }

    /**
     * 插入1
     *
     * @param sqlBeanConfig
     */
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

    /**
     * 插入2
     *
     * @param sqlBeanConfig
     */
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
        System.out.println("---insert2---");
        System.out.println(SqlHelper.buildInsertSql(insert));
    }

    /**
     * 更新
     *
     * @param sqlBeanConfig
     */
    private static void update(SqlBeanConfig sqlBeanConfig) {
        User user = new User();
        user.setHeadPortrait("logo.png");
        user.setUsername("123");
        user.setGender(1);
        Update update = new Update();
        update.setSqlBeanConfig(sqlBeanConfig);
        update.setTable(User.class);
        update.setFilterFields("username");
        update.setUpdateBean(user);
        update.setUpdateNotNull(true);
        update.where("id", 0, SqlOperator.GREATER_THAN);
        update.wAND("id", 10, SqlOperator.LESS_THAN);
        System.out.println("---update---");
        System.out.println(SqlHelper.buildUpdateSql(update));
    }

    /**
     * 删除
     *
     * @param sqlBeanConfig
     */
    private static void delete(SqlBeanConfig sqlBeanConfig) {
        Delete delete = new Delete();
        delete.setSqlBeanConfig(sqlBeanConfig);
        delete.where("id", 1, SqlOperator.GREATER_THAN);
        delete.wOR("name", "jovi");
        delete.setTable(User.class);
        System.out.println("---delete---");
        System.out.println(SqlHelper.buildDeleteSql(delete));
    }

}
