package com.redis.adapter;

import com.redis.config.RedisSentinelConfig;
import com.redis.util.CommonUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.*;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@Log4j2
public class RedisSentinelAdapter {
    //        implements DisposableBean
    private static final String WORK_QUEUE_POSTFIX = "_WORK";
    //	them prefix de tranh trung voi proj khac
    private static final String PREFIX_NAME = "NSLD_";
    private static JedisSentinelPool jedisPool = null;
    private static final int DEFAULT_TIME_OUT = 10;

    @Autowired
    private RedisSentinelConfig redisSentinelConfig;

    private Set<String> sentinelUri = new HashSet<>();

    private JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();

    @PostConstruct
    public void init() {
        jedisPoolConfig.setMaxIdle(redisSentinelConfig.getMaxIdle());
        jedisPoolConfig.setMaxTotal(redisSentinelConfig.getMaxTotal());
        jedisPoolConfig.setMinIdle(redisSentinelConfig.getMinIdle());
        jedisPoolConfig.setMaxWait(Duration.ofMillis(redisSentinelConfig.getMaxWait()));//đơn vị: millis

        sentinelUri.clear();
        redisSentinelConfig.getAddress().forEach(u ->
                sentinelUri.add(u.getUri())
        );
    }

    private JedisSentinelPool getRedisPool() {
        if (jedisPool == null) {
            //JedisSentinelPool(String masterName, Set<String> sentinels, GenericObjectPoolConfig<Jedis> poolConfig, int timeout, String password, int database)
            jedisPool = new JedisSentinelPool(redisSentinelConfig.getMasterName(), sentinelUri, jedisPoolConfig, redisSentinelConfig.getTimeout(), redisSentinelConfig.getPassword(), redisSentinelConfig.getDatabase());
        }
        return jedisPool;
    }

    public long sendJobToWaitQueue(String queue, String job) {
        Jedis jedis = null;
        try {
            jedis = getRedisPool().getResource();
            return jedis.lpush(queue, job);
        } catch (Exception e) {
            log.error("getJobAndSendToWorkQueue redis error : " + e.getMessage());
            return -1;
        } finally {
            returnToPool(jedis);
        }
    }

    public String getJobAndSendToWorkQueue(String queue) {
        Jedis jedis = null;
        try {
            jedis = getRedisPool().getResource();
            // set timeout to release connection will make server more available , event it
            // shutdown
            return jedis.brpoplpush(queue, queue + WORK_QUEUE_POSTFIX, DEFAULT_TIME_OUT);
        } catch (Exception e) {
            log.error("getJobAndSendToWorkQueue redis error : " + e.getMessage());
            return null;
        } finally {
            returnToPool(jedis);
        }
    }

    public <T> boolean set(String key, long expireInSeconds, T value) {
        if (CommonUtil.isNullOrEmpty(key)) {
            return false;
        }
        String str = CommonUtil.beanToString(value);
        Jedis jedis = null;
        try {
            jedis = getRedisPool().getResource();
            if (expireInSeconds < 1) {
                jedis.set(PREFIX_NAME + key, str);
            } else {
                jedis.setex(PREFIX_NAME + key, expireInSeconds, str);
            }
            return true;
        } catch (Exception e) {
            log.error("Set redis error : " + e.getMessage());
            return false;
        } finally {
            returnToPool(jedis);
        }
    }

    public <T> T get(String key, Class<T> clazz) {
        Jedis jedis = null;
        try {
            jedis = getRedisPool().getResource();
            String str = jedis.get(PREFIX_NAME + key);
            return CommonUtil.stringToBean(str, clazz);
        } catch (Exception e) {
            log.error("Get redis error : " + e.getMessage());
            return null;
        } finally {
            returnToPool(jedis);
        }
    }

    public boolean exists(String key) {
        Jedis jedis = null;
        try {
            jedis = getRedisPool().getResource();
            return jedis.exists(PREFIX_NAME + key);
        } catch (Exception e) {
            log.error("exists redis error : " + e.getMessage());
            return false;
        } finally {
            returnToPool(jedis);
        }
    }

    public boolean delete(String key) {
        Jedis jedis = null;
        try {
            jedis = getRedisPool().getResource();
            long ret = jedis.del(PREFIX_NAME + key);
            return ret > 0;
        } catch (Exception e) {
            log.error("delete redis error : " + e.getMessage());
            return false;
        } finally {
            returnToPool(jedis);
        }
    }

    public boolean deleteByPattern(String pattern) {
        Jedis jedis = null;
        try {
            jedis = getRedisPool().getResource();
            Set<String> matchingKeys = new HashSet<>();
            ScanParams params = new ScanParams();
            params.match("*" + pattern + "*");

            String nextCursor = "0";

            do {
                ScanResult<String> scanResult = jedis.scan(nextCursor, params);
                List<String> keys = scanResult.getResult();
                nextCursor = scanResult.getCursor();

                matchingKeys.addAll(keys);

            } while (!nextCursor.equals("0"));

            if (matchingKeys.isEmpty()) {
                return false;
            }

            long ret = jedis.del(matchingKeys.toArray(new String[matchingKeys.size()]));
            return ret > 0;
        } catch (Exception e) {
            log.error("delete redis error : " + e.getMessage());
            return false;
        } finally {
            returnToPool(jedis);
        }
    }

    public Long returnJobBackToWaitQueue(String queue, String job) {
        Long count = removeJobFromWorkQueue(queue, job);
        if (count > 0) {
            sendJobToWaitQueue(queue, job);
        }
        return count;
    }

    public Long removeJobFromWorkQueue(String queue, String job) {
        Jedis jedis = null;
        try {
            jedis = getRedisPool().getResource();
            return jedis.lrem(queue, -1, job);
        } finally {
            returnToPool(jedis);
        }
    }

    public Long getTimeToLive(String key) {
        Jedis jedis = null;
        try {
            jedis = getRedisPool().getResource();
            return jedis.ttl(PREFIX_NAME + key);
        } catch (Exception e) {
            log.error("Get redis error : " + e.getMessage());
            return null;
        } finally {
            returnToPool(jedis);
        }
    }

    private void returnToPool(Jedis jedis) {
        if (jedis != null) {
            jedis.close();
        }
    }

    //    @Override
//    public void destroy() {
//        if (jedisPool != null) {
//            jedisPool.close();
//        }
//    }
}