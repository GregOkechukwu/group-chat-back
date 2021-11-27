package com.groupchatback.controller.admin;

import com.amazonaws.services.cognitoidp.model.NotAuthorizedException;
import com.groupchatback.pojo.request.LoginRequest;
import com.groupchatback.pojo.request.Request;
import com.groupchatback.pojo.response.Response;
import com.groupchatback.service.LoginService;

import com.groupchatback.util.CookieUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("admin/login")
public class LoginController_Admin {

    @Autowired
    private LoginService loginService;

    @PostMapping
    public ResponseEntity<?> loginUser(@NonNull @RequestBody LoginRequest loginRequest, HttpServletRequest request, HttpServletResponse response) throws NotAuthorizedException {

        String usernameOrEmail = loginRequest.getUsernameOrEmail();
        String password = (String)loginRequest.get("password");

        try {
            String[] tokens = this.loginService.signIn(usernameOrEmail, password);
            String accessTkn = tokens[0];
            String refreshTkn = tokens[1];

            CookieUtil.addCookiesInResponse(accessTkn, refreshTkn, request, response);

        } catch (Exception e) {
            CookieUtil.expireCookies(request.getCookies(), response);
            throw new NotAuthorizedException("Invalid credentials");
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/firstlogin")
    public ResponseEntity<?> firstLoginUser(@NonNull @RequestBody LoginRequest loginRequest, HttpServletRequest request, HttpServletResponse response) throws NotAuthorizedException {

        String username = loginRequest.getUsernameOrEmail();
        String newPassword = (String)loginRequest.get("newPassword");
        String tempPassword = (String)loginRequest.get("tempPassword");

        try {
            String[] tokens = this.loginService.firstSignIn(username, newPassword, tempPassword);
            String accessTkn = tokens[0];
            String refreshTkn = tokens[1];

            CookieUtil.addCookiesInResponse(accessTkn, refreshTkn, request, response);

        } catch (Exception e) {
            throw new NotAuthorizedException("Invalid credentials");
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/send")
    public Response forgotPassword(@NonNull @RequestParam(value = "username") String username) throws Exception {
        String emailDestination = this.loginService.forgotPassword(username);
        return new Response("emailDestination", emailDestination);
    }

    @PutMapping("/forgot")
    public ResponseEntity<?> confirmForgottenPassword(@NonNull @RequestBody Request forgotPasswordRequest) throws Exception {
        String username = (String)forgotPasswordRequest.get("username");
        String code = (String)forgotPasswordRequest.get("code");
        String newPassword = (String)forgotPasswordRequest.get("newPassword");

        this.loginService.confirmForgottenPassword(username, code, newPassword);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
