package org.frosty.object_storage.service.cache;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.function.TriConsumer;
import org.frosty.common.exception.InternalException;
import org.frosty.common.utils.Ex;
import org.frosty.object_storage.entity.FlowWithMetadata;
import org.frosty.object_storage.service.cache.writer.WriteType;

import java.io.InputStream;
import java.util.List;
import java.util.function.BiFunction;

@Slf4j
@AllArgsConstructor
@Data
public class KVCacheChain implements KVStorage {
    private List<KVStorage> cachesLayer;
    private KVStorage end;
    private WriteType writeType = WriteType.WRITE_THROUGH_IGNORE_CACHE_FAILURE;
//    private boolean containsCheckFlushCache = true;
    private boolean containsCheckFlushCache = false;
    public KVCacheChain(List<KVStorage> cachesLayer, KVStorage end) {
        this.cachesLayer = cachesLayer;
        this.end = end;
    }


    @Override
    public void put(String key, Object value) {
        Ex.check(value != null, new InternalException("kvstorage value cannot be null! key:" + key));
        writeType.write(this, key, value);
    }

    @Override
    public void putStream(String key, FlowWithMetadata in) {
        writeType.writeStream(this,key,in);
    }

    @Override
    public Object get(String key){
        return retrieveFromCache(key, KVStorage::get, KVStorage::put);
    }


    @Override
    public FlowWithMetadata getStream(String key) {
        return retrieveFromCache(key, KVStorage::getStream, KVStorage::putStream);
    }

    private <T> T retrieveFromCache(String key,
                                    BiFunction<KVStorage, String, T> getter,
                                    TriConsumer<KVStorage, String, T> setter) {
        T result = null;
        int idx = 0;
        // 查找缓存层
        for (; idx < cachesLayer.size(); idx++) {
            KVStorage currentLayer = cachesLayer.get(idx);
            result = getter.apply(currentLayer, key);
            if (result != null) {
                break;
            }
        }

        // 如果在缓存层未找到，则从end获取
        if (result == null) {
            result = getter.apply(end, key);
        }

        // 回溯更新缓存
        if (result != null) {
            for (idx -= 1; idx >= 0; idx--) {
                KVStorage currentLayer = cachesLayer.get(idx);
                if (!currentLayer.contains(key)) {
                    setter.accept(currentLayer,key,result);
                } else {
                    log.warn("get | push | get scenario occurs!");
                }
            }
        }
        return result;
    }



//    @Override
//    public Object get(String key) {
//        Object object = null;
//        int idx = 0;
//        for (; idx < cachesLayer.size(); idx++) {
//            KVStorage currentLayer = cachesLayer.get(idx);
//            object = currentLayer.get(key);
//            if (object != null) {
//                break;
//            }
//        }
//        if (object == null) {
//            object = end.get(key);
//        }
//        // 回溯更新缓存
//        if (object != null) {
//            for (idx -= 1; idx >= 0; idx--) {
//                KVStorage currentLayer = cachesLayer.get(idx);
//                if (!currentLayer.contains(key)) {
//                    //这个检查是防止兵法异常的，设想这样一个场景
//                    // get |  push (更新完全部新的+缓存) | 回溯更新<=如果此时再覆盖旧的就会造成缓存不一致。
//                    currentLayer.put(key, object);
//                } else {
//                    log.warn("get | push | get scenario occurs!");
//                }
//            }
//        }
//        return object;
//    }
//
//    @Override
//    public InputStream getStream(String key) {
//        InputStream flow = null;
//        int idx = 0;
//        for (; idx < cachesLayer.size(); idx++) {
//            KVStorage currentLayer = cachesLayer.get(idx);
//            flow = currentLayer.getStream(key);
//            if (flow != null) {
//                break;
//            }
//        }
//        if (flow == null) {
//            flow = end.getStream(key);
//        }
//        // 回溯更新缓存
//        if (flow != null) {
//            for (idx -= 1; idx >= 0; idx--) {
//                KVStorage currentLayer = cachesLayer.get(idx);
//                if (!currentLayer.contains(key)) {
//                    //这个检查是防止兵法异常的，设想这样一个场景
//                    // get |  push (更新完全部新的+缓存) | 回溯更新<=如果此时再覆盖旧的就会造成缓存不一致。
//                    currentLayer.putStream(key, flow);
//                } else {
//                    log.warn("get | push | get scenario occurs!");
//                }
//            }
//        }
//        return flow;
//    }

//    private Object getObject(int idx, String key) {
//        // 逐个层级获取，并且更新上层缓存
//        if (idx == cachesLayer.size()) {
//            return end.get(key);
//        }
//        KVStorage currentLayer = cachesLayer.get(idx);
//        Object object = currentLayer.get(key);
//        if (object != null) return object;
//        object = getObject(idx + 1, key);
//        if (object != null) {
//            currentLayer.put(key, object);
//        }
//        return object;
//    }

    @Override
    public void remove(String key) {
        for (KVStorage storage : cachesLayer) {
            storage.remove(key);
        }
        end.remove(key);
    }

    @Override
    public boolean contains(String key) {
        if(containsCheckFlushCache) return get(key)!=null;
        for (KVStorage storage : cachesLayer) {
            if(storage.contains(key)){
                return true;
            }
        }
        return end.contains(key);
    }
}
