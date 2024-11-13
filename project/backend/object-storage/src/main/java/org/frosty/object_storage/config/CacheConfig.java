package org.frosty.object_storage.config;

import lombok.RequiredArgsConstructor;
import org.frosty.object_storage.service.cache.layer.EndOSSLayer;
import org.frosty.object_storage.service.cache.KVCacheChain;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
@RequiredArgsConstructor
public class CacheConfig {
    private final EndOSSLayer ossLayer;
    public KVCacheChain storageCache(){
        return new KVCacheChain(
                List.of(),
                ossLayer
        );
    }
}
