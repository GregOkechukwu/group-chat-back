package com.groupchatback.controller;

import com.groupchatback.pojo.request.ConversationRequest;
import com.groupchatback.pojo.request.Request;
import com.groupchatback.pojo.response.ConversationResponse;
import com.groupchatback.pojo.response.Response;
import com.groupchatback.service.ConversationService;
import com.groupchatback.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("conversation")
public class ConversationController {

    @Autowired
    private ConversationService conversationService;

    @Autowired
    private ImageService imageService;

    @PostMapping
    public ResponseEntity<?> postConversation(@NonNull @RequestBody ConversationRequest request) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.ENGLISH);

        String conversationName = request.getConversationName();
        String[] invitedUsers = request.getInvitedUsers();
        Date dateCreated = sdf.parse((String)request.get("dateCreated"));

        this.conversationService.postConversation(conversationName, invitedUsers, dateCreated);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteConversation(@NonNull @RequestParam Map<String, String> requestParams) {
        String conversationId = requestParams.get("conversationId");
        this.conversationService.deleteConversation(conversationId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping
    public Response getConversations() throws Exception {
        final int keyIdx = 0, hasPicIdx = 4;

        List<Object[]> conversationRecords = this.conversationService.getConversations();
        List<ConversationResponse> conversations = new ArrayList<>();

        int n = conversationRecords.size();
        char[] userSuffixes = new char[]{'A', 'B'};

        for (int i = 0; i < n; i++) {
            Object[] record = conversationRecords.get(i);

            String conversationId = (String)record[0];
            String conversationName = (String)record[1];
            String conversationHostId = (String)record[2];
            String conversationHostUsername = (String)record[3];
            int userCount = ((Long)record[4]).intValue();

            ConversationResponse conversation = new ConversationResponse(
                    conversationId,
                    conversationName,
                    conversationHostId,
                    conversationHostUsername,
                    userCount
            );

            List<Object[]> prioritizedUsers = this.conversationService.getPrioritizedUsersInConversation(conversationId);
            List<Object[]> prioritizedPics = this.imageService.getProfileOrDefaultPics(prioritizedUsers, keyIdx, hasPicIdx);

            Response picResponse = new Response();

            for (int j = 0; j < 3; j++) {
                byte[] userPic = prioritizedPics.get(j) == null ? null : (byte[])prioritizedPics.get(j)[0];
                String mimeType = prioritizedPics.get(j) == null ? null : (String)prioritizedPics.get(j)[1];

                String picKey = j == 0 ? "hostUser" : "prioritizedUser" + userSuffixes[j - 1];

                picResponse.add(
                        picKey,
                    new Response(
                            "byteArrBase64", userPic,
                            "mimeType", mimeType,
                            "hasProfilePic", prioritizedUsers.get(j) == null ? false : prioritizedUsers.get(j)[hasPicIdx]
                    )
                );
            }

            conversation.add("prioritizedPics", picResponse);
            conversations.add(conversation);
        }

        return new Response("conversations", conversations);
    }

    @GetMapping("/check")
    public Response getConversationAvailability(@NonNull @RequestParam Map<String, String> requestParams) {
        String conversationName = requestParams.get("name");
        boolean isTaken = this.conversationService.conversationNameIsTaken(conversationName);

        return new Response("conversationNameTaken", isTaken);
    }

    @PutMapping("/chatstatus")
    public ResponseEntity<?> updateInChatStatus(@NonNull @RequestBody Request request) throws Exception {
        String conversationId = (String)request.get("conversationId");
        boolean inChatStatus = (boolean)request.get("inChatStatus");

        this.conversationService.updateInChatStatus(conversationId, inChatStatus);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/leave")
    public ResponseEntity<?> leaveConversation(@NonNull @RequestParam Map<String, String> requestParams) {
        String conversationId = requestParams.get("conversationId");
        this.conversationService.leaveConversation(conversationId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/host")
    public ResponseEntity<?> updateConversationHost(@NonNull @RequestBody Request request) {
        String conversationId = (String)request.get("conversationId");
        String newHostId = (String)request.get("newHostId");

        this.conversationService.updateConversationHost(conversationId, newHostId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
