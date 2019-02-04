package com.it18zhang.umeng.hive.udf;

import com.it18zhang.umeng.util.DateUtil;
import org.apache.hadoop.hive.ql.exec.UDF;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 自定义函数,获取天的起始时刻
 */
public class DayBeginUDF extends UDF {
    /**
     * 今天零时刻
     */
    public long evaluate() {
        Date date = new Date();
        return evaluate(date, 0);
    }

    public long evaluate(int offset) {
        Date date = new Date();
        return evaluate(date, offset);
    }

    public long evaluate(long timestamp, int offset) {
        Date date = new Date(timestamp);
        return evaluate(date, offset);
    }

    public long evaluate(Date date, int offset) {
        return DateUtil.getDayBegin(date, offset);
    }

    public long evaluate(String date, String fmt, int offset) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(fmt);
            return DateUtil.getDayBegin(sdf.parse(date), offset);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }
}