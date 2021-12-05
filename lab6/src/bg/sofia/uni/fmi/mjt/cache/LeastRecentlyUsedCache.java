package bg.sofia.uni.fmi.mjt.cache;

import bg.sofia.uni.fmi.mjt.cache.storage.Storage;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;

public class LeastRecentlyUsedCache<K, V> extends CacheBase<K, V> {
    public static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private final LinkedHashMap<K, V> cache;

    public LeastRecentlyUsedCache(Storage<K, V> storage, int capacity) {
        super(storage, capacity);
        this.cache = new LinkedHashMap<>(capacity, DEFAULT_LOAD_FACTOR, true);
    }

    @Override
    public int size() {
        return cache.size();
    }

    @Override
    public void clear() {
        super.resetHitRate();
        cache.clear();
    }

    @Override
    public Collection<V> values() {
        return this.cache.values();
    }

    public V getFromCache(K k) {
        return cache.get(k);
    }

    public V put(K k, V v) {
        return cache.put(k, v);
    }

    public boolean containsKey(K k) {
        return cache.containsKey(k);
    }

    public void evictFromCache() {
        Iterator<K> it = cache.keySet().iterator();
        if (it.hasNext()) {
            it.next();
            it.remove();
        }
    }

}
