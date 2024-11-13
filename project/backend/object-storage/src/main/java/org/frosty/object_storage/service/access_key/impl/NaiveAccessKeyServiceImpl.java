package org.frosty.object_storage.service.access_key.impl;


import org.frosty.common.utils.Pair;
import org.frosty.object_storage.service.access_key.AccessKeyService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class NaiveAccessKeyServiceImpl implements AccessKeyService {
    private final Map<Pair<String, String>, String> storage = new HashMap<>();

    @Override
    public String getOrCreate(String objectKey, String caseName) {
        var pair = new Pair<>(objectKey, caseName);
        if (storage.containsKey(pair)) {
            return storage.get(pair);
        }
        var accessKey = UUID.randomUUID().toString();
        storage.put(pair, accessKey);
        return accessKey;
    }

    @Override
    public void withdraw(String objectKey, String caseName) {
        storage.remove(new Pair<>(objectKey, caseName));
    }

    @Override
    public boolean valid(String objName, String caseName, String accessKey) {
        if (!storage.containsKey(new Pair<>(objName, caseName))) {
            return false;
        }
        return storage.get(new Pair<>(objName, caseName)).equals(accessKey);
    }
}
