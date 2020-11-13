package com.project.base;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.base.util.HttpUtils;
import com.project.base.util.JSONUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MainTest {

    @JsonProperty(value = "test001")
    private String name;

    public static void main(String[] args) throws Exception {
        Map<String, Object> map = new HashMap(2);
        map.put("id", 1L);
        map.put("name", "张三");
        map.put("age", 18);
        String json = JSONUtils.toJson(map);

        Map<String, String> headers = new HashMap<>(2);
        headers.put("h1", "v1");
        headers.put("h2", "v2");


//        System.out.println(HttpGetUtils.get("http://localhost:8080/get", headers));
//
//        System.out.println(HttpPostUtils.postForm("http://localhost:8080/postForm", "id=10&name=张三&age=18", headers));
//
//        System.out.println(HttpPostUtils.postJson("http://localhost:8080/postJson", json, headers));


        Map<String, File> map1 = new HashMap<>(2);
        map1.put("file", new File("C:\\Users\\shaob\\Downloads\\直辖组员活动量统计 (1).xlsx"));
        System.out.println(HttpUtils.postBinary("http://localhost:8080/file/upload", map1));

    }



}
