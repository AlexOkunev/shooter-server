package ru.otus.courses.java.advanced.shooter.server.gateway.admin.service.player;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import ru.otus.courses.java.advanced.shooter.server.common.protobuf.PaginationRequest;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.grpc.client.player.PlayerGrpcClient;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.grpc.client.player.PlayerGrpcClientRateLimitingWrapper;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.util.GrpcSchedulers;
import ru.otus.courses.java.advanced.shooter.server.players.protobuf.*;

@Slf4j
@Service
public class PlayerServiceImpl implements PlayerService {

    private final PlayerGrpcClient playerGrpcClient;
    private final Scheduler playerScheduler;

    public PlayerServiceImpl(
            @Qualifier(PlayerGrpcClientRateLimitingWrapper.NAME) PlayerGrpcClient playerGrpcClient,
            @Qualifier(GrpcSchedulers.PLAYER) Scheduler playerScheduler
    ) {
        this.playerGrpcClient = playerGrpcClient;
        this.playerScheduler = playerScheduler;
    }

    @Override
    public Mono<PlayerInfo> getPlayerInfo(String uuid) {
        GetPlayerRequest request = GetPlayerRequest.newBuilder()
                .setPlayerUuid(uuid)
                .build();

        return Mono.fromCallable(() -> playerGrpcClient.getPlayer(request))
                .subscribeOn(playerScheduler)
                .doOnSubscribe(s -> log.info("gRPC getPlayer start uuid={}", uuid))
                .doOnSuccess(resp -> log.info("gRPC getPlayer success uuid={}", uuid))
                .doOnError(e -> log.error("gRPC getPlayer error uuid={} err={}",
                        uuid, e.getMessage(), e));
    }

    @Override
    public Mono<PlayerInfoListPage> searchPlayers(PlayersFilter requestFilter, PaginationRequest paginationRequest) {
        GetPlayersRequest request = GetPlayersRequest.newBuilder()
                .setFilter(requestFilter)
                .setPaginationRequest(paginationRequest)
                .build();

        return Mono.fromCallable(() -> playerGrpcClient.getPlayers(request))
                .subscribeOn(playerScheduler)
                .doOnSubscribe(s -> log.info("gRPC getPlayers start"))
                .doOnSuccess(resp -> log.info("gRPC getPlayers success items={}", resp != null ? resp.getDataCount() : 0))
                .doOnError(e -> log.error("gRPC getPlayers error: {}", e.getMessage(), e));
    }
}