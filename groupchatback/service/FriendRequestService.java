package com.groupchatback.service;

import com.groupchatback.config.LocalMetaData;
import com.groupchatback.dao.interfaces.FriendRequestDao;
import com.groupchatback.dao.interfaces.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class FriendRequestService {

    @Autowired
    private FriendRequestDao friendRequestDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private LocalMetaData localBean;

    public void sendFriendRequests(List<String> usersToAdd, Date dateSent) {
        String decodedUserId = this.localBean.getDecodedUserId();
        this.friendRequestDao.saveFriendRequests(decodedUserId, usersToAdd, dateSent);
    }

    public List<Object[]> getFriendRequests(boolean getReceivedRequests) {
        String decodedUserId = this.localBean.getDecodedUserId();
        return this.friendRequestDao.getFriendRequests(decodedUserId, getReceivedRequests);
    }

    public void acceptFriendRequest(String friendRequestId, String senderId, Date dateAdded) {
        String decodedUserId = this.localBean.getDecodedUserId();
        this.userDao.addFriend(senderId, decodedUserId, dateAdded);
        this.friendRequestDao.deleteFriendRequest(friendRequestId);
    }

    public void declineFriendRequest(String friendRequestId) {
        this.friendRequestDao.deleteFriendRequest(friendRequestId);
    }
}
