package com.github.ddth.redis.impl;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.ddth.redis.PoolConfig;

/**
 * Apache commons-pool2 of {@link JedisRedisClient} instances.
 * 
 * @author Thanh Ba Nguyen <btnguyen2k@gmail.com>
 * @since 0.1.0
 */
public class JedisClientPool extends GenericObjectPool<JedisRedisClient> {

    private Logger LOGGER = LoggerFactory.getLogger(JedisClientPool.class);
    private PoolConfig poolConfig;
    private Set<JedisRedisClient> activeClients = new HashSet<JedisRedisClient>();

    public JedisClientPool(PooledObjectFactory<JedisRedisClient> factory, PoolConfig poolConfig) {
        super(factory);
        setPoolConfig(poolConfig);
    }

    public JedisClientPool setPoolConfig(PoolConfig poolConfig) {
        this.poolConfig = poolConfig;
        return this;
    }

    public PoolConfig getPoolConfig() {
        return poolConfig;
    }

    public void init() {
        setTestOnBorrow(true);
        setTestWhileIdle(true);
        setBlockWhenExhausted(true);

        if (poolConfig != null) {
            int maxActive = poolConfig != null ? poolConfig.getMaxActive()
                    : PoolConfig.DEFAULT_MAX_ACTIVE;
            long maxWaitTime = poolConfig != null ? poolConfig.getMaxWaitTime()
                    : PoolConfig.DEFAULT_MAX_WAIT_TIME;
            int maxIdle = poolConfig != null ? poolConfig.getMaxIdle()
                    : PoolConfig.DEFAULT_MAX_IDLE;
            int minIdle = poolConfig != null ? poolConfig.getMinIdle()
                    : PoolConfig.DEFAULT_MIN_IDLE;
            LOGGER.debug("Updating Redis client pool {maxActive:" + maxActive + ";maxWait:"
                    + maxWaitTime + ";minIdle:" + minIdle + ";maxIdle:" + maxIdle + "}...");
            this.setMaxTotal(maxActive);
            this.setMaxIdle(maxIdle);
            this.setMinIdle(minIdle);
            this.setMaxWaitMillis(maxWaitTime);
        }
    }

    public void destroy() {
        close();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() {
        try {
            for (JedisRedisClient client : activeClients) {
                try {
                    invalidateObject(client);
                } catch (Exception e) {
                    LOGGER.warn(e.getMessage(), e);
                }
            }
        } finally {
            super.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JedisRedisClient borrowObject() throws Exception {
        JedisRedisClient redisClient = super.borrowObject();
        if (redisClient != null) {
            redisClient.setRedisClientPool(this);
            activeClients.add(redisClient);
        }
        return redisClient;
    }

    /**
     * {@inheritDoc}
     * 
     * @throws Exception
     */
    @Override
    public void returnObject(JedisRedisClient redisClient) {
        try {
            super.returnObject(redisClient);
        } finally {
            activeClients.remove(redisClient);
        }
    }
}
