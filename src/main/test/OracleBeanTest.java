import cn.vonce.generate.bean.ColumnInfo;
import cn.vonce.sql.bean.Delete;
import cn.vonce.sql.bean.Insert;
import cn.vonce.sql.bean.Select;
import cn.vonce.sql.bean.Update;
import cn.vonce.sql.config.SqlBeanConfig;
import cn.vonce.sql.enumerate.DbType;
import cn.vonce.sql.enumerate.SqlLogic;
import cn.vonce.sql.helper.SqlHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class OracleBeanTest {

    public static void main(String[] agrs) {

        sql();

    }

    public static void sql(){
        long startTime = System.currentTimeMillis();

        // select
        Select select = new Select();
        select.column("id,name");
        select.setFrom(ColumnInfo.class);
        select.where("name", "jenny");
        select.where(SqlLogic.OR, "name", "jovi");
        select.where(SqlLogic.ANDBracket, "sex", 1);
        select.setPage(0, 10);
        System.out.println("---select---");
        SqlBeanConfig sqlBeanConfig = new SqlBeanConfig(DbType.ORACLE);
//        sqlBeanConfig.setOracleToUpperCase(false);
        select.setSqlBeanConfig(sqlBeanConfig);
        System.out.println(SqlHelper.buildSelectSql(select));

        // insert
        Insert insert = new Insert();
        List<ColumnInfo> list = new ArrayList<>();
        ColumnInfo insertColumnInfo = new ColumnInfo();
        insertColumnInfo.setColumnName("name1");
        insertColumnInfo.setComments("sex1");
        list.add(insertColumnInfo);
        ColumnInfo insertColumnInfo2 = new ColumnInfo();
        insertColumnInfo2.setColumnName("name2");
        insertColumnInfo2.setComments("sex2");
        list.add(insertColumnInfo2);
        insert.setInsertBean(list);
        System.out.println("---insert---");
        SqlBeanConfig sqlBeanConfig2 = new SqlBeanConfig(DbType.ORACLE);
//        sqlBeanConfig2.setOracleToUpperCase(false);
        insert.setSqlBeanConfig(sqlBeanConfig2);
        System.out.println(SqlHelper.buildInsertSql(insert));

        // update
        Update update = new Update();
        update.setUpdateTable("sys_user");
        update.setFilterFields("extra");
        ColumnInfo updateColumnInfo = new ColumnInfo();
        updateColumnInfo.setColumnName("name");
        updateColumnInfo.setComments("sex");
        updateColumnInfo.setExtra("123");
        update.setUpdateBean(updateColumnInfo);
        update.setUpdateNotNull(true);
//		update.setWhere("userId = 1111");
        update.where("id", 11);
        System.out.println("---update---");
        update.setSqlBeanConfig(new SqlBeanConfig(DbType.ORACLE));
        System.out.println(SqlHelper.buildUpdateSql(update));

        // delete
        Delete delete = new Delete();
        delete.where("id", 1, ">");
        delete.where("name", "jovi");
        delete.setDeleteBable("user");
        System.out.println("---delete---");
        delete.setSqlBeanConfig(new SqlBeanConfig(DbType.ORACLE));
        System.out.println(SqlHelper.buildDeleteSql(delete));


        float excTime = (float) (System.currentTimeMillis() - startTime) / 1000;
        System.out.println("耗时：" + excTime + "秒");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }

}
