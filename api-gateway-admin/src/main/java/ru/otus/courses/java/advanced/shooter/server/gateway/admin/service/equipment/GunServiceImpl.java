package ru.otus.courses.java.advanced.shooter.server.gateway.admin.service.equipment;

import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import ru.otus.courses.java.advanced.shooter.server.common.protobuf.PaginationRequest;
import ru.otus.courses.java.advanced.shooter.server.common.protobuf.RelatedEntitiesInclusionMode;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.ammunition.AmmunitionInfo;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.ammunition.AmmunitionWritableData;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.ammunition.CreateAmmunitionRequest;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.ammunition.UpdateAmmunitionRequest;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.gun.*;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.exception.GunNotFoundException;

import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class GunServiceImpl implements GunService {

    private final ObjectFactory<GunServiceAPIGrpc.GunServiceAPIBlockingStub> gunServiceAPIBlockingStubObjectFactory;

    @Override
    public Mono<GunInfo> getOne(int id) {
        GetEnabledGunRequest request = GetEnabledGunRequest.newBuilder()
                .setGunId(id)
                .build();

        return Mono.fromCallable(() -> gunServiceAPIBlockingStubObjectFactory.getObject().getEnabledGun(request))
                .subscribeOn(Schedulers.boundedElastic())
                .doOnSubscribe(s -> log.info("gRPC getEnabledGun start gunId={}", id))
                .doOnSuccess(resp -> log.info("gRPC getEnabledGun success gunId={}", id))
                .doOnError(e -> log.error("gRPC getEnabledGun error gunId={} err={}", id, e.getMessage(), e))
                .onErrorMap(StatusRuntimeException.class,
                        e -> e.getStatus().getCode() == Status.Code.NOT_FOUND ? new GunNotFoundException(id) : e
                );
    }

    @Override
    public Mono<GunInfoListPage> search(GunsFilter requestFilter, PaginationRequest paginationRequest) {
        GetGunsRequest request = GetGunsRequest.newBuilder()
                .setFilter(requestFilter)
                .setPaginationRequest(paginationRequest)
                .setRelatedEntitiesInclusionMode(RelatedEntitiesInclusionMode.INCLUDE_ALL)
                .build();

        return Mono.fromCallable(() -> gunServiceAPIBlockingStubObjectFactory.getObject().getGuns(request))
                .subscribeOn(Schedulers.boundedElastic())
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
                .setRelatedEntitiesInclusionMode(RelatedEntitiesInclusionMode.INCLUDE_ALL)
                .build();

        return Mono.fromCallable(() -> gunServiceAPIBlockingStubObjectFactory.getObject().getGuns(request))
                .subscribeOn(Schedulers.boundedElastic())
                .doOnSubscribe(s -> log.info("gRPC getGuns start by ids={}", ids))
                .doOnSuccess(resp -> log.info("gRPC getGuns success by ids={}", ids))
                .doOnError(e -> log.error("gRPC getGuns error by ids={}: err={}", ids, e.getMessage(), e));
    }

    @Override
    public Mono<GunInfo> create(GunWritableData data) {
        CreateGunRequest request = CreateGunRequest.newBuilder()
                .setData(data)
                .build();

        return Mono.fromCallable(() -> gunServiceAPIBlockingStubObjectFactory.getObject().createGun(request))
                .subscribeOn(Schedulers.boundedElastic())
                .doOnSubscribe(s -> log.info("gRPC createGun start"))
                .doOnSuccess(resp -> log.info("gRPC createGun success id={}", resp.getId()))
                .doOnError(e -> log.error("gRPC createGun error err={}", e.getMessage(), e));
    }

    @Override
    public Mono<GunInfo> update(int id, GunWritableData data) {
        UpdateGunRequest request = UpdateGunRequest.newBuilder()
                .setGunId(id)
                .setData(data)
                .build();

        return Mono.fromCallable(() -> gunServiceAPIBlockingStubObjectFactory.getObject().updateGun(request))
                .subscribeOn(Schedulers.boundedElastic())
                .doOnSubscribe(s -> log.info("gRPC updateGun start"))
                .doOnSuccess(resp -> log.info("gRPC updateGun success id={}", resp.getId()))
                .doOnError(e -> log.error("gRPC updateGun error err={}", e.getMessage(), e));
    }
}
//TODO logging
//TODO настроить пулы потоков для отказоустойчивости и производительности