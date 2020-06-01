package com.groupchatback.aws;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProviderClientBuilder;
import com.amazonaws.services.cognitoidp.model.*;
import com.groupchatback.util.EnvConfigUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CognitoIdentityProvider {

    private static AWSCredentialsProvider credentialsProvider;
    private static String userPoolId;
    private static String clientId;

    static {
        credentialsProvider = STSProvider.getCredentialsProvider();
        userPoolId = EnvConfigUtil.getProperty("USER_POOL_ID");
        clientId = EnvConfigUtil.getProperty("CLIENT_ID");
    }

    private static AWSCognitoIdentityProvider getProvider() {
        return AWSCognitoIdentityProviderClientBuilder.standard()
                .withCredentials(credentialsProvider)
                .withRegion(EnvConfigUtil.getProperty("REGION"))
                .build();
    }

    private static void addUserToGroup(AWSCognitoIdentityProvider provider, String username) throws Exception {
        String groupName = EnvConfigUtil.getProperty("USER_POOL_GROUP_NAME");

        AdminAddUserToGroupRequest addUserToGroupRequest = new AdminAddUserToGroupRequest()
                .withGroupName(groupName)
                .withUserPoolId(userPoolId)
                .withUsername(username);

        provider.adminAddUserToGroup(addUserToGroupRequest);
    }

    public static String[] signInAndGetTokens(String usernameOrEmail, String password) throws Exception {
        AWSCognitoIdentityProvider cognitoIdentityProvider = getProvider();

        final Map<String, String> authParams = new HashMap<>();
        authParams.put("USERNAME", usernameOrEmail);
        authParams.put("PASSWORD", password);

        AdminInitiateAuthRequest authRequest = new AdminInitiateAuthRequest();

        authRequest.withAuthParameters(authParams)
                .withAuthFlow(AuthFlowType.ADMIN_NO_SRP_AUTH)
                .withUserPoolId(userPoolId)
                .withClientId(clientId);

        /** Null Pointer Exception Bug Here If Password is Invalid **/
        AdminInitiateAuthResult initiateAuthResult = cognitoIdentityProvider.adminInitiateAuth(authRequest);
        AuthenticationResultType result = initiateAuthResult.getAuthenticationResult();

        return new String[]{result.getAccessToken(), result.getRefreshToken()};
    }

    public static String[] firstSignInAndGetTokens(String userId, String newPassword, String tempPassword) throws Exception {
        AWSCognitoIdentityProvider cognitoIdentityProvider = getProvider();

        final Map<String, String> authParams = new HashMap<>();
        authParams.put("USERNAME", userId);
        authParams.put("PASSWORD", tempPassword);

        AdminInitiateAuthRequest authRequest = new AdminInitiateAuthRequest();

        authRequest.withAuthParameters(authParams)
                .withAuthFlow(AuthFlowType.ADMIN_NO_SRP_AUTH)
                .withUserPoolId(userPoolId)
                .withClientId(clientId);

        AdminInitiateAuthResult initiateAuthResult = cognitoIdentityProvider.adminInitiateAuth(authRequest);
        String challengeName = initiateAuthResult.getChallengeName();

        if (!StringUtils.equals(challengeName, "NEW_PASSWORD_REQUIRED")) {
            throw new NotAuthorizedException("Invalid Challenge");
        }

        Map<String, String> challengeResponses = new HashMap<>();
        challengeResponses.put("USERNAME", userId);
        challengeResponses.put("NEW_PASSWORD", newPassword);

        AdminRespondToAuthChallengeRequest authChallengeRequest = new AdminRespondToAuthChallengeRequest()
                .withSession(initiateAuthResult.getSession())
                .withClientId(clientId)
                .withUserPoolId(userPoolId)
                .withChallengeName(ChallengeNameType.NEW_PASSWORD_REQUIRED)
                .withChallengeResponses(challengeResponses);

        AdminRespondToAuthChallengeResult authChallengeResult = cognitoIdentityProvider.adminRespondToAuthChallenge(authChallengeRequest);
        AuthenticationResultType result = authChallengeResult.getAuthenticationResult();

        return new String[]{ result.getAccessToken(), result.getRefreshToken() };
    }

    public static String refreshToken(String refreshTkn) throws NotAuthorizedException {
        AWSCognitoIdentityProvider cognitoIdentityProvider = getProvider();

        final Map<String, String> authParams = new HashMap<>();
        authParams.put("REFRESH_TOKEN", refreshTkn);

        AdminInitiateAuthRequest authRequest = new AdminInitiateAuthRequest();

        authRequest.withAuthParameters(authParams)
                .withAuthFlow(AuthFlowType.REFRESH_TOKEN_AUTH)
                .withUserPoolId(userPoolId)
                .withClientId(clientId);

        AuthenticationResultType result = cognitoIdentityProvider
                .adminInitiateAuth(authRequest)
                .getAuthenticationResult();

        return result.getAccessToken();
    }

    public static UserType signUp(
            String userId,
            String username,
            String firstName,
            String lastName,
            String email
    ) throws Exception {
        AWSCognitoIdentityProvider cognitoIdentityProvider = getProvider();

        List<AttributeType> myAttributes = new ArrayList<>();

        myAttributes.add(new AttributeType().withName("preferred_username").withValue(username));
        myAttributes.add(new AttributeType().withName("email").withValue(email));
        myAttributes.add(new AttributeType().withName("name").withValue(firstName + " " + lastName));
        myAttributes.add(new AttributeType().withName("email_verified").withValue("true"));

        AdminCreateUserRequest cognitoRequest = new AdminCreateUserRequest()
                .withUserPoolId(userPoolId)
                .withUsername(userId)
                .withUserAttributes(myAttributes);

        AdminCreateUserResult createUserResult = cognitoIdentityProvider.adminCreateUser(cognitoRequest);
        addUserToGroup(cognitoIdentityProvider, username);

        return createUserResult.getUser();
    }

    public static String forgotPassword(String username) throws Exception {
        AWSCognitoIdentityProvider cognitoIdentityProvider = getProvider();

        ForgotPasswordRequest forgotPasswordRequest = new ForgotPasswordRequest()
                .withUsername(username)
                .withClientId(clientId);

        CodeDeliveryDetailsType codeDeliveryDetails = cognitoIdentityProvider.forgotPassword(forgotPasswordRequest).getCodeDeliveryDetails();

        String emailDestination = codeDeliveryDetails.getDestination();
        return emailDestination;
    }

    public static void confirmForgotPassword(String username, String code, String newPassword) throws Exception {
        AWSCognitoIdentityProvider cognitoIdentityProvider = getProvider();

        ConfirmForgotPasswordRequest forgotPasswordRequest = new ConfirmForgotPasswordRequest()
                .withUsername(username)
                .withClientId(clientId)
                .withConfirmationCode(code)
                .withPassword(newPassword);

        cognitoIdentityProvider.confirmForgotPassword(forgotPasswordRequest);
    }

    public static void deleteUser(String userId) throws Exception {
        AWSCognitoIdentityProvider cognitoIdentityProvider = getProvider();

        AdminDeleteUserRequest deleteUserRequest = new AdminDeleteUserRequest()
                .withUsername(userId)
                .withUserPoolId(userPoolId);

        cognitoIdentityProvider.adminDeleteUser(deleteUserRequest);
    }

    public static void updateUserInfo(String userId, Map<String, Object> attributeLookup) throws Exception {
        AWSCognitoIdentityProvider cognitoIdentityProvider = getProvider();
        List<AttributeType> attributeList = new ArrayList<>();

        for (Map.Entry<String, Object> entry : attributeLookup.entrySet()) {
            String attribute = entry.getKey();
            String value = (String)entry.getValue();

            attributeList.add(new AttributeType().withName(attribute).withValue(value));

            if (StringUtils.equals(attribute, "email")) {
                attributeList.add(new AttributeType().withName("email_verified").withValue("true"));
            }
        }

        AdminUpdateUserAttributesRequest updateAttributeRequest = new AdminUpdateUserAttributesRequest()
                .withUsername(userId)
                .withUserPoolId(userPoolId)
                .withUserAttributes(attributeList);

        cognitoIdentityProvider.adminUpdateUserAttributes(updateAttributeRequest);
    }

    public static void globalSignOut(String userId) throws Exception {
        AWSCognitoIdentityProvider cognitoIdentityProvider = getProvider();

        AdminUserGlobalSignOutRequest signOutRequest = new AdminUserGlobalSignOutRequest()
                .withUsername(userId)
                .withUserPoolId(userPoolId);

        cognitoIdentityProvider.adminUserGlobalSignOut(signOutRequest);
    }
}
