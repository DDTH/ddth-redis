package com.github.ddth.redis.impl;

import org.apache.commons.pool2.PooledObjectFactory;

import com.github.ddth.redis.PoolConfig;
import com.github.ddth.redis.RedisClientPool;

/**
 * Apache commons-pool2 of {@link JedisRedisClient} instances.
 * 
 * @author Thanh Ba Nguyen <btnguyen2k@gmail.com>
 * @since 0.1.0
 */
public class JedisClientPool extends RedisClientPool<JedisRedisClient> {

    public JedisClientPool(PooledObjectFactory<JedisRedisClient> factory, PoolConfig poolConfig) {
        super(factory, poolConfig);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JedisRedisClient borrowObject() throws Exception {
        JedisRedisClient redisClient = (JedisRedisClient) super.borrowObject();
        if (redisClient != null) {
            redisClient.setRedisClientPool(this);
        }
        return redisClient;
    }
}
