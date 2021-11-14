package com.ikwattro.neo4j.protocol;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.S3Object;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

public class S3ProtocolHandler extends URLStreamHandler {

    @Override
    protected URLConnection openConnection(URL url) throws IOException {
        return new URLConnection(url) {

            @Override
            public InputStream getInputStream() throws IOException {

                //aws credentials
                String accessKey;
                String secretKey;
                String[] credentials = url.getUserInfo().split("[:]");
                accessKey = credentials[0];
                secretKey = credentials[1];

                String bucket = url.getHost().substring(0, url.getHost().indexOf("."));

                String key = url.getPath().substring(1);

                AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                        .withRegion("eu-central-1")
                        .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
                        .build();

                try {
                    S3Object s3Object = s3Client.getObject(bucket, key);

                    return new ByteArrayInputStream(s3Object.getObjectContent().readAllBytes());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void connect() throws IOException { }

        };
    }
}
