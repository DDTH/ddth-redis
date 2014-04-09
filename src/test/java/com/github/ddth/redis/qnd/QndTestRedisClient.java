package com.github.ddth.redis.qnd;

import java.util.List;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.util.SafeEncoder;

import com.github.ddth.redis.IRedisClient;
import com.github.ddth.redis.PoolConfig;
import com.github.ddth.redis.RedisClientFactory;

public class QndTestRedisClient {

    private static int NUM_RUNS = 86400 / 4;
    private static String HOST = "127.0.0.1";

    private static void test1(RedisClientFactory factory) {
        System.out.println("Test: testOnBorrow=false");
        long t1 = System.currentTimeMillis();
        for (int i = 0; i < NUM_RUNS; i++) {
            IRedisClient client = factory.getRedisClient(HOST);
            try {
                client.get("DEMO");
            } finally {
                client.close();
            }
        }
        long t2 = System.currentTimeMillis();
        System.out.println("Get [" + NUM_RUNS + "] times in " + (t2 - t1) + " ms.");
        System.out.println("Speed: " + ((double) NUM_RUNS * 1000) / (t2 - t1) + " gets/sec");
        System.out.println();
    }

    private static void test2(RedisClientFactory factory) {
        System.out.println("Test: single instance");
        IRedisClient client = factory.getRedisClient(HOST);
        long t1 = System.currentTimeMillis();
        try {
            for (int i = 0; i < NUM_RUNS; i++) {
                client.get("DEMO");
            }
        } finally {
            client.close();
        }
        long t2 = System.currentTimeMillis();
        System.out.println("Get [" + NUM_RUNS + "] times in " + (t2 - t1) + " ms.");
        System.out.println("Speed: " + ((double) NUM_RUNS * 1000) / (t2 - t1) + " gets/sec");
        System.out.println();
    }

    private static void test3(RedisClientFactory factory) {
        System.out.println("Test: testOnBorrow=true");
        PoolConfig poolConfig = new PoolConfig().setTestOnBorrow(true).setTestOnCreate(false)
                .setTestWhileIdle(false).setMinIdle(4);
        long t1 = System.currentTimeMillis();
        for (int i = 0; i < NUM_RUNS; i++) {
            IRedisClient client = factory.getRedisClient(HOST, IRedisClient.DEFAULT_REDIS_PORT,
                    null, null, poolConfig);
            try {
                client.get("DEMO");
            } finally {
                client.close();
            }
        }
        long t2 = System.currentTimeMillis();
        System.out.println("Get [" + NUM_RUNS + "] times in " + (t2 - t1) + " ms.");
        System.out.println("Speed: " + ((double) NUM_RUNS * 1000) / (t2 - t1) + " gets/sec");
        System.out.println();
    }

    private static void test4() {
        System.out.println("Test: [raw] single instance");
        Jedis client = new Jedis(HOST);
        long t1 = System.currentTimeMillis();
        try {
            for (int i = 0; i < NUM_RUNS; i++) {
                client.get("DEMO");
            }
        } finally {
            client.close();
        }
        long t2 = System.currentTimeMillis();
        System.out.println("Get [" + NUM_RUNS + "] times in " + (t2 - t1) + " ms.");
        System.out.println("Speed: " + ((double) NUM_RUNS * 1000) / (t2 - t1) + " gets/sec");
        System.out.println();
    }

    private static void test5() {
        System.out.println("Test: [raw/pipeline] single instance");
        Jedis client = new Jedis(HOST);
        long t1 = System.currentTimeMillis();
        try {
            Pipeline p = client.pipelined();
            for (int i = 0; i < NUM_RUNS; i++) {
                p.get(SafeEncoder.encode("DEMO"));
            }
            List<Object> result = p.syncAndReturnAll();
            System.out.println(result);
        } finally {
            client.close();
        }
        long t2 = System.currentTimeMillis();
        System.out.println("Get [" + NUM_RUNS + "] times in " + (t2 - t1) + " ms.");
        System.out.println("Speed: " + ((double) NUM_RUNS * 1000) / (t2 - t1) + " gets/sec");
        System.out.println();
    }

    public static void main(String[] args) {
        RedisClientFactory factory = RedisClientFactory.newFactory();
        try {
            test1(factory);
            test2(factory);
            test3(factory);
            test4();
            test5();
        } finally {
            factory.destroy();
        }
    }

}
