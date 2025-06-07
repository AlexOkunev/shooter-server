package ru.otus.courses.java.advanced.shooter.server.common.utils.cache.data;


import java.util.List;

public record CacheableDataPage<T extends CacheableData<?>>(List<T> data, int pageNumber, int pagesCount) {
}
