package ru.otus.courses.java.advanced.shooter.server.gateway.player.grpc.client.inventory;

import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.grpc.client.RateLimitingGrpcCallExecutor;
import ru.otus.courses.java.advanced.shooter.server.inventory.protobuf.inventory.log.GetPlayerInventoryLogRequest;
import ru.otus.courses.java.advanced.shooter.server.inventory.protobuf.inventory.log.PlayerInventoryLogPage;

import java.util.List;

@Slf4j
@Component(PlayerInventoryLogGrpcClientRateLimitingWrapper.NAME)
public class PlayerInventoryLogGrpcClientRateLimitingWrapper implements PlayerInventoryLogGrpcClient {

    public static final String NAME = "playerInventoryLogGrpcClientRateLimitingWrapper";

    private final PlayerInventoryLogGrpcClient playerInventoryLogGrpcClient;
    private final RateLimitingGrpcCallExecutor rateLimitingGrpcCallExecutor;
    private final List<RateLimiter> rateLimiters;

    public PlayerInventoryLogGrpcClientRateLimitingWrapper(
            @Qualifier(PlayerInventoryLogGrpcClientBaseImpl.NAME) PlayerInventoryLogGrpcClient playerInventoryLogGrpcClient,
            RateLimitingGrpcCallExecutor rateLimitingGrpcCallExecutor,
            RateLimiterRegistry rateLimiterRegistry
    ) {
        this.playerInventoryLogGrpcClient = playerInventoryLogGrpcClient;
        this.rateLimitingGrpcCallExecutor = rateLimitingGrpcCallExecutor;
        this.rateLimiters = List.of(
                rateLimiterRegistry.rateLimiter("grpc_inventory_rps"),
                rateLimiterRegistry.rateLimiter("grpc_inventory_rpm")
        );
    }

    @Override
    public PlayerInventoryLogPage getPlayerInventoryLog(GetPlayerInventoryLogRequest request) {
        return rateLimitingGrpcCallExecutor.execute(
                () -> playerInventoryLogGrpcClient.getPlayerInventoryLog(request),
                rateLimiters
        );
    }
}
