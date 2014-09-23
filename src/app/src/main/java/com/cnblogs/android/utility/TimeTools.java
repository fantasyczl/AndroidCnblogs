package com.cnblogs.android.utility;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时间工具类
 * Created by ygl on 14-9-16.
 */
public class TimeTools {
    public static final String YMD = "yyyy-MM-dd";
    public static final String YMDHms = "yyyy-MM-dd HH:mm:ss";

    /**
     * String转换为时间
     * @param str
     * @return
     */
    public static Date parseDate(String str){
        Date addTime = null;

        if (TextUtils.isEmpty(str)) {
            return new Date();
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat(YMDHms);

        try {
            if (str.length() <= 10) {
                dateFormat.applyPattern(TimeTools.YMD);
            }
            addTime = dateFormat.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return addTime;
    }

    /**
     * 将日期转换为字符串
     * @param date
     * @return
     */
    public static String parseDateToString(Date date){
        if (date == null) {
            date = new Date();
        }

        return ParseDateToString(date, YMDHms);
    }

    /**
     * 将日期转换为字符串（重载）
     * @param date
     * @param format:时间格式，必须符合yyyy-MM-dd hh:mm:ss
     * @return
     */
    public static String ParseDateToString(Date date,String format){
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);

        return dateFormat.format(date);
    }
}