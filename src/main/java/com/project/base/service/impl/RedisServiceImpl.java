package com.project.base.service.impl;


import com.project.base.model.RedisClient;
import com.project.base.service.IRedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.ShardedJedis;

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
        if (jedis.exists(key)) {
            result = jedis.set(key, value, "XX", "EX", seconds);
        } else {
            result = jedis.set(key, value, "NX", "EX", seconds);
        }
        return result;
    }

    @Override
    public String get(String key) {
        return jedis.get(key);
    }

    @Override
    public String expire(String key, long seconds) {
        if (jedis.exists(key)) {
            return jedis.set(key, jedis.get(key), "XX", "EX", seconds);
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
}
