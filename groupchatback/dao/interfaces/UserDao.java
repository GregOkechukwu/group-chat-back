package com.groupchatback.dao.interfaces;

import com.groupchatback.entity.User;
import com.groupchatback.entity.UserFriend;

import javax.persistence.EntityManager;
import javax.ws.rs.BadRequestException;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface UserDao {
    User findUser(EntityManager em, String userId) throws BadRequestException;
    UserFriend findUserFriend(EntityManager em, String userFriendId) throws BadRequestException;
    Object[] getFullName(String userId);
    Object[] getUser(String userId);
    List<Object[]> getAllUsersIncludingFriends(String userId, String usernameSubstr, String firstNameSubstr, String lastNameSubstr);
    List<Object[]> getAllUsersExcludingFriends(String userId, String usernameSubstr, String firstNameSubstr, String lastNameSubstr);
    List<Object[]> getAllFriends(String userId, String usernameSubstr, String firstNameSubstr, String lastNameSubstr);
    List<Object[]> getUsersInConversation(String conversationId);
    void saveUser(String userId, String firstName, String lastName, String email, Date dateCreated);
    long getEmailCount(String email);
    long getUsernameCount(String username);
    void deleteUser(String userId);
    boolean hasProfilePic(String userId);
    void updateUser(String userId, Map<String, Object> attributes);
    void addFriend(String userId, String friendId, Date dateAdded);
    void removeFriend(String userId, String friendId);
}
