package com.project.base.model;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @author yin
 * @desc BaiduTokenRequest
 * @date 2018/12/30 2:52
 */
public class BaiduTokenRequest {

    @JSONField(name = "grant_type")
    private String grantType;

    @JSONField(name = "client_id")
    private String appKey;

    @JSONField(name = "client_secret")
    private String secretKey;

    public String getGrantType() {
        return grantType;
    }

    public void setGrantType(String grantType) {
        this.grantType = grantType;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    @Override
    public String toString() {
        return "BaiduTokenRequest{" +
                "grantType='" + grantType + '\'' +
                ", appKey='" + appKey + '\'' +
                ", secretKey='" + secretKey + '\'' +
                '}';
    }
}
