package com.cometsrv.cache.providers;

import com.cometsrv.cache.CacheProvider;

import java.util.concurrent.TimeUnit;

public class MemcachedProvider implements CacheProvider {
    @Override
    public void init() {

    }

    @Override
    public void deinitialize() {

    }

    @Override
    public void put(Object identifier, Object obj) {

    }

    @Override
    public void put(Object identifer, Object obj, Long expires) {

    }

    @Override
    public void put(Object identifier, Object obj, Long expires, TimeUnit unit) {

    }

    @Override
    public Object get(Object identifier) {
        return null;
    }
}
