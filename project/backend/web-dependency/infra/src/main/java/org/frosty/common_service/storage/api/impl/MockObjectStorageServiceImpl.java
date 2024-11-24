package org.frosty.common_service.storage.api.impl;

import org.frosty.common.exception.InternalException;
import org.frosty.common.utils.Pair;
import org.frosty.common_service.storage.api.ObjectStorageService;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

public class MockObjectStorageServiceImpl implements ObjectStorageService {
    private final Map<String, Object> storage = new HashMap<>();
    private final Map<Pair<String, String>, String> token = new HashMap<>();

    @Override
    public void save(String key, Object value) {
        storage.put(key, value);
    }

    @Override
    public void flowSave(String key, InputStream flow) {
        try {
            byte[] bytes = flow.readAllBytes();
            storage.put(key, bytes);
        } catch (Exception e) {
            throw new InternalException("Failed to read flow", e);
        }
    }

    @Override
    public <T> T get(String key, Class<T> type) {
        return type.cast(storage.get(key));
    }

    @Override
    public InputStream flowGet(String key) {
        var object = storage.get(key);
        if (object instanceof byte[] o) {
            return new ByteArrayInputStream(o);
        }
        try (var byteArrayOutputStream = new ByteArrayOutputStream();
             var objectOutputStream = new ObjectOutputStream(byteArrayOutputStream)) {

            objectOutputStream.writeObject(object);
            objectOutputStream.flush();
            objectOutputStream.close();
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            return new ByteArrayInputStream(byteArray);
        } catch (Exception e) {
            throw new InternalException("Failed to read object", e);
        }
    }

    @Override
    public void delete(String key) {
        storage.remove(key);
    }

    @Override
    public boolean exist(String key) {
        return storage.containsKey(key);
    }

    @Override
    public String getAccessKey(String key, String caseName) {
        token.put(new Pair<>(key, caseName), caseName);
        return caseName;
    }

    @Override
    public void withdrawAccessKey(String key, String caseName) {
        token.remove(new Pair<>(key, caseName));
    }

    public void clearAll() {
        storage.clear();
    }
}
