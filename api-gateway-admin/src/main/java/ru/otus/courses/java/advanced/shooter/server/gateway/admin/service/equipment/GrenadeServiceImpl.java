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
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.grenade.*;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.exception.GrenadeNotFoundException;

import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class GrenadeServiceImpl implements GrenadeService {

    private final ObjectFactory<GrenadeServiceAPIGrpc.GrenadeServiceAPIBlockingStub> grenadeServiceAPIBlockingStubObjectFactory;

    @Override
    public Mono<GrenadeInfo> getOne(int id) {
        GetEnabledGrenadeRequest request = GetEnabledGrenadeRequest.newBuilder()
                .setGrenadeId(id)
                .build();

        return Mono.fromCallable(() -> grenadeServiceAPIBlockingStubObjectFactory.getObject().getEnabledGrenade(request))
                .subscribeOn(Schedulers.boundedElastic())
                .doOnSubscribe(s -> log.info("gRPC getEnabledGrenade start grenadeId={}", id))
                .doOnSuccess(resp -> log.info("gRPC getEnabledGrenade success grenadeId={}", id))
                .doOnError(e -> log.error("gRPC getEnabledGrenade error grenadeId={} err={}", id, e.getMessage(), e))
                .onErrorMap(StatusRuntimeException.class,
                        e -> e.getStatus().getCode() == Status.Code.NOT_FOUND ? new GrenadeNotFoundException(id) : e
                );
    }

    @Override
    public Mono<GrenadeInfoListPage> search(GrenadesFilter requestFilter, PaginationRequest paginationRequest) {
        GetGrenadesRequest request = GetGrenadesRequest.newBuilder()
                .setFilter(requestFilter)
                .setPaginationRequest(paginationRequest)
                .build();

        return Mono.fromCallable(() -> grenadeServiceAPIBlockingStubObjectFactory.getObject().getGrenades(request))
                .subscribeOn(Schedulers.boundedElastic())
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

        return Mono.fromCallable(() -> grenadeServiceAPIBlockingStubObjectFactory.getObject().getGrenades(request))
                .subscribeOn(Schedulers.boundedElastic())
                .doOnSubscribe(s -> log.info("gRPC getGrenades start by ids={}", ids))
                .doOnSuccess(resp -> log.info("gRPC getGrenades success by ids={}", ids))
                .doOnError(e -> log.error("gRPC getGrenades error by ids={}: err={}", ids, e.getMessage(), e));
    }
}
//TODO logging
//TODO настроить пулы потоков для отказоустойчивости и производительности