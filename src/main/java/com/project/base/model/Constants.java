package com.project.base.model;

import com.project.base.service.impl.RedisServiceImpl;

/**
 * @author yin
 * @date 2018/12/29 20:02
 */
public class Constants {

    private Constants() {}

    private static String redisHost;

    private static int redisPort;

    private static String redisPassword;

    public static void authRedis() {
        new RedisServiceImpl();
    }

    public static String getRedisHost() {
        return redisHost;
    }

    public static void setRedisHost(String redisHost) {
        Constants.redisHost = redisHost;
    }

    public static int getRedisPort() {
        return redisPort;
    }

    public static void setRedisPort(int redisPort) {
        Constants.redisPort = redisPort;
    }

    public static String getRedisPassword() {
        return redisPassword;
    }

    public static void setRedisPassword(String redisPassword) {
        Constants.redisPassword = redisPassword;
    }
}
