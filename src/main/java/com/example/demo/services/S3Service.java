package com.example.demo.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.exception.S3ServiceException;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectResponse;
import software.amazon.awssdk.services.s3.model.ListObjectsResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.model.S3Object;

import java.io.IOException;
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
    	try {
            Thread.sleep(1000); 
            ListObjectsResponse listObjectsResponse = s3Client.listObjects(b -> b.bucket(bucketName));
            return listObjectsResponse.contents().stream()
                    .map(S3Object::key)
                    .collect(Collectors.toList());
        } catch (Exception ex) {
            throw new S3ServiceException("Error occurred while listing S3 objects", ex);
        }
    }
    
    public void uploadObject(MultipartFile file) {
    	try {
            RequestBody requestBody = RequestBody.fromInputStream(file.getInputStream(), file.getSize());
            s3Client.putObject(PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(file.getOriginalFilename())
                    .build(), requestBody);
        } catch (IOException ex) {
            throw new S3ServiceException("Error occurred while uploading object", ex);
        }
    }
    
    public void updateObject(String objectKey, MultipartFile file) {
    	try {
            try {
                s3Client.headObject(HeadObjectRequest.builder()
                        .bucket(bucketName)
                        .key(objectKey)
                        .build());
            } catch (S3Exception e) {
                if (e.statusCode() == 404) {
                    throw new S3ServiceException("Object not found");
                }
                throw e;
            }

            HeadObjectResponse existingObjectMetadata = s3Client.headObject(HeadObjectRequest.builder()
                    .bucket(bucketName)
                    .key(objectKey)
                    .build());

            long existingSize = existingObjectMetadata.contentLength();
            long newSize = file.getSize();

            if (existingSize == newSize) {
                return;
            }

            RequestBody requestBody = RequestBody.fromInputStream(file.getInputStream(), file.getSize());
            s3Client.putObject(PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(objectKey)
                    .build(), requestBody);
        } catch (IOException ex) {
            throw new S3ServiceException("Error occurred while updating object", ex);
        }
    }

    public void deleteObject(String objectKey) {
        try {
            s3Client.deleteObject(DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(objectKey)
                    .build());

            Thread.sleep(1000); 
            
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt(); 
        } catch (S3Exception ex) {
            throw new S3ServiceException("Error occurred while deleting object", ex);
        }
    }

}