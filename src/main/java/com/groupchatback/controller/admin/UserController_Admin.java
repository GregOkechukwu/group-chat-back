package com.groupchatback.controller.admin;

import com.groupchatback.pojo.response.Response;
import com.groupchatback.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("admin/user")
public class UserController_Admin {

    @Autowired
    private UserService userService;

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUser(@NonNull @RequestParam(value = "userId") String userId) throws Exception {
        this.userService.deleteUserById(userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/authstatus")
    public Response isAuthenticated(HttpServletRequest request) {
        boolean isAuthenticated = this.userService.isAuthenticated(request.getCookies());
        return new Response("isAuthenticated", isAuthenticated);
    }

    @GetMapping("/check")
    public Response geUserAvailability(@NonNull @RequestParam Map<String, String> requestParams) {
        String username = requestParams.getOrDefault("username", null);
        String email = requestParams.getOrDefault("email", null);

        Response response = new Response();

        if (username != null) {
            boolean isTaken = this.userService.usernameIsTaken(username);
            response.add("usernameTaken", isTaken);
        }

        if (email != null) {
            boolean isTaken = this.userService.emailIsTaken(email);
            response.add("emailTaken", isTaken);
        }

        return response;
    }
}
