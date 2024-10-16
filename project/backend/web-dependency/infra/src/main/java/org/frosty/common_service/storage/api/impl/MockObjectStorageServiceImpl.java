package org.frosty.common_service.storage.api.impl;

import org.frosty.common_service.storage.api.ObjectStorageService;

import java.util.HashMap;
import java.util.Map;

public class MockObjectStorageServiceImpl implements ObjectStorageService {
    private final Map<String,Object> storage = new HashMap<>();
    @Override
    public void save(String key, Object value) {
        storage.put(key,value);
    }

    @Override
    public <T> T get(String key, Class<T> type) {
        return type.cast(storage.get(key));
    }

    @Override
    public void delete(String key) {
        storage.remove(key);
    }

    @Override
    public boolean exist(String key) {
        return storage.containsKey(key);
    }

    public void clearAll(){
        storage.clear();
    }
}
