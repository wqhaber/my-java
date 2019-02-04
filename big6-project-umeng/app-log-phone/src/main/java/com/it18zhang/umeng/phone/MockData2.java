package com.it18zhang.umeng.phone;

import com.alibaba.fastjson.JSONObject;
import com.it18zhang.umeng.common.domain.AppBaseLog.*;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.*;

/**
 * 模拟数据
 */
public class MockData2 {
    private static Random rand = new Random();

    //八位数字
    private static DecimalFormat df = new DecimalFormat("00000000");

    private static Map<String, List<String>> map = new HashMap<String, List<String>>();

    static {
        try {
            //得到当前线程使用的类加载器
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            InputStream in = loader.getResourceAsStream("test.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line = null;

            //条目
            String entry = null;
            List<String> list = null;
            while ((line = br.readLine()) != null) {
                //滤空
                line = line.trim();
                if (line.equals("")) {
                    continue;
                }
                //条目
                if (line.startsWith("[")) {
                    entry = line.substring(1, line.length() - 1);
                    list = new ArrayList<String>();
                    map.put(entry, list);
                } else {
                    //条目下的数据
                    list.add(line);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 随机访问条目
     */
    public static String randStr(String entry) {
        List<String> list = map.get(entry);
        if (list != null) {
            return list.get(rand.nextInt(list.size()));
        }
        return null;
    }

    public static void main(String[] args) {
        while (true) {
            try {
                AppLogEntity entity = createAppLogEntity();
                String json = JSONObject.toJSONString(entity);
                uploadJson(json);
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 上传json文件到服务器
     */
    private static void uploadJson(String json) {
        try {
            String urlStr = "http://s100:8080/colllog/colllog/index";
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //设置请求方式
            conn.setRequestMethod("POST");
            //允许输出到服务器
            conn.setDoOutput(true);
            //设置上传数据的内容类型
            conn.setRequestProperty("content-Type", "application/json");
            //将client端时间上传给服务器
            conn.setRequestProperty("clientTime", System.currentTimeMillis() + "");

            OutputStream out = conn.getOutputStream();
            out.write(json.getBytes());
            out.close();

            int code = conn.getResponseCode();
            System.out.println(code);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建Log对象
     */
    public static <T> T createLog(Class<T> t) {
        T obj = null;
        try {
            //休眠
            Thread.sleep(10);
            obj = t.newInstance();
            //设置日志的创建时间
            if (obj instanceof AppBaseLog) {
                ((AppBaseLog) obj).setCreatedAtMs(System.currentTimeMillis());
            }
            Field[] fs = t.getDeclaredFields();
            for (Field f : fs) {
                String fname = f.getName();
                Class ftype = f.getType();
                //设置可访问性
                f.setAccessible(true);
                String entry = fname.toLowerCase();
                if (ftype == String.class) {
                    String value = randStr(entry);
                    f.set(obj, value);
                } else if (ftype == int.class || ftype == Integer.class) {
                    String value = randStr(entry);
                    if (value != null) {
                        f.set(obj, Integer.parseInt(value));
                    }
                } else if (ftype == long.class || ftype == Long.class) {
                    String value = randStr(entry);
                    if (value != null) {
                        f.set(obj, Long.parseLong(value));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }

    public static <T> List<T> createLogs(Class<T> t) {
        List<T> list = new ArrayList<T>();
        int n = rand.nextInt(5) + 1;
        for (int i = 0; i < n; i++) {
            list.add(createLog(t));
        }
        return list;
    }

    /**
     * 创建日志聚合体
     */
    public static AppLogEntity createAppLogEntity() {
        AppLogEntity e = createLog(AppLogEntity.class);
        //设置设备id号
        e.setDeviceId(genDeviceId());

        e.setAppErrorLogs(createLogs(AppErrorLog.class));
        e.setAppEventLogs(createLogs(AppEventLog.class));
        e.setAppPageLogs(createLogs(AppPageLog.class));
        e.setAppStartupLogs(createLogs(AppStartupLog.class));
        e.setAppUsageLogs(createLogs(AppUsageLog.class));
        return e;
    }

    /**
     * 生成设备id
     */
    public static String genDeviceId() {
        int did = rand.nextInt(1000) + 1;
        return "um-" + df.format(did);
    }



}
