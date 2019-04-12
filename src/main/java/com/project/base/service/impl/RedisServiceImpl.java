package com.project.base.service.impl;


import com.project.base.model.RedisClient;
import com.project.base.service.IRedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.ShardedJedis;

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
    public void set(String key, String value) {
        this.jedis.set(key, value);
    }

    @Override
    public void set(String key, String value, long seconds) {
        // NX是不存在时才set， XX是存在时才set， EX是秒，PX是毫秒
        if (jedis.exists(key)) {
            jedis.set(key, value, "XX", "EX", seconds);
        } else {
            jedis.set(key, value, "NX", "EX", seconds);
        }
        jedis.close();
    }

    @Override
    public String get(String key) {
        return jedis.get(key);
    }
}
