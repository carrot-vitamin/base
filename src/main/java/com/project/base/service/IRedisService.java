package com.project.base.service;

public interface IRedisService {

    String set(String key, String value);

    String set(String key, String value, long seconds);

    String get(String key);

    String expire(String key, long seconds);

}
