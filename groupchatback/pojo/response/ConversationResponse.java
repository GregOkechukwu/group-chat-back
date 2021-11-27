package com.groupchatback.pojo.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ConversationResponse extends UserResponse {
    private String conversationId;
    private String conversationName;
    private String conversationHostId;
    private String conversationHostUsername;
    private int userCount;

    public ConversationResponse() {
    }

    public ConversationResponse(String conversationId, String conversationName, String conversationHostId, String conversationHostUsername, int userCount) {
        super();
        setConversationId(conversationId);
        setConversationName(conversationName);
        setConversationHostId(conversationHostId);
        setConversationHostUsername(conversationHostUsername);
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

    @JsonProperty(value = "conversationHostId")
    public String getConversationHostId() {
        return conversationHostId;
    }

    @JsonProperty(value = "conversationHostId")
    public void setConversationHostId(String conversationHostId) {
        this.conversationHostId = conversationHostId;
    }

    @JsonProperty(value = "conversationHostUsername")
    public String getConversationHostUsername() {
        return conversationHostUsername;
    }

    @JsonProperty(value = "conversationHostUsername")
    public void setConversationHostUsername(String conversationHostUsername) {
        this.conversationHostUsername = conversationHostUsername;
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
