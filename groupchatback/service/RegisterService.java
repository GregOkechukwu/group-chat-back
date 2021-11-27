package com.groupchatback.service;

import com.amazonaws.services.cognitoidp.model.AttributeType;
import com.amazonaws.services.cognitoidp.model.UserType;
import com.groupchatback.aws.CognitoIdentityProvider;
import com.groupchatback.dao.interfaces.UserDao;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class RegisterService {

    @Autowired
    private UserDao userDao;

    private String[] getAttributeTypes(List<AttributeType> attributeList) {
        String[] attributes = new String[3];

        attributeList.forEach((AttributeType attributeType) -> {
            String attributeName = attributeType.getName();

            if (StringUtils.equals(attributeName, "name")) {
                String[] name = attributeType.getValue().split(" ");
                attributes[0] = name[0];
                attributes[1] = name[1];
            } else if (StringUtils.equals(attributeName, "email")) {
                attributes[2] = attributeType.getValue();
            }
        });

        return attributes;
    }

    public Object[] registerUser(String username, String firstName, String lastName, String email) throws Exception {
        UserType user = CognitoIdentityProvider.signUp(
                username.toLowerCase(),
                username.toLowerCase(),
                firstName.toLowerCase(),
                lastName.toLowerCase(),
                email.toLowerCase()
        );

        String[] attributes = this.getAttributeTypes(user.getAttributes());

        String registeredUsername = user.getUsername();
        String registeredFirstName = attributes[0];
        String registeredLastName = attributes[1];
        String registeredEmail = attributes[2];
        Date dateCreated = user.getUserCreateDate();

        return new Object[]{
                registeredUsername,
                registeredFirstName,
                registeredLastName,
                registeredEmail,
                dateCreated
        };
    }
}
