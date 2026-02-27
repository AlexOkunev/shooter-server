package ru.otus.courses.java.advanced.shooter.server.gateway.player.service.cache;

import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import ru.otus.courses.java.advanced.shooter.server.common.protobuf.PaginationRequest;
import ru.otus.courses.java.advanced.shooter.server.common.protobuf.RelatedEntitiesInclusionMode;
import ru.otus.courses.java.advanced.shooter.server.common.utils.cache.data.CacheableDataPage;
import ru.otus.courses.java.advanced.shooter.server.common.utils.cache.service.IncrementallyRefreshableCacheServiceImplBase;
import ru.otus.courses.java.advanced.shooter.server.common.utils.cache.structure.impl.SoftReferenceMapCache;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.gun.*;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.dto.equipment.GunDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.grpc.client.equipment.GunGrpcClient;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.grpc.client.equipment.GunGrpcClientRateLimitingWrapper;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.mapper.equipment.GunMapper;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class GunDtoCacheService extends IncrementallyRefreshableCacheServiceImplBase<Integer, GunDto> {

    private final GunGrpcClient gunGrpcClient;
    private final GunMapper gunMapper;

    public GunDtoCacheService(
            @Value("${caches.gun.page-size:100}") int dataPageSize,
            @Qualifier(GunGrpcClientRateLimitingWrapper.NAME) GunGrpcClient gunGrpcClient,
            GunMapper gunMapper
    ) {
        super(new SoftReferenceMapCache<>(new ConcurrentHashMap<>()), dataPageSize);
        this.gunGrpcClient = gunGrpcClient;
        this.gunMapper = gunMapper;
    }

    @Override
    protected CacheableDataPage<GunDto> loadDataPage(int page, int size, ZonedDateTime lastRefreshTime) {
        log.info("Loading gun data page: page={}, size={}, lastRefreshTime={}", page, size, lastRefreshTime);

        GunInfoListPage guns = gunGrpcClient.getGuns(
                GetGunsRequest.newBuilder()
                        .setFilter(GunsFilter.newBuilder()
                                .setUpdatedAfter(lastRefreshTime.toInstant().toEpochMilli())
                                .build())
                        .setPaginationRequest(PaginationRequest.newBuilder()
                                .setPage(page)
                                .setCount(size)
                                .build())
                        .setRelatedEntitiesInclusionMode(RelatedEntitiesInclusionMode.INCLUDE_ONLY_ENABLED)
                        .build()
        );

        log.info("Loaded gun data page: page={}, size={}, total pages={}", page, guns.getDataList().size(), guns.getPaginationInfo().getTotalPages());

        guns.getDataList()
                .forEach(gunInfo -> log.info("Loaded gun {} {}", gunInfo.getId(), gunInfo.getName()));

        return new CacheableDataPage<>(
                gunMapper.toDtoList(guns.getDataList()),
                guns.getPaginationInfo().getCurrentPageNumber(),
                guns.getPaginationInfo().getTotalPages()
        );
    }

    @Override
    protected Optional<GunDto> produceDataById(Integer integer) {
        try {
            log.info("Loading gun by ID: {}", integer);

            GunInfo gunInfo = gunGrpcClient.getGun(
                    GetGunRequest.newBuilder()
                            .setGunId(integer)
                            .build()
            );

            log.info("Loaded gun by ID: {}", integer);

            return Optional.of(gunMapper.toDto(gunInfo));
        } catch (StatusRuntimeException e) {
            if (e.getStatus().getCode() == Status.Code.NOT_FOUND) {
                log.info("Gun not found by ID: {}", integer);
                return Optional.empty();
            }

            throw e;
        }
    }

    @Override
    protected List<GunDto> produceDataByIds(Collection<Integer> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return List.of();
        }

        log.info("Loading guns by IDs: {}", ids);

        GunInfoListPage guns = gunGrpcClient.getGuns(
                GetGunsRequest.newBuilder()
                        .setFilter(GunsFilter.newBuilder()
                                .addAllGunIds(ids)
                                .build())
                        .setPaginationRequest(PaginationRequest.newBuilder()
                                .setPage(0)
                                .setCount(ids.size())
                                .build())
                        .build()
        );

        log.info("Loaded guns by IDs: {}", ids);

        return gunMapper.toDtoList(guns.getDataList());
    }
}
