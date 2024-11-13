package org.frosty.object_storage.service.cache;

import org.frosty.object_storage.entity.FlowWithMetadata;

import java.io.InputStream;

public interface KVStorage {
    // NOT SUPPORT TO STORE A NULL VALUE!
    // 准则：如果KVStorage服务发生了异常，那重启时应该清空全部的cache，因为并没有机制保证重启后上下层的cache一致
    void put(String key, Object value);
    void putStream(String key, FlowWithMetadata in);
    Object get(String key);
    FlowWithMetadata getStream(String key);
    void remove(String key);

    boolean contains(String key);
}
