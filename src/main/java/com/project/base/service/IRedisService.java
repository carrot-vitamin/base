package com.project.base.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IRedisService {

    <T extends Serializable> String set(String key, T value);

    /**
     * 设置缓存
     * @param key 键
     * @param value 值
     * @param cacheSeconds 超时时间，0为不超时
     * @return String
     */
    <T extends Serializable> String set(String key, T value, int cacheSeconds);

    /**
     * 设置key的生存时间
     * @param key 键
     * @param seconds 生存时间 秒
     * @return String
     */
    long expire(String key, int seconds);

    /**
     * 删除缓存
     * @param key 键
     * @return
     */
    long del(String key);

    /**
     * 获取缓存的值
     * @param key 键
     * @param <T> 值
     * @return T
     */
    <T extends Serializable> T get(String key);

    /**
     * 获取所有的key
     * @return string
     */
    Set<String> keys();

    /**
     * 列举list中所有元素
     * @param key key值
     * @return T
     */
    <T extends Serializable> List<T> lrange(String key);

    /**
     * 列举指定区间元素
     * @param key 存储list的key
     * @param start 元素下标
     * @param end 元素下标；-1代表倒数一个元素，-2代表倒数第二个元素
     * @return 区间元素集合
     */
    <T extends Serializable> List<T> lrange(String key, long start, long end);

    /**
     * 设置List缓存
     * @param key 键
     * @param value 值
     * @return
     */
    <T extends Serializable> long setList(String key, List<T> value);

    /**
     * 设置List缓存
     * @param key 键
     * @param value 值
     * @param cacheSeconds 超时时间，0为不超时
     * @return
     */
    <T extends Serializable> long setList(String key, List<T> value, int cacheSeconds);

    /**
     * 设置Map缓存
     * @param key 键
     * @param value 值
     * @return
     */
    <T extends Serializable> String hmset(String key, Map<String, T> value);

    /**
     * 设置Map缓存
     * @param key 键
     * @param value 值
     * @param cacheSeconds 超时时间，0为不超时
     * @return
     */
    <T extends Serializable> String hmset(String key, Map<String, T> value, int cacheSeconds);

    /**
     * 向Map缓存中添加值
     * @param key 键
     * @param value 值
     * @return
     */
    <T extends Serializable> long hPut(String key, String mKey, T value);

    /**
     * 获取Map缓存
     * @param key 键
     * @return 值
     */
    <T extends Serializable> Map<String, T> hGetAll(String key);

    /**
     * 获取map值
     * @param key key
     * @param field map key
     * @param <T> V
     * @return T
     */
    <T extends Serializable> T hGet(String key, String field);

    /**
     * 移除Map缓存中的值
     * @param key 键
     * @param mapKey map键
     * @return
     */
    long hDel(String key, String mapKey);

    /**
     * 判断Map缓存中的Key是否存在
     * @param key 键
     * @param field map键
     * @return
     */
    boolean hexists(String key, String field);

    /**
     * 缓存是否存在
     * @param key 键
     * @return
     */
    boolean exists(String key);



    /*FIXME 以上已验证通过*/






















    /*******************************************List操作********************************/

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
     * key值对应value + 1，key不存在则设为1
     * @param key key值
     * @return 返回增加后的值
     */
    Long incr(String key);

    /**
     * key值对应value - 1，key不存在设为-1
     * @param key key值
     * @return 返回减去后的值
     */
    Long decr(String key);


    /*---------------------新工具类分割线---------------------*/












}
