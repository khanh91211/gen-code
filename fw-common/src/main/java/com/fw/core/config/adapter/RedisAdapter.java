package com.fw.core.config.adapter;

import org.springframework.stereotype.Component;

@Component
public interface RedisAdapter{

	public <T> boolean set(String key, long expireInSeconds, T value) ;

	public <T> T get(String key, Class<T> clazz) ;

	public boolean exists(String key) ;

	public boolean delete(String key) ;

	public boolean deleteByPattern(String pattern) ;

	public Long returnJobBackToWaitQueue(String queue, String job) ;

	public Long removeJobFromWorkQueue(String queue, String job) ;

	public Long getTimeToLive(String key) ;
}
