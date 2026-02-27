package ru.otus.courses.java.advanced.shooter.server.gateway.player.service.inventory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import ru.otus.courses.java.advanced.shooter.server.common.protobuf.PaginationRequest;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.grpc.client.inventory.PlayerInventoryGrpcClient;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.grpc.client.inventory.PlayerInventoryGrpcClientRateLimitingWrapper;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.util.GrpcSchedulers;
import ru.otus.courses.java.advanced.shooter.server.inventory.protobuf.inventory.GetPlayerInventoryRequest;
import ru.otus.courses.java.advanced.shooter.server.inventory.protobuf.inventory.PlayerInventoryItemsPage;

@Slf4j
@Service
public class PlayerInventoryServiceImpl implements PlayerInventoryService {

    private final PlayerInventoryGrpcClient playerInventoryGrpcClient;
    private final Scheduler inventoryScheduler;

    public PlayerInventoryServiceImpl(
            @Qualifier(PlayerInventoryGrpcClientRateLimitingWrapper.NAME) PlayerInventoryGrpcClient playerInventoryGrpcClient,
            @Qualifier(GrpcSchedulers.INVENTORY) Scheduler inventoryScheduler
    ) {
        this.playerInventoryGrpcClient = playerInventoryGrpcClient;
        this.inventoryScheduler = inventoryScheduler;
    }

    @Override
    public Mono<PlayerInventoryItemsPage> getPlayerInventoryItemsPage(String playerUuid, PaginationRequest paginationRequest) {
        GetPlayerInventoryRequest request = GetPlayerInventoryRequest.newBuilder()
                .setPlayerUuid(playerUuid)
                .setFilter(GetPlayerInventoryRequest.Filter.newBuilder()
                        .setEnabled(true)
                        .build()
                )
                .setPaginationRequest(paginationRequest)
                .build();

        return Mono.fromCallable(() -> playerInventoryGrpcClient.getPlayerInventory(request))
                .subscribeOn(inventoryScheduler)
                .doOnSubscribe(s -> log.info("gRPC getPlayerInventory start playerUuid={}", playerUuid))
                .doOnSuccess(resp -> log.info("gRPC getPlayerInventory success playerUuid={}", playerUuid))
                .doOnError(e -> log.error("gRPC getPlayerInventory error playerUuid={} err={}", playerUuid, e.getMessage(), e));
    }
}