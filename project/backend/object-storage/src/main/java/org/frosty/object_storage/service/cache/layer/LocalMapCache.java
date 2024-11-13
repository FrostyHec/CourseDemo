package org.frosty.object_storage.service.cache.layer;

import org.frosty.object_storage.entity.FlowWithMetadata;
import org.frosty.object_storage.service.cache.KVStorage;

import java.io.InputStream;

public class LocalMapCache implements KVStorage<Object> {
    @Override
    public void put(String key, Object value) {

    }

    @Override
    public void putStream(String key, FlowWithMetadata in) {

    }

    @Override
    public Object get(String key) {
        return null;
    }

    @Override
    public FlowWithMetadata getStream(String key) {
        return null;
    }

    @Override
    public void remove(String key) {

    }

    @Override
    public boolean contains(String key) {
        return false;
    }
}
