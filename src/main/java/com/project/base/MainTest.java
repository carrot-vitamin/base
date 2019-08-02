package com.project.base;

import com.project.base.service.IRedisService;
import com.project.base.service.impl.RedisServiceImpl;
import com.project.base.util.AESUtils;
import com.project.base.util.KeyGenerator;

import java.io.Serializable;

public class MainTest {

    private static IRedisService redisService = new RedisServiceImpl("test", "0okm(IJN");

    public static void main(String[] args) throws Exception {
        String key = KeyGenerator.generate();
        System.out.println("WEo5o93xhAiZkpzRH4NpZQ==\n");
        for (int i = 0; i < 20; i ++) {
            System.out.println(AESUtils.encrypt("1234567", key));
        }
    }

    private static void sleep(int seconds) throws Exception {
        Thread.sleep(seconds * 1000);
    }

    static class Person implements Serializable {
        String name;

        public Person(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "Person{" +
                    "name='" + name + '\'' +
                    '}';
        }
    }

}
