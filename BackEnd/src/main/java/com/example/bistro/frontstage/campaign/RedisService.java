package com.example.bistro.frontstage.campaign;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisService {
    private static final String UNLUCKY_COUNT_PREFIX = "unlucky:count:";
    private static final long EXPIRE_TIME = 30; 
    
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    
    public int getUnluckyCount(Integer memberId) {
        String key = UNLUCKY_COUNT_PREFIX + memberId;
        String count = redisTemplate.opsForValue().get(key);
        return count != null ? Integer.parseInt(count) : 0;
    }
    
    public void incrementUnluckyCount(Integer memberId) {
        String key = UNLUCKY_COUNT_PREFIX + memberId;
        redisTemplate.opsForValue().increment(key, 1);
        redisTemplate.expire(key, EXPIRE_TIME, TimeUnit.DAYS);
    }
    
    public void resetUnluckyCount(Integer memberId) {
        String key = UNLUCKY_COUNT_PREFIX + memberId;
        redisTemplate.delete(key);
    }
}
