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
     * @param <T> T
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
     * @return long
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
     * @param <T> T
     * @return T
     */
    <T extends Serializable> List<T> lrange(String key);

    /**
     * 列举指定区间元素
     * @param key 存储list的key
     * @param start 元素下标
     * @param end 元素下标；-1代表倒数一个元素，-2代表倒数第二个元素
     * @param <T> T
     * @return 区间元素集合
     */
    <T extends Serializable> List<T> lrange(String key, long start, long end);

    /**
     * 设置List缓存
     * @param key key
     * @param value list
     * @param <T> T
     * @return long
     */
    <T extends Serializable> long setList(String key, List<T> value);

    /**
     * 设置List缓存
     * @param key 键
     * @param value 值
     * @param cacheSeconds 超时时间，0为不超时
     * @param <T> T
     * @return T
     */
    <T extends Serializable> long setList(String key, List<T> value, int cacheSeconds);

    /**
     * 设置Map缓存
     * @param key 键
     * @param value 值
     * @param <T> T
     * @return T
     */
    <T extends Serializable> String hmset(String key, Map<String, T> value);

    /**
     * 设置Map缓存
     * @param key 键
     * @param value 值
     * @param <T> T
     * @param cacheSeconds 超时时间，0为不超时
     * @return T
     */
    <T extends Serializable> String hmset(String key, Map<String, T> value, int cacheSeconds);

    /**
     * 向Map缓存中添加值
     * @param key 键
     * @param value 值
     * @param mKey map key
     * @param <T> T
     * @return T
     */
    <T extends Serializable> long hPut(String key, String mKey, T value);

    /**
     * 获取Map缓存
     * @param key 键
     * @param <T> T
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
     * @return long
     */
    long hDel(String key, String mapKey);

    /**
     * 判断Map缓存中的Key是否存在
     * @param key 键
     * @param field map键
     * @return boolean
     */
    boolean hexists(String key, String field);

    /**
     * 缓存是否存在
     * @param key 键
     * @return boolean
     */
    boolean exists(String key);

    /**
     * 通过key 对value进行加值+1操作,当value不是int类型时会返回错误,当key不存在是则value为1
     * @param key key
     * @return +1 后的值
     */
    Long incr(String key);

    /**
     * 通过key给指定的value加值,如果key不存在,则这时value为该值
     * @param key key
     * @param integer v
     * @return Long
     */
    Long incrBy(String key, long integer);

    /**
     * 对key的值做减减操作,如果key不存在,则设置key为-1
     * @param key key
     * @return -1后的值
     */
    Long decr(String key);

    /**
     * 减去指定的值
     * @param key k
     * @param integer v
     * @return 减去后的值
     */
    Long decrBy(String key, long integer);
}
