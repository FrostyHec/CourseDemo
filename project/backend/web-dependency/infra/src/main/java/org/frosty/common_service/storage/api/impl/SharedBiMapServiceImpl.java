package org.frosty.common_service.storage.api.impl;

import org.frosty.common_service.storage.api.SharedBiMapService;

public class SharedBiMapServiceImpl implements SharedBiMapService {
    //  TODO connect to redis
    @Override
    public void init(String baseKeyName) {

    }

    @Override
    public void put(String key, String value) {

    }

    @Override
    public String get(String key) {
        return null;
    }

    @Override
    public void delete(String key) {

    }

    @Override
    public boolean exist(String key) {
        return false;
    }

    @Override
    public String getOrInsert(String key, String defaultValue) {
        return null;
    }

    @Override
    public String getKeyByValue(String value) {
        return null;
    }

    @Override
    public boolean valueExist(String name) {
        return false;
    }
}
