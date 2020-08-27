package com.project.base;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.base.util.JSONUtils;

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
    }



}
