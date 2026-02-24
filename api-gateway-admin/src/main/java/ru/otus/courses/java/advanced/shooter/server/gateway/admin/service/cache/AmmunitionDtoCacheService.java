package ru.otus.courses.java.advanced.shooter.server.gateway.admin.service.cache;

import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import ru.otus.courses.java.advanced.shooter.server.common.protobuf.PaginationRequest;
import ru.otus.courses.java.advanced.shooter.server.common.utils.cache.data.CacheableDataPage;
import ru.otus.courses.java.advanced.shooter.server.common.utils.cache.service.IncrementallyRefreshableCacheServiceImplBase;
import ru.otus.courses.java.advanced.shooter.server.common.utils.cache.structure.impl.SoftReferenceMapCache;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.ammunition.*;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.equipment.AmmunitionDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.mapper.equipment.AmmunitionMapper;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class AmmunitionDtoCacheService extends IncrementallyRefreshableCacheServiceImplBase<Integer, AmmunitionDto> {

    private final ObjectFactory<AmmunitionServiceAPIGrpc.AmmunitionServiceAPIBlockingStub> ammunitionServiceAPIBlockingStubObjectFactory;
    private final AmmunitionMapper ammunitionMapper;

    public AmmunitionDtoCacheService(
            @Value("${caches.ammunition.page-size:100}") int dataPageSize,
            ObjectFactory<AmmunitionServiceAPIGrpc.AmmunitionServiceAPIBlockingStub> ammunitionServiceAPIBlockingStubObjectFactory,
            AmmunitionMapper ammunitionMapper
    ) {
        super(new SoftReferenceMapCache<>(new ConcurrentHashMap<>()), dataPageSize);
        this.ammunitionServiceAPIBlockingStubObjectFactory = ammunitionServiceAPIBlockingStubObjectFactory;
        this.ammunitionMapper = ammunitionMapper;
    }

    @Override
    protected CacheableDataPage<AmmunitionDto> loadDataPage(int page, int size, ZonedDateTime lastRefreshTime) {
        log.info("Loading ammunition data page: page={}, size={}, lastRefreshTime={}", page, size, lastRefreshTime);

        AmmunitionInfoListPage ammunitionListPage = ammunitionServiceAPIBlockingStubObjectFactory.getObject().getAmmunitionList(
                GetAmmunitionListRequest.newBuilder()
                        .setFilter(AmmunitionFilter.newBuilder()
                                .setUpdatedAfter(lastRefreshTime.toInstant().toEpochMilli())
                                .build())
                        .setPaginationRequest(PaginationRequest.newBuilder()
                                .setPage(page)
                                .setCount(size)
                                .build())
                        .build()
        );

        log.info("Loaded ammunition data page: page={}, size={}, total pages={}", page, ammunitionListPage.getDataList().size(), ammunitionListPage.getPaginationInfo().getTotalPages());

        ammunitionListPage.getDataList()
                .forEach(ammunitionInfo -> log.info("Loaded ammunition {} {}", ammunitionInfo.getId(), ammunitionInfo.getName()));

        return new CacheableDataPage<>(
                ammunitionMapper.toDtoList(ammunitionListPage.getDataList()),
                ammunitionListPage.getPaginationInfo().getCurrentPageNumber(),
                ammunitionListPage.getPaginationInfo().getTotalPages()
        );
    }

    @Override
    protected Optional<AmmunitionDto> produceDataById(Integer integer) {
        try {
            log.info("Loading ammunition by ID: {}", integer);

            AmmunitionInfo ammunitionInfo = ammunitionServiceAPIBlockingStubObjectFactory.getObject().getAmmunition(
                    GetAmmunitionRequest.newBuilder()
                            .setAmmunitionId(integer)
                            .build()
            );

            log.info("Loaded ammunition by ID: {}", integer);

            return Optional.of(ammunitionMapper.toDto(ammunitionInfo));
        } catch (StatusRuntimeException e) {
            if (e.getStatus().getCode() == Status.Code.NOT_FOUND) {
                log.info("Ammunition not found by ID: {}", integer);
                return Optional.empty();
            }

            throw e;
        }
    }

    @Override
    protected List<AmmunitionDto> produceDataByIds(Collection<Integer> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return List.of();
        }

        log.info("Loading ammunition list by IDs: {}", ids);

        AmmunitionInfoListPage ammunitionListPage = ammunitionServiceAPIBlockingStubObjectFactory.getObject().getAmmunitionList(
                GetAmmunitionListRequest.newBuilder()
                        .setFilter(AmmunitionFilter.newBuilder()
                                .addAllAmmunitionIds(ids)
                                .build())
                        .setPaginationRequest(PaginationRequest.newBuilder()
                                .setPage(0)
                                .setCount(ids.size())
                                .build())
                        .build()
        );

        log.info("Loaded ammunition list by IDs: {}", ids);

        return ammunitionMapper.toDtoList(ammunitionListPage.getDataList());
    }
}
