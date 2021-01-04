package com.project.base.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author za-yinshaobo at 2021/1/4 14:52
 * 缓存对象
 */
public class ExpireObject<T extends Serializable> implements Serializable {

    private static final long serialVersionUID = -4550689753581277824L;

    /**
     * 缓存key
     */
    private String key;

    /**
     * 缓存value
     */
    private T t;

    /**
     * 失效时间
     */
    private Date expireTime;

    public ExpireObject(String key, T t, Date expireTime) {
        this.key = key;
        this.t = t;
        this.expireTime = expireTime;
    }

    public ExpireObject() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public T getT() {
        return t;
    }

    public void setT(T t) {
        this.t = t;
    }

    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }

    @Override
    public String toString() {
        return "ExpireObject{" +
                "key='" + key + '\'' +
                ", t=" + t +
                ", expireTime=" + new SimpleDateFormat("yyyy-MM-dd HH:mm:sss").format(this.expireTime) +
                '}';
    }
}
