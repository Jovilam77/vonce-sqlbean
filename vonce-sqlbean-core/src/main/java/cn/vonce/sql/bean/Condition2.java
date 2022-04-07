package cn.vonce.sql.bean;

import cn.vonce.sql.helper.Wrapper;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.ListMultimap;

import java.util.List;

/**
 * where条件
 *
 * @author Jovi
 * @version 1.0
 * @email 766255988@qq.com
 * @date 2020年3月1日上午10:00:10
 */
public class Condition2 extends Common {

    private String where = "";//条件
    private Object[] agrs = null;
    private ListMultimap<String, ConditionInfo> whereMap = LinkedListMultimap.create();//where条件包含的逻辑
    private Wrapper whereWrapper = new Wrapper();
    SimpleCondition conditionTemp = new SimpleCondition(null);

    public SimpleCondition where() {
        return conditionTemp;
    }

    public static void main(String[] args) {
        Condition2 condition2 = new Condition2();
        //id = 1 and name = jovi or name = vicky and id in (1,2)
        //id = 1 and name = jovi or name = vicky and id in (1,2)
        condition2.where().eq("id", 1).and().eq("name", "jovi").or().eq("name", "vicky").and().in("age", new Integer[]{1, 2});

        System.out.println(condition2.conditionTemp.getSqlLogic());
        System.out.println(condition2.conditionTemp.getDataList().size());
        List<ConditionData> list = condition2.conditionTemp.getDataList();
        for (ConditionData conditionData : list) {
            System.out.println(conditionData.getSqlLogic() + " : " + conditionData.getItem());
        }


//        Select select = new Select().column("id").column("name").where().eq("id", "name").back();
//        Delete delete = new Delete().where().eq("id", 1).back();
//        Update<String> update = new Update<String>().where().eq("id", 1).back();
    }

}
