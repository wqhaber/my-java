package com.it18zhang.umeng.colllog.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.maxmind.db.Reader;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class GeoLiteUtil {

    private static Reader reader = null;

    private static Map<String, String> cache = new HashMap<String, String>();

    static {
        try {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            InputStream in = loader.getResource("GeoLite2-City.mmdb").openStream();
            reader = new Reader(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取条目
     */
    public static String getEntry(String ip) {
        try {
            String entry = cache.get(ip);
            if (entry == null) {
                JsonNode node = reader.get(InetAddress.getByName(ip));
                if(node == null){
                    return "未知国家,未知省份";
                }
                String country = node.get("country").get("names").get("zh-CN").textValue();
                String province = node.get("subdivisions").get(0).get("names").get("zh-CN").textValue();
                entry = country + "," + province;
                cache.put(ip, entry);
                return entry;
            }
            return entry;
        } catch (IOException e) {
            System.out.println(ip + " = null");
        }
        return null;
    }

    public static String getCountry(String ip) {
        String entry = getEntry(ip);
        if (entry != null) {
            return entry.split(",")[0];
        }
        return null;
    }

    public static String getProvince(String ip) {
        String entry = getEntry(ip);
        if (entry != null) {
            return entry.split(",")[1];
        }
        return null;
    }
}