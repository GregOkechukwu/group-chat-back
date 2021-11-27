package com.groupchatback.aws;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicSessionCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.securitytoken.AWSSecurityTokenService;
import com.amazonaws.services.securitytoken.AWSSecurityTokenServiceClientBuilder;
import com.amazonaws.services.securitytoken.model.AssumeRoleRequest;
import com.amazonaws.services.securitytoken.model.Credentials;
import com.groupchatback.util.EnvConfigUtil;

public class STSProvider {
    private static AWSCredentialsProvider credentialsProvider;

    private static BasicSessionCredentials getSessionCredentials(String roleType, String sessionType) {
        AWSSecurityTokenService sts = getSecurityTokenService();
        AssumeRoleRequest roleRequest = getRoleRequest(roleType, sessionType);

        Credentials tempCredentials =  sts.assumeRole(roleRequest).getCredentials();

        return new BasicSessionCredentials(
                tempCredentials.getAccessKeyId(),
                tempCredentials.getSecretAccessKey(),
                tempCredentials.getSessionToken()
        );
    }

    private static AWSSecurityTokenService getSecurityTokenService() {
        final ProfileCredentialsProvider longLivedCred = new ProfileCredentialsProvider();

        return AWSSecurityTokenServiceClientBuilder.standard()
                .withRegion(EnvConfigUtil.getProperty("REGION"))
                .withCredentials(longLivedCred)
                .build();
    }

    private static AssumeRoleRequest getRoleRequest(String roleType, String sessionType) {
        final String roleArn = EnvConfigUtil.getProperty(roleType);
        final String sessionId = EnvConfigUtil.getProperty(sessionType);

        return  new AssumeRoleRequest()
                .withRoleArn(roleArn)
                .withRoleSessionName(sessionId)
                .withDurationSeconds(3600);
    }

    public static AWSCredentialsProvider getCredentialsProvider() {
        if (credentialsProvider != null) {
            credentialsProvider.refresh();
        } else {
            BasicSessionCredentials sessionCredentials = STSProvider.getSessionCredentials("BACKEND_ROLE_ARN", "BACKEND_SESSION_ID");
            credentialsProvider = new AWSStaticCredentialsProvider(sessionCredentials);
        }

        return credentialsProvider;
    }

}
