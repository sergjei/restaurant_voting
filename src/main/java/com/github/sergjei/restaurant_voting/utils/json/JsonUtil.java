package com.github.sergjei.restaurant_voting.utils.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class JsonUtil {
    private static ObjectMapper mapper;
    @Autowired
    private ObjectMapper tmpMapper;

    @PostConstruct
    public void setMapper() {
        mapper = tmpMapper;
    }

    public static <T> List<T> readValues(String json, Class<T> clazz) {
        ObjectReader reader = mapper.readerFor(clazz);
        try {
            return reader.<T>readValues(json).readAll();
        } catch (IOException e) {
            throw new IllegalArgumentException("Invalid read array from JSON:\n'" + json + "'", e);
        }
    }

    public static <T> T readValue(String json, Class<T> clazz) {
        try {
            return mapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Invalid read from JSON:\n'" + json + "'", e);
        }
    }

    public static <T> String writeValue(T obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Invalid write to JSON:\n'" + obj + "'", e);
        }
    }

    public static <T> String writeAdditionProp(T obj, String propName, Object prop) {
        return writeAdditionProp(obj, Map.of(propName, prop));
    }

    public static <T> String writeAdditionProp(T obj, Map<String, Object> addProps) {
        Map<String, Object> map = mapper.convertValue(obj, new TypeReference<>() {
        });
        map.putAll(addProps);
        return writeValue(map);
    }

    public static void setMapper(ObjectMapper mapper) {
        JsonUtil.mapper = mapper;
    }

    public static List<String> jsonToStringList(String json) {
        if (json.contains("[")) {
            List<String> result = new ArrayList<>();
            StringBuilder sb = new StringBuilder(json);
            while (sb.length() > 1) {
                int start = sb.indexOf("{");
                int end = sb.indexOf("}") + 1;
                result.add(sb.substring(start, end));
                sb.delete(0, end);
            }
            return result;
        } else {
            throw new IllegalArgumentException("Invalid input data, must be serialized collection");
        }
    }
}
