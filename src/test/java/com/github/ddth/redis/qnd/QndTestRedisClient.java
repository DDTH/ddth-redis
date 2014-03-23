package com.github.ddth.redis.qnd;

import com.github.ddth.redis.IRedisClient;
import com.github.ddth.redis.RedisClientFactory;

public class QndTestRedisClient {

    public static void main(String[] args) {
        RedisClientFactory factory = RedisClientFactory.newFactory();
        try {
            IRedisClient client = factory.getRedisClient("localhost");
            try {
                client.set("user:1", "hello", 30);
            } finally {
                client.close();
            }
        } finally {
            factory.destroy();
        }
    }

}
