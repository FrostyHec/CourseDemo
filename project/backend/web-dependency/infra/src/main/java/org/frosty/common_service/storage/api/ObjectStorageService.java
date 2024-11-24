package org.frosty.common_service.storage.api;

import java.io.InputStream;

public interface ObjectStorageService {
    void save(String key, Object value);

    void flowSave(String key, InputStream flow);

    <T> T get(String key, Class<T> type);

    InputStream flowGet(String key);

    void delete(String key);

    boolean exist(String key);

    String getAccessKey(String key, String caseName);

    void withdrawAccessKey(String key, String caseName);
}
