package ru.otus.courses.java.advanced.shooter.server.gateway.player.service.cache;

import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import ru.otus.courses.java.advanced.shooter.server.common.protobuf.PaginationRequest;
import ru.otus.courses.java.advanced.shooter.server.common.utils.cache.data.CacheableDataPage;
import ru.otus.courses.java.advanced.shooter.server.common.utils.cache.service.IncrementallyRefreshableCacheServiceImplBase;
import ru.otus.courses.java.advanced.shooter.server.common.utils.cache.structure.impl.SoftReferenceMapCache;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.grenade.*;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.dto.equipment.GrenadeDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.grpc.client.equipment.GrenadeGrpcClient;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.grpc.client.equipment.GrenadeGrpcClientRateLimitingWrapper;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.mapper.equipment.GrenadeMapper;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class GrenadeDtoCacheService extends IncrementallyRefreshableCacheServiceImplBase<Integer, GrenadeDto> {

    private final GrenadeGrpcClient grenadeGrpcClient;
    private final GrenadeMapper grenadeMapper;

    public GrenadeDtoCacheService(
            @Value("${caches.grenade.page-size:100}") int dataPageSize,
            @Qualifier(GrenadeGrpcClientRateLimitingWrapper.NAME) GrenadeGrpcClient grenadeGrpcClient,
            GrenadeMapper grenadeMapper
    ) {
        super(new SoftReferenceMapCache<>(new ConcurrentHashMap<>()), dataPageSize);
        this.grenadeGrpcClient = grenadeGrpcClient;
        this.grenadeMapper = grenadeMapper;
    }

    @Override
    protected CacheableDataPage<GrenadeDto> loadDataPage(int page, int size, ZonedDateTime lastRefreshTime) {
        log.info("Loading grenade data page: page={}, size={}, lastRefreshTime={}", page, size, lastRefreshTime);

        GrenadeInfoListPage grenades = grenadeGrpcClient.getGrenades(
                GetGrenadesRequest.newBuilder()
                        .setFilter(GrenadesFilter.newBuilder()
                                .setUpdatedAfter(lastRefreshTime.toInstant().toEpochMilli())
                                .build())
                        .setPaginationRequest(PaginationRequest.newBuilder()
                                .setPage(page)
                                .setCount(size)
                                .build())
                        .build()
        );

        log.info("Loaded grenade data page: page={}, size={}, total pages={}", page, grenades.getDataList().size(), grenades.getPaginationInfo().getTotalPages());

        grenades.getDataList()
                .forEach(grenadeInfo -> log.info("Loaded grenade {} {}", grenadeInfo.getId(), grenadeInfo.getName()));

        return new CacheableDataPage<>(
                grenadeMapper.toDtoList(grenades.getDataList()),
                grenades.getPaginationInfo().getCurrentPageNumber(),
                grenades.getPaginationInfo().getTotalPages()
        );
    }

    @Override
    protected Optional<GrenadeDto> produceDataById(Integer integer) {
        try {
            log.info("Loading grenade by ID: {}", integer);

            GrenadeInfo grenadeInfo = grenadeGrpcClient.getGrenade(
                    GetGrenadeRequest.newBuilder()
                            .setGrenadeId(integer)
                            .build()
            );

            log.info("Loaded grenade by ID: {}", integer);

            return Optional.of(grenadeMapper.toDto(grenadeInfo));
        } catch (StatusRuntimeException e) {
            if (e.getStatus().getCode() == Status.Code.NOT_FOUND) {
                log.info("Grenade not found by ID: {}", integer);
                return Optional.empty();
            }

            throw e;
        }
    }

    @Override
    protected List<GrenadeDto> produceDataByIds(Collection<Integer> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return List.of();
        }

        log.info("Loading grenades by IDs: {}", ids);

        GrenadeInfoListPage grenades = grenadeGrpcClient.getGrenades(
                GetGrenadesRequest.newBuilder()
                        .setFilter(GrenadesFilter.newBuilder()
                                .addAllGrenadeIds(ids)
                                .build())
                        .setPaginationRequest(PaginationRequest.newBuilder()
                                .setPage(0)
                                .setCount(ids.size())
                                .build())
                        .build()
        );

        log.info("Loaded grenades by IDs: {}", ids);

        return grenadeMapper.toDtoList(grenades.getDataList());
    }
}
