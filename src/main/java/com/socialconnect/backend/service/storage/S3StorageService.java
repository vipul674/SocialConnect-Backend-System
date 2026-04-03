package com.socialconnect.backend.service.storage;

import com.socialconnect.backend.config.S3Properties;
import com.socialconnect.backend.exception.StorageException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.IOException;
import java.util.UUID;

@Service
public class S3StorageService implements StorageService {

    private final S3Client s3Client;
    private final S3Properties s3Properties;

    public S3StorageService(S3Client s3Client, S3Properties s3Properties) {
        this.s3Client = s3Client;
        this.s3Properties = s3Properties;
    }

    @Override
    public String upload(String key, MultipartFile file) {
        String objectKey = StringUtils.hasText(key) ? key : UUID.randomUUID().toString();

        try {
            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(s3Properties.getS3().getBucket())
                    .key(objectKey)
                    .contentType(file.getContentType())
                    .build();

            s3Client.putObject(request, RequestBody.fromBytes(file.getBytes()));
            return objectKey;
        } catch (IOException | S3Exception ex) {
            throw new StorageException("Failed to upload object to S3", ex);
        }
    }

    @Override
    public byte[] getObject(String key) {
        try (ResponseInputStream<GetObjectResponse> objectStream = s3Client.getObject(
                GetObjectRequest.builder()
                        .bucket(s3Properties.getS3().getBucket())
                        .key(key)
                        .build())) {
            return objectStream.readAllBytes();
        } catch (IOException | S3Exception ex) {
            throw new StorageException("Failed to read object from S3", ex);
        }
    }

    @Override
    public void deleteObject(String key) {
        try {
            s3Client.deleteObject(DeleteObjectRequest.builder()
                    .bucket(s3Properties.getS3().getBucket())
                    .key(key)
                    .build());
        } catch (S3Exception ex) {
            throw new StorageException("Failed to delete object from S3", ex);
        }
    }

    @Override
    public String getObjectUrl(String key) {
        String bucket = s3Properties.getS3().getBucket();
        String region = s3Properties.getRegion();

        if (StringUtils.hasText(region)) {
            return String.format("https://%s.s3.%s.amazonaws.com/%s", bucket, region, key);
        }
        return String.format("https://%s.s3.amazonaws.com/%s", bucket, key);
    }
}
