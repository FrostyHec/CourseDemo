package org.frosty.common_service.storage.api;

public interface ObjectStorageService {
    void save(String key, Object value);

    <T> T get(String key,Class<T> type);

    void delete(String key);

    boolean exist(String key);
}
