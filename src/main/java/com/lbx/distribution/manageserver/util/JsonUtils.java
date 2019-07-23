package com.lbx.distribution.manageserver.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * json处理工具类
 */
public class JsonUtils {
    private static JsonUtils instance = new JsonUtils();
    private JsonUtils(){}
    public static JsonUtils getInstance(){
        return instance;
    }
    private static ObjectMapper mapper = new ObjectMapper();

    public static String objectToJson(Object object) throws IOException {
        return mapper.writeValueAsString(object);
    }

    public <T> T readValue(String jsonStr, Class<T> valueType) throws IOException {
        return mapper.readValue(jsonStr, valueType);
    }

    public static void main(String[] args) {

    }
}
