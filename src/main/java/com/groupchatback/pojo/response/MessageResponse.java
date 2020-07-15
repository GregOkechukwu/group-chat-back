package com.groupchatback.pojo.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.groupchatback.pojo.request.MessageMetaData;

public class MessageResponse extends Response {
    private MessageMetaData message;
    private UserResponse user;

    public MessageResponse(MessageMetaData message, UserResponse user) {
        this.message = message;
        this.user = user;
    }

    @JsonProperty(value = "message")
    public MessageMetaData getMessage() {
        return message;
    }

    @JsonProperty(value = "message")
    public void setMessage(MessageMetaData message) {
        this.message = message;
    }

    @JsonProperty(value = "user")
    public UserResponse getUser() {
        return user;
    }

    @JsonProperty(value = "user")
    public void setUser(UserResponse user) {
        this.user = user;
    }
}
