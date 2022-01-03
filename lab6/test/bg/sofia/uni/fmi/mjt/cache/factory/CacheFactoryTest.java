package bg.sofia.uni.fmi.mjt.cache;

import bg.sofia.uni.fmi.mjt.cache.factory.CacheFactory;
import bg.sofia.uni.fmi.mjt.cache.factory.EvictionPolicy;
import bg.sofia.uni.fmi.mjt.cache.storage.Storage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CacheFactoryTest {

    private static final int CAPACITY = 100;

    @Mock
    private Storage<String, String> storage;

    @Test
    public void testGetInstanceWhenCapacityIsNotPositiveThrowsException() {
        Assertions.assertThrows(IllegalArgumentException.class,
            () -> CacheFactory.getInstance(storage, -3, EvictionPolicy.LEAST_FREQUENTLY_USED));
    }

    @Test
    public void testGetInstanceReturnsCorrectInstance() {
        Cache<String, String> cache1 =
            CacheFactory.getInstance(storage, CAPACITY, EvictionPolicy.LEAST_FREQUENTLY_USED);
        Cache<String, String> cache2 = CacheFactory.getInstance(storage, CAPACITY, EvictionPolicy.LEAST_RECENTLY_USED);

        assertTrue(cache1 instanceof LeastFrequentlyUsedCache);
        assertTrue(cache2 instanceof LeastRecentlyUsedCache);
    }

    @Test
    public void testGetInstanceWithDefaultCapacityReturnsCorrectInstance() {
        Cache<String, String> cache1 = CacheFactory.getInstance(storage, EvictionPolicy.LEAST_FREQUENTLY_USED);
        Cache<String, String> cache2 = CacheFactory.getInstance(storage, EvictionPolicy.LEAST_RECENTLY_USED);

        assertTrue(cache1 instanceof LeastFrequentlyUsedCache);
        assertTrue(cache2 instanceof LeastRecentlyUsedCache);
    }
}
