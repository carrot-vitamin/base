package com.project.base.util;

import com.alibaba.fastjson.JSONObject;
import com.project.base.model.BaiduTokenRequest;
import com.project.base.service.IHttpService;
import com.project.base.service.impl.HttpServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yin
 * @desc BaiduUtils
 * @date 2018/12/29 20:43
 */
public class BaiduUtils {

    private static Logger logger = LoggerFactory.getLogger(BaiduUtils.class);

    private static IHttpService httpService = new HttpServiceImpl();

    public static String getAccessToken(BaiduTokenRequest request) {
        Map<String, Object> params = new HashMap<>(16);
        params.put("grant_type", request.getGrantType());
        params.put("client_id", request.getAppKey());
        params.put("client_secret", request.getSecretKey());
        String result;
        try {
            logger.info("get baidu access token, request={}", request);
            result = httpService.get("https://openapi.baidu.com/oauth/2.0/token", params);
        } catch (Exception e) {
            logger.error("get baidu access token failure... ...", e);
            throw new RuntimeException("get baidu access token failure... ...");
        }
        logger.info("get baidu access_token response={}", result);
        JSONObject jsonObject = JSONObject.parseObject(result);
        return jsonObject.getString("access_token");
    }
}
