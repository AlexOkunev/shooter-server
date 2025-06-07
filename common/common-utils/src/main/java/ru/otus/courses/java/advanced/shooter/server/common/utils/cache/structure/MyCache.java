package ru.otus.courses.java.advanced.shooter.server.common.utils.cache.structure;

import java.util.Collection;

//TODO jmh: concurrent, readers-writers, mutex
public interface MyCache<K, V> {
    V get(K key);

    boolean containsKey(K key);

    V put(K key, V value);

    Collection<K> keys();

    void remove(K key);

    long count();

    void clear();
}
