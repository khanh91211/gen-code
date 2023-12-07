package com.redis.ctrl;

import com.redis.adapter.RedisSentinelAdapter;
import com.redis.dto.RedisDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/redis")
public class TestRedisCtrl {
    @Autowired
    private RedisSentinelAdapter adapter;

    @ResponseBody
    @PostMapping(value = "/set")
    public boolean set(@RequestBody RedisDto dto) {
        return adapter.set(dto.getKey(), 1000, dto.getBody());
    }

    @ResponseBody
    @GetMapping(value = "/get")
    public String get(@RequestBody RedisDto dto) {
        return adapter.get(dto.getKey(), String.class);
    }
}
