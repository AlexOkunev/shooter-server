package ru.otus.courses.java.advanced.shooter.server.gateway.admin.service.equipment;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import ru.otus.courses.java.advanced.shooter.server.common.protobuf.PaginationRequest;
import ru.otus.courses.java.advanced.shooter.server.common.protobuf.RelatedEntitiesInclusionMode;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.ammunition.*;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.grpc.client.equipment.AmmunitionGrpcClient;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.grpc.client.equipment.AmmunitionGrpcClientRateLimitingWrapper;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.util.GrpcSchedulers;

import java.util.Collection;

@Slf4j
@Service
public class AmmunitionServiceImpl implements AmmunitionService {

    private final AmmunitionGrpcClient ammunitionGrpcClient;
    private final Scheduler schedulerEquipment;

    public AmmunitionServiceImpl(
            @Qualifier(AmmunitionGrpcClientRateLimitingWrapper.NAME) AmmunitionGrpcClient ammunitionGrpcClient,
            @Qualifier(GrpcSchedulers.EQUIPMENT) Scheduler schedulerEquipment
    ) {
        this.ammunitionGrpcClient = ammunitionGrpcClient;
        this.schedulerEquipment = schedulerEquipment;
    }

    @Override
    public Mono<AmmunitionInfo> getOne(int id) {
        GetEnabledAmmunitionRequest request = GetEnabledAmmunitionRequest.newBuilder()
                .setAmmunitionId(id)
                .build();

        return Mono.fromCallable(() -> ammunitionGrpcClient.getEnabledAmmunition(request))
                .subscribeOn(schedulerEquipment)
                .doOnSubscribe(s -> log.info("gRPC getEnabledAmmunition start ammunitionId={}", id))
                .doOnSuccess(resp -> log.info("gRPC getEnabledAmmunition success ammunitionId={}", id))
                .doOnError(e -> log.error("gRPC getEnabledAmmunition error ammunitionId={} err={}", id, e.getMessage(), e));
    }

    @Override
    public Mono<AmmunitionInfoListPage> search(AmmunitionFilter requestFilter, PaginationRequest paginationRequest) {
        GetAmmunitionListRequest request = GetAmmunitionListRequest.newBuilder()
                .setFilter(requestFilter)
                .setPaginationRequest(paginationRequest)
                .setCompatibleGunsInclusionMode(RelatedEntitiesInclusionMode.INCLUDE_ALL)
                .build();

        return Mono.fromCallable(() -> ammunitionGrpcClient.getAmmunitionList(request))
                .subscribeOn(schedulerEquipment)
                .doOnSubscribe(s -> log.info("gRPC getAmmunitionList start"))
                .doOnSuccess(resp -> log.info("gRPC getAmmunitionList success items={}",
                        resp != null ? resp.getDataCount() : 0))
                .doOnError(e -> log.error("gRPC getAmmunitionList error err={}", e.getMessage(), e));
    }

    @Override
    public Mono<AmmunitionInfoListPage> fetchByIds(Collection<Integer> ids) {
        if (ids.isEmpty()) {
            return Mono.just(AmmunitionInfoListPage.newBuilder().build());
        }

        GetAmmunitionListRequest request = GetAmmunitionListRequest.newBuilder()
                .setFilter(AmmunitionFilter.newBuilder()
                        .addAllAmmunitionIds(ids)
                        .build()
                )
                .setPaginationRequest(PaginationRequest.newBuilder()
                        .setPage(0)
                        .setCount(ids.size())
                        .build())
                .setCompatibleGunsInclusionMode(RelatedEntitiesInclusionMode.INCLUDE_ALL)
                .build();

        return Mono.fromCallable(() -> ammunitionGrpcClient.getAmmunitionList(request))
                .subscribeOn(schedulerEquipment)
                .doOnSubscribe(s -> log.info("gRPC getAmmunitionList start by ids={}", ids))
                .doOnSuccess(resp -> log.info("gRPC getAmmunitionList success by ids={}", ids))
                .doOnError(e -> log.error("gRPC getAmmunitionList error by ids={}: err={}", ids, e.getMessage(), e));
    }

    @Override
    public Mono<AmmunitionInfo> create(AmmunitionWritableData data) {
        CreateAmmunitionRequest request = CreateAmmunitionRequest.newBuilder()
                .setData(data)
                .build();

        return Mono.fromCallable(() -> ammunitionGrpcClient.createAmmunition(request))
                .subscribeOn(schedulerEquipment)
                .doOnSubscribe(s -> log.info("gRPC createAmmunition start"))
                .doOnSuccess(resp -> log.info("gRPC createAmmunition success id={}", resp.getId()))
                .doOnError(e -> log.error("gRPC createAmmunition error err={}", e.getMessage(), e));
    }

    @Override
    public Mono<AmmunitionInfo> update(int id, AmmunitionWritableData data) {
        UpdateAmmunitionRequest request = UpdateAmmunitionRequest.newBuilder()
                .setAmmunitionId(id)
                .setData(data)
                .build();

        return Mono.fromCallable(() -> ammunitionGrpcClient.updateAmmunition(request))
                .subscribeOn(schedulerEquipment)
                .doOnSubscribe(s -> log.info("gRPC updateAmmunition start"))
                .doOnSuccess(resp -> log.info("gRPC updateAmmunition success id={}", resp.getId()))
                .doOnError(e -> log.error("gRPC updateAmmunition error err={}", e.getMessage(), e));
    }
}