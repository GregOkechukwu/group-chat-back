package com.groupchatback.dao.interfaces;

import com.groupchatback.entity.FriendRequest;

import javax.persistence.EntityManager;
import java.util.Date;
import java.util.List;

public interface FriendRequestDao {
    FriendRequest findFriendRequest(EntityManager em, String friendRequestId);
    void saveFriendRequests(String senderId, List<String> usersToAdd, Date dateSent);
    List<Object[]> getFriendRequests(String userId, boolean getReceivedRequests);
    void deleteFriendRequest(String friendRequestId);
}
