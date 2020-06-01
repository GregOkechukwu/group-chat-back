package com.groupchatback.pojo.request;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;

import java.util.HashMap;
import java.util.Map;

public class Request {
    protected Map<String, Object> properties = new HashMap<>();

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

