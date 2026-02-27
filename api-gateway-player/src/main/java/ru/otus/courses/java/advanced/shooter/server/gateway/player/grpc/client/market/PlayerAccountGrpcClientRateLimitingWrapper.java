package ru.otus.courses.java.advanced.shooter.server.gateway.player.grpc.client.market;

import com.google.protobuf.Empty;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.grpc.client.RateLimitingGrpcCallExecutor;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.account.*;

import java.util.List;

@Slf4j
@Component(PlayerAccountGrpcClientRateLimitingWrapper.NAME)
public class PlayerAccountGrpcClientRateLimitingWrapper implements PlayerAccountGrpcClient {

    public static final String NAME = "playerAccountGrpcClientRateLimitingWrapper";

    private final PlayerAccountGrpcClient playerAccountGrpcClient;
    private final RateLimitingGrpcCallExecutor rateLimitingGrpcCallExecutor;
    private final List<RateLimiter> rateLimiters;

    public PlayerAccountGrpcClientRateLimitingWrapper(
            @Qualifier(PlayerAccountGrpcClientBaseImpl.NAME) PlayerAccountGrpcClient playerAccountGrpcClient,
            RateLimitingGrpcCallExecutor rateLimitingGrpcCallExecutor,
            RateLimiterRegistry rateLimiterRegistry
    ) {
        this.playerAccountGrpcClient = playerAccountGrpcClient;
        this.rateLimitingGrpcCallExecutor = rateLimitingGrpcCallExecutor;
        this.rateLimiters = List.of(
                rateLimiterRegistry.rateLimiter("grpc_market_rps"),
                rateLimiterRegistry.rateLimiter("grpc_market_rpm")
        );
    }

    @Override
    public PlayerAccountItemsPage getPlayerAccount(GetPlayerAccountRequest request) {
        return rateLimitingGrpcCallExecutor.execute(
                () -> playerAccountGrpcClient.getPlayerAccount(request),
                rateLimiters
        );
    }

    @Override
    public Empty initializePlayerAccount(InitializePlayerAccountRequest request) {
        return rateLimitingGrpcCallExecutor.execute(
                () -> playerAccountGrpcClient.initializePlayerAccount(request),
                rateLimiters
        );
    }

    @Override
    public PlayerAccountItemInfo giveCurrency(PlayerCurrencyOperationRequest request) {
        return rateLimitingGrpcCallExecutor.execute(
                () -> playerAccountGrpcClient.giveCurrency(request),
                rateLimiters
        );
    }

    @Override
    public PlayerAccountItemInfo takeAwayCurrency(PlayerCurrencyOperationRequest request) {
        return rateLimitingGrpcCallExecutor.execute(
                () -> playerAccountGrpcClient.takeAwayCurrency(request),
                rateLimiters
        );
    }
}
