package com.it18zhang.umeng.colllog.controller;

import com.alibaba.fastjson.JSONObject;
import com.it18zhang.umeng.colllog.util.DataUtil;
import com.it18zhang.umeng.colllog.util.GeoLiteUtil;
import com.it18zhang.umeng.common.domain.AppBaseLog.AppBaseLog;
import com.it18zhang.umeng.common.domain.AppBaseLog.AppLogEntity;
import com.it18zhang.umeng.common.domain.AppBaseLog.AppStartupLog;
import com.it18zhang.umeng.common.domain.AppBaseLog.AppUsageLog;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Properties;

/**
 * 收集日志
 */
@Controller
@RequestMapping("/colllog")
public class CollectLogController {

    @RequestMapping(value = "/index", method = RequestMethod.POST)
    @ResponseBody
    public String collLog(@RequestBody AppLogEntity e, HttpServletRequest req) {
        //完成时间对齐
        //client端时间
        long clientMS = Long.parseLong(req.getHeader("clientTime"));
        //服务器时间
        long serverMS = System.currentTimeMillis();
        //计算时间差
        long offset = serverMS - clientMS;
        //对齐时间
        alignTime(offset, e);

        //处理客户端ip地址问题
        String clientIP = req.getRemoteAddr();
        processIp(clientIP, e);

        //实现属性复制
        DataUtil.copyProperites(e, e.getAppErrorLogs());
        DataUtil.copyProperites(e, e.getAppEventLogs());
        DataUtil.copyProperites(e, e.getAppPageLogs());
        DataUtil.copyProperites(e, e.getAppStartupLogs());
        DataUtil.copyProperites(e, e.getAppUsageLogs());
        sendLogToKafka(e);

        return "";
    }

    private void outAllLogs(AppLogEntity e) {
        outLogJson(e.getAppUsageLogs());
        outLogJson(e.getAppStartupLogs());
        outLogJson(e.getAppPageLogs());
        outLogJson(e.getAppEventLogs());
        outLogJson(e.getAppErrorLogs());
    }

    private <T extends AppBaseLog> void outLogJson(List<T> list) {
        for (T t : list) {
            String json = JSONObject.toJSONString(t);
            System.out.println(json);
        }
    }


    //对齐时间
    public void alignTime(long offset, AppLogEntity e) {
        alignTime(offset, e.getAppErrorLogs());
        alignTime(offset, e.getAppEventLogs());
        alignTime(offset, e.getAppPageLogs());
        alignTime(offset, e.getAppStartupLogs());
        alignTime(offset, e.getAppUsageLogs());
    }

    /**
     * 处理client ip地址问题
     */
    private void processIp(String clientIP, AppLogEntity e) {
        for (AppStartupLog log : e.getAppStartupLogs()) {
            log.setCountry(GeoLiteUtil.getCountry(clientIP));
            log.setProvince(GeoLiteUtil.getProvince(clientIP));
            log.setIpAddress(clientIP);
        }
    }

    //对齐时间
    public void alignTime(long offset, List<? extends AppBaseLog> logs) {
        for (AppBaseLog log : logs) {
            log.setCreatedAtMs(log.getCreatedAtMs() + offset);
        }
    }

    /**
     *
     */
    private void sendLogToKafka(AppLogEntity e) {
        Properties props = new Properties();
        props.put("bootstrap.servers", "s101:9092,s102:9092,s103:9092");
        props.put("acks", "all");
        props.put("retries", 3);
        props.put("batch.size", 16384);
        props.put("linger.ms", 60000);
        props.put("buffer.memory", 33554432);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        Producer<String, String> p = new KafkaProducer<String, String>(props);
        doSendLog(p, "topic-startup-log", e.getAppStartupLogs());
        doSendLog(p, "topic-event-log", e.getAppEventLogs());
        doSendLog(p, "topic-error-log", e.getAppErrorLogs());
        doSendLog(p, "topic-page-log", e.getAppPageLogs());
        doSendLog(p, "topic-usage-log", e.getAppUsageLogs());
        p.close();
    }

    /**
     * 发送单个日志
     */
    private void doSendLog(Producer p, String topic, List<? extends AppBaseLog> list) {
        for (AppBaseLog log : list) {
            p.send(new ProducerRecord<String, String>(topic, JSONObject.toJSONString(log)));
        }
    }
}
