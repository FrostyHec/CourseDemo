package org.frosty.common_service.storage.api.impl;

import com.google.common.collect.HashBiMap;
import org.frosty.common.exception.InternalException;
import org.frosty.common.utils.Ex;
import org.frosty.common_service.storage.api.SharedBiMapService;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class MockSharedBiMapServiceImpl implements SharedBiMapService {
    private String baseKeyName;
    private boolean isInit = false;
    private static final HashBiMap<String, String> biMap = HashBiMap.create();

    @Override
    public void init(String baseKeyName) {
        Ex.check(!isInit, new InternalException("already-init"));
        isInit = true;
        this.baseKeyName = baseKeyName+".";
    }

    @Override
    public void put(String key, String value) {
        Ex.check(isInit, new InternalException("not-init"));
        biMap.put(baseKeyName+key, value);
    }

    @Override
    public String get(String key) {
        Ex.check(isInit, new InternalException("not-init"));
        return biMap.get(baseKeyName+key);
    }

    @Override
    public void delete(String key) {
        Ex.check(isInit, new InternalException("not-init"));
        biMap.remove(baseKeyName+key);
    }

    @Override
    public boolean exist(String key) {
        Ex.check(isInit, new InternalException("not-init"));
        return biMap.containsKey(baseKeyName+key);
    }

    @Override
    public String getOrInsert(String key, String insertValue) {
        Ex.check(isInit, new InternalException("not-init"));
        key = baseKeyName+key;
        if(biMap.containsKey(key)){
            return biMap.get(key);
        }
        biMap.put(key,insertValue);
        return insertValue;
    }

    @Override
    public String getKeyByValue(String value) {
        Ex.check(isInit, new InternalException("not-init"));
        return biMap.inverse().get(value);
    }

    @Override
    public boolean valueExist(String name) {
        Ex.check(isInit, new InternalException("not-init"));
        return biMap.inverse().containsKey(name);
    }

}
