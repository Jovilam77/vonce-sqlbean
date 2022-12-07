package cn.vonce.sql.bean;

/**
 * @author Jovi
 * @email imjovi@qq.com
 * @date 2022/11/30 11:45
 */
public class TestJoin implements JoinOn {
    @Override
    public Condition<Select> on(Join join) {
        return join.on("", "");
    }
}
