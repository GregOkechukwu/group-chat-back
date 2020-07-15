package com.groupchatback.pojo.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MessageRequest extends Request {

    private String conversationId;
    private MessageMetaData message;

    @JsonProperty(value = "conversationId")
    public String getConversationId() {
        return conversationId;
    }

    @JsonProperty(value = "conversationId")
    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    @JsonProperty(value = "message")
    public MessageMetaData getMessage() {
        return message;
    }

    @JsonProperty(value = "message")
    public void setMessage(MessageMetaData message) {
        this.message = message;
    }
}
