package com.groupchatback.pojo.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class InviteResponse extends ConversationResponse {
    private String inviteId;
    private Date dateSent;

    public InviteResponse(String inviteId, String userId, String username, String firstName, String lastName, boolean isOnline, boolean hasProfilePic, String conversationId, String conversationName, Date dateSent) {
        super(conversationId, conversationName, userId, username, firstName, lastName, isOnline, hasProfilePic);
        setInviteId(inviteId);
        setDateSent(dateSent);
    }

   @JsonProperty(value = "inviteId")
    public String getInviteId() {
        return inviteId;
    }

    @JsonProperty(value = "inviteId")
    public void setInviteId(String inviteId) {
        this.inviteId = inviteId;
    }

    @JsonProperty(value = "dateSent")
    public Date getDateSent() {
        return dateSent;
    }

    @JsonProperty(value = "dateSent")
    public void setDateSent(Date dateSent) {
        this.dateSent = dateSent;
    }
}
