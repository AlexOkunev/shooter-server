package ru.otus.courses.java.advanced.shooter.server.gateway.player.service.market;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import ru.otus.courses.java.advanced.shooter.server.common.protobuf.PaginationRequest;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.grpc.client.market.PlayerAccountLogGrpcClient;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.grpc.client.market.PlayerAccountLogGrpcClientRateLimitingWrapper;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.util.GrpcSchedulers;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.account.log.GetPlayerAccountLogRequest;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.account.log.PlayerAccountLogPage;

@Slf4j
@Service
public class PlayerAccountLogServiceImpl implements PlayerAccountLogService {

    private final PlayerAccountLogGrpcClient playerAccountLogGrpcClient;
    private final Scheduler marketScheduler;

    public PlayerAccountLogServiceImpl(
            @Qualifier(PlayerAccountLogGrpcClientRateLimitingWrapper.NAME) PlayerAccountLogGrpcClient playerAccountLogGrpcClient,
            @Qualifier(GrpcSchedulers.MARKET) Scheduler marketScheduler
    ) {
        this.playerAccountLogGrpcClient = playerAccountLogGrpcClient;
        this.marketScheduler = marketScheduler;
    }

    @Override
    public Mono<PlayerAccountLogPage> getPlayerAccountLogPage(String playerUuid, PaginationRequest paginationRequest) {
        GetPlayerAccountLogRequest request = GetPlayerAccountLogRequest.newBuilder()
                .setPlayerUuid(playerUuid)
                .setPaginationRequest(paginationRequest)
                .build();

        return Mono.fromCallable(() -> playerAccountLogGrpcClient.getPlayerAccountLog(request))
                .subscribeOn(marketScheduler)
                .doOnSubscribe(s -> log.info("gRPC getPlayerAccountLog start playerUuid={}", playerUuid))
                .doOnSuccess(resp -> log.info("gRPC getPlayerAccountLog success playerUuid={}", playerUuid))
                .doOnError(e -> log.error("gRPC getPlayerAccountLog error playerUuid={} err={}", playerUuid, e.getMessage(), e));
    }
}