package com.groupchatback.controller;

import com.groupchatback.pojo.request.MessageMetaData;
import com.groupchatback.pojo.request.MessageRequest;
import com.groupchatback.pojo.response.MessageResponse;
import com.groupchatback.pojo.response.Response;
import com.groupchatback.pojo.response.UserResponse;
import com.groupchatback.service.ImageService;
import com.groupchatback.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("message")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private ImageService imageService;

    @PostMapping
    public Response createMessage(@NonNull @RequestBody MessageRequest messageRequest) throws Exception {
        String conversationId = messageRequest.getConversationId();
        String messageContent = messageRequest.getMessage().getMessageContent();
        Date dateSent = messageRequest.getMessage().getDateSent();

        String messageId = this.messageService.createMessage(conversationId, messageContent, dateSent);
        return new Response("messageId", messageId);
    }

    @GetMapping("/{conversationId}")
    public Response getUserMessages(@NonNull @PathVariable String conversationId) throws Exception {
        int userIdIdx = 0, hasPicIdx = 5;

        List<Object[]> userMessageRecords = this.messageService.getUserMessages(conversationId);
        List<Object[]> pics = this.imageService.getProfileOrDefaultPics(userMessageRecords, userIdIdx, hasPicIdx);
        List<MessageResponse> userMessages = new ArrayList<>();

        int n = userMessageRecords.size();

        for (int i = 0; i < n; i++) {
            Object[] record = userMessageRecords.get(i), pic = pics.get(i);

            String userId = (String)record[0];
            String username = (String)record[1];
            String firstName = (String)record[2];
            String lastName =  (String)record[3];

            boolean isOnline = (boolean)record[4];
            boolean hasProfilePic = (boolean)record[5];

            String messageId = (String)record[6];
            String messageContent = (String)record[7];
            Date dateSent = (Date)record[8];

            byte[] byteArr = (byte[])pic[0];
            String mimeType = (String)pic[1];

            MessageMetaData message = new MessageMetaData(
                    messageId,
                    messageContent,
                    dateSent
            );

            UserResponse user = new UserResponse(
                    userId,
                    username,
                    firstName,
                    lastName,
                    isOnline,
                    hasProfilePic
            );

            user.add("byteArrBase64", byteArr);
            user.add("mimeType", mimeType);

            userMessages.add(new MessageResponse(message, user));
        }

        return new Response("userMessages", userMessages);
    }
}
