package com.groupchatback.pojo.response;

import com.fasterxml.jackson.annotation.JsonProperty;


public class UserResponse extends Response {
    private String userId;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private boolean isOnline;
    private boolean hasProfilePic;

    public UserResponse() {
    }

    public UserResponse(
            String userId,
            String username,
            String firstName,
            String lastName,
            String email
    ) {
        super();
        setUserId(userId);
        setUsername(username);
        setFirstName(firstName);
        setLastName(lastName);
        setEmail(email);
    }

    public UserResponse(
            String username,
            String firstName,
            String lastName,
            String email
    ) {
        super();
        setUsername(username);
        setFirstName(firstName);
        setLastName(lastName);
        setEmail(email);
    }

    public UserResponse(
             String userId,
             String username,
             String firstName,
             String lastName,
             boolean isOnline
    ) {
        super();
        setUserId(userId);
        setUsername(username);
        setFirstName(firstName);
        setLastName(lastName);
        setIsOnline(isOnline);
    }

    public UserResponse(
            String userId,
            String username,
            String firstName,
            String lastName,
            boolean isOnline,
            boolean hasProfilePic
    ) {
        super();
        setUserId(userId);
        setUsername(username);
        setFirstName(firstName);
        setLastName(lastName);
        setIsOnline(isOnline);
        setHasProfilePic(hasProfilePic);
    }

    @JsonProperty(value = "userId")
    public String getUserId() { return userId; }

    @JsonProperty(value = "userId")
    public void setUserId(String userId)  { this.userId = userId; }

    @JsonProperty(value = "username")
    public String getUsername() {
        return username;
    }

    @JsonProperty(value = "username")
    public void setUsername(String username) {
        this.username = username;
    }

    @JsonProperty(value = "firstName")
    public String getFirstName() {
        return firstName;
    }

    @JsonProperty(value = "firstName")
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @JsonProperty(value = "lastName")
    public String getLastName() {
        return lastName;
    }

    @JsonProperty(value = "lastName")
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @JsonProperty(value = "email")
    public String getEmail() {
        return email;
    }

    @JsonProperty(value = "email")
    public void setEmail(String email) {
        this.email = email;
    }

    @JsonProperty(value = "isOnline")
    public boolean isOnline() {
        return isOnline;
    }

    @JsonProperty(value = "isOnline")
    public void setIsOnline(boolean isOnline) {
        this.isOnline = isOnline;
    }

    @JsonProperty(value = "hasProfilePic")
    public boolean isHasProfilePic() {
        return hasProfilePic;
    }

    @JsonProperty(value = "hasProfilePic")
    public void setHasProfilePic(boolean hasProfilePic) {
        this.hasProfilePic = hasProfilePic;
    }
}


