package ru.otus.courses.java.advanced.shooter.server.gateway.player.service.market;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import ru.otus.courses.java.advanced.shooter.server.common.protobuf.PaginationRequest;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.money.bundle.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class MoneyBundleServiceImpl implements MoneyBundleService {

    private final ObjectFactory<MoneyBundleServiceAPIGrpc.MoneyBundleServiceAPIBlockingStub>
            moneyBundleServiceAPIBlockingStubObjectFactory;

    @Override
    public Mono<MoneyBundleInfo> fetchOne(int id) {
        GetMoneyBundleRequest request = GetMoneyBundleRequest.newBuilder()
                .setId(id)
                .build();

        return Mono.fromCallable(() ->
                        moneyBundleServiceAPIBlockingStubObjectFactory.getObject().getEnabledMoneyBundle(request))
                .subscribeOn(Schedulers.boundedElastic())
                .doOnSubscribe(s -> log.info("gRPC getEnabledMoneyBundle start id={}", id))
                .doOnSuccess(resp -> log.info("gRPC getEnabledMoneyBundle success id={}", id))
                .doOnError(e -> log.error("gRPC getEnabledMoneyBundle error id={} err={}", id, e.getMessage(), e));
    }

    @Override
    public Mono<MoneyBundleInfoListPage> fetchMoneyBundlesPage(PaginationRequest paginationRequest) {
        GetMoneyBundlesRequest request = GetMoneyBundlesRequest.newBuilder()
                .setPaginationRequest(paginationRequest)
                .setFilter(GetMoneyBundlesRequest.Filter.newBuilder()
                        .setEnabled(true)
                        .setCurrencyEnabled(true)
                        .setCurrencyCanBeBought(true)
                        .build()
                )
                .build();

        return Mono.fromCallable(() ->
                        moneyBundleServiceAPIBlockingStubObjectFactory.getObject().getMoneyBundles(request))
                .subscribeOn(Schedulers.boundedElastic())
                .doOnSubscribe(s -> log.info("gRPC getMoneyBundles start paginationRequest={}", paginationRequest))
                .doOnSuccess(resp -> log.info("gRPC getMoneyBundles success paginationRequest={}", paginationRequest))
                .doOnError(e -> log.error("gRPC getMoneyBundles error paginationRequest={} err={}", paginationRequest, e.getMessage(), e));
    }

    @Override
    public Mono<MoneyBundleInfoListPage> fetchMoneyBundlesPageByCurrencyId(int currencyId, PaginationRequest paginationRequest) {
        GetMoneyBundlesRequest request = GetMoneyBundlesRequest.newBuilder()
                .setPaginationRequest(paginationRequest)
                .setFilter(GetMoneyBundlesRequest.Filter.newBuilder()
                        .setEnabled(true)
                        .setCurrencyEnabled(true)
                        .setCurrencyCanBeBought(true)
                        .addCurrencyIds(currencyId)
                        .build()
                )
                .build();

        return Mono.fromCallable(() ->
                        moneyBundleServiceAPIBlockingStubObjectFactory.getObject().getMoneyBundles(request))
                .subscribeOn(Schedulers.boundedElastic())
                .doOnSubscribe(s -> log.info("gRPC getMoneyBundles start paginationRequest={} currencyId={}", paginationRequest, currencyId))
                .doOnSuccess(resp -> log.info("gRPC getMoneyBundles success paginationRequest={}  currencyId={}", paginationRequest, currencyId))
                .doOnError(e -> log.error("gRPC getMoneyBundles error paginationRequest={}  currencyId={} err={}", paginationRequest, currencyId, e.getMessage(), e));
    }
}
//TODO logging
//TODO настроить пулы потоков для отказоустойчивости и производительности
//TODO возврат default в случае ошибки, но ошибку логировать. тоже для отказоусточивости. подумать, мб сделать везде