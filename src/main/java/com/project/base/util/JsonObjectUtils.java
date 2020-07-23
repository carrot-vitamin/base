package com.project.base.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author yinshaobo at 2020/7/23 17:05
 */
public class JsonObjectUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonObjectUtils.class);

    public static String toJsonString(Object o) {
        String json = "{}";
        if (o != null) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                json = objectMapper.writeValueAsString(o);
            } catch (JsonProcessingException e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
        return json;
    }

    public static <T> T parseObject(String json, Class<T> tClass) {
        return parseJson(json, tClass);
    }

    public static <T> T parseObject(String json, TypeReference<T> typeReference) {
        return parseJson(json, typeReference);
    }

    private static <T> T parseJson(String json, Object o) {
        T t = null;
        if (StringUtils.isNotBlank(json)) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                if (o instanceof Class) {
                    t = (T) objectMapper.readValue(json, (Class) o);
                } else if (o instanceof TypeReference) {
                    t = (T) objectMapper.readValue(json, (TypeReference) o);
                }
            } catch (JsonProcessingException e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
        return t;
    }

}
