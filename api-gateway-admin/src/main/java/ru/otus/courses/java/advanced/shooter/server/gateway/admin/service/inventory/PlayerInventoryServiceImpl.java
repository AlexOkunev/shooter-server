package ru.otus.courses.java.advanced.shooter.server.gateway.admin.service.inventory;

import com.google.protobuf.Empty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import ru.otus.courses.java.advanced.shooter.server.common.protobuf.PaginationRequest;
import ru.otus.courses.java.advanced.shooter.server.equipment.contract.EquipmentType;
import ru.otus.courses.java.advanced.shooter.server.inventory.protobuf.inventory.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlayerInventoryServiceImpl implements PlayerInventoryService {

    private final ObjectFactory<PlayerInventoryServiceAPIGrpc.PlayerInventoryServiceAPIBlockingStub>
            playerInventoryServiceAPIBlockingStubObjectFactory;

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

        return Mono.fromCallable(() ->
                        playerInventoryServiceAPIBlockingStubObjectFactory.getObject().getPlayerInventory(request))
                .subscribeOn(Schedulers.boundedElastic())
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

        return Mono.fromCallable(() ->
                        playerInventoryServiceAPIBlockingStubObjectFactory.getObject().giveEquipment(request))
                .subscribeOn(Schedulers.boundedElastic())
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

        return Mono.fromCallable(() ->
                        playerInventoryServiceAPIBlockingStubObjectFactory.getObject().takeAwayEquipment(request))
                .subscribeOn(Schedulers.boundedElastic())
                .doOnSubscribe(s -> log.info("gRPC takeAwayEquipment start playerUuid={} equipmentType={} equipmentId={} amount={}", playerUuid, equipmentType, equipmentId, amount))
                .doOnSuccess(resp -> log.info("gRPC takeAwayEquipment success playerUuid={} equipmentType={} equipmentId={} amount={}", playerUuid, equipmentType, equipmentId, amount))
                .doOnError(e -> log.error("gRPC takeAwayEquipment error playerUuid={} equipmentType={} equipmentId={} amount={} err={}", playerUuid, equipmentType, equipmentId, amount, e.getMessage(), e));
    }

    @Override
    public Mono<Empty> initializeInventory(String playerUuid) {
        InitializePlayerInventoryRequest request = InitializePlayerInventoryRequest.newBuilder()
                .setPlayerUuid(playerUuid)
                .build();

        return Mono.fromCallable(() ->
                        playerInventoryServiceAPIBlockingStubObjectFactory.getObject().initializePlayerInventory(request))
                .subscribeOn(Schedulers.boundedElastic())
                .doOnSubscribe(s -> log.info("gRPC initializeInventory start playerUuid={}", playerUuid))
                .doOnSuccess(resp -> log.info("gRPC initializeInventory success playerUuid={}", playerUuid))
                .doOnError(e -> log.error("gRPC initializeInventory error playerUuid={} err={}", playerUuid, e.getMessage(), e));
    }
}
//TODO logging
//TODO настроить пулы потоков для отказоустойчивости и производительности
//TODO возврат default в случае ошибки, но ошибку логировать. тоже для отказоусточивости. подумать, мб сделать везде