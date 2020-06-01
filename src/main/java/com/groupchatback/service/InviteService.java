package com.groupchatback.service;

import com.groupchatback.config.LocalMetaData;
import com.groupchatback.dao.interfaces.ConversationDao;
import com.groupchatback.dao.interfaces.InviteDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class InviteService {

    @Autowired
    private LocalMetaData localBean;

    @Autowired
    private InviteDao inviteDao;

    @Autowired
    private ConversationService conversationService;

    public List<Object[]> getInvites(boolean getReceivedInvites) {
        String decodedUserId = this.localBean.getDecodedUserId();
        return this.inviteDao.getInvites(decodedUserId, getReceivedInvites);
    }

    public void acceptInvite(String inviteId, String conversationId, Date dateJoined) {
        this.conversationService.joinConversationAsNewMember(conversationId, dateJoined);
        this.inviteDao.deleteInvite(inviteId);
    }

    public void declineInvite(String inviteId) {
        this.inviteDao.deleteInvite(inviteId);
    }

    public long getReceivedInvitesCount() {
        String decodedUserId = this.localBean.getDecodedUserId();
        return this.inviteDao.getReceivedInvitesCount(decodedUserId);
    }
}
