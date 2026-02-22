package ru.otus.courses.java.advanced.shooter.server.gateway.admin.service.market;

import com.google.protobuf.Empty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import ru.otus.courses.java.advanced.shooter.server.common.protobuf.PaginationRequest;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.account.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlayerAccountServiceImpl implements PlayerAccountService {

    private final ObjectFactory<PlayerAccountServiceAPIGrpc.PlayerAccountServiceAPIBlockingStub>
            playerAccountServiceAPIBlockingStubObjectFactory;

    @Override
    public Mono<PlayerAccountItemsPage> getPlayerAccountItemsPage(String playerUuid, PaginationRequest paginationRequest) {
        GetPlayerAccountRequest request = GetPlayerAccountRequest.newBuilder()
                .setPlayerUuid(playerUuid)
                .setPaginationRequest(paginationRequest)
                .setOnlyEnabledCurrencies(true)
                .build();

        return Mono.fromCallable(() ->
                        playerAccountServiceAPIBlockingStubObjectFactory.getObject().getPlayerAccount(request))
                .subscribeOn(Schedulers.boundedElastic())
                .doOnSubscribe(s -> log.info("gRPC getPlayerAccount start playerUuid={}", playerUuid))
                .doOnSuccess(resp -> log.info("gRPC getPlayerAccount success playerUuid={}", playerUuid))
                .doOnError(e -> log.error("gRPC getPlayerAccount error playerUuid={} err={}", playerUuid, e.getMessage(), e));
    }

    @Override
    public Mono<PlayerAccountItemInfo> takeAwayCurrency(String playerUuid, int currencyId, int amount) {
        PlayerCurrencyOperationRequest request = PlayerCurrencyOperationRequest.newBuilder()
                .setPlayerUuid(playerUuid)
                .setCurrencyId(currencyId)
                .setAmount(amount)
                .build();

        return Mono.fromCallable(() ->
                        playerAccountServiceAPIBlockingStubObjectFactory.getObject().takeAwayCurrency(request))
                .subscribeOn(Schedulers.boundedElastic())
                .doOnSubscribe(s -> log.info("gRPC takeAwayCurrency start playerUuid={} currencyId={} amount={}", playerUuid, currencyId, amount))
                .doOnSuccess(resp -> log.info("gRPC takeAwayCurrency success playerUuid={} currencyId={} amount={}", playerUuid, currencyId, amount))
                .doOnError(e -> log.error("gRPC takeAwayCurrency error playerUuid={} currencyId={} amount={} err={}", playerUuid, currencyId, amount, e.getMessage(), e));
    }

    @Override
    public Mono<PlayerAccountItemInfo> giveCurrency(String playerUuid, int currencyId, int amount) {
        PlayerCurrencyOperationRequest request = PlayerCurrencyOperationRequest.newBuilder()
                .setPlayerUuid(playerUuid)
                .setCurrencyId(currencyId)
                .setAmount(amount)
                .build();

        return Mono.fromCallable(() ->
                        playerAccountServiceAPIBlockingStubObjectFactory.getObject().giveCurrency(request))
                .subscribeOn(Schedulers.boundedElastic())
                .doOnSubscribe(s -> log.info("gRPC takeAwayCurrency start playerUuid={} currencyId={} amount={}", playerUuid, currencyId, amount))
                .doOnSuccess(resp -> log.info("gRPC takeAwayCurrency success playerUuid={} currencyId={} amount={}", playerUuid, currencyId, amount))
                .doOnError(e -> log.error("gRPC takeAwayCurrency error playerUuid={} currencyId={} amount={} err={}", playerUuid, currencyId, amount, e.getMessage(), e));
    }

    @Override
    public Mono<Empty> initializeAccount(String playerUuid) {
        InitializePlayerAccountRequest request = InitializePlayerAccountRequest.newBuilder()
                .setPlayerUuid(playerUuid)
                .build();

        return Mono.fromCallable(() ->
                        playerAccountServiceAPIBlockingStubObjectFactory.getObject().initializePlayerAccount(request))
                .subscribeOn(Schedulers.boundedElastic())
                .doOnSubscribe(s -> log.info("gRPC initializePlayerAccount start playerUuid={}", playerUuid))
                .doOnSuccess(resp -> log.info("gRPC initializePlayerAccount success playerUuid={}", playerUuid))
                .doOnError(e -> log.error("gRPC initializePlayerAccount error playerUuid={} err={}", playerUuid, e.getMessage(), e));
    }
}
//TODO logging
//TODO настроить пулы потоков для отказоустойчивости и производительности
//TODO возврат default в случае ошибки, но ошибку логировать. тоже для отказоусточивости. подумать, мб сделать везде