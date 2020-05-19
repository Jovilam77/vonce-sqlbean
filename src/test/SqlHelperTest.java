
import cn.vonce.sql.bean.*;
import cn.vonce.sql.config.SqlBeanConfig;
import cn.vonce.sql.enumerate.*;
import cn.vonce.sql.helper.SqlHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SqlHelperTest {

    public static void main(String[] args) {
        SqlBeanConfig sqlBeanConfig = new SqlBeanConfig();
        sqlBeanConfig.setDbType(DbType.MySQL);
        sqlBeanConfig.setToUpperCase(false);

        long startTime = System.currentTimeMillis();

        // select
        Select select = new Select();
        select.setSqlBeanConfig(sqlBeanConfig);
        select.setColumn("essay_comment.*", "user.headPortrait", "user.nickname");
        select.setTable("`essay_comment`");
        select.join("user", "user.id", "essay_comment.userId");
        select.where("#essay_comment.essayId", "22essay_comment.essayId", SqlOperator.GREATER_THAN);
        select.where(SqlLogic.AND, "", "", "essay_comment.essayId", 1, SqlOperator.LESS_THAN).where(SqlLogic.AND, "", "essay_comment.userId", "1111")
                .where(SqlLogic.ORBracket, "", "user.nickname", "jovi");
        select.groupBy("id");
        select.having("sum(price)", 2999, SqlOperator.GREATER_THAN);
        select.orderBy("user.id", SqlSort.DESC).orderBy("essay_comment.userId", SqlSort.ASC);
        System.out.println("---select---");
        System.out.println(SqlHelper.buildSelectSql(select));

        // select2
        Select select2 = new Select();
        select2.setSqlBeanConfig(sqlBeanConfig);
        select2.column("*");
        select2.setTable("user");
        select2.join(JoinType.INNER_JOIN, "userLog", "id", "userId");
        select2.where("date_format(date,'%y%m%m ')", "jovi");
        select2.where(SqlLogic.OR, "", "name", "vicky");
        select2.where(SqlLogic.ANDBracket, "", "sex", 1);
        System.out.println("---select2---");
        System.out.println(SqlHelper.buildSelectSql(select2));

        // select3
        Select select3 = new Select();
        select3.setSqlBeanConfig(sqlBeanConfig);
        select3.column("*");
        select3.setTable("user");
        // String[] between = { "1", "5" };
        List<Object> stringList = new ArrayList<>();
        stringList.add(2);
        stringList.add("6");
        select3.where("", "id", stringList, SqlOperator.BETWEEN);
        // MysqlBean3.where("name", "vicky");
        select3.where(SqlLogic.AND, "", "", "sex", stringList, SqlOperator.IN);
        System.out.println("---select3---");// where id between 1 and 2
        System.out.println(SqlHelper.buildSelectSql(select3));

        // select4
        Select select4 = new Select();
        select4.setSqlBeanConfig(sqlBeanConfig);
        select4.column("*");
        select4.setTable("user");
        // String[] between = { "1", "5" };
        List<Object> stringList2 = new ArrayList<>();
        stringList2.add(2);
        stringList2.add("6");
        select4.where("", "id", stringList2, SqlOperator.BETWEEN);
        select4.wANDBracket("", "", "name", "vicky", SqlOperator.EQUAL_TO);
        select4.wOR("sex", stringList2, SqlOperator.IN);
        System.out.println("---select4---");// where id between 1 and 2
        System.out.println(SqlHelper.buildSelectSql(select4));

        // update
        Update update = new Update();
        update.setSqlBeanConfig(sqlBeanConfig);
        update.setTable("sys_user");
        update.setFilterFields("extra");
        Join join = new Join();
        join.setTableName("name");
        join.setTableKeyword("sex");
        join.setMainKeyword("123");
        update.setUpdateBean(join);
        update.setUpdateNotNull(true);
//		update.setWhere("userId = 1111");
        update.where("id", 11);
        System.out.println("---update---");
        System.out.println(SqlHelper.buildUpdateSql(update));

        // update 2
        Update update2 = new Update();
        update2.setSqlBeanConfig(sqlBeanConfig);
        update2.setTable("sys_user");
        update2.setUpdateBean(join);
//		update2.where("id",11);
        System.out.println("---update 2---");
        System.out.println(SqlHelper.buildUpdateSql(update2));


        // insert
        Insert insert = new Insert();
        insert.setSqlBeanConfig(sqlBeanConfig);
        List<Join> list = new ArrayList<>();
        Join insertJoin = new Join();
        insertJoin.setMainKeyword("name1");
        insertJoin.setTableName("sex1");
        list.add(insertJoin);
        Join insertJoin2 = new Join();
        insertJoin2.setMainKeyword("name2");
        insertJoin2.setTableName("sex2");
        list.add(insertJoin2);
        insert.setInsertBean(list);
        System.out.println("---insert---");
        System.out.println(SqlHelper.buildInsertSql(insert));

        // delete
        Delete delete = new Delete();
        delete.setSqlBeanConfig(sqlBeanConfig);
        delete.where("", "id", 1, SqlOperator.GREATER_THAN);
        delete.where("name", "jovi");
        delete.setTable("user");
        System.out.println("---delete---");
        System.out.println(SqlHelper.buildDeleteSql(delete));

        float excTime = (float) (System.currentTimeMillis() - startTime) / 1000;
        System.out.println("耗时：" + excTime + "秒");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Object o = new Date();
        System.out.println(sdf.format(o));
    }

}
