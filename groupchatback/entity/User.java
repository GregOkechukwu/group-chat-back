package com.groupchatback.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "MyUser")
public class User {

    private String id;
    private String username;
    private String firstname;
    private String lastname;
    private String email;
    private Date datecreated;
    private boolean hasprofilepic;
    private boolean isonline;
    private List<ConversationMember> hostconversations;
    private List<ConversationMember> conversations;
    private List<Invite> invitessent;
    private List<Invite> invitesreceived;
    private List<FriendRequest> friendrequestssent;
    private List<FriendRequest> friendrequestreceived;
    private List<UserFriend> userFriends;
    private List<Message> messages;

    public User() {
    }

    public User(String id) {
        this.id = id;
    }

    @Id
    @Column(nullable = false)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Column(nullable = false)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Column(name = "firstname", nullable = false)
    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    @Column(name = "lastname", nullable = false)
    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    @Column(name = "email", nullable = false)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(name = "datecreated", nullable = false)
    public Date getDatecreated() {
        return datecreated;
    }

    public void setDatecreated(Date datecreated) {
        this.datecreated = datecreated;
    }

    @Column(name = "hasprofilepic", nullable = false)
    public boolean isHasprofilepic() {
        return hasprofilepic;
    }

    public void setHasprofilepic(boolean hasprofilepic) {
        this.hasprofilepic = hasprofilepic;
    }

    @Column(name = "isonline", nullable = false)
    public boolean isIsonline() {
        return isonline;
    }

    public void setIsonline(boolean isonline) {
        this.isonline = isonline;
    }

    @OneToMany(targetEntity = com.groupchatback.entity.ConversationMember.class, cascade = { CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH }, mappedBy = "member")
    public List<ConversationMember> getHostconversations() {
        return hostconversations;
    }

    public void setHostconversations(List<ConversationMember> hostconversations) {
        this.hostconversations = hostconversations;
    }

    @OneToMany(targetEntity = com.groupchatback.entity.ConversationMember.class, cascade = { CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH }, mappedBy = "member")
    public List<ConversationMember> getConversations() {
        return conversations;
    }

    public void setConversations(List<ConversationMember> conversations) {
        this.conversations = conversations;
    }

    @OneToMany(targetEntity = com.groupchatback.entity.Invite.class, cascade = { CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH }, mappedBy = "sender")
    public List<Invite> getInvitessent() {
        return invitessent;
    }

    public void setInvitessent(List<Invite> invitessent) {
        this.invitessent = invitessent;
    }

    @OneToMany(targetEntity = com.groupchatback.entity.Invite.class, cascade = { CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH }, mappedBy = "recipient")
    public List<Invite> getInvitesreceived() {
        return invitesreceived;
    }

    public void setInvitesreceived(List<Invite> invitesreceived) {
        this.invitesreceived = invitesreceived;
    }

    @OneToMany(targetEntity = com.groupchatback.entity.FriendRequest.class, cascade = { CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH }, mappedBy = "sender")
    public List<FriendRequest> getFriendrequestssent() {
        return friendrequestssent;
    }

    public void setFriendrequestssent(List<FriendRequest> friendrequestssent) {
        this.friendrequestssent = friendrequestssent;
    }

    @OneToMany(targetEntity = com.groupchatback.entity.FriendRequest.class, cascade = { CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH }, mappedBy = "recipient")
    public List<FriendRequest> getFriendrequestreceived() {
        return friendrequestreceived;
    }

    public void setFriendrequestreceived(List<FriendRequest> friendrequestreceived) {
        this.friendrequestreceived = friendrequestreceived;
    }

    @OneToMany(targetEntity = UserFriend.class, cascade = { CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH }, mappedBy = "user")
    public List<UserFriend> getUserFriends() {
        return userFriends;
    }

    public void setUserFriends(List<UserFriend> userFriends) {
        this.userFriends = userFriends;
    }

    @OneToMany(targetEntity = Message.class, cascade = { CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH }, mappedBy = "sender")
    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }
}
