package cn.duanyufei.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by DUAN Yufei on 2017/5/8.
 */

public class DateUtil {
    /**
     * 将日期转换为2017-05-08 13:38:09格式
     *
     * @param date 日期
     * @return 字符串
     */
    public static String toDateTimeString(Date date) {
        if (date == null)
            return "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        String temp = sdf.format(date);
        return temp;
    }

    /**
     * 将日期转换为13:38:09格式
     *
     * @param date 日期
     * @return 字符串
     */
    public static String toTimeString(Date date) {
        if (date == null)
            return "";
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.CHINA);
        String temp = sdf.format(date);
        return temp;
    }

    /**
     * 将日期字符串（格式：yyyy-MM-dd HH:mm:ss）转换为日期
     *
     * @param str 日期字符串
     * @return 日期类型
     * @throws ParseException
     */
    public static Date fromDateTimeString(String str) throws ParseException {
        if (str == null || str.length() == 0)
            return null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        return sdf.parse(str);
    }

    /**
     * 将日期转换为下午03:13格式
     */
    public static String toShortTimeString(Date date) {
        if (date == null) return "";
        return new SimpleDateFormat("aahh:mm", Locale.CHINA).format(date);
    }

    /**
     * 将日期转换为2017年5月9日格式
     */
    public static String toShortDateString(Date date) {
        if (date == null) return "";
        return new SimpleDateFormat("yyyy/M/d", Locale.CHINA).format(date);
    }

    public static String toDateString(Date date) {
        if (date == null) return "";
        return new SimpleDateFormat("yyyy.MM.dd", Locale.CHINA).format(date);
    }
}
