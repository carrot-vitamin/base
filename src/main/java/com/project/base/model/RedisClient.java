package com.project.base.model;

import org.apache.commons.lang3.StringUtils;
import redis.clients.jedis.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yinshaobo001
 * 2019/4/12 17:50
 */
public class RedisClient {

    /**
     * 非切片额客户端连接
     */
    private Jedis jedis;

    /**
     * 非切片连接池
     */
    private JedisPool jedisPool;

    /**
     * 切片额客户端连接
     */
    private ShardedJedis shardedJedis;

    /**
     * 切片连接池
     */
    private ShardedJedisPool shardedJedisPool;

    private String host;

    private Integer port;

    private String password;

    public RedisClient() {
        this(Constants.LOCALHOST, Constants.DEFAULT_REDIS_PORT);
    }

    public RedisClient(String password) {
        this(Constants.LOCALHOST, Constants.DEFAULT_REDIS_PORT, password);
    }

    public RedisClient(String host, Integer port) {
        this(host, port, null);
    }

    public RedisClient(String host, Integer port, String password) {
        this.host = host;
        this.port = port;
        this.password = password;
        this.init();
    }

    private void init () {
        initialPool();
        initialShardedPool();
    }

    public Jedis getJedis() {
        return jedisPool.getResource();
    }

    public void setJedis(Jedis jedis) {
        this.jedis = jedis;
    }

    public ShardedJedis getShardedJedis() {
        return shardedJedisPool.getResource();
    }

    public void setShardedJedis(ShardedJedis shardedJedis) {
        this.shardedJedis = shardedJedis;
    }

    /**
     * 初始化非切片池
     */
    private void initialPool() {
        // 池基本配置
        JedisPoolConfig config = new JedisPoolConfig();
        //可用连接实例的最大数目，默认值为8
        //如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
        config.setMaxTotal(1024);
        //等待可用连接的最大时间，单位毫秒，默认值为-1，表示永不超时。如果超过等待时间，则直接抛出JedisConnectionException；
        config.setMaxWaitMillis(10 * 1000L);
        //控制一个pool最多有多少个状态为idle(空闲的)的jedis实例，默认值也是8。
        config.setMaxIdle(200);
        //在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
        config.setTestOnBorrow(false);
        if (StringUtils.isNotBlank(this.password)) {
            jedisPool = new JedisPool(config, this.host, this.port, 10 * 1000, this.password);
        } else {
            jedisPool = new JedisPool(config, this.host, this.port);
        }
    }

    /**
     * 初始化切片池
     */
    private void initialShardedPool() {
        // 池基本配置
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(1024);
        config.setMaxWaitMillis(10000L);
        config.setMaxIdle(200);
        config.setTestOnBorrow(false);
        // slave链接
        List<JedisShardInfo> shards = new ArrayList<>();
        JedisShardInfo jedisShardInfo = new JedisShardInfo("127.0.0.1", 6379, "master");
        if (StringUtils.isNotBlank(this.password)) {
            jedisShardInfo.setPassword(this.password);
        }
        shards.add(jedisShardInfo);
        // 构造池
        shardedJedisPool = new ShardedJedisPool(config, shards);
    }
}
