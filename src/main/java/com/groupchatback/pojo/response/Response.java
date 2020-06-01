package com.groupchatback.pojo.response;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;

import java.util.HashMap;
import java.util.Map;

public class Response {
    private Map<String, Object> properties = new HashMap<>();

    public Response() {
    }

    public Response(String key, Object value) {
        this.add(key, value);
    }

    public Response(String key1, Object value1, String key2, Object value2) {
        this.add(key1, value1);
        this.add(key2, value2);
    }

    public Response(String key1, Object value1, String key2, Object value2, String key3, Object value3) {
        this.add(key1, value1);
        this.add(key2, value2);
        this.add(key3, value3);
    }

    @JsonAnyGetter
    public Map<String, Object> getProperties() {
        return properties;
    }

    @JsonAnySetter
    public void add(String key, Object value) {
        this.properties.put(key, value);
    }

    public Object get(String key) {
        return this.getProperties().get(key);
    }
}
