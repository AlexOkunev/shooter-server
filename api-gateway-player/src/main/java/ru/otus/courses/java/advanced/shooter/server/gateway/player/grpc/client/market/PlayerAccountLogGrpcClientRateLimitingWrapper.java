package ru.otus.courses.java.advanced.shooter.server.gateway.player.grpc.client.market;

import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.grpc.client.RateLimitingGrpcCallExecutor;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.account.log.GetPlayerAccountLogRequest;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.account.log.PlayerAccountLogPage;

import java.util.List;

@Slf4j
@Component(PlayerAccountLogGrpcClientRateLimitingWrapper.NAME)
public class PlayerAccountLogGrpcClientRateLimitingWrapper implements PlayerAccountLogGrpcClient {

    public static final String NAME = "playerAccountLogGrpcClientRateLimitingWrapper";

    private final PlayerAccountLogGrpcClient playerAccountLogGrpcClient;
    private final RateLimitingGrpcCallExecutor rateLimitingGrpcCallExecutor;
    private final List<RateLimiter> rateLimiters;

    public PlayerAccountLogGrpcClientRateLimitingWrapper(
            @Qualifier(PlayerAccountLogGrpcClientBaseImpl.NAME) PlayerAccountLogGrpcClient playerAccountLogGrpcClient,
            RateLimitingGrpcCallExecutor rateLimitingGrpcCallExecutor,
            RateLimiterRegistry rateLimiterRegistry
    ) {
        this.playerAccountLogGrpcClient = playerAccountLogGrpcClient;
        this.rateLimitingGrpcCallExecutor = rateLimitingGrpcCallExecutor;
        this.rateLimiters = List.of(
                rateLimiterRegistry.rateLimiter("grpc_market_rps"),
                rateLimiterRegistry.rateLimiter("grpc_market_rpm")
        );
    }

    @Override
    public PlayerAccountLogPage getPlayerAccountLog(GetPlayerAccountLogRequest request) {
        return rateLimitingGrpcCallExecutor.execute(
                () -> playerAccountLogGrpcClient.getPlayerAccountLog(request),
                rateLimiters
        );
    }
}
