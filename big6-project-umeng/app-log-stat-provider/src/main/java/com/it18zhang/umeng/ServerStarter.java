package com.it18zhang.umeng;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 */
public class ServerStarter {
    public static void main(String[] args) throws Exception {
        ApplicationContext ac = new ClassPathXmlApplicationContext("beans.xml");
        while (true) {
            Thread.sleep(2000);
        }
    }
}