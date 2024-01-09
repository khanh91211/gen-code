package com.fw.common.config.adapter;

import com.fw.common.config.RedisConfig;
import com.fw.common.util.CommonUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import redis.clients.jedis.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author thangnq.os redis queue adapter
 */
@Component
@Log4j2
@ConditionalOnProperty(name = "redis.config.type", havingValue = "single")
public class RedisSingleAdapter implements RedisAdapter {
	private static final String WORK_QUEUE_POSTFIX = "_WORK";
//	them prefix de tranh trung voi proj khac
	private static final String PREFIX_NAME = "NSLD_";
	private static JedisPool jedisPool = null;
	private static JedisCluster jedisCluster = null;
	private static final int DEFAULT_TIME_OUT = 10;
	@Autowired
	private RedisConfig redisConfig;

	public long sendJobToWaitQueue(String queue, String job) {
		if (redisConfig.isCluster()) {
			JedisCluster jedisCluster = getJedisCluster();
			return jedisCluster.lpush(queue, job);
		} else {
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
	}

	public String getJobAndSendToWorkQueue(String queue) {
		if (redisConfig.isCluster()) {
			JedisCluster jedisCluster = getJedisCluster();
			return jedisCluster.brpoplpush(queue, queue + WORK_QUEUE_POSTFIX, DEFAULT_TIME_OUT);
		} else {
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

	}

	private JedisCluster getJedisCluster() {
		if (jedisCluster == null) {
			JedisPoolConfig config = new JedisPoolConfig();
			config.setMaxTotal(redisConfig.getMaxTotal());
			config.setMaxIdle(redisConfig.getMaxIdle());
			String[] urlList = redisConfig.getHost().split(",");
			Set<HostAndPort> hostAndPortList = new HashSet<>();
			for (String url : urlList) {
				String[] ss = url.split(":");
				HostAndPort hostAndPort = new HostAndPort(ss[0], Integer.parseInt(ss[1]));
				hostAndPortList.add(hostAndPort);
			}
			jedisCluster = new JedisCluster(hostAndPortList, config);
		}
		return jedisCluster;
	}

	private JedisPool getRedisPool() {
		if (jedisPool == null) {
			JedisPoolConfig config = new JedisPoolConfig();
			jedisPool = new JedisPool(config, redisConfig.getHost(), redisConfig.getPort());
		}
		return jedisPool;
	}

	public <T> boolean set(String key, long expireInSeconds, T value) {
		if (CommonUtil.isNullOrEmpty(key)) {
			return false;
		}
		String str = CommonUtil.beanToString(value);
		if (redisConfig.isCluster()) {
			JedisCluster jedisCluster = getJedisCluster();
			if (expireInSeconds < 1) {
				jedisCluster.set(PREFIX_NAME+key, str);
			} else {
				jedisCluster.setex(PREFIX_NAME+key, expireInSeconds, str);
			}
			return true;
		} else {
			Jedis jedis = null;
			try {
				jedis = getRedisPool().getResource();
				if (expireInSeconds < 1) {
					jedis.set(PREFIX_NAME+key, str);
				} else {
					jedis.setex(PREFIX_NAME+key, expireInSeconds, str);
				}
				return true;
			} catch (Exception e) {
				log.error("Set redis error : " + e.getMessage());
				return false;
			} finally {
				returnToPool(jedis);
			}
		}
	}

	public <T> T get(String key, Class<T> clazz) {
		if (redisConfig.isCluster()) {
			JedisCluster jedisCluster = getJedisCluster();
			String str = jedisCluster.get(PREFIX_NAME+key);
			return CommonUtil.stringToBean(str, clazz);
		} else {
			Jedis jedis = null;
			try {
				jedis = getRedisPool().getResource();
				String str = jedis.get(PREFIX_NAME+key);
				return CommonUtil.stringToBean(str, clazz);
			} catch (Exception e) {
				log.error("Get redis error : " + e.getMessage());
				return null;
			} finally {
				returnToPool(jedis);
			}
		}
	}

	public boolean exists(String key) {
		if (redisConfig.isCluster()) {
			JedisCluster jedisCluster = getJedisCluster();
			return jedisCluster.exists(PREFIX_NAME+key);
		} else {
			Jedis jedis = null;
			try {
				jedis = getRedisPool().getResource();
				return jedis.exists(PREFIX_NAME+key);
			} catch (Exception e) {
				log.error("exists redis error : " + e.getMessage());
				return false;
			} finally {
				returnToPool(jedis);
			}
		}

	}

	public boolean delete(String key) {
		if (redisConfig.isCluster()) {
			JedisCluster jedisCluster = getJedisCluster();
			long ret = jedisCluster.del(PREFIX_NAME+key);
			return ret > 0;
		} else {
			Jedis jedis = null;
			try {
				jedis = getRedisPool().getResource();
				long ret = jedis.del(PREFIX_NAME+key);
				return ret > 0;
			} catch (Exception e) {
				log.error("delete redis error : " + e.getMessage());
				return false;
			} finally {
				returnToPool(jedis);
			}
		}
	}

	public boolean deleteByPattern(String pattern) {
		if (redisConfig.isCluster()) {
			JedisCluster jedisCluster = getJedisCluster();
			Set<String> matchingKeys = new HashSet<>();
			ScanParams params = new ScanParams();
			params.match("*" + pattern + "*");

			String nextCursor = "0";

			do {
				ScanResult<String> scanResult = jedisCluster.scan(nextCursor, params);
				List<String> keys = scanResult.getResult();
				nextCursor = scanResult.getCursor();

				matchingKeys.addAll(keys);

			} while (!nextCursor.equals("0"));

			if (matchingKeys.isEmpty()) {
				return false;
			}

			long ret = jedisCluster.del(matchingKeys.toArray(new String[matchingKeys.size()]));
			return ret > 0;
		} else {
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
	}

	private void returnToPool(Jedis jedis) {
		if (jedis != null) {
			jedis.close();
		}
	}

	public Long returnJobBackToWaitQueue(String queue, String job) {
		Long count = removeJobFromWorkQueue(queue, job);
		if (count > 0) {
			sendJobToWaitQueue(queue, job);
		}
		return count;
	}

//	public Long sendJobToIncompleteQueue(String job) {
//		if (redisConfig.isCluster()) {
//			JedisCluster jedisCluster = getJedisCluster();
//			return jedisCluster.lpush(Constant.GLOBAL_INCOMPLETE_QUEUE, job);
//		} else {
//			Jedis jedis = null;
//			try {
//				jedis = getRedisPool().getResource();
//				return jedis.lpush(Constant.GLOBAL_INCOMPLETE_QUEUE, job);
//			} finally {
//				returnToPool(jedis);
//			}
//		}
//	}

	public Long removeJobFromWorkQueue(String queue, String job) {
		if (redisConfig.isCluster()) {
			JedisCluster jedisCluster = getJedisCluster();
			return jedisCluster.lrem(queue, -1, job);
		} else {
			Jedis jedis = null;
			try {
				jedis = getRedisPool().getResource();
				return jedis.lrem(queue, -1, job);
			} finally {
				returnToPool(jedis);
			}
		}
	}

	public Long getTimeToLive(String key) {
		if (redisConfig.isCluster()) {
			JedisCluster jedisCluster = getJedisCluster();
			return jedisCluster.ttl(PREFIX_NAME+key);
		} else {
			Jedis jedis = null;
			try {
				jedis = getRedisPool().getResource();
				return jedis.ttl(PREFIX_NAME+key);
			} catch (Exception e) {
				log.error("Get redis error : " + e.getMessage());
				return null;
			} finally {
				returnToPool(jedis);
			}
		}
	}
}