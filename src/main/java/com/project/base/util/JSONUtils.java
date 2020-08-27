package com.project.base.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author za-yinshaobo
 * 2020/8/27 17:35
 */
public class JSONUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(JSONUtils.class);

    public static String toJson(Object o) {
        if (o == null) {
            return "{}";
        }
        ObjectMapper mapper=new ObjectMapper();
        try {
            return mapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            LOGGER.error(e.getMessage(), e);
            return "{}";
        }
    }
}
