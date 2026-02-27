package ru.otus.courses.java.advanced.shooter.server.gateway.player.service.market;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import ru.otus.courses.java.advanced.shooter.server.common.protobuf.PaginationRequest;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.grpc.client.market.PlayerAccountGrpcClient;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.grpc.client.market.PlayerAccountGrpcClientRateLimitingWrapper;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.util.GrpcSchedulers;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.account.GetPlayerAccountRequest;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.account.PlayerAccountItemsPage;

@Slf4j
@Service
public class PlayerAccountServiceImpl implements PlayerAccountService {

    private final PlayerAccountGrpcClient playerAccountGrpcClient;
    private final Scheduler marketScheduler;

    public PlayerAccountServiceImpl(
            @Qualifier(PlayerAccountGrpcClientRateLimitingWrapper.NAME) PlayerAccountGrpcClient playerAccountGrpcClient,
            @Qualifier(GrpcSchedulers.MARKET) Scheduler marketScheduler
    ) {
        this.playerAccountGrpcClient = playerAccountGrpcClient;
        this.marketScheduler = marketScheduler;
    }

    @Override
    public Mono<PlayerAccountItemsPage> getPlayerAccountItemsPage(String playerUuid, PaginationRequest paginationRequest) {
        GetPlayerAccountRequest request = GetPlayerAccountRequest.newBuilder()
                .setPlayerUuid(playerUuid)
                .setPaginationRequest(paginationRequest)
                .setOnlyEnabledCurrencies(true)
                .build();

        return Mono.fromCallable(() -> playerAccountGrpcClient.getPlayerAccount(request))
                .subscribeOn(marketScheduler)
                .doOnSubscribe(s -> log.info("gRPC getPlayerAccount start playerUuid={}", playerUuid))
                .doOnSuccess(resp -> log.info("gRPC getPlayerAccount success playerUuid={}", playerUuid))
                .doOnError(e -> log.error("gRPC getPlayerAccount error playerUuid={} err={}", playerUuid, e.getMessage(), e));
    }
}