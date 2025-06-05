package com.edme.processingCenter.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class CacheConfig {

    //вручную - очень долго
//    @Bean
//    public CacheManager cacheManager() {
//        SimpleCacheManager cacheManager = new SimpleCacheManager();
//        cacheManager.setCaches(Arrays.asList(
//                new ConcurrentMapCache("allBanksCache"),
//                new ConcurrentMapCache("bankByIdCache")));
//        return cacheManager;
//    }

    //для автоматического регистрирования имен кэшей
    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager();
    }


    //c настройкой времени TTL
    //@Bean
    //public RedisCacheManager cacheManager(RedisConnectionFactory factory) {
    //    RedisCacheConfiguration cacheConfig = RedisCacheConfiguration.defaultCacheConfig()
    //        .entryTtl(Duration.ofMinutes(30)) // 30 минут кэш
    //        .disableCachingNullValues();
    //
    //    return RedisCacheManager.builder(factory)
    //        .cacheDefaults(cacheConfig)
    //        .build();
    //}
}
