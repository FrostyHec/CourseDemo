package org.frosty.common_service.storage.api.impl;

import com.google.common.collect.HashBiMap;
import org.frosty.common_service.storage.api.SharedBiMapService;
import org.springframework.stereotype.Component;

@Component
public class MockSharedBiMapServiceImpl implements SharedBiMapService {
    private String baseKeyName;

    private final HashBiMap<String, String> biMap = HashBiMap.create();

    @Override
    public void init(String baseKeyName) {
        this.baseKeyName = baseKeyName+".";
    }

    @Override
    public void put(String key, String value) {
        biMap.put(baseKeyName+key, value);
    }

    @Override
    public String get(String key) {
        return biMap.get(baseKeyName+key);
    }

    @Override
    public void delete(String key) {
        biMap.remove(baseKeyName+key);
    }

    @Override
    public boolean exist(String key) {
        return biMap.containsKey(baseKeyName+key);
    }

    @Override
    public String getOrInsert(String key, String insertValue) {
        key = baseKeyName+key;
        if(biMap.containsKey(key)){
            return biMap.get(key);
        }
        biMap.put(key,insertValue);
        return insertValue;
    }

    @Override
    public String getKeyByValue(String value) {
        return biMap.inverse().get(value);
    }

    @Override
    public boolean valueExist(String name) {
        return biMap.inverse().containsKey(name);
    }

}
