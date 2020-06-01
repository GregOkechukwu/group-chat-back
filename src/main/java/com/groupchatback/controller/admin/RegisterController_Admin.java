package com.groupchatback.controller.admin;

import com.groupchatback.pojo.request.UserRequest;
import com.groupchatback.pojo.response.RegisterResponse;
import com.groupchatback.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("admin/register")
public class RegisterController_Admin {

    @Autowired
    private RegisterService registerService;

    @PostMapping
    public RegisterResponse registerUser(@NonNull @RequestBody UserRequest registerRequest) throws Exception {
        Object[] registeredUserInfo = this.registerService.registerUser(
                registerRequest.getUsername(),
                registerRequest.getFirstName(),
                registerRequest.getLastName(),
                registerRequest.getEmail()
        );

        String registeredUsername = (String)registeredUserInfo[0];
        String registeredFirstName = (String)registeredUserInfo[1];
        String registeredLastName = (String)registeredUserInfo[2];
        String registeredEmail = (String)registeredUserInfo[3];
        Date dateCreated = (Date)registeredUserInfo[4];

        return new RegisterResponse(
                registeredUsername,
                registeredFirstName,
                registeredLastName,
                registeredEmail,
                dateCreated
        );
    }

}
