package com.it18zhang.umeng.flume;

import com.alibaba.fastjson.JSON;
import com.it18zhang.umeng.common.domain.AppBaseLog.AppBaseLog;
import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.interceptor.Interceptor;
import org.apache.flume.interceptor.StaticInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * 日志拦截器
 */
public class AppLogInterceptor implements Interceptor {

    private static final Logger logger = LoggerFactory.getLogger(StaticInterceptor.class);

    /**
     */
    private AppLogInterceptor() {
    }

    public void initialize() {
    }

    /**
     * Modifies events in-place.
     */
    public Event intercept(Event event) {
        String json = new String(event.getBody());
        Map<String, String> headers = event.getHeaders();
        AppBaseLog log = JSON.parseObject(json, AppBaseLog.class);
        long time = log.getCreatedAtMs();
        String appid = log.getAppId();
        //放置时间戳头
        headers.put("timestamp", time + "");
        //
        headers.put("appid", appid);

        headers.put("logType", log.getLogType() + "");

        System.out.printf("time=%d,appid=%s,logtype=%s", time, appid, log.getLogType());

        return event;
    }

    /**
     */
    public List<Event> intercept(List<Event> events) {
        for (Event event : events) {
            intercept(event);
        }
        return events;
    }

    public void close() {
    }

    /**
     */
    public static class Builder implements Interceptor.Builder {

        private boolean preserveExisting;
        private String key;
        private String value;

        public void configure(Context context) {
        }

        public Interceptor build() {
            return new AppLogInterceptor();
        }

    }
}