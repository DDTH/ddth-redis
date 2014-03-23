package com.github.ddth.redis.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;

import com.github.ddth.redis.PoolConfig;
import com.github.ddth.redis.RedisClientFactory;

/**
 * Redis client factory that uses {@link Jedis
 * https://github.com/xetorthio/jedis} as the underlying Redis client library.
 * 
 * @author Thanh Ba Nguyen <btnguyen2k@gmail.com>
 * @since 0.1.0
 */
public class JedisClientFactory extends RedisClientFactory {

    private final Logger LOGGER = LoggerFactory.getLogger(RedisClientFactory.class);

    /**
     * {@inheritDoc}
     */
    @Override
    protected JedisClientPool createRedisClientPool(String host, int port, String username,
            String password, PoolConfig poolConfig) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Building a Redis client pool {host:" + host + ";port:" + port
                    + ";username:" + username + "}...");
        }
        JedisClientPoolableObjectFactory factory = new JedisClientPoolableObjectFactory(host, port,
                username, password);
        JedisClientPool redisClientPool = new JedisClientPool(factory, poolConfig);
        redisClientPool.init();
        return redisClientPool;
    }

}
