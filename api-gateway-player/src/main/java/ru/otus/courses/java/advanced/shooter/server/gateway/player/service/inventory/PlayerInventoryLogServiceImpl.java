package ru.otus.courses.java.advanced.shooter.server.gateway.player.service.inventory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import ru.otus.courses.java.advanced.shooter.server.common.protobuf.PaginationRequest;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.grpc.client.inventory.PlayerInventoryLogGrpcClient;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.grpc.client.inventory.PlayerInventoryLogGrpcClientRateLimitingWrapper;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.util.GrpcSchedulers;
import ru.otus.courses.java.advanced.shooter.server.inventory.protobuf.inventory.log.GetPlayerInventoryLogRequest;
import ru.otus.courses.java.advanced.shooter.server.inventory.protobuf.inventory.log.PlayerInventoryLogPage;

@Slf4j
@Service
public class PlayerInventoryLogServiceImpl implements PlayerInventoryLogService {

    private final PlayerInventoryLogGrpcClient playerInventoryLogGrpcClient;
    private final Scheduler inventoryScheduler;

    public PlayerInventoryLogServiceImpl(
            @Qualifier(PlayerInventoryLogGrpcClientRateLimitingWrapper.NAME) PlayerInventoryLogGrpcClient playerInventoryLogGrpcClient,
            @Qualifier(GrpcSchedulers.INVENTORY) Scheduler inventoryScheduler
    ) {
        this.playerInventoryLogGrpcClient = playerInventoryLogGrpcClient;
        this.inventoryScheduler = inventoryScheduler;
    }

    @Override
    public Mono<PlayerInventoryLogPage> getPlayerInventoryLogPage(String playerUuid, PaginationRequest paginationRequest) {
        GetPlayerInventoryLogRequest request = GetPlayerInventoryLogRequest.newBuilder()
                .setPlayerUuid(playerUuid)
                .setPaginationRequest(paginationRequest)
                .build();

        return Mono.fromCallable(() -> playerInventoryLogGrpcClient.getPlayerInventoryLog(request))
                .subscribeOn(inventoryScheduler)
                .doOnSubscribe(s -> log.info("gRPC getPlayerInventoryLog start playerUuid={}", playerUuid))
                .doOnSuccess(resp -> log.info("gRPC getPlayerInventoryLog success playerUuid={}", playerUuid))
                .doOnError(e -> log.error("gRPC getPlayerInventoryLog error playerUuid={} err={}", playerUuid, e.getMessage(), e));
    }
}