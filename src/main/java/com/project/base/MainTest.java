package com.project.base;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.base.util.JSONUtils;
import com.project.base.util.RSAUtils;

import java.util.HashMap;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MainTest {

    @JsonProperty(value = "test001")
    private String name;

    public static void main(String[] args) throws Exception {
        String url = "http://localhost:8080/testGet";
        Map<String, Object> map = new HashMap(2);
        map.put("id", 1L);
        map.put("name", "张三");
        map.put("age", 18);
        String json = JSONUtils.toJson(map);

        Map<String, String> headers = new HashMap<>(2);
        headers.put("h1", "v1");
        headers.put("h2", "v2");

        String s = RSAUtils.encryptWithPublicKey("hello world", "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDAvyACzLhuzg5zY3auMGkuOOJFbGaoDTXVkpyUBEX+2A4rg1j/z0d8zZ+cq0JhmBs0F9iGoz+KiYyTkYQXsPEMA3MpJ73t55yjNdPudnOVC2NLKcCb+3eFD2ecQRWuhulO20HOhoaJAErsdMviVBimZ/+fvuSpIRoSYUa1ENMcWwIDAQAB");
        System.out.println(s);
        System.out.println(RSAUtils.decryptWithPrivateKey(s, "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAMC/IALMuG7ODnNjdq4waS444kVsZqgNNdWSnJQERf7YDiuDWP/PR3zNn5yrQmGYGzQX2IajP4qJjJORhBew8QwDcyknve3nnKM10+52c5ULY0spwJv7d4UPZ5xBFa6G6U7bQc6GhokASux0y+JUGKZn/5++5KkhGhJhRrUQ0xxbAgMBAAECgYBcmTWKwsl0SkA9BTLWGmHdl+x0x9BFuhr74PSiU69A8JFWLEMMmotQlQSyYsCAXG/tRet7O2BicRR9LLKWIyaZ9M4omOGePY8MghIFwlbF41UR5marDk7O8womVb2N+L+D9XGvYqr99eNljXk9b2bTI8juswyX0zm3nNWw9xX7OQJBAPvBDR+vttYz8LPkWqqwRDmJWOJEIP5E1lJzSlebVsfR1caal4ZIN8OI/UCxWdx/9SU4uYFq/DAteclcgw486K8CQQDD/08HIo65rs0NyEIFX7X7pPCAvTbvGaVEUv4bselPil9a38hYYxBOJ5lZ0woJB+0flgVXP2tC87Y9/kORAdoVAkEAk81HMy8qHJ/p1PNf9438v/rO4Cg3ZpBrc4SURNLCJBYhd7QS+Zc0hevLI0v8AeRtvCiNYFm7LV3Ffl594sIHqQJAZCvdYSEpXv9W799U1thG81kmTjXmmKc7z0K3esIgIzXubEyJYZsn3znf54ezzk/NwmHzUtcn4+0ZTG/ian9OzQJAUDKMnMpZSK3w7DG970JaihzeL6qgqbVdnlX/Hko5OHo6aCbbH/dKqsfyU2qn1mNSo+se+ewZd+uW9Mo6XQl4DA=="));
    }



}
