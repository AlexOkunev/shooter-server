package ru.otus.courses.java.advanced.shooter.server.gateway.admin.service.equipment;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import ru.otus.courses.java.advanced.shooter.server.common.protobuf.PaginationRequest;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.grenade.*;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.grpc.client.equipment.GrenadeGrpcClient;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.grpc.client.equipment.GrenadeGrpcClientRateLimitingWrapper;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.util.GrpcSchedulers;

import java.util.Collection;

@Slf4j
@Service
public class GrenadeServiceImpl implements GrenadeService {

    private final GrenadeGrpcClient grenadeGrpcClient;
    private final Scheduler schedulerEquipment;

    public GrenadeServiceImpl(
            @Qualifier(GrenadeGrpcClientRateLimitingWrapper.NAME) GrenadeGrpcClient grenadeGrpcClient,
            @Qualifier(GrpcSchedulers.EQUIPMENT) Scheduler schedulerEquipment
    ) {
        this.grenadeGrpcClient = grenadeGrpcClient;
        this.schedulerEquipment = schedulerEquipment;
    }

    @Override
    public Mono<GrenadeInfo> getOne(int id) {
        GetEnabledGrenadeRequest request = GetEnabledGrenadeRequest.newBuilder()
                .setGrenadeId(id)
                .build();

        return Mono.fromCallable(() -> grenadeGrpcClient.getEnabledGrenade(request))
                .subscribeOn(schedulerEquipment)
                .doOnSubscribe(s -> log.info("gRPC getEnabledGrenade start grenadeId={}", id))
                .doOnSuccess(resp -> log.info("gRPC getEnabledGrenade success grenadeId={}", id))
                .doOnError(e -> log.error("gRPC getEnabledGrenade error grenadeId={} err={}", id, e.getMessage(), e));
    }

    @Override
    public Mono<GrenadeInfoListPage> search(GrenadesFilter requestFilter, PaginationRequest paginationRequest) {
        GetGrenadesRequest request = GetGrenadesRequest.newBuilder()
                .setFilter(requestFilter)
                .setPaginationRequest(paginationRequest)
                .build();

        return Mono.fromCallable(() -> grenadeGrpcClient.getGrenades(request))
                .subscribeOn(schedulerEquipment)
                .doOnSubscribe(s -> log.info("gRPC getGrenades start"))
                .doOnSuccess(resp -> log.info("gRPC getGrenades success items={}",
                        resp != null ? resp.getDataCount() : 0))
                .doOnError(e -> log.error("gRPC getGrenades error err={}", e.getMessage(), e));
    }

    @Override
    public Mono<GrenadeInfoListPage> fetchByIds(Collection<Integer> ids) {
        if (ids.isEmpty()) {
            return Mono.just(GrenadeInfoListPage.newBuilder().build());
        }

        GetGrenadesRequest request = GetGrenadesRequest.newBuilder()
                .setFilter(GrenadesFilter.newBuilder()
                        .addAllGrenadeIds(ids)
                        .build()
                )
                .setPaginationRequest(PaginationRequest.newBuilder()
                        .setPage(0)
                        .setCount(ids.size())
                        .build())
                .build();

        return Mono.fromCallable(() -> grenadeGrpcClient.getGrenades(request))
                .subscribeOn(schedulerEquipment)
                .doOnSubscribe(s -> log.info("gRPC getGrenades start by ids={}", ids))
                .doOnSuccess(resp -> log.info("gRPC getGrenades success by ids={}", ids))
                .doOnError(e -> log.error("gRPC getGrenades error by ids={}: err={}", ids, e.getMessage(), e));
    }

    @Override
    public Mono<GrenadeInfo> create(GrenadeWritableData data) {
        CreateGrenadeRequest request = CreateGrenadeRequest.newBuilder()
                .setData(data)
                .build();

        return Mono.fromCallable(() -> grenadeGrpcClient.createGrenade(request))
                .subscribeOn(schedulerEquipment)
                .doOnSubscribe(s -> log.info("gRPC createGrenade start"))
                .doOnSuccess(resp -> log.info("gRPC createGrenade success id={}", resp.getId()))
                .doOnError(e -> log.error("gRPC createGrenade error err={}", e.getMessage(), e));
    }

    @Override
    public Mono<GrenadeInfo> update(int id, GrenadeWritableData data) {
        UpdateGrenadeRequest request = UpdateGrenadeRequest.newBuilder()
                .setGrenadeId(id)
                .setData(data)
                .build();

        return Mono.fromCallable(() -> grenadeGrpcClient.updateGrenade(request))
                .subscribeOn(schedulerEquipment)
                .doOnSubscribe(s -> log.info("gRPC updateGrenade start"))
                .doOnSuccess(resp -> log.info("gRPC updateGrenade success id={}", resp.getId()))
                .doOnError(e -> log.error("gRPC updateGrenade error err={}", e.getMessage(), e));
    }
}