package org.frosty.object_storage.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.frosty.object_storage.config.CacheConfig;
import org.frosty.object_storage.entity.FlowWithMetadata;
import org.frosty.object_storage.service.access_key.AccessKeyService;
import org.frosty.object_storage.service.cache.KVCacheChain;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
@RequiredArgsConstructor
public class StorageService {
    private final AccessKeyService accessKeyService;
    private final CacheConfig cacheConfig;
    private KVCacheChain chain;

    @PostConstruct
    public void init(){
        chain = cacheConfig.storageCache();
    }

    public void uploadFile(String objectName, InputStream inputStream, String contentType) throws Exception {
        chain.putStream(objectName,new FlowWithMetadata(inputStream,contentType));
    }

    public InputStream getFile(String objectName) throws Exception {
        return chain.getStream(objectName).getStream();
    }

    public void deleteFile(String objectName) throws Exception {
        chain.remove(objectName);
    }

    public boolean checkFileExist(String objectName) throws Exception {
        return chain.contains(objectName);
    }

    public void deleteAccessKey(String objectKey, String caseName) {
        accessKeyService.withdraw(objectKey, caseName);
    }

    public String getAccessKey(String objectKey, String caseName) {
        return accessKeyService.getOrCreate(objectKey, caseName);
    }
}
