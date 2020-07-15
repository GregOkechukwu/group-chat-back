package com.groupchatback.controller;

import com.groupchatback.pojo.request.Request;
import com.groupchatback.pojo.response.FriendRequestResponse;
import com.groupchatback.pojo.response.Response;
import com.groupchatback.service.FriendRequestService;
import com.groupchatback.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("friendrequest")
public class FriendRequestController {

    @Autowired
    private FriendRequestService friendRequestService;

    @Autowired
    private ImageService imageService;

    private List<FriendRequestResponse> getFriendRequests(List<Object[]> requestRecords) throws Exception {
        int userIdIdx = 2, hasPicIdx = 7;
        int n = requestRecords.size();

        List<Object[]> pics = this.imageService.getProfileOrDefaultPics(requestRecords, userIdIdx, hasPicIdx);
        List<FriendRequestResponse> friendRequests = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            Object[] record = requestRecords.get(i), pic = pics.get(i);

            String friendRequestId = (String)record[0];
            Date dateSent = (Date)record[1];
            String userId = (String)record[2];
            String username = (String)record[3];
            String firstName = (String)record[4];
            String lastName = (String)record[5];
            boolean isOnline = (boolean)record[6];
            boolean hasProfilePic = (boolean)record[7];

            byte[] byteArr = (byte[])pic[0];
            String mimeType = (String)pic[1];

            FriendRequestResponse friendRequest = new FriendRequestResponse(
                    friendRequestId,
                    dateSent,
                    userId,
                    username,
                    firstName,
                    lastName,
                    isOnline,
                    hasProfilePic
            );

            friendRequest.add("byteArrBase64", byteArr);
            friendRequest.add("mimeType", mimeType);
            friendRequests.add(friendRequest);
        }

        return friendRequests;
    }

    @PostMapping
    public ResponseEntity<?> sendFriendRequests(@NonNull @RequestBody Request request) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.ENGLISH);

        List<String> usersToAdd = (List<String>)request.get("usersToAdd");
        Date dateSent = sdf.parse((String)request.get("dateSent"));

        this.friendRequestService.sendFriendRequests(usersToAdd, dateSent);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/received")
    public Response getReceivedFriendRequests() throws Exception {
        List<Object[]> friendRequestRecords = this.friendRequestService.getFriendRequests(true);
        return new Response("friendRequests", getFriendRequests(friendRequestRecords));
    }

    @GetMapping("/sent")
    public Response getSentFriendRequests() throws Exception {
        List<Object[]> friendRequestRecords = this.friendRequestService.getFriendRequests(false);
        return new Response("friendRequests", getFriendRequests(friendRequestRecords));
    }

    @DeleteMapping("/accept")
    public ResponseEntity<?> acceptFriendRequest(@NonNull @RequestParam Map<String, String> requestParams) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.ENGLISH);

        String friendRequestId = requestParams.get("friendRequestId");
        String senderId = requestParams.get("senderId");
        Date dateAdded = sdf.parse(requestParams.get("dateAdded"));

        this.friendRequestService.acceptFriendRequest(friendRequestId, senderId, dateAdded);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/decline")
    public ResponseEntity<?> declineFriendRequest(@NonNull @RequestParam Map<String, String> requestParams) {
        String friendRequestId = requestParams.get("friendRequestId");

        this.friendRequestService.declineFriendRequest(friendRequestId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/cancel")
    public ResponseEntity<?> cancelFriendRequest(@NonNull @RequestParam Map<String, String> requestParams) {
        String friendRequestId = requestParams.get("friendRequestId");

        this.friendRequestService.declineFriendRequest(friendRequestId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
