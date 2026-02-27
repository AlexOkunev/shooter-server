package ru.otus.courses.java.advanced.shooter.server.gateway.admin.grpc.client.inventory;

import com.google.protobuf.Empty;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.grpc.client.RateLimitingGrpcCallExecutor;
import ru.otus.courses.java.advanced.shooter.server.inventory.protobuf.inventory.initial.GetInitialPlayerInventoryRequest;
import ru.otus.courses.java.advanced.shooter.server.inventory.protobuf.inventory.initial.InitialPlayerInventoryItemsPage;
import ru.otus.courses.java.advanced.shooter.server.inventory.protobuf.inventory.initial.UpdateInitialPlayerInventoryRequest;

import java.util.List;

@Slf4j
@Component(InitialPlayerInventoryGrpcClientRateLimitingWrapper.NAME)
public class InitialPlayerInventoryGrpcClientRateLimitingWrapper implements InitialPlayerInventoryGrpcClient {

    public static final String NAME = "initialPlayerInventoryGrpcClientRateLimitingWrapper";

    private final InitialPlayerInventoryGrpcClient initialPlayerInventoryGrpcClient;
    private final RateLimitingGrpcCallExecutor rateLimitingGrpcCallExecutor;
    private final List<RateLimiter> rateLimiters;

    public InitialPlayerInventoryGrpcClientRateLimitingWrapper(
            @Qualifier(InitialPlayerInventoryGrpcClientBaseImpl.NAME) InitialPlayerInventoryGrpcClient initialPlayerInventoryGrpcClient,
            RateLimitingGrpcCallExecutor rateLimitingGrpcCallExecutor,
            RateLimiterRegistry rateLimiterRegistry
    ) {
        this.initialPlayerInventoryGrpcClient = initialPlayerInventoryGrpcClient;
        this.rateLimitingGrpcCallExecutor = rateLimitingGrpcCallExecutor;
        this.rateLimiters = List.of(
                rateLimiterRegistry.rateLimiter("grpc_inventory_rps"),
                rateLimiterRegistry.rateLimiter("grpc_inventory_rpm")
        );
    }

    @Override
    public InitialPlayerInventoryItemsPage getInitialPlayerInventory(GetInitialPlayerInventoryRequest request) {
        return rateLimitingGrpcCallExecutor.execute(
                () -> initialPlayerInventoryGrpcClient.getInitialPlayerInventory(request),
                rateLimiters
        );
    }

    @Override
    public Empty updateInitialPlayerInventory(UpdateInitialPlayerInventoryRequest request) {
        return rateLimitingGrpcCallExecutor.execute(
                () -> initialPlayerInventoryGrpcClient.updateInitialPlayerInventory(request),
                rateLimiters
        );
    }
}
