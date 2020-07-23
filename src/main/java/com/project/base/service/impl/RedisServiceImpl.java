package com.project.base.service.impl;


import com.project.base.model.Constants;
import com.project.base.model.RedisClient;
import com.project.base.service.IRedisService;
import com.project.base.util.SerializationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.*;

public class RedisServiceImpl implements IRedisService {

    private static Logger logger = LoggerFactory.getLogger(RedisServiceImpl.class);

    private static final String CACHE_PREFIX = "BASE:";

    private String prefix;

    private RedisClient redisClient;

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
        this.redisClient = new RedisClient(host, port, password);
        this.prefix = CACHE_PREFIX + prefix;
        logger.info("初始化redis正常！prefix={}, host={}, port={}, password={}", prefix, host, port, password);
    }

    private Jedis getJedis() {
        return redisClient.getJedis();
    }

    private void close(Jedis jedis) {
        if (jedis != null) {
            jedis.close();
        }
    }


    @Override
    public <T extends Serializable> String set(String key, T value) {
        return this.set(key, value, 0);
    }

    @Override
    public <T extends Serializable> String set(String key, T value, int cacheSeconds) {
        Jedis jedis = null;
        String result = null;
        try {
            key = this.prefix + key;
            jedis = this.getJedis();
            result = jedis.set(getBytesKey(key), toBytes(value));
            if (cacheSeconds != 0) {
                jedis.expire(getBytesKey(key), cacheSeconds);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            this.close(jedis);
        }
        return result;
    }

    @Override
    public long expire(String key, int seconds) {
        Jedis jedis = null;
        long result = -1;
        try {
            key = this.prefix + key;
            jedis = this.getJedis();
            if (jedis.exists(getBytesKey(key))) {
                result = jedis.expire(getBytesKey(key), seconds);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            this.close(jedis);
        }
        return result;
    }

    @Override
    public long del(String key) {
        Jedis jedis = null;
        long result = -1;
        try {
            key = this.prefix + key;
            jedis = this.getJedis();
            if (jedis.exists(getBytesKey(key))) {
                result = jedis.del(getBytesKey(key));
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            this.close(jedis);
        }
        return result;
    }

    @Override
    public <T extends Serializable> T get(String key) {
        Jedis jedis = null;
        T value = null;
        try {
            key = this.prefix + key;
            jedis = this.getJedis();
            if (jedis.exists(getBytesKey(key))) {
                value = toObject(jedis.get(getBytesKey(key)));
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            this.close(jedis);
        }
        return value;
    }

    @Override
    public Set<String> keys() {
        Jedis jedis = null;
        Set<String> set = null;
        try {
            jedis = this.getJedis();
            set = jedis.keys("*");;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            this.close(jedis);
        }
        return set;
    }

    @Override
    public <T extends Serializable> List<T> lrange(String key) {
        return this.lrange(key, 0, -1);
    }

    @Override
    public <T extends Serializable> List<T> lrange(String key, long start, long end) {
        List<T> value = null;
        Jedis jedis = null;
        try {
            key = this.prefix + key;
            jedis = this.getJedis();
            if (jedis.exists(getBytesKey(key))) {
                List<byte[]> list = jedis.lrange(getBytesKey(key), start, end);
                value = new ArrayList<>();
                for (byte[] bs : list){
                    value.add(toObject(bs));
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            this.close(jedis);
        }
        return value;
    }

    @Override
    public <T extends Serializable> long setList(String key, List<T> value) {
        return this.setList(key, value, 0);
    }

    @Override
    public <T extends Serializable> long setList(String key, List<T> value, int cacheSeconds) {
        Jedis jedis = null;
        long result = -1;
        try {
            key = this.prefix + key;
            jedis = this.getJedis();
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
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            this.close(jedis);
        }
        return result;
    }

    @Override
    public <T extends Serializable> String hmset(String key, Map<String, T> value) {
        return this.hmset(key, value, 0);
    }

    @Override
    public <T extends Serializable> String hmset(String key, Map<String, T> value, int cacheSeconds) {
        Jedis jedis = null;
        String result = null;
        try {
            key = this.prefix + key;
            jedis = this.getJedis();
            if (jedis.exists(getBytesKey(key))) {
                jedis.del(getBytesKey(key));
            }
            Map<byte[], byte[]> map = new HashMap<>(16);
            for (Map.Entry<String, T> e : value.entrySet()){
                map.put(getBytesKey(e.getKey()), toBytes(e.getValue()));
            }
            result = jedis.hmset(getBytesKey(key), map);
            if (cacheSeconds != 0) {
                jedis.expire(getBytesKey(key), cacheSeconds);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            this.close(jedis);
        }
        return result;
    }

    @Override
    public <T extends Serializable> long hPut(String key, String mKey, T value) {
        Jedis jedis = null;
        long result = -1;
        try {
            key = this.prefix + key;
            jedis = this.getJedis();
            result = jedis.hset(getBytesKey(key), getBytesKey(mKey), getBytesKey(value));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            this.close(jedis);
        }
        return result;
    }

    @Override
    public <T extends Serializable> T hGet(String key, String field) {
        byte [] bytes = null;
        Jedis jedis = null;
        try {
            key = this.prefix + key;
            jedis = this.getJedis();
            bytes = jedis.hget(getBytesKey(key), getBytesKey(field));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            this.close(jedis);
        }
        return toObject(bytes);
    }

    @Override
    public <T extends Serializable> Map<String, T> hGetAll(String key) {
        key = this.prefix + key;
        Map<String, T> value = null;
        Jedis jedis = null;
        try {
            jedis = this.getJedis();
            if (jedis.exists(getBytesKey(key))) {
                value = new HashMap<>(16);
                Map<byte[], byte[]> map = jedis.hgetAll(getBytesKey(key));
                for (Map.Entry<byte[], byte[]> e : map.entrySet()){
                    value.put(new String(e.getKey(), Charset.defaultCharset()), toObject(e.getValue()));
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            this.close(jedis);
        }
        return value;
    }

    @Override
    public long hDel(String key, String mapKey) {
        key = this.prefix + key;
        long result = -1;
        Jedis jedis = null;
        try {
            jedis = this.getJedis();
            result = jedis.hdel(getBytesKey(key), getBytesKey(mapKey));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            this.close(jedis);
        }
        return result;
    }

    @Override
    public boolean hexists(String key, String field) {
        key = this.prefix + key;
        Jedis jedis = null;
        boolean result = false;
        try {
            jedis = this.getJedis();
            result = jedis.hexists(getBytesKey(key), getBytesKey(field));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            this.close(jedis);
        }
        return result;
    }

    @Override
    public boolean exists(String key) {
        key = this.prefix + key;
        boolean result = false;
        Jedis jedis = null;
        try {
            jedis = this.getJedis();
            result = jedis.exists(getBytesKey(key));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            this.close(jedis);
        }
        return result;
    }

    @Override
    public Long incr(String key) {
        key = this.prefix + key;
        Long value = null;
        Jedis jedis = null;
        try {
            jedis = this.getJedis();
            value = jedis.incr(getBytesKey(key));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            this.close(jedis);
        }
        return value;
    }

    @Override
    public Long incrBy(String key, long integer) {
        key = this.prefix + key;
        Long value = null;
        Jedis jedis = null;
        try {
            jedis = this.getJedis();
            value = jedis.incrBy(getBytesKey(key), integer);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            this.close(jedis);
        }
        return value;
    }

    @Override
    public Long decr(String key) {
        key = this.prefix + key;
        Long value = null;
        Jedis jedis = null;
        try {
            jedis = this.getJedis();
            value = jedis.decr(getBytesKey(key));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            this.close(jedis);
        }
        return value;
    }

    @Override
    public Long decrBy(String key, long integer) {
        key = this.prefix + key;
        Long value = null;
        Jedis jedis = null;
        try {
            jedis = this.getJedis();
            value = jedis.decrBy(getBytesKey(key), integer);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            this.close(jedis);
        }
        return value;
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
