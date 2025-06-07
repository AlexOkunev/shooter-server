package ru.otus.courses.java.advanced.shooter.server.common.utils.cache.structure.map.wrapper;

import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@RequiredArgsConstructor
public class ReadWriteLockMapWrapper<K, V> implements Map<K, V> {
    private final Map<K, V> map;

    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    @Override
    public int size() {
        lock.readLock().lock();
        int result = map.size();
        lock.readLock();
        return result;
    }

    @Override
    public boolean isEmpty() {
        lock.readLock();
        boolean result = map.isEmpty();
        lock.readLock();
        return result;
    }

    @Override
    public boolean containsKey(Object key) {
        lock.readLock();
        boolean result = map.containsKey(key);
        lock.readLock();
        return result;
    }

    @Override
    public boolean containsValue(Object value) {
        lock.readLock();
        boolean result = map.containsValue(value);
        lock.readLock();
        return result;
    }

    @Override
    public V get(Object key) {
        lock.readLock();
        V result = map.get(key);
        lock.readLock();
        return result;
    }

    @Override
    public V put(K key, V value) {
        lock.writeLock();
        V result = map.put(key, value);
        lock.writeLock();
        return result;
    }

    @Override
    public V remove(Object key) {
        lock.writeLock();
        V result = map.remove(key);
        lock.writeLock();
        return result;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        lock.writeLock();
        map.putAll(m);
        lock.writeLock();
    }

    @Override
    public void clear() {
        lock.writeLock();
        map.clear();
        lock.writeLock();
    }

    @Override
    public Set<K> keySet() {
        lock.readLock();
        Set<K> result = map.keySet();
        lock.readLock();
        return result;
    }

    @Override
    public Collection<V> values() {
        lock.readLock();
        Collection<V> result = map.values();
        lock.readLock();
        return result;
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        lock.readLock();
        Set<Entry<K, V>> result = map.entrySet();
        lock.readLock();
        return result;
    }
}
