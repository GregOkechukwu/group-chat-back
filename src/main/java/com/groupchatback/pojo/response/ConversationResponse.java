package com.groupchatback.pojo.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ConversationResponse extends UserResponse {
    private String conversationId;
    private String conversationName;
    private String conversationHost;
    private int userCount;

    public ConversationResponse() {
    }

    public ConversationResponse(String conversationId, String conversationName, String conversationHost, int userCount) {
        super();
        setConversationId(conversationId);
        setConversationName(conversationName);
        setConversationHost(conversationHost);
        setUserCount(userCount);
    }

    public ConversationResponse(String conversationId, String conversationName, String userId, String username, String firstName, String lastName, boolean isOnline, boolean hasProfilePic) {
        super(userId, username, firstName, lastName, isOnline, hasProfilePic);
        setConversationId(conversationId);
        setConversationName(conversationName);
    }

    @JsonProperty(value = "conversationId")
    public String getConversationId() {
        return conversationId;
    }

    @JsonProperty(value = "conversationId")
    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    @JsonProperty(value = "conversationName")
    public String getConversationName() {
        return conversationName;
    }

    @JsonProperty(value = "conversationName")
    public void setConversationName(String conversationName) {
        this.conversationName = conversationName;
    }

    @JsonProperty(value = "conversationHost")
    public String getConversationHost() {
        return conversationHost;
    }

    @JsonProperty(value = "conversationHost")
    public void setConversationHost(String conversationHost) {
        this.conversationHost = conversationHost;
    }

    @JsonProperty(value = "userCount")
    public long getUserCount() {
        return userCount;
    }

    @JsonProperty(value = "userCount")
    public void setUserCount(int userCount) {
        this.userCount = userCount;
    }
}
