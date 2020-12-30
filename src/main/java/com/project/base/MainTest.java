package com.project.base;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.project.base.util.URLUtils;

import java.util.HashMap;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MainTest {

    public static void main(String[] args) throws Exception {
        String url = "http://www.baidu.com";
        Map<String, Object> map = new HashMap<>(2);
        map.put("k1", "v1");
        map.put("k2", "v2");
        System.out.println(URLUtils.appendParams(url, map));
    }



}
