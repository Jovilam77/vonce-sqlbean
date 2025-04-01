package cn.vonce.sql.uitls;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

/**
 * 时间工具类 Created by jovi on 2017/7/10.
 */

public class DateUtil {

    /**
     * 获取时间间隔(用于显示动态)
     *
     * @param createTime
     * @return
     */
    public static String getInterval(String createTime) { // 传入的时间格式必须类似于2012-8-21
        // 17:53:20这样的格式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ParsePosition pos = new ParsePosition(0);
        Date date = sdf.parse(createTime, pos);
        return getInterval(date);
    }

    /**
     * 获取时间间隔(用于显示动态)
     *
     * @param date
     * @return
     */
    public static String getInterval(Date date) { // 传入的时间格式必须类似于2012-8-21
        // 17:53:20这样的格式
        String interval = null;

        // 用现在距离1970年的时间间隔new
        // Date().getTime()减去以前的时间距离1970年的时间间隔d1.getTime()得出的就是以前的时间与现在时间的时间间隔
        long time = (new Date().getTime() - date.getTime()) / 1000;// 得出的时间间隔是毫秒
        if (time > 0 && time < 60) { // 1小时内
            interval = time + "秒前";
        } else if (time > 60 && time < 3600) {
            interval = time / 60 + "分钟前";
        } else if (time >= 3600 && time < 3600 * 24) {
            interval = time / 3600 + "小时前";
        } else if (time >= 3600 * 24 && time < 3600 * 48) {
            interval = "昨天";
        } else if (time >= 3600 * 48 && time < 3600 * 72) {
            interval = "前天";
        } else {
            // 大于24小时，则显示正常的时间，但是不显示秒
            interval = dateToString(date, "yyyy-MM-dd HH:mm");
        }
        return interval;
    }

    /**
     * 时间格式化为字符串
     *
     * @param date
     * @return
     */
    public static String dateToString(Date date) {
        if (date != null) {
            return dateToString(date, "yyyy-MM-dd HH:mm:ss");
        }
        return "";
    }

    /**
     * 时间格式化为字符串
     *
     * @param date
     * @param format
     * @return
     */
    public static String dateToString(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        if (date != null) {
            return sdf.format(date);
        }
        return "";
    }

    /**
     * 将字符串日期转为日期对象(默认格式：yyyy-MM-dd HH:mm:ss)
     *
     * @param createTime
     * @return
     */
    public static Date stringToDate(String createTime) {
        return stringToDate(createTime, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 将字符串日期转为日期对象
     *
     * @param createTime
     * @param format
     * @return
     */
    public static Date stringToDate(String createTime, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        ParsePosition pos = new ParsePosition(0);
        return sdf.parse(createTime, pos);
    }

    /**
     * 计算两个时间的差的天数
     *
     * @param date1 时间较小的日期
     * @param date2 时间较大的日期
     * @return
     * @author Jovi
     * @date 2018年2月1日下午6:31:41
     */
    public static int differentDays(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        int day1 = cal1.get(Calendar.DAY_OF_YEAR);
        int day2 = cal2.get(Calendar.DAY_OF_YEAR);

        int year1 = cal1.get(Calendar.YEAR);
        int year2 = cal2.get(Calendar.YEAR);
        if (year1 != year2) // 同一年
        {
            int timeDistance = 0;
            for (int i = year1; i < year2; i++) {
                if (i % 4 == 0 && i % 100 != 0 || i % 400 == 0) // 闰年
                {
                    timeDistance += 366;
                } else // 不是闰年
                {
                    timeDistance += 365;
                }
            }

            return timeDistance + (day2 - day1);
        } else // 不同年
        {
            return day2 - day1;
        }
    }

    /**
     * 将字符串 转成 LocalDateTime 示例："2016-04-04 11:50:53"
     *
     * @param dateTime 时间
     * @return LocalDateTime
     */
    public static LocalDateTime strTimeToLocalDateTime(String dateTime) {
        DateTimeFormatter FORMATTER_FULL = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.of("+8"));
        return LocalDateTime.parse(dateTime, FORMATTER_FULL);
    }

    /**
     * 将字符串 转成 LocalDateTime 示例："2016-04-04 11:50:53"
     *
     * @param dateTime          时间
     * @param dateTimeFormatter 格式化
     * @return LocalDateTime
     */
    public static LocalDateTime strTimeToLocalDateTime(String dateTime, DateTimeFormatter dateTimeFormatter) {
        return LocalDateTime.parse(dateTime, dateTimeFormatter);
    }

    /**
     * date 转成 LocalDateTime
     *
     * @param date 日期
     */
    public static LocalDateTime dateToLocalDateTime(Date date) {
        if (date == null) {
            return null;
        }
        Instant instant = date.toInstant();
        ZoneId zone = ZoneId.systemDefault();
        return LocalDateTime.ofInstant(instant, zone);
    }

    /**
     * date 转成 LocalDate
     *
     * @param date 日期
     */
    public static LocalDate dateToLocalDate(Date date) {
        if (date == null) {
            return null;
        }
        Instant instant = date.toInstant();
        ZoneId zone = ZoneId.systemDefault();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);
        return localDateTime.toLocalDate();
    }

    /**
     * date 转成 LocalTime
     *
     * @param date 日期
     */
    public static LocalTime dateToLocalTime(Date date) {
        if (date == null) {
            return null;
        }
        Instant instant = date.toInstant();
        ZoneId zone = ZoneId.systemDefault();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);
        return localDateTime.toLocalTime();
    }

    /**
     * LocalDateTime 转成 Date
     *
     * @param localDateTime 时间
     * @return 结果集
     */
    public static Date localDateTimeToDate(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        }
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDateTime.atZone(zone).toInstant();
        return Date.from(instant);
    }

    /**
     * localDate 转成 Date
     *
     * @param localDate 结果集
     */
    public static Date localDateToDate(LocalDate localDate) {
        if (localDate == null) {
            return null;
        }
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDate.atStartOfDay().atZone(zone).toInstant();
        return Date.from(instant);
    }

    /**
     * LocalTime 转成 Date
     *
     * @param localTime 本地时间
     * @param localDate 时间
     */
    public static Date localTimeToDate(LocalTime localTime, LocalDate localDate) {
        LocalDateTime localDateTime = LocalDateTime.of(localDate, localTime);
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDateTime.atZone(zone).toInstant();
        return Date.from(instant);
    }

    /**
     * 日期转字符串 支持Jdk8之前日期及Jdk8+的日期
     *
     * @param date
     * @return
     */
    public static String unifyDateToString(Object date) {
        if (date == null) {
            return null;
        }
        if (date instanceof String) {
            return date.toString();
        } else if (date instanceof Date || date instanceof java.sql.Date || date instanceof java.sql.Timestamp || date instanceof java.sql.Time) {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(date);
        } else if (date instanceof LocalDateTime) {
            DateTimeFormatter FORMATTER_FULL = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS").withZone(ZoneId.of("+8"));
            return ((LocalDateTime) date).format(FORMATTER_FULL);
        } else if (date instanceof LocalDate) {
            return ((LocalDate) date).format(DateTimeFormatter.ofPattern("yyyy-MM-dd").withZone(ZoneId.of("+8")));
        } else if (date instanceof LocalTime) {
            return ((LocalTime) date).format(DateTimeFormatter.ofPattern("HH:mm:ss").withZone(ZoneId.of("+8")));
        }
        return null;
    }

    /**
     * 日期转字符串 支持Jdk8之前日期及Jdk8+的日期
     *
     * @param date
     * @param format
     * @return
     */
    public static String unifyDateToString(Object date, String format) {
        if (date == null) {
            return null;
        }
        if (date instanceof String) {
            return date.toString();
        } else if (date instanceof Date || date instanceof java.sql.Date || date instanceof java.sql.Timestamp || date instanceof java.sql.Time) {
            return new SimpleDateFormat(format).format(date);
        } else if (date instanceof LocalDateTime) {
            DateTimeFormatter FORMATTER_FULL = DateTimeFormatter.ofPattern(format).withZone(ZoneId.of("+8"));
            return ((LocalDateTime) date).format(FORMATTER_FULL);
        } else if (date instanceof LocalDate) {
            return ((LocalDate) date).format(DateTimeFormatter.ofPattern(format).withZone(ZoneId.of("+8")));
        } else if (date instanceof LocalTime) {
            return ((LocalTime) date).format(DateTimeFormatter.ofPattern(format).withZone(ZoneId.of("+8")));
        }
        return null;
    }

    /**
     * 日期转时间戳 支持Jdk8之前日期及Jdk8+的日期
     *
     * @param date
     * @return
     */
    public static Long unifyDateToTimestamp(Object date) {
        if (date == null) {
            return null;
        }
        if (date instanceof Date || date instanceof java.sql.Date || date instanceof java.sql.Timestamp || date instanceof java.sql.Time) {
            java.util.Date utilDate = (Date) date;
            return utilDate.getTime();
        } else if (date instanceof LocalDateTime) {
            return ((LocalDateTime) date).toInstant(ZoneOffset.of("+8")).toEpochMilli();
        } else if (date instanceof LocalDate) {
            return ((LocalDate) date).atStartOfDay(ZoneOffset.of("+8")).toInstant().toEpochMilli();
        } else if (date instanceof LocalTime) {
            return ((LocalTime) date).atDate(LocalDate.now()).atZone(ZoneOffset.of("+8")).toInstant().toEpochMilli();
        }
        return null;
    }


    /**
     * 获取前一个月的第一天的时间
     *
     * @return 几号
     */
    public Integer getLastMonthEndDay() {
        LocalDate with = LocalDate.now().plusMonths(-1).with(TemporalAdjusters.firstDayOfMonth());
        return with.with(TemporalAdjusters.lastDayOfMonth()).getDayOfMonth();
    }

    /**
     * 获取后一个月的第一天的时间
     *
     * @return 几号
     */
    public static Integer getNextMonthEndDay() {
        LocalDate with = LocalDate.now().plusMonths(1).with(TemporalAdjusters.firstDayOfMonth());
        return with.with(TemporalAdjusters.lastDayOfMonth()).getDayOfMonth();
    }

    /**
     * 获取日历表
     *
     * @param index -1表示上月 0表示当月 1表示下月
     * @return 结果集
     */
    public static List<String[]> getCalendarTable(Integer index) {
        // 根据输入确定月份
        LocalDate months = LocalDate.now().plusMonths(index);
        // 获取当前月的第一天
        LocalDate currentMonthFirstDay = months.with(TemporalAdjusters.firstDayOfMonth());
        // 获取当前月的最后一天
        LocalDate currentMonthEndDay = months.with(TemporalAdjusters.lastDayOfMonth());
        // 获取一个月多少 //当月的天数
        int days = LocalDate.now().lengthOfMonth();
        // 获取周几
        int dayOfWeek = currentMonthFirstDay.getDayOfWeek().getValue();
        // 获取日历第一天
        LocalDate calendarFirstDay = months.minus(dayOfWeek - 1, ChronoUnit.DAYS);
        // 获取日历最后一天
        LocalDate calendarEndDay = currentMonthEndDay.plus(42 - days - (dayOfWeek - 1), ChronoUnit.DAYS);
        List<String[]> localDateList = new ArrayList<>();
        long length = calendarEndDay.toEpochDay() - calendarFirstDay.toEpochDay();
        String[] dataCalendarDTO;
        for (long i = length; i >= 0; i--) {
            dataCalendarDTO = new String[2];
            // 当前日期
            LocalDate localDate = calendarEndDay.minusDays(i);
            DateTimeFormatter DATE_STR = DateTimeFormatter.ofPattern("yyyy-MM-dd").withZone(ZoneId.of("+8"));
            dataCalendarDTO[0] = localDate.format(DATE_STR);
            // 周几
            dataCalendarDTO[1] = localDate.getDayOfWeek().getValue() + "";
            localDateList.add(dataCalendarDTO);
        }
        return localDateList;
    }

    /**
     * @param begin 开始日期
     * @param end   结束日期
     * @return 开始与结束之间的所以日期，包括起止
     */
    public static List<LocalDate> getMiddleAllDate(LocalDate begin, LocalDate end) {
        List<LocalDate> localDateList = new ArrayList<>();
        long length = end.toEpochDay() - begin.toEpochDay();
        for (long i = length; i >= 0; i--) {
            localDateList.add(end.minusDays(i));
        }
        return localDateList;
    }

    /**
     * 获取前几天或者后几天的时间
     *
     * @param offSet     负数代表前几天
     * @param timeFormat 时间格式化类型
     * @return 格式化后的结果
     */
    public static String getYesterdayByFormat(Integer offSet, String timeFormat) {
        return LocalDateTime.now().plusDays(offSet).format(DateTimeFormatter.ofPattern(timeFormat));
    }

    /**
     * 获取当前时间的这个周的周一
     *
     * @return
     */
    public static Date getMonday() {
        return getMonday(null);
    }

    /**
     * 获取指定时间的这个周的周一
     *
     * @param date
     * @return
     */
    public static Date getMonday(Date date) {
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        calendar.setFirstDayOfWeek(Calendar.MONDAY);//以周一为首日
        if (date != null) {
            calendar.setTimeInMillis(date.getTime());
        } else {
            calendar.setTimeInMillis(System.currentTimeMillis());//当前时间
        }
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);//周一
        return calendar.getTime();
    }

}
