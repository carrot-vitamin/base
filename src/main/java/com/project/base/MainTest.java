package com.project.base;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.project.base.util.JSONUtils;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MainTest {

    @JsonProperty(value = "test001")
    private String name;

    public static void main(String[] args) throws Exception {
        MainTest m = new MainTest();
        m.name = "abc";
        String s = JSONUtils.toJson(m);
        System.out.println(s);


        MainTest t = JSONUtils.toJava("{\"test0012\":\"abc\"}", MainTest.class);
        System.out.println(t.name);

        Map<String, Object> map = JSONUtils.toJava("{\"test0012\":\"abc\"}", new TypeReference<Map<String, Object>>() {
        });
        System.out.println(map);
    }



}
