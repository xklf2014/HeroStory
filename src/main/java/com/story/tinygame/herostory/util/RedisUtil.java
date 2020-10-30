package com.story.tinygame.herostory.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @Author story
 * @CreateTIme 2020/10/29
 * Redis 工具类
 **/
public final class RedisUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisUtil.class);

    /**
     * redis 连接池
     */
    private static JedisPool jedisPool = null;

    private RedisUtil() {
    }

    public static void init() {
        try {
            jedisPool = new JedisPool("127.0.0.1", 6379);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    /**
     * 获取redis实例
     * @return Jedis
     */
    public static Jedis getRedis(){
        if (jedisPool == null) throw new RuntimeException("jedisPool尚未初始化");

        Jedis redis = jedisPool.getResource();

        return redis;
    }

}
