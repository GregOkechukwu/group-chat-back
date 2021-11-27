package com.groupchatback.controller;

import com.groupchatback.pojo.response.InviteResponse;
import com.groupchatback.pojo.response.Response;
import com.groupchatback.service.ImageService;
import com.groupchatback.service.InviteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("invite")
public class InviteController {

    @Autowired
    private InviteService inviteService;

    @Autowired
    private ImageService imageService;

    private List<InviteResponse> getInvites(List<Object[]> inviteRecords) throws Exception {
        int userIdIdx = 3, hasPicIdx = 8;
        int n = inviteRecords.size();

        List<Object[]> pics = this.imageService.getProfileOrDefaultPics(inviteRecords, userIdIdx, hasPicIdx);
        List<InviteResponse> invites = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            Object[] record = inviteRecords.get(i), pic = pics.get(i);

            String inviteId = (String)record[0];
            String conversationId = (String)record[1];
            String conversationName = (String)record[2];
            String userId = (String)record[3];
            String username = (String)record[4];
            String firstName = (String)record[5];
            String lastName = (String)record[6];
            boolean isOnline = (boolean)record[7];
            boolean hasProfilePic = (boolean)record[8];

            Date dateSent = (Date)record[9];

            byte[] byteArr = (byte[])pic[0];
            String mimeType = (String)pic[1];

            InviteResponse invite = new InviteResponse(
                    inviteId,
                    userId,
                    username,
                    firstName,
                    lastName,
                    isOnline,
                    hasProfilePic,
                    conversationId,
                    conversationName,
                    dateSent
            );

            invite.add("byteArrBase64", byteArr);
            invite.add("mimeType", mimeType);

            invites.add(invite);
        }

        return invites;
    }

    @GetMapping("count")
    public Response getReceivedInvitesCount() {
        long inviteCount = this.inviteService.getReceivedInvitesCount();
        return new Response("inviteCount", inviteCount);
    }

    @GetMapping("received")
    public Response getReceivedInvites() throws Exception {
        List<Object[]> inviteRecords = this.inviteService.getInvites(true);
        return new Response("invites", getInvites(inviteRecords));
    }

    @GetMapping("sent")
    public Response getSentInvites() throws Exception {
        List<Object[]> inviteRecords = this.inviteService.getInvites(false);
        return new Response("invites", getInvites(inviteRecords));
    }

    @DeleteMapping("accept")
    public ResponseEntity<?> acceptInvite(@NonNull @RequestParam Map<String, String> requestParams) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.ENGLISH);

        String inviteId = requestParams.get("inviteId");
        String conversationId = requestParams.get("conversationId");
        Date dateJoined = sdf.parse(requestParams.get("dateJoined"));

        this.inviteService.acceptInvite(inviteId, conversationId, dateJoined);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("decline")
    public ResponseEntity<?> declineInvite(@NonNull @RequestParam Map<String, String> requestParams) {
        String inviteId = requestParams.get("inviteId");
        this.inviteService.declineInvite(inviteId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("cancel")
    public ResponseEntity<?> cancelInvite(@NonNull @RequestParam Map<String, String> requestParams) {
        String inviteId = requestParams.get("inviteId");
        this.inviteService.declineInvite(inviteId);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
