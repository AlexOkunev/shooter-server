package ru.otus.courses.java.advanced.shooter.server.gateway.admin.service.inventory;

import com.google.protobuf.Empty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import ru.otus.courses.java.advanced.shooter.server.common.protobuf.PaginationRequest;
import ru.otus.courses.java.advanced.shooter.server.equipment.contract.EquipmentType;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.grpc.client.inventory.PlayerInventoryGrpcClient;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.grpc.client.inventory.PlayerInventoryGrpcClientRateLimitingWrapper;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.util.GrpcSchedulers;
import ru.otus.courses.java.advanced.shooter.server.inventory.protobuf.inventory.GetPlayerInventoryRequest;
import ru.otus.courses.java.advanced.shooter.server.inventory.protobuf.inventory.InitializePlayerInventoryRequest;
import ru.otus.courses.java.advanced.shooter.server.inventory.protobuf.inventory.PlayerEquipmentOperationRequest;
import ru.otus.courses.java.advanced.shooter.server.inventory.protobuf.inventory.PlayerInventoryItemsPage;

@Slf4j
@Service
public class PlayerInventoryServiceImpl implements PlayerInventoryService {

    private final PlayerInventoryGrpcClient playerInventoryGrpcClient;
    public final Scheduler schedulerInventory;

    public PlayerInventoryServiceImpl(
            @Qualifier(PlayerInventoryGrpcClientRateLimitingWrapper.NAME) PlayerInventoryGrpcClient playerInventoryGrpcClient,
            @Qualifier(GrpcSchedulers.INVENTORY) Scheduler schedulerInventory
    ) {
        this.playerInventoryGrpcClient = playerInventoryGrpcClient;
        this.schedulerInventory = schedulerInventory;
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
                .subscribeOn(schedulerInventory)
                .doOnSubscribe(s -> log.info("gRPC getPlayerInventory start playerUuid={}", playerUuid))
                .doOnSuccess(resp -> log.info("gRPC getPlayerInventory success playerUuid={}", playerUuid))
                .doOnError(e -> log.error("gRPC getPlayerInventory error playerUuid={} err={}", playerUuid, e.getMessage(), e));
    }

    @Override
    public Mono<Empty> giveEquipment(String playerUuid, EquipmentType equipmentType, int equipmentId, int amount) {
        PlayerEquipmentOperationRequest request = PlayerEquipmentOperationRequest.newBuilder()
                .setPlayerUuid(playerUuid)
                .setEquipmentType(equipmentType)
                .setEquipmentId(equipmentId)
                .setAmount(amount)
                .build();

        return Mono.fromCallable(() -> playerInventoryGrpcClient.giveEquipment(request))
                .subscribeOn(schedulerInventory)
                .doOnSubscribe(s -> log.info("gRPC giveEquipment start playerUuid={} equipmentType={} equipmentId={} amount={}", playerUuid, equipmentType, equipmentId, amount))
                .doOnSuccess(resp -> log.info("gRPC giveEquipment success playerUuid={} equipmentType={} equipmentId={} amount={}", playerUuid, equipmentType, equipmentId, amount))
                .doOnError(e -> log.error("gRPC giveEquipment error playerUuid={} equipmentType={} equipmentId={} amount={} err={}", playerUuid, equipmentType, equipmentId, amount, e.getMessage(), e));
    }

    @Override
    public Mono<Empty> takeAwayEquipment(String playerUuid, EquipmentType equipmentType, int equipmentId, int amount) {
        PlayerEquipmentOperationRequest request = PlayerEquipmentOperationRequest.newBuilder()
                .setPlayerUuid(playerUuid)
                .setEquipmentType(equipmentType)
                .setEquipmentId(equipmentId)
                .setAmount(amount)
                .build();

        return Mono.fromCallable(() -> playerInventoryGrpcClient.takeAwayEquipment(request))
                .subscribeOn(schedulerInventory)
                .doOnSubscribe(s -> log.info("gRPC takeAwayEquipment start playerUuid={} equipmentType={} equipmentId={} amount={}", playerUuid, equipmentType, equipmentId, amount))
                .doOnSuccess(resp -> log.info("gRPC takeAwayEquipment success playerUuid={} equipmentType={} equipmentId={} amount={}", playerUuid, equipmentType, equipmentId, amount))
                .doOnError(e -> log.error("gRPC takeAwayEquipment error playerUuid={} equipmentType={} equipmentId={} amount={} err={}", playerUuid, equipmentType, equipmentId, amount, e.getMessage(), e));
    }

    @Override
    public Mono<Empty> initializeInventory(String playerUuid) {
        InitializePlayerInventoryRequest request = InitializePlayerInventoryRequest.newBuilder()
                .setPlayerUuid(playerUuid)
                .build();

        return Mono.fromCallable(() -> playerInventoryGrpcClient.initializePlayerInventory(request))
                .subscribeOn(schedulerInventory)
                .doOnSubscribe(s -> log.info("gRPC initializeInventory start playerUuid={}", playerUuid))
                .doOnSuccess(resp -> log.info("gRPC initializeInventory success playerUuid={}", playerUuid))
                .doOnError(e -> log.error("gRPC initializeInventory error playerUuid={} err={}", playerUuid, e.getMessage(), e));
    }
}