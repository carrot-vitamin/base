package com.project.base;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.base.util.CacheUtils;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MainTest {

    @JsonProperty(value = "abc")
    private String name;

    public MainTest(String name) {
        this.name = name;
    }

    public static void main(String[] args) throws Exception {
        CacheUtils.resetFilePath("C:\\Users\\shaob\\Downloads");

        CacheUtils.set("k1", "v1", 5);

        String v = CacheUtils.get("k1");
        System.out.println("1================" + v);

        Thread.sleep(8000);

        String v2 = CacheUtils.get("k1");
        System.out.println("2================" + v2);

        CacheUtils.expire("k1", 5);

        String v3 = CacheUtils.get("k1");
        System.out.println("3================" + v3);

        Thread.sleep(8000);

        String v4 = CacheUtils.get("k1");
        System.out.println("4================" + v4);
    }



}
