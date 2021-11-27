package com.groupchatback.pojo.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ConversationRequest extends Request {
    private String conversationName;
    private String[] invitedUsers;

    @JsonProperty(value = "conversationName")
    public String getConversationName() {
        return conversationName;
    }

    @JsonProperty(value = "conversationName")
    public void setConversationName(String conversationName) {
        this.conversationName = conversationName;
    }

    @JsonProperty(value = "invitedUsers")
    public String[] getInvitedUsers() {
        return invitedUsers;
    }

    @JsonProperty(value = "invitedUsers")
    public void setInvitedUsers(String[] invitedUsers) {
        this.invitedUsers = invitedUsers;
    }
}
