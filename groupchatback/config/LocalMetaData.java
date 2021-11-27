package com.groupchatback.config;

import com.groupchatback.interceptor.CookieInterceptor;

public class LocalMetaData {
    private String decodedUserId;

    public LocalMetaData() {
        setDecodedUserId(CookieInterceptor.getDecodedUserId());
    }

    public String getDecodedUserId() {
        return decodedUserId;
    }

    public void setDecodedUserId(String decodedUserId) {
        this.decodedUserId = decodedUserId;
    }
}
