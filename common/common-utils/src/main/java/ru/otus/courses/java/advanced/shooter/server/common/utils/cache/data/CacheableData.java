package ru.otus.courses.java.advanced.shooter.server.common.utils.cache.data;

public interface CacheableData<ID> {
    ID getId();

    boolean isEnabled();
}