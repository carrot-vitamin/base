package com.project.base.service;

import java.util.List;
import java.util.Set;

public interface IRedisService {

    String set(String key, String value);

    String set(String key, String value, long seconds);

    String get(String key);

    String expire(String key, long seconds);

    /**
     * 获取所有的key
     * @return
     */
    Set<String> keys();

    /**
     * 查看某个key的剩余生存时间,单位【秒】.永久生存或者不存在的都返回-1
     * @param key
     * @return
     */
    Long ttl(String key);

    /**
     * 移除某个key的生存时间
     * @param key
     * @return
     */
    Long persist(String key);

    /**
     * 查看key所储存的值的类型
     * @param key
     * @return
     */
    String type(String key);

    /**
     * list中添加数据
     * @param key
     * @param value
     * @return
     */
    Long lpush(String key, String value);

    /**
     * 列举list中所有元素
     * @param key
     * @return
     */
    List<String> lrange(String key);

}
