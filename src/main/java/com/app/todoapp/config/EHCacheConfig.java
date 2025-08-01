package com.app.todoapp.config;

import net.sf.ehcache.config.CacheConfiguration;

import java.util.Collection;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.interceptor.SimpleKeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by vanbosb on 26/11/14.
 */
@Configuration
@EnableCaching
public class EHCacheConfig extends CachingConfigurerSupport {

    @Bean //(destroyMethod = "shutdown")
    public net.sf.ehcache.CacheManager ehCacheManager() {
        CacheConfiguration userCache = new CacheConfiguration();
        userCache.setName("userCache");
        userCache.setMemoryStoreEvictionPolicy("LRU");
        userCache.setMaxEntriesLocalHeap(1000);
      
        
        CacheConfiguration apiRateCache = new CacheConfiguration();
        apiRateCache.setName("apiRateCache");
        apiRateCache.setMemoryStoreEvictionPolicy("LRU");
        apiRateCache.setMaxEntriesLocalHeap(1000);
      
        CacheConfiguration buckets = new CacheConfiguration();
        buckets.setName("apiBuckets");
        buckets.setMemoryStoreEvictionPolicy("LRU");
        buckets.setMaxEntriesLocalHeap(1000);
       
        CacheConfiguration productCache = new CacheConfiguration();
        productCache.setName("logInOut");
        productCache.setMemoryStoreEvictionPolicy("LRU");
        productCache.setMaxEntriesLocalHeap(1000);
        
        net.sf.ehcache.config.Configuration config = new net.sf.ehcache.config.Configuration();
        config.addCache(userCache);
        config.addCache(productCache);

        return net.sf.ehcache.CacheManager.newInstance(config);
    }

    @Bean
    @Override
    public CacheManager cacheManager() {
        return new EhCacheCacheManager(ehCacheManager());
    }

//    @Bean
//    @Override
//    public KeyGenerator keyGenerator() {
//        return new SimpleKeyGenerator();
//    }

}