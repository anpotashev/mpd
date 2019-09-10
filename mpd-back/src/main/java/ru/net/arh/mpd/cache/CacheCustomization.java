package ru.net.arh.mpd.cache;

import org.springframework.boot.autoconfigure.cache.CacheManagerCustomizer;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CacheCustomization implements CacheManagerCustomizer<ConcurrentMapCacheManager> {

    @Override
    public void customize(ConcurrentMapCacheManager cacheManager) {
        List<String> cacheNames = Arrays.stream(CacheNames.values()).map(s -> s.getCacheName()).collect(Collectors.toList());
        cacheManager.setCacheNames(cacheNames);
    }
}
