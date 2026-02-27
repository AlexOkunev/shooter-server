package ru.otus.courses.java.advanced.shooter.server.gateway.admin.grpc.client.inventory;

import com.google.protobuf.Empty;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.grpc.client.RateLimitingGrpcCallExecutor;
import ru.otus.courses.java.advanced.shooter.server.inventory.protobuf.inventory.GetPlayerInventoryRequest;
import ru.otus.courses.java.advanced.shooter.server.inventory.protobuf.inventory.InitializePlayerInventoryRequest;
import ru.otus.courses.java.advanced.shooter.server.inventory.protobuf.inventory.PlayerEquipmentOperationRequest;
import ru.otus.courses.java.advanced.shooter.server.inventory.protobuf.inventory.PlayerInventoryItemsPage;

import java.util.List;

@Slf4j
@Component(PlayerInventoryGrpcClientRateLimitingWrapper.NAME)
public class PlayerInventoryGrpcClientRateLimitingWrapper implements PlayerInventoryGrpcClient {

    public static final String NAME = "playerInventoryGrpcClientRateLimitingWrapper";

    private final PlayerInventoryGrpcClient playerInventoryGrpcClient;
    private final RateLimitingGrpcCallExecutor rateLimitingGrpcCallExecutor;
    private final List<RateLimiter> rateLimiters;

    public PlayerInventoryGrpcClientRateLimitingWrapper(
            @Qualifier(PlayerInventoryGrpcClientBaseImpl.NAME) PlayerInventoryGrpcClient playerInventoryGrpcClient,
            RateLimitingGrpcCallExecutor rateLimitingGrpcCallExecutor,
            RateLimiterRegistry rateLimiterRegistry
    ) {
        this.playerInventoryGrpcClient = playerInventoryGrpcClient;
        this.rateLimitingGrpcCallExecutor = rateLimitingGrpcCallExecutor;
        this.rateLimiters = List.of(
                rateLimiterRegistry.rateLimiter("grpc_inventory_rps"),
                rateLimiterRegistry.rateLimiter("grpc_inventory_rpm")
        );
    }

    @Override
    public PlayerInventoryItemsPage getPlayerInventory(GetPlayerInventoryRequest request) {
        return rateLimitingGrpcCallExecutor.execute(
                () -> playerInventoryGrpcClient.getPlayerInventory(request),
                rateLimiters
        );
    }

    @Override
    public Empty initializePlayerInventory(InitializePlayerInventoryRequest request) {
        return rateLimitingGrpcCallExecutor.execute(
                () -> playerInventoryGrpcClient.initializePlayerInventory(request),
                rateLimiters
        );
    }

    @Override
    public Empty giveEquipment(PlayerEquipmentOperationRequest request) {
        return rateLimitingGrpcCallExecutor.execute(
                () -> playerInventoryGrpcClient.giveEquipment(request),
                rateLimiters
        );
    }

    @Override
    public Empty takeAwayEquipment(PlayerEquipmentOperationRequest request) {
        return rateLimitingGrpcCallExecutor.execute(
                () -> playerInventoryGrpcClient.takeAwayEquipment(request),
                rateLimiters
        );
    }

    @Override
    public Empty spendEquipment(PlayerEquipmentOperationRequest request) {
        return rateLimitingGrpcCallExecutor.execute(
                () -> playerInventoryGrpcClient.spendEquipment(request),
                rateLimiters
        );
    }
}
