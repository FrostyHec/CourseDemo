package org.frosty.object_storage.service.access_key;

public interface AccessKeyService {
    String getOrCreate(String objectKey, String caseName);

    void withdraw(String objectKey, String caseName);

    boolean valid(String objName, String caseName, String accessKey);
}
