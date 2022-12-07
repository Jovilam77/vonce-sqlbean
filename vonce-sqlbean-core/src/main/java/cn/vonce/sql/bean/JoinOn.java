package cn.vonce.sql.bean;

/**
 * @author Jovi
 * @email imjovi@qq.com
 * @date 2022/11/30 11:44
 */
public interface JoinOn {

    Condition<Select> on(Join join);

}
