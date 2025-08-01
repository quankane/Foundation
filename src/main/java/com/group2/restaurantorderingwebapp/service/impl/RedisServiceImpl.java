package com.group2.restaurantorderingwebapp.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.group2.restaurantorderingwebapp.service.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisServiceImpl implements RedisService {
    final RedisTemplate<String, Object> redisTemplate ;
    final ObjectMapper objectMapper ;
    @Override
    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    @Override
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public void setHashRedis(String key, String field, Object value) {
       setHashRedis(key, field, value, 3600000L) ;
    }
    @Override
    public void setHashRedis(String key, String field, Object value, Long timeToLive) {
        redisTemplate.opsForHash().put(key, field, value);
        setTTL(key, timeToLive) ;
    }

    @Override
    public Object getHash(String key, String field) {
        return redisTemplate.opsForHash().get(key, field);
    }

    @Override
    public List<Object> getAll(String key) {
        return redisTemplate.opsForHash().values(key) ;
    }

    @Override
    public void delete(String key) {
        redisTemplate.delete(key) ;
    }

    @SneakyThrows
    @Override
    public String convertToJson(Object t) {
        return objectMapper.writer().writeValueAsString(t) ;
    }
    @Override
    @SneakyThrows
    public <T>T convertToObject(String json, Class<T> t ) {
        return objectMapper.readValue(json ,  t);
    }

    @Override
    public void deleteAll(String key){
        redisTemplate.delete(key) ;
    }

    @Override
    public void delete(String key, String field) {
        redisTemplate.opsForHash().keys("*") ;
    }

    @Override
    public void setTTL(String key, Long timeToLive) {
        redisTemplate.expire(key, timeToLive, TimeUnit.MILLISECONDS);
    }

    @Override
    public void flushAll() {
        redisTemplate.getConnectionFactory().getConnection().flushAll();
    }
}
