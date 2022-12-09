package cn.vonce.sql.define;

import cn.vonce.sql.bean.Column;

import java.util.Arrays;

/**
 * Sql函数
 *
 * @author Jovi
 * @email imjovi@qq.com
 * @date 2022/12/8 15:15
 */
public class SqlFun extends Column {

    private String funName;
    private Object[] values;

    private SqlFun(String funName, Object[] values) {
        this.funName = funName;
        this.values = values;
    }

    public String getFunName() {
        return funName;
    }

    public Object[] getValues() {
        return values;
    }

    @Override
    public String toString() {
        return "SqlFun{" + "funName" + funName + '\'' + ", values=" + Arrays.toString(values) + '}';
    }

    /**
     * 版本（mysql）
     *
     * @return
     */
    public static SqlFun version() {
        return new SqlFun("version", null);
    }

    /**
     * 当前数据库（mysql）
     *
     * @return
     */
    public static SqlFun database() {
        return new SqlFun("database", null);
    }

    /**
     * 当前用户（mysql）
     *
     * @return
     */
    public static SqlFun user() {
        return new SqlFun("user", null);
    }

    /**
     * 统计
     *
     * @param value
     * @return
     */
    public static SqlFun count(Object value) {
        return new SqlFun("count", new Object[]{value});
    }

    /**
     * 统计
     *
     * @param value
     * @return
     */
    public static SqlFun avg(Object value) {
        return new SqlFun("avg", new Object[]{value});
    }

    /**
     * 最大值
     *
     * @param value
     * @return
     */
    public static SqlFun max(Object value) {
        return new SqlFun("max", new Object[]{value});
    }

    /**
     * 最小值
     *
     * @param value
     * @return
     */
    public static SqlFun min(Object value) {
        return new SqlFun("min", new Object[]{value});
    }

    /**
     * 列总和
     *
     * @param value
     * @return
     */
    public static SqlFun sum(Object value) {
        return new SqlFun("sum", new Object[]{value});
    }

    /**
     * 当前系统时间，1997-06-03 19:23:12
     *
     * @return
     */
    public static SqlFun now() {
        return new SqlFun("now", null);
    }

    /**
     * 当前系统时间的日期，1997-06-03
     *
     * @return
     */
    public static SqlFun curdate() {
        return new SqlFun("curdate", null);
    }

    /**
     * 当前系统时间的时间，19:23:12
     *
     * @return
     */
    public static SqlFun curtime() {
        return new SqlFun("curtime", null);
    }

    /**
     * 年份，1997
     *
     * @param date 合法的日期
     * @return
     */
    public static SqlFun year(Object date) {
        return new SqlFun("year", new Object[]{date});
    }

    /**
     * 月份，6
     *
     * @param date 合法的日期
     * @return
     */
    public static SqlFun month(Object date) {
        return new SqlFun("month", new Object[]{date});
    }

    /**
     * 月份英文形式，June
     *
     * @param date 合法的日期
     * @return
     */
    public static SqlFun monthname(Object date) {
        return new SqlFun("monthname", new Object[]{date});
    }

    /**
     * 日， 3
     *
     * @param date
     * @return
     */
    public static SqlFun day(Object date) {
        return new SqlFun("day", new Object[]{date});
    }

    /**
     * 小时， 19
     *
     * @param date 合法的日期
     * @return
     */
    public static SqlFun hour(Object date) {
        return new SqlFun("hour", new Object[]{date});
    }

    /**
     * 分钟， 23
     *
     * @param date 合法的日期
     * @return
     */
    public static SqlFun minute(Object date) {
        return new SqlFun("minute", new Object[]{date});
    }

    /**
     * 秒， 12
     *
     * @param date 合法的日期
     * @return
     */
    public static SqlFun second(Object date) {
        return new SqlFun("second", new Object[]{date});
    }

    /**
     * 两个时间相差的天数
     *
     * @param date1 合法的日期
     * @param date2 合法的日期
     * @return
     */
    public static SqlFun datediff(Object date1, Object date2) {
        return new SqlFun("datediff", new Object[]{date1, date2});
    }

    /**
     * 日期类型格式化成指定格式的字符串
     *
     * @param date   合法的日期
     * @param format 规定日期/时间的输出格式
     * @return
     */
    public static SqlFun date_format(Object date, String format) {
        return new SqlFun("date_format", new Object[]{date, format});
    }

    /**
     * 字符串格式的时间格式转日期
     *
     * @param date   合法的日期
     * @param format 规定日期/时间的格式
     * @return
     */
    public static SqlFun str_to_date(Object date, String format) {
        return new SqlFun("str_to_date", new Object[]{date, format});
    }

    /**
     * 判断，类似于三目运算
     *
     * @param bool        布尔值
     * @param trueResult  true返回的结果
     * @param falseResult false返回到结果
     * @return
     */
    public static SqlFun iF(Object bool, Object trueResult, Object falseResult) {
        return new SqlFun("if", new Object[]{bool, trueResult, falseResult});
    }

    /**
     * 统计字符串的字节数（取决于编码方式，utf8汉字3字节，gbk汉字2字节）
     *
     * @param str 字符串
     * @return
     */
    public static SqlFun length(Object str) {
        return new SqlFun("length", new Object[]{str});
    }

    /**
     * 拼接字符
     *
     * @param str1 字符串1
     * @param str2 字符串2
     * @return
     */
    public static SqlFun concat(Object str1, Object str2) {
        return new SqlFun("concat", new Object[]{str1, str2});
    }

    /**
     * 切割字符，startIndex起始位置（mysql下标从1开始）
     *
     * @param str        字符串
     * @param startIndex startIndex起始位置
     * @return
     */
    public static SqlFun substring(Object str, int startIndex) {
        return new SqlFun("substring", new Object[]{str, startIndex});
    }

    /**
     * 切割字符，start起始位置（mysql下标从1开始），end结束位置，表示切割长度
     *
     * @param str        字符串
     * @param startIndex startIndex起始位置
     * @param endIndex   startIndex结束位置
     * @return
     */
    public static SqlFun substring(Object str, int startIndex, int endIndex) {
        return new SqlFun("substring", new Object[]{str, startIndex, endIndex});
    }

    /**
     * 返回str2在str1中首次出现的位置；如果没有找到，则返回0。不区分大小写
     *
     * @param str1 字符串1
     * @param str2 字符串2
     * @return
     */
    public static SqlFun instr(Object str1, Object str2) {
        return new SqlFun("instr", new Object[]{str1, str2});
    }

    /**
     * 字母变大写
     *
     * @param str 字符串
     * @return
     */
    public static SqlFun upper(Object str) {
        return new SqlFun("upper", new Object[]{str});
    }

    /**
     * 字母变小写
     *
     * @param str 字符串
     * @return
     */
    public static SqlFun lower(Object str) {
        return new SqlFun("lower", new Object[]{str});
    }

    /**
     * 其中str1是第一个字符串，length是结果字符串的长度，str2是一个填充字符串。如果str1的长度没有length那么长，则使用str2往左边填充；如果str1的长度大于length，则截断
     *
     * @param str1   字符串
     * @param length 字符串
     * @param str2   字符串
     * @return
     */
    public static SqlFun lpad(Object str1, int length, Object str2) {
        return new SqlFun("lpad", new Object[]{str1, length, str2});
    }

    /**
     * 其中str1是第一个字符串，length是结果字符串的长度，str2是一个填充字符串。如果str1的长度没有length那么长，则使用str2往右边填充；如果str1的长度大于length，则截断
     *
     * @param str1   字符串
     * @param length 字符串
     * @param str2   字符串
     * @return
     */
    public static SqlFun rpad(Object str1, int length, Object str2) {
        return new SqlFun("rpad", new Object[]{str1, length, str2});
    }

    /**
     * 把object对象中出现的的search全部替换成replace
     *
     * @param str     字符串
     * @param search  要替换的字符串
     * @param replace 替换后的字符串
     * @return
     */
    public static SqlFun replace(Object str, Object search, Object replace) {
        return new SqlFun("replace", new Object[]{str, search, replace});
    }

    /**
     * 四舍五入，保留两位小数
     *
     * @param num 数值
     * @return
     */
    public static SqlFun round(Object num) {
        return new SqlFun("round", new Object[]{num});
    }

    /**
     * 向上取整
     *
     * @param num 数值
     * @return
     */
    public static SqlFun ceil(Object num) {
        return new SqlFun("ceil", new Object[]{num});
    }

    /**
     * 向下取整
     *
     * @param num 数值
     * @return
     */
    public static SqlFun floor(Object num) {
        return new SqlFun("floor", new Object[]{num});
    }

    /**
     * 保留几位小数
     *
     * @param num    数值
     * @param length 保留的小数长度
     * @return
     */
    public static SqlFun truncate(Object num, int length) {
        return new SqlFun("truncate", new Object[]{num, length});
    }

    /**
     * 求余数 num % 2
     *
     * @param num1 数值
     * @param num2 被%数
     * @return
     */
    public static SqlFun mod(Object num1, Object num2) {
        return new SqlFun("mod", new Object[]{num1, num2});
    }

}
