package ru.otus.courses.java.advanced.shooter.server.gateway.admin.grpc.client.market;

import com.google.protobuf.Empty;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.grpc.client.RateLimitingGrpcCallExecutor;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.initial.GetInitialPlayerAccountRequest;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.initial.InitialPlayerAccountItemsPage;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.initial.UpdateInitialPlayerAccountRequest;

import java.util.List;

@Component(InitialPlayerAccountGrpcClientRateLimitingWrapper.NAME)
public class InitialPlayerAccountGrpcClientRateLimitingWrapper implements InitialPlayerAccountGrpcClient {

    public static final String NAME = "initialPlayerAccountGrpcClientRateLimitingWrapper";

    private final InitialPlayerAccountGrpcClient initialPlayerAccountGrpcClient;
    private final RateLimitingGrpcCallExecutor rateLimitingGrpcCallExecutor;
    private final List<RateLimiter> rateLimiters;

    public InitialPlayerAccountGrpcClientRateLimitingWrapper(
            @Qualifier(InitialPlayerAccountGrpcClientBaseImpl.NAME) InitialPlayerAccountGrpcClient initialPlayerAccountGrpcClient,
            RateLimitingGrpcCallExecutor rateLimitingGrpcCallExecutor,
            RateLimiterRegistry rateLimiterRegistry
    ) {
        this.initialPlayerAccountGrpcClient = initialPlayerAccountGrpcClient;
        this.rateLimitingGrpcCallExecutor = rateLimitingGrpcCallExecutor;
        this.rateLimiters = List.of(
                rateLimiterRegistry.rateLimiter("grpc_market_rps"),
                rateLimiterRegistry.rateLimiter("grpc_market_rpm")
        );
    }

    @Override
    public InitialPlayerAccountItemsPage getInitialPlayerAccount(GetInitialPlayerAccountRequest request) {
        return rateLimitingGrpcCallExecutor.execute(
                () -> initialPlayerAccountGrpcClient.getInitialPlayerAccount(request),
                rateLimiters
        );
    }

    @Override
    public Empty updateInitialPlayerAccount(UpdateInitialPlayerAccountRequest request) {
        return rateLimitingGrpcCallExecutor.execute(
                () -> initialPlayerAccountGrpcClient.updateInitialPlayerAccount(request),
                rateLimiters
        );
    }
}
