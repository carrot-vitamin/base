package com.project.base.service.impl;


import com.project.base.model.Constants;
import com.project.base.service.IRedisService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisServiceImpl implements IRedisService {

    private static Logger logger = LoggerFactory.getLogger(RedisServiceImpl.class);

    private static Jedis jedis;

    /**
     * 初始化Redis连接池
     */
    static {
        JedisPoolConfig config = new JedisPoolConfig();
        //可用连接实例的最大数目，默认值为8
        //如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
        config.setMaxTotal(1024);
        //控制一个pool最多有多少个状态为idle(空闲的)的jedis实例，默认值也是8。
        config.setMaxIdle(200);
        //等待可用连接的最大时间，单位毫秒，默认值为-1，表示永不超时。如果超过等待时间，则直接抛出JedisConnectionException；
        config.setMaxWaitMillis(10000);
        //在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
        config.setTestOnBorrow(true);
        JedisPool jedisPool;
        if (StringUtils.isNoneBlank(Constants.getRedisPassword())) {
            jedisPool = new JedisPool(config, Constants.getRedisHost(), Constants.getRedisPort(), 10000, Constants.getRedisPassword());
        } else {
            jedisPool = new JedisPool(config, Constants.getRedisHost(), Constants.getRedisPort(), 10000);
        }
        jedis = jedisPool.getResource();
        logger.info("init common redis success... ...");
    }

    @Override
    public void set(String key, String value) {
        jedis.set(key, value);
        jedis.close();
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
