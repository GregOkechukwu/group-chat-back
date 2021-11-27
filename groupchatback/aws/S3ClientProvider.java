package com.groupchatback.aws;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import com.groupchatback.util.EnvConfigUtil;
import org.hibernate.sql.Delete;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class S3ClientProvider {
    private static AWSCredentialsProvider credentialsProvider;
    private static String bucketName;

    static {
        credentialsProvider = STSProvider.getCredentialsProvider();
        bucketName = EnvConfigUtil.getProperty("BUCKET_NAME");
    }

    private static AmazonS3 getProvider() {
        return  AmazonS3ClientBuilder.standard()
                .withCredentials(credentialsProvider)
                .withRegion(EnvConfigUtil.getProperty("REGION"))
                .build();
    }

    public static byte[] getImage(String key) throws IOException {
        AmazonS3 s3Client = getProvider();
        GetObjectRequest objectRequest = new GetObjectRequest(bucketName, key);

        S3ObjectInputStream objectContent = s3Client.getObject(objectRequest).getObjectContent();
        return IOUtils.toByteArray((InputStream)objectContent);
    }

    public static void putImage(String key, byte[] buffer, String mimeType) throws Exception {
        AmazonS3 s3Client = getProvider();
        InputStream inputStream = new ByteArrayInputStream(buffer);

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(buffer.length);
        metadata.setContentType(mimeType);
        metadata.setSSEAlgorithm(ObjectMetadata.AES_256_SERVER_SIDE_ENCRYPTION);

        PutObjectRequest objectRequest = new PutObjectRequest(bucketName, key, inputStream, metadata);
        s3Client.putObject(objectRequest);
    }

    public static void deleteImage(String key) throws Exception {
        AmazonS3 s3Client = getProvider();

        DeleteObjectRequest objectRequest = new DeleteObjectRequest(bucketName, key);
        s3Client.deleteObject(objectRequest);
    }

    public static String getMimeType(String key) throws Exception {
        AmazonS3 s3Client = getProvider();
        GetObjectMetadataRequest getObjectMetadataRequest = new GetObjectMetadataRequest(bucketName, key);

        ObjectMetadata metadata = s3Client.getObjectMetadata(getObjectMetadataRequest);
        return metadata.getContentType();
    }
}
