package com.project.base;

import com.project.base.service.IRedisService;
import com.project.base.service.impl.RedisServiceImpl;

/**
 * @ClassName: MainTest
 * @Description:
 * @author: yinshaobo
 * @date: 2019/4/12 15:58
 */
public class MainTest {

    public static void main(String[] args) throws InterruptedException {
        IRedisService redisService = new RedisServiceImpl("192.168.32.128", 6379, "123456");
        redisService.set("key", "1");
        redisService.set("key", "2", 2);
        System.out.println(redisService.get("key"));
        Thread.sleep(3 * 1000);
        System.out.println(redisService.get("key"));
    }
}
