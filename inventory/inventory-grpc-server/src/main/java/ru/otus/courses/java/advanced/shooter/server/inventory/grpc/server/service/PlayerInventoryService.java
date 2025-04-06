package ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.service;

import ru.otus.courses.java.advanced.shooter.server.inventory.protobuf.inventory.GetPlayerInventoryRequest;
import ru.otus.courses.java.advanced.shooter.server.inventory.protobuf.inventory.InitializePlayerInventoryRequest;
import ru.otus.courses.java.advanced.shooter.server.inventory.protobuf.inventory.PlayerEquipmentOperationRequest;
import ru.otus.courses.java.advanced.shooter.server.inventory.protobuf.inventory.PlayerInventoryItemsPage;

public interface PlayerInventoryService {
    PlayerInventoryItemsPage getPlayerInventoryItemsPage(GetPlayerInventoryRequest request);

    void initializePlayerInventory(InitializePlayerInventoryRequest request);

    void giveEquipment(PlayerEquipmentOperationRequest request);

    void takeAwayEquipment(PlayerEquipmentOperationRequest request);

    void spendEquipment(PlayerEquipmentOperationRequest request);
}
