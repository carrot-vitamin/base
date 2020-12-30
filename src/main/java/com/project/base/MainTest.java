package com.project.base;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.base.util.URLUtils;

import java.util.HashMap;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MainTest {

    @JsonProperty(value = "abc")
    private String name;

    public MainTest(String name) {
        this.name = name;
    }

    public static void main(String[] args) throws Exception {
        Map<String, Object> map = new HashMap<>(2);
        map.put("k1", "v1");
        map.put("k2", 2);
        System.out.println(URLUtils.appendParams("http://local?kk=vv", map));
    }



}
