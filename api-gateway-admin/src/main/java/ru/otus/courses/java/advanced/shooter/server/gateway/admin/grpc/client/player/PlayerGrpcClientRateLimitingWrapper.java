package ru.otus.courses.java.advanced.shooter.server.gateway.admin.grpc.client.player;

import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.grpc.client.RateLimitingGrpcCallExecutor;
import ru.otus.courses.java.advanced.shooter.server.players.protobuf.GetPlayerRequest;
import ru.otus.courses.java.advanced.shooter.server.players.protobuf.GetPlayersRequest;
import ru.otus.courses.java.advanced.shooter.server.players.protobuf.PlayerInfo;
import ru.otus.courses.java.advanced.shooter.server.players.protobuf.PlayerInfoListPage;

import java.util.List;

@Component(PlayerGrpcClientRateLimitingWrapper.NAME)
public class PlayerGrpcClientRateLimitingWrapper implements PlayerGrpcClient {

    public static final String NAME = "playerGrpcClientRateLimitingWrapper";

    private final PlayerGrpcClient playerGrpcClient;
    private final RateLimitingGrpcCallExecutor rateLimitingGrpcCallExecutor;
    private final List<RateLimiter> rateLimiters;

    public PlayerGrpcClientRateLimitingWrapper(
            @Qualifier(PlayerGrpcClientBaseImpl.NAME) PlayerGrpcClient playerGrpcClient,
            RateLimitingGrpcCallExecutor rateLimitingGrpcCallExecutor,
            RateLimiterRegistry rateLimiterRegistry
    ) {
        this.playerGrpcClient = playerGrpcClient;
        this.rateLimitingGrpcCallExecutor = rateLimitingGrpcCallExecutor;
        this.rateLimiters = List.of(
                rateLimiterRegistry.rateLimiter("grpc_player_rps"),
                rateLimiterRegistry.rateLimiter("grpc_player_rpm")
        );
    }

    @Override
    public PlayerInfo getPlayer(GetPlayerRequest request) {
        return rateLimitingGrpcCallExecutor.execute(
                () -> playerGrpcClient.getPlayer(request),
                rateLimiters
        );
    }

    @Override
    public PlayerInfoListPage getPlayers(GetPlayersRequest request) {
        return rateLimitingGrpcCallExecutor.execute(
                () -> playerGrpcClient.getPlayers(request),
                rateLimiters
        );
    }
}
