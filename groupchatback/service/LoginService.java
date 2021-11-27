package com.groupchatback.service;

import com.groupchatback.aws.CognitoIdentityProvider;
import com.groupchatback.config.LocalMetaData;
import com.groupchatback.dao.interfaces.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private LocalMetaData localBean;

    public String[] signIn(String usernameOrEmail, String password) throws Exception {
        return CognitoIdentityProvider.signInAndGetTokens(usernameOrEmail, password);
    }

    public String[] firstSignIn(String username, String newPassword, String tempPassword) throws Exception  {
        return CognitoIdentityProvider.firstSignInAndGetTokens(username, newPassword, tempPassword);
    }

    public String forgotPassword(String username) throws Exception {
        return CognitoIdentityProvider.forgotPassword(username);
    }

    public void confirmForgottenPassword(String username, String code, String newPassword) throws Exception {
        CognitoIdentityProvider.confirmForgotPassword(username, code, newPassword);
    }

    public void globalSignOut() throws Exception {
        String decodedUserId = this.localBean.getDecodedUserId();
        CognitoIdentityProvider.globalSignOut(decodedUserId);
    }

    public String[] checkPassword(String password) throws Exception {
        String decodedUserId = this.localBean.getDecodedUserId();
        return CognitoIdentityProvider.signInAndGetTokens(decodedUserId, password);
    }
}
