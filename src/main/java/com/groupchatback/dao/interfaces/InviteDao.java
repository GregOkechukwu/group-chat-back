package com.groupchatback.dao.interfaces;

import com.groupchatback.entity.Invite;
import javax.persistence.EntityManager;
import javax.ws.rs.BadRequestException;
import java.util.List;

public interface InviteDao {
    Invite findInvite(EntityManager em, String inviteId) throws BadRequestException;
    List<Object[]> getInvites(String userId, boolean invitesReceived);
    void deleteInvite(String inviteId);
    long getReceivedInvitesCount(String userId);
}
