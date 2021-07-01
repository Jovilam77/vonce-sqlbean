package cn.vonce.sql.enumerate;

/**
 * NORMAL：常规的，手写id
 * AUTO：自增长id
 * UUID：长度32位的uuid
 * SNOWFLAKE_ID_18：长度18位的唯一有序id（64bits JavaScript会丢失精度，只能当作String类型处理）
 * SNOWFLAKE_ID_16：长度16位的唯一有序id（53bits JavaScript能正常处理）
 *
 * @author Jovi
 * @version 1.0
 * @email imjovi@qq.com
 * @date 2020/2/28 9:15
 */
public enum IdType {

    NORMAL, AUTO, UUID, SNOWFLAKE_ID_16, SNOWFLAKE_ID_18

}
