package com.groupchatback.controller;

import com.amazonaws.services.cognitoidp.model.NotAuthorizedException;
import com.groupchatback.pojo.request.Request;
import com.groupchatback.pojo.request.UserRequest;
import com.groupchatback.pojo.response.Response;
import com.groupchatback.pojo.response.UserResponse;
import com.groupchatback.service.ConversationService;
import com.groupchatback.service.ImageService;
import com.groupchatback.service.LoginService;
import com.groupchatback.service.UserService;
import com.groupchatback.util.CookieUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private LoginService loginService;

    @Autowired
    private ImageService imageService;

    @Autowired
    private ConversationService conversationService;

    @PostMapping
    public ResponseEntity<?> postUser(@NonNull @RequestBody UserRequest request) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

        String firstName = request.getFirstName();
        String lastName = request.getLastName();
        String email = request.getEmail();
        Date dateCreated = sdf.parse((String)request.get("dateCreated"));

        this.userService.postUserById(firstName, lastName, email, dateCreated);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping
    public UserResponse getUser() {
        Object[] userInfo = this.userService.getUserById();

        String userId = (String)userInfo[0];
        String username  = (String)userInfo[1];
        String firstName = (String)userInfo[2];
        String lastName  = (String)userInfo[3];

        return new UserResponse(userId, username, firstName, lastName, null);
    }

    @PutMapping
    public ResponseEntity<?> updateUser(@NonNull @RequestBody UserRequest userRequest, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String newUsername = userRequest.getUsername();
        String newFirstName = userRequest.getFirstName();
        String newLastName = userRequest.getLastName();
        String newEmail = userRequest.getEmail();
        String password = (String)userRequest.get("password");

        String[] updatedTokens = this.userService.updateUserInfo(newUsername, newFirstName, newLastName, newEmail, password);
        String accessTkn = updatedTokens[0];
        String refreshTkn = updatedTokens[1];

        CookieUtil.addCookiesInResponse(accessTkn, refreshTkn, request, response);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/onlinestatus")
    public ResponseEntity<?> updateOnlineStatus(@NonNull @RequestBody Request request) {
        boolean isOnline = (boolean)request.get("isOnline");
        this.userService.updateOnlineStatus(isOnline);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/logout")
    public ResponseEntity<?> signOutUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
        CookieUtil.expireCookies(request.getCookies(), response);
        this.loginService.globalSignOut();

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/password")
    public ResponseEntity<?> checkPassword(@NonNull @RequestBody Request passwordRequest) throws NotAuthorizedException {
        String password = (String)passwordRequest.get("password");

        try {
            String[] unusedTokens = this.loginService.checkPassword(password);
            /** No need to update cookies with these tokens **/

        } catch (Exception e) {
            throw new NotAuthorizedException("Invalid credentials");
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/users")
    public Response getAllUsers(@NonNull @RequestParam Map<String, String> requestParams) throws Exception {
        boolean includeFriends = StringUtils.equals(requestParams.getOrDefault("includefriends", null), "true") ? true : false;
        String username = requestParams.getOrDefault("username", null);
        String firstName = requestParams.getOrDefault("firstname", null);
        String lastName = requestParams.getOrDefault("lastname", null);

        int userIdIdx = 0, hasPicIdx = 5;

        List<Object[]> userRecords = this.userService.getAllUsers(username, firstName, lastName, includeFriends);
        List<Object[]> pics = this.imageService.getProfileOrDefaultPics(userRecords, userIdIdx, hasPicIdx);
        List<UserResponse> users = new ArrayList<>();

        int n = userRecords.size();

        for (int i = 0; i < n; i++) {
            Object[] record = userRecords.get(i);

            String userId = (String)record[0];
            username = (String)record[1];
            firstName = (String)record[2];
            lastName =  (String)record[3];
            boolean isOnline = (boolean)record[4];

            byte[] byteArr = (byte[]) pics.get(i)[0];
            String mimeType = (String)pics.get(i)[1];

            UserResponse user = new UserResponse(
                    userId,
                    username,
                    firstName,
                    lastName,
                    isOnline
            );

            user.add("byteArrBase64", byteArr);
            user.add("mimeType", mimeType);

            users.add(user);
        }

        return new Response("users", users);
    }

    @GetMapping("/friends")
    public Response getAllFriends(@NonNull @RequestParam Map<String, String> requestParams) throws Exception {
        String username = requestParams.getOrDefault("username", null);
        String firstName = requestParams.getOrDefault("firstname", null);
        String lastName = requestParams.getOrDefault("lastname", null);

        int userIdIdx = 0, hasPicIdx = 5;

        List<Object[]> friendRecords = this.userService.getAllFriends(username, firstName, lastName);
        List<Object[]> pics = this.imageService.getProfileOrDefaultPics(friendRecords, userIdIdx, hasPicIdx);
        List<UserResponse> friends = new ArrayList<>();

        int n = friendRecords.size();

        for (int i = 0; i < n; i++) {
            Object[] record = friendRecords.get(i);

            String userId = (String)record[0];
            username = (String)record[1];
            firstName = (String)record[2];
            lastName =  (String)record[3];

            boolean isOnline = (boolean)record[4];
            boolean hasProfilePic = (boolean)record[5];

            Date dateAdded = (Date)record[6];

            byte[] byteArr = (byte[])pics.get(i)[0];
            String mimeType = (String)pics.get(i)[1];

            UserResponse friend = new UserResponse(
                    userId,
                    username,
                    firstName,
                    lastName,
                    isOnline,
                    hasProfilePic
            );

            friend.add("dateAdded", dateAdded);
            friend.add("byteArrBase64", byteArr);
            friend.add("mimeType", mimeType);

            friends.add(friend);
        }

        return new Response("friends", friends);
    }

    @DeleteMapping("/friend")
    public ResponseEntity<?> removeFriend(@NonNull @RequestParam Map<String, String> requestParams) {
        String friendId = requestParams.get("friendId");
        this.userService.removeFriend(friendId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/conversation/{conversationId}")
    public Response getUsersInConversation(@NonNull @PathVariable String conversationId) throws Exception {
        int userIdIdx = 0, hasPicIdx = 5;

        this.conversationService.updateInChatStatus(conversationId, true);

        List<Object[]> userRecords = this.userService.getUsersInConversation(conversationId);
        List<Object[]> pics = this.imageService.getProfileOrDefaultPics(userRecords, userIdIdx, hasPicIdx);
        List<UserResponse> conversationMembers = new ArrayList<>();

        int n = userRecords.size();

        for (int i = 0; i < n; i++) {
            Object[] record = userRecords.get(i), pic = pics.get(i);

            String userId = (String)record[0];
            String username = (String)record[1];
            String firstName = (String)record[2];
            String lastName =  (String)record[3];

            boolean isOnline = (boolean)record[4];
            boolean hasProfilePic = (boolean)record[5];
            boolean inChat = (boolean)record[6];
            boolean isHost = (boolean)record[7];

            byte[] byteArr = (byte[])pic[0];
            String mimeType = (String)pic[1];

            UserResponse conversationMember = new UserResponse(
                    userId,
                    username,
                    firstName,
                    lastName,
                    isOnline,
                    hasProfilePic
            );

            conversationMember.add("inChat", inChat);
            conversationMember.add("isHost", isHost);
            conversationMember.add("byteArrBase64", byteArr);
            conversationMember.add("mimeType", mimeType);

            conversationMembers.add(conversationMember);
        }

        return new Response("conversationMembers", conversationMembers);
    }

}
