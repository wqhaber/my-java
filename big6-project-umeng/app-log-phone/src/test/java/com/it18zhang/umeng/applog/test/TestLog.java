package com.it18zhang.umeng.applog.test;

import com.alibaba.fastjson.JSONObject;
import com.it18zhang.umeng.common.domain.AppBaseLog.AppLogEntity;
import com.it18zhang.umeng.common.domain.AppBaseLog.AppStartupLog;
import com.it18zhang.umeng.phone.MockData2;
import org.junit.Test;

import java.util.List;

/**
 * Created by Administrator on 2017/9/24.
 */
public class TestLog {
    @Test
    public void testStartupLog() {
        AppStartupLog log = MockData2.createLog(AppStartupLog.class);
        System.out.println(log.getBrand());
    }

    @Test
    public void testStartupLogs() {
        List<AppStartupLog> logs = MockData2.createLogs(AppStartupLog.class);
        System.out.println();
    }

    @Test
    public void testAppLogEntity() throws Exception {
        for (; ; ) {
            AppLogEntity log = MockData2.createAppLogEntity();
            System.out.println(JSONObject.toJSONString(log));
            Thread.sleep(1000);
        }
    }
}
