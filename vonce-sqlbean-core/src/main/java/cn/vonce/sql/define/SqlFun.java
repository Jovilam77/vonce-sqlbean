package cn.vonce.sql.define;

import cn.vonce.sql.bean.Column;
import cn.vonce.sql.bean.Original;

import java.util.Arrays;
import java.util.List;

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
     * 日期
     *
     * @return
     */
    public static SqlFun date(Object date) {
        return new SqlFun("date", new Object[]{date});
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
    public static SqlFun curDate() {
        return new SqlFun("curDate", null);
    }

    /**
     * 当前系统时间的时间，19:23:12
     *
     * @return
     */
    public static SqlFun curTime() {
        return new SqlFun("curTime", null);
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
    public static SqlFun monthName(Object date) {
        return new SqlFun("monthName", new Object[]{date});
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
    public static SqlFun dateDiff(Object date1, Object date2) {
        return new SqlFun("dateDiff", new Object[]{date1, date2});
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
     * @param str 字符串数组
     * @return
     */
    public static SqlFun concat(Object... str) {
        return new SqlFun("concat", str);
    }

    /**
     * 拼接字符
     *
     * @param separator 分隔符
     * @param str       字符串数组
     * @return
     */
    public static SqlFun concat_ws(String separator, Object... str) {
        List<Object> objectList = Arrays.asList(str);
        objectList.add(0, separator);
        return new SqlFun("concat", objectList.toArray());
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
    public static SqlFun lPad(Object str1, int length, Object str2) {
        return new SqlFun("lPad", new Object[]{str1, length, str2});
    }

    /**
     * 其中str1是第一个字符串，length是结果字符串的长度，str2是一个填充字符串。如果str1的长度没有length那么长，则使用str2往右边填充；如果str1的长度大于length，则截断
     *
     * @param str1   字符串
     * @param length 字符串
     * @param str2   字符串
     * @return
     */
    public static SqlFun rPad(Object str1, int length, Object str2) {
        return new SqlFun("rPad", new Object[]{str1, length, str2});
    }

    /**
     * 清除字符左边的空格
     *
     * @param str 字符串
     * @return
     */
    public static SqlFun ltrim(Object str) {
        return new SqlFun("ltrim", new Object[]{str});
    }

    /**
     * 清除字符右边的空格
     *
     * @param str 字符串
     * @return
     */
    public static SqlFun rtrim(Object str) {
        return new SqlFun("rtrim", new Object[]{str});
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

    /************Sqlserver************/

    /**
     * 查找字符对应的下标
     *
     * @param query 查找的内容
     * @param str   数据源字符串
     * @return
     */
    public static SqlFun charIndex(Object query, Object str) {
        return new SqlFun("charIndex", new Object[]{query, str});
    }

    /**
     * 查找字符对应的下标
     *
     * @param query 查找的内容
     * @param str   数据源字符串
     * @param index 指定位置开始查找
     * @return
     */
    public static SqlFun charIndex(Object query, Object str, int index) {
        return new SqlFun("charIndex", new Object[]{query, str, index});
    }

    /**
     * 字符串长度
     *
     * @param str 字符串
     * @return
     */
    public static SqlFun len(Object str) {
        return new SqlFun("len", new Object[]{str});
    }

    /**
     * 从左边开始截取指定长度的字符串
     *
     * @param str    字符串
     * @param length 长度
     * @return
     */
    public static SqlFun left(Object str, int length) {
        return new SqlFun("left", new Object[]{str, length});
    }

    /**
     * 从右边开始截取指定长度的字符串
     *
     * @param str    字符串
     * @param length 长度
     * @return
     */
    public static SqlFun right(Object str, int length) {
        return new SqlFun("right", new Object[]{str, length});
    }

    /**
     * 从字符串的某个位置删除指定长度的字符串之后插入新字符串
     *
     * @param str    数据源字符串
     * @param index  开始位置
     * @param length 要删除的字符串长度
     * @param newStr 新字符串
     * @return
     */
    public static SqlFun stuff(Object str, int index, int length, Object newStr) {
        return new SqlFun("stuff", new Object[]{str, index, length, newStr});
    }

    /**
     * 当前登录的计算机名称
     *
     * @return
     */
    public static SqlFun host_name() {
        return new SqlFun("host_name", null);
    }

    /**
     * 指定用户名Id的用户名
     *
     * @return
     */
    public static SqlFun user_name(Object id) {
        return new SqlFun("user_name", new Object[]{id});
    }

    /**
     * 转换数据类型
     *
     * @return
     */
    public static SqlFun convert(Object type, Object value) {
        return new SqlFun("convert", new Object[]{type, value});
    }

    /**
     * 返回字节数
     *
     * @return
     */
    public static SqlFun dataLength(Object value) {
        return new SqlFun("dataLength", new Object[]{value});
    }

    /**
     * 获取当前系统日期
     *
     * @return
     */
    public static SqlFun getDate() {
        return new SqlFun("getDate", null);
    }

    /**
     * 添加指定日期后的日期
     *
     * @param datePart 年份(yy . yyyy . year)、季度(qq , q . quarter)、月份(mm , m , month)、年中的日(dy. y)、日(dd ,d ,day)、周(wk ,ww , week)、星期(dw.w)、小时(hh , hour)、分钟(mi , n . minute)、秒(ss , s , second)、毫秒(ms)、微秒(mcs)、纳秒(ns)
     * @param num      添加的间隔数，最好是整数，对于未来的时间，此数是正数，对于过去的时间，此数是负数
     * @param date     合法的日期表达式，类型可以是datetime、smalldatetime、char
     * @return
     */
    public static SqlFun dateAdd(Original datePart, int num, Object date) {
        return new SqlFun("dateAdd", new Object[]{datePart, num, date});
    }

    /**
     * 获取时差
     *
     * @param datePart  年份(yy . yyyy . year)、季度(qq , q . quarter)、月份(mm , m , month)、年中的日(dy. y)、日(dd ,d ,day)、周(wk ,ww , week)、星期(dw.w)、小时(hh , hour)、分钟(mi , n . minute)、秒(ss , s , second)、毫秒(ms)、微秒(mcs)、纳秒(ns)
     * @param startDate 开始时间,合法的日期表达式，类型可以是datetime、smalldatetime、char
     * @param endDate   结束时间,合法的日期表达式，类型可以是datetime、smalldatetime、char
     * @return
     */
    public static SqlFun dateDiff(Original datePart, Object startDate, Object endDate) {
        return new SqlFun("dateDiff", new Object[]{datePart, startDate, endDate});
    }

    /**
     * 获取指定日期部分的字符串形式
     *
     * @param datePart 年份(yy . yyyy . year)、季度(qq , q . quarter)、月份(mm , m , month)、年中的日(dy. y)、日(dd ,d ,day)、周(wk ,ww , week)、星期(dw.w)、小时(hh , hour)、分钟(mi , n . minute)、秒(ss , s , second)、毫秒(ms)、微秒(mcs)、纳秒(ns)
     * @param date     合法的日期表达式，类型可以是datetime、smalldatetime、char
     * @return
     */
    public static SqlFun dateName(Original datePart, Object date) {
        return new SqlFun("dateName", new Object[]{datePart, date});
    }

    /**
     * 获取指定日期部分的整数形式
     *
     * @param datePart 年份(yy . yyyy . year)、季度(qq , q . quarter)、月份(mm , m , month)、年中的日(dy. y)、日(dd ,d ,day)、周(wk ,ww , week)、星期(dw.w)、小时(hh , hour)、分钟(mi , n . minute)、秒(ss , s , second)、毫秒(ms)、微秒(mcs)、纳秒(ns)
     * @param date     合法的日期表达式，类型可以是datetime、smalldatetime、char
     * @return
     */
    public static SqlFun datePart(Original datePart, Object date) {
        return new SqlFun("datePart", new Object[]{datePart, date});
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
     * 向上取整
     *
     * @param num 数值
     * @return
     */
    public static SqlFun ceiling(Object num) {
        return new SqlFun("ceiling", new Object[]{num});
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

    /**
     * 返回符号或0
     *
     * @param num 数值
     * @return
     */
    public static SqlFun sign(Object num) {
        return new SqlFun("sign", new Object[]{num});
    }

    /**
     * 返回平方根
     *
     * @param num 数值
     * @return
     */
    public static SqlFun sqrt(Object num) {
        return new SqlFun("sqrt", new Object[]{num});
    }

    /**
     * 获取随机数
     *
     * @return
     */
    public static SqlFun rand() {
        return new SqlFun("rand", null);
    }

}
