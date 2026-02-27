package ru.otus.courses.java.advanced.shooter.server.gateway.admin.service.market;

import com.google.protobuf.Empty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import ru.otus.courses.java.advanced.shooter.server.common.protobuf.PaginationRequest;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.grpc.client.market.PlayerAccountGrpcClient;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.grpc.client.market.PlayerAccountGrpcClientRateLimitingWrapper;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.util.GrpcSchedulers;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.account.*;

@Slf4j
@Service
public class PlayerAccountServiceImpl implements PlayerAccountService {

    private final PlayerAccountGrpcClient playerAccountGrpcClient;
    public final Scheduler schedulerMarket;

    public PlayerAccountServiceImpl(
            @Qualifier(PlayerAccountGrpcClientRateLimitingWrapper.NAME) PlayerAccountGrpcClient playerAccountGrpcClient,
            @Qualifier(GrpcSchedulers.MARKET) Scheduler schedulerMarket
    ) {
        this.playerAccountGrpcClient = playerAccountGrpcClient;
        this.schedulerMarket = schedulerMarket;
    }

    @Override
    public Mono<PlayerAccountItemsPage> getPlayerAccountItemsPage(String playerUuid, PaginationRequest paginationRequest) {
        GetPlayerAccountRequest request = GetPlayerAccountRequest.newBuilder()
                .setPlayerUuid(playerUuid)
                .setPaginationRequest(paginationRequest)
                .setOnlyEnabledCurrencies(true)
                .build();

        return Mono.fromCallable(() -> playerAccountGrpcClient.getPlayerAccount(request))
                .subscribeOn(schedulerMarket)
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

        return Mono.fromCallable(() -> playerAccountGrpcClient.takeAwayCurrency(request))
                .subscribeOn(schedulerMarket)
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

        return Mono.fromCallable(() -> playerAccountGrpcClient.giveCurrency(request))
                .subscribeOn(schedulerMarket)
                .doOnSubscribe(s -> log.info("gRPC giveCurrency start playerUuid={} currencyId={} amount={}", playerUuid, currencyId, amount))
                .doOnSuccess(resp -> log.info("gRPC giveCurrency success playerUuid={} currencyId={} amount={}", playerUuid, currencyId, amount))
                .doOnError(e -> log.error("gRPC giveCurrency error playerUuid={} currencyId={} amount={} err={}", playerUuid, currencyId, amount, e.getMessage(), e));
    }

    @Override
    public Mono<Empty> initializeAccount(String playerUuid) {
        InitializePlayerAccountRequest request = InitializePlayerAccountRequest.newBuilder()
                .setPlayerUuid(playerUuid)
                .build();

        return Mono.fromCallable(() -> playerAccountGrpcClient.initializePlayerAccount(request))
                .subscribeOn(schedulerMarket)
                .doOnSubscribe(s -> log.info("gRPC initializePlayerAccount start playerUuid={}", playerUuid))
                .doOnSuccess(resp -> log.info("gRPC initializePlayerAccount success playerUuid={}", playerUuid))
                .doOnError(e -> log.error("gRPC initializePlayerAccount error playerUuid={} err={}", playerUuid, e.getMessage(), e));
    }
}