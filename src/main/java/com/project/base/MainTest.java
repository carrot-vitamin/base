package com.project.base;

import com.project.base.service.IRedisService;
import com.project.base.service.impl.RedisServiceImpl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class MainTest {

    private static IRedisService redisService = new RedisServiceImpl("test", "0okm(IJN");

    public static void main(String[] args) throws Exception {
        Map<String, Person> map = new HashMap<>(2);
        map.put("k1", new Person("张三"));
        map.put("k2", new Person("张三1"));
        redisService.hmset("key1", map);
        System.out.println(redisService.hGetAll("key1"));
        redisService.hPut("key1", "k5", new Person("vvv"));
        System.out.println(redisService.hGetAll("key1"));
        redisService.hDel("key1", "k2");
//        sleep(5);
        System.out.println(redisService.hGetAll("key1"));

        Person p = redisService.hGet("key1", "k5");
        System.out.println(p);
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
