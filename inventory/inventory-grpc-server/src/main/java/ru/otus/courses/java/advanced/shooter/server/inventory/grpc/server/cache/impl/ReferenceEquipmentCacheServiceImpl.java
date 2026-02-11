package ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.cache.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.otus.courses.java.advanced.shooter.server.common.utils.cache.data.CacheableDataPage;
import ru.otus.courses.java.advanced.shooter.server.common.utils.cache.service.EntirelyRefreshableCacheServiceImplBase;
import ru.otus.courses.java.advanced.shooter.server.common.utils.cache.structure.impl.SoftReferenceMapCache;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.cache.base.ReferenceEquipmentCacheService;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.entity.ReferenceEquipment;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.entity.ReferenceEquipmentId;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.properties.ReferenceDataCachingProperties;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.repository.ReferenceEquipmentRepository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class ReferenceEquipmentCacheServiceImpl
        extends EntirelyRefreshableCacheServiceImplBase<ReferenceEquipmentId, ReferenceEquipment>
        implements ReferenceEquipmentCacheService {

    private final ReferenceEquipmentRepository referenceEquipmentRepository;

    public ReferenceEquipmentCacheServiceImpl(ReferenceEquipmentRepository referenceEquipmentRepository,
                                              ReferenceDataCachingProperties referenceDataCachingProperties) {
        super(
                () -> new SoftReferenceMapCache<>(new ConcurrentHashMap<>()),
                referenceDataCachingProperties.getDataPageSize()
        );
        this.referenceEquipmentRepository = referenceEquipmentRepository;
    }

    @Override
    protected Optional<ReferenceEquipment> produceDataById(ReferenceEquipmentId id) {
        log.debug("Load equipment with id {}", id);
        return referenceEquipmentRepository.findById(id);
    }

    @Override
    protected CacheableDataPage<ReferenceEquipment> loadDataPage(int page, int size) {
        log.debug("Load equipment page {} size {}", page, size);
        Page<ReferenceEquipment> dataPage = referenceEquipmentRepository.findAll(PageRequest.of(page, size));
        return new CacheableDataPage<>(dataPage.getContent(), dataPage.getNumber(), dataPage.getTotalPages());
    }
}
