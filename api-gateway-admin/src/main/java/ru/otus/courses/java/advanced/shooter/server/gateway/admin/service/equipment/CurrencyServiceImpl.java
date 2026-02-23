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
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.currency.*;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.exception.CurrencyNotFoundException;

import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class CurrencyServiceImpl implements CurrencyService {

    private final ObjectFactory<CurrencyServiceAPIGrpc.CurrencyServiceAPIBlockingStub> currencyServiceStubObjectFactory;

    @Override
    public Mono<CurrencyInfo> getOne(int id) {
        GetEnabledCurrencyRequest request = GetEnabledCurrencyRequest.newBuilder()
                .setCurrencyId(id)
                .build();

        return Mono.fromCallable(() -> currencyServiceStubObjectFactory.getObject().getEnabledCurrency(request))
                .subscribeOn(Schedulers.boundedElastic())
                .doOnSubscribe(s -> log.info("gRPC getEnabledCurrency start currencyId={}", id))
                .doOnSuccess(resp -> log.info("gRPC getEnabledCurrency success currencyId={}", id))
                .doOnError(e -> log.error("gRPC getEnabledCurrency error currencyId={} err={}", id, e.getMessage(), e))
                .onErrorMap(StatusRuntimeException.class,
                        e -> e.getStatus().getCode() == Status.Code.NOT_FOUND ? new CurrencyNotFoundException(id) : e
                );
    }

    @Override
    public Mono<CurrencyInfoListPage> search(CurrenciesFilter requestFilter, PaginationRequest paginationRequest) {
        GetCurrenciesRequest request = GetCurrenciesRequest.newBuilder()
                .setFilter(requestFilter)
                .setPaginationRequest(paginationRequest)
                .build();

        return Mono.fromCallable(() -> currencyServiceStubObjectFactory.getObject().getCurrencies(request))
                .subscribeOn(Schedulers.boundedElastic())
                .doOnSubscribe(s -> log.info("gRPC getCurrencies start"))
                .doOnSuccess(resp -> log.info("gRPC getCurrencies success items={}",
                        resp != null ? resp.getDataCount() : 0))
                .doOnError(e -> log.error("gRPC getCurrencies error err={}", e.getMessage(), e));
    }

    @Override
    public Mono<CurrencyInfoListPage> fetchByIds(Collection<Integer> ids) {
        if (ids.isEmpty()) {
            return Mono.just(CurrencyInfoListPage.newBuilder().build());
        }

        GetCurrenciesRequest request = GetCurrenciesRequest.newBuilder()
                .setFilter(CurrenciesFilter.newBuilder()
                        .addAllCurrencyIds(ids)
                        .build()
                )
                .setPaginationRequest(PaginationRequest.newBuilder()
                        .setPage(0)
                        .setCount(ids.size())
                        .build())
                .build();

        return Mono.fromCallable(() -> currencyServiceStubObjectFactory.getObject().getCurrencies(request))
                .subscribeOn(Schedulers.boundedElastic())
                .doOnSubscribe(s -> log.info("gRPC getCurrencies start by ids={}", ids))
                .doOnSuccess(resp -> log.info("gRPC getCurrencies success by ids={}", ids))
                .doOnError(e -> log.error("gRPC getCurrencies error by ids={}: err={}", ids, e.getMessage(), e));
    }

    @Override
    public Mono<CurrencyInfo> create(CurrencyWritableData data) {
        CreateCurrencyRequest request = CreateCurrencyRequest.newBuilder()
                .setData(data)
                .build();

        return Mono.fromCallable(() -> currencyServiceStubObjectFactory.getObject().createCurrency(request))
                .subscribeOn(Schedulers.boundedElastic())
                .doOnSubscribe(s -> log.info("gRPC createCurrency start"))
                .doOnSuccess(resp -> log.info("gRPC createCurrency success id={}", resp.getId()))
                .doOnError(e -> log.error("gRPC createCurrency error err={}", e.getMessage(), e));
    }

    @Override
    public Mono<CurrencyInfo> update(int currencyId, CurrencyWritableData data) {
        UpdateCurrencyRequest request = UpdateCurrencyRequest.newBuilder()
                .setCurrencyId(currencyId)
                .setData(data)
                .build();

        return Mono.fromCallable(() -> currencyServiceStubObjectFactory.getObject().updateCurrency(request))
                .subscribeOn(Schedulers.boundedElastic())
                .doOnSubscribe(s -> log.info("gRPC updateCurrency start"))
                .doOnSuccess(resp -> log.info("gRPC updateCurrency success id={}", resp.getId()))
                .doOnError(e -> log.error("gRPC updateCurrency error err={}", e.getMessage(), e));
    }
}
//TODO logging
//TODO настроить пулы потоков для отказоустойчивости и производительности