package com.groupchatback.pojo.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserRequest extends Request {

    private String username;
    private String firstName;
    private String lastName;
    private String email;

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
}
