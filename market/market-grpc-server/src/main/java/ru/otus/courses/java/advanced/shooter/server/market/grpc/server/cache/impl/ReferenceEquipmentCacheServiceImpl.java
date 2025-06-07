package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.cache.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.otus.courses.java.advanced.shooter.server.common.utils.cache.service.CacheableDataCacheServiceImplBase;
import ru.otus.courses.java.advanced.shooter.server.common.utils.cache.data.CacheableDataPage;
import ru.otus.courses.java.advanced.shooter.server.common.utils.cache.structure.impl.SoftReferenceMapCache;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.cache.base.ReferenceEquipmentCacheService;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.ReferenceEquipment;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.ReferenceEquipmentId;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.properties.ReferenceDataCachingProperties;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.repository.ReferenceEquipmentRepository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class ReferenceEquipmentCacheServiceImpl extends CacheableDataCacheServiceImplBase<ReferenceEquipmentId, ReferenceEquipment> implements ReferenceEquipmentCacheService {

    private final ReferenceEquipmentRepository referenceEquipmentRepository;

    public ReferenceEquipmentCacheServiceImpl(ReferenceEquipmentRepository referenceEquipmentRepository,
                                              ReferenceDataCachingProperties referenceDataCachingProperties) {
        super(new SoftReferenceMapCache<>(new ConcurrentHashMap<>()), referenceDataCachingProperties.getDataPageSize());
        this.referenceEquipmentRepository = referenceEquipmentRepository;
    }

    @Override
    protected Optional<ReferenceEquipment> loadReferenceDataById(ReferenceEquipmentId id) {
        log.debug("Load equipment with id {}", id);
        return referenceEquipmentRepository.findById(id);
    }

    @Override
    protected CacheableDataPage<ReferenceEquipment> loadReferenceDataPage(int page, int size) {
        log.debug("Load equipment page {} size {}", page, size);
        Page<ReferenceEquipment> dataPage = referenceEquipmentRepository.findAll(PageRequest.of(page, size));
        return new CacheableDataPage<>(dataPage.getContent(), dataPage.getNumber(), dataPage.getTotalPages());
    }
}
