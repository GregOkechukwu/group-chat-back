package com.groupchatback.pojo.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginRequest extends Request {
    private String usernameOrEmail;

    @JsonProperty(value = "usernameOrEmail")
    public String getUsernameOrEmail() {
        return usernameOrEmail;
    }

    @JsonProperty(value = "usernameOrEmail")
    public void setUsernameOrEmail(String usernameOrEmail) {
        this.usernameOrEmail = usernameOrEmail;
    }
}
