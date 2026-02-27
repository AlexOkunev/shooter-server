package ru.otus.courses.java.advanced.shooter.server.gateway.player.service.equipment;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import ru.otus.courses.java.advanced.shooter.server.common.protobuf.PaginationRequest;
import ru.otus.courses.java.advanced.shooter.server.common.protobuf.RelatedEntitiesInclusionMode;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.gun.*;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.grpc.client.equipment.GunGrpcClient;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.grpc.client.equipment.GunGrpcClientRateLimitingWrapper;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.util.GrpcSchedulers;

import java.util.Collection;

@Slf4j
@Service
public class GunServiceImpl implements GunService {

    private final GunGrpcClient gunGrpcClient;
    private final Scheduler equipmentScheduler;

    public GunServiceImpl(
            @Qualifier(GunGrpcClientRateLimitingWrapper.NAME) GunGrpcClient gunGrpcClient,
            @Qualifier(GrpcSchedulers.EQUIPMENT) Scheduler equipmentScheduler
    ) {
        this.gunGrpcClient = gunGrpcClient;
        this.equipmentScheduler = equipmentScheduler;
    }

    @Override
    public Mono<GunInfo> getOne(int id) {
        GetEnabledGunRequest request = GetEnabledGunRequest.newBuilder()
                .setGunId(id)
                .build();

        return Mono.fromCallable(() -> gunGrpcClient.getEnabledGun(request))
                .subscribeOn(equipmentScheduler)
                .doOnSubscribe(s -> log.info("gRPC getEnabledGun start gunId={}", id))
                .doOnSuccess(resp -> log.info("gRPC getEnabledGun success gunId={}", id))
                .doOnError(e -> log.error("gRPC getEnabledGun error gunId={} err={}", id, e.getMessage(), e));
    }

    @Override
    public Mono<GunInfoListPage> search(GunsFilter requestFilter, PaginationRequest paginationRequest) {
        GetGunsRequest request = GetGunsRequest.newBuilder()
                .setFilter(requestFilter)
                .setPaginationRequest(paginationRequest)
                .setRelatedEntitiesInclusionMode(RelatedEntitiesInclusionMode.INCLUDE_ONLY_ENABLED)
                .build();

        return Mono.fromCallable(() -> gunGrpcClient.getGuns(request))
                .subscribeOn(equipmentScheduler)
                .doOnSubscribe(s -> log.info("gRPC getGuns start"))
                .doOnSuccess(resp -> log.info("gRPC getGuns success items={}",
                        resp != null ? resp.getDataCount() : 0))
                .doOnError(e -> log.error("gRPC getGuns error err={}", e.getMessage(), e));
    }

    @Override
    public Mono<GunInfoListPage> fetchByIds(Collection<Integer> ids) {
        if (ids.isEmpty()) {
            return Mono.just(GunInfoListPage.newBuilder().build());
        }

        GetGunsRequest request = GetGunsRequest.newBuilder()
                .setFilter(GunsFilter.newBuilder()
                        .addAllGunIds(ids)
                        .build()
                )
                .setPaginationRequest(PaginationRequest.newBuilder()
                        .setPage(0)
                        .setCount(ids.size())
                        .build())
                .setRelatedEntitiesInclusionMode(RelatedEntitiesInclusionMode.INCLUDE_ONLY_ENABLED)
                .build();

        return Mono.fromCallable(() -> gunGrpcClient.getGuns(request))
                .subscribeOn(equipmentScheduler)
                .doOnSubscribe(s -> log.info("gRPC getGuns start by ids={}", ids))
                .doOnSuccess(resp -> log.info("gRPC getGuns success by ids={}", ids))
                .doOnError(e -> log.error("gRPC getGuns error by ids={}: err={}", ids, e.getMessage(), e));
    }
}