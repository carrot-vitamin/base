package com.project.base.service.impl;


import com.project.base.model.Constants;
import com.project.base.model.RedisClient;
import com.project.base.service.IRedisService;
import org.apache.commons.lang3.SerializationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.ShardedJedis;

import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.*;

public class RedisServiceImpl implements IRedisService {

    private static Logger logger = LoggerFactory.getLogger(RedisServiceImpl.class);

    private Jedis jedis;

    private ShardedJedis shardedJedis;

    private static final String CACHE_PREFIX = "BASE:";

    private String prefix;

    public RedisServiceImpl(String prefix) {
        this(prefix, Constants.LOCALHOST, Constants.DEFAULT_REDIS_PORT);
    }

    public RedisServiceImpl(String prefix, String password) {
        this(prefix, Constants.LOCALHOST, Constants.DEFAULT_REDIS_PORT, password);
    }

    public RedisServiceImpl(String prefix, String host, Integer port) {
        this(prefix, host, port, null);
    }

    public RedisServiceImpl(String prefix, String host, Integer port, String password) {
        RedisClient redisClient = new RedisClient(host, port, password);
        this.jedis = redisClient.getJedis();
        this.shardedJedis = redisClient.getShardedJedis();
        this.prefix = CACHE_PREFIX + prefix;
        logger.info("初始化redis正常！prefix={}, host={}, port={}, password={}", prefix, host, port, password);
    }


    @Override
    public <T extends Serializable> String set(String key, T value) {
        return this.set(key, value, 0);
    }

    @Override
    public <T extends Serializable> String set(String key, T value, int cacheSeconds) {
        key = this.prefix + key;
        String result = jedis.set(getBytesKey(key), toBytes(value));
        if (cacheSeconds != 0) {
            jedis.expire(getBytesKey(key), cacheSeconds);
        }
        return result;
    }

    @Override
    public long expire(String key, int seconds) {
        key = this.prefix + key;
        long result = -1;
        if (jedis.exists(getBytesKey(key))) {
            result = jedis.expire(getBytesKey(key), seconds);
        }
        return result;
    }

    @Override
    public long del(String key) {
        key = this.prefix + key;
        long result = -1;
        if (jedis.exists(getBytesKey(key))) {
            result = jedis.del(getBytesKey(key));
        }
        return result;
    }

    @Override
    public <T extends Serializable> T get(String key) {
        key = this.prefix + key;
        T value = null;
        if (jedis.exists(getBytesKey(key))) {
            value = toObject(jedis.get(getBytesKey(key)));
        }
        return value;
    }

    @Override
    public Set<String> keys() {
        return this.jedis.keys("*");
    }

    @Override
    public <T extends Serializable> List<T> lrange(String key) {
        return this.lrange(key, 0, -1);
    }

    @Override
    public <T extends Serializable> List<T> lrange(String key, long start, long end) {
        key = this.prefix + key;
        List<T> value = null;
        if (jedis.exists(getBytesKey(key))) {
            List<byte[]> list = jedis.lrange(getBytesKey(key), start, end);
            value = new ArrayList<>();
            for (byte[] bs : list){
                value.add(toObject(bs));
            }
        }
        return value;
    }

    @Override
    public <T extends Serializable> long setList(String key, List<T> value) {
        return this.setList(key, value, 0);
    }

    @Override
    public <T extends Serializable> long setList(String key, List<T> value, int cacheSeconds) {
        key = this.prefix + key;
        long result;
        if (jedis.exists(getBytesKey(key))) {
            jedis.del(getBytesKey(key));
        }
        List<byte[]> list = new ArrayList<>();
        for (T o : value){
            list.add(toBytes(o));
        }
        byte[][] bytes = new byte[0][value.size()];
        result = jedis.lpush(getBytesKey(key), list.toArray(bytes));
        if (cacheSeconds != 0) {
            jedis.expire(getBytesKey(key), cacheSeconds);
        }
        return result;
    }

    @Override
    public <T extends Serializable> String hmset(String key, Map<String, T> value) {
        return this.hmset(key, value, 0);
    }

    @Override
    public <T extends Serializable> String hmset(String key, Map<String, T> value, int cacheSeconds) {
        key = this.prefix + key;
        if (jedis.exists(getBytesKey(key))) {
            jedis.del(getBytesKey(key));
        }
        Map<byte[], byte[]> map = new HashMap<>(16);
        for (Map.Entry<String, T> e : value.entrySet()){
            map.put(getBytesKey(e.getKey()), toBytes(e.getValue()));
        }
        String result = jedis.hmset(getBytesKey(key), map);
        if (cacheSeconds != 0) {
            jedis.expire(getBytesKey(key), cacheSeconds);
        }
        return result;
    }

    @Override
    public <T extends Serializable> long hPut(String key, String mKey, T value) {
        key = this.prefix + key;
        return jedis.hset(getBytesKey(key), getBytesKey(mKey), getBytesKey(value));
    }

    @Override
    public <T extends Serializable> T hGet(String key, String field) {
        key = this.prefix + key;
        byte [] bytes = jedis.hget(getBytesKey(key), getBytesKey(field));
        return toObject(bytes);
    }

    @Override
    public <T extends Serializable> Map<String, T> hGetAll(String key) {
        key = this.prefix + key;
        Map<String, T> value = null;
        if (jedis.exists(getBytesKey(key))) {
            value = new HashMap<>(16);
            Map<byte[], byte[]> map = jedis.hgetAll(getBytesKey(key));
            for (Map.Entry<byte[], byte[]> e : map.entrySet()){
                value.put(new String(e.getKey(), Charset.defaultCharset()), toObject(e.getValue()));
            }
        }
        return value;
    }

    @Override
    public long hDel(String key, String mapKey) {
        key = this.prefix + key;
        return jedis.hdel(getBytesKey(key), getBytesKey(mapKey));
    }

    @Override
    public boolean hexists(String key, String field) {
        key = this.prefix + key;
        return jedis.hexists(getBytesKey(key), getBytesKey(field));
    }

    @Override
    public boolean exists(String key) {
        key = this.prefix + key;
        return jedis.exists(getBytesKey(key));
    }

    /**
     * 获取byte[]类型Key
     * @param t t
     * @return byte[]
     */
    private <T extends Serializable> byte[] getBytesKey(T t) {
        byte [] bytes;
        if (t instanceof String) {
            bytes = ((String) t).getBytes(Charset.defaultCharset());
        } else {
            bytes = toBytes(t);
        }
        return bytes;
    }

    /**
     * Object转换byte[]类型
     * @param t t
     * @return byte[]
     */
    private <T extends Serializable> byte[] toBytes(T t){
        return SerializationUtils.serialize(t);
    }

    /**
     * byte[]型转换Object
     * @param bytes bytes
     * @return T
     */
    private <T extends Serializable> T toObject(byte[] bytes){
        return SerializationUtils.deserialize(bytes);
    }
}
