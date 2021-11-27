package com.groupchatback.service;

import com.groupchatback.config.LocalMetaData;
import com.groupchatback.dao.interfaces.ConversationDao;
import com.groupchatback.util.DaoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.*;

@Service
public class ConversationService {

    @Autowired
    private ConversationDao conversationDao;

    @Autowired
    private LocalMetaData localBean;

    private Map<String, Date> getDateJoinedLookup(List<Object[]> userRecords, String conversationId) {
        Map<String, Date> dateJoinedLookup = new HashMap<>();
        int n = userRecords.size();

        EntityManager em = DaoUtil.getEntityManagerFactory().createEntityManager();

        for (int i = 0; i < n; i++) {
            String userId = (String)userRecords.get(i)[0];
            Date conversationJoinDate = this.conversationDao.getDateJoined(em, conversationId, userId);
            dateJoinedLookup.put(userId, conversationJoinDate);
        }

        return dateJoinedLookup;
    }

    private List<Object[]> getUsersSortedByPicStatusAndTenure(String conversationId, String hostId) {
        List<Object[]> userIds = this.conversationDao.getUsersInConversationExceptHost(conversationId, hostId);
        Map<String, Date> dateJoinedLookup = this.getDateJoinedLookup(userIds, conversationId);

        Collections.sort(userIds, (Object[] recordA, Object[] recordB) -> {
            String userIdA = (String)recordA[0];
            String userIdB = (String)recordB[0];

            boolean aHasProfilePic = (boolean)recordA[4];
            boolean bHasProfilePic = (boolean)recordB[4];

            Date aDateJoined = dateJoinedLookup.get(userIdA);
            Date bDateJoined = dateJoinedLookup.get(userIdB);

            return aHasProfilePic && !bHasProfilePic ? -1 : !aHasProfilePic && bHasProfilePic ? 1 : (int)aDateJoined.getTime() - (int)bDateJoined.getTime();
        });

        return userIds;
    }

    /**
     *  Given a conversation name, return the profile pics of at most three users in the conversation. If they are less than three users in the conversation return K profile pics where K < 3.
     *  Aside from the host, prioritize users with profile pics. If more than 3 users have profile pics you want to prioritize users that has been in the conversation the longest.
     *
     *  Constraints : the host will always be a user in the conversation.
     *
     * Approach : Custom Sort with Comparator:
     *
     * Query the database to get the hostId of the conversation
     * Query the database to get the list of userId in the conversation
     * Get the profile pic of the user
     * Scan the userIds and query the database to retrieve the date in which the each of the users have joined the conversation. Also retrieve a flag letting us know if the user has a profile pic or not.
     * Sort the list of users using a comparator prioritizing users with profile pics. If two users do have profile pics then prioritize the user that has been in the conversation longer.
     * Get the profile pic of the first two users after sorting
     */

    public List<Object[]> getPrioritizedUsersInConversation(String conversationId) {
        List<Object[]> prioritizedUsers = new ArrayList<>();

        Object[] hostRecord = this.conversationDao.getHostOfConversation(conversationId);
        prioritizedUsers.add(hostRecord);

        List<Object[]> sortedUserIds = this.getUsersSortedByPicStatusAndTenure(conversationId, (String)hostRecord[0]);
        int n = sortedUserIds.size();

        Object[] userRecordA = n == 0 ? null : sortedUserIds.get(0);
        Object[] userRecordB = n < 2 ? null : sortedUserIds.get(1);

        prioritizedUsers.add(userRecordA);
        prioritizedUsers.add(userRecordB);

        return prioritizedUsers;
    }

    public List<Object[]> getConversations() {
        String decodedUserId = this.localBean.getDecodedUserId();
        return this.conversationDao.getConversations(decodedUserId);
    }

    public void postConversation(String name, String[] users, Date dateCreated) {
        String decodedUserId = this.localBean.getDecodedUserId();
        this.conversationDao.saveConversationAndInviteUsers(decodedUserId, name, users, dateCreated);
    }

    public boolean conversationNameIsTaken(String conversationName) {
        return this.conversationDao.getConversationNameCount(conversationName) == 1;
    }

    public void joinConversationAsNewMember(String conversationId, Date dateJoined) {
        String decodedUserId = this.localBean.getDecodedUserId();
        this.conversationDao.joinConversationAsNewMember(decodedUserId, conversationId, dateJoined);
    }

    public void updateInChatStatus(String conversationid, boolean inChat) {
        String decodedUserId = this.localBean.getDecodedUserId();
        this.conversationDao.updateInChatStatus(decodedUserId, conversationid, inChat);
    }

    public void leaveConversation(String conversationId) {
        String decodedUserId = this.localBean.getDecodedUserId();
        this.conversationDao.leaveConversation(decodedUserId, conversationId);
    }

    public void deleteConversation(String conversationId) {
        String decodedUserId = this.localBean.getDecodedUserId();
        this.conversationDao.deleteConversation(decodedUserId, conversationId);
    }

    public void updateConversationHost(String converstaionId, String newHostId) {
        this.conversationDao.updateConversationHost(converstaionId, newHostId);
    }

}
