package com.example.oauthstarter.infrastructure.minio;

import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.errors.*;
import io.minio.http.Method;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class MinioService {
    private final MinioClient minioClient;
    @Value("${spring.minio.bucketName}")
    private String bucket;

    public MinioService(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    public String generatePresignedUrl(String fileName) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        log.info("File name: {}", fileName);
        var params = Map.of("Content-Type", "application/octet-stream");
        var args = GetPresignedObjectUrlArgs.builder()
                .method(Method.PUT)
                .bucket(bucket)
                .object(fileName)
                .expiry(2, TimeUnit.HOURS)
                .extraQueryParams(params)
                .build();
        return minioClient.getPresignedObjectUrl(args);
    }
}
