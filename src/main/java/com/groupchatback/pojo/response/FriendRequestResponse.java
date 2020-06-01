package com.groupchatback.pojo.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;

public class FriendRequestResponse extends UserResponse {

    private String friendRequestId;
    private Date dateSent;

    public FriendRequestResponse(String friendRequestId, Date dateSent, String userId, String username, String firstName, String lastName, boolean isOnline, boolean hasProfilePic) {
        super(userId, username, firstName, lastName, isOnline, hasProfilePic);
        setFriendRequestId(friendRequestId);
        setDateSent(dateSent);
    }

    @JsonProperty (value = "friendRequestId")
    public String getFriendRequestId() {
        return friendRequestId;
    }

    @JsonProperty (value = "friendRequestId")
    public void setFriendRequestId(String friendRequestId) {
        this.friendRequestId = friendRequestId;
    }

    @JsonProperty (value = "dateSent")
    public Date getDateSent() {
        return dateSent;
    }

    @JsonProperty (value = "dateSent")
    public void setDateSent(Date dateSent) {
        this.dateSent = dateSent;
    }
}
