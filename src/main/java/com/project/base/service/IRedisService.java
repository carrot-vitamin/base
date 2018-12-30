package com.project.base.service;

public interface IRedisService {

    void set(String key, String value);

    void set(String key, String value, long seconds);

    String get(String key);
}
