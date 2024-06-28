package cn.vonce.sql.enumerate;

/**
 * 时间单位
 *
 * @author Jovi
 * @email imjovi@qq.com
 * @date 2024/6/28 14:10
 */
public enum TimeUnit {

    MICROSECOND("微秒"),
    FRAC_SECOND("毫秒"),
    SECOND("秒"),
    MINUTE("分"),
    HOUR("时"),
    DAY("天"),
    WEEK("周"),
    MONTH("月"),
    QUARTER("季"),
    YEAR("年");

    private String desc;

    TimeUnit(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

}
