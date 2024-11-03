package org.frosty.common_service.storage.api;

public interface SharedBiMapService {
    void init(String baseKeyName);
    void put(String key, String value);
    String get(String key);
    void delete(String key);
    boolean exist(String key);
    String getOrDefault(String key, String defaultValue);
    String getKeyByValue(String value);
    boolean valueExist(String name);
}
