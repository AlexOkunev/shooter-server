package ru.otus.courses.java.advanced.shooter.server.gateway.admin.grpc.client.inventory;


import com.google.protobuf.Empty;
import ru.otus.courses.java.advanced.shooter.server.inventory.protobuf.inventory.GetPlayerInventoryRequest;
import ru.otus.courses.java.advanced.shooter.server.inventory.protobuf.inventory.InitializePlayerInventoryRequest;
import ru.otus.courses.java.advanced.shooter.server.inventory.protobuf.inventory.PlayerEquipmentOperationRequest;
import ru.otus.courses.java.advanced.shooter.server.inventory.protobuf.inventory.PlayerInventoryItemsPage;

public interface PlayerInventoryGrpcClient {

    PlayerInventoryItemsPage getPlayerInventory(GetPlayerInventoryRequest request);

    Empty initializePlayerInventory(InitializePlayerInventoryRequest request);

    Empty giveEquipment(PlayerEquipmentOperationRequest request);

    Empty takeAwayEquipment(PlayerEquipmentOperationRequest request);

    Empty spendEquipment(PlayerEquipmentOperationRequest request);
}
