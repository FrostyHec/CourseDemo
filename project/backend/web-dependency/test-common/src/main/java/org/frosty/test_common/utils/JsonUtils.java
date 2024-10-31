package org.frosty.test_common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.frosty.common.exception.InternalException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MvcResult;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class JsonUtils {

    private static ObjectMapper objectMapper;
    @Autowired
    private ObjectMapper injectedObjectMapper;

    public static <T> T dataCast(Object map, Class<T> type) throws JsonProcessingException {
        JsonNode node = objectMapper.valueToTree(map);
        return objectMapper.treeToValue(node, type);
    }
    public static String getErrorString(MvcResult mvcResult) throws UnsupportedEncodingException, JsonProcessingException {
        return toJson(mvcResult).get("msg").asText();
    }

    public static String toString(Object o) {
        try {
            return objectMapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            throw new InternalException("unable to parse object:" + o, e);
        }
    }

    public static Map toMapData(MvcResult mvcResult) throws JsonProcessingException, UnsupportedEncodingException {
        return objectMapper.treeToValue(toJsonData(mvcResult), Map.class);
    }

    private static String getJsonString(MvcResult mvcResult) throws UnsupportedEncodingException {
        return mvcResult.getResponse().getContentAsString();
    }

    public static JsonNode toJson(MvcResult result) throws UnsupportedEncodingException,
            JsonProcessingException {
        return objectMapper.readTree(getJsonString(result));
    }

    public static JsonNode toJsonData(MvcResult result) throws UnsupportedEncodingException,
            JsonProcessingException {
        return toJson(result).get("data");
    }

    public static <T> T toObject(MvcResult result, Class<T> type) throws
            UnsupportedEncodingException,
            JsonProcessingException {
        return objectMapper.treeToValue(toJsonData(result), type);
    }

    public static <T> List<T> nodeToList(JsonNode node, Class<?> clazz) throws
            JsonProcessingException {
        assert node.isArray();
        List<T> list = new ArrayList<>();
        for (var e : node) {
            list.add((T) objectMapper.treeToValue(e, clazz));
        }
        return list;
    }

    @PostConstruct
    public void init() {
        objectMapper = injectedObjectMapper;
    }
}
