package cn.vonce.sql.enumerate;

/**
 * NORMAL：自定义的id
 * AUTO：使用数据库的自增id
 * UUID：Universally Unique Identifier，即通用唯一识别码（长度32位无序）
 * ULID：全称是Universally Unique Lexicographically Sortable Identifier，直译过来就是通用唯一按字典排序的标识符（长度26位有序）
 * SNOWFLAKE_ID_18：长度18位的唯一有序雪花id（64bits JavaScript会丢失精度，只能当作String类型处理）
 * SNOWFLAKE_ID_16：长度16位的唯一有序雪花id（53bits JavaScript能正常处理）
 *
 * @author Jovi
 * @version 1.0
 * @email imjovi@qq.com
 * @date 2020/2/28 9:15
 */
public enum IdType {

    NORMAL, AUTO, UUID, ULID, SNOWFLAKE_ID_16, SNOWFLAKE_ID_18

}
