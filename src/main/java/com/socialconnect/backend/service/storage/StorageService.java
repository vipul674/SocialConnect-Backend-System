package com.socialconnect.backend.service.storage;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

    String upload(String key, MultipartFile file);

    byte[] getObject(String key);

    void deleteObject(String key);

    String getObjectUrl(String key);
}
