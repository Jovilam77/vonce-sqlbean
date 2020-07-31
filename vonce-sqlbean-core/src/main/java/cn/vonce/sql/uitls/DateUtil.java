package cn.vonce.sql.uitls;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
	 * @author Jovi
	 * @date 2018年2月1日下午6:31:41
	 * @param date1
	 *            时间较小的日期
	 * @param date2
	 *            时间较大的日期
	 * @return
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

}
