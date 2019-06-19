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
     * @return string
     */
    Set<String> keys();

    /**
     * 查看某个key的剩余生存时间,单位【秒】.永久生存或者不存在的都返回-1
     * @param key key值
     * @return string
     */
    Long ttl(String key);

    /**
     * 移除某个key的生存时间
     * @param key key值
     * @return string
     */
    Long persist(String key);

    /**
     * 查看key所储存的值的类型
     * @param key key值
     * @return string
     */
    String type(String key);

    /*******************************************List操作********************************/

    /**
     * list中添加数据
     * @param key key值
     * @param value value
     * @return string
     */
    Long lpush(String key, String value);

    /**
     * 列举list中所有元素
     * @param key key值
     * @return string
     */
    List<String> lrange(String key);

    /**
     * 列举指定区间元素
     * @param key 存储list的key
     * @param start 元素下标
     * @param end 元素下标；-1代表倒数一个元素，-2代表倒数第二个元素
     * @return 区间元素集合
     */
    List<String> lrange(String key, long start, long end);

    /**
     * 删除列表指定的值 ，后add进去的值先被删，类似于出栈
     * @param key 存储list的key
     * @param count 删除的个数（有重复时）
     * @param value 要删除的value
     * @return 成功删除指定元素个数
     */
    Long lrem(String key, long count, String value);

    /**
     * 删除区间以外的数据
     * @param key 存储list的key
     * @param start 下标起始
     * @param end 下标结束
     * @return String
     */
    String ltrim(String key, long start, long end);

    /**
     * 列表元素出栈，先进后出，后进先出
     * @param key 存储list的key
     * @return value
     */
    String lpop(String key);

    /**
     * 列表长度
     * @param key 存储list的key
     * @return 列表长度
     */
    Long llen(String key);

    /**
     * 获取指定下标的值
     * @param key 存储list的key
     * @param index 下标
     * @return 指定下标的值
     */
    String lindex(String key, long index);


    /*******************************************Set操作********************************/

    /**
     * 想set中添加元素
     * @param key 存储set的key
     * @param value value
     * @return long
     */
    Long sadd(String key, String...value);

    /**
     * 列举set所有元素
     * @param key 存储set的key
     * @return 元素集合
     */
    Set<String> smembers(String key);

    /**
     * 删除set元素
     * @param key 存储set的key
     * @param value value集合
     * @return long
     */
    Long srem(String key, String...value);

    /**
     * 判断元素是否在set中
     * @param key 存储set的key
     * @param value value
     * @return 元素是否在set中
     */
    Boolean sismember(String key, String value);

    /**
     * 两set交集
     * @param set1 集合1
     * @param set2 集合2
     * @return 两set交集
     */
    Set<String> sinter(String set1, String set2);

    /**
     * 两set并集
     * @param set1 集合1
     * @param set2 集合2
     * @return 两set并集
     */
    Set<String> sunion(String set1, String set2);

    /**
     * 两set差集：set1中有，set2中没有的元素
     * @param set1 集合1
     * @param set2 集合2
     * @return 两set差集
     */
    Set<String> sdiff(String set1, String set2);


    /*******************************************Hash操作********************************/

    /**
     * hash中添加元素
     * @param hKey 保存hash的key
     * @param key key
     * @param value value
     * @return long
     */
    Long hset(String hKey, String key, String value);

    /**
     * 添加整形元素
     * @param hKey 保存hash的key
     * @param key key
     * @param value value
     * @return long
     */
    Long hincrBy(String hKey, String key, long value);

    /**
     * 获取hash中所有key
     * @param hKey 保存hash的key
     * @return hash中所有key
     */
    Set<String> hkeys(String hKey);

    /**
     * 获取hash中所有的value
     * @param hKey 保存hash的key
     * @return hash中所有的value
     */
    List<String> hvals(String hKey);

    /**
     * 删除hash中的key值
     * @param hKey 保存hash的key
     * @param fields key
     * @return long
     */
    Long hdel(String hKey, String...fields);

    /**
     * 判断key是否存在
     * @param hKey 保存hash的key
     * @param field key
     * @return key是否存在
     */
    Boolean hexists(String hKey, String field);

    /**
     * 获取key对应的值
     * @param hKey 保存hash的key
     * @param field key
     * @return key对应的值
     */
    String hget(String hKey, String field);

    /**
     * 批量获取key对应的value
     * @param hKey 保存hash的key
     * @param value key
     * @return key对应的value
     */
    List<String> hmget(String hKey, String...value);
}
