package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.cache.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.otus.courses.java.advanced.shooter.server.common.utils.cache.data.CacheableDataPage;
import ru.otus.courses.java.advanced.shooter.server.common.utils.cache.service.EntirelyRefreshableCacheServiceImplBase;
import ru.otus.courses.java.advanced.shooter.server.common.utils.cache.structure.impl.SoftReferenceMapCache;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.cache.base.MoneyBundleCacheService;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.MoneyBundle;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.properties.ReferenceDataCachingProperties;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.repository.MoneyBundleRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class MoneyBundleCacheServiceImpl extends EntirelyRefreshableCacheServiceImplBase<Integer, MoneyBundle>
        implements MoneyBundleCacheService {

    private final MoneyBundleRepository moneyBundleRepository;

    public MoneyBundleCacheServiceImpl(MoneyBundleRepository moneyBundleRepository,
                                       ReferenceDataCachingProperties referenceDataCachingProperties) {
        super(
                () -> new SoftReferenceMapCache<>(new ConcurrentHashMap<>()),
                referenceDataCachingProperties.getDataPageSize()
        );
        this.moneyBundleRepository = moneyBundleRepository;
    }

    @Override
    protected Optional<MoneyBundle> produceDataById(Integer id) {
        log.debug("Load money bundle with ID {}", id);
        return moneyBundleRepository.findById(id);
    }

    @Override
    protected CacheableDataPage<MoneyBundle> loadDataPage(int page, int size) {
        log.debug("Load money bundles from page {} size {}", page, size);
        Page<MoneyBundle> dataPage = moneyBundleRepository.findAll(PageRequest.of(page, size));
        return new CacheableDataPage<>(dataPage.getContent(), dataPage.getNumber(), dataPage.getTotalPages());
    }

    @Override
    protected List<MoneyBundle> produceDataByIds(Collection<Integer> id) {
        throw new UnsupportedOperationException("Money bundle caching does not support bulk loading by IDs");
    }
}