package org.frosty.object_storage.service.cache.writer;

import org.frosty.object_storage.entity.FlowWithMetadata;
import org.frosty.object_storage.service.cache.KVCacheChain;

import java.io.InputStream;

public interface WriteHandler {
    void write(KVCacheChain chain, String key, Object value);

    void writeStream(KVCacheChain kvCacheChain, String key, FlowWithMetadata in);
}
