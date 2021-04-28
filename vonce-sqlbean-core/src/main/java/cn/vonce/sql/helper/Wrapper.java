package cn.vonce.sql.helper;

import java.util.ArrayList;
import java.util.List;

/**
 * 条件包装器
 *
 * @author Jovi
 * @version 1.0
 * @email 766255988@qq.com
 * @date 2021年4月28日下午5:49:00
 */
public class Wrapper {

    private List<Object> condList = new ArrayList<>();

    public static Wrapper cond(Cond cond) {
        Wrapper wrapper = new Wrapper();
        wrapper.condList.add(cond);
        return wrapper;
    }

    public Wrapper and(Wrapper wrapper) {
        condList.add(wrapper);
        return this;
    }

    public Wrapper and(Cond cond) {
        condList.add(cond);
        return this;
    }

    public Wrapper or(Wrapper wrapper) {
        condList.add(wrapper);
        return this;
    }

    public Wrapper or(Cond cond) {
        condList.add(cond);
        return this;
    }

    public static void main(String[] args) {

        //WHERE type = 1 AND time between '2018-01-01' AND '2021-05-01'
        Wrapper.cond(Cond.lt("type", "1")).and(Cond.between("time", "2018-01-01", "2021-05-01"));

        //WHERE time > '2018-01-01' AND (type = 1 OR type = 2)
        Wrapper.cond(Cond.gt("time","2018-01-01"))
                .and(Wrapper.cond(Cond.eq("type", 1)).or(Cond.eq("type", 2)));
    }

}
