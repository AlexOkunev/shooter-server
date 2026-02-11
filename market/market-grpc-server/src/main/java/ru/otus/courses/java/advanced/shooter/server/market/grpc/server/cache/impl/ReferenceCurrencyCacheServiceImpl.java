package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.cache.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.otus.courses.java.advanced.shooter.server.common.utils.cache.data.CacheableDataPage;
import ru.otus.courses.java.advanced.shooter.server.common.utils.cache.service.EntirelyRefreshableCacheServiceImplBase;
import ru.otus.courses.java.advanced.shooter.server.common.utils.cache.structure.impl.SoftReferenceMapCache;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.cache.base.ReferenceCurrencyCacheService;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.ReferenceCurrency;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.properties.ReferenceDataCachingProperties;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.repository.ReferenceCurrencyRepository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class ReferenceCurrencyCacheServiceImpl extends EntirelyRefreshableCacheServiceImplBase<Integer, ReferenceCurrency>
        implements ReferenceCurrencyCacheService {

    private final ReferenceCurrencyRepository referenceCurrencyRepository;

    public ReferenceCurrencyCacheServiceImpl(ReferenceCurrencyRepository referenceCurrencyRepository,
                                             ReferenceDataCachingProperties referenceDataCachingProperties) {
        super(
                () -> new SoftReferenceMapCache<>(new ConcurrentHashMap<>()),
                referenceDataCachingProperties.getDataPageSize()
        );
        this.referenceCurrencyRepository = referenceCurrencyRepository;
    }

    @Override
    protected Optional<ReferenceCurrency> produceDataById(Integer id) {
        log.debug("Load currency with ID {}", id);
        return referenceCurrencyRepository.findById(id);
    }

    @Override
    protected CacheableDataPage<ReferenceCurrency> loadDataPage(int page, int size) {
        log.debug("Load currencies from page {} size {}", page, size);
        Page<ReferenceCurrency> dataPage = referenceCurrencyRepository.findAll(PageRequest.of(page, size));
        return new CacheableDataPage<>(dataPage.getContent(), dataPage.getNumber(), dataPage.getTotalPages());
    }
}