package com.project.base.service.impl;


import com.project.base.model.RedisClient;
import com.project.base.service.IRedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.ShardedJedis;

import java.util.List;
import java.util.Set;

public class RedisServiceImpl implements IRedisService {

    private static Logger logger = LoggerFactory.getLogger(RedisServiceImpl.class);

    private Jedis jedis;

    private ShardedJedis shardedJedis;

    public RedisServiceImpl() {
        RedisClient redisClient = new RedisClient();
        this.jedis = redisClient.getJedis();
        this.shardedJedis = redisClient.getShardedJedis();
    }

    public RedisServiceImpl(String host, Integer port, String password) {
        RedisClient redisClient = new RedisClient(host, port, password);
        this.jedis = redisClient.getJedis();
        this.shardedJedis = redisClient.getShardedJedis();
    }

    @Override
    public String set(String key, String value) {
        return this.jedis.set(key, value);
    }

    @Override
    public String set(String key, String value, long seconds) {
        String result;
        // NX是不存在时才set， XX是存在时才set， EX是秒，PX是毫秒
        if (this.jedis.exists(key)) {
            result = this.jedis.set(key, value, "XX", "EX", seconds);
        } else {
            result = this.jedis.set(key, value, "NX", "EX", seconds);
        }
        return result;
    }

    @Override
    public String get(String key) {
        return this.jedis.get(key);
    }

    @Override
    public String expire(String key, long seconds) {
        if (this.jedis.exists(key)) {
            return this.jedis.set(key, this.jedis.get(key), "XX", "EX", seconds);
        }
        return null;
    }

    @Override
    public Set<String> keys() {
        return this.jedis.keys("*");
    }

    @Override
    public Long ttl(String key) {
        return this.jedis.ttl(key);
    }

    @Override
    public Long persist(String key) {
        return this.jedis.persist(key);
    }

    @Override
    public String type(String key) {
        return this.jedis.type(key);
    }

    @Override
    public Long lpush(String key, String value) {
        return this.jedis.lpush(key, value);
    }

    @Override
    public List<String> lrange(String key) {
        return this.jedis.lrange(key, 0, -1);
    }

    @Override
    public List<String> lrange(String key, long start, long end) {
        return this.jedis.lrange(key, start, end);
    }

    @Override
    public Long lrem(String key, long count, String value) {
        return this.jedis.lrem(key, count, value);
    }

    @Override
    public String ltrim(String key, long start, long end) {
        return this.jedis.ltrim(key, start, end);
    }

    @Override
    public String lpop(String key) {
        return this.jedis.lpop(key);
    }

    @Override
    public Long llen(String key) {
        return this.jedis.llen(key);
    }

    @Override
    public String lindex(String key, long index) {
        return this.jedis.lindex(key, index);
    }

    @Override
    public Long sadd(String key, String...value) {
        return this.jedis.sadd(key, value);
    }

    @Override
    public Set<String> smembers(String key) {
        return this.jedis.smembers(key);
    }

    @Override
    public Long srem(String key, String... value) {
        return this.jedis.srem(key, value);
    }

    @Override
    public Boolean sismember(String key, String value) {
        return this.jedis.sismember(key, value);
    }

    @Override
    public Set<String> sinter(String set1, String set2) {
        return this.jedis.sinter(set1, set2);
    }

    @Override
    public Set<String> sunion(String set1, String set2) {
        return this.jedis.sunion(set1, set2);
    }

    @Override
    public Set<String> sdiff(String set1, String set2) {
        return this.jedis.sdiff(set1, set2);
    }

    @Override
    public Long hset(String hKey, String key, String value) {
        return this.jedis.hset(hKey, key, value);
    }

    @Override
    public Long hincrBy(String hKey, String key, long value) {
        return this.jedis.hincrBy(hKey, key, value);
    }

    @Override
    public Set<String> hkeys(String hKey) {
        return this.jedis.hkeys(hKey);
    }

    @Override
    public List<String> hvals(String hKey) {
        return this.jedis.hvals(hKey);
    }

    @Override
    public Long hdel(String hKey, String... fields) {
        return this.jedis.hdel(hKey, fields);
    }

    @Override
    public Boolean hexists(String hKey, String field) {
        return this.jedis.hexists(hKey, field);
    }

    @Override
    public String hget(String hKey, String field) {
        return this.jedis.hget(hKey, field);
    }

    @Override
    public List<String> hmget(String hKey, String... value) {
        return this.jedis.hmget(hKey, value);
    }

    @Override
    public Long incr(String key) {
        return this.jedis.incr(key);
    }

    @Override
    public Long decr(String key) {
        return this.jedis.decr(key);
    }
}
