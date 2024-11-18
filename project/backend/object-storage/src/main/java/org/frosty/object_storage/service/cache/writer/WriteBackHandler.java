package org.frosty.object_storage.service.cache.writer;

import org.frosty.object_storage.entity.FlowWithMetadata;
import org.frosty.object_storage.service.cache.KVCacheChain;

import java.io.InputStream;

public class WriteBackHandler implements WriteHandler{
    // TODO
    public static final WriteBackHandler instance = new WriteBackHandler();
    private WriteBackHandler() {
    }

    @Override
    public void write(KVCacheChain chain, String key, Object value) {

    }

    @Override
    public void writeStream(KVCacheChain kvCacheChain, String key, FlowWithMetadata in) {

    }
}

