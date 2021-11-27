package com.groupchatback.service;

import com.groupchatback.aws.CognitoIdentityProvider;
import com.groupchatback.config.LocalMetaData;
import com.groupchatback.dao.interfaces.UserDao;
import com.groupchatback.util.CookieUtil;
import com.groupchatback.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import java.util.*;

@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private LoginService loginService;

    @Autowired
    private LocalMetaData localBean;

    private String getFullName(String username, String firstName, String lastName) {
        Object[] fullName = this.userDao.getFullName(username);
        String currFirstName = (String)fullName[0];
        String currLastName = (String)fullName[1];

        if (firstName == null) {
            firstName = currFirstName;
        }

        if (lastName == null) {
            lastName = currLastName;
        }

        return firstName.toLowerCase() + " " + lastName.toLowerCase();
    }

    public void postUserById(String firstName, String lastName, String email, Date dateCreated) {
        String decodedUserId = this.localBean.getDecodedUserId();
        firstName = firstName.toLowerCase();
        lastName = lastName.toLowerCase();
        email = email.toLowerCase();

        this.userDao.saveUser(decodedUserId, firstName, lastName, email, dateCreated);
    }

    public Object[] getUserById() {
        String decodedUserId = this.localBean.getDecodedUserId();
        return this.userDao.getUser(decodedUserId);
    }

    public List<Object[]> getAllUsers(String usernameSubstr, String firstNameSubstr, String lastNameSubstr, boolean includeFriends) {
        String decodedUserId = this.localBean.getDecodedUserId();

        usernameSubstr = usernameSubstr == null ? null : usernameSubstr.toLowerCase();
        firstNameSubstr = firstNameSubstr == null ? null : firstNameSubstr.toLowerCase();
        lastNameSubstr = lastNameSubstr == null ? null : lastNameSubstr.toLowerCase();

        return includeFriends ?
                this.userDao.getAllUsersIncludingFriends(decodedUserId, usernameSubstr, firstNameSubstr, lastNameSubstr) :
                this.userDao.getAllUsersExcludingFriends(decodedUserId, usernameSubstr, firstNameSubstr, lastNameSubstr);
    }

    public List<Object[]> getAllFriends(String usernameSubstr, String firstNameSubstr, String lastNameSubstr) {
        String decodedUserId = this.localBean.getDecodedUserId();

        usernameSubstr = usernameSubstr == null ? null : usernameSubstr.toLowerCase();
        firstNameSubstr = firstNameSubstr == null ? null : firstNameSubstr.toLowerCase();
        lastNameSubstr = lastNameSubstr == null ? null : lastNameSubstr.toLowerCase();

        return this.userDao.getAllFriends(decodedUserId, usernameSubstr, firstNameSubstr, lastNameSubstr);
    }

    public List<Object[]> getUsersInConversation(String conversationId) {
        return this.userDao.getUsersInConversation(conversationId);
    }

    public void deleteUserById(String userId) throws Exception {
        String decodedUserId = this.localBean.getDecodedUserId();
        boolean isAuthenticated = decodedUserId != null;

        CognitoIdentityProvider.deleteUser(isAuthenticated ? decodedUserId : userId);
        this.userDao.deleteUser(isAuthenticated ? decodedUserId : userId);
    }

    public void updateOnlineStatus(boolean isOnline) {
        String decodedUserId = this.localBean.getDecodedUserId();
        Map<String, Object> attributeLookup = new HashMap<>();

        attributeLookup.put("isOnline", isOnline);
        this.userDao.updateUser(decodedUserId, attributeLookup);
    }

    public String[] updateUserInfo(String newUsername, String newFirstName, String newLastName, String newEmail, String password) throws Exception {
        String decodedUserId = this.localBean.getDecodedUserId();
        Map<String, Object> attributeLookup = new HashMap<>();

        if (newUsername != null) {
            attributeLookup.put("preferred_username", newUsername);
        }

        if (newEmail != null) {
            attributeLookup.put("email", newEmail);
        }

        if (newFirstName != null || newLastName != null) {
            String fullName = getFullName(decodedUserId, newFirstName, newLastName);
            attributeLookup.put("name", fullName);
        }

        String usernameOrEmail = newUsername != null ? newUsername : newEmail != null ? newEmail : decodedUserId;

        CognitoIdentityProvider.updateUserInfo(decodedUserId, attributeLookup);
        this.userDao.updateUser(decodedUserId, attributeLookup);

        return this.loginService.signIn(usernameOrEmail, password);
    }

    public boolean isAuthenticated(Cookie[] cookies) {
        if (cookies == null || cookies.length == 0) {
            return false;
        }

        Cookie accessCookie = CookieUtil.getCookie(cookies, "ACCESS_TKN");
        String accessTknBase64 = accessCookie != null ? accessCookie.getValue() : null;

        return accessTknBase64 == null ? false : TokenUtil.isValid(accessTknBase64);
    }

    public boolean emailIsTaken(String email) {
        return this.userDao.getEmailCount(email) == 1;
    }

    public boolean usernameIsTaken(String username) {
        return this.userDao.getUsernameCount(username.toLowerCase()) == 1;
    }

    public void removeFriend(String friendId) {
        String decodedUserId = this.localBean.getDecodedUserId();
        this.userDao.removeFriend(decodedUserId, friendId);
    }
}
