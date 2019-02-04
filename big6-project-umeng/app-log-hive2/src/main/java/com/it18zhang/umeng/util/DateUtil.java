package com.it18zhang.umeng.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class DateUtil {
    /**
     * 得到指定日期的起始时刻
     */
    public static long getDayBegin(Date date, int offset) {
        try {
            //日历类
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            //时间增减
            c.add(Calendar.DAY_OF_MONTH, offset);
            Date newDate = c.getTime();
            //
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd 00:00:00");
            return sdf.parse(sdf.format(newDate)).getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 得到指定日期的起始时刻
     */
    public static long getMonthBegin(Date date, int offset) {
        try {
            //日历类
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.add(Calendar.MONTH, offset);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/01 00:00:00");
            String ymd = sdf.format(c.getTime());
            return sdf.parse(ymd).getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 得到指定日期的起始时刻
     */
    public static long getWeekBegin(Date date, int offset) {
        try {
            //日历类
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            int n = c.get(Calendar.DAY_OF_WEEK);
            c.add(Calendar.DAY_OF_MONTH, -(n - 1));
            c.add(Calendar.DAY_OF_MONTH, offset * 7);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd 00:00:00");
            String ymd = sdf.format(c.getTime());
            return sdf.parse(ymd).getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }
}