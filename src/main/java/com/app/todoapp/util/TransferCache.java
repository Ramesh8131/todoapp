package com.app.todoapp.util;


import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Caching(cacheable = @Cacheable(cacheNames = "cacheUser", key = "#cacheUser.id"))
public @interface TransferCache {
}