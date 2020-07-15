package com.groupchatback.dao.interfaces;

import com.groupchatback.entity.Conversation;
import com.groupchatback.entity.ConversationMember;

import javax.persistence.EntityManager;
import javax.ws.rs.BadRequestException;
import java.util.Date;
import java.util.List;

public interface ConversationDao {
    Conversation findConversation(EntityManager em, String conversationId) throws BadRequestException;
    ConversationMember findConversationMember(EntityManager em, String userId, String conversationId) throws BadRequestException;
    List<Object[]> getConversations(String userId);
    List<Object[]> getUsersInConversationExceptHost(String conversationId, String hostId);
    Object[] getHostOfConversation(String conversationId);
    Date getDateJoined(EntityManager em, String conversationId, String userId);
    void saveConversationAndInviteUsers(String hostId, String conversationName, String[] invitedUsers, Date dateCreated);
    long getConversationNameCount(String conversationName);
    void joinConversationAsNewMember(String userId, String conversationId, Date dateJoined);
    void updateInChatStatus(String userId, String conversationId, boolean inChat);
    void leaveConversation(String userId, String conversationId);
    void deleteConversation(String userId, String conversationId);
    void updateConversationHost(String conversationId, String newHostId);
}
