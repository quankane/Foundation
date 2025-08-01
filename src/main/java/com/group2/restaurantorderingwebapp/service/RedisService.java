package com.group2.restaurantorderingwebapp.service;

import org.springframework.stereotype.Service;

import java.util.List;
public interface RedisService {

    void set(String key, Object value) ;
    Object get(String key) ;
    void setHashRedis(String key,  String field, Object value) ;

    void setHashRedis(String key, String field, Object value, Long timeToLive);

    Object getHash(String key, String field) ;
    List<Object> getAll(String key) ;
    void delete(String key) ;
    String convertToJson(Object t) ;
    <T>T convertToObject(String json, Class<T> t );
    void deleteAll(String key) ;
    void delete(String key, String field) ;
    void setTTL(String key, Long timeToLive);

    void flushAll();
}
