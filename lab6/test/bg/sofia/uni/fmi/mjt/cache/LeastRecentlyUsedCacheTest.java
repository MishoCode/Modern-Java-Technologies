package bg.sofia.uni.fmi.mjt.cache;

import bg.sofia.uni.fmi.mjt.cache.exception.ItemNotFound;
import bg.sofia.uni.fmi.mjt.cache.storage.Storage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LeastRecentlyUsedCacheTest {

    @Mock
    private Storage<String, String> storageMock;

    private CacheBase<String, String> cache;

    @BeforeEach
    void setUp() {
        cache = new LeastRecentlyUsedCache<>(storageMock, 4);
    }

    @Test
    public void testSizeSuccessSuccessfully() throws ItemNotFound {
        cache.put("key", "value");
        int expected = 1;
        int actual = cache.size();

        assertEquals(expected, actual, "size does not return the correct size");
    }

    @Test
    public void testGetKeyWhenKeyIsNull() {
        assertThrows(IllegalArgumentException.class, () -> cache.get(null),
            "Illegal argument exception is thrown when the argument is null");
    }

    @Test
    public void testGetKeyWhenKeyNotFoundInCacheThrowsException() {
        assertThrows(ItemNotFound.class, () -> cache.get("key1"),
            "ItemNotFound exception is thrown when the key is not present");
    }

    @Test
    public void testGetKeyAddsToCacheStorageValue() throws ItemNotFound {
        when(storageMock.retrieve("key1")).thenReturn("value1");

        String actual = cache.get("key1");
        String expected = "value1";

        assertEquals(expected, actual);
    }

    @Test
    public void testGetKeyWhenEvictIsRequired() throws ItemNotFound {
        cache.put("key1", "value1");
        cache.put("key2", "value2");
        cache.put("key3", "value3");
        cache.put("key4", "value4");

        when(storageMock.retrieve("key5")).thenReturn("value5");

        String actual = cache.get("key5");
        String expected = "value5";

        assertEquals(actual, expected);
        verify(storageMock).retrieve("key5");
    }

    @Test
    public void testGetKeySuccessfully() throws ItemNotFound {
        cache.put("key1", "value1");
        String actual = cache.get("key1");
        String expected = "value1";

        assertEquals(actual, expected, "getKey does not return the correct value");
    }

    @Test
    public void testGetHitRateSuccessfully() throws ItemNotFound {
        cache.put("key1", "value1");
        cache.put("key2", "value2");
        cache.put("key3", "value3");


        String value1 = cache.get("key1");
        String value2 = cache.get("key2");
        try {
            String value3 = cache.get("key");
        } catch (ItemNotFound e) {
            assertEquals(cache.getHitRate(), 0.66, 0.01);
            verify(storageMock).retrieve("key");
        }
    }

    @Test
    public void testClearSuccessfully() {
        cache.put("key1", "value1");
        cache.put("key2", "value2");
        cache.put("key3", "value3");

        cache.clear();
        assertEquals(cache.size(), 0, "clear does not clear the content properly");
    }

    @Test
    public void testClearResetsTheHitRateSuccessfully() throws ItemNotFound {
        cache.put("key1", "value1");
        cache.put("key2", "value2");
        cache.put("key3", "value3");


        String value1 = cache.get("key1");
        String value2 = cache.get("key2");

        cache.clear();
        assertEquals(cache.getHitRate(), 0.0, "clear does not reset the hit rate");
    }

    @Test
    public void testGetValuesSuccessfully() {
        cache.put("key1", "value1");
        cache.put("key2", "value2");
        cache.put("key3", "value3");

        Collection<String> actual = cache.values();
        List<String> expected = List.of("value1", "value2", "value3");

        assertIterableEquals(expected, actual);
    }
}
