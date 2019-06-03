package com.phantom.seckill.common;


import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.List;
import java.util.Set;

/**
 * redis工具类
 */

@Component
public class RedisAdapter implements InitializingBean {

    private JedisPool jedisPool;

    @Override
    public void afterPropertiesSet() throws Exception {
        jedisPool = new JedisPool();
    }

    public Long zadd(String key, double score, String member) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.zadd(key, score, member);
        }
    }

    public Boolean exists(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.exists(key);
        }
    }
    public  Long del(String key){
        try(Jedis jedis = jedisPool.getResource()) {
            return jedis.del(key);
        }
    }

    public Long expire(String key, int seconds) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.expire(key, seconds);
        }
    }

    public String setex(String key, String value, int seconds) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.setex(key, seconds, value);
        }
    }

    public String set(String key, String value) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.set(key, value);
        }
    }

    public List<String> lrange(String key, long start, long stop) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.lrange(key, start, stop);
        }
    }

    public Set<String> zrange(String key, long start, long stop) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.zrange(key, start, stop);
        }
    }

    public Set<String> zrangebyscore(String key, String min, String max) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.zrangeByScore(key, min, max);
        }
    }

    public Set<String> zrevrangebyscore(String key, String max, String min) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.zrevrangeByScore(key, max, min);
        }
    }

    public Double zscore(String key, String member) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.zscore(key, member);
        }
    }

    public Long zcard(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.zcard(key);
        }
    }


    public List<String> blpop(String... key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.blpop(0, key);
        }
    }

    public Long lpush(String key, String... s) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.lpush(key, s);
        }
    }

    public Long sadd(String key, String... members) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.sadd(key, members);
        }
    }

    public Long srem(String key, String... members) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.srem(key, members);
        }
    }

    public Long scard(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.scard(key);
        }
    }

    public Boolean sismember(String key, String member) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.sismember(key, member);
        }
    }

    public Long incr(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.incr(key);
        }
    }

    public Long decr(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.decr(key);
        }
    }

    public String get(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.get(key);
        }
    }
}
