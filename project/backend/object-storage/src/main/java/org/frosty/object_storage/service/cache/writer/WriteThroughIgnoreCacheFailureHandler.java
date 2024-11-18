package org.frosty.object_storage.service.cache.writer;

import org.frosty.object_storage.entity.FlowWithMetadata;
import org.frosty.object_storage.service.cache.KVCacheChain;

import java.io.InputStream;

public class WriteThroughIgnoreCacheFailureHandler implements WriteHandler{
    // TODO
    public static final WriteThroughIgnoreCacheFailureHandler instance = new WriteThroughIgnoreCacheFailureHandler();
    private WriteThroughIgnoreCacheFailureHandler() {
    }

    @Override
    public void write(KVCacheChain chain, String key, Object value) {
        chain.getEnd().put(key,value);
        // TODO cache
    }

    @Override
    public void writeStream(KVCacheChain chain, String key, FlowWithMetadata in) {
        // TODO cache
        chain.getEnd().putStream(key,in);
    }
}
