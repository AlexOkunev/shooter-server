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
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.ammunition.*;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.exception.AmmunitionNotFoundException;

import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class AmmunitionServiceImpl implements AmmunitionService {

    private final ObjectFactory<AmmunitionServiceAPIGrpc.AmmunitionServiceAPIBlockingStub> ammunitionServiceAPIBlockingStubObjectFactory;

    @Override
    public Mono<AmmunitionInfo> getOne(int id) {
        GetEnabledAmmunitionRequest request = GetEnabledAmmunitionRequest.newBuilder()
                .setAmmunitionId(id)
                .build();

        return Mono.fromCallable(() -> ammunitionServiceAPIBlockingStubObjectFactory.getObject().getEnabledAmmunition(request))
                .subscribeOn(Schedulers.boundedElastic())
                .doOnSubscribe(s -> log.info("gRPC getEnabledAmmunition start ammunitionId={}", id))
                .doOnSuccess(resp -> log.info("gRPC getEnabledAmmunition success ammunitionId={}", id))
                .doOnError(e -> log.error("gRPC getEnabledAmmunition error ammunitionId={} err={}", id, e.getMessage(), e))
                .onErrorMap(StatusRuntimeException.class,
                        e -> e.getStatus().getCode() == Status.Code.NOT_FOUND ? new AmmunitionNotFoundException(id) : e
                );
    }

    @Override
    public Mono<AmmunitionInfoListPage> search(AmmunitionFilter requestFilter, PaginationRequest paginationRequest) {
        GetAmmunitionListRequest request = GetAmmunitionListRequest.newBuilder()
                .setFilter(requestFilter)
                .setPaginationRequest(paginationRequest)
                .setCompatibleGunsInclusionMode(RelatedEntitiesInclusionMode.INCLUDE_ALL)
                .build();

        return Mono.fromCallable(() -> ammunitionServiceAPIBlockingStubObjectFactory.getObject().getAmmunitionList(request))
                .subscribeOn(Schedulers.boundedElastic())
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

        return Mono.fromCallable(() -> ammunitionServiceAPIBlockingStubObjectFactory.getObject().getAmmunitionList(request))
                .subscribeOn(Schedulers.boundedElastic())
                .doOnSubscribe(s -> log.info("gRPC getAmmunitionList start by ids={}", ids))
                .doOnSuccess(resp -> log.info("gRPC getAmmunitionList success by ids={}", ids))
                .doOnError(e -> log.error("gRPC getAmmunitionList error by ids={}: err={}", ids, e.getMessage(), e));
    }

    @Override
    public Mono<AmmunitionInfo> create(AmmunitionWritableData data) {
        CreateAmmunitionRequest request = CreateAmmunitionRequest.newBuilder()
                .setData(data)
                .build();

        return Mono.fromCallable(() -> ammunitionServiceAPIBlockingStubObjectFactory.getObject().createAmmunition(request))
                .subscribeOn(Schedulers.boundedElastic())
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

        return Mono.fromCallable(() -> ammunitionServiceAPIBlockingStubObjectFactory.getObject().updateAmmunition(request))
                .subscribeOn(Schedulers.boundedElastic())
                .doOnSubscribe(s -> log.info("gRPC updateAmmunition start"))
                .doOnSuccess(resp -> log.info("gRPC updateAmmunition success id={}", resp.getId()))
                .doOnError(e -> log.error("gRPC updateAmmunition error err={}", e.getMessage(), e));
    }
}
//TODO logging
//TODO настроить пулы потоков для отказоустойчивости и производительности