package com.groupchatback.pojo.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;

public class RegisterResponse extends UserResponse {
    private Date dateCreated;

    @JsonCreator
    public RegisterResponse(
            @JsonProperty(value = "username") String username,
            @JsonProperty(value = "firstName") String firstname,
            @JsonProperty(value = "lastName") String lastname,
            @JsonProperty(value = "email") String email,
            @JsonProperty(value = "dateCreated") Date dateCreated
    ) {
        super(username, firstname, lastname, email);
        setDateCreated(dateCreated);
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }
}
