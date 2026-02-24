package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.cache.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.otus.courses.java.advanced.shooter.server.common.utils.cache.data.CacheableDataPage;
import ru.otus.courses.java.advanced.shooter.server.common.utils.cache.service.EntirelyRefreshableCacheServiceImplBase;
import ru.otus.courses.java.advanced.shooter.server.common.utils.cache.structure.impl.SoftReferenceMapCache;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.cache.base.ProductCacheService;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.Product;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.properties.ReferenceDataCachingProperties;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.repository.ProductRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class ProductCacheServiceImpl extends EntirelyRefreshableCacheServiceImplBase<Integer, Product>
        implements ProductCacheService {

    private final ProductRepository productRepository;

    public ProductCacheServiceImpl(ProductRepository productRepository,
                                   ReferenceDataCachingProperties referenceDataCachingProperties) {
        super(
                () -> new SoftReferenceMapCache<>(new ConcurrentHashMap<>()),
                referenceDataCachingProperties.getDataPageSize()
        );
        this.productRepository = productRepository;
    }

    @Override
    protected Optional<Product> produceDataById(Integer id) {
        log.debug("Load product with ID {}", id);
        return productRepository.findById(id);
    }

    @Override
    protected CacheableDataPage<Product> loadDataPage(int page, int size) {
        log.debug("Load products from page {} size {}", page, size);
        //TODO смотреть какие будут запросы связанных сущностей, мб нужен entity graph как в equipment
        Page<Product> dataPage = productRepository.findAll(PageRequest.of(page, size));
        return new CacheableDataPage<>(dataPage.getContent(), dataPage.getNumber(), dataPage.getTotalPages());
    }

    @Override
    protected List<Product> produceDataByIds(Collection<Integer> id) {
        throw new UnsupportedOperationException("Product caching does not support bulk loading by IDs");
    }
}