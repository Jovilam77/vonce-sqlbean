package cn.vonce.sql.bean;

/**
 * @author Jovi
 * @email imjovi@qq.com
 * @date 2022/11/30 11:45
 */
public class TestJoin implements JoinOn {
    @Override
    public void on(Join join) {
        join.on("", "");
    }
}
