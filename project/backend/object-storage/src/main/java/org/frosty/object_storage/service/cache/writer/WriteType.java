package org.frosty.object_storage.service.cache.writer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.frosty.object_storage.entity.FlowWithMetadata;
import org.frosty.object_storage.service.cache.KVCacheChain;

import java.io.InputStream;

@AllArgsConstructor
public enum WriteType implements WriteHandler{
    // TODO
    WRITE_THROUGH(WriteThroughHandler.instance),
    WRITE_BACK(WriteBackHandler.instance),
    WRITE_THROUGH_IGNORE_CACHE_FAILURE(WriteThroughIgnoreCacheFailureHandler.instance);

    private final WriteHandler handler;

    @Override
    public void write(KVCacheChain chain, String key, Object value) {
        handler.write(chain,key,value);
    }

    @Override
    public void writeStream(KVCacheChain kvCacheChain, String key, FlowWithMetadata in) {
        handler.writeStream(kvCacheChain,key,in);
    }

}
    
