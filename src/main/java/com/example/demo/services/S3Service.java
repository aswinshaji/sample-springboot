package com.example.demo.services;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ListObjectsResponse;
import software.amazon.awssdk.services.s3.model.S3Object;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class S3Service {
    @Value("${aws.bucketName}")
    private String bucketName;

    private final S3Client s3Client;

    public S3Service(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public List<String> listObjects() {
        ListObjectsResponse listObjectsResponse = s3Client.listObjects(b -> b.bucket(bucketName));
        return listObjectsResponse.contents().stream()
                .map(S3Object::key)
                .collect(Collectors.toList());
    }
}